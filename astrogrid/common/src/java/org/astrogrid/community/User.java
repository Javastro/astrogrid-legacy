/*
 * $Id: User.java,v 1.7 2004/01/08 22:14:32 pah Exp $
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
 * A bean to hold what is passed in the "community snippet".
 * 
 * Note that the current interpretation of this is that the account contains an identifier of the form user@community - The group relates to the
 *  group that the user wishes to use for its credentials, this may be a cross community group. 
 * 
 * note that this should really be merged with the @link org.astrogrid.community.common.util.CommunityMessage class, which effectively does xml deserializtion
 * @author Paul Harrison (pah@jb.man.ac.uk), mch
 * @version $Name:  $
 * @since iteration4
 */
public class User {

    String account = null;
    String group = null;
    String token = null;
   
    public final static User ANONYMOUS = new User("Anon@nowhere","Anonymous","None");

   /** Default constructor - creates null user
    */
    public User(){
       this("Anon@nowhere","Anonymous", "None");
    }

    /**
     * Creates a user from the given account group & token.  The account &
     * group cannot be null
     * @param givenAccount the account in the form userid@community.
     * @param givenGroup The group credential.
     * @param givenToken The security token.
     */
    public User(String givenAccount, String givenGroup, String givenToken)
    {
       this.account = givenAccount;
       this.group = givenGroup;
       this.token = givenToken;
    }
    
    /**
     * Creates a user from "community snippet" representation.
     * @param snippet The community snippet - this is an xml fragment.
     */
    public User(String snippet)
    {
       this(CommunityMessage.getAccount(snippet),CommunityMessage.getGroup(snippet),CommunityMessage.getToken(snippet));
    }
    
   /**
    * @return
    */
   public String getAccount() {
      return account;
   }

   /**
    * @return
    */
   public String getGroup() {
      return group;
   }

   /**
    * @return
    */
   public String getToken() {
      return token;
   }

   /**
    * @param string
    */
   public void setAccount(String string) {
      account = string;
   }

   /**
    * @param string
    */
   public void setGroup(String string) {
      group = string;
   }

   /**
    * @param string
    */
   public void setToken(String string) {
      token = string;
   }


   /**
    * Checks to see if this user refers to the same account as the given
    * user
    */
   public boolean equals(User user)
   {
      return (user.getAccount().equals(this.account) && (user.getGroup().equals(this.group)));
   }

   /**
    * Returns a string with the 'normal' representation of account@community
    */
   public String toString()
   {
      return account;
   }
   
   /**
    * Create a "community snippet" from this User.
    * @return The XML string that is the community snippet.
    */
   public String toSnippet()
   {
      return CommunityMessage.getMessage(token, account, group);
   }
   
   /**
    * Return the community part of the account string
    * @return will return null if the account is null or the community part does not exist - i.e. there is no @ in the string.
    */
   public String getCommunity()
   {
      if(account != null)
      {
         int i;
         if((i= account.indexOf("@"))!= -1)
         {
            return account.substring(i+1);
         }
      }
      return null;
   }
}

/* $Log: User.java,v $
 * Revision 1.7  2004/01/08 22:14:32  pah
 * add a method to return the community part of the user account
 *
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

