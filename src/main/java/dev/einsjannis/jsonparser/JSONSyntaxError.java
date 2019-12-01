package dev.einsjannis.jsonparser;

/**
 * This class defines the {@code JSONSyntaxError} witch is thrown in the
 * occurrence of malformed {@code JSON}.
 *
 * @author einsJannis
 * @author Flexusma
 * @version 1.0
 */
public class JSONSyntaxError extends Exception {

    private char errorChar;
    private int errorPosition;
    private JSONPart errorPart;

    /**
     * Constructs a new {@code JSONSyntaxError} from a {@code Character} and
     * its {@code position}.
     *
     * @param errorPosition char position of char witch caused the error
     * @param errorChar char witch caused the error
     */
    public JSONSyntaxError(int errorPosition, char errorChar) {
        this.errorChar = errorChar;
        this.errorPosition = errorPosition;
    }

    /**
     * Constructs a new {@code JSONSyntaxError} from a {@link JSONPart}.
     *
     * @param errorPart {@link JSONPart} witch caused the error
     */
    public JSONSyntaxError(JSONPart errorPart) {
        this.errorPart = errorPart;
    }

    @Override
    public String getMessage() {
        if (errorPart != null) {
            return "Error in json at part: " + errorPart.toString();
        } else {
            return "Error in json at position " + String.valueOf(errorPosition) + " with char: '" + String.valueOf(errorChar) + "'.";
        }
    }
}
