/*
 * @(#)Configurator.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.LogFactory;
import org.jconfig.Configuration;
import org.jconfig.ConfigurationManager;
import org.jconfig.ConfigurationManagerException;
import org.jconfig.handler.ConfigurationHandler;
import org.jconfig.handler.InputStreamHandler;
import org.jconfig.handler.URLHandler;
import org.jconfig.handler.XMLFileHandler;

import org.astrogrid.i18n.AstroGridMessage;

/**
 * A wrapper around jconfig to provide access to configuration files.
 * Subclass this for each component and implement the methods giving the name of the config file.
 * The class first searches JNDI for the location of the file, if that files it looks for config file
 * on the classpath. 
 * @author unknown
 * @see http://www.jconfig.org
 * @TODO factor out the logging and replace with commons logging http://jakarta.apache.org/commons/logging/api/index.html
 */
public abstract class Configurator {
/** error message */
  private static final String ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE =
    "AG{0}Z00001:{1}: Could not read my configuration file {2}. Missing file or malformed XML.",
    ASTROGRIDERROR_COMPONENT_NOT_INITIALIZED =
      "AG{0}Z00002:{1}: Not initialized. Perhaps my configuration file is missing or contains malformed XML.";
/** keys for config file */
  public static final String GENERAL_CATEGORY = "GENERAL",
    GENERAL_VERSION_NUMBER = "VERSION";

/** key to indicate template file should be loaded */
  private static final String TEMPLATE = "TEMPLATE.";

/** config files that have already been loaded*/
  private static Hashtable loadedConfigurations = new Hashtable();
/** ctor */
  protected Configurator() {
    this.init();
  }
/**
 * Initialisation of configurator
 *
 */
  private void init() {
    Log.trace("Configurator.init(): entry");

    try {
      // Attempts to load the Subsystem's configuration details
      // from an appropriate xml-based properties file...
      if (Configurator
        .getConfig(getSubsystemAcronym(), getConfigFileName(), getJNDIName())
        == null) {

        // But it couldn't be found, so log a message...
        AstroGridMessage message =
          new AstroGridMessage(
            ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE,
            getSubsystemAcronym(),
            Configurator.getClassName(Configurator.class),
            getConfigFileName());
        Log.logError(message.toString());
        return;

      }

      try {
        // Attempts to load the Subsystem's default installation messages
        // from an appropriate properties' file...
        AstroGridMessage.loadMessages(this.getSubsystemAcronym());
      } catch (AstroGridException agex) {
        Log.logError(agex.getAstroGridMessage().toString());
      }

    } finally {
      Log.trace("Configurator.init(): exit");
    }

  } // end of init()


  /**
   * Verify that the configuration file has been located and loaded
   * @throws AstroGridException if not
   */
  public final void checkPropertiesLoaded() throws AstroGridException {
    Log.trace("checkPropertiesLoaded() entry");

    String check = "NOT LOADED";
    Configuration config =
      getConfig(
        this.getSubsystemAcronym(),
        this.getConfigFileName(),
        this.getJNDIName());

    try {
      if (config == null
        || config.getProperty(
          GENERAL_VERSION_NUMBER,
          check,
          GENERAL_CATEGORY).equals(
          check)) {

        AstroGridMessage message =
          new AstroGridMessage(
            ASTROGRIDERROR_COMPONENT_NOT_INITIALIZED,
            this.getSubsystemAcronym(),
            Configurator.getClassName(Configurator.class));
        Log.logError(message.toString());
        throw new AstroGridException(message);
      }
    } finally {
      Log.trace("checkPropertiesLoaded() exit");
    }

  } // end checkPropertiesLoaded()

  /**
    *
    * Static getter for properties from the component's configuration.
    * <p>
    *
    * @param subsystemAcronym TLA used to identify component
    * @param key - the property key within category
    * @param category - the category within the configuration
    * @return the String value of the property, or the empty string if null
    *
    * @see org.jconfig.jConfig
    **/
  public static String getProperty(
    final String subsystemAcronym,
    final String key,
    final String category) {
    Log.trace("getProperty() entry");

    String targetProperty = null;

    try {

      Configuration config =
        ConfigurationManager.getConfiguration(subsystemAcronym);

        targetProperty = config.getProperty(key // key within category
    , "" // default value
    , category) // category within config
  .trim(); // ensure no surrounding spaces

      if (key.startsWith(TEMPLATE)) {
        targetProperty =
          TemplateManager.getInstance().getTemplate(
            subsystemAcronym,
            targetProperty);
      }

    } finally {
      Log.trace("getProperty() exit");
    }

    return targetProperty;

  } // end of getProperty()

  /**
   * Set a property
   * @param subsystemAcronym e.g. JES
   * @param key key
   * @param category category
   * @param value value
   */
  public static void setProperty(
    final String subsystemAcronym,
    final String key,
    final String category,
    final String value) {

    Log.trace("setProperty() entry");
    try {

      Configuration config =
        ConfigurationManager.getConfiguration(subsystemAcronym);

      config.setProperty(key, value, category);

      if (key.startsWith(TEMPLATE)) {
        //following the philosophy of if you don't need it now don't write it...
        throw new UnsupportedOperationException("This method isn't supported for TEMPLATEs");
      }

    } finally {
      Log.trace("setProperty() exit");
    }
  }

