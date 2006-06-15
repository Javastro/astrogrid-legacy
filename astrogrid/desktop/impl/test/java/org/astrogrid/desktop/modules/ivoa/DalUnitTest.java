/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.TestCase;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.easymock.MockControl;

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
	}
	private MockControl myspaceControl;
	private MockControl registryControl;
	private MyspaceInternal mockMs;
	private Registry mockReg;
	private DALImpl dal;
	private URL u;

	public void testResolveEndpointURL() throws InvalidArgumentException, NotFoundException, URISyntaxException {
		myspaceControl.replay();
		registryControl.replay();
		URL resolved = dal.resolveEndpoint(new URI(u.toString()));
		assertEquals(resolved,u);
	}
	
	public void testResolveEndpointIVO() throws NotFoundException, ServiceException, URISyntaxException, InvalidArgumentException {
		myspaceControl.replay();
		URI uri = new URI("ivo://wibble");
		mockReg.getResourceInformation(uri);
		ResourceInformation info = new ResourceInformation(uri,null,null,u,null);
		registryControl.setReturnValue(info);
		registryControl.replay();
		URL resolved = dal.resolveEndpoint(uri);
		assertEquals(resolved,u);
		
		
	}
	
	public void testResolveEndpointIVOUnknown() throws NotFoundException, ServiceException, URISyntaxException, InvalidArgumentException {
		myspaceControl.replay();
		URI uri = new URI("ivo://wibble");
		mockReg.getResourceInformation(uri);
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
	

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.DALImpl.getResults(URL)'
	 */
	//public void testGetResults() {
	//	fail("implement me");
//	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ivoa.DALImpl.saveResults(URL, URI)'
	 */
	public void testSaveResultsIvo() throws URISyntaxException, NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException {
		URI location = new URI("ivo://org.astrogrid/test#storage/foo/result.vot");
		mockMs.copyURLToContent(u,location);
		myspaceControl.replay();
		registryControl.replay();
		dal.saveResults(u,location);
		myspaceControl.verify();
		registryControl.verify();
	}
	
	//* can't mock the rest of this
//	public void testSaveResultsOther() {
//	}
	
	/** concrete implementatio - jst so we can test bits */
public static class TestDAL extends DALImpl {

	/**
	 * @param reg
	 * @param ms
	 */
	public TestDAL(Registry reg, MyspaceInternal ms) {
		super(reg, ms);
	}
}

}
