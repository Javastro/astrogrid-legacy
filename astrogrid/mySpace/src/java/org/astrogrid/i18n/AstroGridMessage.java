/*
 * @(#)AstroGridMessage.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Hashtable ;

import org.astrogrid.Configurator ;
import org.astrogrid.AstroGridException ;

import org.apache.log4j.Logger;

/**
 * The <code>AstroGridMessage</code> class represents a message in a way
 * amenable to internationalization.
 * <p>
 * It utilizes the technique of the java.text.MessageFormat class to merge
 * inserts into a message template. The message templates are held in
 * properties file(s) which can be produced on a per language basis
 * or even a per country basis.
 * <p> 
 * The basic AstroGrid file is ASTROGRID_datacentermessages.properties
 * Each properties file is loaded as a resource bundle.
 * <p>
 * The philosophy on messages is two-fold:
 * <p>
 * (1) It is assumed an installation has a default language. Such might
 * be used for displaying logging messages to system administrators. This
 * should be loaded at static initialization time by a major AstroGrid
 * component (in this instance the DatasetAgent of the datacenter).
 * If messages are required on this basis, use the toString() method.
 * <p>
 * (2) However, astronomers/customers are likely to be from many countries.
 * If messages are required on such a basis, then pass the appropriate
 * language resource bundle to the toString(ResourceBundle) method.
 *
 * @author  Jeff Lusted
 * @version 1.0 28-May-2003
 * @since   AstroGrid 1.2
 * 
 * @see     org.astrogrid.Component
 * @see     java.text.MessageFormat
 * @see     java.util.ResourceBundle
 */
public class AstroGridMessage {
	
	/** Compile-time switch used to turn tracing on/off. */
	private static final boolean 
		TRACE_ENABLED = false ;
	
	/** Log4J logger for this class. */    			
	private static Logger 
		logger = Logger.getLogger( AstroGridMessage.class ) ;
        
    public static final String
    /** Messages category within the component's configuration */  
        MESSAGES_CATEGORY     = "MESSAGES" ,    
    /** Key within the component's configuration which helps identify the appropriate
     *  language ResourceBundle. */  
        MESSAGES_BASENAME     = "INSTALLATION.BASENAME" ,
    /** Key within the component's configuration which helps identify the appropriate
     *  language ResourceBundle. */  
        MESSAGES_LANGUAGECODE = "INSTALLATION.LANGUAGECODE" ,
    /** Key within the component's configuration which helps identify the appropriate
     *  language ResourceBundle. */  
        MESSAGES_COUNTRYCODE  = "INSTALLATION.COUNTRYCODE" ,      
    /** Key within the component's configuration which helps identify the appropriate
     *  language ResourceBundle. */  
        MESSAGES_VARIANTCODE  = "INSTALLATION.VARIANT" ;                
        
    private static final String
        SUBSYSTEM_KEY= "SUBSYSTEM" ;
	
	/** These messages are ones hard-coded in the program. */	
	private static final String
        ASTROGRIDERROR_MESSAGEKEY_NULL = 
           "AG{0}Z00003:AstroGridMessage: Message key is null",
        ASTROGRIDERROR_MESSAGE_PATTERN_OR_INSERTS_INVALID = 
           "AG{0}Z00004:AstroGridMessage: Message pattern or its inserts are invalid for message with key [{1}]",
        ASTROGRIDERROR_MESSAGE_NOT_FOUND_IN_RESOURCEBUNDLE = 
           "AG{0}Z00005:AstroGridMessage: Message with key [{1}] not found in ResourceBundle" ,
        ASTROGRIDERROR_MESSAGE_RESOURCEBUNDLE_NOTFOUND = 
           "AG{0}Z00006:{1}: Message ResourceBundle [{2}] not found in classpath" ;           
           
    private static final String
        Z_MESSAGE_PREFIX_PATTERN = "AG{0}Z" ;
        
    private static final int
        SYSTEMACRONYM_STARTINDEX = 2,
        SYSTEMACRONYM_ENDINDEX = 5 ;
	
    /** The installation default language resource bundles.
     *  There can be one per AstroGrid subsystem. That means if two subsystems
     *  are running in the same VM, there will be two. */   		 
    private static Hashtable
        resourceBundles = new Hashtable() ;
	
