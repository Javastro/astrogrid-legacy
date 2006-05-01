/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author jdt
 */
public class ApplicationStore {
	private Map registeredApps = new HashMap();

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
		Iterator it = registeredApps.keySet().iterator();
		List interestedClients = new Vector();
		//TODO can worry about caching etc if efficiency is shown to be a problem
		while (it.hasNext()) {
			PlasticClientProxy client = (PlasticClientProxy) registeredApps.get((URI) it.next());
			if (client.understands(message)) {
				interestedClients.add(client.getId());
			}
		}
		return interestedClients;
	}

	public List getIds() {
		return new Vector(registeredApps.keySet());
	}

}
