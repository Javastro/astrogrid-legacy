/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**A mock stub for the login dialogue, for use when running in headless mode.
 * @author Noel Winstanley
 * @since Apr 20, 20066:06:19 PM
 */
public class HeadlessLoginDialogue implements LoginDialogue {

	public HeadlessLoginDialogue(final String category) {
		logger = LogFactory.getLog(category);
	}
	private final Log logger;

    public void  login() {
        logger.warn("Something's trying to prompt for a login - will fail it");
    }


}
