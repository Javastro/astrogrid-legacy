/**
 * 
 */
package org.astrogrid;

import org.apache.commons.cli.CommandLine;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:18:22 PM
 */
public class CmdLineParserUnitTest extends TestCase {


	

	/*
	 * Test method for 'org.astrogrid.CmdLineParser.createDefaultOptions()'
	 */
	public void testCreateDefaultOptions() {
		assertNotNull(CmdLineParser.createDefaultOptions());
	}

	/*
	 * Test method for 'org.astrogrid.CmdLineParser.parse(String[], String, Options)'
	 */
	public void testParseEmpty() {
		CommandLine c = doParse(new String[]{});
		assertNotNull(c);
		assertFalse(c.iterator().hasNext());
	}


	/**
	 * @param args
	 */
	private CommandLine doParse(String[] args) {
		return CmdLineParser.parse(args,"test",CmdLineParser.createDefaultOptions());
	}

	/*
	 * Test method for 'org.astrogrid.CmdLineParser.showHelp(String, Options)'
	 */
	public void testShowHelp() {
		CmdLineParser.showHelp("testing",CmdLineParser.createDefaultOptions());

	}

	/*
	 * Test method for 'org.astrogrid.CmdLineParser.processCommandLine(CommandLine, Launcher)'
	 */
	public void testProcessCommandLine() {
		fail("implement me");
	}

}
