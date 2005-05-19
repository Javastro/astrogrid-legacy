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

package org.altara.mars.engine;

import java.io.*;
import java.net.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;
import org.altara.mars.*;
import org.apache.oro.text.regex.*;

/** This class implements a SendExpectProbe-creating ProbeFactory
	configured from an incoming JDOM mdef:svctype element.
*/

public class XmlProbeFactory extends ProbeFactory {

	public static final Namespace NAMESPACE =
		Namespace.getNamespace("mdef","http://www.altara.org/mars/xmlns/def/");
	
	private int defaultPort;
	private List paramNames;
	private List paramLabels;
	private HashMap paramDefaults;
	private Element scriptElem;
	private SendExpectProbe prototypeProbe;

	public XmlProbeFactory(String name, Element in) 
			throws InvalidDocumentException {
		super(name);
		
		// set the default port
		try {
			this.defaultPort = Integer.parseInt(
				in.getAttributeValue("defaultPort"));
		} catch (Exception ex) {
			throw new InvalidDocumentException("Missing default port");
		}
		
		// read parameter definitions
		paramNames = new LinkedList();
		paramLabels = new LinkedList();
		paramDefaults = new HashMap();
		Iterator paramElemIter = in.getChildren("param",NAMESPACE).iterator();
		while (paramElemIter.hasNext()) {
			parseParam((Element)paramElemIter.next());
		}

		// read the script
		scriptElem = in.getChild("script",NAMESPACE);
		if (scriptElem == null)
			throw new InvalidDocumentException("Missing script");

		// parse it into an example SendExpectProbe
		parseScript(scriptElem);
	}

	public int getDefaultPort() {
		return defaultPort;
	}

	public Probe createProbe(Service service) {
		SendExpectProbe out = prototypeProbe.instantiatePrototype(service);
		return out;
	}

	public String[] getServiceParamNames() {
		if (paramNames.size() == 0) return null;
		return (String[])paramNames.toArray(new String[paramNames.size()]);
	}

	public String[] getServiceParamLabels() {
		if (paramLabels.size() == 0) return null;
		return (String[])paramLabels.toArray(new String[paramLabels.size()]);
	}

	public String getServiceParamDefault(Service service, String name) {
		String def = (String)paramDefaults.get(name);
        if (def.equals("%%(remote-hostname)")) {
            def = service.getHost().getAddress().getHostName();
        }
        return def;
	}

// ---------------------------------------------------------------------
// Script Parsing - parses XML into verified runtime script
// ---------------------------------------------------------------------

	private void parseScript(Element in) throws InvalidDocumentException {
		prototypeProbe = new SendExpectProbe();
		Iterator scriptIter = in.getChildren().iterator();
		while (scriptIter.hasNext()) {
			Element nextLine = (Element)scriptIter.next();
			String lineName = nextLine.getName();
			if (lineName.equals("param")) {
				parseParam(nextLine);
			} else if (lineName.equals("send")) {
				parseSend(nextLine);
			} else if (lineName.equals("expect")) {
				parseExpect(nextLine);
			} else if (lineName.equals("fail")) {
				Element nextExpect = (Element)scriptIter.next();
				if (nextExpect == null ||
					!nextExpect.getName().equals("expect")) {
					throw new InvalidDocumentException("fail wasn't followed "+
					"by expect");
				}
				parseFailExpect(nextLine,nextExpect);
			} else {
				throw new InvalidDocumentException("Unexpected line in script");
			}
		}
		/*
		// print the script out - for debugging purposes
		System.err.println("SVCTYPE "+getName());
		System.err.println("Default Port "+getDefaultPort());
		System.err.println("Parameters:");
		String[] pnStrs= getServiceParamNames();
		String[] plStrs= getServiceParamLabels();
		if (pnStrs == null) {
			System.err.println("\tNo Parameters");
		} else {
			for (int i = 0; i < pnStrs.length; i++) {
				System.err.println("\t"+pnStrs[i]+" ("+plStrs[i]+")");
			}
		}
		System.err.println("Created prototype:\n"+prototypeProbe.dumpScript());
		*/
	}

	private void parseParam(Element in) 
			throws InvalidDocumentException {
		String paramName = in.getAttributeValue("name");
		if (paramName == null)
			throw new InvalidDocumentException("Missing "+
			"parameter name");
		String paramLabel = in.getAttributeValue("label");
		if (paramLabel == null)
			throw new InvalidDocumentException("Missing "+
			"parameter label");
		paramNames.add(paramName);
		paramLabels.add(paramLabel);
		String paramDefault = in.getAttributeValue("default");
		if (paramDefault != null) {
			paramDefaults.put(paramName,paramDefault);
		}
	}

