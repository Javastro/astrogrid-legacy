/*
 * @(#)Allocation.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */
package org.astrogrid.datacenter;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;
import java.io.OutputStream;


/**
 * The <code>Allocation</code> class represents 
 * <p>
 *
 * @author  Jeff Lusted
 * @version 1.0 27-May-2003
 * @since   AstroGrid 1.2
 */
public class Allocation {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Allocation.class ) ;
		
	private static String
		ASTROGRIDERROR_COULD_NOT_CREATE_MYSPACEFACTORY_IMPL = "AGDTCE00090" ;
        
	private static String
		MYSPACEFACTORY_KEY = "MYSPACEFACTORY_KEY" ; 
	
	public static MySpaceFactory
	    factory ;
	    
	private OutputStream
	    outputStream = null ;
	
	public static MySpaceFactory getFactory() throws AllocationException { 
		if( TRACE_ENABLED ) logger.debug( "getFactory(): entry") ;   	
    	
		String
			implementationFactoryName = DatasetAgent.getProperty( MYSPACEFACTORY_KEY ) ;
    	
		try{
			// Note the double lock strategy				
			if( factory == null ){
				synchronized ( Allocation.class ) {
					if( factory == null ){
						Object
						   obj = Class.forName( implementationFactoryName ).newInstance() ;			    			
						factory = (MySpaceFactory)obj ;
					}
				} // end synchronized
			}
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_MYSPACEFACTORY_IMPL, implementationFactoryName ) ;
			logger.error( message.toString(), ex ) ;
			throw new AllocationException( message, ex );
		}
		finally{
			if( TRACE_ENABLED ) logger.debug( "getFactory(): exit") ; 	
		}    
					
		return factory; 
	
	} // end of getFactory()
		
	
    public Allocation() {
    	
    }
  
    
    public Allocation( OutputStream outputStream ) {
		if( TRACE_ENABLED ) logger.debug( "Allocation(): entry") ; 	
        this.outputStream = outputStream ;	
		if( TRACE_ENABLED ) logger.debug( "Allocation(): exit") ; 	
    }
    
    
    public OutputStream getOutputStream() {
    	return outputStream ;
    }
   
   
    public void close () {
		if( TRACE_ENABLED ) logger.debug( "close(): entry") ; 	
		
    	if( outputStream != null ) {
    	    try{ 
    	    	outputStream.close(); 
    	    } 
    	    catch( java.io.IOException ex ) {;} 
    	    finally{ 
    	    	outputStream = null ; 
				if( TRACE_ENABLED ) logger.debug( "close(): exit") ; 	
    	    } 
    	}  
    	
    }// end of close()
    
    
    public void informMySpace() throws AllocationException {
    }


}// end of class Allocation
