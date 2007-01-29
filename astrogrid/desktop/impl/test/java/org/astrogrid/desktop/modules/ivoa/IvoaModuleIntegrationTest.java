/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Adql074;
import org.astrogrid.acr.ivoa.CacheFactory;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.ExternalRegistry;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.SkyNode;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** Integration test that verifies all components in ivoa module are at least instantiable
 *  - by fetching each in turn, and callinig trivial methods on them
 *  
 *  this test verifes that the hivemind config for this module is working - not
 *  overly concerned with funciotnlaity of the components themselves.
 * @author Noel Winstanley
 * @since Jan 7, 200712:58:25 AM
 */
public class IvoaModuleIntegrationTest extends InARTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		reg = ARTestSetup.fixture.getACR();
		assertNotNull(reg);		
	}
	ACR reg;

	protected void tearDown() throws Exception {
		super.tearDown();
		reg = null;
	}
	
	public void testExternalRegistry() throws Exception {
		Object r = assertServiceExists(ExternalRegistry.class, "ivoa.externalRegistry");
		
		// call a method - triggers invocation.
		assertNotNull(((ExternalRegistry)r).getRegistryOfRegistriesEndpoint());
	}
	
	public void testRegistry() throws Exception {
		Registry r = (Registry) assertServiceExists(Registry.class, "ivoa.registry");
		assertNotNull(r.getFallbackSystemRegistryEndpoint());
		assertNotNull(r.getSystemRegistryEndpoint());
	}
	
	public void testAdql074() throws Exception {
		Adql074 qd = (Adql074) assertServiceExists(Adql074.class, "ivoa.adql074");
		qd.toString(); //@todo doesn't test fully.
	}

	
	public void testSKyNode() throws Exception {
		SkyNode sn  = (SkyNode)assertServiceExists(SkyNode.class,"ivoa.skyNode");
		sn.getRegistryXQuery(); // forces service instantiation.
	}
	
	public void testCOne() throws Exception {
		Cone c = (Cone)assertServiceExists(Cone.class,"ivoa.cone");
		c.getRegistryXQuery(); 
	}
	
	public void testSiap() throws Exception {
		Siap c = (Siap)assertServiceExists(Siap.class,"ivoa.siap");
		c.getRegistryXQuery(); 
	}
	
	public void testSsap() throws Exception {
		Ssap c = (Ssap)assertServiceExists(Ssap.class,"ivoa.ssap");
		c.getRegistryXQuery(); 
	}
	
	public void testCache() throws Exception {
		CacheFactory fac = (CacheFactory)assertComponentExists(CacheFactory.class, "ivoa.cache");
		//@todo find something non-descrutive to do here.
	}
	

    public static Test suite() {
        return new ARTestSetup(new TestSuite(IvoaModuleIntegrationTest.class));
    }
}
