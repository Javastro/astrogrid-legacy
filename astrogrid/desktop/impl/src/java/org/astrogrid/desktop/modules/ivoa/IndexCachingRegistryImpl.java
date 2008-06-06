/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamReader;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceStreamParser;
import org.astrogrid.desktop.modules.system.ScheduledTask;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.WorkerProgressReporter;
import org.w3c.dom.Document;

/** Alternate implementation of registry which uses a different caching strategy for xquerying.
 * 
 * 
 * results aren't stored in bulk cache. instead, a preliminary query is done to get all indexes of 
 * results - this is stored in index cache.
 * then resources that are alreeady in cache are located.
 * then other resources in the index are queried for (and cached in resource cache)
 * finally, resources that aren't in cache, and can't be got from registry service are marked in cache as 'Missing'.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 13, 200810:10:06 PM
 */
public class IndexCachingRegistryImpl extends StreamingRegistryImpl{

    /**
     * @param reg
     * @param endpoint
     * @param fallbackEndpoint
     * @param resource
     * @param document
     * @param bulk
     * @throws URISyntaxException
     */
    public IndexCachingRegistryImpl(ExternalRegistryInternal reg,
            Preference endpoint, Preference fallbackEndpoint, Ehcache resource,
            Ehcache document,Ehcache index) throws URISyntaxException {
        super(reg, endpoint, fallbackEndpoint, resource, document, null/*bulk not used*/);
        this.indexCache = index;
        expirer= new CachedResourceExpirer(reg,endpoint,resource,document);
        logger.info("Using registy index caching");
    }

    
    // map from xquery to URI[]
    private final Ehcache indexCache;
    // subcomponent that handles the expiration process.
    private final ScheduledTask expirer;


    @Override
    public void consumeXQueryReload(String xquery, ResourceConsumer processor)
            throws ServiceException {
        if (indexCache.isKeyInCache(xquery)) {
            indexCache.remove(xquery);
            //@todo remove resources (or missing resources) too??
        }
        // now proceed as normal.
        consumeXQuery(xquery,processor);
    }

    @Override
    public Resource[] xquerySearch(String arg0) throws ServiceException {
        ResourceAccumulator rc = new ResourceAccumulator();
        consumeXQuery(arg0,rc);
        return rc.getResources();        
    }
    /** resource processor that just accumulates the resources into a list */
    private static final class  ResourceAccumulator implements ResourceConsumer {
        private final ArrayList resources = new ArrayList();
        public void process(Resource s) {
            resources.add(s);
        }
        public final Resource[] getResources() {
            return (Resource[])this.resources.toArray(new Resource[resources.size()]);
        }
        public void estimatedSize(int i) {
            resources.ensureCapacity(i);
        }
    }

