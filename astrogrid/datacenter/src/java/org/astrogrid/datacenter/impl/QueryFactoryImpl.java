
package org.astrogrid.datacenter.impl;

import org.astrogrid.datacenter.QueryFactory;
import org.astrogrid.datacenter.Query;
import org.astrogrid.datacenter.QueryException;
import org.astrogrid.datacenter.DatasetAgent ;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;

// import javax.xml.parsers.*;
import org.w3c.dom.*;

import javax.sql.DataSource ;
import javax.naming.*; 
import java.sql.Connection ;
import java.sql.Statement ;
import java.sql.ResultSet ;
import java.sql.SQLException ;
import java.util.Hashtable ;


public class QueryFactoryImpl implements QueryFactory {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( QueryFactoryImpl.class ) ;
	
	private static InitialContext 
	    initialContext = null ;
	    
	private static final String
	    JNDI_DATASOURCE_LOCATION = "java:comp/env/my-datasource" ;

//	private static DataSource
//	    datasource = null ;
	private static Hashtable 
	    datasources = null ;
	    
	private static final String
	    ASTROGRIDERROR_COULD_NOT_CREATE_DATASOURCE = "AGDTCE00060",
	    ASTROGRIDERROR_COULD_NOT_CREATE_CONNECTION = "AGDTCE00070",
	    ASTROGRIDERROR_QUERY_EXECUTION_FAILED      = "AGDTCE00080" ;
	    	    
	private Connection
	    connection = null ;
	private Statement 
	    statement = null ;  
	private ResultSet 
	    resultSet = null ;
	
	private static Hashtable getDataSources() {
		if( TRACE_ENABLED ) logger.debug( "getDataSources(): entry") ; 
		
		try {
			// Note the double lock strategy			
		    if( datasources == null ){
			   synchronized ( QueryFactoryImpl.class ) {
				   if( datasources == null ){
					   datasources = new Hashtable( 10 ) ;
				   }
			   } // end synchronized
		    }
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "getDataSources(): exit") ; 
		}
		
		return datasources ;
		
	} // end of getDataSources()
	
	
	private static DataSource getDataSource( String catalogName ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "getDataSource(): entry") ; 
		
		DataSource
		    retValue = null ;
		String
			datasourceName = null ;
					
		try{
			
			retValue = (DataSource) getDataSources().get( catalogName ) ;
			
			if( retValue == null ) {
				datasourceName = getDataSourceName( catalogName );
				retValue = (DataSource)getInitialContext().lookup( datasourceName ) ;	
				getDataSources().put( datasourceName, retValue ) ;
			}
			
		}
		catch( NamingException ne ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_DATASOURCE
				                     , (datasourceName != null ? datasourceName : "") 
                                     , (catalogName != null ? catalogName : "") ) ;
				                     
			logger.error( message.toString(), ne ) ;
			throw new QueryException( message, ne );
		}
		finally{
			if( TRACE_ENABLED ) logger.debug( "getDataSource(): exit") ; 	
		}
		
		return retValue ;	
			
	} // end of getDataSource()
	
	
	public QueryFactoryImpl () {
		if( TRACE_ENABLED ) logger.debug( "QueryFactoryImpl(): entry") ; 
	}
	
	private Connection getConnection( String catalogName ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "getConnection(): entry") ; 
		
		try{
			if( connection == null ) {
		    	connection = getDataSource( catalogName ).getConnection() ;
			}
		}
		catch( SQLException e) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_CONNECTION ) ;
			logger.error( message.toString(), e ) ;
			throw new QueryException( message, e );
		}
		finally{
			if( TRACE_ENABLED ) logger.debug( "getConnection(): exit") ; 		
		}
		    
		return connection ;  

	} // end of getConnection()
	
	
    public Query createQuery( Element queryElement ) throws QueryException { 
       return new Query( queryElement, this ) ;
    }


    public void execute( Query query ) throws QueryException { 
		if( TRACE_ENABLED ) logger.debug( "execute(): entry") ;  
		   	
		try {
		    String
		       selectString = query.toSQLString(),
		       catalogName = query.getFrom().getCatalogs()[0].getName() ;
		    statement = getConnection( catalogName ).createStatement() ;
		    resultSet = statement.executeQuery( selectString );
		} catch (SQLException e) {
			Message
				message = new Message( ASTROGRIDERROR_QUERY_EXECUTION_FAILED ) ;
			logger.error( message.toString(), e ) ;
			throw new QueryException( message, e );
		} finally {
		    if( TRACE_ENABLED ) logger.debug( "execute(): exit") ; 				  
		}

    } // end of execute()
    
   
    public void end() {
		if( TRACE_ENABLED ) logger.debug( "end(): entry") ; 		
		
		if( resultSet != null ) {
		   try { resultSet.close(); } catch (SQLException e) { ; }
		   resultSet = null;
		}
		if( statement != null ) {
		   try { statement.close(); } catch (SQLException e) { ; }
		   statement = null;
		}
		if( connection != null ) {
		  try { connection.close(); } catch (SQLException e) { ; }
		  connection = null;
		}	
		
		if( TRACE_ENABLED ) logger.debug( "end(): exit") ; 				   		   
    } // end of end() !
    
    
	public Object getImplementation() {
		return this ;
	}
    
    
    public ResultSet getResultSet() {
    	return resultSet ;
    }
    

	private static InitialContext getInitialContext() throws NamingException {
		if( TRACE_ENABLED ) logger.debug( "getInitialContext(): entry") ;  	
		
		try {
			if( initialContext == null ) {
			
				synchronized( QueryFactoryImpl.class ) {
				
					if( initialContext == null ) {
						initialContext = new InitialContext() ;
					}
			    
				}// end synchronized
			
			} 		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "getInitialContext(): exit") ;  				
		}
		
		return initialContext ;
		
	} // end of getInitialContext()
	
	
	private static String getDataSourceName ( String catalogName ) {
		if( TRACE_ENABLED ) logger.debug( "getDataSourceName(): entry") ;  	
			
		String
		    datasourceName = DatasetAgent.getProperty( catalogName.toUpperCase() + ".DATASOURCE" ) ;
		    
		if( datasourceName == null ) { 
			datasourceName = DatasetAgent.getProperty( ".DATASOURCE" ) ;
		}
		
		if( TRACE_ENABLED ) logger.debug( "getDataSourceName(): exit") ;  		
		return datasourceName ;
		
	}// end of getDataSourceName()
  
       
} // end of class QueryFactoryImpl


