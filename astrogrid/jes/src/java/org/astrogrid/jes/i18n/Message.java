/*
 * @(#)Message.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */
package org.astrogrid.jes.i18n;

import org.apache.log4j.Logger;
import java.util.ResourceBundle ;
import java.text.MessageFormat ;

import java.util.MissingResourceException ;

/**
 * The <code>Message</code> class represents a message in a way
 * amenable to internationalization.
 * <p>
 * .... For example:
 * <p><blockquote><pre>
 *     
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @version 1.0 28-May-2003
 * @since   AstroGrid 1.2
 */
public class Message {
	
	private static final boolean 
		TRACE_ENABLED = false ;
	    			
	private static Logger 
		logger = Logger.getLogger( Message.class ) ;
		
	private static final String
		ASTROGRIDERROR_MESSAGEKEY_NULL = "AGJESZ00003:Message: Message key is null",
		ASTROGRIDERROR_MESSAGE_PATTERN_OR_INSERTS_INVALID = "AGJESZ00004:Message: Message pattern or inserts are invalid",
		ASTROGRIDERROR_MESSAGE_NOT_FOUND_IN_RESOURCEBUNDLE = "AGJESZ00005:Message: Message not found in ResourceBundle" ;
		
    private static ResourceBundle
        messages ;
		
	private String
		key ;
	private Object[]
	    inserts ; 
	    
	    
	// Must only be called during the static initialization of a component (eg: DatasetAgent)
	// And thereafter not changed. Otherwise toString() will not be thread safe.
	public static void setMessageResource ( ResourceBundle aResourceBundle ) {
		if( TRACE_ENABLED ) logger.debug( "setMessageResource(): entry") ;
	    messages = aResourceBundle ;	
		logger.debug( messages.toString() ) ;  
		if( TRACE_ENABLED ) logger.debug( "setMessageResource(): exit") ;  
	}
	    
	    
	public Message( String key ) {
		this.key = key ;
		this.inserts = null ;
	}

		    
	public Message( String key, Object insert1 ) {
		this.key = key ;
		this.inserts = new Object[] {insert1} ;
	}
	

	public Message( String key, Object insert1, Object insert2 ) {
		this(key, new Object[] {insert1,insert2} ) ;
	}
	
	
	public Message( String key, Object insert1, Object insert2, Object insert3 ) {
		this(key, new Object[] {insert1,insert2,insert3} ) ;
	}	
	
	
    public Message( String key, Object[] inserts ) {
       this.key = key ;
       this.inserts = inserts ;	
    }
  
  
	public String toString( ResourceBundle mssgs ) {
		if( TRACE_ENABLED ) logger.debug( "toString(ResourceBundle): entry") ;	
		
		String
		   retValue = null,
		   messageString = null ;
		   
		try {
			messageString = key + mssgs.getString( key ) ;   
			MessageFormat.format( messageString, inserts ) ;
		}
		catch( NullPointerException npex ) { 
			retValue = ASTROGRIDERROR_MESSAGEKEY_NULL ;
			logger.debug( ASTROGRIDERROR_MESSAGEKEY_NULL ) ;  
		}
		catch( MissingResourceException mrex ) {
			retValue = ASTROGRIDERROR_MESSAGE_NOT_FOUND_IN_RESOURCEBUNDLE ;
			logger.debug( ASTROGRIDERROR_MESSAGE_NOT_FOUND_IN_RESOURCEBUNDLE ) ;  
		}
		catch( IllegalArgumentException iaex ) {
			retValue = ASTROGRIDERROR_MESSAGE_PATTERN_OR_INSERTS_INVALID ;
			logger.debug( ASTROGRIDERROR_MESSAGE_PATTERN_OR_INSERTS_INVALID ) ;  
		}
		finally {   
			if( TRACE_ENABLED ) logger.debug( "toString(ResourceBundle): exit") ;
		}

		return retValue ;
		
	}
 
    
   /**
    *  
    * 
    *  <p>This method concatinates message key with message inserts, 
	*   plus suitable wrappers.
    * 
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
	   * <p>Concatinates message key with message inserts, plus suitable wrappers.   
	   *
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
