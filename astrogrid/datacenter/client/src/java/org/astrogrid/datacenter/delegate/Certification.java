/*
 * $Id User.java $
 *
 */

package org.astrogrid.datacenter.delegate;

import org.astrogrid.community.User;



/**
 * Tenporary class so that DatacenterDelegate has something to work with...
 * Expect to replace with something from community
 * @author M Hill
 * @modified nww -  extends a axis-generated _community soapy bean.
 * @deprecated use {@link org.astrogrid.community.User} instead (in astrogrid-common)
 *  this class will vanish very soon - at present is just a compatability wrapper around User
 */

public class Certification {
   public final static Certification ANONYMOUS = new Certification(User.ANONYMOUS);
   
   private Certification(User u) {
      this.user = u;
   
   }
   /** user instance this dummy class now wraps.
    * package-private so can be accessed by other compatability code in this package 
    */
   User user;
   
   public Certification(String givenUserId, String givenCommunityId)
   {
      user = new User(givenUserId + "@" + givenCommunityId);
   }
   
   public String getUserId() { return user.getAccount(); }
   public String getCommunityId() { return user.getGroup(); }
   public String getCredentials() { return user.getToken(); }
   
   

}

/*
$Log: Certification.java,v $
Revision 1.4  2004/01/13 00:32:47  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.3.10.3  2004/01/12 16:44:07  nw
Reintroduced deprecated interfaces for backwards compatability

sql passthru finished and tested

Revision 1.3  2003/11/27 00:49:52  nw
added community bean to query

Revision 1.2  2003/11/25 15:04:21  mch
Extended to be more useful

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/
