/*
 * $Id: SecurityTokenTest.java,v 1.1 2003/09/16 22:23:24 pah Exp $
 * 
 * Created on 16-Sep-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.community.authentication.data;

import java.util.Calendar;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration3
 */
public class SecurityTokenTest extends TestCase {

   /**
    * Constructor for SecurityTokenTest.
    * @param name
    */
   public SecurityTokenTest(String name) {
      super(name);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(SecurityTokenTest.class);
   }

   public void testConversion() {
      SecurityToken token = new SecurityToken("test@nowhere", "target");
      org
         .astrogrid
         .community
         .service
         .authentication
         .data
         .SecurityToken soaptoken =
         token.createSoapToken();
      assertTrue(token.getAccount().equals(soaptoken.getAccount()));
      assertTrue(token.getTarget().equals(soaptoken.getTarget()));
      assertTrue(token.getToken().equals(soaptoken.getToken()));
      assertTrue(token.getUsed().equals(soaptoken.getUsed()));

      //dates are tricky! - maybe better to pass as milliseconds UTC
      System.out.println(
         "token millisec="
            + token.getExpirationDate().getTime()
            + " soap millisec="
            + soaptoken.getExpirationDate().getTime().getTime());
      System.out.println(
         "token date="
            + token.getExpirationDate()
            + " soap date="
            + soaptoken.getExpirationDate().getTime());
      Calendar cal = Calendar.getInstance();
      cal.setTime(token.getExpirationDate());
      System.out.println(
         "token cal=" + cal + " soap cal=" + soaptoken.getExpirationDate());
      System.out.println(cal.equals(soaptoken.getExpirationDate()));

      
      assertTrue(
         token.getExpirationDate().equals(
            soaptoken.getExpirationDate().getTime()));
      assertTrue(
         token.getStartDate().equals(soaptoken.getStartDate().getTime()));

      SecurityToken newtoken = new SecurityToken(soaptoken);
      assertEquals("tokens should be the same", newtoken, token);
   }
}
