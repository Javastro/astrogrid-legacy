/*
 * $Id: ConfigFactory.java,v 1.2 2005/03/28 01:24:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.cfg;

import java.io.IOException;
import java.util.Hashtable;

/**
 * For those who might want to create multiple configuration <i>instances</i>,
 * this factory can be used.  NB multiple configuration <i>files</i> can be
 * read into a single instance.
 * <p>
 * If Astrogrid's config implementation changes, change the factory method.
 * <p>
 * This class is temporarily renamed CfgFactory - it should really be ConfigFactory -
 * to separate it from the 'old' one in common
 * <p>
 *
 * @author M Hill
 */


public class ConfigFactory
{
   /** Created PropertyConfig instances */
   private static final Hashtable configs = new Hashtable();
   
   /** A 'common-to-all' config, @see getCommonConfig */
   private static ConfigReader common = makeConfig(ConfigFactory.class);
   
   /**
    * Creates an instance of a config reader (that provides property values)
    * associated with the given id.
    * @param id specifies a particular instance
    */
   public static synchronized ConfigReader getConfig(Object id)
   {
      ConfigReader config = (ConfigReader) configs.get(id);
   
      if (config == null) {
         config = makeConfig(id);
         configs.put(id, config);
      }

      return config;
   }

   /** Creates an instance of the right reader for the given id */
   public static ConfigReader makeConfig(Object id) {
      return new FailbackConfig(id);
   }
   
   
/** Returns the same instance every time so that all packages have easy access to the *same*
 * configuration set in one runtime environment, although this may be loaded
 * from several files.  So although this configuration is a common access point, it can be used to
 * access several configuration files.  Any application can call the 'load'
 * methods and properties will be loaded from the given file/url/etc.
 * <p/>
 * This does mean that packages using the configuration file must make sure their
 * keys are unique. The most reliable way to do this is to prefix each key with
 * the package name (ie the code namespace) but this doesn't produce very nice
 * property files for humans to edit.
 */
   public static ConfigReader getCommonConfig() {
      return common;
   }

   /** Returns the configuration for the current user that the application is
    * running under. This is useful for things like user options. The given
    * context is for seperating out different option files; eg 'storebrowser'
    * options, or 'Look And Feel' options */
   public static ConfigReader getUserOptions(String context) throws IOException {
      return new UserOptions(context);
   }
   
}

