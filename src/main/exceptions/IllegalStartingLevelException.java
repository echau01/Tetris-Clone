package exceptions;

public class IllegalStartingLevelException extends RuntimeException {
    // The linked StackOverflow answer helped me decide whether this class (and the other classes in the
    // exceptions package) should extend Exception or RuntimeException.
    // https://stackoverflow.com/questions/27578/when-to-choose-checked-and-unchecked-exceptions/19061110#19061110
}
