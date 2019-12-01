package dev.einsjannis.jsonparser;

/**
 * This class defines a part of a {@code JSON structure}.
 * <p>
 * Every {@code JSONPart} contains a {@link #type} and an additional
 * {@link #value}.
 *
 * @author einsJannis
 * @author Flexusma
 * @version 1.0
 */
public class JSONPart {

    private final JSONPart.Type type;
    private final String value;

    /**
     * Constructs a new {@code JSONPart} with a type and an additional value
     *
     * @param type type of {@code JSONPart}
     * @param value value of {@code JSONPart} (can be {@code null})
     */
    public JSONPart(JSONPart.Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(\"" + type.name() + "\", \"" + value + "\")";
    }

    /**
     * every possible type of an {@link JSONPart}
     */
    public enum Type {
        OBJECT_START, OBJECT_END, ARRAY_START, ARRAY_END, COMMA, DOUBLE_POINT, STRING, NUMBER, BOOLEAN, NULL
    }
}
