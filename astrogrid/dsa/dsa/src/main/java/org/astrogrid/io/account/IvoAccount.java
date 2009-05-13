/*
 * $Id: IvoAccount.java,v 1.1 2009/05/13 13:20:35 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.io.account;

import java.security.Principal;

/**
 * Represents an individual user account within the International Virtual
 * Observatory; ie one that is managed by VO Communities.
 *
 * An account consists of an individual within a community.  The individual may be an
 * automaton (eg a server) rather than human.
 *
 * AstroGrid references to users/accounts are of the form individual@community.
 *
 * While an individual can belong to one and only one community, he/she/it may
 * belong to several 'groups'.  A group may cross community boundaries.
 *
 * [MCH: I'm not sure if group should be specified here - it may be that the user
 * has picked which is the relevent group that has the right credentials for the
 * task.  On the other hand it should probably be the community that looks to see if
 * any of the groups the account belongs to have the right permissions]
 *
 * There are no setter methods.  This is because the token goes along with the
 * individual and community identifiers, and we shouldn't be able to change one
 * without the other.
 *
 * The token is some kind of certification that is carried along with the account
 * and community identifiers to prove that this individual really is this individual...

 * @author M Hill
 * @since iteration4
 */


public class IvoAccount implements Principal {

   private String individual = null;
   private String community = null;
   private String securityToken = null;

   public static final String SCHEME = "account";
   
   /** Create an account for the individual with the given name who belongs
    * to the given community, with some kind of security token */
   public IvoAccount(String anIndividualName, String aCommunity, String aToken) {
      this.individual = anIndividualName;
      this.community = aCommunity;
      this.securityToken = aToken;
   }
   
   /** Create an account from the given uri of the form 'account:mch@roe.ac.uk' */
   public IvoAccount(String uri) {
      int atIdx = uri.indexOf("@");
      if (!uri.startsWith(SCHEME+":") || (atIdx == -1)) {
         throw new IllegalArgumentException("Account uri should be of the form 'account:{individual}@{community}'");
      }
      uri = uri.substring(SCHEME.length()+1);
      this.individual = uri.substring(0, atIdx);
      this.community = uri.substring(atIdx+1);
   }
   
   /** Returns the canonical (fully descriptive) name of this account, implementing
    * the Principal getName() method. */
   public String getName()          { return individual+"@"+community; }

   /** Returns the individual id within the community (eg mch) */
   public String getIndividual()     { return individual; }

   /** Returns the community id of the account (eg roe.ac.uk) */
   public String getCommunity()     { return community; }

   /** Property getter: Returns the security token */
   public String getSecurityToken() { return securityToken; }
   
   /** Returns the URI used to represent this account 'on the wire'. This is
    * of the form account:<community>/<individual> */
   public String toUri() {
      return "account:"+individual+"@"+community;
   }
   
   /** Returns the IVORN that shouldn't be used to represent this account, but is
    * in some places.  @deprecated, use getUri */
   public String toIvorn() {
      return "ivo://"+community+"/"+individual;
   }
   
   public String toString() {
      return getName();
   }
   
}


