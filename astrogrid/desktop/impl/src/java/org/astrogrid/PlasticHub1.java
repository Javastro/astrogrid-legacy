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

		configureLauncherAsHub(l);
    	
     	parser.processCommandLine(l);  	
    	l.run();
		}

	/**
	 * @param l
	 */
	public static void configureLauncherAsHub(Launcher l) {
		System.setProperty("hub.mode","true");	
		System.setProperty("system.scheduler.disabled","true");
		l.addModuleByName("plastic");
    	l.addModuleByName("system");
        l.addModuleByName("network");
	}
}
