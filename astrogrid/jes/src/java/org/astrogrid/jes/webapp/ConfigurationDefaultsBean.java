/* $Id: ConfigurationDefaultsBean.java,v 1.1 2004/01/19 19:33:01 jdt Exp $
 * Created on 19-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.webapp;

import org.astrogrid.jes.JES;

/**
 * @author jdt
 *
 * Simple data holder used in the configuration.jsp page
 */
public final class ConfigurationDefaultsBean {
  /** 
   * bean property associated with property in config file
   */
  private String version;
  /** 
   * bean property associated with property in config file
   */
  private String monitorUrl;
  /** 
   * bean property associated with property in config file
   */
  private String controllerUrl;
  /** 
   * bean property associated with property in config file
   */
  private String schedulerUrl;
  /** 
   * bean property associated with property in config file
   */
  private String queryToolUrl;
  /** 
   * bean property associated with property in config file
   */
  private String dataFederationUrl;
  /** 
   * bean property associated with property in config file
   */
  private String sExtractorUrl;
  /** 
   * bean property associated with property in config file
   */
  private String hyperZUrl;

  /**
   * Getter
   * @return property
   */
  public String getControllerUrl() {
    return controllerUrl;
  }

  /**
   * Getter
   * @return property
   */
  public String getDataFederationUrl() {
    return dataFederationUrl;
  }

  /**
   * Getter
   * @return property
   */
  public String getHyperZUrl() {
    return hyperZUrl;
  }

  /**
   * Getter
   * @return property
   */
  public String getMonitorUrl() {
    return monitorUrl;
  }

  /**
   * Getter
   * @return property
   */
  public String getQueryToolUrl() {
    return queryToolUrl;
  }

  /**
   * Getter
   * @return property
   */
  public String getSchedulerUrl() {
    return schedulerUrl;
  }

  /**
   * Getter
   * @return property
   */
  public String getSExtractorUrl() {
    return sExtractorUrl;
  }

  /**
   * Getter
   * @return property
   */
  public String getVersion() {
    return version;
  }

  /**
   * setter
   * @param string property
   */
  public void setControllerUrl(final String string) {
    controllerUrl = string;
  }

  /**
   * setter
   * @param string property
   */
  public void setDataFederationUrl(final String string) {
    dataFederationUrl = string;
  }

  /**
   * setter
   * @param string property
   */
  public void setHyperZUrl(final String string) {
    hyperZUrl = string;
  }

  /**
  * setter
  * @param string property
  */
  public void setMonitorUrl(final String string) {
    monitorUrl = string;
  }

  /**
   * setter
   * @param string property
   */
  public void setQueryToolUrl(final String string) {
    queryToolUrl = string;
  }

  /**
   * setter
   * @param string property
   */
  public void setSchedulerUrl(final String string) {
    schedulerUrl = string;
  }

  /**
   * setter
   * @param string property
   */
  public void setSExtractorUrl(final String string) {
    sExtractorUrl = string;
  }

  /**
   * setter
   * @param string property
   */
  public void setVersion(final String string) {
    version = string;
  }

  /**
   * Attempt to set the values from MMC
   *
   */
  public ConfigurationDefaultsBean() {
    version = JES.getProperty(JES.GENERAL_VERSION_NUMBER, JES.GENERAL_CATEGORY);
    controllerUrl =
      JES.getProperty(JES.CONTROLLER_URL, JES.CONTROLLER_CATEGORY);
    monitorUrl = JES.getProperty(JES.MONITOR_URL, JES.MONITOR_CATEGORY);
    schedulerUrl = JES.getProperty(JES.SCHEDULER_URL, JES.SCHEDULER_CATEGORY);

    dataFederationUrl =
      JES.getProperty(
        JES.TOOLS_LOCATION + "DataFederation",
        JES.TOOLS_CATEGORY);
    hyperZUrl =
      JES.getProperty(JES.TOOLS_LOCATION + "HyperZ", JES.TOOLS_CATEGORY);
    sExtractorUrl =
      JES.getProperty(JES.TOOLS_LOCATION + "SExtractor", JES.TOOLS_CATEGORY);
    queryToolUrl =
      JES.getProperty(JES.TOOLS_LOCATION + "QueryTool", JES.TOOLS_CATEGORY);
  }

  /**
   * Sets the configuration (MMC) with the values in this bean   *
   */
  public void update() {
    JES.setProperty(JES.GENERAL_VERSION_NUMBER, JES.GENERAL_CATEGORY, version);
    JES.setProperty(JES.CONTROLLER_URL, JES.CONTROLLER_CATEGORY, controllerUrl);
    JES.setProperty(JES.MONITOR_URL, JES.MONITOR_CATEGORY, monitorUrl);
    JES.setProperty(JES.SCHEDULER_URL, JES.SCHEDULER_CATEGORY, schedulerUrl);

    JES.setProperty(
      JES.TOOLS_LOCATION + "DataFederation",
      JES.TOOLS_CATEGORY,
      dataFederationUrl);
    JES.setProperty(
      JES.TOOLS_LOCATION + "HyperZ",
      JES.TOOLS_CATEGORY,
      hyperZUrl);
    JES.setProperty(
      JES.TOOLS_LOCATION + "SExtractor",
      JES.TOOLS_CATEGORY,
      sExtractorUrl);
    JES.setProperty(
      JES.TOOLS_LOCATION + "QueryTool",
      JES.TOOLS_CATEGORY,
      queryToolUrl);
  }
}

/*
*$Log: ConfigurationDefaultsBean.java,v $
*Revision 1.1  2004/01/19 19:33:01  jdt
*tarting up the config pages
*
*/