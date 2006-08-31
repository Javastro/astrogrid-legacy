/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import javax.swing.JFrame;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ACRTestSetup;
import org.astrogrid.desktop.BuildInprocessWorkbench;
import org.uispec4j.TabGroup;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;

/** first attempt at a uispec test case - see if I can launch workbench, and get a handle on it.
 * @author Noel Winstanley
 * @since Jun 15, 20064:37:40 PM
 */
public class Uispec4jtryout extends UISpecTestCase {

	/*
	 * @see UISpecTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		System.out.println("un setup");
		//setAdapter(ACRTestSetup.acrFactory);
		BuildInprocessWorkbench wb = new BuildInprocessWorkbench();
		wb.start();
		//setAdapter(wb);
	}
  //  static {
  //  	System.out.println("in init");
   //     UISpec4J.init();
   // }
	public void testSimple() {
		System.out.println("in test");
		Window mainWindow = getMainWindow();
		System.out.println("got main window");
		TabGroup tg = mainWindow.getTabGroup();
		assertNotNull(tg);
		assertTrue(tg.tabNamesEquals(new String[]{"Data Explore","Vizualize","Workflow","Advanced"}));
		System.out.println("finished test");
	}
	
	/*
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(MainWindowUISystemTest.class));
    }*/
}
