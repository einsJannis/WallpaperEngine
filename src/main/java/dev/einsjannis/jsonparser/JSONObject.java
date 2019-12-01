package dev.einsjannis.jsonparser;

import java.util.HashMap;
import java.util.List;

/**
 * An extension to the {@link HashMap} class. In addition to all the
 * {@link HashMap} features, this class provides the functionality to parse
 * itself from a {@code JSON string}.
 * <p>
 * As part of the {@code JSON parser} witch is contained inside of this class,
 * it implements {@link ClassWithIterator}. The ability to change the position
 * of the {@code iterator} ({@link #i}) allows an external class to skip a section in the
 * parsing process and thus allowing {@code nested data structures}.
 *
 * @author einsJannis
 * @author Flexusma
 * @version 1.0
 * @see HashMap
 * @see ClassWithIterator
 */
public class JSONObject extends HashMap<String, Object> implements ClassWithIterator {

    private int i;

    @Override
    public int getIterator() {
        return i;
    }
    @Override
    public void setIterator(int i) {
        this.i = i;
    }

    /**
     * Constructs a new {@code JSONObject} with the elements provided in the list
     * of {@link JSONPart}s.
     *
     * @param parts a list of {@link JSONPart}s which should get parsed
     * @throws JSONSyntaxError if the json is malformed
     */
    public JSONObject(List<JSONPart> parts) throws JSONSyntaxError {
        parseJSON(parts);
    }

    private void parseJSON(List<JSONPart> parts) throws JSONSyntaxError {
        if (parts.get(0).getType() != JSONPart.Type.OBJECT_START) {
            throw new JSONSyntaxError(parts.get(0));
        }
        i = 1;
        while (i < parts.size()) {
            if (parts.get(i).getType() != JSONPart.Type.STRING) {
                throw new JSONSyntaxError(parts.get(i));
            }
            i++;
            if (parts.get(i).getType() != JSONPart.Type.DOUBLE_POINT) {
                throw new JSONSyntaxError(parts.get(i));
            }
            i++;
            switch (parts.get(i).getType()) {
                case ARRAY_START:
                    this.put(parts.get(i-2).getValue(), JSONArray.scanForSelf(this, parts));
                    break;
                case OBJECT_START:
                    this.put(parts.get(i-2).getValue(), JSONObject.scanForSelf(this, parts));
                    break;
                case BOOLEAN:
                    this.put(parts.get(i-2).getValue(), parts.get(i).getValue().equals("true"));
                    break;
                case STRING:
                    this.put(parts.get(i-2).getValue(), parts.get(i).getValue());
                    break;
                case NUMBER:
                    this.put(parts.get(i-2).getValue(), Double.parseDouble(parts.get(i).getValue()));
                    break;
                case NULL:
                    this.put(parts.get(i-2).getValue(), null);
                    break;
                default:
                    throw new JSONSyntaxError(parts.get(i));
            }
            i++;
            if (parts.get(i).getType() == JSONPart.Type.OBJECT_END) {
                return;
            } else if (parts.get(i).getType() == JSONPart.Type.COMMA) {
                i++;
                continue;
            }
            throw new JSONSyntaxError(parts.get(i));
        }
    }

    /**
     * Returns {@code JSONObject} to witch the iterator of the
     * {@link ClassWithIterator} points to.
     * <p>
     * If no JSONArray can be found it throws an {@link JSONSyntaxError}.
     *
     * @param parent {@link ClassWithIterator} with an iterator which points to the object start ('{')
     * @param parts list of {@link JSONPart}s witch contain the requested JSONObject
     * @return the requested JSONObject
     * @throws JSONSyntaxError if json is malformed
     */
    public static JSONObject scanForSelf(ClassWithIterator parent, List<JSONPart> parts) throws JSONSyntaxError {
        List<JSONPart> partList = parts.subList(parent.getIterator(), parts.size());
        int openObjects = 0;
        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getType() == JSONPart.Type.OBJECT_START) {
                openObjects++;
            }
            if (partList.get(i).getType() == JSONPart.Type.OBJECT_END) {
                if (openObjects == 1) {
                    parent.setIterator(parent.getIterator() + i);
                    return new JSONObject(partList.subList(0,i+1));
                } else {
                    openObjects--;
                }
            }
        }
        throw new JSONSyntaxError(parts.get(0));
    }

    /**
     * Creates a {@code JSONObject} from a {@code JSON string}.
     *
     * @param string json string witch should get parsed
     * @return the parsed JSONObject
     * @throws JSONSyntaxError if json is malformed
     */
    public static JSONObject fromString(String string) throws JSONSyntaxError {
        return fromChars(string.toCharArray());
    }

    /**
     * Creates a {@code JSONObject} from a {@code JSON string} as
     * {@code char array}.
     *
     * @param chars json string as char array witch should get parsed
     * @return the parsed JSONObject
     * @throws JSONSyntaxError if json is malformed
     */
    public static JSONObject fromChars(char[] chars) throws JSONSyntaxError {
        return new JSONObject(new JSONLexer(chars).lex());
    }
}
