/*
 * $Id User.java $
 *
 */

package org.astrogrid.datacenter.delegate;


/**
 * Tenporary class so that DatacenterDelegate has something to work with...
 * Expect to replace with something from community
 * @author M Hill
 */

public class Certification
{
   public final static Certification ANONYMOUS = new Certification("Anon", "None");
   
   String userId = null;
   String communityId = null;

   public Certification(String givenUserId, String givenCommunityId)
   {
      this.userId = givenUserId;
      this.communityId = givenCommunityId;
   }
   
   public String getUserId() { return userId; }
   public String getCommunityId() { return communityId; }
   public String getCredentials() { return "UNKNOWN"; }
   
   
   public boolean equals(Certification cert)
   {
      return (cert.getUserId().equals(this.userId) && (cert.getCommunityId().equals(this.communityId)));
   }
}

/*
$Log: Certification.java,v $
Revision 1.2  2003/11/25 15:04:21  mch
Extended to be more useful

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/
