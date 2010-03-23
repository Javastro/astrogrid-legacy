/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;


/**
 * A 'subype' of reosurce set message, for exchanging a set of cone resources.
 *  Extends the 'resource set message type', as this message has the same schema, 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 23, 201012:32:16 PM
 */
public class ConeSetMessageType extends TypedResourceSetMessageType {

    public static final ConeSetMessageType instance = new ConeSetMessageType();
    
    @Override
    public
    final String suffix() {
        return ".cone";
    }
    
}
