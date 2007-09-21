/*$Id: DALImpl.java,v 1.14 2007/09/21 16:35:15 nw Exp $
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
import org.astrogrid.io.Piper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.votable.TableContentHandler;
import uk.ac.starlink.votable.TableHandler;


/** Abstract class for implemntations of HTTP-GET based DAL standards
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Oct-2005
 *@todo move to new registry
 */
public abstract class DALImpl implements Dal{
    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(DALImpl.class);

    /** Construct a new DALImpl
     * 
     */
    public DALImpl(Registry reg, MyspaceInternal ms) {
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
    protected URL resolveEndpoint(URI arg0) throws InvalidArgumentException, NotFoundException {
    	if (arg0 == null || arg0.getScheme() == null) {
    		throw new InvalidArgumentException("No endpoint provided");
    	}
        if (arg0.getScheme().equals("http")) {
            try {
                return arg0.toURL();
            } catch (MalformedURLException e) {
                throw new InvalidArgumentException(e);
            }
        } else if (arg0.getScheme().equals("ivo")) {
                try {
                    Resource r=  reg.getResource(arg0);
                    // hope for now we've only got one service capability.
                    if (! (r instanceof Service)) {
                    	throw new InvalidArgumentException(arg0 + " is not a known type of service");
                    }
                    Service s = (Service)r;
                    if (s.getCapabilities().length == 0 || s.getCapabilities()[0].getInterfaces().length == 0 || s.getCapabilities()[0].getInterfaces()[0].getAccessUrls().length == 0){
                    	throw new InvalidArgumentException(arg0 + " does not provide an access URL");
                    }
                    return s.getCapabilities()[0].getInterfaces()[0].getAccessUrls()[0].getValue();
                } catch (ServiceException e) {
                    throw new NotFoundException(e);
                }
        } else {
            throw new InvalidArgumentException("Don't know what to do with this: " + arg0);
        }    	
    }
    
    /** Adds an option - safely handling case of nulls, options that already occur, urls ending with ? or &
     *  do we need to handle case of trailing '/' too?? or no filepath at all - just server name? may be this willnever happen 
     * @see org.astrogrid.acr.nvo.Cone#addOption(java.net.URL, java.lang.String, java.lang.String)
     */
    public final URL addOption(URL arg0, String arg1, String arg2) throws InvalidArgumentException{
    	if (arg0 == null || arg1 == null ) {
    		throw new InvalidArgumentException("Nulls in " + arg0 + " " + arg1 + " " + arg2);
    	}  
    	try {
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
    		String preQuery = StringUtils.split(arg0.toString(),'?')[0];
    		return new URL(preQuery + '?' + newQuery);
    		
    	}
        } catch (IOException e) {
            throw new InvalidArgumentException(e);
        } 
    }
    
