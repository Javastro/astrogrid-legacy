/**
 * 
 */
package org.astrogrid;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.astrogrid.desktop.hivemind.Launcher;

/** 
 * @author Noel Winstanley
 * @since Apr 11, 20062:40:03 AM
 */
public class PlasticHub1 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Options o = CmdLineParser.createDefaultOptions();
		// @todo later add in app-specific options here.
		CommandLine cl = CmdLineParser.parse(args,"hub",o);
		if (cl != null) {
		//@todo later process app-specific options here.
		System.setProperty("hub.mode","true");
		System.setProperty("system.help.disabled","true");
		System.setProperty("system.apihelp.disabled","true");
		System.setProperty("system.scheduler.disabled","true");
		System.setProperty("system.configuration.preferenceClass",PlasticHub.class.getName());
				
    	Launcher l = new Launcher();
    	l.addModuleByName("plastic");
    	l.addModuleByName("system");
    	CmdLineParser.processCommandLine(cl,l);    	
    	l.run();
		}
	}
}
