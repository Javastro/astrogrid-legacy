/*
 * $Id: JNDIConfig.java,v 1.2 2003/12/09 23:01:15 pah Exp $
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

package org.astrogrid.applications.common.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * This type of config loads a property file using a JNDI key as an indirect reference
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration3
 */

public class JNDIConfig implements Config {
   private boolean propertiesLoaded;
   /**
    * The property set
    */
   private Properties props;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(JNDIConfig.class);
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

   /* (non-Javadoc)
    * @see org.astrogrid.community.common.Config#getProperty(java.lang.String)
    */
   public String getProperty(String key) {
      return props.getProperty(key);
   }

   public JNDIConfig(String jndiName) {
      props = new Properties();
      propertiesLoaded = loadProperties(jndiName);
   }

   private boolean loadProperties(String jndiName) {
      URL configUrl = null;
      boolean retval = false;
      try {
         logger.info("looking for properties with JNDI name " + jndiName);
         Object o = null;
         Context initCtx = new InitialContext();
//         printJNDIenv(initCtx, "init environment");
         Context javaContext = (Context)initCtx.lookup("java:comp/env");
         logger.info("got the java context ok");
//         printJNDIenv(javaContext, "java environment");

         o = javaContext.lookup(jndiName);
         

         if (o instanceof URL) {
            configUrl = (URL)o;
            
         }
         else
            if (o instanceof String) {
               logger.info("got string "+ (String)o);
               configUrl = new URL((String)o);
            }
            else {
               logger.debug(
                  "Found resource in JNDI, but of incorrect type :"
                     + o.getClass().getName());

            }

         //load the resources
         if (configUrl != null) {
            logger.info("found resource pointer at " + configUrl.toString());
            InputStream str = configUrl.openStream();
            logger.info(str.toString());
            if(str==null)
            {
               logger.error("cannot open stream to "+ configUrl.toString());
            }
            props.load(str);
            retval = true; // have been successfull
         }

      }
      catch (NamingException e) {
         logger.error("problem finding the name", e);
 
      }
      catch (MalformedURLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return retval;
   }

   private void printJNDIenv(Context javaContext, String ctx) throws NamingException {
      Hashtable env = javaContext.getEnvironment();
      logger.info(ctx);
      Set keys = env.keySet();
      for (Iterator iter = keys.iterator(); iter.hasNext();) {
         Object element = iter.next();
         logger.info(element.toString());
         
      }
      logger.info(ctx + " end");
   }

   private DataSource loadDataSource(String jndiName) {
      DataSource configUrl = null;
      boolean retval = false;
      try {
         Object o = null;
         Context initCtx = new InitialContext();
         logger.info("looking for Datasource JNDI name " + jndiName);
         if (o == null || o instanceof Context) {
            Context javaContext = (Context)initCtx.lookup("java:comp/env");
            o = javaContext.lookup(jndiName);
         }

         if (o instanceof DataSource) {
            configUrl = (DataSource)o;
            logger.info("created datasource "+ configUrl.toString());
         }
         else {
            logger.debug(
               "Found resource in JNDI, but of incorrect type :"
                  + o.getClass().getName());

         }

      }
      catch (NamingException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return configUrl;
   }

   /**
    * @return
    */

   /* (non-Javadoc)
    * @see org.astrogrid.applications.common.config.Config#getDataSource(java.lang.String)
    */
   public DataSource getDataSource(String key) {
      return loadDataSource(key);
   }

}
