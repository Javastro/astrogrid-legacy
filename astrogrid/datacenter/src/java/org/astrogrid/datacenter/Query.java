/*
 * @(#)Query.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.datacenter;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;
import org.w3c.dom.* ;

public class Query {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Query.class ) ;
		
    private static final String
	    ASTROGRIDERROR_COULD_NOT_CREATE_QUERYFACTORY_IMPL = "AGDTCE00050", 
	    ASTROGRIDERROR_COULD_NOT_dosomething = "AGDTCE00???" ;
        
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
						   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				element = (Element) nodeList.item(i) ;
				
				if( element.getTagName().equals( JobDocDescriptor.CRITERIA_ELEMENT ) ) {
					setCriteria(new Criteria( element )) ;
				}
				else if( element.getTagName().equals( JobDocDescriptor.RETURN_ELEMENT ) ) {
				    setReturn(new Return( element )) ;
				}
				else if( element.getTagName().equals( JobDocDescriptor.FROM_ELEMENT ) ) {
					setFrom(new From( element )) ;
				}
				else {
					; // JBL Note: What do I do here?
				}
				
			} // end for		
			
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_dosomething ) ;
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
    	
    	StringBuffer
    	   buffer = new StringBuffer(256) ;
    	   
    	buffer
    	    .append( "SELECT " )
    	    .append( getReturn().toSQLString() )
    	    .append( " FROM ")
    	    .append( getFrom().toSQLString() )
    	    .append( " WHERE ")
    	    .append( getCriteria().toSQLString() );
		     logger.debug("SQL Query: "+ buffer.toString()); 
    	     	   
    	return buffer.toString().toUpperCase() ;
    	
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
