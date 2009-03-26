/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.StringReader;
import java.net.URI;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Statistics;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import junit.framework.TestCase;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/** Unit test for some of the bits of the cached resource expirer.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 15, 20084:00:37 PM
 */
public class CachedResourceExpirerUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        reg = createMock(ExternalRegistryInternal.class);
        mockCache = createNiceMock(Ehcache.class);
        cMan = CacheManager.getInstance();
        cMan.addCache("resources");
        cache = cMan.getCache("resources");
        assertNotNull(cache);
        cre = new CachedResourceExpirer(reg,new Preference(),cache, mockCache);
    }
    CacheManager cMan ;
    ExternalRegistryInternal reg;
    CachedResourceExpirer cre;
    Ehcache cache;
    Ehcache mockCache;
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        cre = null;
        reg = null;
        cache = null;
        mockCache = null;
        cMan.shutdown();
        cMan = null;
    }

    public void testFindEarliestUpdateInEmptycache() throws Exception {
        // for an empty cache, should be roughly one day back in the past, in UTC.
        DateTime now = new DateTime().toDateTime(DateTimeZone.UTC);
        assertThat(cre.findEarliestUpdateInCache()
                ,allOf( 
                        greaterThan(now)
                        , lessThan(now.plusMinutes(1))
                        ));
    }
    
    public void testFindEarliestUpdatedinPopulatedCache() throws Exception {
        DateTime now = new DateTime().toDateTime(DateTimeZone.UTC);
        Thread.sleep(100); // to give a different time for stats from 'npw
        cache.setStatisticsAccuracy(Statistics.STATISTICS_ACCURACY_GUARANTEED);
        Element a = new Element("key","a");
        cache.put(a);
        Thread.sleep(100); // to give a different time between a and b.
        Element b = new Element("key1","b");
        cache.put(b);
        // I'd expect a to be the oldest.
        assertThat(a.getCreationTime(),lessThan(b.getCreationTime()));
        // I'd also expect that the creationTime of A is later than 'now' (test of parsing of joda time, really
        assertThat(new DateTime(a.getCreationTime()), greaterThan(now)); // interesting - takes time zones into account.
        
       // ok, now verify that we find the same time as a.
        assertThat(cre.findEarliestUpdateInCache()
                , equalTo(new DateTime(a.getCreationTime()).toDateTime(DateTimeZone.UTC))
                );                
    }
        
    public void testFindEarliestUpdatedUnexpiredinPopulatedCache() throws Exception {
        DateTime now = new DateTime().toDateTime(DateTimeZone.UTC);
        Thread.sleep(100); // to give a different time for stats from 'npw
        cache.setStatisticsAccuracy(Statistics.STATISTICS_ACCURACY_GUARANTEED);
        Element a = new Element("key","a");
        a.setTimeToLive(1);
        cache.put(a);
        Thread.sleep(1100); // need to wait for a to expire.
        assertTrue(a.isExpired());
        Element b = new Element("key1","b");
        cache.put(b);

       // ok, now verify that we find the same time as b - the oldest unexpired.
        assertThat(cre.findEarliestUpdateInCache()
                , equalTo(new DateTime(b.getCreationTime()).toDateTime(DateTimeZone.UTC))
                );
                
    }
    
    private final static String ts = "2004-09-23T22:11:42";
    public void testProcessTimestamp() throws Exception {
        String input = "<update-check><timestamp>" + ts + "</timestamp></update-check>";
        assertFalse(cache.isKeyInCache(CachedResourceExpirer.LAST_UPDATE_CHECK));
        XMLInputFactory fac = XMLInputFactory.newInstance();
        XMLStreamReader r = fac.createXMLStreamReader(new StringReader(input));
        cre.process(r);
        Element element = cache.get(CachedResourceExpirer.LAST_UPDATE_CHECK);
        assertNotNull(element);
        assertThat(element.getValue(), instanceOf(DateTime.class));
        assertThat((DateTime)element.getValue(),equalTo(new DateTime(ts)));
        
    }
    /** when an identifier is encountered that isn't in the cache, nothing should happen */
    public void testProcessIdentifierNotInCache() throws Exception {
        URI id = new URI("ivo://foo.bar.choo");
        String input = "<update-check><identifier updated='" + ts + "'>"+id+"</identifier></update-check>";        
        XMLInputFactory fac = XMLInputFactory.newInstance();
        XMLStreamReader r = fac.createXMLStreamReader(new StringReader(input));
        cre.process(r);
    }
    
    /** when an id is encountered that is in the cache, but has the same modification date, nothing should happen */
    public void testProcessIdentifierInCacheOld() throws Exception {
        DateTime now = new DateTime();
        URI id = new URI("ivo://foo.bar.choo");
        Resource resource = createMock(Resource.class);
        expect(resource.getUpdated()).andStubReturn(now.toString());
        replay(resource);
        cache.put(new Element(id,resource));
        String input = "<update-check><identifier updated='" + now + "'>"+id+"</identifier></update-check>";        
        XMLInputFactory fac = XMLInputFactory.newInstance();
        XMLStreamReader r = fac.createXMLStreamReader(new StringReader(input));
        cre.process(r);
        verify(resource);
        assertTrue(cache.isKeyInCache(id));
        assertSame(resource, cache.get(id).getValue());
    }
    
    /** when an id is encountered that is in the cache, but  has a more recent modification date, remove it */
    public void testProcessIdentifierInCacheNew() throws Exception {
        DateTime now = new DateTime();
        URI id = new URI("ivo://foo.bar.choo");
        Resource resource = createMock(Resource.class);
        expect(resource.getUpdated()).andStubReturn(now.toString());
        replay(resource);
        cache.put(new Element(id,resource));
        String input = "<update-check><identifier updated='" + now.plusHours(3) + "'>"+id+"</identifier></update-check>";        
        XMLInputFactory fac = XMLInputFactory.newInstance();
        XMLStreamReader r = fac.createXMLStreamReader(new StringReader(input));
        cre.process(r);
        verify(resource);
        assertFalse(cache.isKeyInCache(id));
    }
    
    /** when an invalid id is encountered, ignore and continue */
    public void testProcessInvalidId() throws Exception {
        DateTime now = new DateTime();
        URI id = new URI("ivo://foo.bar.choo");
        Resource resource = createMock(Resource.class);
        expect(resource.getUpdated()).andStubReturn(now.toString());
        replay(resource);
        cache.put(new Element(id,resource));
        // added garbage
        String input = "<update-check><identifier updated='" + ts+"'>this is a load of utter rubbish</identifier><identifier updated='" + now.plusHours(3) + "'>"+id+"</identifier></update-check>";        
        XMLInputFactory fac = XMLInputFactory.newInstance();
        XMLStreamReader r = fac.createXMLStreamReader(new StringReader(input));
        cre.process(r);
        // expect to see same behaviour as previous test - ie. the garbage is ignored, and the processing proceeds
        verify(resource);
        assertFalse(cache.isKeyInCache(id));
    }
    
    /** when an invalid date  is encountered, ignore and continue */
    public void testProcessInvalidDate() throws Exception {
        DateTime now = new DateTime();
        URI id = new URI("ivo://foo.bar.choo");
        Resource resource = createMock(Resource.class);
        expect(resource.getUpdated()).andStubReturn(now.toString());
        replay(resource);
        cache.put(new Element(id,resource));
        // added garbage
        String input = "<update-check><identifier updated='no chance of parsing this as a date'>ivo://some.trash</identifier><identifier updated='" + now.plusHours(3) + "'>"+id+"</identifier></update-check>";        
        XMLInputFactory fac = XMLInputFactory.newInstance();
        XMLStreamReader r = fac.createXMLStreamReader(new StringReader(input));
        cre.process(r);
        // expect to see same behaviour as previous test - ie. the garbage is ignored, and the processing proceeds
        verify(resource);
        assertFalse(cache.isKeyInCache(id));
    }
    

}
