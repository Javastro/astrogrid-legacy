/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:36:11 PM
 */
public class TapSetMessageType extends TypedResourceSetMessageType {
    public static final TapSetMessageType instance = new TapSetMessageType();
    @Override
    public
    final String suffix() {
        return ".tap";
    }

}
