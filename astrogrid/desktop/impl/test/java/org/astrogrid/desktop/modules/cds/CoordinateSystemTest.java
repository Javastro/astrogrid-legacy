/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.cds.Coordinate;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** test of the coordinate conversion service from cds.
 * not a test of the correctness of the data from the service - that's their problem.
 * myinputs are all nonsense anyhow - partly because there's no documentation.
 * rather, it's a test of whether the connection works.
 * @author Noel Winstanley
 * @since Jun 9, 20065:37:47 PM
 */
public class CoordinateSystemTest extends InARTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ACR reg = getACR();
		assertNotNull(reg);
		coord = (Coordinate)reg.getService(Coordinate.class);
		assertNotNull(coord);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		coord = null;
	}
	protected Coordinate coord;

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.CoordinateImpl.convert(double, double, double, int)'
	 */
	public void testConvert() throws ServiceException {
		String result = coord.convert(10.0,15.0,20.0,1);
		assertNotNull(result);
		System.out.println(result);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.CoordinateImpl.convertL(double, double, int)'
	 */
	public void testConvertL() throws ServiceException {
		String result = coord.convertL(12.0,45.0,1);
		assertNotNull(result);		
		System.out.println(result);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.CoordinateImpl.convertE(int, int, double, double, double, int, double, double)'
	 */
	public void testConvertE() throws ServiceException {
		String result = coord.convertE(1,2,10.0,15.0,10.0,1,0.1,0.1);
		assertNotNull(result);	
		System.out.println(result);
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.cds.CoordinateImpl.convertLE(int, int, double, double, int, double, double)'
	 */
	public void testConvertLE() throws ServiceException {
		String result = coord.convertLE(1,2,12.0,45.0,1,0.1,0.1);
		assertNotNull(result);			
		System.out.println(result);

	}
    public static Test suite() {
        return new ARTestSetup(new TestSuite(CoordinateSystemTest.class));
    }

}
