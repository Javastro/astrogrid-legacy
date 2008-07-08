/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.File;
import java.net.URI;
import java.util.Collection;

import javax.xml.stream.XMLStreamReader;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.ScheduledTask;
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
public interface RegistryInternal extends org.astrogrid.acr.ivoa.Registry, Selftest, ScheduledTask {
    /** interface to a component that can process a stream of resources */
    public static interface ResourceConsumer {

        /**depending on usage, this method <b>might</b> be called before any call to process() to 
         * inform the consumer how many resources to expect, or it <b> might</b> be called at somtime 
         * between calls to process() 
         * @param i a best-guess of the number of resources that will be passed. may be incorrect.
         */
        public void estimatedSize(int i);
        /** called for each resource in the stream */
        public void process( Resource s);

    }
    

    /** bulk query to retrieve a list of resources, caching the result
     * @param uriList list of resources ids to retrieve
     * @param resourceConsumer processor to call for each resource.
     * @throws ServiceException
     */
    public void consumeResourceList(Collection<URI> uriList,
            ResourceConsumer resourceConsumer) throws ServiceException ;
    
    
    /** bulk query to retrieve a list of resources, caching the result
     * @param uriList list of resources ids to retrieve
     * 
     *      * this method forces a search of the registry -so forcing a 'refresh' by ignoring any previusy cached results.
     * @param resourceConsumer processor to call for each resource.
     * @throws ServiceException

     */
    public void consumeResourceListReload(Collection<URI> ids,
            ResourceConsumer resourceConsumer) throws ServiceException ;
    
	   /** perform an xquery, and consume results with the parameter processor
	    * here the registry impl takes care of parsing and caching, hence the query must 
	    * return a sequence of resource documents.
	    *  */
    public void consumeXQuery(String xquery, ResourceConsumer processor) throws ServiceException;

    
    /** perform an xquery, and consume results with the parameter processor
     * here the registry impl takes care of parsing and caching, hence the query must 
     * return a sequence of resource documents.
     * this method forces a search of the registry -so forcing a 'refresh' by ignoring any previusy cached results.
     *  */
 public void consumeXQueryReload(String xquery, ResourceConsumer processor) throws ServiceException;

    	

// more low-level interface - to raw xml
	/** interface to a component that consumes a response stream */
	public static interface StreamProcessor {
		void process(XMLStreamReader r) throws Exception;
		
	}
	
    /** query to retrieve a single resource,
     * no caching is performed
     * 
     * @param ivorn the id of the resource to retrieve.
     * @param processor processor to call for this resource
     * @throws ServiceException 
     * @throws NotFoundException
     */
    public void getResourceStream(URI ivorn, StreamProcessor processor) throws ServiceException, NotFoundException;
    
    
	
    /** perform an xquery, and consume results with the parameter processor 
     * the stream processor is responsible for parsing and caching as needed.
     *  but the query may return any kind of result document.
     * */
    public void xquerySearchStream(String xquery, StreamProcessor processor) throws ServiceException;
    
    //deliberately restruct to saviing to local file - otherwise introduces all kinds of deps, login requirements, etc.
    //hard to know how to add as a public interface.. need to find way to xml-serialize File
    public void xquerySearchSave(String xquery, File saveLocation)  throws InvalidArgumentException, ServiceException;
 
    public Document getResourceXML(URI ivorn) throws ServiceException, NotFoundException;


    
	
}
