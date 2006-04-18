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
		// @todo later add in app-specific options here.
		CommandLine cl = CmdLineParser.parse(args,"acr",o);
		if (cl != null) {
		//@todo later process app-specific options here.
    	Launcher l = new Launcher();
    	System.setProperty("acr.mode","true");
    	l.addModuleByName("background");
    	l.addModuleByName("dialogs");
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
