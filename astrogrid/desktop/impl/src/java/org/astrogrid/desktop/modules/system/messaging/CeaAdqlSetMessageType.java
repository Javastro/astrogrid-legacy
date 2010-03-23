/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:38:43 PM
 */
public class CeaAdqlSetMessageType extends TypedResourceSetMessageType {
    public static final CeaAdqlSetMessageType instance = new CeaAdqlSetMessageType();
    @Override
    public
    final String suffix() {
        return ".cea-adql";
    }

}
