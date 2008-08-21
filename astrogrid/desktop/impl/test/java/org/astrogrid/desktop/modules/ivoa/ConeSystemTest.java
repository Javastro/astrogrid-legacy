/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URL;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.votech.VoMon;

/** System test for the cone search interface.
 * @author Noel Winstanley
 * @since Jun 10, 200610:18:51 AM
 */
public class ConeSystemTest extends InARTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();

		cone = (Cone)assertServiceExists(Cone.class,"ivoa.cone");
		reg = (Registry)assertServiceExists(Registry.class,"ivoa.registry");
		ses = (Sesame)assertServiceExists(Sesame.class,"cds.sesame");
		vomon = (VoMon)assertServiceExists(VoMon.class,"votech.vomon");
	}
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		cone =null;
		reg = null;
		ses = null;
		vomon = null;
	}
	protected Cone cone;
	protected Registry reg;
	protected Sesame ses;
	protected VoMon vomon;
 
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ConeSystemTest.class));
    }    
    public static final String CONE_TEST_SERVICE ="ivo://nasa.heasarc/swiftmastr";
	
    public static final String VIZIER_TEST_SERVICE = "ivo://CDS/VizieR/VII/188/table3";
    /*
	 * Test method for 'org.astrogrid.desktop.modules.nvo.ConeImpl.constructQuery(URI, double, double, double)'
	 */
	public void testResolvedQuery() throws Exception{
		final Resource r = reg.getResource(new URI(CONE_TEST_SERVICE));
		final SesamePositionBean pos = ses.resolve("crab");		
		final URL u = cone.constructQuery(r.getId(),pos.getRa(),pos.getDec(),0.001);
		final Map[] rows = cone.execute(u);
		assertNotNull(rows);
		assertTrue("no results returned",rows.length > 0);

	}
	
	public void testVizierPositiveParameters() throws Exception {// see bz#2372
	    final Resource r= reg.getResource(new URI(VIZIER_TEST_SERVICE));
	    assertNotNull(r);
	    final URL u = cone.constructQuery(r.getId(),45.0,0.5,20.0);
	    final Map[] rows = cone.execute(u);
	    assertNotNull(rows);
	    assertTrue("no results",rows.length > 0);
	}
	
	
	   public void testVizierNegativeParameters() throws Exception {// see bz#2372
	        final Resource r= reg.getResource(new URI(VIZIER_TEST_SERVICE));
	        assertNotNull(r);
	        final URL u = cone.constructQuery(r.getId(),45.0,-0.5,20.0);
	        final Map[] rows = cone.execute(u);
	        assertNotNull(rows);
	        assertTrue("no results",rows.length > 0);
	    }

	public void testGetAdqlRegistryQueryNewReg() throws InvalidArgumentException, NotFoundException, ACRException, Exception {
		final String q = cone.getRegistryAdqlQuery();
		assertNotNull(q);
		final org.astrogrid.acr.ivoa.Registry reg = (org.astrogrid.acr.ivoa.Registry)getACR().getService(org.astrogrid.acr.ivoa.Registry.class);
		final Resource[] arr = reg.adqlsSearch(q);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		// just services for now..
		for (int i = 0; i < arr.length; i++) {
			checkConeResource(arr[i]);
		}
	}
//	
//	public void testGetXQueryRegistryQuery() throws Exception {
//		String xq = cone.getRegistryXQuery();
//		assertNotNull(xq);
//		org.astrogrid.acr.ivoa.Registry reg = (org.astrogrid.acr.ivoa.Registry)getACR().getService(org.astrogrid.acr.ivoa.Registry.class);
//		Resource[] arr = reg.xquerySearch(xq);
//		assertNotNull(arr);
//		assertTrue(arr.length > 0);
//		// just services for now..
//		for (int i = 0; i < arr.length; i++) {
//			checkConeResource(arr[i]);
//		}		
//		
//	}
//	

	
	private void checkConeResource(final Resource r) {

		assertTrue(r instanceof ConeService);
		assertNotNull(((ConeService)r).findConeCapability());
	}	
	

}
