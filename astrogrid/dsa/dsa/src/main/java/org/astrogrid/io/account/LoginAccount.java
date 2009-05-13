/*
 $Id: LoginAccount.java,v 1.1.1.1 2009/05/13 13:20:35 gtr Exp $

 (c) Copyright...
 */

package org.astrogrid.io.account;

import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

/**
 * Represents an Principal used to login to, eg, ftp servers or http or maybe
 * a local unix system.
 * Tt consists just of a name and a password that will be used to gain access to
 * the resource.
 * <p>
 * Bear in mind that the password will be exposed when used for FTP/HTTP, which
 * implies that a user will have several accounts for different services.
 */
public class LoginAccount implements Principal {
   
   public final static LoginAccount ANONYMOUS = new LoginAccount("anonymous", "LoginAccount@astrogrid.org");
   
   String userId = null;
   String password = null;
   
   public LoginAccount(String aUserId, String aPassword) {
      this.userId = aUserId;
      this.password = aPassword;
   }
   
   /**
    * Returns the name of this principal.
    */
   public String getName() {
      return userId;
   }
   
   public String getPassword() {
      return password;
   }
   
   public String toString() {
      return getName();
   }
   
}
