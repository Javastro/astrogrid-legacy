/*
 * $Id: User.java,v 1.11 2004/02/15 23:18:17 mch Exp $
 *
 * Created on 27-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.community;

import org.astrogrid.community.common.util.CommunityMessage;

/**
 * Represents an individual user account.
 *
 * An individual consists of an account and a community, where the account
 * identifies the individual within a community.  The individual may be an
 * automaton (eg a server) rather than human.
 *
 * IVO references to users/accounts are of the form individual@community.
 *
 * While an individual can belong to one and only one community, he/she/it may
 * belong to several 'groups'.  A group may cross community boundaries.
 *
 * [MCH: I'm not sure if group should be specified here - it may be that the user
 * has picked which is the relevent group that has the right credentials for the
 * task.  On the other hand it should probably be the community that looks to see if
 * any of the groups the account belongs to have the right permissions]
 *
 * [MCH: There seems to have been some confusion between 'group' and 'community' in this
 * object.  The old 'group' term later came to represent a community, and now we
 * have a different thing called group.  In adding community to this, the way the datacenter
 * calls myspace has changed...  I've made the terms more explicit by adding
 * a commnunity property; hopefully this won't break anything... 14/2/04]
 *
 * The token is some kind of certification that is carried along with the account
 * and community identifiers to prove that this individual really is this individual...

 * @todo merge with @link org.astrogrid.community.common.util.CommunityMessage, which effectively does xml deserializtion
 *
 * @author Paul Harrison (pah@jb.man.ac.uk), mch
 * @since iteration4
 */
public class User {

    String individual = null;
    String community = null;
    String token = null;
    String group = null; //deprecated?
   
    public final static User ANONYMOUS = new User("Anonymous","Unknown","None",null);

   /** Default constructor - creates null user
    * @deprecated - use ANONYMOUS
    */
    public User(){
       this("Anonymous","Unknown", "None", null);
    }

    /**
     * Creates a user from the given full account details. Account and
     * community *must* be specified.
     */
    public User(String anIndividual, String aCommunity, String aGroup, String aToken)
    {
       assert (anIndividual != null) && (anIndividual.length()>0) : "an Individual must be given";
       assert (aCommunity != null) && (aCommunity.length()>0) : "a Community must be given";

       //@todo should now check the token matches the account & community...
       
       this.individual = anIndividual;
       this.community = aCommunity;
       this.group = aGroup;
       this.token = aToken;
    }
    
    
    /**
     * Creates a user from the "community snippet" xml representation.
     * @param snippet The community snippet - this is an xml fragment.
     */
    public User(String snippet)
    {
       this(CommunityMessage.getIndividual(snippet), CommunityMessage.getCommunity(snippet), CommunityMessage.getGroup(snippet),CommunityMessage.getToken(snippet));
    }
    
    /** Returns the individual reference within the community */
    public String getIndividual() {
       return individual;
    }

    /** Returns the vo reference
     * @deprecated - use getVOReference or getIndividual so we are explicit...
     */
    public String getAccount() {
       return getIvoRef();
    }

    /**
     * Returns the individual
     * @deprecated - use getIndividual
     */
    public String getUserId() {
       return individual;
    }
    
    /** Returns the IVO format used to refer to an account - ie individual@community
     */
    public String getIvoRef() {
       return individual+"@"+community;
    }
    
   public String getGroup() {
      return group;
   }

   public String getCommunity() {
      return community;
   }

   /** Returns the token that certifies this instance as really representing
    * the individual given by account & community */
   public String getToken() {
      return token;
   }

   public void setIndividual(String string) {
      individual = string;
   }

   public void setGroup(String string) {
      group = string;
   }

   public void setToken(String string) {
      token = string;
   }


   /**
    * Checks to see if this user refers to the same party as the given user.
    * Compares only individual and commnunity - the same individual might belong
    * to several groups but is still the same individual.
    */
   public boolean equals(User user)
   {
      return (user.getIvoRef().equals(this.getIvoRef()));
   }

   /**
    * Returns a string with the 'normal' representation of account@community, with
    * group
    */
   public String toString()
   {
      return getIvoRef() +" ("+group+")";
   }
   
   /**
    * Create a "community snippet" from this User.
    * @return The XML string that is the community snippet.
    */
   public String toSnippet()
   {
      return CommunityMessage.getMessage(token, getIvoRef(), group);
   }
   
}

/* $Log: User.java,v $
 * Revision 1.11  2004/02/15 23:18:17  mch
 * minor doc changes
 *
/* Revision 1.10  2004/02/14 17:15:56  mch
/* Added assertions and made community, individual, ivoreference etc explicit
/*
/* Revision 1.9  2004/01/09 21:38:13  pah
/* added new constructor
/*
/* Revision 1.8  2004/01/09 00:25:54  pah
/* added get userid
/*
/* Revision 1.7  2004/01/08 22:14:32  pah
/* add a method to return the community part of the user account
/*
/* Revision 1.6  2004/01/08 21:19:30  pah
/* changed to reflect the fact that the account should be user@community
/*
/* Revision 1.5  2003/12/30 10:40:02  pah
/* made the default constructor non-deprecated - this is a bean it has to have a default constructor - make it acutally put in some values
/*
/* Revision 1.4  2003/12/16 15:49:17  mch
/* Extended to include Certification functionality - in prep to replace it
/*
/* Revision 1.3  2003/12/16 13:35:06  mch
/* Added anonymous class constant for 'default' user
/* */


