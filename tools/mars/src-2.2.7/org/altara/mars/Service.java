/* MARS Network Monitoring Engine
   Copyright (C) 1999 Brian H. Trammell
   Copyright (C) 2002-2003 Leapfrog Research & Development, LLC

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
import org.jdom.*;
import javax.swing.tree.*;
import org.altara.mars.engine.*;
import org.apache.oro.text.regex.*;

/** Represents a service on a host monitored by MARS. Each service
	has a probe factory (which the service can use to build a probe
	that knows how to monitor said service), a port on which the
	service is running, a set of arguments to give to that factory's
	create() method, and a representation of the current status
	of that service.
*/

public class Service implements java.io.Serializable, TreeNode {

    public static final int NOTAC_NEVER = 0;
	public static final int NOTAC_IMMEDIATE = 1;
	public static final int DEFAULT_NOTAC = NOTAC_IMMEDIATE;
	public static final int MAX_NOTAC = 3;

	public static final long DEFAULT_PERIOD = 60000L; // 60 sec.
	public static final long DEFAULT_TIMEOUT = 5000L; // 5 sec.

	private Host host;
	private String name;
	private ProbeFactory factory;
	private int port;
	private HashMap params;
	private HashMap paramRegexCache;
	private Status status;
	private long timeout;
	private long period;
    private int notac;

	public Service (Host host, String name, String svctype, int port)
			throws InvalidServiceTypeException {
		this(host,name,svctype,port,DEFAULT_TIMEOUT,DEFAULT_PERIOD,
            DEFAULT_NOTAC);
	}

	public Service (Host host, String name, String svctype,
			int port, long timeout, long period, int notac)
			throws InvalidServiceTypeException {
		this(host,name,svctype,port,timeout,period,notac,new HashMap());
	}

	public Service (Host host, String name, String svctype,
			int port, long timeout, long period, int notac, HashMap params)
			throws InvalidServiceTypeException {
		this.host = host;
		this.name = name;
		this.factory = ProbeFactory.getFactory(svctype);
		if (factory == null) throw new InvalidServiceTypeException();
		this.port = port;
		this.params = params;
        this.paramRegexCache = new HashMap();
		this.status = new Status(Status.UNKNOWN);
		this.timeout = timeout;
		this.period = period;
        this.notac = notac;
		host.addService(this);
	}

