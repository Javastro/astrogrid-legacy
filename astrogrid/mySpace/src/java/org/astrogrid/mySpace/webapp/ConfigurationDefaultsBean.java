/* $Id: ConfigurationDefaultsBean.java,v 1.2 2004/01/16 17:09:07 jdt Exp $
 * Created on 15-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.mySpace.webapp;

import org.astrogrid.mySpace.mySpaceManager.MMC;
/**
 * @author jdt
 *
 * Simple data holder used in the configuration.jsp page
 */
public final class ConfigurationDefaultsBean {
  /**
   * MySpaceManager URL property
   */
  private String msmUrl;
  /**
   * MySpaceManager version
   */
  private String version;
  /**
   * MySpaceManager registry location
   */
  private String registryconf;
  /**
   * MySpaceServer URL property
   */
  private String mssUrl;
  /**
   * MySpaceServers URL property
   */
  private String msmsUrl; //an accident waiting to happen
  
  /**
   * getter
   * @return bean prop
   */
  public String getMsmUrl() {
    return msmUrl;
  }

  /**
   * setter
   * @param string bean prop
   */
  public void setMsmUrl(final String string) {
    msmUrl = string;
  }

  /**
   * @return
   */
  public String getMsmsUrl() {
    return msmsUrl;
  }

  /**
   * @return
   */
  public String getMssUrl() {
    return mssUrl;
  }

  /**
   * @return
   */
  public String getRegistryconf() {
    return registryconf;
  }

  /**
   * @return
   */
  public String getVersion() {
    return version;
  }

  /**
   * @param string
   */
  public void setMsmsUrl(String string) {
    msmsUrl = string;
  }

  /**
   * @param string
   */
  public void setMssUrl(String string) {
    mssUrl = string;
  }

  /**
   * @param string
   */
  public void setRegistryconf(String string) {
    registryconf = string;
  }

  /**
   * @param string
   */
  public void setVersion(String string) {
    version = string;
  }
  
  /**
   * Attempt to set the values from MMC
   *
   */
  public ConfigurationDefaultsBean() {
    version = MMC.getProperty(MMC.GENERAL_VERSION_NUMBER, MMC.GENERAL_CATEGORY);
    registryconf = MMC.getProperty(MMC.REGISTRYCONF, MMC.CATLOG);
    msmUrl = MMC.getProperty(MMC.mySpaceManagerLoc,MMC.CATLOG); 
    mssUrl = MMC.getProperty(MMC.serverManagerLoc,MMC.CATLOG); 
    msmsUrl = MMC.getProperty(MMC.MYSPACEMANAGERURLs,MMC.CATLOG); 
  }
  
  public void update() {
    MMC.setProperty(MMC.GENERAL_VERSION_NUMBER, MMC.GENERAL_CATEGORY, version);
    MMC.setProperty(MMC.REGISTRYCONF, MMC.CATLOG, registryconf);
    MMC.setProperty(MMC.mySpaceManagerLoc,MMC.CATLOG, msmUrl); 
    MMC.setProperty(MMC.serverManagerLoc,MMC.CATLOG, mssUrl); 
    MMC.setProperty(MMC.MYSPACEMANAGERURLs,MMC.CATLOG, msmsUrl); 
  }

}

/*
*$Log: ConfigurationDefaultsBean.java,v $
*Revision 1.2  2004/01/16 17:09:07  jdt
*Changes to allow the changing and saving of properties
*
*Revision 1.1  2004/01/15 17:39:54  jdt
*Added a tag lib and a few mods to allow the config file to be set up from a JSP page.
*
*/