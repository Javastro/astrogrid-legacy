/**
 * 
 */
package org.astrogrid.desktop.modules.ag;


/** Bean-type interace to a login dialogue.
 * implemented by swing, and also as a no-op.
 * @author Noel Winstanley
 * @since Apr 20, 20066:02:47 PM
 */
public interface LoginDialogue {

	/**
	 * Sets the content of the community text entry field.
	 *
	 * @param  comm  community identifier
	 */
	void setCommunity(String comm);

	/**
	 * Returns the content of the community text entry field.
	 *
	 * @return  community identifier
	 */
	String getCommunity();

	/**
	 * Sets the content of the user text entry field.
	 *
	 * @param   user  user identifier
	 */
	void setUser(String user);

	/**
	 * Returns the content of the user text entry field.
	 * 
	 * @return  user identifier
	 */
	String getUser();

	String getPassword();

	void setPassword(String password);

	/** show the dialog, to get user input. will return 'true' if user confirms, 'false' if user cancels*/
	boolean showDialog();

}