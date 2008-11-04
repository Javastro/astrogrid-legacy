/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import org.apache.hivemind.service.ObjectProvider;
import org.astrogrid.acr.ivoa.CacheFactory;
import org.astrogrid.desktop.modules.system.ScheduledTask;

/**Internal interface to cache.
 * <p/>
 * A union interface, necessary for hivemind. Combines the cache factory, with Object provider (exposes cache objects in hivemind),
 * and with scheduled task (for runing periodic cache expiry)
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 7, 20082:43:49 PM
 */
public interface CacheFactoryInternal extends CacheFactory, ObjectProvider, ScheduledTask {

}
