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
import org.jdom.*;
import javax.swing.tree.*;

/** Represents a single host monitored by MARS. Each Host contains a
	set of Services, which contain service specific information.
*/

public class Host implements java.io.Serializable, TreeNode {

	private MarsModel model;
	private String name;
	private InetAddress address;
	private SortedMap services;
	private LinkedList serviceListCache;

	public Host(MarsModel model, String nickname, InetAddress address) {
		this.model = model;
		this.name = nickname;
		this.address = address;
		this.services = new TreeMap();
		updateServiceListCache();
		model.addHost(this);
	}
	
	public Host(MarsModel model, String nickname, String hostname)
			throws UnknownHostException {
		this(model,nickname,InetAddress.getByName(hostname));
	}

	public Host(MarsModel model, String hostname)
			throws UnknownHostException {
		this(model,hostname,hostname);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public void addService(Service service) {
		services.put(service.getName(),service);
		updateServiceListCache();
		model.serviceListChanged(this);
	}

	public void fireHostChanged() {
		model.hostChanged(this);
	}

	public MarsModel getModel() {
		return model;
	}

	public String getName() {
		return name;
	}

	public InetAddress getAddress() {
		return address;
	}

	public Service getService(String name) {
		return (Service)services.get(name);
	}

	public Iterator getServiceNames() {
		return services.keySet().iterator();
	}

	public Iterator getServices() {
		return services.values().iterator();
	}

	public void removeService(String name) {
		services.remove(name);
		updateServiceListCache();
		model.serviceListChanged(this);
	}

	public void removeService(Service service) {
		removeService(service.getName());
	}

	private void updateServiceListCache() {
		serviceListCache = new LinkedList();
		serviceListCache.addAll(services.values());
	}

    public boolean isOK() {
        Iterator svcIter = getServices();
        while (svcIter.hasNext()) {
            if (((Service)svcIter.next()).getStatus().isFault()) {
                return false;
            }
        }
        return true;
    }

	public String toString() {
		return name+" at "+address.getHostName();
	}

	/*------------------------------------------------------------
		TreeNode implementation
	------------------------------------------------------------*/

	public boolean getAllowsChildren() {
		return true;
	}

	public int getChildCount() {
		return serviceListCache.size();
	}

	public boolean isLeaf() {
		return (getChildCount() == 0);
	}

	public TreeNode getParent() {
		return model;
	}

	public Enumeration children() {
		return Collections.enumeration(serviceListCache);
	}

	public TreeNode getChildAt(int childIndex) {
		return (TreeNode)serviceListCache.get(childIndex);
	}

	public int getIndex(TreeNode node) {
		return serviceListCache.indexOf(node);
	}

	/*------------------------------------------------------------
		Cloning
	------------------------------------------------------------*/

	public Host duplicate() {
		Host newHost = new Host(model, name+" [duplicate]", address);
		Iterator services = getServices();
		while (services.hasNext()) {
			((Service)services.next()).duplicate(newHost);
		}
		return newHost;
	}

	/*------------------------------------------------------------
		XML parsing/generation methods
	------------------------------------------------------------*/

	public Element toJDOMElem(boolean includeStatus) {
		Element hostelem = new Element("host", MarsModel.NAMESPACE);
		hostelem.setAttribute("name",getName());
		hostelem.setAttribute("address",getAddress().getHostName());
		// get this host's services
		Iterator serviceNames = getServiceNames();
		while (serviceNames.hasNext()) {
			Service service = getService(((String)serviceNames.next()));
			// create an element for this service and add it to the host
			Element servelem = service.toJDOMElem(includeStatus);
			hostelem.addContent(servelem);
		}
		return hostelem;
	}

	public static Host fromJDOMElem(MarsModel model, Element in)
			throws InvalidDocumentException, UnknownHostException {
		// get host name and address from attributes
		String hostName = in.getAttributeValue("name");
		if (hostName == null)
			throw new InvalidDocumentException("Missing host name");
		String addressStr = in.getAttributeValue("address");
		InetAddress address = InetAddress.getByName(addressStr);
		// create the Host
		Host out = new Host(model,hostName,address);
		// find all this host's services and create them
		Iterator svcElems =
			in.getChildren("service",MarsModel.NAMESPACE).iterator();
		while (svcElems.hasNext()) {
			Element thisSvcElem = (Element)svcElems.next();
			Service.fromJDOMElem(out,thisSvcElem);
		}
		// done, return the new host
		return out;
	}
}	
