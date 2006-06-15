/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ByteConverter;

import junit.framework.TestCase;

/** simple tests for the converter contribution.
 * @author Noel Winstanley
 * @since Jun 7, 20069:29:19 AM
 */
public class ConverterContributionUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		c = new ConverterContribution();
	}
	
	protected ConverterContribution c;

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.ConverterContribution.setConverter(Converter)'
	 */
	public void testSetConverter() {
		assertNull(c.getConverter());
		Converter conv = new ByteConverter();
		c.setConverter(conv);
		assertEquals(conv,c.getConverter());
		
	}
	
	public void testSetArray() {
		assertFalse(c.isArray());
		c.setArray(true);
		assertTrue(c.isArray());
	}

	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.ConverterContribution.setOutput(Class)'
	 */
	public void testSetOutput() {
		assertNull(c.getOutput());
		c.setOutput(Class.class);
		assertEquals(Class.class, c.getOutput());
	}

}
