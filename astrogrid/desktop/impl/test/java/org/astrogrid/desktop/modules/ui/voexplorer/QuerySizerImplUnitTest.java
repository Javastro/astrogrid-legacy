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

    protected void setUp() throws Exception {
        super.setUp();
        this.registry = createMock("registry",RegistryInternal.class);
        this.cache = createMock("cache",Ehcache.class);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        registry = null;
        cache = null;        
    }
    
    public void testCompleteQueriesVisitor() throws Exception {
        Builder b = new HeadClauseSRQLVisitor();
        TermSRQL t = new TermSRQL();
        t.setTerm("fred");
        assertNotNull(b.visit(t));
        t.setTerm(" ");
        try {
            assertNotNull(b.visit(t));
            fail("expected to chuck");
        } catch (IllegalArgumentException e) {
        }
        t.setTerm(null);
        try {
            assertNotNull(b.visit(t));
            fail("expected to chuck");
        } catch (IllegalArgumentException e) {
        }        
        
    }
    
    public void testConstructSizingQuery() throws Exception {
        try {
            QuerySizerImpl.constructSizingQuery(null);
            fail("expected to chuck");
        } catch (IllegalArgumentException e) {
        };
        
        String q = "a query";
        assertThat(QuerySizerImpl.constructSizingQuery(q),containsString(q));
    }
    
    public void testSizeStringCacheHit() throws Exception {
        String q = "a query";
        Element el = new Element(null,42);
        expect(cache.get(contains(q))).andReturn(el);
        replay(registry,cache);
        QuerySizerImpl qsi = new QuerySizerImpl(registry,cache);
        assertThat(qsi.size(q),is(42));
        verify(registry,cache);
               
    }
    
    public void testSizeStringCacheMiss() throws Exception {
        String q = "a query";
        expect(cache.get(contains(q))).andReturn(null); // cache miss
        registry.xquerySearchStream(contains(q),(StreamProcessor)notNull());
        expectLastCall().andAnswer(new IAnswer() { // call back into the processor
            public Object answer() throws Throwable {
                StreamProcessor proc = (StreamProcessor)getCurrentArguments()[1];
                XMLInputFactory fac = XMLInputFactory.newInstance();
                // this reverse-engineers the perceived behaviour of registry service.
                InputStream is = new ByteArrayInputStream("<?xml version='1.0'?><size>42</size>".getBytes());
                XMLStreamReader reader = fac.createXMLStreamReader(is);
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
        QuerySizerImpl qsi = new QuerySizerImpl(registry,cache);
        assertThat(qsi.size(q),is(42));
        verify(registry,cache);
               
    }
    /** what happens when registry service returns something not a number */
    public void testSizeStringCacheMissBadRegResponse() throws Exception {
        String q = "a query";
        expect(cache.get(contains(q))).andReturn(null); // cache miss
        registry.xquerySearchStream(contains(q),(StreamProcessor)notNull());
        expectLastCall().andAnswer(new IAnswer() { // call back into the processor
            public Object answer() throws Throwable {
                StreamProcessor proc = (StreamProcessor)getCurrentArguments()[1];
                XMLInputFactory fac = XMLInputFactory.newInstance();
                // this reverse-engineers the perceived behaviour of registry service.
                InputStream is = new ByteArrayInputStream("<?xml version='1.0'?><size>arnold</size>".getBytes()); // not returned a number
                XMLStreamReader reader = fac.createXMLStreamReader(is);
                reader.nextTag();
                proc.process(reader);
                return null;
            }
        });
        // no cache store
        replay(registry,cache);
        QuerySizerImpl qsi = new QuerySizerImpl(registry,cache);
        assertThat(qsi.size(q),is(QuerySizer.ERROR));
        verify(registry,cache);
               
    }
    
    /** what happens when registry service fails*/
    public void testSizeStringCacheMissBadRegResponseXML() throws Exception {
        String q = "a query";
        expect(cache.get(contains(q))).andReturn(null); // cache miss
        registry.xquerySearchStream(contains(q),(StreamProcessor)notNull());
        expectLastCall().andThrow(new ServiceException(""));
        // no cache store
        replay(registry,cache);
        QuerySizerImpl qsi = new QuerySizerImpl(registry,cache);
        assertThat(qsi.size(q),is(QuerySizer.ERROR));
        verify(registry,cache);
               
    }
    

}
