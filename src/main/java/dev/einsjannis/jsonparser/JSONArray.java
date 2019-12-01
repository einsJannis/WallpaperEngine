package dev.einsjannis.jsonparser;

import java.util.ArrayList;
import java.util.List;

/**
 * An extension to the {@link ArrayList} class. In addition to all the
 * {@link ArrayList} features, this class provides the functionality to parse
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
 * @see ArrayList
 * @see ClassWithIterator
 */
public class JSONArray extends ArrayList<Object> implements ClassWithIterator {

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
     * Constructs a new {@code JSONArray} with the elements provided in the list
     * of {@link JSONPart}s.
     *
     * @param parts a list of {@link JSONPart}s which should get parsed
     * @throws JSONSyntaxError if the json is malformed
     */
    public JSONArray(List<JSONPart> parts) throws JSONSyntaxError {
        parseJSON(parts);
    }

    private void parseJSON(List<JSONPart> parts) throws JSONSyntaxError {
        if (parts.get(0).getType() != JSONPart.Type.ARRAY_START) {
            throw new JSONSyntaxError(parts.get(0));
        }
        if (parts.get(1).getType() == JSONPart.Type.ARRAY_END) {
            return;
        }
        i = 1;
        while (i < parts.size()) {
            switch (parts.get(i).getType()) {
                case ARRAY_START:
                    this.add(JSONArray.scanForSelf(this, parts));
                    break;
                case OBJECT_START:
                    this.add(JSONObject.scanForSelf(this, parts));
                    break;
                case BOOLEAN:
                    this.add(parts.get(i).getValue().equals("true"));
                    break;
                case STRING:
                    this.add(parts.get(i).getValue());
                    break;
                case NUMBER:
                    this.add(Long.valueOf(parts.get(i).getValue()));
                    break;
                case NULL:
                    this.add(null);
                    break;
                default:
                    throw new JSONSyntaxError(parts.get(i));
            }
            i++;
            if (parts.get(i).getType() == JSONPart.Type.ARRAY_END) {
                return;
            } else if (parts.get(i).getType() == JSONPart.Type.COMMA) {
                i++;
                continue;
            }
            throw new JSONSyntaxError(parts.get(i));
        }
        throw new JSONSyntaxError(parts.get(0));
    }

    /**
     * Returns {@code JSONArray} to witch the iterator of the
     * {@link ClassWithIterator} points to.
     * <p>
     * If no JSONArray can be found it throws an {@link JSONSyntaxError}.
     *
     * @param parent {@link ClassWithIterator} which iterator points to the array start ({@code '['})
     * @param parts list of {@link JSONPart}s witch contain the requested JSONArray
     * @return the requested JSONArray
     * @throws JSONSyntaxError if json is malformed
     */
    public static JSONArray scanForSelf(ClassWithIterator parent, List<JSONPart> parts) throws JSONSyntaxError {
        List<JSONPart> partList = parts.subList(parent.getIterator(), parts.size());
        int openObjects = 0;
        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getType() == JSONPart.Type.ARRAY_START) {
                openObjects++;
            }
            if (partList.get(i).getType() == JSONPart.Type.ARRAY_END) {
                if (openObjects == 1) {
                    parent.setIterator(parent.getIterator() + i);
                    return new JSONArray(partList.subList(0, i + 1));
                } else {
                    openObjects--;
                }
            }
        }
        throw new JSONSyntaxError(parts.get(0));
    }

    /**
     * Creates a new {@code JSONArray} with a {@code JSON string}.
     *
     * @param string json string witch should get parsed
     * @return the parsed JSONArray
     * @throws JSONSyntaxError if parsed json is malformed
     */
    public static JSONArray fromString(String string) throws JSONSyntaxError {
        return fromChars(string.toCharArray());
    }

    /**
     * Creates a new {@code JSONArray} with a {@code JSON string} as a
     * {@code Character array}.
     *
     * @param chars json string as char array witch should get parsed
     * @return the parsed JSONArray
     * @throws JSONSyntaxError if parsed json is malformed
     */
    public static JSONArray fromChars(char[] chars) throws JSONSyntaxError {
        return new JSONArray(new JSONLexer(chars).lex());
    }
}
