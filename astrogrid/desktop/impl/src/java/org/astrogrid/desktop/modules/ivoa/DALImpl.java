/*$Id: DALImpl.java,v 1.28 2009/06/19 16:40:23 mbt Exp $
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
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.Selectors;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Dal;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.actions.BulkCopyWorker;
import org.astrogrid.desktop.modules.ui.actions.CopyAsCommand;
import org.astrogrid.desktop.modules.ui.actions.CopyCommand;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.scope.DalProtocolException;
import org.astrogrid.desktop.modules.ui.scope.VotableContentHandler;
import org.astrogrid.desktop.modules.ui.scope.VotableContentHandler.VotableHandler;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.DescribedValue;
import uk.ac.starlink.table.StarTable;


/** Abstract class for implemntations of HTTP-GET based DAL standards
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Oct-2005
 */
public abstract class DALImpl implements Dal{
    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(DALImpl.class);

    /** Construct a new DALImpl
     * @param context @todo
     * @param context 
     * 
     */
    public DALImpl(final Registry reg, final FileSystemManager vfs, final UIContext context) {
        super();
        this.reg = reg;
        this.vfs = vfs;
        this.context=context;
    }
  	protected final Registry reg;
    protected final FileSystemManager vfs;
    protected  UIContext context;


    /** utility method for subclasses to use to resolve an abstract service id to url endpoint
     * @TODO this will be involved in handling multiple similar capabilities in the same resource.
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
    private URL findFirstAccessURL( final Service s)
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

    /** Formats a floating point value as a string for use as a URL query parameter value.
     *  It's prudent to avoid use of exponential format more aggressively than Double.toString()
     *  does since some servers don't like it.  It's not clear from the DAL standards what formats
     *  servers ought to accept though.
     */
    public String formatDouble(double value) {
        return formatDouble(value, 16, 32);
    }

    /** Formats a floating point value.
     *  It will be done in fixed point format if it can be done within the
     *  given number of characters, else exponential notation.
     * @param   value  value
     * @param   nsf   number of significant figures
     * @param   maxleng  maximum length of string - if longer than this,
     *          will return to exponential notation
     * @return  string representation of value
     */
    private String formatDouble(double value, int nsf, int maxleng) {
        String sval = Double.toString( value );
        if ( sval.indexOf( 'E' ) < 0 ) {
            return sval;
        }
        else {
            int log10 = log10( value );
            StringBuffer fbuf = new StringBuffer( "0." );
            for ( int i = 0; i < nsf - log10; i++ ) {
                fbuf.append( '0' );
            }
            String fval =
                new DecimalFormat( fbuf.toString() ).format( value );
            fval = fval.replaceFirst( "0+$", "" );
            if ( fval.length() <= maxleng ) {
                return fval;
            }
            else {
                return sval;
            }
        }
    }

    /**
     * Returns approximate logarithm to base 10 of the value.
     *
     * @param  value  value
     * @return  approximate log to base 10
     */
    private int log10( double value ) {
        return (int) Math.round( Math.log( Math.abs( value ) ) / Math.log( 10 ) );
    }
    
    public final Map[] execute(final URL arg0) throws ServiceException {
        try {
            final XMLReader parser = createParser();
            final VotableContentHandler votHandler = new VotableContentHandler(false);
            votHandler.setReadHrefTables(true);
            final StructureBuilder sb = newStructureBuilder();
            votHandler.setVotableHandler(sb);
            parser.setContentHandler(votHandler);
            parser.parse(	arg0.toString());
            return sb.getResult();
        } catch (final Exception x) {
            throw new ServiceException(new ExceptionFormatter().format(x));
        }
    }

