/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:36:44 PM
 */
public class VospaceSetMessageType extends TypedResourceSetMessageType {
    public static final VospaceSetMessageType instance = new VospaceSetMessageType();
    @Override
    public
    final String suffix() {
        return ".vospace";
    }

}
