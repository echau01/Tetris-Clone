package persistence;

import java.io.PrintWriter;

/* This interface comes from the Saveable interface in the TellerApp repository.
 * https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank
 * /persistence/Saveable.java
 */

// Represents something that can be saved to a file.
public interface Saveable {
    // MODIFIES: printWriter
    // EFFECTS: writes this Saveable object to the given printWriter.
    // NOTE: The printWriter might not be closed afterwards.
    void saveTo(PrintWriter printWriter);
}
