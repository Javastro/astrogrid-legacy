/*
 * @(#)Component.java   1.0
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
import java.io.IOException ;
import java.io.InputStream ;
import java.io.BufferedInputStream ;
import org.apache.log4j.Logger;
import org.astrogrid.i18n.AstroGridMessage ;


/**
 * The <code>TemplateManager</code> class represents
 *
 * @author  Jeff Lusted
 * @version 1.0 08-Jul-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.2
 */
public class TemplateManager {
	
	/** Compile-time switch used to turn tracing on/off. 
	  * Set this to false to eliminate all trace statements within the byte code.*/	 
	private static final boolean 
		TRACE_ENABLED = true ;
			
    private static TemplateManager
        singletonTemplateManager = new TemplateManager() ;
	
	/** Log4J logger for this class. */    			    			   			
	private static Logger 
		logger = Logger.getLogger( TemplateManager.class ) ;
	
	private static String	
	    ASTROGRIDERROR_UNABLE_TO_LOCATE_TEMPLATE = "" ;

    private Hashtable
        templates = new Hashtable() ;
        
        
	public static TemplateManager getInstance(){ return singletonTemplateManager; }
	
	      
    private TemplateManager() {}
    
    
    public String getTemplate( String name ) {
		if( TRACE_ENABLED ) logger.debug( "getTemplate(): entry") ;	
		
		String
		   targetTemplate = null ; 
		BufferedInputStream
			bistream = null ;
		   	
		try {
			
			targetTemplate = (String)templates.get( name ) ;
			
			if( targetTemplate == null ) {
				
				StringBuffer
				    sBuffer = new StringBuffer( 1024 ) ;
				int c ;
				InputStream 
					istream = TemplateManager.class.getClassLoader().getResourceAsStream( name ) ;
		        bistream = new BufferedInputStream( istream, 1024 ) ;
			        
				c = bistream.read() ;
			    while( c != -1 ){
			    	sBuffer.append( c ) ;
					c = bistream.read() ;
			    }
	
				targetTemplate = sBuffer.toString() ;
				templates.put( name, targetTemplate ) ;
					
			} // endif
			
		}
		catch ( IOException ex ) {
			
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_LOCATE_TEMPLATE, name ) ;
			logger.error( message.toString(), ex ) ;
			
		}
		finally {
			if( bistream != null ) try{ bistream.close(); } catch( IOException ioex ){ ; }
			if( TRACE_ENABLED ) logger.debug( "getTemplate(): exit") ;			
		}
		
		return targetTemplate ;
		
    } // end of getTemplate()


} // end of class TemplateManager