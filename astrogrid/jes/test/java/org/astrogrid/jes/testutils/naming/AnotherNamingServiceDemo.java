/* $Id: AnotherNamingServiceDemo.java,v 1.2 2003/11/10 18:49:43 jdt Exp $
 * Created on 30-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.testutils.naming;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Demonstration of how to use my noddy naming service.
 * @author jdt
 *
 */
public final class AnotherNamingServiceDemo {

  /**
   * Demonstrates another way of using the naming service without setting
   * the ContextFactoryBuilder.
   * @see java.org.astrogrid.jes.testutils.naming.NamingServiceDemo
   * @param args ignored
   * @throws NamingException if it don't work!
   */
  public static void main(final String[] args) throws NamingException {

    AnotherNamingServiceDemo demo = new AnotherNamingServiceDemo();
    demo.createName();
    System.out.println(demo.getName());

  }

  /**
   * Sticks "/config/applicationName", "MyApp" in the naming service
  * @throws NamingException if it don't work!
  */
  private final void createName() throws NamingException {
    Context context = new InitialContext(envs);
    context.bind("/config/applicationName", "MyApp");
  }

  /**
   * Gets "/config/applicationName" from the naming service
  * @return whatever it was mapped to
  * @throws NamingException if it don't work!
  */
  private final String getName() throws NamingException {
    Context context = new InitialContext(envs);
    return (String) context.lookup("/config/applicationName");
  }
  /**
   * Environment variables
   */
  private Hashtable envs = new Hashtable();
  /**
   * Ctor sets the environment vars
   *
   */
  public AnotherNamingServiceDemo() {
    envs.put(
      Context.INITIAL_CONTEXT_FACTORY,
      "org.astrogrid.jes.testutils.naming.SimpleInitialContextFactory");
  }
}

/*
*$Log: AnotherNamingServiceDemo.java,v $
*Revision 1.2  2003/11/10 18:49:43  jdt
*Minor bits and pieces to satisfy the coding standards
*
*Revision 1.1  2003/11/10 18:49:04  jdt
*Initial Commit
*
*/