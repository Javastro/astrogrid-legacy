/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.File;
import java.net.URI;

import javax.xml.stream.XMLStreamReader;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.util.Selftest;
import org.w3c.dom.Document;

/** Internal extension interface - provides more efficient access to the data.
 * 
 *  * Provides streaming variants of the standard query methods - allows 
 * in-vm users to stream results direct to consumers.
 * 
 * thee stream methods are uncached - it's up to the client to cache the results
 * @author Noel Winstanley
 * @since Jul 26, 200611:41:37 PM
 */
public interface RegistryInternal extends org.astrogrid.acr.ivoa.Registry, Selftest {

	/** perform an xquery, and consume results with the parameter processor 
	 * the stream processor is responsible for parsing and caching as needed.
	 *  but the query may return any kind of result document.
	 * */
	public void xquerySearchStream(String xquery, StreamProcessor processor) throws ServiceException;

	   /** perform an xquery, and consume results with the parameter processor
	    * here the registry impl takes care of parsing and caching, hence the query must 
	    * return a sequence of resource documents.
	    *  */
    public void xquerySearchStream(String xquery, ResourceProcessor processor) throws ServiceException;

    public static interface ResourceProcessor {
        public void process(Resource s);
    }
	
	
	//deliberately restruct to saviing to local file - otherwise introduces all kinds of deps, login requirements, etc.
	//hard to know how to add as a public interface.. need to find way to xml-serialize File
	public void xquerySearchSave(String xquery, File saveLocation)  throws InvalidArgumentException, ServiceException;
 
	public void getResourceStream(URI ivorn, StreamProcessor processor) throws ServiceException, NotFoundException;
	
	public Document getResourceXML(URI ivorn) throws ServiceException, NotFoundException;
	
	public void keywordSearchStream(String keywords, boolean orValues, StreamProcessor processor) throws ServiceException;
	
	void adqlxSearchStream(Document adqlx, boolean identifiersOnly,
			StreamProcessor proc) throws ServiceException, InvalidArgumentException;

	void getIdentityStream(StreamProcessor proc)
			throws ServiceException;
	
	/** interface to a component that consumes a response stream */
	public static interface StreamProcessor {
		void process(XMLStreamReader r) throws Exception;
		
	}
	
}
