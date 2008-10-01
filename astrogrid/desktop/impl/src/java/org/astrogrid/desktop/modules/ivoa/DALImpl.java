/*$Id: DALImpl.java,v 1.22 2008/10/01 09:53:43 nw Exp $
 * Created on 17-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ivoa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Dal;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.io.Piper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.DescribedValue;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.votable.TableContentHandler;
import uk.ac.starlink.votable.TableHandler;


/** Abstract class for implemntations of HTTP-GET based DAL standards
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Oct-2005
 */
public abstract class DALImpl implements Dal{
    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(DALImpl.class);

    /** Construct a new DALImpl
     * 
     */
    public DALImpl(final Registry reg, final MyspaceInternal ms) {
        super();
        this.reg = reg;
        this.ms = ms;

    }
  	protected final Registry reg;
    protected final MyspaceInternal ms;


    /** utility method for subclasses to use to resolve an abstract service id to url endpoint
     * @fixme replace this general class with protocol-specific methods.
     * @param arg0 service id (ivo://) or endpoint (http://)
     * @return resolved, or pass-thru service endpoint.
     * @throws InvalidArgumentException if arg0 is an unknown scheme of URI.
     * @throws NotFoundException if service cannot be resolved
     */
    protected URL resolveEndpoint(final URI arg0) throws InvalidArgumentException, NotFoundException {
    	if (arg0 == null || arg0.getScheme() == null) {
    		throw new InvalidArgumentException("No endpoint provided");
    	}
        if (arg0.getScheme().equals("http")) {
            try {
                return arg0.toURL();
            } catch (final MalformedURLException e) {
                throw new InvalidArgumentException(e);
            }
        } else if (arg0.getScheme().equals("ivo")) {
                try {
                    final Resource r=  reg.getResource(arg0);
                    // hope for now we've only got one service capability.
                    if (! (r instanceof Service)) {
                    	throw new InvalidArgumentException(arg0 + " is not a known type of service");
                    }
                    final Service s = (Service)r;
                    final URL u = findAccessURL(s);
                    return u != null ? u : findFirstAccessURL(s);
                } catch (final ServiceException e) {
                    throw new NotFoundException(e);
                }
        } else {
            throw new InvalidArgumentException("Don't know what to do with this: " + arg0);
        }    	
    }
    
    /** abstract method - subclasses should implement to find the corrent accessURL
     * in a given service.
     * @param s
     * @return
     */
    protected abstract URL findAccessURL(Service s) throws InvalidArgumentException ;

    /** Helper function - find the first url in the first interface in the first capability.
     * used as last resort - subtypes should provide their own mechanism to search for a url. 
     * @param arg0
     * @param s
     * @return
     * @throws InvalidArgumentException
     */
    protected URL findFirstAccessURL( final Service s)
            throws InvalidArgumentException {
        if (s.getCapabilities().length == 0 || s.getCapabilities()[0].getInterfaces().length == 0 || s.getCapabilities()[0].getInterfaces()[0].getAccessUrls().length == 0){
        	throw new InvalidArgumentException(s.getId() + " does not provide an access URL");
        }
        return s.getCapabilities()[0].getInterfaces()[0].getAccessUrls()[0].getValue();
    }
    
    /** Adds an option - safely handling case of nulls, options that already occur, urls ending with ? or &
     *  do we need to handle case of trailing '/' too?? or no filepath at all - just server name? may be this willnever happen 
     * @see org.astrogrid.acr.nvo.Cone#addOption(java.net.URL, java.lang.String, java.lang.String)
     */
    public final URL addOption(final URL arg0, final String arg1, final String arg2) throws InvalidArgumentException{
    	if (arg0 == null || arg1 == null ) {
    		throw new InvalidArgumentException("Nulls in " + arg0 + " " + arg1 + " " + arg2);
    	}  
    	try {
    	   // encoding the arg causes problems with 2 poorly implemented ssap services - my vote is to leave this as-is though.
    	final String encoded = arg2 == null ? "" : URLEncoder.encode(arg2,"UTF-8");
    	final String query = arg0.getQuery();
    	if (query == null) {
    		return new URL(arg0.toString() + "?" + arg1 + "=" + encoded);
    	} else if (query.length() == 0) { // case for trailing ?
    			return new URL(arg0.toString() + arg1 + "=" + encoded);
    	} else {
    		final String[] params = StringUtils.split(query,"&");
    		boolean found = false;
    		for (int i = 0; i < params.length; i++) {
    			if (params[i].startsWith(arg1)) {
    				found = true;
    				params[i] = arg1 + "=" + encoded;
    			}
    		}
    		// there are more efficient ways of doing this in some cases
    		// but rebuilding query string each time ensures that previous garbage 
    		// - e.g. extraneous & signs, are filtered out.
    		String newQuery = StringUtils.join(params,"&");
    		if (!found) {
    			newQuery += "&" + arg1 + "=" + encoded;
    		} 
    		final String preQuery = StringUtils.split(arg0.toString(),'?')[0];
    		return new URL(preQuery + '?' + newQuery);
    		
    	}
        } catch (final IOException e) {
            throw new InvalidArgumentException(e);
        } 
    }
    
