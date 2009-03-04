/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import static org.easymock.EasyMock.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.Selectors;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.alternatives.HeadlessUIComponent;
import org.astrogrid.desktop.alternatives.InThreadExecutor;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.util.TablesImplUnitTest;
import org.easymock.IArgumentMatcher;
import org.w3c.dom.Document;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

/**
 * @author Noel Winstanley
 * @since Jun 13, 200612:25:53 AM
 */
public class DalUnitTest extends TestCase {


    /*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mockReg = createMock(Registry.class);
        mockCxt = createMock(UIContext.class);
        // set up the context as a stub.
        final HeadlessUIComponent comp = new HeadlessUIComponent("test",mockCxt);
        expect(mockCxt.findMainWindow())
            .andStubReturn(comp);
        final InThreadExecutor exec = new InThreadExecutor();
        expect(mockCxt.getExecutor())
            .andStubReturn(exec);
        final Configuration conf = createNiceMock(Configuration.class);
        replay(conf);
        expect(mockCxt.getConfiguration())
            .andStubReturn(conf);
        final EventList el = new BasicEventList();
        expect(mockCxt.getTasksList()).andStubReturn(el);
        
        mockVFS = createMock(FileSystemManager.class);		
		dal = new TestDAL(mockReg,mockVFS,mockCxt);
		u = new URL("http://www.slashdot.org/foo/");	
		localSiapURL = TablesImplUnitTest.class.getResource("siap.vot");
		nonCompliantSiapService = new URL("http://www.slashdot.org");		
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		mockReg = null;
		mockVFS = null;
		mockCxt = null;
		dal = null;
		u = null;
		localSiapURL = null;
		nonCompliantSiapService = null;
	}
	protected FileSystemManager mockVFS;
	private Registry mockReg;
    protected UIContext mockCxt;	
	private DALImpl dal;
	private URL u;
	private URL localSiapURL;
	private URL nonCompliantSiapService;

	public void testResolveEndpointURL() throws InvalidArgumentException, NotFoundException, URISyntaxException {
	    replay(mockVFS,mockReg);
		final URL resolved = dal.resolveEndpoint(new URI(u.toString()));
		assertEquals(resolved,u);
	}
	/* @todo implement - if we can find a way to mock resources
	public void testResolveEndpointIVO() throws NotFoundException, ServiceException, URISyntaxException, InvalidArgumentException {
		myspaceControl.replay();
		URI uri = new URI("ivo://wibble");
		mockReg.getResource(uri);
		Resource info = new ResourceInformation(uri,null,null,u,null);
		registryControl.setReturnValue(info);
		registryControl.replay();
		URL resolved = dal.resolveEndpoint(uri);
		assertEquals(resolved,u);
	}
	*/
	
	public void testResolveEndpointIVOUnknown() throws NotFoundException, ServiceException, URISyntaxException, InvalidArgumentException {
		final URI uri = new URI("ivo://wibble");
		expect(mockReg.getResource(uri)).andThrow(new NotFoundException());
        replay(mockVFS,mockReg);
		try {
			final URL resolved = dal.resolveEndpoint(uri);
			fail("expected to fail");
		} catch (final NotFoundException e) {
			// ok
		}
		verify(mockVFS,mockReg);
	}
	
	public void testResolveEndpointUnknownScheme() throws NotFoundException, URISyntaxException {
        replay(mockVFS,mockReg);
		try {
			dal.resolveEndpoint(new URI("isbn:1023456"));
			fail("expected to fail");
		} catch (final InvalidArgumentException e) {
			// ok
		}
	}
	
	
	
	
	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.DALImpl.addOption(URL, String, String)'
	 */
	public void testAddOption() throws InvalidArgumentException{

		final URL u1 = dal.addOption(u,"page","foo 32");
		assertNotNull(u1);
		assertEquals(u.getHost(),u1.getHost());
		assertEquals(u.getPath(),u1.getPath());
		assertNull(u.getQuery());
		assertNotNull(u1.getQuery());
		assertEquals("page=foo+32",u1.getQuery());
	}
	
	public void testAddOptionToExistingOptions() throws InvalidArgumentException {

		final URL url1 = dal.addOption(u,"page","foo 32");
		final URL url2 = dal.addOption(url1,"length","32");
		assertNotNull(url2);
		assertEquals("page=foo+32&length=32",url2.getQuery());
	}
	
	public void testAddOptionThatIsAlreadyPresent() throws InvalidArgumentException {

		final URL url1 = dal.addOption(u,"page","foo 32");
		final URL url2 = dal.addOption(url1,"page","foo 32");
		assertNotNull(url2);
		assertEquals("page=foo+32",url2.getQuery());
	}

