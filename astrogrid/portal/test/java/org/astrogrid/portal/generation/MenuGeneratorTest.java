/*
 * AstroGrid Portal: MenuGeneratorTest
 */
package org.astrogrid.portal.generation;

import org.apache.cocoon.environment.mock.MockEnvironment;

import junit.framework.TestCase;

/**
 * Test the dynamic generation of the AstroGrid site menu.
 */
public class MenuGeneratorTest extends TestCase {

	/**
	 * Create the named test.
	 * 
	 * @param name
	 * @see junit.framework.TestCase#TestCase(java.lang.String)
	 */
	public MenuGeneratorTest(String name) {
		super(name);
	}

	/**
	 * Run the unit test.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(MenuGeneratorTest.class);
	}

	/**
	 * Test the generate of the site menu.
	 */
	public void testGenerate() throws Exception {
		MenuGenerator generator = new MenuGenerator();
		generator.setup(new MockEnvironment(null), null, null, null);
		
		generator.generate();
		
		assertTrue("[MenuGeneratorTest.testGenerate] should have got here", true);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
