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

import java.util.Hashtable ;
import org.jconfig.* ;
import org.jconfig.handler.* ;
import org.astrogrid.log.Log;
import org.astrogrid.i18n.AstroGridMessage;

public abstract class Configurator {


   private static final String
      ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE =
            "AG{0}Z00001:{1}: Could not read my configuration file {2}. Missing file or malformed XML.",
      ASTROGRIDERROR_COMPONENT_NOT_INITIALIZED =
            "AG{0}Z00002:{1}: Not initialized. Perhaps my configuration file is missing or contains malformed XML." ;

    public static final String
        GENERAL_CATEGORY = "GENERAL" ,
        GENERAL_VERSION_NUMBER = "VERSION" ;

   private static final String
       TEMPLATE = "TEMPLATE." ;

    private static Hashtable
        loadedConfigurations = new Hashtable() ;


   protected Configurator() { this.init() ; }


    private void init() {
        Log.trace( "Configurator.init(): entry") ;

        try{
            // Attempts to load the Subsystem's configuration details
            // from an appropriate xml-based properties file...
            if( Configurator.getConfig( getSubsystemAcronym()
                                      , getConfigFileName() ) == null ) {

                // But it couldn't be found, so log a message...
                AstroGridMessage
                   message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE
                                                 , getSubsystemAcronym()
                                                 , Configurator.getClassName( Configurator.class )
                                                 , getConfigFileName() ) ;
                Log.logError( message.toString() ) ;
                return ;

            }

            try {
                // Attempts to load the Subsystem's default installation messages
                // from an appropriate properties' file...
                AstroGridMessage.loadMessages( this.getSubsystemAcronym() ) ;
            }
            catch( AstroGridException agex ) {
                Log.logError( agex.getAstroGridMessage().toString() ) ;
            }

        }
        finally {
            Log.trace( "Configurator.init(): exit") ;
        }

    } // end of init()


   public void checkPropertiesLoaded() throws AstroGridException {
      Log.trace( "checkPropertiesLoaded() entry") ;

        String
           check = "NOT LOADED" ;
        Configuration
           config = getConfig( this.getSubsystemAcronym(), this.getConfigFileName() ) ;

      try{
         if( config == null
                ||
                config.getProperty( GENERAL_VERSION_NUMBER, check, GENERAL_CATEGORY ).equals( check ) ) {

            AstroGridMessage
               message = new AstroGridMessage( ASTROGRIDERROR_COMPONENT_NOT_INITIALIZED
                                             , this.getSubsystemAcronym()
                                             , Configurator.getClassName( Configurator.class ) ) ;
            Log.logError( message.toString() ) ;
            throw new AstroGridException( message ) ;
         }
      }
      finally {
         Log.trace( "checkPropertiesLoaded() exit") ;
      }

   } // end checkPropertiesLoaded()


    /**
      *
      * Static getter for properties from the component's configuration.
      * <p>
      *
      * @param key - the property key within category
      * @param category - the category within the configuration
      * @return the String value of the property, or the empty string if null
      *
      * @see org.jconfig.jConfig
      **/
    public static String getProperty( String subsystemAcronym, String key, String category ) {
        Log.trace( "getProperty() entry") ;

        String
            targetProperty = null ;

        try {

            Configuration
               config = ConfigurationManager.getConfiguration( subsystemAcronym ) ;

            targetProperty = config.getProperty( key                  // key within category
                                               , ""                   // default value
                                               , category )           // category within config
                                               .trim() ;              // ensure no surrounding spaces

            if( key.startsWith( TEMPLATE ) ) {
                targetProperty = TemplateManager.getInstance().getTemplate( subsystemAcronym
                                                                          , targetProperty ) ;
            }

        }
        finally {
            Log.trace( "getProperty() exit") ;
        }

        return targetProperty ;

    } // end of getProperty()


   abstract protected String getConfigFileName() ;
    abstract protected String getSubsystemAcronym() ;

    private static Configuration getConfig( String subsystemAcronym, String configFileName ) {
        Log.trace( "Configurator.getConfig(): entry") ;

        Configuration
            configuration = null ;

        try {

            if( loadedConfigurations.containsKey( subsystemAcronym ) == false ) {

                InputStreamHandler
                    streamHandler = new InputStreamHandler( configFileName ) ;

                ConfigurationManager.getInstance().load( streamHandler, subsystemAcronym ) ;
                loadedConfigurations.put( subsystemAcronym, configFileName ) ;
            }

            configuration = ConfigurationManager.getConfiguration( subsystemAcronym ) ;
        }
        catch ( ConfigurationManagerException cme ) {
            Log.logError("Could not get config subsystem '"+subsystemAcronym+"', filename '"+configFileName+"'", cme ) ;
        }
        finally {
            Log.trace( "Configurator.getConfig(): exit") ;
        }

        return configuration ;

    } // end of getConfig()


    public static String getClassName ( java.lang.Class cls ) {

        String
            componentName = cls.getName() ;
         int
            iLastPoint = componentName.lastIndexOf('.') ;
        return componentName.substring( iLastPoint + 1) ;

    }

} // end of class Configuration
