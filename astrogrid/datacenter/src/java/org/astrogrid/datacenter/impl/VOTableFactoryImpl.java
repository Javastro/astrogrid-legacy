
package org.astrogrid.datacenter.impl;

import org.astrogrid.datacenter.impl.QueryFactoryImpl ;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;

import org.astrogrid.datacenter.VOTableFactory;
import org.astrogrid.datacenter.VOTable;
import org.astrogrid.datacenter.Query;
import org.astrogrid.datacenter.VOTableException;
import org.astrogrid.datacenter.Allocation;

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
				   ;
			    }
			}
			out = null ;
			if( TRACE_ENABLED ) logger.debug( "stream(): exit") ; 
		}
    
    } // end of stream()


} // end of class VOTableFactoryImpl
 