  /**
   * Save the configuration to a file
   * @param subsystemAcronym e.g.JES
   * @param configFileName where do you want it?
   * @throws AstroGridException probably an IOException
   */
  public static void save(
    final String subsystemAcronym,
    final String configFileName)
    throws AstroGridException {

    Log.trace("save() entry");

    try {

      Configuration config =
        ConfigurationManager.getConfiguration(subsystemAcronym);
      ConfigurationHandler handler = new XMLFileHandler(configFileName);
      ConfigurationManager.getInstance().save(handler, config);

    } catch (ConfigurationManagerException e) {
      Log.logError("Error saving configuration file ", e);
      throw new AstroGridException(e);
    } finally {
      Log.trace("save() exit");
    }
  }

  /**
   * Save the configuration to a file
   * @throws AstroGridException probably an IOException
   */
  public final void save() throws AstroGridException {
    assert getSubsystemAcronym() != null;
    assert getConfigFileName() != null;
    this.save(getConfigFileName());
  }

  /**
   * Save the configuration to a given file
   * @param fileName Name of file
   * @throws AstroGridException probably an IOException
   */
  public final void save(final String fileName) throws AstroGridException {
    assert getSubsystemAcronym() != null;
    Configurator.save(getSubsystemAcronym(), fileName);
  }

  /**
   * Name of xml configuration file - implemented by subclass
   * @return see above
   */
  protected abstract String getConfigFileName();
  /**
   * TLA for specific component - implemented by subclass
   * @return see above
   */
  protected abstract String getSubsystemAcronym();
  /**
   * Returns the JNDI name of the URL for locating the config file.
   * e.g. if you place
   * <verbatim> 
   * &lt;Environment description="URL giving location of the properties file to use for configuration" 
   * name="jesConfigFileURL" override="false" type="java.lang.String" value="http://localhost:8080/ASTROGRID_jesconfig.xml"/&gt;
   * </verbatim>
   * in Tomcat's server.xml file (under a &lt;context&gt; element), then the name you return here would be
   * <verbatim>
   * java:comp/env/jesConfigFileURL
   * </verbatim>
   * @return To be implemented by subclasses.  This just returns null.
   */
  protected abstract String getJNDIName();

  /**
   * Loads the configuration file.  Firstly an attempt is made to find a URL
   * in the naming service.  If the subclass has not set a key, or that key
   * is not set in the NamingService then an attempt is made to load the file
   * from the classpath.  Otherwise the file is loaded from the URL.
   * @param subsystemAcronym the TLA for the component - supplied by subclass
   * @param configFileName the name of the config file on the classpath - supplied by subclass
   * @param jndiName name to lookfor in JNDI, may be null
   * @return The configuration
   */
  private static Configuration getConfig(
    final String subsystemAcronym,
    final String configFileName,
    final String jndiName) {
    Log.trace("Configurator.getConfig(): entry");

    assert subsystemAcronym != null;
    assert !(
      configFileName == null
        && jndiName == null) : "Either configFileName or jndiName must be nonnull";

    Configuration configuration = null;

    try {

      if (!loadedConfigurations.containsKey(subsystemAcronym)) {
        ConfigurationHandler handler = null;
        if (jndiName != null) {
          //  Try Obtaining the config file from a URL stored in JNDI
          try {
            Context ic = new InitialContext();
            String url = (String) ic.lookup(jndiName);
            URLHandler urlHandler = new URLHandler();
            assert url != null;
            urlHandler.setURL(url);
            handler = urlHandler;
          } catch (NamingException ne) {
            Log.logDebug(
              "No InitialContext in Configurator:getConfig "
                + "- unable to get log file URL from JNDI - switch to loading from classpath",
              ne);
          }
        }

        if (handler == null) {
          handler = new InputStreamHandler(configFileName);
        }

        ConfigurationManager.getInstance().load(handler, subsystemAcronym);
        loadedConfigurations.put(subsystemAcronym, configFileName);
      }

      configuration = ConfigurationManager.getConfiguration(subsystemAcronym);
    } catch (Exception cme) {
      Log.logError(
        "Could not get config subsystem '"
          + subsystemAcronym
          + "', filename '"
          + configFileName
          + "'",
        cme);
    } finally {
      Log.trace("Configurator.getConfig(): exit");
    }
    return configuration;

  } // end of getConfig()

  /**
   * Utility method to get the name of a class
   * @param cls the class in question
   * @return its name of course, minus the .class bit
   */
  public static String getClassName(final java.lang.Class cls) {

    String componentName = cls.getName();
    int iLastPoint = componentName.lastIndexOf('.');
    return componentName.substring(iLastPoint + 1);

  }

} // end of class Configuration

/**
 * Delegates to commons logging
 * This class used to use org.astrogrid.log.Log
 * 
 * @author jdt
 */
final class Log {
  /**
   * Do nothing ctor
   *
   */
  private Log() {
  }
  /**
   * Logger for this class
   */
  private static org.apache.commons.logging.Log log =
    LogFactory.getLog(Configurator.class);
  /**
   * delegates to commons logging
   * @param string message
   */
  public static void trace(final String string) {
    log.trace(string);
  }

  /**
   * * delegates to commons logging
   * @param string message
   * @param cme exception
   */
  public static void logError(final String string, final Throwable cme) {
    log.error(string, cme);
  }

  /**
   * * delegates to commons logging
   * @param string message
   * @param ne exception
   */
  public static void logDebug(final String string, final Throwable ne) {
    log.debug(string, ne);

  }

  /**
   * * delegates to commons logging
   * @param string message
   */
  public static void logError(final String string) {
    log.error(string);
  }

}
