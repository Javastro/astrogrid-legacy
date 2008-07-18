/**
 * 
 */
package org.astrogrid;

import org.astrogrid.desktop.hivemind.Launcher;

/**
 * @author Noel Winstanley
 * @since Apr 11, 20062:41:10 AM
 */
public class VODesktop1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    // forces initialization of default properties before loading CmdLineParser (where some are required - i.e. log4j.logger)
	    Launcher.spliceInDefaults();
	    // now get on with the parsing.
		CmdLineParser parser = new CmdLineParser();
		Launcher l = parser.parse(args,"vodesktop");  
		configureLauncherAsVODesktop(l);
     	parser.processCommandLine(l);    	
    	l.run();
	}

	/**
	 * @param l
	 */
	public static void configureLauncherAsVODesktop(Launcher l) {
		// we're packaging log4j in this release, so configure clogging to use it.
    	System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.Log4JLogger");
  		
		System.setProperty("workbench.mode","true");
		System.setProperty("voexplorer.mode","true");
    	l.addModuleByName("ui");
    	l.addModuleByName("dialogs");
    	l.addModuleByName("plastic");
    	l.addModuleByName("ivoa");
    	//l.addModuleByName("voevent"); nothing in it.  
    	l.addModuleByName("votech");    
    	l.addModuleByName("nvo");	
    	l.addModuleByName("cds");
    	l.addModuleByName("astrogrid");
    	l.addModuleByName("system");
        l.addModuleByName("util");    	
        l.addModuleByName("network");
	}

}
