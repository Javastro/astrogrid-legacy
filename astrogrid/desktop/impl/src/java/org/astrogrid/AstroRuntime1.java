/**
 * 
 */
package org.astrogrid;

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
	    // forces initialization of default properties before loading CmdLineParser (where some are required - i.e. log4j.logger)
        Launcher.spliceInDefaults();
    	    
		CmdLineParser parser = new CmdLineParser();
		Launcher l = parser.parse(args,"acr");
    	configureLauncherAsACR(l);
     	parser.processCommandLine(l);    	
    	l.run();
	}

	/** Configure a launcher as the ACR variant.
	 * @param l a fresh launcher that has not yet been 'run'
	 */
	public static void configureLauncherAsACR(Launcher l) {
	  	System.setProperty("acr.mode","true");
   	
		// we're packaging log4j in this release, so configure clogging to use it.
    	System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.Log4JLogger");
  
    	l.addModuleByName("background");
    	l.addModuleByName("dialogs");
    	l.addModuleByName("plastic");
    	l.addModuleByName("ivoa");
    	l.addModuleByName("voevent");
    	l.addModuleByName("votech");
    	l.addModuleByName("nvo");
    	l.addModuleByName("cds");
    	l.addModuleByName("astrogrid");
    	l.addModuleByName("system");
        l.addModuleByName("util");  
        l.addModuleByName("network");
	}

}