    public final Map[] execute(final URL arg0) throws ServiceException {
		try {
			final SAXParserFactory newInstance = SAXParserFactory.newInstance();
			newInstance.setValidating(false);
            final XMLReader parser = newInstance.newSAXParser().getXMLReader();
        final TableContentHandler votHandler = new TableContentHandler(false);
        votHandler.setReadHrefTables(true);
        final StructureBuilder sb = newStructureBuilder();
        votHandler.setTableHandler(sb);
        parser.setContentHandler(votHandler);
        parser.parse(	arg0.toString());
        return sb.getResult();
		} catch (final Exception x) {
			throw new ServiceException(new ExceptionFormatter().format(x));
		}
	}


    
    public final Document executeVotable(final URL arg0) throws ServiceException {
        try {
            return XMLUtils.newDocument(arg0.toString());
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }
    /**
     * @see org.astrogrid.acr.nvo.Cone#getResults(java.net.URL)
     */
    public final Document getResults(final URL arg0) throws ServiceException {
    	return executeVotable(arg0);
    }

    public final void saveResults(final URL arg0, final URI arg1) throws InvalidArgumentException, ServiceException, SecurityException {
    	executeAndSave(arg0,arg1);
    }
    /**
     * @throws InvalidArgumentException
     * @throws ServiceException
     * @throws SecurityException
     * @see org.astrogrid.acr.nvo.Cone#saveResults(java.net.URL, java.net.URI)
     */
    public final void executeAndSave(final URL arg0, final URI arg1) throws InvalidArgumentException, ServiceException, SecurityException {
        if (arg1.getScheme().equals("ivo")) { // save to myspace - can optimize this
            try {
                ms.copyURLToContent(arg0,arg1);
            } catch (final NotFoundException e) {
                throw new InvalidArgumentException(e);
            } catch (final NotApplicableException e) {
                throw new InvalidArgumentException(e);
            }
        } else {
            OutputStream os = null;
            try {
                os = getOutputStream(arg1);
                final InputStream is = arg0.openStream();
                Piper.pipe(is,os);
            } catch (final FileNotFoundException e) {
            	throw new InvalidArgumentException(e);
            } catch (final MalformedURLException e) {
            	throw new InvalidArgumentException(e);            	
            } catch (final IOException e) {
                throw new ServiceException(e);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (final Exception e) {
                        logger.warn("Exception closing output stream",e);
                    }
                }
            }
        }
    }

	public int saveDatasets(final URL query, final URI root) throws SecurityException, ServiceException, InvalidArgumentException {
		try {
			final SAXParserFactory newInstance = SAXParserFactory.newInstance();
			newInstance.setValidating(false);
            final XMLReader parser = newInstance.newSAXParser().getXMLReader();
        final TableContentHandler votHandler = new TableContentHandler(false);
        votHandler.setReadHrefTables(true);
        final DatasetSaver saver = newDatasetSaver();
        saver.setRoot(root);
        votHandler.setTableHandler(saver);
        parser.setContentHandler(votHandler);
        parser.parse(	query.toString());
        return doSaveDatasets(saver);

		} catch (final SAXException x) {
			throw new ServiceException(new ExceptionFormatter().format(x));
		} catch (final ParserConfigurationException x) {
			throw new ServiceException(new ExceptionFormatter().format(x));
		} catch (final IOException x) {
			throw new ServiceException(new ExceptionFormatter().format(x));
		}		
	}

