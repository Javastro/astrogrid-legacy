/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.cds.Coordinate;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.UCD;
import org.astrogrid.acr.cds.VizieR;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/**
 * @author Noel Winstanley
 * @since Jan 8, 20071:28:54 PM
 */
public class CdsModuleIntegrationTest extends InARTestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testCoordinate() throws Exception {
		Coordinate c = assertServiceExists(Coordinate.class,"cds.coordinate");
	
		
	}
	
	
	public void testSesame() throws Exception {
		Sesame ses = assertServiceExists(Sesame.class, "cds.sesame");
		
	}
	
	public void testUCD() throws Exception {
		UCD ucd = assertServiceExists(UCD.class, "cds.ucd");
	}
	
	public void testVizier() throws Exception {
		VizieR v = assertServiceExists(VizieR.class, "cds.vizier");
	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(CdsModuleIntegrationTest.class));
    }
	
}
