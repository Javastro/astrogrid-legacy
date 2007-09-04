/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.UserInformation;


/** a do-nothing stub - for when running in headless mode.
 * @author Noel Winstanley
 * @since Apr 20, 20066:06:19 PM
 */
public class HeadlessLoginDialogue implements LoginDialogue {

	public HeadlessLoginDialogue(String category) {
		logger = LogFactory.getLog(category);
	}
	private final Log logger;

    public UserInformation show() {
        logger.warn("Something's trying to prompt for a login - will fail it");
        return null;
    }


}