	/**
	 * @param saver
	 * @throws InvalidArgumentException
	 * @throws ServiceException
	 * @throws SecurityException
	 */
	private int doSaveDatasets(final DatasetSaver saver) throws InvalidArgumentException, ServiceException, SecurityException {
		int saved = 0;
		for (final Iterator i = saver.getResult().entrySet().iterator(); i.hasNext(); ) {
			saved++;
        	final Map.Entry entry = (Map.Entry)i.next();
        	final URL u = (URL)entry.getKey();
        	final URI location = (URI)entry.getValue();
        	if (location.getScheme().equals("ivo")) {
                try {
                    ms.copyURLToContent(u,location);
                } catch (final NotFoundException e) {
                    throw new InvalidArgumentException(e);
                } catch (final NotApplicableException e) {
                    throw new InvalidArgumentException(e);
                }        		
        	} else {
        	     OutputStream os = null;
                 try {
                	 os = getOutputStream(location);
                     final InputStream is = u.openStream();
                     Piper.pipe(is,os);
                 
                 } catch (final FileNotFoundException x) {
                	 throw new InvalidArgumentException(x);
                 } catch (final MalformedURLException x) {
                	 throw new InvalidArgumentException(x);
                 }  catch (final IOException e) {
                     throw new ServiceException(e);
				} finally {
                     if (os != null) {
                         try {
                             os.close();
                         } catch (final Exception e) {
                             logger.warn("Exception closing output stream",e);
                         }
                     }
                 }        		
        	}
        }
		return saved;
	}

	/** returns a stream for any non-myspace URI.
	 * @param location
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @todo move this elsewhere
	 */
	private OutputStream getOutputStream(final URI location) throws FileNotFoundException, IOException, MalformedURLException {
		OutputStream os;
		if (location.getScheme().equals("file")) { //FIXME - this code needs to be factored out and reused
			 os = new FileOutputStream(new File(location));
		 } else {
			 os = location.toURL().openConnection().getOutputStream();
		 }
		return os;
	}

	public int saveDatasetsSubset(final URL query, final URI root, final List rows) throws SecurityException, ServiceException, InvalidArgumentException {
		try {
			final SAXParserFactory newInstance = SAXParserFactory.newInstance();
			newInstance.setValidating(false);
            final XMLReader parser = newInstance.newSAXParser().getXMLReader();
        final TableContentHandler votHandler = new TableContentHandler(false);
        votHandler.setReadHrefTables(true);
        final DatasetSaver saver = newDatasetSaver();
        saver.setRoot(root);
        saver.setSubset(rows);
        votHandler.setTableHandler(saver);
        parser.setContentHandler(votHandler);
        parser.parse(	query.toString());
        return doSaveDatasets(saver);

		} catch (final SAXException x) {
			throw new ServiceException(new ExceptionFormatter().format(x));
		} catch (final ParserConfigurationException x) {
			throw new ServiceException(new ExceptionFormatter().format(x));
		} catch (final IOException x) {
			throw new ServiceException(new ExceptionFormatter().format(x));
		}		        

	}
	// factory method - allows subclasses to substitute their own implementaiton.
	protected StructureBuilder newStructureBuilder() {
		return new StructureBuilder();
	}
	/** table handler that builds an array of maps as a result of parsing a votable */
	protected static class StructureBuilder implements TableHandler {
		List result = new ArrayList();
		public Map[] getResult() {
			return (Map[])result.toArray(new Map[result.size()]);
		}
		protected String[] keys;
		int colCount;
		public void startTable(final StarTable t) throws SAXException {
		      DescribedValue qStatus = t.getParameterByName("Error");
		        if (qStatus != null) {
		            String message = qStatus.getInfo().getDescription();
		            if (message == null) {
		                message = qStatus.getValueAsString(1000);
		            }
		            throw new SAXException(message);
		        }
		        qStatus = t.getParameterByName("QUERY_STATUS");		        
		        if (qStatus != null && qStatus.getValue() != null &&  ! "OK".equalsIgnoreCase(qStatus.getValueAsString(1000))) {
	                    String message = qStatus.getInfo().getDescription();
	                    if (message == null) {
	                        message = qStatus.getValueAsString(1000);
	                    }
	                    throw new SAXException(message);		            
		        }
			colCount = t.getColumnCount();
			keys = new String[colCount];
			for (int col = 0; col < colCount; col++) {
				final ColumnInfo nfo = t.getColumnInfo(col);
				keys[col] = nfo.getUCD() != null ? nfo.getUCD() : nfo.getName();
			}
		}
		public void endTable() throws SAXException {
		    // does nothing
		}

