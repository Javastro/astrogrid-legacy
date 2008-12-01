/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceStreamParser;
import org.astrogrid.desktop.modules.system.ScheduledTask;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.WorkerProgressReporter;
import org.astrogrid.desktop.modules.ui.voexplorer.QuerySizerImpl;
import org.astrogrid.util.DomHelper;
import org.codehaus.xfire.util.STAXUtils;
import org.w3c.dom.Document;

/** Optimized registry client which uses an index caching strategy for xquerying.
 * 
 * <p/>
 * results aren't stored in bulk cache (as with previous version). instead, a preliminary query is done to get all indexes of 
 * results - this is stored in index cache.
 * then resources that are alreeady in cache are located.
 * then other resources in the index are queried for (and cached in resource cache)
 * finally, resources that aren't in cache, and can't be got from registry service are marked in cache as 'Missing'.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 13, 200810:10:06 PM
 */
public class IndexCachingRegistryImpl implements RegistryInternal{
    /** object used to indicate that registry doesn't have a resource associated with a particular ivorn */
    private static final MissingResource MISSING_RESOURCE = new MissingResource();

    /**
     * Logger for this class
     */
    protected static final Log logger = LogFactory
            .getLog(IndexCachingRegistryImpl.class);

    /** limit to number of resources to request in a single query - any larger lists will be split at this threshold */
    private final int splitQueryThreshold;

    /**
     * @param reg
     * @param endpoint
     * @param fallbackEndpoint
     * @param resource
     * @param document
     * @param index cache of result indexes.
     * @throws URISyntaxException
     */
    public IndexCachingRegistryImpl(final ExternalRegistryInternal reg,
            final Preference endpoint, final Preference fallbackEndpoint, 
             final int splitQueryThreshold
            ,final Ehcache resource,
            final Ehcache document,final Ehcache index) throws URISyntaxException {
        this.reg = reg;
        this.endpoint = endpoint;
        this.fallbackEndpoint =fallbackEndpoint;
        this.splitQueryThreshold = splitQueryThreshold;
        this.outputFactory = XMLOutputFactory.newInstance();    
        this.resourceCache  = resource;
        this.documentCache = document;

        checkEndpointPref(endpoint, "main");
        checkEndpointPref(fallbackEndpoint, "fallback");
        this.indexCache = index;
        expirer= new CachedResourceExpirer(reg,endpoint,resource,document);
        logger.info("Using registy index caching");
    }

    // map from URI to Document
    private final Ehcache documentCache;

    // subcomponent that handles the expiration process.
    private final ScheduledTask expirer;
    // map from xquery to URI[]
    private final Ehcache indexCache;
    private final XMLOutputFactory outputFactory;
    protected final Preference endpoint;
    protected final Preference fallbackEndpoint;
    protected final ExternalRegistryInternal reg;       
    // map from URI to Resource
    protected final Ehcache resourceCache;
    
    
    public Resource[] adqlsSearch(final String arg0) throws ServiceException,
    InvalidArgumentException {
        try {
            final Element cachedIndex = indexCache.get(arg0);
            if (cachedIndex != null) { // resources will be partialy in the cache already.
                final ResourceAccumulator rc = new ResourceAccumulator();
                final CacheFetcher fetcher = new CacheFetcher(rc);
                for (final URI index : (URI[])cachedIndex.getValue()) {
                    fetcher.fetchFromCache(index);
                }
                final Set<URI> misses = fetcher.getMisses();
                if (! misses.isEmpty()) { // query for the remainder
                    queryForResourceList(misses, rc);
                }                
                return rc.getResources();
            } else { // query registry
                final Resource[]res =  reg.adqlsSearch(getSystemRegistryEndpoint(),arg0);
                cacheResourcesAndIndex(arg0,res);
                return res;
            }
        } catch (final ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            return reg.adqlsSearch(getFallbackSystemRegistryEndpoint(),arg0);
        }
    }



