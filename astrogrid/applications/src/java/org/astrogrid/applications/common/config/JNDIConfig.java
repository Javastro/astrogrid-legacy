/*
 * $Id: JNDIConfig.java,v 1.1 2003/11/26 22:07:24 pah Exp $
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


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
      String path;
      propertiesLoaded = loadProperties(jndiName);
   }

   private boolean loadProperties(String jndiName) {
      URL configUrl=null;
      boolean retval = false;
      try {
          Object o = null;
          Context initCtx = new InitialContext();
          o = initCtx.lookup(jndiName);
          if (o == null || o instanceof Context) {
              Context javaContext = (Context)initCtx.lookup("java:comp/env");
              o = javaContext.lookup(jndiName);
          }
          
          if (o instanceof URL) {                            
                configUrl = (URL) o;             
          } else if (o instanceof String) {
              configUrl = new URL((String)o);
          } else {
              logger.debug("Found resource in JNDI, but of incorrect type :" + o.getClass().getName());
            
          }
          
          //load the resources
          if (configUrl != null) {
            props.load(configUrl.openStream());
            retval = true; // have been successfull
         }
          
      }
      catch (NamingException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (MalformedURLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return retval;
   }

   /**
    * @return
    */
 
}