	/** AstroGridMessage key 
	 *  @see the ASTROGRID_jesmessages.properties file
	 * 	@see java.text.MessageFormat                       */	
	private String
		key ;
		
	/** Any inserts that may be required by the message.
	 *  Null if message has no inserts. 
	 * 
	 *  @see java.text.MessageFormat
	 *                                                     */
	private Object[]
	    inserts ; 
        
    private static class ResourceBundleKey {
        
        private String key ;
        
        ResourceBundleKey( String subsystemAcronym ) {
            key = subsystemAcronym ;
        }
        
        ResourceBundleKey( String subsystemAcronym, Locale locale ) {
            key = subsystemAcronym + locale.toString() ;               
        }
        
        public int hashCode(){ return key.hashCode() ; }
        
    } // end of class ResourceBundleKey
	    
        
	/**
	   *  
	   * Establishes the default language for an installation.
	   * <p>
	   * Must only be called during the static initialization 
	   * of a component (eg: DatasetAgent), and thereafter not changed!
	   * Otherwise toString() will not be thread safe.
	   * 
	   **/         
	public static void setMessageResource ( String subsystemAcronym, ResourceBundle aResourceBundle  ) {
		if( TRACE_ENABLED ) logger.debug( "setMessageResource(): entry") ;
		
		try {
			resourceBundles.put( subsystemAcronym, aResourceBundle ) ;
        }
		finally {
			if( TRACE_ENABLED ) logger.debug( "setMessageResource(): exit") ;  
		}

	} // end of setMessageResource()
	
	
	public static boolean isMessageResourceLoaded( String subsystemAcronym ) {
		boolean
		    bLoaded = false ;
		    
		try {
			bLoaded = resourceBundles.containsKey( subsystemAcronym.toUpperCase() ) ;
		}
		catch( Exception ex){
			; // Do nothing. Any problems and we default to false.
		}
		
		return bLoaded ;
		
	} // end of isMessageResourceLoaded()
    
    
    /**
      *  
      * <p>
      * 
      **/               
    public static ResourceBundle loadMessages( String subsystemAcronym ) 
           throws AstroGridException {
              
        String 
            language = Configurator.getProperty( subsystemAcronym 
                                               , MESSAGES_LANGUAGECODE
                                               , MESSAGES_CATEGORY) ,
            country = Configurator.getProperty( subsystemAcronym
                                              , MESSAGES_COUNTRYCODE
                                              , MESSAGES_CATEGORY) ,
            variant = Configurator.getProperty( subsystemAcronym
                                              , MESSAGES_VARIANTCODE
                                              , MESSAGES_CATEGORY) ;
                                           
        Locale
            locale = new Locale( language, country, variant ) ;

        logger.debug( "language[" + language + "]\t" +
                      "country[" + country + "]\t" +
                      "variant[" + variant + "]\t" +
                      "locale[" + locale.toString() + "]"  ) ; 
                            
        return AstroGridMessage.loadMessages( subsystemAcronym
                                            , locale ) ;
              
    } // end of loadMessages()
    
     
    /**
      *  
      * <p>
      * 
      **/               
    public static synchronized ResourceBundle loadMessages( String subsystemAcronym, Locale locale ) 
                  throws AstroGridException {
        if( TRACE_ENABLED ) logger.debug( "loadMessages(): entry") ;
        
        ResourceBundle
            resourceBundle = null ;
            
        try {
            ResourceBundleKey
               key =  new ResourceBundleKey( subsystemAcronym, locale ) ;
            resourceBundle = (ResourceBundle)resourceBundles.get( key ) ;
            
            if( resourceBundle == null ) {
          
                String 
                    messageBundleBaseName = Configurator.getProperty( subsystemAcronym
                                                                    , MESSAGES_BASENAME
                                                                    , MESSAGES_CATEGORY) ;

                resourceBundle = ResourceBundle.getBundle( messageBundleBaseName, locale ) ; 
                resourceBundles.put( key, resourceBundle ) ;
            }  
              
            
        }
        catch( MissingResourceException mrex ) {
            AstroGridMessage
               message = new AstroGridMessage( ASTROGRIDERROR_MESSAGE_RESOURCEBUNDLE_NOTFOUND
                                             , subsystemAcronym
                                             , Configurator.getClassName( AstroGridMessage.class )
                                             , locale.toString() ) ;
            throw new AstroGridException( message ) ; 
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "loadMessages(): exit") ;     
        }
        
