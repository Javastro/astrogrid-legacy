/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.util.Tables;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.system.SystemModuleIntegrationTest;

/**
 * @author Noel Winstanley
 * @since Jan 8, 20077:40:02 PM
 */
public class UtilModuleIntegrationTest extends InARTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testTables() throws Exception {
		Tables t = (Tables)assertServiceExists(Tables.class, "util.tables");
		assertNotNull(t.listInputFormats());
	}
public static Test suite() {
    return new ARTestSetup(new TestSuite(UtilModuleIntegrationTest.class));
}

}
