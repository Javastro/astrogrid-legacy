/*
 * $Id: ApplicationControllerConfig.java,v 1.4 2003/12/04 13:26:25 pah Exp $
 * 
 * Created on 26-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.common.config;

import java.io.File;

import javax.sql.DataSource;





import org.astrogrid.applications.common.ApplicationsConstants;

/**
 * Important application controller configuration constants. This is a singleton, with the actual configuration that is loaded being controlled by the {@link ConfigLoader} class.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationControllerConfig {

 private static ApplicationControllerConfig instance = null;
 private Config config;
 static private org.apache.commons.logging.Log logger =
   org.apache.commons.logging.LogFactory.getLog(
      ApplicationControllerConfig.class);
      
 private javax.sql.DataSource dataSource = null;
 
 private ApplicationControllerConfig()
 {
    config = ConfigLoader.LoadConfig(ApplicationsConstants.CONFIGFILEKEY);
    //TODO get the real datasource from JNDI...
 }
 
 /**
  * constructor to allow unit testing. Note that this is package private
 * @param ds
 */
ApplicationControllerConfig(DataSource ds)
 {
    instance = this;
    dataSource = ds;
 }
 
 public static ApplicationControllerConfig getInstance()
 {
    // note the double check......
    if (instance == null)
    {
       synchronized (ApplicationControllerConfig.class){
          if (instance == null)
          {
          
          instance = new ApplicationControllerConfig();
          }
       }
       
    }
    return instance;
 }
 
 public File getApplicationConfigFile()
 {
    
    File file = new File(config.getProperty(ApplicationsConstants.ApplicationConfigKey));
    //TODO should test for the existance of the file here.
    return file;
 }
 
 public File getWorkingDirectory(){
    File dir = new File(config.getProperty(ApplicationsConstants.WorkingDirectory));
    return dir;
 }
 
 public String getDatasourceName(){
    return config.getProperty(ApplicationsConstants.DataSourceName);
 }
 
/**
 * @return
 */
public javax.sql.DataSource getDataSource() {
   return dataSource;
}

}
