package persistence;

import java.io.Closeable;
import java.io.PrintWriter;

// This class is used for writing Saveable objects to file.
public class Writer implements Closeable {
    /* Code adapted from TellerApp's Writer:
     * https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/persistence/Writer.java
     */
    private PrintWriter printWriter;

    // EFFECTS: Creates a new Writer that writes to the given PrintWriter
    public Writer(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    // MODIFIES: this
    // EFFECTS: writes given Saveable to this.
    public void write(Saveable saveable) {
        saveable.saveTo(printWriter);
    }

    // MODIFIES: this
    // EFFECTS: closes this writer.
    public void close() {
        printWriter.close();
    }
}
