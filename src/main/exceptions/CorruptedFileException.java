package exceptions;

// Thrown when trying to read a file containing Saveable data that has been altered such
// that it is no longer readable/parsable.
public class CorruptedFileException extends Exception {
    // EFFECTS: constructs a CorruptedFileException with given message
    public CorruptedFileException(String message) {
        super(message);
    }
}