    public Resource[] adqlxSearch(final Document arg0) throws ServiceException,
    InvalidArgumentException {
        try {
            final Element cachedIndex = indexCache.get(arg0);
            if (cachedIndex != null) { // resources will be partialy in the cache already.
                final ResourceAccumulator rc = new ResourceAccumulator();
                final CacheFetcher fetcher = new CacheFetcher(rc);
                for (final URI index : (URI[])cachedIndex.getValue()) {
                    fetcher.fetchFromCache(index);
                }
                final Set<URI> misses = fetcher.getMisses();
                if (! misses.isEmpty()) { // query for the remainder
                    queryForResourceList(misses, rc);
                }                
                return rc.getResources();
            } else { // query registry
                final Resource[]res =  reg.adqlxSearch(getSystemRegistryEndpoint(),arg0);
                cacheResourcesAndIndex(arg0,res);
                return res;
            }
        } catch (final ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            return reg.adqlxSearch(getFallbackSystemRegistryEndpoint(),arg0);
        }
    }
    
    /** bulk query to retrieve a list of resources.
     * @todo : need to add a variant which checks for cache here...
     * @param uriList list of resources ids to retrieve
     * @param resourceConsumer processor to call for each resource.
     * @throws ServiceException
     */
    public void consumeResourceList(final Collection<URI> uriList,
            final ResourceConsumer resourceConsumer) throws ServiceException {
        if (uriList.isEmpty()) {
            return; // done
        }
        resourceConsumer.estimatedSize(uriList.size());
        final CacheFetcher fetcher = new CacheFetcher(resourceConsumer);
        for (final URI uri : uriList) {
            fetcher.fetchFromCache(uri);
        }
        final Set<URI> misses = fetcher.getMisses();
        if (! misses.isEmpty()) {
            // now query for the remainder.
            queryForResourceList(misses, resourceConsumer);
        }
    }


    // slightly different behaviour compared to previous impl. - will cache results no matter whethe they come from main or fallback reg.
    // as is implemented in terms of super.xquerySearchStream
    // minor issue - dn't care for now.
    public void consumeXQuery(final String xquery, final ResourceConsumer resourceConsumer)
            throws ServiceException {
            logger.debug("consumeXQuery " + xquery);
            if (xquery == null) {
                throw new IllegalArgumentException("null query");
            }
            final Element cachedIndex = indexCache.get(xquery);
            final CacheFetcher fetcher = new CacheFetcher(resourceConsumer);
            if (cachedIndex != null) {
                logger.debug("found cached index");
                final URI[] indexes = (URI[])cachedIndex.getValue();
                resourceConsumer.estimatedSize(indexes.length);
                for (final URI index : indexes) {
                    fetcher.fetchFromCache(index);
                }
            } else { // need to query the registry to get the list of indexes
                logger.debug("Querying for index");
               final IndexStreamProcessor indexStreamProcessor = new IndexStreamProcessor(fetcher);
               final String indexQuery =  "<indexes>{( " + xquery.trim() + ")/identifier}</indexes>";               
               xquerySearchStream(indexQuery,indexStreamProcessor);
               // cache the query response for next time
               final URI[] indexes = indexStreamProcessor.getIndexes();
               indexCache.put(new Element(xquery,indexes));
               // tell the consumer (belatedly) how many resources to expect - they'll have seen any which are cached already.
               logger.debug("Index size " + indexes.length);
               resourceConsumer.estimatedSize(indexes.length);
            }
            final Set<URI> misses = fetcher.getMisses();          
            if (! misses.isEmpty()) { // query for the remainder
                logger.debug("Querying for remaining resources");
                queryForResourceList(misses, resourceConsumer);
            }
    }
    
    
    public void consumeXQueryReload(final String xquery, final ResourceConsumer processor)
            throws ServiceException {
        if (indexCache.isKeyInCache(xquery)) {
            // remove the resources (or missing resources) 
            final Element cachedIndex = indexCache.get(xquery);
            if (cachedIndex != null) {
                final URI[] indexes = (URI[])cachedIndex.getValue();
                for (final URI ivoid : indexes) {
                    resourceCache.remove(ivoid);
                }
            }
            // remove the cached index.
            indexCache.remove(xquery);
        }
        // now proceed as normal.
        consumeXQuery(xquery,processor);
    }
    
    public void consumeResourceListReload(final Collection<URI> ids,
            final ResourceConsumer resourceConsumer) throws ServiceException {
        // remove the resources (or missing resources) 
        for (final URI ivoid : ids) {
            resourceCache.remove(ivoid);
        }
        consumeResourceList(ids,resourceConsumer);
    }
    
    


    
    public final URI getSystemRegistryEndpoint() throws ServiceException {
        try {
            return new URI(endpoint.getValue());
        } catch (final URISyntaxException x) {
            throw new ServiceException("Misconfigured url",x);
        }               
    }


