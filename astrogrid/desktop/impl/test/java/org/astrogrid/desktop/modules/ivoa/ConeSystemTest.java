/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.file.Info;
import org.astrogrid.acr.file.Manager;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.test.AstrogridAssert;
import org.w3c.dom.Document;

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
		info = (Info) assertServiceExists(Info.class,"file.info");
		manager = (Manager) assertServiceExists(Manager.class,"file.manager");
        pos = ses.resolve("crab");  
	}
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		cone =null;
		reg = null;
		ses = null;
		info = null;
		manager = null;
		pos = null;
	}
	protected SesamePositionBean pos;
	protected Cone cone;
	protected Registry reg;
	protected Sesame ses;
	protected Info info;
	protected Manager manager;
 
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ConeSystemTest.class),true);
    }    
    public static final String CONE_TEST_SERVICE ="ivo://nasa.heasarc/swiftmastr";
	
    public static final String VIZIER_TEST_SERVICE = "ivo://CDS.VizieR/J/ApJ/613/682";
    /*
	 * Test method for 'org.astrogrid.desktop.modules.nvo.ConeImpl.constructQuery(URI, double, double, double)'
	 */
	public void testExecute() throws Exception{
		final Resource r = reg.getResource(new URI(CONE_TEST_SERVICE));
		final URL u = cone.constructQuery(r.getId(),pos.getRa(),pos.getDec(),0.001);
		final Map[] rows = cone.execute(u);
		assertNotNull(rows);
		assertTrue("no results returned",rows.length > 0);

	}
	
	   public void testExecuteVotable() throws Exception{
	        final Resource r = reg.getResource(new URI(CONE_TEST_SERVICE));
	        URL u = cone.constructQuery(r.getId(),pos.getRa(),pos.getDec(),0.001);
	        u = cone.addOption(u,"VERB","3");
	        final Document votable = cone.executeVotable(u);
	        assertNotNull(votable);
	        AstrogridAssert.assertVotable(votable);

	    }
	
	   public void testExecuteSaveTmp() throws Exception{
	        final Resource r = reg.getResource(new URI(CONE_TEST_SERVICE));   
	        final URL u = cone.constructQuery(r.getId(),pos.getRa(),pos.getDec(),0.001);
	        final URI tmp = new URI("tmp://cone/executeSaveTmp/results.vot");
	        assertFalse(info.exists(tmp));
	        cone.executeAndSave(u,tmp);
	        // verify this file exists.
	        assertTrue(info.exists(tmp));
	        // check file contents.. - by executing).
	        final Map[] rows = cone.execute(tmp.toURL());
	        assertNotNull(rows);
	        assertTrue(rows.length > 0);
	        assertTrue(rows[0].size() > 0);

	    }
	
       public void testExecuteSaveWorkspace() throws Exception{
           final Resource r = reg.getResource(new URI(CONE_TEST_SERVICE));
           final URL u = cone.constructQuery(r.getId(),pos.getRa(),pos.getDec(),0.001);
           final URI my = new URI("workspace:/ConeSystemTest/results.vot");
           if (info.exists(my)) {
               manager.delete(my);
           }
           assertFalse(info.exists(my));
           cone.executeAndSave(u,my);
           // verify this file exists.
           manager.refresh(my);
           assertTrue(info.exists(my));
           // check file contents.. - by executing).
           final Map[] rows = cone.execute(my.toURL());
           assertNotNull(rows);

       }
       
       
       // check that these two invalid functions throw correctly.
       public void testSaveDataset() throws Exception{
           final Resource r = reg.getResource(new URI(CONE_TEST_SERVICE));
           final URL u = cone.constructQuery(r.getId(),pos.getRa(),pos.getDec(),0.001);
           final URI tmp = new URI("tmp://cone/executeSaveDataset");
           
           try {
               cone.saveDatasets(u,tmp);
           } catch (final ServiceException e) {
           }
           try {
               cone.saveDatasetsSubset(u,tmp,new ArrayList());
           } catch (final ServiceException e) {
           }           
           assertFalse(info.exists(tmp));
           cone.executeAndSave(u,tmp);
           // verify this file exists.
           assertTrue(info.exists(tmp));
           // check file contents.. - by executing).
           final Map[] rows = cone.execute(tmp.toURL());
           assertNotNull(rows);

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

	public void testSRQLQuery() throws Exception {
		final String q = cone.getRegistryQuery();
		assertNotNull(q);
		final Resource[] arr = reg.search(q + " and title = abell");
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		for (int i = 0; i < arr.length; i++) {
			checkConeResource(arr[i]);
		}
	}
	
	   public void testXQuery() throws Exception {
	        final String q = cone.getRegistryXQuery();
	        assertNotNull(q);
	        final String fullQ = "for $x in (" + q +") where $x/title &= '*abell*' return $x";
	        final Resource[] arr = reg.xquerySearch(fullQ);
	        assertNotNull(arr);
	        assertTrue(arr.length > 0);
	        for (int i = 0; i < arr.length; i++) {
	            checkConeResource(arr[i]);
	        }
	    }


	
	private void checkConeResource(final Resource r) {

		assertTrue(r instanceof ConeService);
		assertNotNull(((ConeService)r).findConeCapability());
	}	
	

}
