/*
 * $Id: ConfigFactory.java,v 1.2 2003/10/07 21:34:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.config;

import java.util.Hashtable;

/**
 * For those who might want to create multiple configuration <i>instances</i>,
 * this factory can be used.  NB multiple configuration <i>files</i> can be
 * read into a single instance.
 * <p>
 * The simplest configuration to use is SimpleConfig, a static Singleton for
 * easy key/value property getting and setting.
 * <p>
 *
 * @author M Hill
 */


public class ConfigFactory
{
   /** Created PropertyConfig instances */
   private static final Hashtable propConfigs = new Hashtable();
   /** Created XmlConfig instances */
   private static final Hashtable xmlConfigs = new Hashtable();
   
   /**
    * Creates an instance of a Property-based (ie key/value pairs) configuration.
    * @param id specifies a particular instance
    */
   public static synchronized PropertyConfig getPropertyConfig(Object id)
   {
      PropertyConfig config = (PropertyConfig) propConfigs.get(id);
   
      if (config == null) {
         config = new PropertyConfig();
         propConfigs.put(id, config);
      }

      return config;
   }
   
   /**
    * Creates an instance of an XML/Document-based (ie XPath/Node list)
    * configuration
    * @param id specifies a particular instance
    */
   public static synchronized PropertyConfig getXmlConfig(Object id)
   {
      PropertyConfig config = (PropertyConfig) xmlConfigs.get(id);
   
      if (config == null) {
         config = new PropertyConfig();
         xmlConfigs.put(id, config);
      }

      return config;
   }
   
}

