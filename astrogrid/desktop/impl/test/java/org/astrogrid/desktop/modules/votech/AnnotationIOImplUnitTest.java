/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.iterators.EmptyIterator;
import org.apache.commons.collections.iterators.SingletonIterator;
import org.apache.commons.io.FileUtils;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import junit.framework.TestCase;
import static org.astrogrid.Fixture.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.integration.EasyMock2Adapter.adapt;
/** unit testing for anntionation io.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 22, 20089:41:26 PM
 */
public class AnnotationIOImplUnitTest extends TestCase {

    private Preference workDir;
    private XmlPersist xmlPersist;
    private IterableObjectBuilder objectBuilder;
    private AnnotationService annotationService;
    private UIContext cxt;
    private List userAnnotations;
    private UserAnnotation userAnnotation;
    private URI testURI;
    private Resource resource;
    private AnnotationSource annSource;
    private ArrayList annotations;
    private Annotation annotation;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.workDir = new Preference();
        final File tmpDir = createTempDir(this.getClass());
        workDir.setValue(tmpDir.toString());
        this.objectBuilder = createMock("objectBuilder",IterableObjectBuilder.class);

        this.xmlPersist = createMock("xmlPersist",XmlPersist.class);
        this.annotationService = createMock("annotation",AnnotationService.class);
        this.cxt = createMockContext();
        
        testURI = new URI("ivo://test.uri");
        this.resource = createMock("resource",Resource.class);
        expect(resource.getId()).andStubReturn(testURI);
        replay(resource);
        
        userAnnotations = new ArrayList();
        userAnnotation = new UserAnnotation();
        userAnnotation.setResourceId(testURI);
        userAnnotation.setFlagged(true);
        userAnnotations.add(userAnnotation);
        File annFile = new File(tmpDir,"annSource");
        FileUtils.touch(annFile);
        this.annSource = new AnnotationSource(annFile.toURI(),"annSource");
        
        annotations = new ArrayList();
        annotation = new Annotation();
        annotation.setResourceId(testURI);
        annotation.setAlternativeTitle("a title");
        annotations.add(annotation);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        workDir = null;
        objectBuilder = null;
        xmlPersist= null;
        annotationService = null;
        cxt = null;
        userAnnotations = null;
        userAnnotation = null;
        testURI = null;
        resource = null;
        annSource = null;
        annotations = null;
        annotation = null;
    }
    
    /** test implementation when given no annotation sources - so is just the user annotations */
    public void testUserAnnotations() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(EmptyIterator.INSTANCE);
        replayAll();
        AnnotationIOImpl aio = createAIO();
        
        final List sources = aio.getSourcesList();
        assertNotNull(sources);
        assertEquals(1,sources.size());
        
        assertNotNull(aio.getUserSource());
        assertSame(aio.getUserSource(),sources.get(0));
        
        verifyAll();  
    }
    
    /**try loading the user annotations. */
    public void testLoadUserAnnotations() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(EmptyIterator.INSTANCE);
        
        expect(xmlPersist.fromXml((InputStream)anyObject())).andReturn(userAnnotations);
        replayAll();
        AnnotationIOImpl aio = createAIO();
        // this collection is delivered from the xmpersist mock.
        Collection collection = aio.load(aio.getUserSource());
        assertSingleUserAnnotation(aio, collection);

        verifyAll();  
    }


    
    /** if persistence returns a null, we get an empty list. */
    public void testLoadUserAnnotationsEmpty() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(EmptyIterator.INSTANCE);
        expect(xmlPersist.fromXml((InputStream)anyObject())).andReturn(null);
        replayAll();
        AnnotationIOImpl aio = createAIO();
        // this collection is delivered from the xmpersist mock.
        Collection collection = aio.load(aio.getUserSource());
        assertNotNull(collection);
        assertTrue(collection.isEmpty());
        verifyAll();  
    }
    
    /** if the returned collection is maltyped, offending objects are removed. */
    public void testLoadUserAnnotationsMaltyped() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(EmptyIterator.INSTANCE);
        userAnnotations.add(new Object());
        userAnnotations.add(new Annotation());
        expect(xmlPersist.fromXml((InputStream)anyObject())).andReturn(userAnnotations);
        replayAll();
        AnnotationIOImpl aio = createAIO();       
        // this collection is delivered from the xmpersist mock.
        Collection collection = aio.load(aio.getUserSource());
        assertSingleUserAnnotation(aio, collection);

        verifyAll();  
    }
    
    /**try updating an annotation */
    public void testUpdateUserAnnotation() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(EmptyIterator.INSTANCE);
        
        expect(xmlPersist.fromXml((InputStream)anyObject())).andReturn(userAnnotations);
        expect(this.annotationService.getUserAnnotation(testURI)).andReturn(userAnnotation);
        xmlPersist.toXml(
                eq(Collections.singletonList(userAnnotation))
                ,isA(OutputStream.class)
                );
        replayAll();
        AnnotationIOImpl aio = createAIO();
        // this collection is delivered from the xmpersist mock.
        Collection collection = aio.load(aio.getUserSource());
        aio.updateUserAnnotation(userAnnotation);
        verifyAll();  
    }
    
    /** try removing an annotation */
    public void testRemoveUserAnnotation() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(EmptyIterator.INSTANCE);
        
        expect(xmlPersist.fromXml((InputStream)anyObject())).andReturn(userAnnotations);
        xmlPersist.toXml(
                eq(Collections.emptyList())
                ,isA(OutputStream.class)
                );
        replayAll();
        AnnotationIOImpl aio = createAIO();
        // this collection is delivered from the xmpersist mock.
        Collection collection = aio.load(aio.getUserSource());
        aio.removeUserAnnotation(resource);
        verifyAll();  
    }
