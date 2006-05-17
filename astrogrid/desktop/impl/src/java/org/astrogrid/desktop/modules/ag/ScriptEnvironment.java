/*
 * ScriptEnvironment.java
 *
 * Created on 27 February 2006, 19:29
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.astrogrid.desktop.modules.ag;

import org.astrogrid.community.User;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.store.Ivorn;

/** Interface to astrogird scripting environment objects
 * @todo move to separate subpackage, or refactor away.
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Dec-2004
 *
 */
interface ScriptEnvironment {
    /** access ivorn represenation of the current user */
    Ivorn getUserIvorn();
    /** access ivorn representaation of the current user's homespace */
    Ivorn getHomeIvorn();
    /** access password used to login */
    String getPassword();
    /** Get credentials **/
    SecurityGuard getSecurityGuard();
}
