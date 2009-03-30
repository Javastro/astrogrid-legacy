/**
 * 
 */
package org.astrogrid.desktop.modules.votech;


import static org.astrogrid.Fixture.createMockContext;
import static org.easymock.EasyMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.collections.IteratorUtils;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.votech.AnnotationService.AnnotationProcessor;

/** Unit test for the annotaiton service.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 23, 20089:04:38 PM
 */
public class AnnotationServiceImplUnitTest extends TestCase {

    private Ehcache cache;
    private UIContext context;
    private AnnotationIO io;
    private URI uri;
    private Resource resource;
    private AnnotationSource userAnnotationSource;
    private UserAnnotation uan;
    private AnnotationSource otherSource;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.cache = createMock("cache",Ehcache.class);
        this.context = createMockContext();
        this.io = createMock("io",AnnotationIO.class);
        
         this.userAnnotationSource = new AnnotationSource();
         userAnnotationSource.setName("ua");
         userAnnotationSource.setSource(new URI("file://ua"));
        expect(io.getUserSource()).andStubReturn(userAnnotationSource);
        
        otherSource = new AnnotationSource();
        otherSource.setSource(new URI("file://foo"));      
        assertThat(userAnnotationSource,not(equalTo(otherSource)));
        
        this.uri = new URI("ivo://test.id");
        this.resource = createMock("resource",Resource.class);
        expect(resource.getId()).andStubReturn(uri);
        replay(resource);
        
        uan = new UserAnnotation();
        uan.setResourceId(uri);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        cache = null;
        context = null;
        io = null;
        uri = null;
        resource = null;
        uan = null;
        otherSource = null;
        userAnnotationSource = null;
    }
    
    public void testInstantiateEmpty() throws Exception {
        expect(io.getSourcesList()).andReturn(Collections.EMPTY_LIST);
        replay(cache,context,io);
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);
    
        //
        assertThat(asi.getUserAnnotationSource(),sameInstance(userAnnotationSource));
        assertThat(asi.listSources(),not(hasItemInArray(anything()))); // complex way of saying 'its empty'
        verify(cache,context,io);
    }

    public void testGetUserAnnotation() throws Exception {
        expect(io.getSourcesList()).andReturn(Collections.EMPTY_LIST);
        // case for no entry
        expect(cache.get(uri)).andReturn(null).times(2);

        // case for an entry
        final Map<AnnotationSource, UserAnnotation> m = new HashMap<AnnotationSource, UserAnnotation>();
        m.put(userAnnotationSource,uan);
        final Element e = new Element(uan.getResourceId(),m);
       
        expect(cache.get(uri)).andReturn(e).times(2);        
        replay(cache,context,io);
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);

        // test when there's nothing in the cache.
        assertThat(asi.getUserAnnotation(uri),nullValue());
        assertThat(asi.getUserAnnotation(resource),nullValue());
                
// test when there's an entry in the cache.        
        assertThat(asi.getUserAnnotation(uri),equalTo(uan));
        assertThat(asi.getUserAnnotation(resource),equalTo(uan));
        