/** try adding an annotaiton */
    public void testAddUserAnnotation() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(EmptyIterator.INSTANCE);
        
        expect(xmlPersist.fromXml((InputStream)anyObject())).andReturn(userAnnotations);
        UserAnnotation ua2 = new UserAnnotation();
        URI uri2 = new URI("ivo://another.one");
        ua2.setResourceId(uri2);
        expect(this.annotationService.getUserAnnotation(testURI)).andReturn(userAnnotation);
        expect(this.annotationService.getUserAnnotation(uri2)).andReturn(ua2);     

        xmlPersist.toXml(
                  adapt(hasItems(userAnnotation,ua2))
                ,isA(OutputStream.class)
                );
        replayAll();
        AnnotationIOImpl aio = createAIO();
        // this collection is delivered from the xmpersist mock.
        Collection collection = aio.load(aio.getUserSource());
        aio.updateUserAnnotation(ua2);
        verifyAll();  
    }
    
    /** try removing a user annotation that doesn't exist. */
    public void testRemoveNonExistentUserAnnotation() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(EmptyIterator.INSTANCE);
        
        expect(xmlPersist.fromXml((InputStream)anyObject())).andReturn(userAnnotations);
        expect(this.annotationService.getUserAnnotation(testURI)).andReturn(userAnnotation);        
        xmlPersist.toXml(
                eq(Collections.singletonList(userAnnotation))
                ,isA(OutputStream.class)
                );
        URI uri2 = new URI("ivo://another.one");
        Resource otherResource = createMock(Resource.class);
        expect(otherResource.getId()).andStubReturn(uri2);
        replay(otherResource);
        replayAll();
        AnnotationIOImpl aio = createAIO();
        // this collection is delivered from the xmpersist mock.
        Collection collection = aio.load(aio.getUserSource());
        aio.removeUserAnnotation(otherResource);
        verifyAll();  
    }    
    
    
    // try modifying user annotations before loading them.
    
    /**try updating an annotation */
    public void testModifyBeforeLoadUserAnnotation() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(EmptyIterator.INSTANCE);
 
        replayAll();
        AnnotationIOImpl aio = createAIO();

         // not loaded - straight into modifying
        try {
            aio.updateUserAnnotation(userAnnotation);
            fail("expected to fail");
        } catch (IllegalStateException e) {
            // ok
        }
        verifyAll();  
    }

    
    /**try loading the user annotations. */
    public void testLoadAnnotations() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(new SingletonIterator(annSource));
        
        expect(xmlPersist.fromXml((InputStream)anyObject())).andReturn(annotations);
        replayAll();
        AnnotationIOImpl aio = createAIO();
        final List<AnnotationSource> sourcesList = aio.getSourcesList();
        assertEquals(2,sourcesList.size());
        // user annotation source is always first.
        assertSame(aio.getUserSource(),sourcesList.get(0));
        assertSame(annSource,sourcesList.get(1));

        Collection collection = aio.load(annSource);
        assertSingleAnnotation( collection);
        verifyAll();  
    }


    
    /** if persistence returns a null, we get an empty list. */
    public void testLoadAnnotationsEmpty() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(new SingletonIterator(annSource));
        expect(xmlPersist.fromXml((InputStream)anyObject())).andReturn(null);
        replayAll();
        AnnotationIOImpl aio = createAIO();
        // this collection is delivered from the xmpersist mock.
        Collection collection = aio.load(annSource);
        assertNotNull(collection);
        assertTrue(collection.isEmpty());
        verifyAll();  
    }
    
    /** if the returned collection is maltyped, offending objects are removed. */
    public void testLoadAnnotationsMaltyped() throws Exception {
        expect(objectBuilder.creationIterator()).andReturn(new SingletonIterator(annSource));
        annotations.add(new Object()); // expect to be removed
        annotations.add(new Annotation()); // expect to keep
        annotations.add(new UserAnnotation()); // expect to keep
        assertEquals(4,annotations.size());
        expect(xmlPersist.fromXml((InputStream)anyObject())).andReturn(annotations);
        replayAll();
        AnnotationIOImpl aio = createAIO();       
        // this collection is delivered from the xmpersist mock.
        Collection collection = aio.load(annSource);
        assertEquals(3,annotations.size());

        verifyAll();  
    }
// supporting methods    
    
    /**
     * verify  all mocks
     */
    private void verifyAll() {
        verify(xmlPersist,objectBuilder,annotationService,cxt);
    }

    /**
     * replay all mocks
     */
    private void replayAll() {
        replay(xmlPersist,objectBuilder,annotationService,cxt);
    }

    /**create the annotations impl
     * @return
     * @throws IOException
     */
    private AnnotationIOImpl createAIO() throws IOException {
        AnnotationIOImpl aio = new AnnotationIOImpl(workDir
                ,objectBuilder
                ,xmlPersist
                ,annotationService
                ,cxt);
        // ok, try loading the user annotations.
        assertFalse(aio.userAnnotationsFile.exists()) ; // no user annotations
        FileUtils.touch(aio.userAnnotationsFile); // create the file - this prevents 'openStream' giving an error
        return aio;
    }
    
    /** assert that the collection contains a single userannotation.
     * @param aio
     * @param collection
     */
    private void assertSingleUserAnnotation(AnnotationIOImpl aio,
            Collection collection) {
        assertSame(userAnnotations,collection);
        assertEquals(1,collection.size());
        assertEquals(userAnnotation,collection.iterator().next());
        assertEquals(1,aio.userAnnotationIds.size());
        assertEquals(testURI,aio.userAnnotationIds.iterator().next());
    }
    
    private void assertSingleAnnotation( Collection collection) {
        assertSame(annotations,collection);
        assertEquals(1,collection.size());
        assertEquals(annotation,collection.iterator().next());
    }


}
