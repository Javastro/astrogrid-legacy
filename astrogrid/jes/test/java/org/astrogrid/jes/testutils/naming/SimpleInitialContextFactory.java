/* $Id: SimpleInitialContextFactory.java,v 1.2 2003/11/10 18:51:07 jdt Exp $
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
import javax.naming.spi.InitialContextFactory;

/**
 *  Trivial class implementing InitialContextFactory that just returns our
 *  SimpleInitialContext
 *   @author jdt
 *
 */
public final class SimpleInitialContextFactory implements InitialContextFactory {


  /**
   * See the docs for the superclass
   * @see javax.naming.spi.InitialContextFactory#getInitialContext(java.util.Hashtable)
   */
  public final Context getInitialContext(final Hashtable environment) {
    return SimpleInitialContext.getInstance();
  }
}

/*
*$Log: SimpleInitialContextFactory.java,v $
*Revision 1.2  2003/11/10 18:51:07  jdt
*No longer a singleton since it turns out that occasionally you need a public default ctor
*
*Revision 1.1  2003/10/31 17:21:44  jdt
*simple naming service to allow testing of database bits
*
*/