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
		// @todo later add in app-specific options here.
		CommandLine cl = CmdLineParser.parse(args,"asr",o);
		//@todo later process app-specific options here.
		if (cl != null) {
    	Launcher l = new Launcher();
    	System.setProperty("acr.mode","true"); // "@todo unsure whether to use a different key here.  
    	System.setProperty("system.ui.disabled","true");
    	System.setProperty("system.systray.disabled","true");
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
