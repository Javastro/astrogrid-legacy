/*
 * $Id: User.java,v 1.5 2003/12/30 10:40:02 pah Exp $
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

/**
 * A bean to hold what is passed in the "community snippet".
 * @author Paul Harrison (pah@jb.man.ac.uk), mch
 * @version $Name:  $
 * @since iteration4
 */
public class User {

    String account = null;
    String group = null;
    String token = null;
   
    public final static User ANONYMOUS = new User("Anon","Anonymous","None");

   /** Default constructor - creates null user
    */
    public User(){
       this("Anon","Anonymous", "None");
    }

    /**
     * Creates a user from the given account group & token.  The account &
     * group cannot be null
     */
    public User(String givenAccount, String givenGroup, String givenToken)
    {
       this.account = givenAccount;
       this.group = givenGroup;
       this.token = givenToken;
    }
    
    /**
     * Creates a user from the given string representation
     */
    public User(String givenString)
    {
        this.account = givenString.substring(0,account.indexOf("@"));
        this.group = givenString.substring(account.indexOf("@")+1,account.length());
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
    * Returns a string with the 'normal' representation of account@group
    */
   public String toString()
   {
      return account+"@"+group;
   }
   
}

/* $Log: User.java,v $
 * Revision 1.5  2003/12/30 10:40:02  pah
 * made the default constructor non-deprecated - this is a bean it has to have a default constructor - make it acutally put in some values
 *
/* Revision 1.4  2003/12/16 15:49:17  mch
/* Extended to include Certification functionality - in prep to replace it
/*
/* Revision 1.3  2003/12/16 13:35:06  mch
/* Added anonymous class constant for 'default' user
/* */

