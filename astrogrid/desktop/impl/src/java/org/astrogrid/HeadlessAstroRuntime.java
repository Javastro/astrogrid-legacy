/**
 * 
 */
package org.astrogrid;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.astrogrid.desktop.hivemind.Launcher;

/** variant of astro runtime in headless (non-ui) mode.<p>
 * <p>
 * No splash screen, obviously.
 * @author Noel Winstanley
 * @since Apr 11, 20062:49:32 AM
 */
public class HeadlessAstroRuntime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Options o = CmdLineParser.createDefaultOptions();
		CommandLine cl = CmdLineParser.parse(args,"asr",o);
		if (cl != null) {
    	Launcher l = new Launcher();

    	System.setProperty("asr.mode","true"); // "@todo unsure whether to use a different key here.  
    	System.setProperty("system.ui.disabled","true");
    	System.setProperty("system.systray.disabled","true");
    	System.setProperty("system.help.disabled","true");
    	System.setProperty("system.browser.disabled","true");
    	System.setProperty("astrogrid.loginDialogue.disabled","true");
    	
    	System.setProperty("system.configuration.preferenceClass",HeadlessAstroRuntime.class.getName());
    	// we're packaging log4j in this release, so configure clogging to use it.
    	System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.Log4JLogger");
    	
    	l.addModuleByName("background");
    	l.addModuleByName("plastic");
    	l.addModuleByName("ivoa");
    	l.addModuleByName("nvo");
    	l.addModuleByName("cds");
    	l.addModuleByName("astrogrid");
    	l.addModuleByName("system");
    	CmdLineParser.processCommandLine(cl,l);
    	
    	l.run();
		}
	}

}
