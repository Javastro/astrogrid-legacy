/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.net.URI;

import org.apache.commons.collections.Factory;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.desktop.modules.system.messaging.MessageTarget;

/** Interface to multicone UI 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 15, 20094:09:32 PM
 */
public interface MultiConeInternal extends Factory, MessageTarget {

    /** run a new instance of multicone against this service
     * (will have to handle multiple / different capabilities.. 
     * */
    public void multiCone(ConeService s) ;
    
    /** run a new instance of multicone agains thtis file
     *  (assumed to be some form of table)
     * @param f
     */
    public void multiCone(URI file) ;
    
   
}