// test for an erroneous inputs.        
        assertThat(asi.getUserAnnotation((URI)null),nullValue());
        assertThat(asi.getUserAnnotation((Resource)null),nullValue());
        verify(cache,context,io);
    }
    
    public void testSetNewUserAnnotation() throws Exception {

        expect(io.getSourcesList()).andReturn(Collections.EMPTY_LIST);
                
        expect(cache.get(uri)).andReturn(null); // it's not in the cache.
        
        final Map<AnnotationSource,UserAnnotation>  m = new HashMap<AnnotationSource, UserAnnotation>();
        m.put(userAnnotationSource,uan);
        cache.put(new Element(uri,m)); // it's stored in the cache.
        
        io.updateUserAnnotation(uan); // it's persisted.
        
        replay(cache,context,io);
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);
        
        uan.setResourceId(null);
        uan.setSource(null);
        asi.setUserAnnotation(resource,uan);
        assertThat(uan.getSource(),equalTo(userAnnotationSource)); // source for the annotation is set
        assertThat(uan.getResourceId(),equalTo(uri)); // resourceId for the annotation is set.
        assertThat(m,hasEntry(userAnnotationSource,uan));
        assertThat(m.size(),is(1));
        //
               
        verify(cache,context,io);
    }
    
    /** test for adding a user annotation to an existing set of annotations */
    public void testAddUserAnnotationToExistingSet() throws Exception {

        expect(io.getSourcesList()).andReturn(Collections.EMPTY_LIST);
        
        final Map<AnnotationSource,Annotation>  m = new HashMap<AnnotationSource, Annotation>();
        m.put(otherSource,new Annotation());
        final Element e = new Element(uri,m);        
        expect(cache.get(uri)).andReturn(e); // there's other annotations for this id.
        
        cache.put(new Element(uri,m)); // it's stored in the cache.
        
        io.updateUserAnnotation(uan); // it's persisted.
        
        replay(cache,context,io);
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);
        
        uan.setResourceId(null);
        uan.setSource(null);
        asi.setUserAnnotation(resource,uan);
        assertThat(uan.getSource(),equalTo(userAnnotationSource)); // source for the annotation is set
        assertThat(uan.getResourceId(),equalTo(uri)); // resourceId for the annotation is set.
        assertThat(m,allOf(
                hasEntry(userAnnotationSource,uan)
                ,hasKey(otherSource)
                ));
        assertThat(m.size(),is(2));
        verify(cache,context,io);
    }
    
    /** test for remove a user annotation where there is additional annotations. */
    public void testRemoveUserAnnotationToExistingSet() throws Exception {

        expect(io.getSourcesList()).andReturn(Collections.EMPTY_LIST);
        
        final Map<AnnotationSource,Object>  m = new HashMap<AnnotationSource, Object>();

        assertFalse(otherSource.equals(userAnnotationSource));
        m.put(otherSource,new Annotation());
        m.put(userAnnotationSource,new UserAnnotation());
        final Element e = new Element(uri,m);        
        expect(cache.get(uri)).andReturn(e); 
        
        cache.put(new Element(uri,m)); // it's stored in the cache.
        
        io.removeUserAnnotation(resource); //deletion is persisted
        
        replay(cache,context,io);
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);

        asi.removeUserAnnotation(resource);
        assertThat(m,allOf(not(hasKey(userAnnotationSource))
                , hasKey(otherSource)
        )  
        );
        assertThat(m.size(),is(1));        
    
        verify(cache,context,io);
    }
    
    
    public void testInstantiateWithSources() throws Exception {
        final AnnotationSource dyn = new DynamicAnnotationSource() {
            @Override
            public Annotation getAnnotationFor(final Resource r) {
                return null;
            }

            @Override
            public boolean shouldCache() {
                return false;
            }
        };
        final List<AnnotationSource> sources = new ArrayList<AnnotationSource>();
        sources.add(otherSource);
        sources.add(userAnnotationSource);
        sources.add(dyn);
        
        expect(io.getSourcesList()).andReturn(sources);
        final List<Annotation> uaAnns = new ArrayList<Annotation>();
        uaAnns.add(uan);
        expect(io.load(userAnnotationSource)).andReturn(uaAnns);
        expect(io.load(otherSource)).andThrow(new RuntimeException());
        
        expect(cache.get(uri)).andReturn(null);
        final Map<AnnotationSource, UserAnnotation> m = new HashMap<AnnotationSource, UserAnnotation>();
        m.put(userAnnotationSource,uan);
        final Element e= new Element(uri,m);
        cache.put(e);
        replay(cache,context,io);
        
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);
            
        assertThat(asi.listSources(),allOf(hasItemInArray(otherSource)
                ,hasItemInArray(userAnnotationSource)
                ,hasItemInArray(dyn)));
                
        verify(cache,context,io);
    }

    
    public void testGetLocalAnnotations() throws Exception {
        expect(io.getSourcesList()).andReturn(Collections.EMPTY_LIST);
        // case for no entry
        expect(cache.get(uri)).andReturn(null);

        // case for an entry
        final Map<AnnotationSource, Annotation> m = new HashMap<AnnotationSource, Annotation>();
        m.put(userAnnotationSource,uan);
        final Annotation otherAnn = new Annotation();
        m.put(otherSource,otherAnn);
        final Element e = new Element(uri,m);
       
        expect(cache.get(uri)).andReturn(e);        
        replay(cache,context,io);
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);

        // test when there's nothing in the cache.
        
        assertFalse( asi.getLocalAnnotations(resource).hasNext());
// test when there's an entry in the cache.        
        final Iterator it = asi.getLocalAnnotations(resource);
        assertTrue(it.hasNext());
        assertThat((List<Annotation>)IteratorUtils.toList(it),allOf(hasItem(uan),hasItem(otherAnn)));
        
// test for an erroneous inputs.        
        assertFalse(asi.getLocalAnnotations((Resource)null).hasNext());
        verify(cache,context,io);
    }
    
    public void testProcessLocalAnnotations() throws Exception {
        expect(io.getSourcesList()).andReturn(Collections.EMPTY_LIST);
        // case for no entry
        expect(cache.get(uri)).andReturn(null);

        // case for an entry
        final Map<AnnotationSource, Annotation> m = new HashMap<AnnotationSource, Annotation>();
        m.put(userAnnotationSource,uan);
        final Annotation otherAnn = new Annotation();
        m.put(otherSource,otherAnn);
        final Element e = new Element(uri,m);
       
        expect(cache.get(uri)).andReturn(e);        
        replay(cache,context,io);
        
        final AnnotationProcessor proc = createMock(AnnotationProcessor.class);
        
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);

        // test when there's nothing in the cache.
        replay(proc);
        asi.processLocalAnnotations(resource,proc);
        verify(proc);
