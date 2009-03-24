/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.apache.commons.collections.Factory;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.system.messaging.MessageTarget;

/** internal interface to the voexplorer factory.
 *  - just a conjunction of other interfaces.
 * necessary to have all bound togehter in a single interface, so can be referred to in hivemind.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20076:47:57 PM
 */
public interface VOExplorerFactoryInternal extends  Factory,RegistryBrowser, Runnable, MessageTarget {

}
