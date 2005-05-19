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

import java.text.*;
import java.util.*;
import org.jdom.*;

/** Represents the status of a given service monitored by MARS.
	Status is a tuple of a status code and a detail string explaining
	what return value from the server led the probe to decide on
	the given status code.
*/

public class Status implements java.io.Serializable {

	private StatusCode code;
	private HashMap properties;
	private long timestamp;
	private long responseTime;

	public Status (StatusCode code, long responseTime) {
		this.code = code;
		this.responseTime = responseTime;
		this.timestamp = System.currentTimeMillis();
		if (code == UNKNOWN) {
			this.timestamp = -1;	// no check time for unknown status
		}
		this.properties = new HashMap();
	}

	public Status (StatusCode code) {
		this(code, -1);
	}

	public StatusCode getCode() {
		return code;
	}

	public boolean isFault() {
		return code.isFault();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public long getResponseTime() {
		return responseTime;
	}

	public String getProperty(String name) {
		return (String)properties.get(name);
	}

	public Iterator getPropertyNames() {
		return properties.keySet().iterator();
	}

	public void setProperty(String name, String value) {
		properties.put(name,value);
	}

	void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String toString() {
		if (getCode() == PROBEFAIL) return "probe error";
		if (getCode() == DOWN) return "down";
		if (getCode() == FASTCLOSE) return "closing";
		if (getCode() == TIMEOUT) return "timed out";
		if (getCode() == UNEXPECTED) return "bad reply";
		if (getCode() == UP) return "up";
		return "unknown";
	}

	/*------------------------------------------------------------
		XML parsing/generation methods
	------------------------------------------------------------*/

	public Element toJDOMElem() {
		// build the status element
		Element statelem = new Element("status", MarsModel.NAMESPACE);
        statelem.setAttribute("status", toString());
        statelem.setAttribute("timestamp", 
                              Main.df.format(new Date(getTimestamp())));
		statelem.setAttribute("code", String.valueOf(getCode().intValue()));
		statelem.setAttribute("millis", String.valueOf(getTimestamp()));
		statelem.setAttribute("resptime", String.valueOf(getResponseTime()));
		// now build properties
		Iterator propNames = getPropertyNames();
		while (propNames.hasNext()) {
			String propName = (String)propNames.next();
			String propValue = getProperty(propName);
			// create an element for this status property
			Element propelem =
				new Element("property", MarsModel.NAMESPACE);
			propelem.setAttribute("name",propName);
			propelem.addContent(propValue);
			statelem.addContent(propelem);
		}
		
		return statelem;
	}

	public static Status fromJDOMElem(Element in)
			throws InvalidDocumentException {
		// Read status code, timestamp, and response time from element
		String codeStr = in.getAttributeValue("code");
		int code = -1;
		String timestampStr = in.getAttributeValue("millis");
		long timestamp = -1;
		String resptimeStr = in.getAttributeValue("resptime");
		long resptime = -1;
		try {
			code = Integer.parseInt(codeStr);
		} catch (Exception ex) {
			throw new InvalidDocumentException("Bad or missing code");
		}
		try {
			timestamp = Long.parseLong(timestampStr);
		} catch (Exception ex) {
			throw new InvalidDocumentException("Bad or missing timestamp");
		}
		try {
			resptime = Long.parseLong(resptimeStr);
		} catch (Exception ex) {
			throw new InvalidDocumentException("Bad or missing resptime");
		}
		Status out = new Status(STATUSCODES[code], resptime);
		out.setTimestamp(timestamp);
		// Now iterate over properties, adding them to the status
		Iterator propElems =
			in.getChildren("property",MarsModel.NAMESPACE).iterator();
		while (propElems.hasNext()) {
			Element thisPropElem = (Element)propElems.next();
			String propName = thisPropElem.getAttributeValue("name");
			if (propName == null)
				throw new InvalidDocumentException("Missing property name");
			String propValue = thisPropElem.getText();
			if (propValue == null)
				throw new InvalidDocumentException("Missing property value");
			out.setProperty(propName,propValue);
		}
		// all done, return.
		return out;
	}

	/*------------------------------------------------------------
		StatusCode enumerated type
	------------------------------------------------------------*/

	public static class StatusCode implements java.io.Serializable {
		
		private int intcode;
		private StatusCode(int intcode) {
			this.intcode = intcode;
		}

		public int intValue() {
			return intcode;
		}

		public boolean isFault() {
			return intcode <= MAX_SOFTFAULTCODE;
		}
	}

	public static int MAX_HARDFAULTCODE = 2;
	public static int MAX_SOFTFAULTCODE = 4;

	public static final StatusCode PROBEFAIL = new StatusCode(0);
	public static final StatusCode DOWN = new StatusCode(1);
	public static final StatusCode FASTCLOSE = new StatusCode(2);
	public static final StatusCode TIMEOUT = new StatusCode(3);
	public static final StatusCode UNEXPECTED = new StatusCode(4);
	public static final StatusCode UP = new StatusCode(5);
	public static final StatusCode UNKNOWN = new StatusCode(6);
	public static final StatusCode[] STATUSCODES =
		{ PROBEFAIL, DOWN, FASTCLOSE, TIMEOUT, UNEXPECTED, UP, UNKNOWN };
}
