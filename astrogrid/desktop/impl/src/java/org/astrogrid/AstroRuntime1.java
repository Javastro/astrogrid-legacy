/**
 * 
 */
package org.astrogrid;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.astrogrid.desktop.hivemind.Launcher;

/** 
 * @author Noel Winstanley
 * @since Apr 11, 20062:42:31 AM
 */
public class AstroRuntime1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Options o = CmdLineParser.createDefaultOptions();
		CommandLine cl = CmdLineParser.parse(args,"acr",o);
		if (cl != null) {
				
    	Launcher l = new Launcher();
    	configureLauncherAsACR(l);
    	CmdLineParser.processCommandLine(cl,l);    	
    	l.run();
		}
	}

	/** Configure a launcher as the ACR variant.
	 * @param l a fresh launcher that has not yet been 'run'
	 */
	public static void configureLauncherAsACR(Launcher l) {
		System.setProperty("system.configuration.preferenceClass",AstroRuntime.class.getName());
    	System.setProperty("acr.mode","true");
		System.setProperty("app.mode","acr");    	
		// we're packaging log4j in this release, so configure clogging to use it.
    	System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.Log4JLogger");
  
    	l.addModuleByName("background");
    	l.addModuleByName("dialogs");
    	l.addModuleByName("plastic");
    	l.addModuleByName("ivoa");
    	l.addModuleByName("voevent");
    	l.addModuleByName("nvo");
    	l.addModuleByName("cds");
    	l.addModuleByName("astrogrid");
    	l.addModuleByName("system");
        l.addModuleByName("util"); 
        l.addModuleByName("externalConfig");         
	}

}
