/*
 * @(#)VOTable.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.votable;

import org.apache.log4j.Logger;
import org.astrogrid.Configurator ;
import org.astrogrid.datacenter.DTC;
import org.astrogrid.datacenter.myspace.Allocation;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.i18n.AstroGridMessage;


public class VOTable {
	
	private static final boolean 
		TRACE_ENABLED = true ;
        
    private static final String
        SUBCOMPONENT_NAME = Configurator.getClassName( VOTable.class ) ;  
	
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
			implementationFactoryName = DTC.getProperty( DTC.VOTABLE_FACTORY
                                                       , DTC.VOTABLE_CATEGORY ) ;
    	
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
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_VOTABLEFACTORY_IMPL
                                              , SUBCOMPONENT_NAME
                                              , implementationFactoryName ) ;
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
