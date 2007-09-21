/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.pref.Preference;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 19, 20071:42:05 PM
 */
public class JackdawAnnotationSource extends DynamicAnnotationSource {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(JackdawAnnotationSource.class);

	public JackdawAnnotationSource(Preference endpoint) {
		super(URI.create(endpoint.getValue()),"Related");
		fac = XMLInputFactory.newInstance();
	}
	private final XMLInputFactory fac;
	
	public Annotation getAnnotationFor(Resource r) {
		Annotation ann = new Annotation();
		ann.setResourceId(r.getId());
		ann.setSource(this);
		XMLStreamReader in = null;
		try {
			URL u = new URL(getSource().toString() + "?url=" + r.getId());
			in = fac.createXMLStreamReader(u.openStream());
			StrBuilder sb = new StrBuilder();
			while (in.hasNext()) {
				in.next();
				if (in.isStartElement()) {
					String localName = in.getLocalName();
					if (localName.equals("group")) {
						sb.append("<b>").append(in.getAttributeValue(null,"description")).append("</b><br>");
					} else if (localName.equals("url")) {
						String title =  in.getAttributeValue(null,"title");
						try {
						URI uri = new URI(in.getElementText());
						sb.append("<a href='")
							.append(uri)
							.append("'>");
						if (title != null) {
							sb.append(title);
						} else {
							sb.append(uri.getAuthority());
							sb.append(uri.getPath());
						}
						sb.append("</a><br>");
						} catch (URISyntaxException e) {
							logger.info("Invalid uri" + e.getMessage());
						}
					}
				}
				if (in.isEndElement()) {
					String localName = in.getLocalName();
					if (localName.equals("group")) {
						sb.append("<br>");
					}
				}
			}
			ann.setNote(sb.toString());
			return ann;
			
		} catch (IOException e) {
			logger.warn("Failed to connect to jackdaw",e);
			e.printStackTrace();
		} catch (XMLStreamException x) {
			logger.warn("Failed to parse jackdaw response",x);
			x.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (XMLStreamException x) {
			    //ignored
			}			
		}
		return ann;
		
	}

	public boolean shouldCache() {
		return true;
	}

}
