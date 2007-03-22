/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** a do-nothing stub - for when running in headless mode.
 * @author Noel Winstanley
 * @since Apr 20, 20066:06:19 PM
 */
public class HeadlessLoginDialogue implements LoginDialogue {

	public HeadlessLoginDialogue(String category) {
		logger = LogFactory.getLog(category);
	}
	private final Log logger;
	
	private String community;
	private String password;
	private String user;
	
	


	/**
	 * @return the community
	 */
	public String getCommunity() {
		return this.community;
	}




	/**
	 * @param community the community to set
	 */
	public void setCommunity(String community) {
		this.community = community;
	}





	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}




	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}




	public boolean showDialog() {
		logger.warn("Something's trying to prompt for a login - will fail it");
		return false;
	}




	/**
	 * @return the user
	 */
	public String getUser() {
		return this.user;
	}




	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

}
