/**
 * 
 */
package org.astrogrid;

import junit.framework.TestCase;

import org.apache.commons.cli.CommandLine;
import org.astrogrid.desktop.hivemind.GenerateHivedoc;
import org.astrogrid.desktop.hivemind.Launcher;
import org.astrogrid.desktop.hivemind.ListProperties;

/** test for the commandline parser.
 * @author Noel Winstanley
 * @since Jun 6, 20062:18:22 PM
 * @TEST loading properties and modules from file and url.
 */
public class CmdLineParserUnitTest extends TestCase {


	@Override
    protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("builtin.shutdown.exit", "false");		
		//System.setProperty("asr.mode","true");
		this.parser = new CmdLineParser();
	}
	
	protected CmdLineParser parser;

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		parser = null;
	}
	
	public void testInitializaiton() {
		assertNotNull(parser);
		assertNotNull(parser.getOptions());
		try {
		assertNull(parser.getCommandLine());
		fail("expected to barf");
		} catch (IllegalStateException e) {
			// expected
		}
	}

	/*
	 * Test method for 'org.astrogrid.CmdLineParser.parse(String[], String, Options)'
	 */
	public void testParseEmpty() {
		Launcher l = parser.parse(new String[]{},"testing");
		assertNotNull(l);
		CommandLine c = parser.getCommandLine();
		assertNotNull(c);
		assertFalse(c.iterator().hasNext());
	}


// test what kind of launcher is returned by different flags.
	public void testShowHelp() {
		Launcher l = parser.parse(new String[]{"--help"},"testing");
		assertNotNull(l);
		assertTrue(l instanceof CmdLineParser.ShowHelp);
		CommandLine c = parser.getCommandLine();
		assertNotNull(c);
		assertTrue(c.iterator().hasNext());
		// just run it - can't really check whether it's produced the right output.
		l.run();
	}
	
	public void testHivedoc() {
		Launcher l = parser.parse(new String[]{"--hivedoc"},"testing");
		assertNotNull(l);
		assertTrue(l instanceof GenerateHivedoc);
		CommandLine c = parser.getCommandLine();
		assertNotNull(c);
		assertTrue(c.iterator().hasNext());
		// just run it.
		l.run();
	}
	

	public void testUnknown() {
		Launcher l = parser.parse(new String[]{"--wibble"},"testing");
		assertNotNull(l);
		assertTrue(l instanceof CmdLineParser.ShowHelp);
		CommandLine c = parser.getCommandLine();
		assertNotNull(c);
		assertFalse(c.iterator().hasNext());
		// just run it
		l.run();
	}
	
	public void testListConfig() {
		Launcher l = parser.parse(new String[]{"--list"},"testing");
		assertNotNull(l);
		assertTrue(l instanceof ListProperties);
		CommandLine c = parser.getCommandLine();
		assertNotNull(c);
		assertTrue(c.iterator().hasNext());
		// just run it.
		l.run();
	}

	
}


