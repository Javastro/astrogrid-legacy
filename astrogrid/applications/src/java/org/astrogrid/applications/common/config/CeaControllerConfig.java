/*
 * $Id: CeaControllerConfig.java,v 1.3 2004/03/23 19:46:04 pah Exp $
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
import java.net.MalformedURLException;
import java.net.URL;

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
 * @TODO should really reimplement config in terms of the common config class now that it is better...
 */
public class CeaControllerConfig {

   private static CeaControllerConfig instance = null;
   RawPropertyConfig rawPropertyConfig;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(
         CeaControllerConfig.class);

   private javax.sql.DataSource dataSource = null;

   protected CeaControllerConfig() {
      logger.info("creating new configuration");
      
      rawPropertyConfig = ConfigLoader.LoadConfig(ApplicationsConstants.CONFIGFILEKEY);
      //get the real datasource from config
      dataSource = rawPropertyConfig.getDataSource(ApplicationsConstants.DataSourceName);
      logger.info("configuration created");
   }

   /**
    * Get an instance. This has been made protected to stop "accidental" use outside the inversion of control pattern.
    * @return
    */
   protected static CeaControllerConfig getInstance() {
      // note the double check......
      if (instance == null) {
         synchronized (CeaControllerConfig.class) {
            if (instance == null) {

               instance = new CeaControllerConfig();
            }
         }

      }
      return instance;
   }

   /**
    * Find the config file that defines which applications we can run...
    * @return The location of the config file
    *
    */
   public URL getApplicationConfigFile() throws MalformedURLException {

      URL file =
         new URL(
            rawPropertyConfig.getProperty(ApplicationsConstants.ApplicationConfigKey));
      return file;
   }
   
   /**
    * Get the URL to the file that is used as the template for registry entries.
    * @return
    * @throws MalformedURLException
    */
   public URL getRegistryTemplateURL() throws MalformedURLException
   {
      URL url = new URL(rawPropertyConfig.getProperty(ApplicationsConstants.RegistryTemplateKey));
      return url;
   }

   public File getWorkingDirectory() {
      File dir =
         new File(rawPropertyConfig.getProperty(ApplicationsConstants.WorkingDirectory));
      return dir;
   }

   public String getDatasourceName() {
      return rawPropertyConfig.getProperty(ApplicationsConstants.DataSourceName);
   }

   /**
    * @return
    */
   public javax.sql.DataSource getDataSource() {
      return dataSource;
   }
   public String getDBuser() {
      return rawPropertyConfig.getProperty(ApplicationsConstants.DATABASE_USER_KEY);
   }
   public String getDBpwd() {
      return rawPropertyConfig.getProperty(ApplicationsConstants.DATABASE_PASSWORD_KEY);
   }
   
   /**
    * Get the myspaceManagerEndpoint that has been configured...
    * @return
    */
   public String getMySpaceManagerEndpoint() {
      
       return rawPropertyConfig.getProperty(ApplicationsConstants.MySpaceManagerKey);
       
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
   
   /**
    * Get the endpoint for the Registry.
    * @return
    */
   public String getRegistryEndpoint()
   {
      return rawPropertyConfig.getProperty(ApplicationsConstants.RegistryEndpointKey);
   }
   
   public String toHTMLReport()
   {
      StringBuffer rep = new StringBuffer(128);
      
      rep.append(rawPropertyConfig.toString());
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
      rep.append(rawPropertyConfig.getProperty(ApplicationsConstants.WorkingDirectory));
      rep.append("</li>");

      rep.append("<li>");
      rep.append("application config file: ");
      rep.append(rawPropertyConfig.getProperty(ApplicationsConstants.ApplicationConfigKey));
      rep.append("</li>");

      rep.append("<li>");
      rep.append("Registry Template file: ");
      rep.append(rawPropertyConfig.getProperty(ApplicationsConstants.RegistryTemplateKey));
      rep.append("</li>");

      rep.append("<li>");
      rep.append("registry endpoint: ");
      rep.append(rawPropertyConfig.getProperty(ApplicationsConstants.RegistryEndpointKey));
      rep.append("</li>");
      
      rep.append("<li>");
      rep.append("myspace manager endpoint: ");
      rep.append(rawPropertyConfig.getProperty(ApplicationsConstants.MySpaceManagerKey));
      rep.append("</li>");
      
      rep.append("<li>");
      rep.append(" Mail Session : ");
      rep.append(mailSessionInstance());
      rep.append("</li>");
      
      rep.append("</ul>");

      
      
      return rep.toString();
   }
   /**
    * @return
    */
   public RawPropertyConfig getRawPropertyConfig() {
      return rawPropertyConfig;
   }

   /**
    * Sets the datasource. This is only used in testing where the datasource is not available from jndi
    * @param source
    */
    void setDataSource(javax.sql.DataSource source) {
      dataSource = source;
   }

}
