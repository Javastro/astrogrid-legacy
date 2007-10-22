/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.voexplorer.QuerySizerImpl;
import org.astrogrid.util.DomHelper;
import org.codehaus.xfire.util.STAXUtils;
import org.w3c.dom.Document;

/** Registry implementaiton,
 * delegates work to the external registry impl
 * @author Noel Winstanley
 * @since Jul 26, 200611:41:13 PM
 */
public class StreamingRegistryImpl implements RegistryInternal {

	/** processor that just copies input to a supplied stream writer */
	public static class WriterStreamProcessor implements StreamProcessor {
		private final XMLStreamWriter os;

		public WriterStreamProcessor(XMLStreamWriter os ) {
			this.os = os;
		}
		public void process(XMLStreamReader r) throws Exception {
			STAXUtils.copy(r,this.os);
		}
	}
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(StreamingRegistryImpl.class);

	private final ExternalRegistryInternal reg;
	private final Preference endpoint;
	private final Preference fallbackEndpoint;
	private final XMLOutputFactory outputFactory;
	private final Ehcache resourceCache;
	private final Ehcache documentCache;	
	private final Ehcache bulkCache;
	
			
		
	public StreamingRegistryImpl(final ExternalRegistryInternal reg,Preference endpoint,Preference fallbackEndpoint, 
			Ehcache resource, Ehcache document, Ehcache bulk) throws URISyntaxException {
		super();
		this.reg = reg;
		this.endpoint = endpoint;
		this.fallbackEndpoint =fallbackEndpoint;
		this.outputFactory = XMLOutputFactory.newInstance();	
		this.resourceCache  = resource;
		this.documentCache = document;
		this.bulkCache =bulk;
		
	}

