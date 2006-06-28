/**
 * 
 */
package org.astrogrid;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
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
		Options o = CmdLineParser.createDefaultOptions();
		CommandLine cl = CmdLineParser.parse(args,"workbench",o);
		if (cl != null) {
    	Launcher l = new Launcher();
    	configureLauncherAsWorkbench(l);
    	
    	
    	CmdLineParser.processCommandLine(cl,l);    	
    	l.run();
		}
	}

	/**
	 * @param l
	 */
	public static void configureLauncherAsWorkbench(Launcher l) {
		System.setProperty("workbench.mode","true");
		System.setProperty("app.mode","workbench");
    	l.addModuleByName("background");
    	l.addModuleByName("ui");
    	l.addModuleByName("dialogs");
    	l.addModuleByName("plastic");
    	l.addModuleByName("ivoa");
    	l.addModuleByName("nvo");
    	l.addModuleByName("cds");
    	l.addModuleByName("astrogrid");
    	l.addModuleByName("system");
	}

}
