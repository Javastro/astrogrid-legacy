/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.file.Info;
import org.astrogrid.acr.file.Manager;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;


/** Tests the SIAP-specific bits of DalImpl
 *  execute, executeSave, executeVotable are tested in ConeSystemTesrt
 *  here just interested in savedatasets, and the custom reg queries.
 *  @todo add test for save datasets and savedatasetssubset.
 * @author Noel Winstanley
 * @since Jun 13, 20062:22:58 PM
 */
public class SiapSystemTest extends InARTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();

		siap = assertServiceExists(Siap.class,"ivoa.siap");
		reg = assertServiceExists(Registry.class,"ivoa.registry");
		ses = assertServiceExists(Sesame.class,"cds.sesame");
        info = assertServiceExists(Info.class,"file.info");
        manager = assertServiceExists(Manager.class,"file.manager");		
        pos = ses.resolve("m32");  
	}
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		siap = null;
		reg = null;
		ses = null;
        info = null;
        manager = null;		
		pos = null;
	}
	protected Siap siap;
	protected Registry reg;
	protected Sesame ses;
    protected Info info;
    protected Manager manager;	
    protected SesamePositionBean pos;

	    public static Test suite() {
	        return new ARTestSetup(new TestSuite(SiapSystemTest.class),true);
	    }    

	    public static final String SIAP_TEST_SERVICE = "ivo://cadc.nrc.ca/siap/hst";
	/*
    Run a test 'execute', just to verify query params and service is ok, and that some rows are returned.
	 */
	public void testQuery() throws Exception {
		final Resource r = reg.getResource(new URI(SIAP_TEST_SERVICE));
		final URL u = siap.constructQuery(r.getId(),pos.getRa(),pos.getDec(),0.0001);
		final Map[] rows = siap.execute(u);
		assertNotNull(rows);
		assertTrue(rows.length > 1); // as we're going to be downloading the first two later..
		for (int i = 0; i < rows.length; i++) {
			assertNotNull(rows[i].get("AccessReference"));
		}
	}
	
	   public void testSaveDatasetsSubsetTmp() throws Exception {
	        final Resource r = reg.getResource(new URI(SIAP_TEST_SERVICE));
	        final URL u = siap.constructQuery(r.getId(),pos.getRa(),pos.getDec(),0.0001);
	        final URI tmp = new URI("tmp://siap/saveDatasetsSubset");
	        final List subset = new ArrayList();
	        subset.add(0);
	        subset.add(1);
	       final int count =  siap.saveDatasetsSubset(u,tmp,subset);	       
	        // check the download dir exists.
	        assertTrue(info.isFolder(tmp));
	        final String[] children = manager.listChildren(tmp);
	        assertEquals(subset.size(),children.length);
	        assertEquals(subset.size(),count);
	        // check that each child has content.
	        for (final String f : children) {
                final URI img = new URI(tmp.toString() + "/" + f);
                assertTrue(info.isFile(img));
                assertTrue(info.getSize(img) > 0);
            }
	    }
//	   won't work - gives a false erorr
//      due to testSetup not passing user credentials correctly between threads.
	   // dunnp if I'm bothered to change this - saving datasets to myspace works
	   // fine from python and Groovy (i.e. over xmlrpc and rmi).
//       public void testSaveDatasetsWorkspace() throws Exception {
//           // check we're logged in.
//           final Community comm = (Community)assertComponentExists(Community.class, "astrogrid.community");
//           assertTrue(comm.isLoggedIn());
//           final Resource r = reg.getResource(new URI(SIAP_TEST_SERVICE));
//           final URL u = siap.constructQuery(r.getId(),pos.getRa(),pos.getDec(),0.0001);
//           final URI my  = new URI("workspace://siap/saveDatasets");
//           if (info.exists(my)) {
//               manager.delete(my);
//           } 
//           assertFalse(info.exists(my));
//           final int count = siap.saveDatasets(u,my);
//           manager.refresh(my);
//           // check the download dir exists.
//           assertTrue(info.isFolder(my));
//           final URI[] children = manager.listChildUris(my);
//           // check how many there should be.           
//           final int expectedCount = siap.execute(u).length;
//           assertEquals(expectedCount,children.length);
//           assertEquals(expectedCount,count);
//           // check that each child has content.
//           for (final URI img : children) {
//               assertTrue(info.isFile(img));
//               assertTrue(info.getSize(img) > 0);
//           }
//       }

    public void testSRQLQuery() throws Exception {
        final String q = siap.getRegistryQuery();
        assertNotNull(q);
        final Resource[] arr = reg.search(q);
        assertNotNull(arr);
        assertTrue(arr.length > 0);
        for (int i = 0; i < arr.length; i++) {
            checkSiapResource(arr[i]);
        }
    }
	
	public void testXQuery() throws Exception {
		final String xq = siap.getRegistryXQuery();
		assertNotNull(xq);
		final Resource[] arr = reg.xquerySearch(xq);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		for (int i = 0; i < arr.length; i++) {
			checkSiapResource(arr[i]);
		}			
	}
	
	private void checkSiapResource(final Resource r) {
		assertTrue(r instanceof Service);
		assertTrue(r instanceof SiapService);
		assertNotNull(((SiapService)r).findSiapCapability());
	}	

}