	public void testAddOptionNulls() {

		try {
			dal.addOption(null,"foo","bar");
			fail("expected to fail");
		} catch(final InvalidArgumentException e) {
			//ok
		}
		try {
			dal.addOption(u,null,"bar");
			fail("expected to fail");
		} catch(final InvalidArgumentException e) {
			//ok
		}	
	}


	public void testAddNullOptionValue() throws InvalidArgumentException {

		final URL u1 = dal.addOption(u,"page",null);
		assertNotNull(u1);
		assertEquals(u.getHost(),u1.getHost());
		assertEquals(u.getPath(),u1.getPath());
		assertNull(u.getQuery());
		assertNotNull(u1.getQuery());
		assertEquals("page=",u1.getQuery());
	}

	public void testAddEmptyOptionValue() throws InvalidArgumentException {

		final URL u1 = dal.addOption(u,"page","");
		assertNotNull(u1);
		assertEquals(u.getHost(),u1.getHost());
		assertEquals(u.getPath(),u1.getPath());
		assertNull(u.getQuery());
		assertNotNull(u1.getQuery());
		assertEquals("page=",u1.getQuery());		
	}
	
	/** some odd services are registered with garbage at the end of the parameter string 
	 * @throws MalformedURLException 
	 * @throws InvalidArgumentException */
	public void testAddOptionTrailingQuestionMark() throws MalformedURLException, InvalidArgumentException {

		// constrcut an odd url
		u = new URL(u.toString() + "?");
		// url class reports that it does have a query.
		assertNotNull(u.getQuery());
		assertEquals(0,u.getQuery().length());
		
		//pass it into the dal code.
		final URL u1 = dal.addOption(u,"page","foo 32");
		assertNotNull(u1);
		assertEquals(u.getHost(),u1.getHost());
		assertEquals(u.getPath(),u1.getPath());
		assertNotNull(u1.getQuery());
		assertEquals("page=foo+32",u1.getQuery());		
	}
	
	public void testAddOptionTrailingAmpersand() throws MalformedURLException, InvalidArgumentException {

		// constrcut an odd url
		u = new URL(u.toString() + "?foo=bar&");
		// url class reports that it does have a query.
		assertNotNull(u.getQuery());
		assertEquals("foo=bar&",u.getQuery());
		
		//pass it into the dal code.
		final URL u1 = dal.addOption(u,"page","foo 32");
		assertNotNull(u1);
		assertEquals(u.getHost(),u1.getHost());
		assertEquals(u.getPath(),u1.getPath());
		assertNotNull(u1.getQuery());
		assertEquals("foo=bar&page=foo+32",u1.getQuery());		
	}
	
	public void testExecute() throws Exception {
		assertNotNull(localSiapURL);
		final Map[] r = dal.execute(localSiapURL);
		assertNotNull(r);
		assertTrue(r.length > 0);
		for (int i = 0; i < r.length; i++) {
			assertNotNull(r[i]);
			assertEquals(r[0].keySet(),r[i].keySet());
			if (i > 0) {
				assertFalse(r[0].values().equals(r[i].values()));
			}
			for (final Iterator j = r[i].values().iterator(); j.hasNext() ; ) {
				assertNotNull(j.next());
			}
		}
	}
	
	public void testExecuteFaultyService()  {
		try {
			dal.execute(nonCompliantSiapService);
			fail("expected to fail");
		} catch (final ServiceException e) {
			// ok
		}
		
	}
	
	public void testExecuteVotable() throws ServiceException {
		assertNotNull(localSiapURL);
		final Document d = dal.executeVotable(localSiapURL);
		assertNotNull(d);
	}
	