// test when there's an entry in the cache.
        reset(proc);
        proc.process(otherAnn);
        proc.process(uan);
        replay(proc);
        asi.processLocalAnnotations(resource,proc);
        verify(proc);        
// test for an erroneous inputs.
        reset(proc);
        replay(proc);        
        asi.processLocalAnnotations((Resource)null,proc);
        verify(proc);
        
        asi.processLocalAnnotations(resource,null);        
        verify(cache,context,io);
    }

/** exiting local cache entries, and dynamic returns nothing */
    public void testProcessRemainingAnnotations1() throws Exception {
        final AnnotationSource dyn = new DynamicAnnotationSource() {
            @Override
            public Annotation getAnnotationFor(final Resource r) {
                return null;
            }

            @Override
            public boolean shouldCache() {
                return false;
            }
        };
        final List<AnnotationSource> sources = new ArrayList<AnnotationSource>();
        sources.add(otherSource);
        sources.add(userAnnotationSource);
        sources.add(dyn);
        expect(io.getSourcesList()).andReturn(sources);
        expect(io.load(otherSource)).andReturn(null);
        expect(io.load(userAnnotationSource)).andReturn(null);
        // case for no entry
        
    //    expect(cache.get(uri)).andReturn(null);

        // case for an entry
        final Map<AnnotationSource, Annotation> m = new HashMap<AnnotationSource, Annotation>();
        m.put(userAnnotationSource,uan);
        final Annotation otherAnn = new Annotation();
        m.put(otherSource,otherAnn);
        final Element e = new Element(uri,m);       
        expect(cache.get(uri)).andReturn(e);
        
        // case for annotation already fetched.
     //   expect(cache.get(uri)).andReturn(e);
        
        final AnnotationProcessor proc = createMock(AnnotationProcessor.class);
        replay(cache,context,io,proc);
                
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);

        asi.processRemainingAnnotations(resource,proc);      
        verify(cache,context,io,proc);
    }
    
