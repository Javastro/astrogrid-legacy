/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.cds.Coordinate;
import org.astrogrid.acr.cds.Glu;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.UCD;
import org.astrogrid.acr.cds.VizieR;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.ag.AstrogridModuleIntegrationTest;

/**
 * @author Noel Winstanley
 * @since Jan 8, 20071:28:54 PM
 */
public class CdsModuleIntegrationTest extends InARTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testCoordinate() throws Exception {
		Coordinate c = (Coordinate)assertServiceExists(Coordinate.class,"cds.coordinate");
		try {
		c.convert(0.0, 0.0, 0.0, 0);
		} catch (Exception e) {
			// expected. - just used to force instantiation.
		}
		
	}
	
	public void testGlu() throws Exception {
		Glu g = (Glu)assertServiceExists(Glu.class,"cds.glu");
		try {
		g.getURLfromTag("fred");
		} catch (Exception e) {
			// expected - used to force instantiation
		}
	}
	
	public void testSesame() throws Exception {
		Sesame ses = (Sesame) assertServiceExists(Sesame.class, "cds.sesame");
		ses.resolve("m32");
		
	}
	
	public void testUCD() throws Exception {
		UCD ucd = (UCD) assertServiceExists(UCD.class, "cds.ucd");
		ucd.assign("fred");
	}
	
	public void testVizier() throws Exception {
		VizieR v = (VizieR)assertServiceExists(VizieR.class, "cds.vizier");
		v.metaAll();
	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(CdsModuleIntegrationTest.class),true);
    }
	
}