    // slightly different behaviour - will cache results no matter whethe they come from main or fallback reg.
    // as is implemented in terms of super.xquerySearchStream
    // minor issue - dn't care for now.
    @Override
    public void consumeXQuery(String xquery, ResourceConsumer resourceConsumer)
            throws ServiceException {
            logger.debug("consumeXQuery " + xquery);
            Element cachedIndex = indexCache.get(xquery);
            final CacheFetcher fetcher = new CacheFetcher(resourceConsumer);
            if (cachedIndex != null) {
                logger.debug("found cached index");
                final URI[] indexes = (URI[])cachedIndex.getValue();
                resourceConsumer.estimatedSize(indexes.length);
                for (URI index : indexes) {
                    fetcher.fetchFromCache(index);
                }
            } else { // need to query the registry to get the list of indexes
                logger.debug("Querying for index");
               final IndexStreamProcessor indexStreamProcessor = new IndexStreamProcessor(fetcher);
               xquerySearchStream(mkIndexQuery(xquery),indexStreamProcessor);
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


    /** bulk query to retrieve a list of resources.
     * todo: need to add a variant which checks for cache here...
     * @param uriList list of resources ids to retrieve
     * @param resourceConsumer processor to call for each resource.
     * @throws ServiceException
     */
    @Override
    public void consumeResourceList(final Collection<URI> uriList,
            ResourceConsumer resourceConsumer) throws ServiceException {
        if (uriList.isEmpty()) {
            return; // done
        }
        resourceConsumer.estimatedSize(uriList.size());
        CacheFetcher fetcher = new CacheFetcher(resourceConsumer);
        for (URI uri : uriList) {
            fetcher.fetchFromCache(uri);
        }
        Set<URI> misses = fetcher.getMisses();
        if (! misses.isEmpty()) {
            // now query for the remainder.
            queryForResourceList(misses, resourceConsumer);
        }
    }


    /** query the registry for a list of resources. (caching the results)
     * @param uriList
     * @param resourceConsumer
     * @throws ServiceException
     */
    private void queryForResourceList(final Set<URI> uriList,
            ResourceConsumer resourceConsumer) throws ServiceException {
        xquerySearchStream(mkListResourcesQuery(uriList)
                ,new ResourceListProcessor(resourceConsumer, uriList));
        // check whether we've got any uri left - these are ones not present in the reg - mark them as unknown.
        for (URI uri : uriList) {
            final Element element = new Element(uri,MISSING_RESOURCE);
            element.setTimeToLive(60 * 60); // just mark it missing for a short amount of time - 1 hour.           
            resourceCache.put(element);
            
        }                
    }

    /** object used to indicate that registry doesn't have a resource associated with a particular ivorn */
    private static final MissingResource MISSING_RESOURCE = new MissingResource();
    
    /** indicator class that represents a missing resource */
    private static class MissingResource implements Serializable{

        /**
         * 
         */
        private static final long serialVersionUID = -5968284172785042470L;
        
    }
 
    /** class that fetches items from the cache by index
     * the retrieved resources are passed to the argument ResourceProcessor,.    
     * cache misses are accumulated into a list */
    private final  class CacheFetcher {
       
        private final ResourceConsumer resourceConsumer;
        private final Set<URI> misses = new HashSet<URI>();
        /**
         * @param resourceConsumer
         */
        public CacheFetcher(ResourceConsumer resourceConsumer) {
            this.resourceConsumer = resourceConsumer;
        }

        /** call this repeatedly to process a list of indexes
         * 
         * @param index a resource id. if in the cache, will call ResourceProcessor, else is added to the 
         * misses list.
         */
        private void fetchFromCache(URI index) {
            Element element = resourceCache.get(index);
            if (element == null) {
                misses.add(index);
            } else if (element.getValue() instanceof MissingResource) {
                // it's an index which we know the registry lacks.
                //do nothing - don't add to missing list, and don't pass to resource consumer.
            } else {
                resourceConsumer.process((Resource)element.getValue()); // no typesafety checking here..
            }
            
        }

        public final Set<URI> getMisses() {
            return this.misses;
        }

        public final ResourceConsumer getResourceConsumer() {
            return this.resourceConsumer;
        }
    }

    /** streamprocessor that parses a list of indexes and passed each one to the associated cache fetcher 
     * designed to operate with a query build by {@link #mkIndexQuery}
     * */
    private  static final class IndexStreamProcessor implements StreamProcessor {

        private final CacheFetcher fetcher;
        private final List<URI> indexes = new ArrayList<URI>();
        
        /**
         * @param fetcher
         */
        public IndexStreamProcessor(CacheFetcher fetcher) {
            this.fetcher = fetcher;
        }

        public void process(XMLStreamReader r) throws Exception {            
            while (r.hasNext()) {
                r.next();
                if (r.isStartElement() && "identifier".equals(r.getLocalName())) {
                    try {
                        // 'expected a text token, got START ELEMENT
                        // malformed XML? or unexpected nested content?
                        URI u = new URI(r.getElementText());
                        indexes.add(u);
                        fetcher.fetchFromCache(u);
                    } catch (URISyntaxException e) {
                        logger.warn("Failed to build uri from index response",e);
                    }
                }
            }
        }

        /** access the indexes returned by this query */
        public final URI[] getIndexes() {
            return this.indexes.toArray(new URI[indexes.size()]);
        }
    }
    
    /** create an xquery from a query that will just list the matching indexes, not the entire resources */
    private String mkIndexQuery(String query) {
        if (query == null) {
            throw new IllegalArgumentException("null query");
        }
        String xq =  "<indexes>{for $__resource_ in  ( " + query.trim() + ") return $__resource_/identifier}</indexes>";
        return xq;
        

    }
    
    /** stream processor that queries for a set of resources, caches each, and passes each to 
     * an argument resourceProcessor
     * @author Noel.Winstanley@manchester.ac.uk
     * @since May 14, 20084:01:24 PM
     */
    private final class ResourceListProcessor implements StreamProcessor {

        private final ResourceConsumer resourceConsumer;
        private final Set<URI> expectedResources ;

        /**
         * @param resourceConsumer onsimer to pass resources onto
         * @param uriList list of resources we expect to see. will be modified in-place - resources removed as they're encountered.
         */
        public ResourceListProcessor(ResourceConsumer resourceConsumer, Set<URI> uriList) {
            this.resourceConsumer = resourceConsumer;
            expectedResources = uriList;
        }

        public void process(XMLStreamReader r) throws Exception {
            ResourceStreamParser parser = new ResourceStreamParser(r);
            while(parser.hasNext()) {
                Resource resource = (Resource)parser.next();
                final URI id = resource.getId();
                resourceCache.put(new Element(id,resource));
                expectedResources.remove(id);
                resourceConsumer.process(resource);
            }                   
        }
    }
    // overridden to use index caching scheme, rather than bulk cache.

    @Override
    public Resource[] adqlsSearch(String arg0) throws ServiceException,
    InvalidArgumentException {
        try {
            Element cachedIndex = indexCache.get(arg0);
            if (cachedIndex != null) { // resources will be partialy in the cache already.
                final ResourceAccumulator rc = new ResourceAccumulator();
                final CacheFetcher fetcher = new CacheFetcher(rc);
                for (URI index : (URI[])cachedIndex.getValue()) {
                    fetcher.fetchFromCache(index);
                }
                final Set<URI> misses = fetcher.getMisses();
                if (! misses.isEmpty()) { // query for the remainder
                    queryForResourceList(misses, rc);
                }                
                return rc.getResources();
            } else { // query registry
                Resource[]res =  reg.adqlsSearch(getSystemRegistryEndpoint(),arg0);
                cacheResourcesAndIndex(arg0,res);
                return res;
            }
        } catch (ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            return reg.adqlsSearch(getFallbackSystemRegistryEndpoint(),arg0);
        }
    }

    /** stuff each resource in the individual resource cache, and add an entry to the index cache
     * for the entire array of identifiers using <tt>indexKey</tt> */
    private void cacheResourcesAndIndex(Object indexKey,Resource[] res) {
        URI[] index = new URI[res.length];
        for (int i = 0; i < res.length; i++) {
            index[i] = res[i].getId();
            resourceCache.put(new Element(res[i].getId(),res[i]));            
        }
        indexCache.put(new Element(indexKey,index));
    }

    @Override
    public Resource[] adqlxSearch(Document arg0) throws ServiceException,
    InvalidArgumentException {
        try {
            Element cachedIndex = indexCache.get(arg0);
            if (cachedIndex != null) { // resources will be partialy in the cache already.
                final ResourceAccumulator rc = new ResourceAccumulator();
                final CacheFetcher fetcher = new CacheFetcher(rc);
                for (URI index : (URI[])cachedIndex.getValue()) {
                    fetcher.fetchFromCache(index);
                }
                final Set<URI> misses = fetcher.getMisses();
                if (! misses.isEmpty()) { // query for the remainder
                    queryForResourceList(misses, rc);
                }                
                return rc.getResources();
            } else { // query registry
                Resource[]res =  reg.adqlxSearch(getSystemRegistryEndpoint(),arg0);
                cacheResourcesAndIndex(arg0,res);
                return res;
            }
        } catch (ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            return reg.adqlxSearch(getFallbackSystemRegistryEndpoint(),arg0);
        }
    }


    @Override
    public Resource[] keywordSearch(String arg0, boolean arg1)
    throws ServiceException {
        try {
            Element cachedIndex = indexCache.get(arg0);
            if (cachedIndex != null) { // resources will be partialy in the cache already.
                final ResourceAccumulator rc = new ResourceAccumulator();
                final CacheFetcher fetcher = new CacheFetcher(rc);
                for (URI index : (URI[])cachedIndex.getValue()) {
                    fetcher.fetchFromCache(index);
                }
                final Set<URI> misses = fetcher.getMisses();
                if (! misses.isEmpty()) { // query for the remainder
                    queryForResourceList(misses, rc);
                }                
                return rc.getResources();
            } else {    
                Resource[] res =  reg.keywordSearch(getSystemRegistryEndpoint(),arg0,arg1);
                cacheResourcesAndIndex(arg0+arg1,res);
                return res;     
            }
        } catch (ServiceException e) {
            logger.warn("Failed to query main system registry - falling back",e);
            return reg.keywordSearch(getFallbackSystemRegistryEndpoint(),arg0,arg1);
        }
    }
    // delegate methods to scheduled task.
    public final void execute(WorkerProgressReporter reporter) {
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
    
}
