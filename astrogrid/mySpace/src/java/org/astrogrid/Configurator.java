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
import org.apache.log4j.Logger;
import org.astrogrid.i18n.AstroGridMessage;

public abstract class Configurator {
	
	/** Compile-time switch used to turn tracing on/off. 
	  * Set this to false to eliminate all trace statements within the byte code.*/	 
	private static final boolean 
		TRACE_ENABLED = true ;
	    
	private static final String
		ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE = "AG{0}Z00001:{1}: Could not read my configuration file {2}",
		ASTROGRIDERROR_COMPONENT_NOT_INITIALIZED = "AG{0}Z00002:{1}: Not initialized. Perhaps my configuration file is missing." ;
		
    public static final String
        GENERAL_CATEGORY = "GENERAL" ,
        GENERAL_VERSION_NUMBER = "VERSION" ;
        
	private static final String
	    TEMPLATE = "TEMPLATE" ;
	
	/** Log4J logger for this class. */    			    			   			
	private static Logger 
		logger = Logger.getLogger( Configurator.class ) ;
        
    private static Hashtable
        loadedConfigurations = new Hashtable() ;
    
    
	protected Configurator() { this.init() ; } 
    
    
    private void init() {
        if( TRACE_ENABLED ) logger.debug( "Configurator.init(): entry") ;
            
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
                logger.error( message.toString() ) ;  
                                            
            }
            else {
            
                // Attempts to load the Subsystem's default installation messages
                // from an appropriate properties' file...
                AstroGridMessage.loadMessages( this.getSubsystemAcronym() ) ;
            }
            
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "Configurator.init(): exit") ;      
        } 
                 
    } // end of init()
    
	
	public void checkPropertiesLoaded() throws AstroGridException { 
		if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() entry") ;
        
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
				logger.error( message.toString() ) ;
				throw new AstroGridException( message ) ;
			}
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() exit") ;
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
        if( TRACE_ENABLED ) logger.debug( "getProperty() entry") ;         

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
                targetProperty = TemplateManager.getInstance().getTemplate( targetProperty ) ;
            }  
                      
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "getProperty() exit") ;                   
        }
        
        return targetProperty ;         
              
    } // end of getProperty()       
    
	
	abstract protected String getConfigFileName() ;
    abstract protected String getSubsystemAcronym() ;
    
    private static Configuration getConfig( String subsystemAcronym, String configFileName ) {
        if( TRACE_ENABLED ) logger.debug( "Configurator.getConfig(): entry") ;  
        
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
            logger.error( cme ) ;
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "Configurator.getConfig(): exit") ;            
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
