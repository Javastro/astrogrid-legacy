/**
 * 
 */
package org.astrogrid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.hivemind.Launcher;

/** variant of astro runtime in headless (non-ui) mode.<p>
 * <p>
 * No splash screen, obviously.
 * @author Noel Winstanley
 * @since Apr 11, 20062:49:32 AM
 */
public class HeadlessAstroRuntime {
	private static final Log logger = LogFactory.getLog("startup");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Starting Headless AstroRuntime");
		CmdLineParser parser = new CmdLineParser();
		Launcher l = parser.parse(args,"asr");		

    	configureLauncherAsASR(l);        

     	parser.processCommandLine(l);    	
    	l.run();
	}

	/**
	 * @param l
	 */
	public static void configureLauncherAsASR(Launcher l) {
		System.setProperty("asr.mode","true");
    	System.setProperty("system.ui.disabled","true");
    	System.setProperty("system.systray.disabled","true");
    	System.setProperty("system.help.disabled","true");
    	System.setProperty("system.browser.disabled","true");
    	System.setProperty("astrogrid.loginDialogue.disabled","true");
   	// we're packaging log4j in this release, so configure clogging to use it.
    	System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.Log4JLogger");
    	
    	l.addModuleByName("background");
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