    public final URI getFallbackSystemRegistryEndpoint() throws ServiceException {
        try {
            return new URI(fallbackEndpoint.getValue());
        } catch (final URISyntaxException x) {
            throw new ServiceException("Misconfigured url",x);
        }
    }
    

    public RegistryService getIdentity() throws ServiceException {
        try {
            Element el = resourceCache.get(getSystemRegistryEndpoint());
            if (el != null && el.getValue() instanceof RegistryService) {
                return (RegistryService)el.getValue();
            } else {
                final RegistryService r = reg.getIdentity(getSystemRegistryEndpoint());
                el = new Element(getSystemRegistryEndpoint(),r);
                resourceCache.put(el);
                return r;
            }
        } catch (final ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            return reg.getIdentity(getFallbackSystemRegistryEndpoint());
        }
    }
    
    


    public Resource getResource(final URI arg0) throws NotFoundException, ServiceException {
        try {
            Element el = resourceCache.get(arg0);
            if (el != null && el.getValue() instanceof Resource) {
                return (Resource)el.getValue();
            } else {
                final Resource res = reg.getResource(getSystemRegistryEndpoint(),arg0);
                el = new Element(arg0,res);
                resourceCache.put(el);
                return res;
            }
        } catch (final ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            return reg.getResource(getFallbackSystemRegistryEndpoint(),arg0);
        }
    }


    public void getResourceStream(final URI ivorn, final StreamProcessor processor) throws ServiceException, NotFoundException {
        try {
            reg.getResourceStream(getSystemRegistryEndpoint(),ivorn,processor);
        } catch (final ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            reg.getResourceStream(getFallbackSystemRegistryEndpoint(),ivorn,processor);
        }
    }
    
    public Document getResourceXML(final URI ivorn) throws ServiceException, NotFoundException {
        try {
            final Element element = documentCache.get(ivorn);
            if (element != null) {
                return (Document)element.getValue();
            } else {
                final Document doc =  reg.getResourceXML(getSystemRegistryEndpoint(),ivorn);
                documentCache.put(new Element(ivorn,doc));
                return doc;
            }
        } catch (final ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            return reg.getResourceXML(getFallbackSystemRegistryEndpoint(),ivorn);
        }
    }
    



    public Resource[] keywordSearch(final String arg0, final boolean arg1)
    throws ServiceException {
        try {
            final Element cachedIndex = indexCache.get(arg0);
            if (cachedIndex != null) { // resources will be partialy in the cache already.
                final ResourceAccumulator rc = new ResourceAccumulator();
                final CacheFetcher fetcher = new CacheFetcher(rc);
                for (final URI index : (URI[])cachedIndex.getValue()) {
                    fetcher.fetchFromCache(index);
                }
                final Set<URI> misses = fetcher.getMisses();
                if (! misses.isEmpty()) { // query for the remainder
                    queryForResourceList(misses, rc);
                }                
                return rc.getResources();
            } else {    
                final Resource[] res =  reg.keywordSearch(getSystemRegistryEndpoint(),arg0,arg1);
                cacheResourcesAndIndex(arg0+arg1,res);
                return res;     
            }
        } catch (final ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            return reg.keywordSearch(getFallbackSystemRegistryEndpoint(),arg0,arg1);
        }
    }

public Resource[] xquerySearch(final String arg0) throws ServiceException {
    final ResourceAccumulator rc = new ResourceAccumulator();
    consumeXQuery(arg0,rc);
    return rc.getResources();        
}

public void xquerySearchSave(final String xquery, final File saveLocation) throws InvalidArgumentException, ServiceException {
        XMLStreamWriter writer = null;
        OutputStream os = null;
        try {
            os = FileUtils.openOutputStream(saveLocation);
            writer = outputFactory.createXMLStreamWriter(os);
            final WriterStreamProcessor proc = new WriterStreamProcessor(writer);
            xquerySearchStream(xquery,proc);
        } catch (final XMLStreamException x) {
            throw new InvalidArgumentException("Failed to open location for writing",x);
        } catch (final IOException x) {
            throw new InvalidArgumentException("Failed to open location for writing",x);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (final XMLStreamException ex) {
                    logger.warn("Exception while closing writer",ex);
                }
            }
            IOUtils.closeQuietly(os);            
        }
    }

    public void xquerySearchStream(final String xquery, final StreamProcessor processor) throws ServiceException {
        try {
            reg.xquerySearchStream(getSystemRegistryEndpoint(),xquery,processor);
        } catch (final ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            reg.xquerySearchStream(getFallbackSystemRegistryEndpoint(),xquery,processor);
        }
    }
    
    public Document xquerySearchXML(final String arg0) throws ServiceException {
        try {
            final Element element = documentCache.get(arg0);
            if (element != null) {
                return (Document)element.getValue();
            } else {
                final Document doc =  reg.xquerySearchXML(getSystemRegistryEndpoint(),arg0);
                documentCache.put(new Element(arg0,doc));
                return doc;
            }
        } catch (final ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            return reg.xquerySearchXML(getFallbackSystemRegistryEndpoint(),arg0);
        }
    }
 
