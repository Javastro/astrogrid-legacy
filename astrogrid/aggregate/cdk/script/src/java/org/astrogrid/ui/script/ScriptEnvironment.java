/*$Id: ScriptEnvironment.java,v 1.1 2004/12/20 15:59:03 nw Exp $
 * Created on 20-Dec-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.ui.script;

import org.astrogrid.community.User;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.scripting.Toolbox;
import org.astrogrid.store.Ivorn;

/** Interface to astrogird scripting environment objects
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Dec-2004
 * @todo maybe move all this into the scripting project later, retrofit into jes, etc.
 *
 */
public interface ScriptEnvironment {
    /** access the astrogrid system object */
    Toolbox getAstrogrid();
    /** access the user representation of the current user */
    User getUser();
    /** access the account representation of the current user */
    Account getAccount();
    /** access ivorn represenation of the current user */
    Ivorn getUserIvorn();
    /** access ivorn representaation of the current user's homespace */
    Ivorn getHomeIvorn();
    /** access password used to login */
    String getPassword();
    
}


/* 
$Log: ScriptEnvironment.java,v $
Revision 1.1  2004/12/20 15:59:03  nw
added factory class to simplfy creation of scripting env. forces login.
 
*/