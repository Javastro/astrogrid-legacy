/* $Id: DummyCrapURLConfigurator.java,v 1.1 2003/12/11 18:19:10 jdt Exp $
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
 * This configurator supplies a JNDI name, but the URL exists in the naming service
 * but the URL is duff, so the properties file can't be found
 * 
 * 
 * @author jdt
 */
public final class DummyCrapURLConfigurator extends Configurator {

  /** The name of the config file we're loading
   * @see org.astrogrid.Configurator#getConfigFileName()
   */
  protected String getConfigFileName() {
    return "fileTestConfig.xml";
  }

  /** A unique TLA designating the subsystem
   * @see org.astrogrid.Configurator#getSubsystemAcronym()
   */
  public String getSubsystemAcronym() {
    return "NRL";
  }
  
  public String getJNDIName() {
    return "badurl";
  }
}

/*
*$Log: DummyCrapURLConfigurator.java,v $
*Revision 1.1  2003/12/11 18:19:10  jdt
*New files to test the modifications to the Configurator which allow it
*to load properties files from URLs
*
*/