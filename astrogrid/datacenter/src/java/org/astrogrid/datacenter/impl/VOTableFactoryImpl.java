/*
 * @(#)DatasetAgent.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.impl;

import org.apache.log4j.Logger;

import org.astrogrid.datacenter.i18n.*;
import org.astrogrid.datacenter.job.Query;
import org.astrogrid.datacenter.myspace.Allocation;
import org.astrogrid.datacenter.votable.VOTable;
import org.astrogrid.datacenter.votable.VOTableException;
import org.astrogrid.datacenter.votable.VOTableFactory;

import java.io.PrintStream;
import org.objectwiz.votable.*;

public class VOTableFactoryImpl implements VOTableFactory {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( VOTableFactoryImpl.class ) ;
		
    //
    // JBL Note: For the moment do not know where these settings should be coming from...
    //
    private static final String
	    doubleOutputPattern = "", 
	    floatOutputPattern = "",
	    dateOutputPattern = "",
		timeOutputPattern = "", 
		timestampOutputPattern = "" ;
		
	private static final String
		ASTROGRIDERROR_VOTABLE_CONVERSION_EXCEPTION = "AGDTCE00130" ;
		
			
    public VOTable createVOTable(Query query) throws VOTableException {    	
       return new VOTable( query ) ;
    }


    public void stream( Query query, Allocation allocation ) throws VOTableException { 
		if( TRACE_ENABLED ) logger.debug( "stream(): entry") ; 
		  
		ResultSetConverter 
		    converter = new ResultSetToSimpleVOTable( doubleOutputPattern
		                                            , floatOutputPattern
		                                            , dateOutputPattern
		                                            , timeOutputPattern
		                                            , timestampOutputPattern ) ;   
		PrintStream
		   out = null ;                        
		
		// JBL Note: When I can get rid of this, I'll be happy...     
		QueryFactoryImpl
		    queryFactoryImpl = (QueryFactoryImpl)query.getFactory().getImplementation() ;
		    
		try{
			out = new PrintStream( allocation.getOutputStream() ); 
			converter.serialize( queryFactoryImpl.getResultSet(), out ) ;        
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_VOTABLE_CONVERSION_EXCEPTION ) ;
			logger.error( message.toString(), ex ) ;
			throw new VOTableException( message, ex );			
		}
		finally {
			if( out != null ) {  
			    try {
				   out.flush();
				   Allocation.getFactory().close( allocation );
				   out.close();                                       
			    }
			    catch( Exception ex ) {
			       // JBL Note: a log entry might be appropriate here...	
				   ;
			    }
			}
			out = null ;
			if( TRACE_ENABLED ) logger.debug( "stream(): exit") ; 
		}
    
    } // end of stream()


} // end of class VOTableFactoryImpl
 