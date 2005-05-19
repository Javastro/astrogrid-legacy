/* MARS Network Monitoring Engine
   Copyright (C) 1999 Brian H. Trammell
   Copyright (C) 2002 Leapfrog Research & Development, LLC

	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, it is available at 
	http:///www.gnu.org/copyleft/gpl.html, or by writing to the
	Free Software Foundation, Inc., 59 Temple Place - Suite 330,
	Boston, MA  02111-1307, USA.
*/

package org.altara.mars;

import java.util.*;
import java.net.*;
import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import javax.swing.tree.*;

/** Represents the whole of the data model used by the
	MARS monitoring engine. MarsModel is the root of the data model
	tree containing hosts and services. MarsModel contains both
	configuration and current service status information.
*/

public class MarsModel implements java.io.Serializable, TreeNode {

	public static final Namespace NAMESPACE = Namespace.getNamespace("mars",
			"http://www.altara.org/mars/xmlns/model/");

	private SortedMap hosts;
	private LinkedList hostListCache;
	private HashSet mmListeners;

	public MarsModel() {
		hosts = new TreeMap();
		updateHostListCache();
		mmListeners = new HashSet();
	}

	public void addHost(Host host) {
		hosts.put(host.getName(),host);
		updateHostListCache();
		hostListChanged();
	}

	public Host getHost(String name) {
		return (Host)hosts.get(name);
	}

	public Iterator getHostNames() {
		return hosts.keySet().iterator();
	}

	public Iterator getHosts() {
		return hosts.values().iterator();
	}

	public void removeHost(String name) {
		hosts.remove(name);
		updateHostListCache();
		hostListChanged();
	}

	public void removeHost(Host host) {
		removeHost(host.getName());
	}

	public Set getServiceSet() {
		HashSet out = new HashSet();
		Iterator hostIter = getHosts();
		while (hostIter.hasNext()) {
			Host host = (Host)hostIter.next();
			Iterator serviceIter = host.getServices();
			while (serviceIter.hasNext()) {
				out.add(serviceIter.next());
			}
		}
		return out;
	}

	private void updateHostListCache() {
		hostListCache = new LinkedList();
		hostListCache.addAll(hosts.values());
	}

	/*------------------------------------------------------------
		Event Distribution
	------------------------------------------------------------*/

	public void addMarsModelListener(MarsModelListener mml) {
		mmListeners.add(mml);
	}

	public void removeMarsModelListener(MarsModelListener mml) {
		mmListeners.remove(mml);
	}

	public void clearMarsModelListeners() {
		mmListeners.clear();
	}

	public void hostChanged(Host host) {
		// ignore irrelevant hosts
		if (host.getModel() != this) return;
		// notify all listeners
		Iterator mmlIter = mmListeners.iterator();
		while (mmlIter.hasNext()) {
			((MarsModelListener)mmlIter.next()).hostChanged(host);
		}
	}

	public void serviceChanged(Service service) {
		// ignore irrelevant services
		if (service.getHost().getModel() != this) return;
		// notify all listeners
		Iterator mmlIter = mmListeners.iterator();
		while (mmlIter.hasNext()) {
			((MarsModelListener)mmlIter.next()).serviceChanged(service);
		}
	}

	public void hostListChanged() {
		// notify all listeners
		Iterator mmlIter = mmListeners.iterator();
		while (mmlIter.hasNext()) {
			((MarsModelListener)mmlIter.next()).hostListChanged();
		}
	}

	public void serviceListChanged(Host host) {
		// ignore irrelevant hosts
		if (host.getModel() != this) return;
		// notify all listeners
		Iterator mmlIter = mmListeners.iterator();
		while (mmlIter.hasNext()) {
			((MarsModelListener)mmlIter.next()).serviceListChanged(host);
		}
	}

	/*------------------------------------------------------------
		TreeNode implementation
	------------------------------------------------------------*/

	public boolean getAllowsChildren() {
		return true;
	}

	public int getChildCount() {
		return hostListCache.size();
	}

	public boolean isLeaf() {
		return (getChildCount() == 0);
	}

	public TreeNode getParent() {
		return null;
	}

	public Enumeration children() {
		return Collections.enumeration(hostListCache);
	}

	public TreeNode getChildAt(int childIndex) {
		return (TreeNode)hostListCache.get(childIndex);
	}

	public int getIndex(TreeNode node) {
		return hostListCache.indexOf(node);
	}

	/*------------------------------------------------------------
		XML parsing/generation methods
	------------------------------------------------------------*/

	public Element toJDOMElem(boolean includeStatus) {
		// create the root element
		Element root = new Element("model", NAMESPACE);
		// create the host-list child element
		Element hostlist = new Element("hostlist", NAMESPACE);
		root.addContent(hostlist);

		// now run through the object tree depth first
		Iterator hostNames = getHostNames();
		while (hostNames.hasNext()) {
			Host host = getHost(((String)hostNames.next()));
			// create an element for this host and add it to the list
			Element hostelem = host.toJDOMElem(includeStatus);
			hostlist.addContent(hostelem);
		}

		// now put the root element into a document and return it
		return root;
	}

	public static MarsModel fromJDOMElem(Element root)
			throws InvalidDocumentException, UnknownHostException {
		// First, create a new Model to attach everything to.
		MarsModel out = new MarsModel();

		// Verify the root element
		if ((!root.getNamespace().equals(NAMESPACE)) ||
			(!root.getName().equals("model")))
			throw new InvalidDocumentException("Invalid model element");

		// Get the first hostlist element within the root
		Element hostlist = root.getChild("hostlist",NAMESPACE);
		if (hostlist == null) 
			throw new InvalidDocumentException("Missing hostlist element");

		// Now extract all the host elements from the hostlist
		Iterator hostElems = hostlist.getChildren("host",NAMESPACE).iterator();
		while (hostElems.hasNext()) {
			Element thisHostElem = (Element)hostElems.next();
			Host.fromJDOMElem(out, thisHostElem);
		}	
		// the internal data structure is now built. enjoy!
		return out;
	}
}
