/*
 * @(#)Query.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.query;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.datasetagent.*;
import org.astrogrid.datacenter.i18n.*;
import org.astrogrid.datacenter.myspace.*;
import org.astrogrid.datacenter.votable.*;
import org.w3c.dom.* ;

public class Query {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Query.class ) ;
		
    private static final String
	    ASTROGRIDERROR_COULD_NOT_CREATE_QUERYFACTORY_IMPL = "AGDTCE00050" , 
	    ASTROGRIDERROR_COULD_NOT_CREATE_QUERY_FROM_ELEMENTS = "AGDTCE00055" ;
        
    public static final String
		QUERYFACTORY_KEY_SUFFIX = ".QUERYFACTORY" ;
        
    private QueryFactory
        factory ;
        
    private Criteria 
        criteria ;
        
    private Return
        returnObject ;
        
    private From
        fromObject ;
	
	
    public Query( Element queryElement, QueryFactory factory ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Query(Element,QueryFactory): entry") ;
		
    	this.factory = factory ;
	   		
		try {

			NodeList
			   nodeList = queryElement.getChildNodes() ;
			Element
				element ;
			Catalog
			    catalog = null;
						   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {	
				if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
					continue ;				
				element = (Element) nodeList.item(i) ;
				
				if( element.getTagName().equals( RunJobRequestDD.FROM_ELEMENT ) ) {
					setFrom(new From( element )) ;
					NodeList nodeList2 = element.getChildNodes();
					for( int j=0 ; j < nodeList2.getLength() ; j++ ) {
					    if (nodeList2.item(j).getNodeType() != Node.ELEMENT_NODE) 
						    continue;
						    for (int k= 0 ; k < nodeList2.getLength() ; k++) {
								if (nodeList2.item(k).getNodeType() != Node.ELEMENT_NODE) 
									continue;
								element = (Element) nodeList2.item(k) ;
								if (element.getTagName().equals(RunJobRequestDD.CATALOG_ELEMENT)) 
									catalog = new Catalog( element );
						    } // end of k						 
					} // end of for j
				} // end of if( element.getTagName().equals( RunJobRequestDD.FROM_ELEMENT ) ) {
				else if( element.getTagName().equals( RunJobRequestDD.CRITERIA_ELEMENT ) ) {
					setCriteria(new Criteria( element, catalog )) ;
				}
				else if( element.getTagName().equals( RunJobRequestDD.RETURN_ELEMENT ) ) {
				    setReturn(new Return( element, catalog )) ;
				}
				else {
					; // JBL Note: What do I do here?
				}
				
			} // end for		
			
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_QUERY_FROM_ELEMENTS ) ;
			logger.error( message.toString(), ex ) ;
			throw new QueryException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Query(Element,QueryFactory): exit") ;   	
		}
		
    } // end of Query() 


    public QueryFactory getFactory() {
    	return factory ;
    }


    public static QueryFactory getFactory( String catalogName ) throws QueryException { 
		if( TRACE_ENABLED ) logger.debug( "getFactory(): entry") ;   	
    	
    	QueryFactory 
    		factory ;
    	String
    		implementationFactoryName = DatasetAgent.getProperty( catalogName ) ;
    		
    	// If we couldn't find a specific factory in the properties file,
    	// Then look for a default factory...
    	if( implementationFactoryName == null )
		    implementationFactoryName = DatasetAgent.getProperty( Query.QUERYFACTORY_KEY_SUFFIX ) ;
    		
		try {
			Object
			   obj = Class.forName( implementationFactoryName ).newInstance() ;			    			
			factory = (QueryFactory)obj ;
		}
		catch ( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_QUERYFACTORY_IMPL, implementationFactoryName ) ;
			logger.error( message.toString(), ex ) ;
			throw new QueryException( message, ex );
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "getFactory(): exit") ;			
		}    	
    	return factory; 
    	
    } // end of getFactory()
    
    
    public void execute() throws QueryException  {
		if( TRACE_ENABLED ) logger.debug( "execute(): entry") ;   	
    	factory.execute( this ) ;
		if( TRACE_ENABLED ) logger.debug( "execute(): exit") ;   	
    }
    
    
    public VOTable toVOTable( Allocation allocation ) throws VOTableException {  
    	
    	VOTable
    	   votable = VOTable.getFactory().createVOTable( this ) ; 
    	   
    	votable.stream( allocation ) ;
    		
    	return votable ;
    	
    } // end of toVOTable()
    
    
    public void close() {
    	factory.end() ;
    }
    
    public String toSQLString() {
		if( TRACE_ENABLED ) logger.debug( "Query.toSQLString(): entry") ;
		
    	StringBuffer
    	   buffer = new StringBuffer(256) ;
    	   	   
		try {
			
			buffer
			    .append( "SELECT " )
			    .append( getReturn().toSQLString() )
			    .append( " FROM ")
			    .append( getFrom().toSQLString() )
			    .append( " WHERE ")
			    .append( getCriteria().toSQLString() );
			     logger.debug("SQL Query: "+ buffer.toString()); 
			     
		} catch (RuntimeException e) {
		     ;
		}
		finally {
			logger.debug( buffer.toString() ) ;
			if( TRACE_ENABLED ) logger.debug( "Query.toSQLString(): exit") ;
		}
    	     	   
    	return buffer.toString() ;
    	
    } // end of toSQLString() 


	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setReturn(Return returnObject) {
		this.returnObject = returnObject;
	}

	public Return getReturn() {
		return returnObject;
	}

	public void setFrom(From fromObject) {
		this.fromObject = fromObject;
	}

	public From getFrom() {
		return fromObject;
	}    
    
} // end of class Query
