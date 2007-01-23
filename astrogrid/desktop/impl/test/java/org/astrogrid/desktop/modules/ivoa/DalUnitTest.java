/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

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
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.util.TablesImplUnitTest;
import org.easymock.AbstractMatcher;
import org.easymock.MockControl;
import org.w3c.dom.Document;

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
		myspaceControl = MockControl.createControl(MyspaceInternal.class);
		registryControl = MockControl.createControl(Registry.class);
		mockMs = (MyspaceInternal)myspaceControl.getMock();
		mockReg = (Registry)registryControl.getMock();
		dal = new TestDAL(mockReg,mockMs);
		u = new URL("http://www.slashdot.org/foo/");	
		localSiapURL = TablesImplUnitTest.class.getResource("siap.vot");
		nonCompliantSiapService = new URL("http://www.slashdot.org");		
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		myspaceControl = null;
		registryControl = null;
		mockMs = null;
		mockReg = null;
		dal = null;
		u = null;
		localSiapURL = null;
		nonCompliantSiapService = null;
	}
	private MockControl myspaceControl;
	private MockControl registryControl;
	private MyspaceInternal mockMs;
	private Registry mockReg;
	private DALImpl dal;
	private URL u;
	private URL localSiapURL;
	private URL nonCompliantSiapService;

	public void testResolveEndpointURL() throws InvalidArgumentException, NotFoundException, URISyntaxException {
		myspaceControl.replay();
		registryControl.replay();
		URL resolved = dal.resolveEndpoint(new URI(u.toString()));
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
		myspaceControl.replay();
		URI uri = new URI("ivo://wibble");
		mockReg.getResource(uri);
		registryControl.setThrowable(new NotFoundException());
		registryControl.replay();
		try {
			URL resolved = dal.resolveEndpoint(uri);
			fail("expected to fail");
		} catch (NotFoundException e) {
			// ok
		}
	}
	
	public void testResolveEndpointUnknownScheme() throws NotFoundException, URISyntaxException {
		myspaceControl.replay();
		registryControl.replay();
		try {
			dal.resolveEndpoint(new URI("isbn:1023456"));
			fail("expected to fail");
		} catch (InvalidArgumentException e) {
			// ok
		}
	}
	
	
	
	
	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.DALImpl.addOption(URL, String, String)'
	 */
	public void testAddOption() throws InvalidArgumentException{
		// expect no calls on myspace or reg.
		myspaceControl.replay();
		registryControl.replay();

		URL u1 = dal.addOption(u,"page","foo 32");
		assertNotNull(u1);
		assertEquals(u.getHost(),u1.getHost());
		assertEquals(u.getPath(),u1.getPath());
		assertNull(u.getQuery());
		assertNotNull(u1.getQuery());
		assertEquals("page=foo+32",u1.getQuery());
	}
	
	public void testAddOptionToExistingOptions() throws InvalidArgumentException {
		// expect no calls on myspace or reg.
		myspaceControl.replay();
		registryControl.replay();		
		URL url1 = dal.addOption(u,"page","foo 32");
		URL url2 = dal.addOption(url1,"length","32");
		assertNotNull(url2);
		assertEquals("page=foo+32&length=32",url2.getQuery());
	}
	
	public void testAddOptionThatIsAlreadyPresent() throws InvalidArgumentException {
		// expect no calls on myspace or reg.
		myspaceControl.replay();
		registryControl.replay();		
		URL url1 = dal.addOption(u,"page","foo 32");
		URL url2 = dal.addOption(url1,"page","foo 32");
		assertNotNull(url2);
		assertEquals("page=foo+32",url2.getQuery());
	}

	public void testAddOptionNulls() {
		// expect no calls on myspace or reg.
		myspaceControl.replay();
		registryControl.replay();		
		try {
			dal.addOption(null,"foo","bar");
			fail("expected to fail");
		} catch(InvalidArgumentException e) {
			//ok
		}
		try {
			dal.addOption(u,null,"bar");
			fail("expected to fail");
		} catch(InvalidArgumentException e) {
			//ok
		}	
	}


	public void testAddNullOptionValue() throws InvalidArgumentException {
		// expect no calls on myspace or reg.
		myspaceControl.replay();
		registryControl.replay();		
		URL u1 = dal.addOption(u,"page",null);
		assertNotNull(u1);
		assertEquals(u.getHost(),u1.getHost());
		assertEquals(u.getPath(),u1.getPath());
		assertNull(u.getQuery());
		assertNotNull(u1.getQuery());
		assertEquals("page=",u1.getQuery());
	}

	public void testAddEmptyOptionValue() throws InvalidArgumentException {
		// expect no calls on myspace or reg.
		myspaceControl.replay();
		registryControl.replay();	
		URL u1 = dal.addOption(u,"page","");
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
		// expect no calls on myspace or reg.
		myspaceControl.replay();
		registryControl.replay();
		// constrcut an odd url
		u = new URL(u.toString() + "?");
		// url class reports that it does have a query.
		assertNotNull(u.getQuery());
		assertEquals(0,u.getQuery().length());
		
		//pass it into the dal code.
		URL u1 = dal.addOption(u,"page","foo 32");
		assertNotNull(u1);
		assertEquals(u.getHost(),u1.getHost());
		assertEquals(u.getPath(),u1.getPath());
		assertNotNull(u1.getQuery());
		assertEquals("page=foo+32",u1.getQuery());		
	}
	
	public void testAddOptionTrailingAmpersand() throws MalformedURLException, InvalidArgumentException {
		// expect no calls on myspace or reg.
		myspaceControl.replay();
		registryControl.replay();
		// constrcut an odd url
		u = new URL(u.toString() + "?foo=bar&");
		// url class reports that it does have a query.
		assertNotNull(u.getQuery());
		assertEquals("foo=bar&",u.getQuery());
		
		//pass it into the dal code.
		URL u1 = dal.addOption(u,"page","foo 32");
		assertNotNull(u1);
		assertEquals(u.getHost(),u1.getHost());
		assertEquals(u.getPath(),u1.getPath());
		assertNotNull(u1.getQuery());
		assertEquals("foo=bar&page=foo+32",u1.getQuery());		
	}
	
	public void testExecute() throws Exception {
		assertNotNull(localSiapURL);
		myspaceControl.replay();
		registryControl.replay();
		Map[] r = dal.execute(localSiapURL);
		assertNotNull(r);
		assertTrue(r.length > 0);
		for (int i = 0; i < r.length; i++) {
			assertNotNull(r[i]);
			assertEquals(r[0].keySet(),r[i].keySet());
			if (i > 0) {
				assertFalse(r[0].values().equals(r[i].values()));
			}
			for (Iterator j = r[i].values().iterator(); j.hasNext() ; ) {
				assertNotNull(j.next());
			}
		}
	}
	
	public void testExecuteFaultyService()  {
		try {
			dal.execute(nonCompliantSiapService);
			fail("expected to fail");
		} catch (ServiceException e) {
			// ok
		}
		
	}
	
	public void testExecuteVotable() throws ServiceException {
		assertNotNull(localSiapURL);
		myspaceControl.replay();
		registryControl.replay();
		Document d = dal.executeVotable(localSiapURL);
		assertNotNull(d);
	}
	
	public void testExecuteVotableFaultyService() {
		try {
			dal.executeVotable(nonCompliantSiapService);
			fail("expected to fail");
		} catch (ServiceException e) {
			// ok
		}		
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.DALImpl.saveResults(URL, URI)'
	 */
	public void testExecuteAndSaveIvo() throws URISyntaxException, NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException {
		URI location = new URI("ivo://org.astrogrid/test#storage/foo/result.vot");
		mockMs.copyURLToContent(localSiapURL,location);
		myspaceControl.replay();
		registryControl.replay();
		dal.executeAndSave(localSiapURL,location);
		myspaceControl.verify();
		registryControl.verify();
	}
	
	/** not easy to test - and any point??
	public void testExecuteAndSaveFiile() throws Exception {
		
	}*/
	
	public void testSaveDatasetsIvo() throws Exception {
		final URI location = new URI("ivo://org.astrogrid/test#storage/foo");
		mockMs.copyURLToContent(localSiapURL,location); // expected values are ignored in this case - just need to tbe the correct type.
		// args to the this method must match as follows..
		myspaceControl.setMatcher(new AbstractMatcher() {
			protected boolean argumentMatches(Object expected, Object actual) {
				if (actual instanceof URI) {
					return actual.toString().startsWith(location.toString() + "/");
				} else if (actual instanceof URL) {
					return true;
				} else {
					return false;
				}
			}			
		});
		
		// find size of the dataset.
		int resultSize = dal.execute(localSiapURL).length;
		
		// call this method as many times as the dataset size.
		myspaceControl.setVoidCallable(resultSize);
		myspaceControl.replay();
		registryControl.replay();
		dal.saveDatasets(localSiapURL,location);
		myspaceControl.verify();
		registryControl.verify();

	}
	
	public void testSaveSubsetDatasetsIvo() throws Exception {
		final URI location = new URI("ivo://org.astrogrid/test#storage/foo");
		mockMs.copyURLToContent(localSiapURL,location); // expected values are ignored in this case - just need to tbe the correct type.
		// args to the this method must match as follows..
		myspaceControl.setMatcher(new AbstractMatcher() {
			protected boolean argumentMatches(Object expected, Object actual) {
				if (actual instanceof URI) {
					return actual.toString().startsWith(location.toString() + "/");
				} else if (actual instanceof URL) {
					return true;
				} else {
					return false;
				}
			}			
		});
		
		// find size of the dataset.
		int resultSize = dal.execute(localSiapURL).length;
		List subset = new ArrayList();
		subset.add(new Integer(0));
		subset.add(new Integer(resultSize -1));
		subset.add(new Integer(resultSize / 2));
		subset.add(new Integer( 1));
		// call this method as many times as the dataset size.
		myspaceControl.setVoidCallable(subset.size());
		myspaceControl.replay();
		registryControl.replay();
		dal.saveDatasetsSubset(localSiapURL,location,subset);
		myspaceControl.verify();
		registryControl.verify();

	}
	
	/** concrete implementatio - jst so we can test bits */
public static class TestDAL extends DALImpl {

	/**
	 * @param reg
	 * @param ms
	 */
	public TestDAL(Registry reg, MyspaceInternal ms) {
		super(reg, ms);
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

	
}

}
