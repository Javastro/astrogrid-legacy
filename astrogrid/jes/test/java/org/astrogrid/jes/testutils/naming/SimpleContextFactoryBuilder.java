/* $Id: SimpleContextFactoryBuilder.java,v 1.2 2003/11/10 18:52:01 jdt Exp $
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

import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;

/**
 * Trivial class implementing InitialContextFactoryBuilder that returns
 * a new SimpleInitialContextFactory
 * @author jdt
 */
public class SimpleContextFactoryBuilder implements InitialContextFactoryBuilder {

  /**
   * Contains the singleton SimpleInitialContextFactory
   */
  private InitialContextFactory icf = new SimpleInitialContextFactory();

  /**
   * Gives you a SimpleInitialContextFactory
   * @param environment A hashtable of environment vars that are cheerfully ignored
   * @return what it says on the tin
   * @see javax.naming.spi.InitialContextFactoryBuilder#createInitialContextFactory(java.util.Hashtable)
   */
  public final InitialContextFactory createInitialContextFactory(final Hashtable environment) {
    return icf;
  }

}

/*
*$Log: SimpleContextFactoryBuilder.java,v $
*Revision 1.2  2003/11/10 18:52:01  jdt
*Following change to SimpleInitialContextFactory
*
*Revision 1.1  2003/10/31 17:21:44  jdt
*simple naming service to allow testing of database bits
*
*/