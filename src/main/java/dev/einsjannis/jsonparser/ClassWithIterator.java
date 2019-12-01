package dev.einsjannis.jsonparser;

/**
 * Allows an external class to access an {@code Iterator} in the class thus
 * allowing code inside a loop to be outside of the class containing the loop.
 *
 * @author einsJannis
 * @author Flexusma
 * @version 1.0
 */
public interface ClassWithIterator {
    int getIterator();
    void setIterator(int i);
}