        return resourceBundle ;
              
    } // end of loadMessages()
    
    
	/**
	   *  
	   * Convenience constructor for message with no inserts.
	   * <p>
	   * @param key - message key
	   **/      
	public AstroGridMessage( String key ) {
		this.key = key ;
		this.inserts = null ;
	}

	
	/**
	   *  
	   * Convenience constructor for message with one insert.
	   * <p>
	   * @param key - message key
	   * @param insert1 - first message insert
	   **/      	    
	public AstroGridMessage( String key, Object insert1 ) {
		this.key = key ;
		this.inserts = new Object[] {insert1} ;
	}
	

	/**
	   *  
	   * Convenience constructor for message with two inserts.
	   * <p>
	   * @param key - message key
	   * @param insert1 - first message insert
	   * @param insert2 - second message insert
	   **/      
	public AstroGridMessage( String key, Object insert1, Object insert2 ) {
		this(key, new Object[] {insert1,insert2} ) ;
	} 
	
	
	/**
	   *  
	   * Convenience constructor for message with three inserts.
	   * <p>
	   * @param key - message key
	   * @param insert1 - first message insert
	   * @param insert2 - second message insert
	   * @param insert3 - third message insert
	   **/      
	public AstroGridMessage( String key, Object insert1, Object insert2, Object insert3 ) {
		this(key, new Object[] {insert1,insert2,insert3} ) ;
	}	
	
	
    /**
       *  
       * Convenience constructor for message with four inserts.
       * <p>
       * @param key - message key
       * @param insert1 - first message insert
       * @param insert2 - second message insert
       * @param insert3 - third message insert
       * @param insert4 - fourth message insert
       **/      
    public AstroGridMessage( String key, Object insert1, Object insert2, Object insert3, Object insert4 ) {
        this(key, new Object[] {insert1,insert2,insert3,insert4} ) ;
    }       
    
	/**
	   *  
	   * Generalized constructor for message with any number of inserts.
	   * <p>
	   * @param key - message key
	   * @param inserts - array of message inserts
	   **/      
    public AstroGridMessage( String key, Object[] inserts ) {
       this.key = key ;
       this.inserts = inserts ;	
    }
  
	/**
	   *  
	   * A resource bundle can be passed across with any message.
	   * The bundle will represent the language that it is desired
	   * the message be 'translated' into.
	   * <p> 
	   * For example, a datacenter may be a French installation
	   * and have its admin messages (for logging purposes) printed 
	   * out in French, but its customers may be any nationality.
	   * If it is desired that astronomers get messages in their
	   * own language rather than the installation default, then
	   * the appropriate language resource bundle must be passed
	   * in to this method.
	   * <p>
	   * @param mssgs - a language-specific ResourceBundle
	   * @return - a language-specific string corresponding to
	   *           this message.
	   **/  
	public String toString( ResourceBundle mssgs ) {
		if( TRACE_ENABLED ) logger.debug( "toString(ResourceBundle): entry") ;	
		
		String
		   retValue = null,
		   messageString = null ;
		   
		try {
			// Note we concatinate the key (the astrogrid message number) 
			// with the message to produce the format 
			// <message number>:<message text>
			messageString = key + mssgs.getString( key ) ;   
			retValue = MessageFormat.format( messageString, inserts ) ;
		}
		catch( NullPointerException npex ) { 
            Object[]
               oa = new Object[0] ;
            retValue = generateErrantMessage( mssgs
                                            , ASTROGRIDERROR_MESSAGEKEY_NULL
                                            , oa ) ;            
			logger.debug( retValue ) ;  
		}
		catch( MissingResourceException mrex ) {
			Object[]
			   oa = { key } ;
            retValue = generateErrantMessage( mssgs
                                            , ASTROGRIDERROR_MESSAGE_NOT_FOUND_IN_RESOURCEBUNDLE
                                            , oa ) ;
                                            
			logger.debug( retValue ) ;   
		}
		catch( IllegalArgumentException iaex ) {
			Object[]
			   oa = { key } ;
            retValue = generateErrantMessage( mssgs
                                            , ASTROGRIDERROR_MESSAGE_PATTERN_OR_INSERTS_INVALID
                                            , oa ) ;            
			logger.debug( retValue ) ;  
		}
		finally {   
			if( TRACE_ENABLED ) logger.debug( "toString(ResourceBundle): exit") ;
		}

		return retValue ;
		
	} // end of toString(ResourceBundle)
 
    
    public String toString( Locale locale ) {
        
        ResourceBundle
           resourceBundle = null ;
        String
           retValue = null ;
           
        try {  
            resourceBundle = AstroGridMessage.loadMessages( this.extractSubsystemAcronym()
                                                          , locale ) ; 
            retValue = this.toString( resourceBundle ) ;
        }
        catch( AstroGridException agex ) {         
            retValue = agex.getAstroGridMessage().toString() ;  
        }
           
        return retValue ;
        
    }
    
    
    
   /**
    *  
    * The default toString() uses the statically defined resource 
    * bundle, which is assumed to be the installation language default for 
    * messages. For example, a datacenter may be a French installation
    * and have its admin messages (for logging purposes) printed out in French.
    * <p>
    * If no installation language default haa been set up, invoking this
    * method will result in a simple concatination of message key with
    * any inserts which have been passed across with the message. 
    *
    * @return - a string hopefully corresponding to the message in 
    *           the installation's default language
	**/
    public String toString() {
	   if( TRACE_ENABLED ) logger.debug( "toString(): entry") ;
  	
       String
          retValue = null ;
		
	   try {
			
           // If this is a hard coded Z-style message, go straight for message formatting
           // without trying to load the message from a ResourceBundle...
           if( key.startsWith( Z_MESSAGE_PREFIX_PATTERN ) ) {
               retValue = retValue = MessageFormat.format( key, inserts ) ;
            }
            else {
                
               // Attempt to get the message from the appropriate messages ResourceBundle... 
               ResourceBundle
                   messages = null ;
                  
			   try {                                                          
				   messages = AstroGridMessage.loadMessages( this.extractSubsystemAcronym() ) ;			
			   }
			   catch( Exception ex ){
				   ; // This is a fail safe, so we do nothing!
			   }
			   	
			   if( messages != null ) {
			       retValue = this.toString( messages );
			   }
			   else {       
			       retValue  = concatinateKeyAndInserts() ;
			   }
            
            }
			   
	   } finally {
		    if( TRACE_ENABLED ) logger.debug( "toString(): exit") ;	
	   }
       	      
       return retValue ;
       
    } // end toString()
    
    
    /**
	   * Concatinates message key with message inserts, plus suitable wrappers.
	   * <p>
	   * This is a simple fail-safe mechanism to ensure something relatively
	   * intelligable emerges in those instances where a message cannot be 
	   * located in a suitable properties file.
	   *    
	   * @return - A simple concatination of message key and inserts
	   */
	private String concatinateKeyAndInserts() {
    	  
	   StringBuffer
		  buffer = new StringBuffer(64) ;
	   buffer
		  .append( "Message[")
		  .append( key )
		  .append( "]  " );
        
	   if( inserts != null ) {  
      
		   for( int i=0; i < inserts.length; i++ ) {
			  buffer
				.append( "Insert")
				.append( i+1 )
				.append( "[")
				.append( inserts[i].toString() )
				.append( "]  ") ;  
       	           
		   } // end for
	   }
          
	   return buffer.toString() ;
			   
    } // end of concatinateKeyAndInserts()
    
    
    private String generateErrantMessage( ResourceBundle mssgs, String message, Object[] inserts ) {
        String
           retValue = null ;
        Object[]
           insertsPlus = new Object[ inserts.length + 1 ] ;
        System.arraycopy( inserts, 0, insertsPlus, 1, inserts.length ) ;
        String
           subsystemAcronym = mssgs.getString( SUBSYSTEM_KEY ) ;
        if( subsystemAcronym == null || subsystemAcronym.equals("") ) {
            subsystemAcronym = "..." ;
        }
        insertsPlus[0] = subsystemAcronym ;
        retValue = MessageFormat.format( message, insertsPlus ) ;
            
        return retValue ;
    }
    
    
    private String extractSubsystemAcronym() {
        return this.key.substring( SYSTEMACRONYM_STARTINDEX
                                 , SYSTEMACRONYM_ENDINDEX ) ;       
    }
    
    
} // end of class Message