	public void xquerySearchSave(String xquery, File saveLocation) throws InvalidArgumentException, ServiceException {
			XMLStreamWriter writer = null;
			OutputStream os = null;
			try {
				os = new FileOutputStream(saveLocation);
				writer = outputFactory.createXMLStreamWriter(os);
				WriterStreamProcessor proc = new WriterStreamProcessor(writer);
				xquerySearchStream(xquery,proc);
			} catch (XMLStreamException x) {
				throw new InvalidArgumentException("Failed to open location for writing",x);
			} catch (FileNotFoundException x) {
				throw new InvalidArgumentException("Failed to open location for writing",x);
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (XMLStreamException ex) {
						logger.warn("Exception while closing writer",ex);
					}
				}
				if (os != null) {
					try {
						os.close();
					} catch (IOException ex) {
						logger.warn("Exception while closing stream",ex);
					}
				}
			}
		}

	// these versions are uncached - up to their clients to cache where possible.
		public void getResourceStream(URI ivorn, StreamProcessor processor) throws ServiceException, NotFoundException {
			try {
				reg.getResourceStream(getSystemRegistryEndpoint(),ivorn,processor);
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				reg.getResourceStream(getFallbackSystemRegistryEndpoint(),ivorn,processor);
			}
		}

		public void keywordSearchStream(String keywords, boolean orValues, StreamProcessor processor) throws ServiceException {
			try {
				reg.keywordSearchStream(getSystemRegistryEndpoint(),keywords, orValues,processor);
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				reg.keywordSearchStream(getFallbackSystemRegistryEndpoint(),keywords,orValues,processor);
			}
		}

		public void xquerySearchStream(String xquery, StreamProcessor processor) throws ServiceException {
			try {
				reg.xquerySearchStream(getSystemRegistryEndpoint(),xquery,processor);
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				reg.xquerySearchStream(getFallbackSystemRegistryEndpoint(),xquery,processor);
			}
		}

		public Resource[] adqlsSearch(String arg0) throws ServiceException, InvalidArgumentException {
			try {
				Element e = bulkCache.get(arg0);
				if (e != null) {
					return (Resource[])e.getValue();
				} else {
					Resource[]res =  reg.adqlsSearch(getSystemRegistryEndpoint(),arg0);
					bulkCache.put(new Element(arg0,res));
					cacheResources(res);
					return res;
					}
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				return reg.adqlsSearch(getFallbackSystemRegistryEndpoint(),arg0);
			}
		}

		public Resource[] adqlxSearch(Document arg0) throws ServiceException , InvalidArgumentException{
			try {
				Element e = bulkCache.get(arg0);
				if (e != null) {
					return (Resource[])e.getValue();
				} else {				
				Resource[] res =  reg.adqlxSearch(getSystemRegistryEndpoint(),arg0);
				bulkCache.put(new Element(arg0,res));				
				cacheResources(res);
				return res;
				}
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				return reg.adqlxSearch(getFallbackSystemRegistryEndpoint(),arg0);
			}
		}
		
		private void cacheResources(Resource[] res) {
			for (int i = 0; i < res.length; i++) {
				if (resourceCache.get(res[i].getId()) == null) {
					resourceCache.put(new Element(res[i].getId(),res[i]));
				}
			}
		}
		
		
		public RegistryService getIdentity() throws ServiceException {
			try {
				Element el = resourceCache.get(getSystemRegistryEndpoint());
				if (el != null) {
					return (RegistryService)el.getValue();
				} else {
					RegistryService r = reg.getIdentity(getSystemRegistryEndpoint());
					el = new Element(getSystemRegistryEndpoint(),r);
					resourceCache.put(el);
					return r;
				}
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				return reg.getIdentity(getFallbackSystemRegistryEndpoint());
			}
		}

		public Resource getResource(URI arg0) throws NotFoundException, ServiceException {
			try {
				Element el = resourceCache.get(arg0);
				if (el != null) {
					return (Resource)el.getValue();
				} else {
					Resource res = reg.getResource(getSystemRegistryEndpoint(),arg0);
					el = new Element(arg0,res);
					resourceCache.put(el);
					return res;
				}
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				return reg.getResource(getFallbackSystemRegistryEndpoint(),arg0);
			}
		}
		public Document getResourceXML(URI ivorn) throws ServiceException, NotFoundException {
			try {
				final Element element = documentCache.get(ivorn);
				if (element != null) {
					return (Document)element.getValue();
				} else {
					Document doc =  reg.getResourceXML(getSystemRegistryEndpoint(),ivorn);
					documentCache.put(new Element(ivorn,doc));
					return doc;
				}
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				return reg.getResourceXML(getFallbackSystemRegistryEndpoint(),ivorn);
			}
		}
		public Resource[] keywordSearch(String arg0, boolean arg1) throws ServiceException {
			try {
				Element e = bulkCache.get(arg0 + arg1);
				if (e != null) {
					return (Resource[])e.getValue();
				} else {	
				Resource[] res =  reg.keywordSearch(getSystemRegistryEndpoint(),arg0,arg1);
				bulkCache.put(new Element(arg0 + arg1,res));					
				cacheResources(res);
				return res;		
				}
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				return reg.keywordSearch(getFallbackSystemRegistryEndpoint(),arg0,arg1);
			}
		}

		public Resource[] xquerySearch(String arg0) throws ServiceException {
			try {
				Element e = bulkCache.get(arg0);
				if (e != null) {
					return (Resource[])e.getValue();
				} else {						
				Resource[] res =  reg.xquerySearch(getSystemRegistryEndpoint(),arg0);
				bulkCache.put(new Element(arg0,res));
				cacheResources(res);
				return res;	
				}
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				return reg.xquerySearch(getFallbackSystemRegistryEndpoint(),arg0);
			}
		}
		
		public Document xquerySearchXML(String arg0) throws ServiceException {
			try {
				final Element element = documentCache.get(arg0);
				if (element != null) {
					return (Document)element.getValue();
				} else {
					Document doc =  reg.xquerySearchXML(getSystemRegistryEndpoint(),arg0);
					documentCache.put(new Element(arg0,doc));
					return doc;
				}
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				return reg.xquerySearchXML(getFallbackSystemRegistryEndpoint(),arg0);
			}
		}

		public void adqlxSearchStream(Document adqlx, boolean identifiersOnly, StreamProcessor processor)
			throws ServiceException, InvalidArgumentException {
			try {
				reg.adqlxSearchStream(getSystemRegistryEndpoint(),adqlx,identifiersOnly,processor);
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				reg.adqlxSearchStream(getFallbackSystemRegistryEndpoint(),adqlx,identifiersOnly,processor);
			}
		}
		
		public void getIdentityStream(StreamProcessor processor) throws ServiceException {
			try {
				reg.getIdentityStream(getSystemRegistryEndpoint(),processor);
			} catch (ServiceException e) {
				logger.warn("Failed to query main system registry - falling back",e);
				reg.getIdentityStream(getFallbackSystemRegistryEndpoint(),processor);
			}
		}
		
		public final URI getFallbackSystemRegistryEndpoint() throws ServiceException {
			try {
				return new URI(fallbackEndpoint.getValue());
			} catch (URISyntaxException x) {
				throw new ServiceException("Misconfigured url",x);
			}
		}

		public final URI getSystemRegistryEndpoint() throws ServiceException {
			try {
				return new URI(endpoint.getValue());
			} catch (URISyntaxException x) {
				throw new ServiceException("Misconfigured url",x);
			}				
		}

		/** self tests that the registry is working */
        public Test getSelftest() {
            TestSuite ts = new TestSuite("Registry tests");
            ts.addTest(new TestCase("Main registry service") {
                protected void runTest() {
                    try {
                        URI r = getSystemRegistryEndpoint();
                        r.toURL().openConnection().connect();
                    } catch (ServiceException x) {
                        logger.error("Misconfigured registry endpoint",x);
                        fail("Misconfigured registry endpoint");
                    } catch (MalformedURLException x) {
                        logger.error("Misconfigured registry endpoint",x);                        
                        fail("Misconfigured registry endpoint");
                    } catch (IOException x) {
                        logger.error("failed to connect to registry service",x);
                        fail("Failed to connect to registry service");
                    }
                    // getIdentity not implemented
//                    try {
//                        RegistryService serv = reg.getIdentity(getSystemRegistryEndpoint());
//                        assertNotNull("No registry identity returned",serv);
//                    } catch (ServiceException x) {
//                        fail("Failed to get registry identity");
//                    }
                   try {
                    Document doc = reg.xquerySearchXML(getSystemRegistryEndpoint(),QuerySizerImpl.constructSizingQuery(QuerySizerImpl.ALL_QUERY));
                    assertNotNull("No response returned from xquery",doc);
                    // in a normal junit test, it'd be more sensible to use XMLAssert for the following.
                    // however, this isn't bundled with the final app - so just vanilla junit.
                    String docString = DomHelper.DocumentToString(doc);
                    assertTrue("xquery didn't return expected response",StringUtils.contains(docString,"<size>"));
                } catch (ServiceException x) {
                    logger.error("Failed to xquery registry",x);
                    fail("Failed to xquery registry");
                }
                // could test 'getResource' here too - but I think this is enough.
                }
            });
            ts.addTest(new TestCase("Fallback registry service") {
                protected void runTest() {
                    try {
                        URI r = getFallbackSystemRegistryEndpoint();
                        r.toURL().openConnection().connect();
                    } catch (ServiceException x) {
                        logger.error("Misconfigured registry endpoint",x);
                        fail("Misconfigured  registry endpoint");
                    } catch (MalformedURLException x) {
                        logger.error("Misconfigured registry endpoint",x);
                        fail("Misconfigured registry endpoint");
                    } catch (IOException x) {
                        logger.error("Failed to connect to registry service",x);
                        fail("Failed to connect to registry service");
                    }
                    // get identntiy not implemented
//                    try {
//                        RegistryService serv = reg.getIdentity(getFallbackSystemRegistryEndpoint());
//                        assertNotNull("No registry identity returned",serv);
//                    } catch (ServiceException x) {
//                        fail("Failed to get registry identity");
//                    }  
                    try {
                        Document doc = reg.xquerySearchXML(getSystemRegistryEndpoint(),QuerySizerImpl.constructSizingQuery(QuerySizerImpl.ALL_QUERY));
                        assertNotNull("No response returned from xquery",doc);
                        String docString = DomHelper.DocumentToString(doc);
                        assertTrue("xquery didn't return expected response",StringUtils.contains(docString,"<size>"));                        
                    } catch (ServiceException x) {
                        logger.error("Failed to xquery registry",x);
                        fail("Failed to xquery registry");
                    }                   
                }
            });      
            ts.addTest(new TestCase("Registry caches") {
                protected void runTest() throws Throwable {
                    assertEquals("Problem with the bulk cache", Status.STATUS_ALIVE,bulkCache.getStatus());
                    assertEquals("Problem with the document cache",Status.STATUS_ALIVE,documentCache.getStatus());
                    assertEquals("Problem with the resource cache",Status.STATUS_ALIVE,resourceCache.getStatus());                    
                }
            });
            return ts;
        }





		
}
