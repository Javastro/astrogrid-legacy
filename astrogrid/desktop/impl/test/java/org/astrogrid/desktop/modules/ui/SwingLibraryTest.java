/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

import junit.framework.TestCase;

/** Test of various oddities of Swing - mostly for me to understand what thigns do.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 25, 20073:55:41 PM
 */
public class SwingLibraryTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testUIManager() throws Exception {
		UIDefaults defs = UIManager.getDefaults();
		System.out.println(defs);
	}
}
