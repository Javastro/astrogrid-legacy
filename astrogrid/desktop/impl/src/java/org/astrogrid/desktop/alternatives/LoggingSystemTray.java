/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.system.SystemTray;

/** Alternative implementation of 'system tray' that just logs all events.
 * @author noel
 * @since Apr 10, 20063:38:14 PM
 */
public class LoggingSystemTray implements SystemTray {
	/**
	 * Logger for this class
	 */
	private  Log logger;
/**
 * construct a new logging implementation of the system tray
 * @param category logger category to log under.
 */
	public LoggingSystemTray(String category) {
		super();
		logger =  LogFactory.getLog(category);		
		
	}

	/* overridden
	 */
	public void displayErrorMessage(String arg0, String arg1) {
		logger.error(arg0 + "\n" + arg1);

	}

	/* overridden
	 */
	public void displayInfoMessage(String arg0, String arg1) {
		logger.info(arg0 + "\n" + arg1);

	}

	/* overridden
	 */
	public void displayWarningMessage(String arg0, String arg1) {
		logger.warn(arg0 + "\n" + arg1);

	}

	/* overridden
	 */
	public void startThrobbing() {
		// does nothing

	}



	/* overridden
	 */
	public void stopThrobbing() {
		// does nothing

	}

}