	public void setParameter(String name, String value) {
		params.put(name,value);
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setName(String name) {
 		this.name = name;
	}

	public void setSvcType(String svctype) throws InvalidServiceTypeException {
		this.factory = ProbeFactory.getFactory(svctype);
		if (factory == null) throw new InvalidServiceTypeException();
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

    public void setNotac(int notac) {
        this.notac = notac;
    }

	public void fireServiceChanged() {
		host.getModel().serviceChanged(this);
	}

	public Host getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
		
	public String getName() {
		return name;
	}

	public Probe getProbe() {
		return factory.createProbe(this);
	}

	public String getSvcType() {
		return factory.getName();
	}

	public ProbeFactory getProbeFactory() {
		return factory;
	}

	public Status getStatus() {
		return status;
	}

	public String getParameter(String name) {
		return (String)params.get(name);
	}

    public Perl5Pattern getParameterAsRegex(String name) 
        throws MalformedPatternException {
        
        String regexString = getParameter(name);
        if (regexString == null || regexString.length() == 0) return null;
        
        Perl5Pattern out = (Perl5Pattern)paramRegexCache.get(regexString);
        if (out == null) {
            out = (Perl5Pattern)ProbeFactory.recompiler.compile(regexString);
            paramRegexCache.put(regexString,out);
        }
        
        return out;
    }

	public Iterator getParameterNames() {
		return params.keySet().iterator();
	}

	public long getTimeout() {
		return timeout;
	}

	public long getPeriod() {
		return period;
	}

    public int getNotac() {
        return notac;
    }

	public boolean isDue() {
		return System.currentTimeMillis() > status.getTimestamp() + period;
	}

	public String toString() {
		return "Service "+name+
			" ("+getSvcType()+"), port "+port+" on "+host.getName();
	}

	/*------------------------------------------------------------
		TreeNode implementation
	------------------------------------------------------------*/

	public boolean getAllowsChildren() {
		return false;
	}

	public int getChildCount() {
		return 0;
	}

	public boolean isLeaf() {
		return true;
	}

	public TreeNode getParent() {
		return host;
	}

	public Enumeration children() {
		return null;
	}

	public TreeNode getChildAt(int childIndex) {
		return null;
	}

	public int getIndex(TreeNode node) {
		return -1;
	}

	public TreePath getTreePath() {
		return new TreePath(host.getModel())
			.pathByAddingChild(host)
			.pathByAddingChild(this);
	}

	/*------------------------------------------------------------
		Cloning
	------------------------------------------------------------*/

	public Service duplicate(Host newHost) {
		try {
			return new Service(newHost, name, factory.getName(), port,
				timeout, period, notac, (HashMap)params.clone());
		} catch (InvalidServiceTypeException ignored) {
			// can never happen
			return null;
		}
	}

	/*------------------------------------------------------------
		XML parsing/generation methods
	------------------------------------------------------------*/

	public Element toJDOMElem(boolean includeStatus) {
		Element servelem = new Element("service", MarsModel.NAMESPACE);
		servelem.setAttribute("name",getName());
		servelem.setAttribute("port",String.valueOf(getPort()));
		servelem.setAttribute("svctype",getSvcType());
		servelem.setAttribute("timeout",String.valueOf(getTimeout()));
		servelem.setAttribute("period",String.valueOf(getPeriod()));
        servelem.setAttribute("notac",String.valueOf(getNotac()));
		// get all the parameters for this service
		Iterator svcparamNames = getParameterNames();
		while (svcparamNames.hasNext()) {
			String paramName = (String)svcparamNames.next();
			String paramValue = getParameter(paramName);
			// create an element for this service parameter
			Element svcparamelem =
				new Element("parameter", MarsModel.NAMESPACE);
			svcparamelem.setAttribute("name",paramName);
			svcparamelem.addContent(paramValue);
			servelem.addContent(svcparamelem);
		}
		// if we're storing status too, do so now
		if (includeStatus) {
			Status status = getStatus();
			Element statelem = status.toJDOMElem();
			servelem.addContent(statelem);
		}
		return servelem;
	}

	public static Service fromJDOMElem(Host host, Element in) 
			throws InvalidDocumentException {
		// get service information from attributes
		String svcName = in.getAttributeValue("name");
		if (svcName == null)
			throw new InvalidDocumentException("Missing service name");
		String svcType = in.getAttributeValue("svctype");
		if (svcType == null)
			throw new InvalidDocumentException("Missing service type");
		String portStr = in.getAttributeValue("port");
		int port = -1;
		String timeoutStr = in.getAttributeValue("timeout");
		long timeout = -1;
		String periodStr = in.getAttributeValue("period");
		long period = -1;
        String notacStr = in.getAttributeValue("notac");
        int notac = -1;
		try {
			port = Integer.parseInt(portStr);
		} catch (Exception ex) {
			throw new InvalidDocumentException(
                "Bad or missing service port");
		}
		try {
			timeout = Long.parseLong(timeoutStr);
            if (timeout < 0) throw new Exception();
		} catch (Exception ex) {
			throw new InvalidDocumentException(
                "Bad or missing service timeout");
		}
		try {
			period = Long.parseLong(periodStr);
            if (period < 0) throw new Exception();
		} catch (Exception ex) {
			throw new InvalidDocumentException(
                "Bad or missing service period");
		}
        // null isn't an error on notac - for backward compatibility
        if (notacStr == null) {
            notac = DEFAULT_NOTAC;
        } else {
            try {
                notac = Integer.parseInt(notacStr);
                if (notac > MAX_NOTAC) throw new Exception();
                if (notac < 0) throw new Exception();
            } catch (Exception ex) {
                throw new InvalidDocumentException(
                    "Bad service notification attempt count");
            }
        }
		// okay. everything's there. try creating the service.
		Service out = null;
		try {
			out = new Service(host,svcName,svcType,port,timeout,period,notac);
		} catch (InvalidServiceTypeException ex) {
			throw new InvalidDocumentException("Bad service type");
		}
		// add any parameters the service has
		Iterator paramElems =
			in.getChildren("parameter",MarsModel.NAMESPACE).iterator();
		while (paramElems.hasNext()) {
			Element thisParamElem = (Element)paramElems.next();
			String paramName = thisParamElem.getAttributeValue("name");
			if (paramName == null)
				throw new InvalidDocumentException("Missing parameter name");
			String paramValue = thisParamElem.getText();
			if (paramValue == null)
				throw new InvalidDocumentException("Missing parameter value");
			out.setParameter(paramName,paramValue);
		}
		// add status information if it exists
		Element statusElem = in.getChild("status", MarsModel.NAMESPACE);
		if (statusElem != null) {
			out.setStatus(Status.fromJDOMElem(statusElem));
		}
		// done building this service, return it
		return out;
	}
}
