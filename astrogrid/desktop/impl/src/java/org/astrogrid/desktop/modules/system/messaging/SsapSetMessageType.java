/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 20101:34:12 PM
 */
public class SsapSetMessageType extends TypedResourceSetMessageType {
    public static final SsapSetMessageType instance = new SsapSetMessageType();
    @Override
    public
    final String suffix() {
        return ".ssap";
    }

}
