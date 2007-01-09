/**
 * 
 */
package org.astrogrid;

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
		CmdLineParser parser = new CmdLineParser();
		Launcher l = parser.parse(args,"hub");

		System.setProperty("hub.mode","true");	
		System.setProperty("system.help.disabled","true");
		System.setProperty("system.apihelp.disabled","true");
		System.setProperty("system.scheduler.disabled","true");
		l.addModuleByName("plastic");
    	l.addModuleByName("system");
    	
     	parser.processCommandLine(l);  	
    	l.run();
		}
}
