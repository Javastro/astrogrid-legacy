/*
 * $Id: CommunityJNDIConfig.java,v 1.2 2003/09/16 22:23:24 pah Exp $
 * 
 * Created on 15-Sep-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.community.common;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jconfig.Configuration;
import org.jconfig.ConfigurationManager;
import org.jconfig.ConfigurationManagerException;
import org.jconfig.handler.URLHandler;
import org.jconfig.handler.XMLFileHandler;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration3
 */

public class CommunityJNDIConfig implements Config {
   /**
    * The jConfig configuration.
    */
   private Configuration config;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(CommunityJNDIConfig.class);

   /**
    * The configuration name to use within jConfig.
    *
    */
   private static final String CONFIG_NAME = "org.astrogrid.community";

   /**
    * The default category to use in the config file.
    * Corresponds to the name of the category element in the config file.
    * <pre>
    *     &lt;properties&gt;
    *         &lt;category name="org.astrogrid.community"&gt;
    *         ....
    *     &lt;/properties&gt;
    * </pre>
    *
    */
   private static final String DEFAULT_CATEGORY = "org.astrogrid.community";

   /**
    * The default JNDI name.
    * Corresponds to the name in the env-entry-name entry in your web.xml.
    * <pre>
    *     &lt;env-entry&gt;
    *         &lt;env-entry-name&gt;org.astrogrid.community.config&lt;/env-entry-name&gt;
    *         &lt;env-entry-value&gt;....&lt;/env-entry-value&gt;
    *         &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;
    *     &lt;/env-entry&gt;
    * </pre>
    *
    */
   private static final String DEFAULT_JNDI_NAME =
      "org.astrogrid.community.config";

   /**
    * The default system property name.
    *
    */
   public static final String DEFAULT_PROPERTY_NAME =
      "org.astrogrid.community.config";

   private ConfigurationManager manager;

   private static CommunityJNDIConfig instance = null;

   /* (non-Javadoc)
    * @see org.astrogrid.community.common.Config#getProperty(java.lang.String)
    */
   public String getProperty(String key) {
      return config.getProperty(key, "no property specified", DEFAULT_CATEGORY);
   }

   private CommunityJNDIConfig() {
      String path;
      try {
         Context initCtx = new InitialContext();
         Context envCtx = (Context)initCtx.lookup("java:comp/env");
         path = (String)envCtx.lookup(DEFAULT_JNDI_NAME);
         logger.info("config path=" + path);
         manager = ConfigurationManager.getInstance();
         if (path.startsWith("http")) {
            URLHandler handler = new URLHandler();
            handler.setURL(path);
            manager.load(handler, CONFIG_NAME);
         }
         else {
            File file = new File(path);
            XMLFileHandler handler = new XMLFileHandler();
            handler.setFile(file);
            handler.load(file);
            manager.load(handler, CONFIG_NAME);
         }
         config = ConfigurationManager.getConfiguration(CONFIG_NAME);
         logger.info("jconfig=" + config.getXMLAsString());
         logger.info(CommunityConstants.DATABASE_NAME_KEY +" is " + config.getProperty(CommunityConstants.DATABASE_NAME_KEY,null,DEFAULT_CATEGORY));
      }
      catch (NamingException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (ConfigurationManagerException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   /**
    * @return
    */
   public static CommunityJNDIConfig getInstance() {
      if (instance == null) {
         instance = new CommunityJNDIConfig();

      }

      return instance;
   }

}
