/*
 * Created on 20-May-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.i18n;

/**
 * Message is a first move towards internationalization.
 * 
 * Message is largely here to prevent our testing code becoming out-of-hand.
 * Use this rather than simple constants in open code.
 * For the moment it uses the key for the message. Later the message text will be loaded
 * using a ResourcBundle. 
 * 
 * 	private static final String
 *	    ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE = "Could not read configuration file" ;
 * 
 *  Message
 *  	message = new Message( ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE, CONFIG_FILENAME ) ;
 *	logger. error( message.toString(), exception ) ;
 * 
 * @author Jeff Lusted
 *
 **/
public class Message {
	
	private String
		key ;
	private String[]
	    inserts ;
	    
	public Message( String key ) {
		this.key = key ;
		this.inserts = null ;
	}
		    
	public Message( String key, String insert1 ) {
		this(key, new String[] {insert1} ) ;
	}

	public Message( String key, String insert1, String insert2 ) {
		this(key, new String[] {insert1,insert2} ) ;
	}
	
	public Message( String key, String insert1, String insert2, String insert3 ) {
		this(key, new String[] {insert1,insert2,insert3} ) ;
	}	
    public Message( String key, String[] inserts ) {
       this.key = key ;
       this.inserts = inserts ;	
    }
    
    
   /**
    *  The implementation of this method is suitable only during testing.
    * 
    *  <p>This method concatinates message key with message inserts, 
	*   plus suitable wrappers.
    * 
	**/
    public String toString() {
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
       	         .append( inserts[i] )
       	         .append( "]  ") ;  
           } // end for
           
       }
       
       return buffer.toString() ;
       
    } // end toString()
    
} // end of class Message