	private void parseSend(Element in) 
			throws InvalidDocumentException {
		Iterator cIter = parseContent(in).iterator();
		while (cIter.hasNext()) prototypeProbe.send(cIter.next());
	}

	private void parseExpect(Element in) 
			throws InvalidDocumentException {
		LinkedList content = parseContent(in);
		if (content.size() != 1) {
			throw new InvalidDocumentException("Expect must be either "+
				"a single string or a regex.");
		}
		prototypeProbe.expect(content.get(0));
	}

	private void parseFailExpect(Element failIn, Element expectIn) 
			throws InvalidDocumentException {
		LinkedList failContent = parseContent(failIn);
		if (failContent.size() != 1) {
			throw new InvalidDocumentException("Fail must be either "+
				"a single string or a regex.");
		}
		LinkedList expectContent = parseContent(expectIn);
		if (expectContent.size() != 1) {
			throw new InvalidDocumentException("Expect must be either "+
				"a single string or a regex.");
		}
		prototypeProbe.expect(expectContent.get(0),failContent.get(0));
	}

	private LinkedList parseContent(Element in)
			throws InvalidDocumentException {
		LinkedList out = new LinkedList();
	
		// get the element's content list
		Iterator contentIter = in.getContent().iterator();

		while (contentIter.hasNext()) {
			Object nextItem = contentIter.next();
			// check for element
			if (nextItem instanceof Element) {
				Element nextElem = (Element)nextItem;
				// What sort of element?
				String nextElemName = nextElem.getName();
				if (nextElemName.equals("regex")) {
					// if there's a regular expression, there can be only
					// one, and nothing else
					String pattern = nextElem.getAttributeValue("pattern");
					String patternName = nextElem.getAttributeValue("name");
                    if (pattern == null && patternName == null)
                        throw new InvalidDocumentException(
                            "Missing regex pattern and pattern name");
                    out.clear();
                    if (pattern != null) {
                        try {
                            out.add(recompiler.compile(pattern));
                        } catch (MalformedPatternException ex) {
                            throw new InvalidDocumentException(
                                "Couldn't compile regex: "+ex.getMessage());
                        }
					} else {
                        out.add(new SendExpectClient.ParameterPattern(
                                    prototypeProbe,patternName));
                    }
					break;
				} else if (nextElemName.equals("param")) {
					String paramName = nextElem.getAttributeValue("name");
					if (paramName == null)
						throw new InvalidDocumentException("Missing "+
							"parameter name");
					out.add(new SendExpectClient.SendParameter(
						prototypeProbe, paramName));
				} else if (nextElemName.equals("remote-hostname")) {
					out.add(new SendExpectClient.SendRemoteHostname(
						prototypeProbe));
				} else if (nextElemName.equals("version")) {
					out.add(Main.VERSION);
				} else if (nextElemName.equals("space")) {
					out.add(" ");
				} else if (nextElemName.equals("crlf")) {
					out.add("\r\n");
				} else {
					throw new InvalidDocumentException("Unexpected element "+
						nextElemName+" in script line content");
				}
			} else if (nextItem instanceof Text) {
				out.add(((Text)nextItem).getTextNormalize());
			} else {
				// Ignore anything else in script line
			}
		}
		return out;
	}

// ---------------------------------------------------------------------
// Autoregistration - called by Main to locate and attach mars-def file
// ---------------------------------------------------------------------

	public static void registerAll(File homeDir)
			throws InvalidDocumentException, IOException, JDOMException {
		// locate a mars-def root element
		Element root = getMarsDefRoot(homeDir);

		// find all svctype children
		Iterator svcIter = root.getChildren("svctype",NAMESPACE).iterator();
		while (svcIter.hasNext()) {
			// determine its name
			Element svcElem = (Element)svcIter.next();
			String svcName = svcElem.getAttributeValue("name");
			if (svcName == null)
				throw new InvalidDocumentException("Missing svctype name");
			// create and register a new factory
			registerFactory(new XmlProbeFactory(svcName,svcElem));
		}
	}

	private static Element getMarsDefRoot(File homeDir)
			throws IOException, JDOMException {
		// look for a mars-def.xml in the home directory first
		File homeDef = new File(homeDir,"mars-def.xml");
		if (homeDef.exists()) {
			Main.getMain().showStatus("Using probe definition file "+homeDef.getAbsolutePath());
			return new SAXBuilder().build(homeDef).getRootElement();
		}

		// look for the JAR-contained mars-def.xml next
		URL jarDef =
			XmlProbeFactory.class
			.getResource("mars-def.xml");
		if (jarDef == null) throw new IOException("JAR missing mars-def.xml");
		return new SAXBuilder().build(jarDef.openStream()).getRootElement();
	}
}
