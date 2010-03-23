/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:42:58 PM
 */
public class StapSetMessageType extends TypedResourceSetMessageType {
    public static final StapSetMessageType instance = new StapSetMessageType();
    @Override
    protected final String suffix() {
        return ".stap";
    }

}