/** existing cache entries, and dynamic returns something, no cache. */
    public void testProcessRemainingAnnotations2() throws Exception {
        final Annotation dynAnn = new Annotation();
        final AnnotationSource dyn = new DynamicAnnotationSource() {
            @Override
            public Annotation getAnnotationFor(final Resource r) {
                return dynAnn;
            }

            @Override
            public boolean shouldCache() {
                return false;
            }
        };
        final List<AnnotationSource> sources = new ArrayList<AnnotationSource>();
        sources.add(otherSource);
        sources.add(userAnnotationSource);
        sources.add(dyn);
        expect(io.getSourcesList()).andReturn(sources);
        expect(io.load(otherSource)).andReturn(null);
        expect(io.load(userAnnotationSource)).andReturn(null);

        // case for an entry
        final Map<AnnotationSource, Annotation> m = new HashMap<AnnotationSource, Annotation>();
        m.put(userAnnotationSource,uan);
        final Annotation otherAnn = new Annotation();
        m.put(otherSource,otherAnn);
        final Element e = new Element(uri,m);       
        expect(cache.get(uri)).andReturn(e);

        final AnnotationProcessor proc = createMock(AnnotationProcessor.class);
        proc.process(dynAnn);
        replay(cache,context,io,proc);
               
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);

        asi.processRemainingAnnotations(resource,proc);      
        verify(cache,context,io,proc);
    }
    
    /** existing cache entries, and dynamic returns something, should cache. */
    public void testProcessRemainingAnnotations3() throws Exception {
        final Annotation dynAnn = new Annotation();
        final AnnotationSource dyn = new DynamicAnnotationSource() {
            @Override
            public Annotation getAnnotationFor(final Resource r) {
                return dynAnn;
            }

            @Override
            public boolean shouldCache() {
                return true;
            }
        };
        final List<AnnotationSource> sources = new ArrayList<AnnotationSource>();
        sources.add(otherSource);
        sources.add(userAnnotationSource);
        sources.add(dyn);
        expect(io.getSourcesList()).andReturn(sources);
        expect(io.load(otherSource)).andReturn(null);
        expect(io.load(userAnnotationSource)).andReturn(null);

        final Map<AnnotationSource, Annotation> m = new HashMap<AnnotationSource, Annotation>();
        m.put(userAnnotationSource,uan);
        final Annotation otherAnn = new Annotation();
        m.put(otherSource,otherAnn);
        final Element e = new Element(uri,m);       
        expect(cache.get(uri)).andReturn(e);
        cache.put(e);
        
        final AnnotationProcessor proc = createMock(AnnotationProcessor.class);
        proc.process(dynAnn);
        replay(cache,context,io,proc);
               
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);

        asi.processRemainingAnnotations(resource,proc);     
        assertThat(m,hasEntry(dyn,dynAnn));
        assertThat(m.size(),is(3));
        verify(cache,context,io,proc);
    }
    
    // throws error.
    /** existing cache entries, and dynamic throws an exception
     * could do with verifying that further dynamic annotaionts are still queried.. */
    public void testProcessRemainingAnnotations4() throws Exception {
        final Annotation dynAnn = new Annotation();
        final AnnotationSource dyn = new DynamicAnnotationSource() {
            @Override
            public Annotation getAnnotationFor(final Resource r) {
                throw new RuntimeException();
            }

            @Override
            public boolean shouldCache() {
                return true;
            }
        };
        final List<AnnotationSource> sources = new ArrayList<AnnotationSource>();
        sources.add(otherSource);
        sources.add(userAnnotationSource);
        sources.add(dyn);
        expect(io.getSourcesList()).andReturn(sources);
        expect(io.load(otherSource)).andReturn(null);
        expect(io.load(userAnnotationSource)).andReturn(null);

        final Map<AnnotationSource, Annotation> m = new HashMap<AnnotationSource, Annotation>();
        m.put(userAnnotationSource,uan);
        final Annotation otherAnn = new Annotation();
        m.put(otherSource,otherAnn);
        final Element e = new Element(uri,m);       
        expect(cache.get(uri)).andReturn(e);
        
        final AnnotationProcessor proc = createMock(AnnotationProcessor.class);
        replay(cache,context,io,proc);
               
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);

        asi.processRemainingAnnotations(resource,proc);     
        assertThat(m,not(hasEntry(dyn,dynAnn)));
        assertThat(m.size(),is(2));
        verify(cache,context,io,proc);
    }    
    // returns a user annotation
    /** existing cache entries, and dynamic returns a user annotation, should cache. */
    public void testProcessRemainingAnnotations5() throws Exception {
        final UserAnnotation dynAnn = new UserAnnotation();
        final AnnotationSource dyn = new DynamicAnnotationSource() {
            @Override
            public Annotation getAnnotationFor(final Resource r) {
                return dynAnn;
            }

            @Override
            public boolean shouldCache() {
                return true;
            }
        };
        final List<AnnotationSource> sources = new ArrayList<AnnotationSource>();
        sources.add(otherSource);
        sources.add(userAnnotationSource);
        sources.add(dyn);
        expect(io.getSourcesList()).andReturn(sources);
        expect(io.load(otherSource)).andReturn(null);
        expect(io.load(userAnnotationSource)).andReturn(null);

        final Map m = new HashMap();
        m.put(userAnnotationSource,uan);
        final Annotation otherAnn = new Annotation();
        m.put(otherSource,otherAnn);
        final Element e = new Element(uri,m);       
        expect(cache.get(uri)).andReturn(e);
        cache.put(e);
        
        final AnnotationProcessor proc = createMock(AnnotationProcessor.class);
        proc.process(dynAnn);
        replay(cache,context,io,proc);
               
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);

        asi.processRemainingAnnotations(resource,proc);     
        assertThat((Map<AnnotationSource,UserAnnotation>) m,hasEntry(dyn,dynAnn));
        assertThat(m.size(),is(3));
        verify(cache,context,io,proc);
    }
    
    /** no pre-existing cache object */
    public void testProcessRemainingAnnotations6() throws Exception {
        final UserAnnotation dynAnn = new UserAnnotation();
        final AnnotationSource dyn = new DynamicAnnotationSource() {
            @Override
            public Annotation getAnnotationFor(final Resource r) {
                return dynAnn;
            }

            @Override
            public boolean shouldCache() {
                return true;
            }
        };
        final List<AnnotationSource> sources = new ArrayList<AnnotationSource>();
        sources.add(otherSource);
        sources.add(userAnnotationSource);
        sources.add(dyn);
        expect(io.getSourcesList()).andReturn(sources);
        expect(io.load(otherSource)).andReturn(null);
        expect(io.load(userAnnotationSource)).andReturn(null);

        expect(cache.get(uri)).andReturn(null);
        final Map<AnnotationSource, UserAnnotation> m = new HashMap<AnnotationSource, UserAnnotation>();
        m.put(dyn,dynAnn);
        final Element e = new Element(uri,m);       
        cache.put(e);
        
        final AnnotationProcessor proc = createMock(AnnotationProcessor.class);
        proc.process(dynAnn);
        replay(cache,context,io,proc);
               
        final AnnotationServiceImpl asi = new AnnotationServiceImpl(cache,context,io);

        asi.processRemainingAnnotations(resource,proc);     
        verify(cache,context,io,proc);
    }    
}
