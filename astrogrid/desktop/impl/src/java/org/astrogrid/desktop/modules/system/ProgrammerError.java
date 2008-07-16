/**
 * 
 */
package org.astrogrid.desktop.modules.system;

/** Indicates an error in the program logic - unrecoverable, and shouldn't ever occur.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 16, 200812:09:07 PM
 */
public class ProgrammerError extends Error {

    public ProgrammerError(String message, Throwable cause) {
        super(message, cause);
    }

    public ProgrammerError(String message) {
        super(message);
    }

    public ProgrammerError(Throwable cause) {
        super(cause);
    }

}