    public final Map[] execute(URL arg0) throws ServiceException {
		try {
			XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        TableContentHandler votHandler = new TableContentHandler(false);
        votHandler.setReadHrefTables(true);
        StructureBuilder sb = newStructureBuilder();
        votHandler.setTableHandler(sb);
        parser.setContentHandler(votHandler);
        parser.parse(	arg0.toString());
        return sb.getResult();
		} catch (Exception x) {
			throw new ServiceException(x.getMessage());
		}
	}


    
    public final Document executeVotable(URL arg0) throws ServiceException {
        try {
            return XMLUtils.newDocument(arg0.toString());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    /**
     * @see org.astrogrid.acr.nvo.Cone#getResults(java.net.URL)
     */
    public final Document getResults(URL arg0) throws ServiceException {
    	return executeVotable(arg0);
    }

    public final void saveResults(URL arg0, URI arg1) throws InvalidArgumentException, ServiceException, SecurityException {
    	executeAndSave(arg0,arg1);
    }
    /**
     * @throws InvalidArgumentException
     * @throws ServiceException
     * @throws SecurityException
     * @see org.astrogrid.acr.nvo.Cone#saveResults(java.net.URL, java.net.URI)
     */
    public final void executeAndSave(URL arg0, URI arg1) throws InvalidArgumentException, ServiceException, SecurityException {
        if (arg1.getScheme().equals("ivo")) { // save to myspace - can optimize this
            try {
                ms.copyURLToContent(arg0,arg1);
            } catch (NotFoundException e) {
                throw new InvalidArgumentException(e);
            } catch (NotApplicableException e) {
                throw new InvalidArgumentException(e);
            }
        } else {
            OutputStream os = null;
            try {
                os = getOutputStream(arg1);
                InputStream is = arg0.openStream();
                Piper.pipe(is,os);
            } catch (FileNotFoundException e) {
            	throw new InvalidArgumentException(e);
            } catch (MalformedURLException e) {
            	throw new InvalidArgumentException(e);            	
            } catch (IOException e) {
                throw new ServiceException(e);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (Exception e) {
                        logger.warn("Exception closing output stream",e);
                    }
                }
            }
        }
    }

	public final int saveDatasets(URL query, URI root) throws SecurityException, ServiceException, InvalidArgumentException {
		try {
			XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        TableContentHandler votHandler = new TableContentHandler(false);
        votHandler.setReadHrefTables(true);
        DatasetSaver saver = newDatasetSaver();
        saver.setRoot(root);
        votHandler.setTableHandler(saver);
        parser.setContentHandler(votHandler);
        parser.parse(	query.toString());
        return doSaveDatasets(saver);

		} catch (SAXException x) {
			throw new ServiceException(x.getMessage());
		} catch (ParserConfigurationException x) {
			throw new ServiceException(x.getMessage());
		} catch (IOException x) {
			throw new ServiceException(x.getMessage());
		}		
	}

	/**
	 * @param saver
	 * @throws InvalidArgumentException
	 * @throws ServiceException
	 * @throws SecurityException
	 */
	private int doSaveDatasets(DatasetSaver saver) throws InvalidArgumentException, ServiceException, SecurityException {
		int saved = 0;
		for (Iterator i = saver.getResult().entrySet().iterator(); i.hasNext(); ) {
			saved++;
        	Map.Entry entry = (Map.Entry)i.next();
        	URL u = (URL)entry.getKey();
        	URI location = (URI)entry.getValue();
        	if (location.getScheme().equals("ivo")) {
                try {
                    ms.copyURLToContent(u,location);
                } catch (NotFoundException e) {
                    throw new InvalidArgumentException(e);
                } catch (NotApplicableException e) {
                    throw new InvalidArgumentException(e);
                }        		
        	} else {
        	     OutputStream os = null;
                 try {
                	 os = getOutputStream(location);
                     InputStream is = u.openStream();
                     Piper.pipe(is,os);
                 
                 } catch (FileNotFoundException x) {
                	 throw new InvalidArgumentException(x);
                 } catch (MalformedURLException x) {
                	 throw new InvalidArgumentException(x);
                 }  catch (IOException e) {
                     throw new ServiceException(e);
				} finally {
                     if (os != null) {
                         try {
                             os.close();
                         } catch (Exception e) {
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
	public static OutputStream getOutputStream(URI location) throws FileNotFoundException, IOException, MalformedURLException {
		OutputStream os;
		if (location.getScheme().equals("file")) { //FIXME - this code needs to be factored out and reused
			 os = new FileOutputStream(new File(location));
		 } else {
			 os = location.toURL().openConnection().getOutputStream();
		 }
		return os;
	}

	public final int saveDatasetsSubset(URL query, URI root, List rows) throws SecurityException, ServiceException, InvalidArgumentException {
		try {
			XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        TableContentHandler votHandler = new TableContentHandler(false);
        votHandler.setReadHrefTables(true);
        DatasetSaver saver = newDatasetSaver();
        saver.setRoot(root);
        saver.setSubset(rows);
        votHandler.setTableHandler(saver);
        parser.setContentHandler(votHandler);
        parser.parse(	query.toString());
        return doSaveDatasets(saver);

		} catch (SAXException x) {
			throw new ServiceException(x.getMessage());
		} catch (ParserConfigurationException x) {
			throw new ServiceException(x.getMessage());
		} catch (IOException x) {
			throw new ServiceException(x.getMessage());
		}		        

	}
	// factory method - allows subclasses to substitute their own implementaiton.
	protected StructureBuilder newStructureBuilder() {
		return new StructureBuilder();
	}
	protected static class StructureBuilder implements TableHandler {
		List result = new ArrayList();
		public Map[] getResult() {
			return (Map[])result.toArray(new Map[result.size()]);
		}
		protected String[] keys;
		int colCount;
		public void startTable(StarTable t) throws SAXException {
			colCount = t.getColumnCount();
			keys = new String[colCount];
			for (int col = 0; col < colCount; col++) {
				ColumnInfo nfo = t.getColumnInfo(col);
				keys[col] = nfo.getUCD() != null ? nfo.getUCD() : nfo.getName();
			}
		}
		public void endTable() throws SAXException {
		    // does nothing
		}

		public void rowData(Object[] cells) throws SAXException {
			LinkedHashMap map = new LinkedHashMap();
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
	
	protected static class DatasetSaver implements TableHandler {
		public void setSubset(List rows) {
			subset = true;
			this.rows = rows;
		}
		public void setRoot(URI root)  {
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
		private Map result = new LinkedHashMap();
		
		private int urlIx = -1;
		private int formatIx = -1;
		
		public Map getResult() {
			return result;
		}
		int colCount;
		public void startTable(StarTable t) throws SAXException {
			colCount = t.getColumnCount();
			for (int col = 0; col < colCount; col++) {
				ColumnInfo nfo = t.getColumnInfo(col);
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

		public void rowData(Object[] cells) throws SAXException {
			if (!subset || rows.contains(new Integer(currentRow))) {
					String format = null;
					if (formatIx > -1) {
						format = "." + StringUtils.substringAfterLast(cells[formatIx].toString(),"/");
					}
					try {
					result.put(new URL(cells[urlIx].toString()),
							URI.create(root.toString() + "data-" + currentRow + format )
							);
					} catch (MalformedURLException e) {
						logger.warn("Failed to construct url",e); // @todo find a way to report this to client..
					}
			}
			currentRow++;
		}
	}
	
}


/* 
$Log: DALImpl.java,v $
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