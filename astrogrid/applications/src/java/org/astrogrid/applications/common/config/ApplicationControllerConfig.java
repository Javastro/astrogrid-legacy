/*
 * $Id: ApplicationControllerConfig.java,v 1.12 2004/01/26 17:46:09 pah Exp $
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
import java.io.IOException;

import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.astrogrid.applications.common.ApplicationsConstants;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;

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

   private ApplicationControllerConfig() {
      logger.info("creating new configuration");
      
      config = ConfigLoader.LoadConfig(ApplicationsConstants.CONFIGFILEKEY);
      //get the real datasource from config
      dataSource = config.getDataSource(ApplicationsConstants.DataSourceName);
      logger.info("configuration created");
   }

   /**
    * constructor to allow unit testing. Note that this is package private
   * @param ds
   */
   ApplicationControllerConfig(DataSource ds) {
      config = ConfigLoader.LoadConfig(ApplicationsConstants.CONFIGFILEKEY);

      instance = this;
      dataSource = ds;
   }

   public static ApplicationControllerConfig getInstance() {
      // note the double check......
      if (instance == null) {
         synchronized (ApplicationControllerConfig.class) {
            if (instance == null) {

               instance = new ApplicationControllerConfig();
            }
         }

      }
      return instance;
   }

   public File getApplicationConfigFile() {

      File file =
         new File(
            config.getProperty(ApplicationsConstants.ApplicationConfigKey));
      //TODO should test for the existance of the file here.
      return file;
   }

   public File getWorkingDirectory() {
      File dir =
         new File(config.getProperty(ApplicationsConstants.WorkingDirectory));
      return dir;
   }

   public String getDatasourceName() {
      return config.getProperty(ApplicationsConstants.DataSourceName);
   }

   /**
    * @return
    */
   public javax.sql.DataSource getDataSource() {
      return dataSource;
   }
   public String getDBuser() {
      return config.getProperty(ApplicationsConstants.DATABASE_USER_KEY);
   }
   public String getDBpwd() {
      return config.getProperty(ApplicationsConstants.DATABASE_PASSWORD_KEY);
   }
   
   public MySpaceClient getMySpaceManager() throws IOException{
      //TODO need to get this from a registry somewhere really
      MySpaceClient manager;
      manager = MySpaceDelegateFactory.createDelegate(
       config.getProperty(ApplicationsConstants.MySpaceManagerKey));
       return manager;
   }
   
   
   public Session mailSessionInstance()
   {
      Session session = null;
      try {
         Context initCtxt = new InitialContext();
         Context envCtxt = (Context)initCtxt.lookup("java:comp/env");
         session = (Session)envCtxt.lookup(ApplicationsConstants.SMTPServerKey);
      }
      catch (NamingException e) {
         logger.error("cannot get the mail server session", e);
      }
      
      return session;
   }
   public String toHTMLReport()
   {
      StringBuffer rep = new StringBuffer(128);
      
      rep.append(config.toString());
      rep.append("<ul>");
      rep.append("<li>");
      
      rep.append("datasource :"); // could possibly get a connection and get more information...
      if(dataSource != null)
      {
         rep.append(dataSource.toString());
      }
      rep.append("</li>");
      
      rep.append("<li>");
      rep.append("working directory: ");
      rep.append(config.getProperty(ApplicationsConstants.WorkingDirectory));
      rep.append("</li>");

      rep.append("<li>");
      rep.append("application config file: ");
      rep.append(config.getProperty(ApplicationsConstants.ApplicationConfigKey));
      rep.append("</li>");

      rep.append("<li>");
      rep.append("myspace manager endpoint: ");
      rep.append(config.getProperty(ApplicationsConstants.MySpaceManagerKey));
      rep.append("</li>");
      
      rep.append("<li>");
      rep.append(" Mail Session : ");
      rep.append(mailSessionInstance());
      rep.append("</li>");
      
      rep.append("</ul>");

      
      
      return rep.toString();
   }
}
