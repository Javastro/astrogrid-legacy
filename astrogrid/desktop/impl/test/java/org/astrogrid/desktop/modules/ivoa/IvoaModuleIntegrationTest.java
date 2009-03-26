/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Adql;
import org.astrogrid.acr.ivoa.CacheFactory;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.ExternalRegistry;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.Vosi;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** Integration test that verifies all components in ivoa module are at least instantiable
 *  - by fetching each in turn, and callinig trivial methods on them
 *  
 *  this test verifes that the hivemind config for this module is working - not
 *  overly concerned with funciotnlaity of the components themselves.
 * @author Noel Winstanley
 * @since Jan 7, 200712:58:25 AM
 */
public class IvoaModuleIntegrationTest extends InARTestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		reg = ARTestSetup.fixture.getACR();
		assertNotNull(reg);		
	}
	ACR reg;

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		reg = null;
	}
	
	public void testExternalRegistry() throws Exception {
		final Object r = assertServiceExists(ExternalRegistry.class, "ivoa.externalRegistry");
		
		// call a method - triggers invocation - nothing that can be called however without going to a service.

	}
	
	public void testRegistry() throws Exception {
		final Registry r = assertServiceExists(Registry.class, "ivoa.registry");
		assertNotNull(r.getFallbackSystemRegistryEndpoint());
		assertNotNull(r.getSystemRegistryEndpoint());
	}
	

	public void testCOne() throws Exception {
		final Cone c = assertServiceExists(Cone.class,"ivoa.cone");
		c.getRegistryXQuery(); 
	}
	
	public void testSiap() throws Exception {
		final Siap c = assertServiceExists(Siap.class,"ivoa.siap");
		c.getRegistryXQuery(); 
	}
	
	public void testSsap() throws Exception {
		final Ssap c = assertServiceExists(Ssap.class,"ivoa.ssap");
		c.getRegistryXQuery(); 
	}
	
	public void testCache() throws Exception {
		final CacheFactory fac = assertComponentExists(CacheFactory.class, "ivoa.cache");
		//@todo find something non-descrutive to do here.
	}
	
	
	public void testAdql() throws Exception {
		final Adql adql = assertComponentExists(Adql.class,"ivoa.adql");
		final Document d= adql.s2x("select * from x as a");
		assertNotNull(d);
		final String string = DomHelper.DocumentToString(d);
		assertTrue(string.indexOf("Select") != -1);
	}
	
	public void testVosi() throws Exception {
	    final Vosi vosi = assertComponentExists(Vosi.class,"ivoa.vosi");
	}

    public static Test suite() {
        return new ARTestSetup(new TestSuite(IvoaModuleIntegrationTest.class));
    }
}
