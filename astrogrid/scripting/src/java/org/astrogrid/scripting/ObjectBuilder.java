/*$Id: ObjectBuilder.java,v 1.3 2004/11/22 18:26:54 clq2 Exp $
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

import org.astrogrid.community.User;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.store.Ivorn;

import java.net.URISyntaxException;

/** Class to help construction of various kinds of object
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 * 
 *
 */
public class ObjectBuilder {
    /** Construct a new ObjectHelper
     * 
     */
    public ObjectBuilder() {
        super();
    }
    /** helper method to create an account object 
     * 
     * @param username name of the user
     * @param community community the user is registered with
     * @return
     */
    public Account createAccount(String username,String community) {
        Account acc = new Account();
        acc.setName(username);
        acc.setCommunity(community);
        return acc;
    }     
    /** create a group object *
     * 
     * @param groupName name of the group
     * @param community community the group belongs to
     * @return
     */
    public Group createGroup(String groupName,String community) {
        Group g = new Group();
        g.setName(groupName);
        g.setCommunity(community);
        return g;
        
    }
    /** create a credentials object from an account and a group */
    public Credentials createCredendtials(Account acc,Group grp) {
        Credentials c = new Credentials();
        c.setAccount(acc);
        c.setGroup(grp);
        c.setSecurityToken("token");
        return c;
    }
    
    /** create the anonymous user */
    public User createUser() {
        return new User();
    }
    
    /** create a user 
     * 
     * @param userid username
     * @param community community the user is registered with
     * @param group group the user belongs to
     * @param token not used
     * @return
     */
    public User createUser(String userid,String community,String group,String token) {
        return new User(userid,community,group,token);
    }
    
      /** convert a user object into an ivorn that represents that user 
       * 
       * @param u a user object
       * @return the equivalent user ivorn.
       * @throws CommunityIdentifierException if the community is not known
       */
    public Ivorn createUserIvorn(User u) throws CommunityIdentifierException {
        return createUserIvorn(u.getCommunity(),u.getAccount());
    }
    /** create a user ivorn, from community and account name 
     * 
     * @param community the name of the community the account is registered with
     * @param account the username
     * @return
     * @throws CommunityIdentifierException if the community is not known.
     */
    public Ivorn createUserIvorn(String community,String account) throws CommunityIdentifierException {
        return CommunityAccountIvornFactory.createIvorn(community,account);
    }
    /** create a user ivorn, from account name and locally-configured community 
     * 
     * @param account the username
     * @return a user ivorn, where the comminity name is provided by local configuration
     * @throws CommunityServiceException is the commnity service can't be accessed
     * @throws CommunityIdentifierException 
     */
    public Ivorn createLocalUserIvorn(String account) throws CommunityServiceException, CommunityIdentifierException {
        return CommunityAccountIvornFactory.createLocal(account);
    }
    /** construct an arbitrary ivorn object 
     * @throws URISyntaxException*/
    public Ivorn createIvorn(String ivorn) throws URISyntaxException {
        return new Ivorn(ivorn);
    }
    /** construct an abritray ivorn object 
     * 
     * @param path the ivorn
     * @param fragment a fragment - i.e. the bit after <tt>#</tt>
     * @return an ivorn of form <tt>ivo://<i>path</i>#<i>fragment</i></tt>
     */
    public Ivorn createIvorn(String path,String fragment) {
        return new Ivorn(path,fragment);
    }
    /** construct an abritray ivorn object 
     * 
     * @param authority the authority of the ivorn
     * @param key key into the authority 
     * @param fragment 
     * @return an ivorn of form <tt>ivo://<i>authority</i>/<i>key</i>#<i>fragment</i></tt>
     */
    public Ivorn createIvorn(String authority,String key,String fragment) {
        return new Ivorn(authority,key,fragment);
    }
    
    
    
}


/* 
$Log: ObjectBuilder.java,v $
Revision 1.3  2004/11/22 18:26:54  clq2
scripting-nww-715

Revision 1.2.68.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.

Revision 1.2  2004/08/09 09:46:05  nw
added method to build user

Revision 1.1  2004/08/09 09:27:20  nw
changed name from objecthelper

Revision 1.1  2004/03/12 13:50:23  nw
improved scripting object
 
*/