/**
 * 
 */
package org.astrogrid.desktop.modules.auth;



/** Login dialogue service.
 * <p/>
 * Exposing this as a service means that the EDT interceptors can be used to ensure it is only
 * ever called on the EDT.
 * @author Noel Winstanley
 * @since Apr 20, 20066:02:47 PM
 */
public interface LoginDialogue {

    /** show a dialogue, prompting for password
    * and performs login.
     */
    void login();
    
}