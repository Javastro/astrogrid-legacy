/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author jdt
 */
public class ApplicationStore {
	private Map registeredApps = Collections.synchronizedMap(new HashMap()); // JDK1.5  Consider replacing this with a CopyOnWrite collection when we switch to Java 5

	public void add(PlasticClientProxy client) {
		registeredApps.put(client.getId(), client);
	}


	public PlasticClientProxy get(URI id) {
		return (PlasticClientProxy) registeredApps.get(id);
	}

	public void remove(URI id) {
		registeredApps.remove(id);
	}

	/**
	 * Return a list of the ids of applications that claim to understand a message.
	 * @param message the message in question
	 * @return a List of URI ids
	 */
	public List getClientIdsSupportingMessage(URI message) {
        synchronized (registeredApps) {
            Iterator it = registeredApps.keySet().iterator();
            List interestedClients = new ArrayList();
            //TODO can worry about caching etc if efficiency is shown to be a problem
            while (it.hasNext()) {
                PlasticClientProxy client = (PlasticClientProxy) registeredApps.get((URI) it.next());
                if (client.understands(message)) {
                    interestedClients.add(client.getId());
                }
            }
            return interestedClients;
        }
	}

    /** 
     * Returns a _copy_ of a collection holding all the IDs
     */
	public List getAppIds() {
        synchronized (registeredApps) {
            return  new ArrayList(registeredApps.keySet()); //make a copy to protect ourselves and for synch issues
        }
	}

}
