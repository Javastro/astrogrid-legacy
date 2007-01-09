/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley
 * @since Oct 10, 20061:01:21 PM
 */
public class SiapRmiTransportTest extends SiapSystemTest {
	   protected ACR getACR() throws Exception{
	        return (new Finder()).find();
	    }
	    public static Test suite() {
	        return new ARTestSetup(new TestSuite(SiapRmiTransportTest.class));
	    }


}