	public void testExecuteVotableFaultyService() {
		try {
			dal.executeVotable(nonCompliantSiapService);
			fail("expected to fail");
		} catch (final ServiceException e) {
			// ok
		}		
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.DALImpl.saveResults(URL, URI)'
	 */
	public void testExecuteAndSaveIvo() throws Exception {
		final URI location = new URI("ivo://org.astrogrid/test#storage/foo/result.vot");
		final FileObject src = createMock(FileObject.class);
		final FileObject dest = createMock(FileObject.class);
		final FileName fn = createMock(FileName.class);
		expect(dest.getName()).andReturn(fn);
		expect(fn.getScheme()).andReturn("ivo");

		expect(	mockVFS.resolveFile(localSiapURL.toString()))
		    .andReturn(src);
		expect(mockVFS.resolveFile(location.toString()))
		    .andReturn(dest);
		dest.copyFrom(src,Selectors.SELECT_SELF);
		
        replay(mockVFS,src,dest,fn,mockReg,mockCxt);
		dal.executeAndSave(localSiapURL,location);
        verify(mockVFS,src,dest,fn,mockReg,mockCxt);
	}
	
	/** not easy to test - and any point??
	public void testExecuteAndSaveFiile() throws Exception {
		
	}*/
	
	public void testSaveDatasetsIvo() throws Exception {
		final URI location = new URI("ivo://org.astrogrid/test#storage/foo");
		final FileName fn = createNiceMock(FileName.class);
		final FileObject f = createNiceMock(FileObject.class);
		expect(mockVFS.resolveFile(location.toString()))
		    .andReturn(f);
		expect(f.getName()).andStubReturn(fn);
		expect(f.exists()).andStubReturn(true);		
		expect(f.isWriteable()).andStubReturn(true);
		
		final FileObject dest = createNiceMock(FileObject.class);
		expect(f.resolveFile((String)anyObject()))
		    .andStubReturn(dest);		
		expect(dest.exists()).andStubReturn(false);
		expect(dest.getName()).andStubReturn(fn);

		
		// find size of the dataset.
		final int resultSize = dal.execute(localSiapURL).length;
		final FileObject src = createNiceMock(FileObject.class);
		expect(mockVFS.resolveFile((String)anyObject()))
		    .andReturn(src)
		    .times(resultSize);
       
        replay(mockVFS,mockReg,mockCxt,f,src,dest,fn);
		dal.saveDatasets(localSiapURL,location);
        verify(mockVFS,mockReg,mockCxt);

	}
	
	public void testSaveSubsetDatasetsIvo() throws Exception {
		final URI location = new URI("ivo://org.astrogrid/test#storage/foo");
        final FileName fn = createNiceMock(FileName.class);
        final FileObject f = createNiceMock(FileObject.class);
        expect(mockVFS.resolveFile(location.toString()))
            .andReturn(f);
        expect(f.getName()).andStubReturn(fn);
        expect(f.exists()).andStubReturn(true);     
        expect(f.isWriteable()).andStubReturn(true);
        
        final FileObject dest = createNiceMock(FileObject.class);
        expect(f.resolveFile((String)anyObject()))
            .andStubReturn(dest);       
        expect(dest.exists()).andStubReturn(false);
        expect(dest.getName()).andStubReturn(fn);

        	

		// find size of the dataset.
		final int resultSize = dal.execute(localSiapURL).length;
		final List subset = new ArrayList();
		subset.add(new Integer(0));
		subset.add(new Integer(resultSize -1));
		subset.add(new Integer(resultSize / 2));
		subset.add(new Integer( 1));
		// call this method as many times as the subset to download
	      final FileObject src = createNiceMock(FileObject.class);
	      expect(mockVFS.resolveFile((String)anyObject()))
          .andReturn(src)
          .times(subset.size());
     
      replay(mockVFS,mockReg,mockCxt,f,src,dest,fn);

		dal.saveDatasetsSubset(localSiapURL,location,subset);
        verify(mockVFS,mockReg,mockCxt);

	}
	public static URI saveLocation(final URI in) {
	    reportMatcher(new SaveLocationMatcher(in));
	    return null;
	}
	private static class SaveLocationMatcher implements IArgumentMatcher {
	    private final Object location;
	    
        public void appendTo(final StringBuffer sb) {
            sb.append("saveLocation(")
            .append(location)
            .append(")");
        }

        public boolean matches(final Object actual) {
            if (actual != null && actual instanceof URI ) {
                return actual.toString().startsWith(location.toString() + "/");            
            } else {
                return false;
            }           
        }

        public SaveLocationMatcher(final Object location) {
            super();
            this.location = location;
        }
	}
	
	/** concrete implementatio - jst so we can test bits */
public static class TestDAL extends DALImpl {

	/**
	 * @param reg
	 * @param ms
	 * @param vf 
	 * @param cxt 
	 */
	public TestDAL(final Registry reg, final FileSystemManager vf, final UIContext cxt) {
		super(reg,  vf, cxt);
	}

	public String getRegistryAdqlQuery() {
		throw new NotImplementedException("not implemntet");
	}

	public String getRegistryQuery() {
		throw new NotImplementedException("not implemntet");
	}

	public String getRegistryXQuery() {
		throw new NotImplementedException("not implemntet");
	}

    protected URL findAccessURL(final Service s) throws InvalidArgumentException {
        return null;
    }

	
}

}
