/**
 * 
 */
package org.astrogrid;

import org.astrogrid.desktop.hivemind.Launcher;

/** 
 * @author Noel Winstanley
 * @since Apr 11, 20062:41:10 AM
 */
public class Workbench1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CmdLineParser parser = new CmdLineParser();
		Launcher l = parser.parse(args,"workbench");
		configureLauncherAsWorkbench(l);
     	parser.processCommandLine(l);    	
    	l.run();
	}

	/**
	 * @param l
	 */
	public static void configureLauncherAsWorkbench(Launcher l) {
		// we're packaging log4j in this release, so configure clogging to use it.
    	System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.Log4JLogger");
  		
		System.setProperty("workbench.mode","true");
    	l.addModuleByName("background");
    	l.addModuleByName("ui");
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
	}

}
