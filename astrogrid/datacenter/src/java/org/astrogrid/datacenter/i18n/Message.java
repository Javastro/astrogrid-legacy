/*
 * @(#)Message.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.i18n;

import org.apache.log4j.Logger;
import java.util.ResourceBundle ;
import java.text.MessageFormat ;
import java.util.MissingResourceException ;

/**
 * The <code>Message</code> class represents a message in a way
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
 * @see     org.astrogrid.datacenter.DatasetAgent
 * @see     java.text.MessageFormat
 * @see     java.util.ResourceBundle
 */
public class Message {
	
	/** Compile-time switch used to turn tracing on/off. */
	private static final boolean 
		TRACE_ENABLED = false ;
	
	/** Log4J logger for this class. */    			
	private static Logger 
		logger = Logger.getLogger( Message.class ) ;
	
	/** These messages are ones hard-coded in the program. */	
	private static final String
        ASTROGRIDERROR_MESSAGEKEY_NULL = "AGDTCZ00003:Message: Message key is null",
        ASTROGRIDERROR_MESSAGE_PATTERN_OR_INSERTS_INVALID = 
			 "AGDTCZ00004:Message: Message pattern or its inserts are invalid for message with key [{0}]",
        ASTROGRIDERROR_MESSAGE_NOT_FOUND_IN_RESOURCEBUNDLE = 
			 "AGDTCZ00005:Message: Message with key [{0}] not found in ResourceBundle" ;
	
	/** The installation default language resource bundle. */	
    private static ResourceBundle
        messages ;
	
	/** Message key 
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
	    
	/**
	   *  
	   * Establishes the default language for an installation.
	   * <p>
	   * Must only be called during the static initialization 
	   * of a component (eg: DatasetAgent), and thereafter not changed!
	   * Otherwise toString() will not be thread safe.
	   * 
	   **/         
	public static void setMessageResource ( ResourceBundle aResourceBundle ) {
		if( TRACE_ENABLED ) logger.debug( "setMessageResource(): entry") ;
	    messages = aResourceBundle ;	
		logger.debug( messages.toString() ) ;  
		if( TRACE_ENABLED ) logger.debug( "setMessageResource(): exit") ;  
	}
	    
	
	/**
	   *  
	   * Convenience constructor for message with no inserts.
	   * <p>
	   * @param key - message key
	   **/      
	public Message( String key ) {
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
	public Message( String key, Object insert1 ) {
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
	public Message( String key, Object insert1, Object insert2 ) {
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
	public Message( String key, Object insert1, Object insert2, Object insert3 ) {
		this(key, new Object[] {insert1,insert2,insert3} ) ;
	}	
	
	
	/**
	   *  
	   * Generalized constructor for message with any number of inserts.
	   * <p>
	   * @param key - message key
	   * @param inserts - array of message inserts
	   **/      
    public Message( String key, Object[] inserts ) {
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
			MessageFormat.format( messageString, inserts ) ;
		}
		catch( NullPointerException npex ) { 
			retValue = ASTROGRIDERROR_MESSAGEKEY_NULL ;
			logger.debug( ASTROGRIDERROR_MESSAGEKEY_NULL ) ;  
		}
		catch( MissingResourceException mrex ) {
			Object[]
			   oa = {key} ;
			retValue = MessageFormat.format( ASTROGRIDERROR_MESSAGE_NOT_FOUND_IN_RESOURCEBUNDLE, oa ) ;
			logger.debug( retValue ) ;   
		}
		catch( IllegalArgumentException iaex ) {
			Object[]
			   oa = {key} ;
			retValue = MessageFormat.format( ASTROGRIDERROR_MESSAGE_PATTERN_OR_INSERTS_INVALID, oa ) ;
			logger.debug( retValue ) ;  
		}
		finally {   
			if( TRACE_ENABLED ) logger.debug( "toString(ResourceBundle): exit") ;
		}

		return retValue ;
		
	} // end of toString(ResourceBundle)
 
    
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
    	
       if( messages != null ) {
       	  retValue = this.toString( messages );
       }
       else {       
          retValue  = concatinateKeyAndInserts() ;
       }
       
	   if( TRACE_ENABLED ) logger.debug( "toString(): exit") ;	  	      
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
    
    
} // end of class Message