		public void rowData(final Object[] cells) throws SAXException {
			final LinkedHashMap map = new LinkedHashMap();
			for (int col = 0; col < colCount; col++) {
				map.put(keys[col],cells[col]);
			}
			result.add(map);
		}

	}
	
	// factory method - allows subclasses of dalImpl to use their own impl
	protected DatasetSaver newDatasetSaver() {
		return new DatasetSaver();
	}
	/** table handler that saves linked data to disk */
	protected static class DatasetSaver implements TableHandler {
		public void setSubset(final List rows) {
			subset = true;
			this.rows = rows;
		}
		public void setRoot(final URI root)  {
			if (! root.toString().endsWith("/")) {
				this.root = URI.create(root.toString() + "/");
			} else {
				this.root = root;
			}
		}
		private  boolean subset;
		private List rows;
		private URI root;
		private int currentRow;
		private final Map result = new LinkedHashMap();
		
		private int urlIx = -1;
		private int formatIx = -1;
		
		public Map getResult() {
			return result;
		}
		int colCount;
		public void startTable(final StarTable t) throws SAXException {
			colCount = t.getColumnCount();
			for (int col = 0; col < colCount; col++) {
				final ColumnInfo nfo = t.getColumnInfo(col);
				if ("VOX:Image_AccessReference".equalsIgnoreCase(nfo.getUCD())) {
					urlIx = col;
				} else if ("VOX:Image_Format".equalsIgnoreCase(nfo.getUCD())) {
					formatIx = col;
				}
			}
		}
		public void endTable() throws SAXException {
		    //does nothing
		}

		public void rowData(final Object[] cells) throws SAXException {
			if (!subset || rows.contains(Integer.valueOf(currentRow))) {
					String format = null;
					if (formatIx > -1) {
						format = "." + StringUtils.substringAfterLast(cells[formatIx].toString(),"/");
					}
					try {
					result.put(new URL(cells[urlIx].toString()),
							URI.create(root.toString() + "data-" + currentRow + format )
							);
					} catch (final MalformedURLException e) {
						logger.warn("Failed to construct url",e); // @todo find a way to report this to client..
					}
			}
			currentRow++;
		}
	}
	
}


/* 
$Log: DALImpl.java,v $
Revision 1.22  2008/10/01 09:53:43  nw
removed final modifier to allow override.

Revision 1.21  2008/08/19 12:47:10  nw
findbugs fixes and improvements.

Revision 1.20  2008/08/07 11:52:36  nw
RESOLVED - bug 2767: VOExplore searching eso-ssap
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2767

Revision 1.19  2008/08/05 14:02:47  nw
configured the parser.

Revision 1.18  2008/01/25 07:53:25  nw
Complete - task 134: Upgrade to reg v1.0

Revision 1.17  2007/12/12 13:54:15  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.16  2007/10/23 12:20:56  nw
RESOLVED - bug 1837: Verify astroscope detects all error responses.
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1837

Revision 1.15  2007/10/08 08:29:02  nw
improved exception formatting

Revision 1.14  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.13  2007/06/18 17:02:24  nw
javadoc fixes.

Revision 1.12  2007/05/03 19:17:29  nw
made more error tolerant.

Revision 1.11  2007/03/08 17:44:03  nw
first draft of voexplorer

Revision 1.10  2007/01/29 16:45:08  nw
cleaned up imports.

Revision 1.9  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.8  2006/10/11 19:41:55  nw
refined interfaces on DAL andTables after some testing of usability.

Revision 1.7  2006/10/11 10:39:01  nw
enhanced dal support.

Revision 1.6  2006/08/31 21:34:46  nw
minor tweaks and doc fixes.

Revision 1.5  2006/08/15 10:13:50  nw
migrated from old to new registry models.

Revision 1.4  2006/06/15 16:34:04  nw
fixed built gotcha

Revision 1.3  2006/06/15 09:49:08  nw
improvements coming from unit testing

Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.48.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1  2005/10/17 16:02:45  nw
added siap and cone interfaces
 
*/