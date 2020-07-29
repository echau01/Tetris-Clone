package exceptions;

public class CorruptedFileException extends Exception {
    // EFFECTS: constructs a CorruptedFileException with given message
    public CorruptedFileException(String message) {
        super(message);
    }
}
