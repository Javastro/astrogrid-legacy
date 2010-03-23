/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:33:21 PM
 */
public class SiapSetMessageType extends TypedResourceSetMessageType {

    public static final SiapSetMessageType instance = new SiapSetMessageType();
    
    @Override
    public
    final String suffix() {
        return ".siap";
    }

}
