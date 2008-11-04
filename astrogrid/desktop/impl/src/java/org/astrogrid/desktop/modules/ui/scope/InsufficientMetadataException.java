/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

/** Exception thrown when a service response table cannot be parsed.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 7, 200811:43:36 AM
 */
public class InsufficientMetadataException extends DalProtocolException {

    /**
     * @param message
     */
    public InsufficientMetadataException(final String message) {
        super("Insufficient metadata provided by response table: " + message);
    }

}
