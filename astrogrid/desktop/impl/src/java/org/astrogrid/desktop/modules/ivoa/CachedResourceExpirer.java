/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.astrogrid.desktop.modules.system.ScheduledTask;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.WorkerProgressReporter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/** subcomponent of the index caching registry - separates out the resource expiry logic
 * 
 *  runs periodically to remove obselete resources.
 *  */
class CachedResourceExpirer implements ScheduledTask, StreamProcessor {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(CachedResourceExpirer.class);

    public CachedResourceExpirer(ExternalRegistryInternal reg, Preference endpoint2,
            Ehcache resourceCache, Ehcache xmlCache) {
        super();
        this.reg = reg;
        this.endpoint = endpoint2;
        this.resourceCache = resourceCache;
        this.xmlCache = xmlCache;
    }
    /** preference containing registry service endpoint */
    private final Preference endpoint;
    /** key to store last update date under */
    final static URI LAST_UPDATE_CHECK = URI.create("internal://last_update_check");
    /** external registry component */
    private final ExternalRegistryInternal reg;
    /** cache containing resource objects */
    private final Ehcache resourceCache;
    /** cache contains xml docuemnts */
    private final Ehcache xmlCache;

    public String getName() {
        return "Expiring out-of-date cached resources";
    }
    
    public long getPeriod() {
        return 1000 * 60 * 60 ; // hourly.
    }
    
    public Principal getPrincipal() {
        return null; // use default principal.
    }

    /** check for updates to the registry, and remove any obsolete cached copies */
    public void execute(WorkerProgressReporter reporter) {
        Element element = resourceCache.get(LAST_UPDATE_CHECK);
        DateTime lastUpdate;
        if (element == null) { ///deduce a starttime from the cache contents
            lastUpdate = findEarliestUpdateInCache();
        } else { // parse last recorded update 
            lastUpdate = (DateTime)element.getValue();
        }
        // add in a fudge factor of 30 mins
        DateTime updatesAfter = lastUpdate.minusMinutes(30).minusDays(10);
        StringBuilder xq = new StringBuilder();
        // create the query.
        xq.append("let $now := current-dateTime()\n")
            .append("let $updatesAfter := xs:dateTime('").append(updatesAfter).append("')\n") // unsure whether this will work
            .append("let $dUpdatesAfter := xs:date($updatesAfter)\n")            
            .append("return <update-check>")
            .append("<timestamp>{$now}</timestamp>")
            .append("{for $r in //vor:Resource where\n")
            .append( "($r/@updated castable as xs:dateTime and xs:dateTime($r/@updated) > $updatesAfter)\n")
            .append("or ($r/@updated castable as xs:date and xs:date($r/@updated) > $dUpdatesAfter)\n")
            .append("return <identifier updated='{$r/@updated}'>{$r/identifier/text()}</identifier>")            
            .append("}</update-check>");
        try {
            URI u = new URI(endpoint.getValue());
            reg.xquerySearchStream(u,xq.toString(),this);
        } catch (ServiceException x) {
            logger.warn("Failed to check for updates",x);
            // wonder if I should just flush the entire cache here?
        } catch (URISyntaxException x) {
            logger.warn("Failed to check for updates - invalid registry endpoint",x);
            // flush it all?
        }
    }

    /** Expensive operation - iterate though all of registry, finding the earliest creation time.
     * @return
     */
    public DateTime findEarliestUpdateInCache() {
        List keys = resourceCache.getKeysNoDuplicateCheck();
        long oldestElement = new DateTime().getMillis(); // start with now.
        for (Object key : keys) {
            Element e= resourceCache.get(key);           
            if (e != null && ! e.isExpired() && oldestElement > e.getCreationTime()) {
                oldestElement = e.getCreationTime();
            }                    
        }
         DateTime firstCreation = new DateTime(oldestElement);
         return firstCreation.toDateTime(DateTimeZone.UTC); // convert from local timezone to UTC (the benefits of living in india)
    }

    /** stream processor callback - used to process the update query 
     * @throws XMLStreamException 
     * @throws CacheException 
     * @throws IllegalStateException */
    public void process(XMLStreamReader r) throws  XMLStreamException {
        DateTime timestamp = null;
        while (r.hasNext()) {
            r.next();
            if (r.isStartElement() && "timestamp".equals(r.getLocalName())) {
                // hang onto the timestamp, for afterwards.
                timestamp = new DateTime(r.getElementText());
            } else if (r.isStartElement() && "identifier".equals(r.getLocalName())) {
                // for each updated index, compare with the local cached copy modification date.
                try {
                    DateTime latest = new DateTime(r.getAttributeValue(null,"updated"));
                    URI u = new URI(r.getElementText());
                    Element element = resourceCache.get(u);                        
                    if (element != null) {
                        if (element.getValue() instanceof Resource) {
                            Resource res = (Resource)element.getValue();
                            DateTime cached = new DateTime(res.getUpdated()); // or I could get a date from the cache itself - but the resource date will be in the same timezone
                            if (latest.compareTo(cached) >= 0) {
                                resourceCache.remove(u);   
                            }
                        } else {
                            // it's something else - remove it.
                            resourceCache.remove(u);
                        }
                    }
                    if (xmlCache.isKeyInCache(u)) { // don't bother comparing dates with the document cache - just remove.
                        xmlCache.remove(u);
                    }
                } catch (URISyntaxException e) {
                    logger.warn("Failed to build uri from index response",e);
                } catch (IllegalArgumentException e) {
                    logger.warn("Failed to build date from index response",e);                    
                }
            }
        } // end while.            
        // if we've got this far, it's all successful - record the timestamp.
        resourceCache.put(new Element(LAST_UPDATE_CHECK,timestamp));
    }
}