/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import org.apache.hivemind.SymbolSource;

import junit.framework.TestCase;

/** unit test for the version symbol source
 * when running within eclipse, may not be present - but should be available when running in maven
 * @author Noel Winstanley
 * @since Jun 6, 20064:03:02 PM
 */
public class VersionSymbolSourceUnitTest extends TestCase {

	protected void setUp() {
		this.ss = new VersionSymbolSource();
	}
	
	private SymbolSource ss;
	
	
	/*
	 * Test method for 'org.astrogrid.desktop.hivemind.VersionSymbolSource.valueForSymbol(String)'
	 */
	public void testValueForSymbolBasics() {
		assertNull(ss.valueForSymbol(null));
		assertNull(ss.valueForSymbol("unknown key"));

	}
	
	
	/** these tests within fail within eclipse - only work in maven. */
	public void testValueForSymbolAstrogridDesktopVersion() {
		assertNotNull(ss.valueForSymbol("astrogrid.desktop.version"));
	}
	
	public void testValueForSymbolAstrogridBuildDate() {
		assertNotNull(ss.valueForSymbol("astrogrid.build.date"));
	}
	public void testValueForSymbolAstrogridBuildLocation() {
		assertNotNull(ss.valueForSymbol("astrogrid.build.location"));
	}
	public void testValueForSymbolAstrogridBuildJDK() {
		assertNotNull(ss.valueForSymbol("astrogrid.build.jdk"));
	}
	public void testValueForSymbolAstrogridBuildBy() {
		assertNotNull(ss.valueForSymbol("astrogrid.build.by"));
	}


}
