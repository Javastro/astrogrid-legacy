/*
 * $Id: User.java,v 1.3 2003/12/16 13:35:06 mch Exp $
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
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class User {

    String account;
    String group;
    String token;
   
    public final static User ANONYMOUS = new User("Anonymous","Anonymous","None");
    
    public User(){
    }
    
    public User(String account, String group, String token)
    {
       this.account = account;
       this.group = group;
       this.token = token;
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

}

/* $Log: User.java,v $
 * Revision 1.3  2003/12/16 13:35:06  mch
 * Added anonymous class constant for 'default' user
 * */

