/* $Id: DummyNoFileConfigurator.java,v 1.2 2004/01/20 17:37:02 jdt Exp $
 * Created on 11-Dec-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid;


/**
 * Example implementation of a configurator for test purposes.
 * This configurator has no associated config file, and so is designed to fail
 * 
 * @author jdt
 */
public final class DummyNoFileConfigurator extends Configurator {

  /** The name of the config file we're loading
   * @see org.astrogrid.Configurator#getConfigFileName()
   */
  protected String getConfigFileName() {
    return "someFileNameYouWontThinkOf.xml";
  }

  /** A unique TLA designating the subsystem
   * @see org.astrogrid.Configurator#getSubsystemAcronym()
   */
  public String getSubsystemAcronym() {
    return "DEF";
  }

  /* (non-Javadoc)
   * @see org.astrogrid.Configurator#getJNDIName()
   */
  protected String getJNDIName() {
    
    return null;
  }
}

/*
*$Log: DummyNoFileConfigurator.java,v $
*Revision 1.2  2004/01/20 17:37:02  jdt
*bit the bullet and made getJNDIName abstract...now let's see what breaks.
*
*Revision 1.1  2003/12/11 18:19:10  jdt
*New files to test the modifications to the Configurator which allow it
*to load properties files from URLs
*
*/