/* $Id: NamingServiceDemo.java,v 1.2 2003/11/10 18:47:18 jdt Exp $
 * Created on 30-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.testutils.naming;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

/**
 * Demonstration of how to use my noddy naming service.
 * @author jdt
 *
 */
public final class NamingServiceDemo {

  /**
   * Demonstrates the use of this noddy naming service
   * @param args ignored
   * @throws NamingException if it don't work!
   */
  public static void main(final String[] args) throws NamingException {
    NamingManager.setInitialContextFactoryBuilder(new SimpleContextFactoryBuilder());
    NamingServiceDemo demo = new NamingServiceDemo();
    demo.createName();
    System.out.println(demo.getName());
    
  }


   /**
    * Sticks "/config/applicationName", "MyApp" in the naming service
   * @throws NamingException if it don't work!
   */
  private final void createName() throws NamingException {
     Context context = new InitialContext();
     context.bind("/config/applicationName", "MyApp");
   }

   /**
    * Gets "/config/applicationName" from the naming service
   * @return whatever it was mapped to
   * @throws NamingException if it don't work!
   */
  private final String getName() throws NamingException {
     Context context = new InitialContext();
     return (String) context.lookup("/config/applicationName");
   }  
}

/*
*$Log: NamingServiceDemo.java,v $
*Revision 1.2  2003/11/10 18:47:18  jdt
*Minor bits and pieces to satisfy the coding standards
*
*Revision 1.1  2003/10/31 17:21:44  jdt
*simple naming service to allow testing of database bits
*
*/