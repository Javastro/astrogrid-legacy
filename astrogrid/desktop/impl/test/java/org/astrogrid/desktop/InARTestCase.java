/**
 * 
 */
package org.astrogrid.desktop;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.hivemind.Registry;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.framework.ACRInternal;
/** Subclass of test case that runs in-ar.
 * @author Noel Winstanley
 * @since Jan 8, 200712:10:53 PM
 */
public class InARTestCase extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
	}
    protected ACR getACR() throws Exception {
        final ACRInternal acr = ARTestSetup.fixture.getACR();
        assertNotNull(acr);
        return acr;
    }	
    
    protected Registry getHivemindRegistry() throws Exception {
    	final Registry hivemindRegistry2 = ARTestSetup.fixture.getHivemindRegistry();
    	assertNotNull(hivemindRegistry2);
    	return hivemindRegistry2;
    }
    
    /** quite hacky way of getting at the test user
     * will only be valid if run in an ARTestSetup where login has been requested
     * @return
     */
    protected String getUsername() {
    	assertTrue("No ag.test.username defined", System.getProperties().containsKey(ARTestSetup.AG_TEST_USERNAME));
    	final String s =  System.getProperty(ARTestSetup.AG_TEST_USERNAME,null);
    	return s;
    }
    
    // access the test user's community.
    protected String getCommunity() {
    	assertTrue("No ag.test.community defined",System.getProperties().containsKey(ARTestSetup.AG_TEST_COMMUNITY));
    	final String s =  System.getProperty(ARTestSetup.AG_TEST_COMMUNITY);
    	return s;
    }

    protected String getPassord() {
    	assertTrue("No ag.test.password defined",System.getProperties().containsKey(ARTestSetup.AG_TEST_PASSWORD));
    	final String s =  System.getProperty(ARTestSetup.AG_TEST_PASSWORD);
    	return s;
    }

	
// assertions.
	/** test  specific ar service exists.
	 * @param clazz
	 * @param name
	 * @return the service. (for further testing).
	 * @throws ACRException
	 * @throws InvalidArgumentException
	 * @throws NotFoundException
	 */
	public final   <T> T assertServiceExists(final Class<T> clazz, final String name) throws Exception {
		final ACR reg = getACR();
		final Object r =  reg.getService(clazz);
		assertNotNull(r);
		assertTrue(clazz.isInstance(r));
		final Object r1 =  reg.getService(name);
		assertNotNull(r1);
		//assertSame(r,r1); won't be the same for the case of RMI. check the same interfaces are supported on both objects instead.
		assertEquals(Arrays.asList(r.getClass().getInterfaces())
		        ,Arrays.asList(r1.getClass().getInterfaces())
		        );
		return (T) r;
	}
	
	/** test a specific underlying component exists */
	public final <T>  T assertComponentExists(final Class<T> clazz, final String name) throws Exception {
		final Registry hivemindRegistry = getHivemindRegistry();
		final Object r =  hivemindRegistry.getService(name, clazz);
		assertNotNull(r);
		assertTrue(clazz.isInstance(r));
		return (T) r;
	}	
}
