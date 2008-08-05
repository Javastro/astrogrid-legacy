/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import static org.easymock.EasyMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.Builder;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.HeadClauseSRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.TermSRQL;
import org.easymock.IAnswer;
/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 25, 20088:52:38 PM
 */
public class QuerySizerImplUnitTest extends TestCase {

    private RegistryInternal registry;
    private Ehcache cache;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.registry = createMock("registry",RegistryInternal.class);
        this.cache = createMock("cache",Ehcache.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        registry = null;
        cache = null;        
    }
    
    public void testCompleteQueriesVisitor() throws Exception {
        final Builder b = new HeadClauseSRQLVisitor();
        final TermSRQL t = new TermSRQL();
        t.setTerm("fred");
        assertNotNull(b.visit(t));
        t.setTerm(" ");
        try {
            assertNotNull(b.visit(t));
            fail("expected to chuck");
        } catch (final IllegalArgumentException e) {
        }
        t.setTerm(null);
        try {
            assertNotNull(b.visit(t));
            fail("expected to chuck");
        } catch (final IllegalArgumentException e) {
        }        
        
    }
    
    public void testConstructSizingQuery() throws Exception {
        try {
            QuerySizerImpl.constructSizingQuery(null);
            fail("expected to chuck");
        } catch (final IllegalArgumentException e) {
        };
        
        final String q = "a query";
        assertThat(QuerySizerImpl.constructSizingQuery(q),containsString(q));
    }
    
    public void testSizeStringCacheHit() throws Exception {
        final String q = "a query";
        final Element el = new Element(null,42);
        expect(cache.get(contains(q))).andReturn(el);
        replay(registry,cache);
        final QuerySizerImpl qsi = new QuerySizerImpl(registry,cache,null,0,0);
        assertThat(qsi.size(q),is(42));
        verify(registry,cache);
               
    }
    
    public void testSizeStringCacheMiss() throws Exception {
        final String q = "a query";
        expect(cache.get(contains(q))).andReturn(null); // cache miss
        registry.xquerySearchStream(contains(q),(StreamProcessor)notNull());
        expectLastCall().andAnswer(new IAnswer() { // call back into the processor
            public Object answer() throws Throwable {
                final StreamProcessor proc = (StreamProcessor)getCurrentArguments()[1];
                final XMLInputFactory fac = XMLInputFactory.newInstance();
                // this reverse-engineers the perceived behaviour of registry service.
                final InputStream is = new ByteArrayInputStream("<?xml version='1.0'?><size>42</size>".getBytes());
                final XMLStreamReader reader = fac.createXMLStreamReader(is);
                reader.nextTag();
                proc.process(reader);
                return null;
            }
        });
        cache.put((Element)notNull());
        expectLastCall().andAnswer(new IAnswer() {

            public Object answer() throws Throwable {
                assertEquals(42,((Element)getCurrentArguments()[0]).getValue());
                     
                return null;
            }
        });
        replay(registry,cache);
        final QuerySizerImpl qsi = new QuerySizerImpl(registry,cache,null,0,0);
        assertThat(qsi.size(q),is(42));
        verify(registry,cache);
               
    }
    /** what happens when registry service returns something not a number */
    public void testSizeStringCacheMissBadRegResponse() throws Exception {
        final String q = "a query";
        expect(cache.get(contains(q))).andReturn(null); // cache miss
        registry.xquerySearchStream(contains(q),(StreamProcessor)notNull());
        expectLastCall().andAnswer(new IAnswer() { // call back into the processor
            public Object answer() throws Throwable {
                final StreamProcessor proc = (StreamProcessor)getCurrentArguments()[1];
                final XMLInputFactory fac = XMLInputFactory.newInstance();
                // this reverse-engineers the perceived behaviour of registry service.
                final InputStream is = new ByteArrayInputStream("<?xml version='1.0'?><size>arnold</size>".getBytes()); // not returned a number
                final XMLStreamReader reader = fac.createXMLStreamReader(is);
                reader.nextTag();
                proc.process(reader);
                return null;
            }
        });
        // no cache store
        replay(registry,cache);
        final QuerySizerImpl qsi = new QuerySizerImpl(registry,cache,null,0,0);
        assertThat(qsi.size(q),is(QuerySizer.ERROR));
        verify(registry,cache);
               
    }
    
    /** what happens when registry service fails*/
    public void testSizeStringCacheMissBadRegResponseXML() throws Exception {
        final String q = "a query";
        expect(cache.get(contains(q))).andReturn(null); // cache miss
        registry.xquerySearchStream(contains(q),(StreamProcessor)notNull());
        expectLastCall().andThrow(new ServiceException(""));
        // no cache store
        replay(registry,cache);
        final QuerySizerImpl qsi = new QuerySizerImpl(registry,cache,null,0,0);
        assertThat(qsi.size(q),is(QuerySizer.ERROR));
        verify(registry,cache);
               
    }
    
    public void testPreferenceAccessor() {
        final Preference p = new Preference();
        p.setValue("true");
        final QuerySizerImpl qsi = new QuerySizerImpl(registry,cache,p,0,0);
        assertTrue(qsi.isPreventOversizeQueries());
        p.setValue("false");
        assertFalse(qsi.isPreventOversizeQueries());
    }
    
    public void testThresholds() {

        final QuerySizerImpl qsi = new QuerySizerImpl(registry,cache,null,42,43);
        assertEquals(42,qsi.getGoodThreshold());
        assertEquals(43,qsi.getOversizeThreshold());
    }
    

}