    /** Create a simple parser suitable for slurping up votables.
     * @return
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static XMLReader createParser() throws SAXException,
            ParserConfigurationException {
        final SAXParserFactory newInstance = SAXParserFactory.newInstance();
        newInstance.setValidating(false);
        final XMLReader parser = newInstance.newSAXParser().getXMLReader();
        // setup an entity resolve to ignore system dtd references,
        // as we know we're getting votable, and referring to off votable dtd's that are off and away somewhere is a
        // cause of odd exceptions.
        parser.setEntityResolver(new EntityResolver() {

            public InputSource resolveEntity(final String publicId,
                    final String systemId) throws SAXException, IOException {
                return new InputSource(new StringReader(""));
            }
        });        
        return parser;
    }

    public final Document executeVotable(final URL arg0) throws ServiceException {
        final File f = downloadAndValidate(arg0);
        try {
            return XMLUtils.newDocument(f.toURL().toString());
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }
    
    /** download a remote votable, validate, and then provide access to the cached file
     * 
     * @param arg0 the url to fetch.
     * @return input stream of cached content
     * @throws IOException 
     */
    private File downloadAndValidate(final URL arg0) throws ServiceException{
        try {
            // create a temporary file.
            final File f = File.createTempFile("dalCached",".vot");
            f.deleteOnExit();
            //fetch URL down to this file.
            FileUtils.copyURLToFile(arg0,f);
            //now parse and validate this.
            final XMLReader parser = createParser();
            final VotableContentHandler votHandler = new VotableContentHandler(false);
            votHandler.setReadHrefTables(true);
            final StructureBuilder sb = newStructureBuilder();
            votHandler.setVotableHandler(sb);
            parser.setContentHandler(votHandler);
            parser.parse(f.toURL().toString());
            // ok. now return the temporary file.
            return f;
        } catch (final Exception x) {
            throw new ServiceException(new ExceptionFormatter().format(x));
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

    public final void executeAndSave(final URL arg0, final URI arg1) throws InvalidArgumentException, ServiceException, SecurityException {
        final File f = downloadAndValidate(arg0); // even though I can short-cut to myspace, still check the data is valid first.
        try {
            final FileObject target = vfs.resolveFile(arg1.toString());
            FileObject source;
            if(target.getName().getScheme().equals("ivo") || target.getName().getScheme().equals("vos")) { // optimzation for myspace.
                source = vfs.resolveFile(arg0.toString());            
            } else {
                source = vfs.resolveFile(f.toString());
            }
            target.copyFrom(source,Selectors.SELECT_SELF);
        } catch (final FileSystemException e) {
            throw new ServiceException(e);
        }

        
//        if (arg1.getScheme().equals("ivo")) { // save to myspace - can optimize this
//            try {
//                ms.copyURLToContent(f.toURL(),arg1);
//            } catch (final NotFoundException e) {
//                throw new InvalidArgumentException(e);
//            } catch (final NotApplicableException e) {
//                throw new InvalidArgumentException(e);
//            } catch (final MalformedURLException x) {
//                throw new ServiceException(x);
//            }
//        } else {
//            OutputStream os = null;
//            try {
//                os = getOutputStream(arg1);
//                final InputStream is = FileUtils.openInputStream(f);
//                IOUtils.copy(is,os);
//            } catch (final FileNotFoundException e) {
//            	throw new InvalidArgumentException(e);
//            } catch (final MalformedURLException e) {
//            	throw new InvalidArgumentException(e);            	
//            } catch (final IOException e) {
//                throw new ServiceException(e);
//            } finally {
//                IOUtils.closeQuietly(os);               
//            }
//        }
    }
    
    public int saveDatasets(final URL query, final URI root) throws SecurityException, ServiceException, InvalidArgumentException {
        try {
            final XMLReader parser = createParser();
            final VotableContentHandler votHandler = new VotableContentHandler(false);
            votHandler.setReadHrefTables(true);
            final DatasetSaver saver = newDatasetSaver();
            votHandler.setVotableHandler(saver);
            parser.setContentHandler(votHandler);
            parser.parse(query.toString());
            return doSaveDatasets(saver.getResult(),root);
        } catch (final SAXException x) {
            throw new ServiceException(new ExceptionFormatter().format(x));
        } catch (final ParserConfigurationException x) {
            throw new ServiceException(new ExceptionFormatter().format(x));
        } catch (final IOException x) {
            throw new ServiceException(new ExceptionFormatter().format(x));
        }			
    }

	/**  extension point that subclasses can override.
     * @return
     */
    protected DatasetSaver newDatasetSaver() {
        return new DatasetSaver();
    }

    /** Actually performs the download / save of the require datasets.
     * @param cmds list of commands - copy source and destination filename
     * @param destDir  directory to save results to
    
	 */
    private int doSaveDatasets(final List<CopyAsCommand> cmds, final URI destDir) throws InvalidArgumentException, ServiceException, SecurityException {
        final BulkCopyWorker copier = new BulkCopyWorker(vfs,context,destDir,cmds.toArray(new CopyCommand[cmds.size()]));
        // usually you just start() a coopier and forget about it.
        // however, here we want to block on it (I think), 
        // yet still have it display to UI, appear in tasklist, etc.
        //copier.
        // do I need to do this?? 
        //copier.getControl().setPrincipal()
        copier.start();
        // block for 'construct()' to complete..
        try {
            final CopyCommand[] results = (CopyCommand[])copier.get(); //blocks
            int count = 0;
            for (final CopyCommand c : results) {
                if (!c.failed()) {
                    count++;
                }
            }
            return count;
        } catch (final InterruptedException x) {
            throw new ServiceException(x);
        } catch (final InvocationTargetException x) {
          throw new ServiceException(x.getCause());
        }
        
//        int saved = 0;
//        for (final Map.Entry<URL,URI> entry : saver.getResult().entrySet()) {
//            saved++;
//            final URL u = entry.getKey();
//            final URI location = entry.getValue();
//
//            if (location.getScheme().equals("ivo")) {
//                try {
//                    ms.copyURLToContent(u,location);
//                } catch (final NotFoundException e) {
//                    throw new InvalidArgumentException(e);
//                } catch (final NotApplicableException e) {
//                    throw new InvalidArgumentException(e);
//                }        		
//            } else {
//                OutputStream os = null;
//                InputStream is = null;
//                try {
//                    os = getOutputStream(location);
//                    is = u.openStream();
//                    IOUtils.copy(is,os);
//                } catch (final FileNotFoundException x) {
//                    throw new InvalidArgumentException(x);
//                } catch (final MalformedURLException x) {
//                    throw new InvalidArgumentException(x);
//                }  catch (final IOException e) {
//                    throw new ServiceException(e);
//                } finally {
//                    IOUtils.closeQuietly(os);
//                    IOUtils.closeQuietly(is);
//                }        		
//            }
//        }
//        return saved;
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
		if (location.getScheme().equals("file")) { 
			 os = FileUtils.openOutputStream(new File(location));
		 } else {
			 os = location.toURL().openConnection().getOutputStream();
		 }
		return os;
	}

	public int saveDatasetsSubset(final URL query, final URI root, final List rows) throws SecurityException, ServiceException, InvalidArgumentException {		
        try {
            final XMLReader parser = createParser();
            final VotableContentHandler votHandler = new VotableContentHandler(false);
            votHandler.setReadHrefTables(true);
            final DatasetSaver saver = newDatasetSaver();
            saver.setSubset(rows);
            votHandler.setVotableHandler(saver);
            parser.setContentHandler(votHandler);
            parser.parse(   query.toString());
            return doSaveDatasets(saver.getResult(),root);
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
	
	/** basic table handler which parses for errors -
	 * forces all subclasses to be votableHandler, which means
	 * thay they need to consider all forms that errrors can be reported in*/
	public abstract static class BasicErrorChecker implements VotableHandler {

	    /** check a star table for parameters called 'Error' or 'Query_STARTUS'
	     * 
	     *  this method should be called from 'startTable'*/
	    public void checkStarTableErrors(final StarTable starTable) throws SAXException {
	        DescribedValue qStatus = starTable.getParameterByName("Error");
	        String message;
	        if (qStatus != null) {
	            
	            message = qStatus.getInfo().getDescription();
	            if (message == null) {
	                message = qStatus.getValueAsString(1000);
	            }
	            throw new DalProtocolException.ERROR(message);
	        }
	        qStatus = starTable.getParameterByName("QUERY_STATUS");	        
	        if (qStatus != null && qStatus.getValue() != null &&  ! "OK".equalsIgnoreCase(qStatus.getValueAsString(1000))) {
	            message = qStatus.getInfo().getDescription();
	            if (message == null) {
	                message = qStatus.getValueAsString(1000);
	            }
	            throw new DalProtocolException.QUERY_STATUS(message);
	        }	        
	    }
	    
	 // methods for inspecting votable content outside tables.
	    /** check for a info field called 'Error' or querystatus != OK */
        public void info(final String name, final String value, final String content)
                throws SAXException {
               checkForError(name,value,content);
               checkForQueryStatus(name,value,content);
        }
        /** check for an external param called 'Error', or querystatus != OK */
        public void param(final String name, final String value, final String description)
                throws SAXException {
           checkForError(name,value,description);
           checkForQueryStatus(name,value,description);           
        }
        
        private void checkForError(final String name,final String value,final String description) throws DalProtocolException {
            if ("error".equalsIgnoreCase(name)) {
                final String message = description != null ? description : value;
                throw new DalProtocolException.ERROR(message);
            }
        }
        
        private void checkForQueryStatus(final String name,final String value,final String description) throws DalProtocolException {
            if ("query_status".equalsIgnoreCase(name) && ! "OK".equalsIgnoreCase(value)) {
                final String message = description != null ? description : value;
                throw new DalProtocolException.QUERY_STATUS(message);
            }
        }        
  
        /** hook called when a new resource is encountered */
	    public void resource(final String name, final String id, final String type)
	            throws SAXException {
	        // unused in this impl        
	    }

	}
	
	
	/** table handler that builds an array of maps as a result of parsing a votable */
	protected static class StructureBuilder extends BasicErrorChecker{
		List<LinkedHashMap<String, Object>> result = new ArrayList<LinkedHashMap<String, Object>>();
		public Map[] getResult() {
			return result.toArray(new Map[result.size()]);
		}
		protected String[] keys = null;
		int colCount;
		public void startTable(final StarTable t) throws SAXException {
		    checkStarTableErrors(t);
			colCount = t.getColumnCount();
			keys = new String[colCount];
			for (int col = 0; col < colCount; col++) {
				final ColumnInfo nfo = t.getColumnInfo(col);
				keys[col] = StringUtils.isNotBlank(nfo.getUCD()) ? nfo.getUCD() : nfo.getName();
			}
		}
		public void endTable() throws SAXException {
		    // does nothing
		}

		public void rowData(final Object[] cells) throws SAXException {
			final LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			for (int col = 0; col < colCount; col++) {
				map.put(keys[col],cells[col]);
			}
			result.add(map);
		}

	}
	
}


/* 
$Log: DALImpl.java,v $
Revision 1.28  2009/06/19 16:40:23  mbt
Avoid exponential format when talking to DAL services - bugzilla 2938

Revision 1.27  2009/04/06 11:43:21  nw
Complete - taskConvert all to generics.

Incomplete - taskVOSpace VFS integration

Revision 1.26  2009/03/04 18:43:09  nw
Complete - taskMove DAL over to VFS

Revision 1.25  2009/02/27 17:16:58  nw
found source of spurious votable dtd errors.

Revision 1.24  2008/12/03 19:40:56  nw
Complete - taskDAL: add error detections and parsing improvements as used in astroscope retrievers.

Revision 1.23  2008/12/01 23:31:49  nw
Complete - taskDAL: add error detections and parsing improvements as used in astroscope retrievers.

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
