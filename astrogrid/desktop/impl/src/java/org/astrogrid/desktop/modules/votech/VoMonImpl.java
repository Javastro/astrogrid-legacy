/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.ivoa.CacheFactoryInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.votech.VoMon;
import org.votech.VoMonBean;

/** Implementation of the VoMon service.
 * @author Noel Winstanley
 * @since Dec 11, 20064:16:06 PM
 */
public class VoMonImpl implements VoMonInternal {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(VoMonImpl.class);

	private final URL voMonEndpoint;
	private final int refreshSeconds;
	private final Cache cache;
	private final UIInternal ui;


	public VoMonBean checkAvailability(URI arg0) {
		if (arg0 == null) {
			return null;
		}
		Element e = cache.get(arg0);
		if (e == null) {
			return null;
		}
		return(VoMonBean) e.getObjectValue();
	}

	public VoMonBean[] checkCeaAvailability(URI id) {
		if (id == null) {
			return new VoMonBean[0];
		}
		Element e = cache.get(id);
		if (e == null) {
			return new VoMonBean[0];
		}
		return (VoMonBean[])e.getObjectValue();
	}

	/** reloads service list - blocking until completed  - storing it all in cache.
	 * @throws ServiceException */
	public void reload() throws ServiceException{ 
		XMLInputFactory fac = XMLInputFactory.newInstance();
		XMLStreamReader in = null;
		try {
			in = fac.createXMLStreamReader(voMonEndpoint.openStream());
			VoMonBean bean = null;
			MultiMap apps = new MultiHashMap();
			while(in.hasNext()) {
				in.next();
				if (in.isStartElement()) {
					final String localName = in.getLocalName();
					if ( localName.equals("host")) { // create a new bean
						try {
							bean = new VoMonBean();
							bean.setId(new URI(in.getAttributeValue(null,"name")));
						} catch (URISyntaxException x) {
							bean = null;
						}
					}
					else if (localName.equals("status")) {
						if (bean != null) {
							try {
								bean.setCode(Integer.parseInt(in.getAttributeValue(null,"code")));
								bean.setMillis(Long.parseLong(in.getAttributeValue(null,"millis")));
							} catch (NumberFormatException e) {
								// oh well.
							}
							bean.setStatus(in.getAttributeValue(null,"status"));
							bean.setTimestamp(in.getAttributeValue(null,"timestamp"));
						}
					}
					else if ( localName.equals("parameter") 
							&& bean != null
							&& in.getAttributeValue(null,"name").equals("provides")) {
						// found a bit of metadata about the applications this service provides - add it to the multimap
						try {
							URI appId = new URI(in.getElementText().trim());
							apps.put(appId,bean);
						} catch (URISyntaxException x) {
							// oh well.
						} 
					}
				}
				if (in.isEndElement() && in.getLocalName().equals("host")) { // cache the info.
					if (bean != null) {
						Element e = new Element(bean.getId(),bean);
						cache.put(e);
						bean = null;
					}
				}
			}
			// save apps into cache too.
			for (Iterator i = apps.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry e = (Map.Entry)i.next();
				final Object appId = e.getKey();
				final Collection serviceCollection = (Collection)e.getValue();
					VoMonBean[] serviceArr = (VoMonBean[])  serviceCollection.toArray(new VoMonBean[serviceCollection.size()]);
					Element el = new Element(appId,serviceArr);
					cache.put(el);
			}
		} catch (XMLStreamException x) {
			throw new ServiceException(x);
		} catch (IOException x) {
			throw new ServiceException(x);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (XMLStreamException x) {
			}
		}
	}

	public VoMonImpl(final String endpoint, final int refreshSeconds, final CacheFactoryInternal cacheFac, final UIInternal ui)  {
		super();
		URL u; 
		try {
			u= new URL(endpoint);
		} catch (MalformedURLException e) {
			u = null; // not going to work..
		}
		this.voMonEndpoint = u;
		this.refreshSeconds = refreshSeconds;
		cache = cacheFac.getManager().getCache(CacheFactoryInternal.VOMON_CACHE);
		this.ui = ui;
		// start things happening - get list of services on a background thread.
		createWorker().start();
	}

	public BackgroundWorker createWorker() {
		return new BackgroundWorker(ui,"Downloading service statuses") {
			protected Object construct() throws Exception {
				try {
					reload();
				} catch (Throwable t) {
					logger.warn("Failed to download service statuses: " + t.getMessage());
				}
				return null;
			}
		};
	}

	public long getPeriod() {
		return 1000 * refreshSeconds;
	}




}
