/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
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

	private final URL endpoint;
	private final int refreshSeconds;
	private final Ehcache cache;

	/** catch - prevents any serivce predictions from being made until the vomon status.xml is first downloaded */
    private boolean populated;


	public VoMonBean checkAvailability(URI arg0) {
		if (arg0 == null) {
			return null;
		}
		if (! populated || ! cache.getStatus().equals(Status.STATUS_ALIVE)) {
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
			in = fac.createXMLStreamReader(endpoint.openStream());
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
					// check for 2 different variants on how apps are represented
					else if ( localName.equals("parameter") && "provides".equals(in.getAttributeValue(null,"name"))
							&& bean != null ) {
						try {
							final String str = in.getElementText().trim();
							if (str.length() > 0) { // else it's probably using the other representation.
								URI appId = new URI(str);
								apps.put(appId,bean);
							}
						} catch (URISyntaxException x) {
							// oh well.
						} 
					} 	
					else if ( localName.equals("parameter") && "provides-list".equals(in.getAttributeValue(null,"name"))
							&& bean != null ) {
						try {
							final String str = in.getElementText().trim();
							if (str.length() > 0) {
								StringTokenizer st = new StringTokenizer(str);
								while (st.hasMoreTokens()) {
									URI appId = new URI(st.nextToken());
									apps.put(appId,bean);
								}
							}
						} catch (URISyntaxException x) {
							// oh well.
						} 
					} 						
				
				}
				if (in.isEndElement() && in.getLocalName().equals("host")) { // cache the info.
					if (bean != null && cache.getStatus().equals(Status.STATUS_ALIVE)) {
						Element e = new Element(bean.getId(),bean);
						cache.put(e);
						bean = null;
					}
				}
			}
			// save apps into cache too.
			final Set s = new HashSet();
			for (Iterator i = apps.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry e = (Map.Entry)i.next();
				final Object appId = e.getKey();
				final Collection serviceCollection = (Collection)e.getValue(); 
				s.clear();
				s.addAll(serviceCollection);// remove duplicates from the collection
					VoMonBean[] serviceArr = (VoMonBean[])  s.toArray(new VoMonBean[s.size()]);
					Element el = new Element(appId,serviceArr);
					cache.put(el);
			}
			populated = true; // now got some data.
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
			    //ignored
			}
		}
	}

	public VoMonImpl(final String endpoint, final int refreshSeconds, Ehcache cache )  {
		super();
		URL u; 
		try {
			u= new URL(endpoint);
		} catch (MalformedURLException e) {
			u = null; // not going to work..
		}
		this.endpoint = u;
		this.refreshSeconds = refreshSeconds;
		this.cache =cache;
		this.populated = false;
	}

	
	public void execute() {
		try {
			reload();
		} catch (Throwable t) {
			logger.warn("Failed to download service statuses: ",t);
		}		
	}

	public long getPeriod() {
		return 1000 * refreshSeconds;
	}
	
	public Principal getPrincipal() {
		return null; // run under default user.
	}

	// higher-level utilities.
    public String getTooltipInformationFor(Resource ri) {
        if (! populated) {
            return null;
        }
        HtmlBuilder result = new HtmlBuilder();
        if (ri instanceof Service) {
            VoMonBean b = checkAvailability(ri.getId());
            if (b == null) {
                result.append("This resource is unknown to the monitoring service");
            } else {
                result.append("The monitoring service judged this resource to be <b>")
                .append(b.getStatus())
                .append("</b> at ")
                .append(b.getTimestamp());
            }
        } else if (ri instanceof CeaApplication) {
            VoMonBean[] arr = checkCeaAvailability(ri.getId());
            if (arr == null || arr.length == 0) {
                result.append("The monitoring service knows of no providers of this application");
            } else {
                result.append("This application is provided by the following services:<ul>");
                for (int i =0; i < arr.length; i++) {
                    VoMonBean b = arr[i];
                    result.append("<li>")
                    .append(b.getId())
                    .append(" - judged to be ")
                    .append(" <b>")
                    .append(b.getStatus())
                    .append("</b> at ")
                    .append(b.getTimestamp());
                }
                result.append("</ul>");
            }
        }
        return result.toString();
    }

    public Icon suggestIconFor(Resource r) {
        if (! populated) {
            return null;
        }
        if (r instanceof Service) {
            VoMonBean b = checkAvailability(r.getId());
            if (b == null) {// unknown
                return UIConstants.UNKNOWN_ICON;
            } else if ( b.getCode() != VoMonBean.UP_CODE) { // service down
                return UIConstants.SERVICE_DOWN_ICON;
            } else {
                return UIConstants.SERVICE_OK_ICON;
            }
        } else if (r instanceof CeaApplication) {
            VoMonBean[] providers = checkCeaAvailability(r.getId());
            if (providers == null ) { 
                // unknown application.
                return UIConstants.UNKNOWN_ICON;
            } else {
                for (int i = 0; i < providers.length; i++) {
                    if (providers[i].getCode() == VoMonBean.UP_CODE) {
                        return UIConstants.SERVICE_OK_ICON;
                    }
                }
                // all servers unavailable.
                return UIConstants.SERVICE_DOWN_ICON;
            }

        } else {
            return null;
        }
    }

    
    public Test getSelftest() {
        return new TestCase("Services monitor") {
            protected void runTest()  {
                assertNotNull("Invalid endpoint",endpoint);
                try {
                    endpoint.openConnection().connect();
                } catch (IOException x) {
                    fail("Unable to access monitor service");
                }
                assertEquals("Problem with cache",Status.STATUS_ALIVE,cache.getStatus());
            //race condition - may not yet have downloaded ...
                assertTrue("Service statuses not downloaded",cache.getSize() > 0);
                
            }
        };
    }

    /**
     * @return the endpoint
     */
    public final URL getEndpoint() {
        return this.endpoint;
    }




}