// background worker interface.

    // BackgroundWorker interface - delegates to expirer class.
    public final void execute(final WorkerProgressReporter reporter) {
        this.expirer.execute(reporter);
    }
    
    public final String getName() {
        return this.expirer.getName();
    }
    public final long getPeriod() {
        return this.expirer.getPeriod();
    }

    public final Principal getPrincipal() {
        return this.expirer.getPrincipal();
    }
 
 //self-testing interface
    
    /** self tests that the registry is working */
    public Test getSelftest() {
        final TestSuite ts = new TestSuite("Registry tests");
        ts.addTest(new RegistryTest("Main registry service", reg, endpoint));
        ts.addTest(new RegistryTest("Fallback registry service", reg, fallbackEndpoint));
        ts.addTest(new TestCase("Registry caches") {
            @Override
            protected void runTest() throws Throwable {
                //assertEquals("Problem with the bulk cache", Status.STATUS_ALIVE,bulkCache.getStatus());
                assertEquals("Problem with the document cache",Status.STATUS_ALIVE,documentCache.getStatus());
                assertEquals("Problem with the resource cache",Status.STATUS_ALIVE,resourceCache.getStatus());    
                assertEquals("Problem with the index cache",Status.STATUS_ALIVE,indexCache.getStatus());
            }
        });
        return ts;
    }

    
    /** stuff each resource in the individual resource cache, and add an entry to the index cache
     * for the entire array of identifiers using <tt>indexKey</tt>
     * 
     *  this method is called by the non-xquery registry search methods (adql, keyword), typically with the original adql/keyword search paramters as the key.
     *  */
    private void cacheResourcesAndIndex(final Object indexKey,final Resource[] res) {
        final URI[] index = new URI[res.length];
        for (int i = 0; i < res.length; i++) {
            index[i] = res[i].getId();
            resourceCache.put(new Element(res[i].getId(),res[i]));            
        }
        indexCache.put(new Element(indexKey,index));
    }

    /**
     * Checks whether the given endpoint preference is one of the given
     * alternatives and warns if not.  It's possible that some other action
     * (popup? automatic silent reselection?) would be more appropriate here.
     */
    private void checkEndpointPref(final Preference endpoint1, final String regLabel) {
        final String value = endpoint1.getValue();
        if (! (endpoint1.getDefaultValue().equals(value) || Arrays.asList(endpoint1.getAlternatives()).contains(value))) {
            logger.warn("Non-recommended " + regLabel + " registry endpoint: " + value);
        }
    }

    
    /** retrieve a list of rsources frm the registry. (caching the results)
     * @param uriList list of resoources to retrieve. this method modifies the contents of this set.
     * @param resourceConsumer consumer that will process the results. (which may be returned in arbitreary order).
     * @throws ServiceException
     */
    private void queryForResourceList(final Set<URI> uriList,
            final ResourceConsumer resourceConsumer) throws ServiceException {
        final Iterator<URI> iterator = uriList.iterator();
        // take a copy of the set, so we don't get any concurrent modification exceptions from the iterator.
        final Set<URI> remainders = new HashSet<URI>(uriList);
        final ResourceListProcessor processor = new ResourceListProcessor(resourceConsumer, remainders);
        while(iterator.hasNext()) { // split a large query into a sequence of smaller ones.
            xquerySearchStream(mkListResourcesQuery(iterator),processor);
        }
        // check whether we've got any uri left - these are ones not present in the reg - mark them as unknown.
        for (final URI uri : remainders) {
            final Element element = new Element(uri,MISSING_RESOURCE);
            element.setTimeToLive(60 * 60); // just mark it missing for a short amount of time - 1 hour.           
            resourceCache.put(element);
            
        }                
    }

    /** build an xquery to retrieve a list of resources
     * alternative implementation method - allows to choose between different impls
     * @param iterator provides the uri to retrieve. Will consume some of the items in the iteraotr, but not necessarily all of them.
     * @return an xquery string.
     */    
    protected String mkListResourcesQuery(final Iterator<URI> iterator) {           
        if (true) {
            return mkListResourcesQueryUsingIdentifierClauses(iterator);
        } else {// alternative implementaiton - dunno which is best yet.
            return mkListResourcesQueryUsingIdentifierList(iterator);
        }
    }
    //unused. would have to see whether this was any faster.
    private String mkListResourcesQueryUsingIdentifierList( final Iterator<URI> iterator) {
        final StringBuilder sb = new StringBuilder();
        sb.append("//vor:Resource[not (@status = 'inactive' or @status= 'deleted') and (( ");
        for (int i = 0; i < splitQueryThreshold && iterator.hasNext(); i++) {       
            final URI u =  iterator.next();
            sb.append("'")
            .append(u)
            .append("'");
            if (iterator.hasNext() && i+1 < splitQueryThreshold) {
                sb.append(" , ");
            }        
        }
        sb.append(") = identifier)]");
        final String xquery = sb.toString();
        return xquery;
    }


    protected String mkListResourcesQueryUsingIdentifierClauses(final Iterator<URI> iterator) {
        final StringBuilder sb = new StringBuilder();
        //sb.append("//vor:Resource[not (@status = 'inactive' or @status= 'deleted') and ( "); // already know we're not getting inactive or deleted ones, don't we??
        // hmm maybe not - is it possible for there to be more than one resource with the same id, but with different statuses.
        sb.append("//vor:Resource[not (@status = 'inactive' or @status= 'deleted') and ( ");
        for (int i = 0; i < splitQueryThreshold && iterator.hasNext(); i++) {  
            sb.append("identifier=");
                
            final URI u =  iterator.next();
            sb.append("'")
            .append(u)
            .append("'");
            if (iterator.hasNext() && i+1 < splitQueryThreshold) {
                sb.append(" or ");
            }        
        }
        sb.append(")]");
        final String xquery = sb.toString();
        return xquery;
    }
    
    /** processor that just copies input to a supplied stream writer */
    public static class WriterStreamProcessor implements StreamProcessor {
        public WriterStreamProcessor(final XMLStreamWriter os ) {
            this.os = os;
        }

        private final XMLStreamWriter os;
        public void process(final XMLStreamReader r) throws XMLStreamException  {
            STAXUtils.copy(r,this.os);
        }
    }

    /** class that fetches items from the cache by index
     * the retrieved resources are passed to the argument ResourceProcessor,.    
     * cache misses are accumulated into a list */
    private final  class CacheFetcher {
       
        /** 
         * @param resourceConsumer a consumer that this class should fetch resources for.
         */
        public CacheFetcher(final ResourceConsumer resourceConsumer) {
            this.resourceConsumer = resourceConsumer;
        }
        private final Set<URI> misses = new HashSet<URI>();
        private final ResourceConsumer resourceConsumer;
        /** retrieve the set of missed resoources - i.e. those not present in the cache */
        public final Set<URI> getMisses() {
            return this.misses;
        }

        /** access the consumer that this fetcher should pass resources onto */
        public final ResourceConsumer getResourceConsumer() {
            return this.resourceConsumer;
        }

        /** call this repeatedly to process a list of indexes
         * 
         * @param index a resource id. if in the cache, will call ResourceProcessor, else is added to the 
         * misses list.
         */
        private void fetchFromCache(final URI index) {
            final Element element = resourceCache.get(index);
            if (element == null) {
                misses.add(index);
            } else if (element.getValue() instanceof MissingResource) {
                // it's an index which we know the registry lacks.
                //do nothing - don't add to missing list, and don't pass to resource consumer.
            } else {
                resourceConsumer.process((Resource)element.getValue()); // no typesafety checking here..
            }
            
        }
    }


    /** streamprocessor that parses a list of indexes and passed each one to the associated cache fetcher 
     * designed to operate with a query build by {@link #mkIndexQuery}
     * */
    private  static final class IndexStreamProcessor implements StreamProcessor {

        /**
         * @param fetcher
         */
        public IndexStreamProcessor(final CacheFetcher fetcher) {
            this.fetcher = fetcher;
        }
        private final CacheFetcher fetcher;
        
        private final List<URI> indexes = new ArrayList<URI>();

        /** access the indexes returned by this query */
        public final URI[] getIndexes() {
            return this.indexes.toArray(new URI[indexes.size()]);
        }

        public void process(final XMLStreamReader r) throws XMLStreamException {            
            while (r.hasNext()) {
                r.next();
                if (r.isStartElement() && "identifier".equals(r.getLocalName())) {
                    try {
                        final URI u = new URI(StringUtils.trim(r.getElementText()));
                        indexes.add(u);
                        fetcher.fetchFromCache(u);
                    } catch (final URISyntaxException e) {
                        logger.warn("Failed to build uri from index response",e);
                    }
                }
            }
        }
    }
    /** indicator class that represents a missing resource */
    private static class MissingResource implements Serializable{

        /**
         * 
         */
        private static final long serialVersionUID = -5968284172785042470L;
        
    }

    /** Self test for registry. */
    private static class RegistryTest extends TestCase implements RegistryInternal.StreamProcessor {
        RegistryTest(final String name, final ExternalRegistryInternal reg, final Preference endpoint) {
            super(name);
            this.reg = reg;
            this.endpoint = endpoint;
        }
        private final Preference endpoint;
        private final ExternalRegistryInternal reg;
        public void process(final XMLStreamReader r) {
        }
        @Override
        protected void runTest() {
            URI endpointUri;
            try {
                endpointUri = new URI(endpoint.getValue());
            } catch (final URISyntaxException x) {
                logger.error("Misconfigured registry endpoint",x);
                fail("Misconfigured  registry endpoint");
                endpointUri = null;  // keep compiler happy
            }
            try {
                endpointUri.toURL().openConnection().connect();
            } catch (final MalformedURLException x) {
                logger.error("Misconfigured registry endpoint",x);
                fail("Misconfigured registry endpoint");
            } catch (final IOException x) {
                logger.error("Failed to connect to registry service",x);
                fail("Failed to connect to registry service");
            }
            try {
                final RegistryService serv = reg.getIdentity(endpointUri);
                assertNotNull("No registry identity returned",serv);
            } catch (final ServiceException x) {
                fail("Failed to get registry identity");
            }
            try {
                final Document doc = reg.xquerySearchXML(endpointUri, QuerySizerImpl.constructSizingQuery(QuerySizerImpl.ALL_QUERY));
                assertNotNull("No response returned from xquery",doc);
                // in a normal junit test, it'd be more sensible to use XMLAssert for the following.
                // however, this isn't bundled with the final app - so just vanilla junit.
                final String docString = DomHelper.DocumentToString(doc);
                assertTrue("xquery didn't return expected response",StringUtils.contains(docString,"<size>"));
            } catch (final ServiceException x) {
                logger.error("Failed to xquery registry",x);
                fail("Failed to xquery registry");
            }
            // could test 'getResource' here too - but I think this is enough.
        }
    }

    /** resource processor that just accumulates the resources into a list */
    private static final class  ResourceAccumulator implements ResourceConsumer {
        private final ArrayList resources = new ArrayList();
        public void estimatedSize(final int i) {
            resources.ensureCapacity(i);
        }
        public final Resource[] getResources() {
            return (Resource[])this.resources.toArray(new Resource[resources.size()]);
        }
        public void process(final Resource s) {
            resources.add(s);
        }
    }

    /** stream processor that queries for a set of resources, caches each, and passes each to 
     * an argument resourceProcessor
     * @author Noel.Winstanley@manchester.ac.uk
     * @since May 14, 20084:01:24 PM
     */
    private final class ResourceListProcessor implements StreamProcessor {

        /**
         * @param resourceConsumer onsimer to pass resources onto
         * @param uriList list of resources we expect to see. will be modified in-place - resources removed as they're encountered.
         */
        public ResourceListProcessor(final ResourceConsumer resourceConsumer, final Set<URI> uriList) {
            this.resourceConsumer = resourceConsumer;
            expectedResources = uriList;
        }
        private final Set<URI> expectedResources ;
        private final ResourceConsumer resourceConsumer;

        
        public void process(final XMLStreamReader r)  {
            final ResourceStreamParser parser = new ResourceStreamParser(r);
            while(parser.hasNext()) {
                final Resource resource = (Resource)parser.next();
                final URI id = resource.getId();
                resourceCache.put(new Element(id,resource));
                expectedResources.remove(id);
                resourceConsumer.process(resource);
            }                   
        }
    }
    
}
