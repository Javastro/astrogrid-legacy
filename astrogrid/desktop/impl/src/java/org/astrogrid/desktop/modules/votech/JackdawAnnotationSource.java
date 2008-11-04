/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.pref.Preference;

/** Query the experimental Jackdaw annotation service.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 19, 20071:42:05 PM
 */
public class JackdawAnnotationSource extends DynamicAnnotationSource {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(JackdawAnnotationSource.class);
    private final Preference enabled;

	public JackdawAnnotationSource(final Preference enabled, final Preference endpoint) {
		super(URI.create(endpoint.getValue()),"Related");
        this.enabled = enabled;
		fac = XMLInputFactory.newInstance();
	}
	private final XMLInputFactory fac;
	
	public Annotation getAnnotationFor(final Resource r) {
	    if (! enabled.asBoolean()) {
	        return null;
	    }
		final Annotation ann = new Annotation();
		ann.setResourceId(r.getId());
		ann.setSource(this);
		XMLStreamReader in = null;
		try {
			final URL u = new URL(getSource().toString() + "?url=" + r.getId());
			in = fac.createXMLStreamReader(u.openStream());
			final StrBuilder sb = new StrBuilder(64);
			while (in.hasNext()) {
				in.next();
				if (in.isStartElement()) {
					final String localName = in.getLocalName();
					if (localName.equals("group")) {
						sb.append("<i>").append(in.getAttributeValue(null,"description")).append("</i><br>");
					} else if (localName.equals("url")) {
						final String title =  in.getAttributeValue(null,"title");
						try {
						final URI uri = new URI(in.getElementText());
						sb.append("<a href='")
							.append(uri)
							.append("'>");
						if (title != null) {
							sb.append(title);
						} else {
							sb.append(uri.getAuthority());
							sb.append(StringUtils.replace(uri.getPath(),"/","/<wbr>"));// mark potential word-wrap points.
						}
						sb.append("</a><br>");
						} catch (final URISyntaxException e) {
							logger.info("Invalid uri" + e.getMessage());
						}
					}
				}
				if (in.isEndElement()) {
					final String localName = in.getLocalName();
					if (localName.equals("group")) {
						sb.append("<br>");
					}
				}
			}
			ann.setNote(sb.toString());
			return ann;
			
		} catch (final IOException e) {
			logger.warn("Failed to connect to jackdaw",e);
		} catch (final XMLStreamException x) {
			logger.warn("Failed to parse jackdaw response",x);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (final XMLStreamException x) {
			    //ignored
			}			
		}
		return ann;
		
	}

	public boolean shouldCache() {
		return true;
	}

}
