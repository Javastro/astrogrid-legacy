/*$Id: ObjectHelper.java,v 1.1 2004/03/12 13:50:23 nw Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;

/** Class to help construction of various kinds of object
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class ObjectHelper {
    /** Construct a new ObjectHelper
     * 
     */
    public ObjectHelper() {
        super();
    }
    /** helper method to create an account object */
    public Account createAccount(String username,String community) {
        Account acc = new Account();
        acc.setName(username);
        acc.setCommunity(community);
        return acc;
    }     
    
    public Group createGroup(String groupName,String community) {
        Group g = new Group();
        g.setName(groupName);
        g.setCommunity(community);
        return g;
        
    }
    
    public Credentials createCredendtials(Account acc,Group grp) {
        Credentials c = new Credentials();
        c.setAccount(acc);
        c.setGroup(grp);
        c.setSecurityToken("token");
        return c;
    }
    
}


/* 
$Log: ObjectHelper.java,v $
Revision 1.1  2004/03/12 13:50:23  nw
improved scripting object
 
*/