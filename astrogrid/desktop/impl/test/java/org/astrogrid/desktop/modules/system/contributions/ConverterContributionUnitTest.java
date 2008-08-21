/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import junit.framework.TestCase;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ByteConverter;

/** simple tests for the converter contribution.
 * @author Noel Winstanley
 * @since Jun 7, 20069:29:19 AM
 */
public class ConverterContributionUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		c = new ConverterContribution();
	}
	
	protected ConverterContribution c;
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		c = null;
	}
	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.contributions.ConverterContribution.setConverter(Converter)'
	 */
	public void testSetConverter() {
		assertNull(c.getConverter());
		final Converter conv = new ByteConverter();
		c.setConverter(conv);
		assertEquals(conv,c.getConverter());
		
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
