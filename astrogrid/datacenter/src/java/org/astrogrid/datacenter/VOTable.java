package org.astrogrid.datacenter;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;


public class VOTable {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( VOTable.class ) ;
		
	private static String
	    ASTROGRIDERROR_COULD_NOT_CREATE_VOTABLEFACTORY_IMPL = "AGDTCE00120" ;
        
    private static String
        VOTABLEFACTORY_KEY = "VOTABLEFACTORY" ; 
        
	private static VOTableFactory
		factory ;
	
	private Query
	    query = null ;
	
	
    public VOTable(Query query) {
    	this.query = query ;
    }


	public static VOTableFactory getFactory() throws VOTableException { 
		if( TRACE_ENABLED ) logger.debug( "getFactory(): entry") ;   	
    	
		String
			implementationFactoryName = DatasetAgent.getProperty( VOTABLEFACTORY_KEY ) ;
    	
		try{
			// Note the double lock strategy				
			if( factory == null ){
				synchronized ( VOTable.class ) {
					if( factory == null ){
						Object
						   obj = Class.forName( implementationFactoryName ).newInstance() ;			    			
						factory = (VOTableFactory)obj ;
					}
				} // end synchronized
			}
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_VOTABLEFACTORY_IMPL, implementationFactoryName ) ;
			logger.error( message.toString(), ex ) ;
			throw new VOTableException( message, ex );
		}
		finally{
			if( TRACE_ENABLED ) logger.debug( "getFactory(): exit") ; 	
		}    	
		
		return factory ;
    	
	} // end of getFactory()


    public void stream(Allocation allocation) throws VOTableException  {
    	factory.stream( query, allocation ) ;
    }


} // end of class VOTable
