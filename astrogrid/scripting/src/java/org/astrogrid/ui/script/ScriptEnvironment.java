/*$Id: ScriptEnvironment.java,v 1.2 2005/01/13 15:18:55 nw Exp $
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
Revision 1.2  2005/01/13 15:18:55  nw
moved classes from scripting cdk - belong here.

Revision 1.1.2.1  2005/01/13 11:46:34  nw
saved work that I've not got time to do

Revision 1.1  2004/12/20 15:59:03  nw
added factory class to simplfy creation of scripting env. forces login.
 
*/