/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:37:32 PM
 */
public class CeaSetMessageType extends TypedResourceSetMessageType {
    public static final CeaSetMessageType instance = new CeaSetMessageType();
    @Override
    public
    final String suffix() {
        return ".cea";
    }

}
