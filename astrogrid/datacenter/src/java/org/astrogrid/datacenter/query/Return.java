/*
 * @(#)Return.java   1.0
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
import org.w3c.dom.* ;

import java.util.ArrayList ;
import java.util.List ;
import java.util.Iterator ;

/**
 * The <code>Return</code> class represents... 
 * <p>
 * Some introductory text.... For example:
 * <p><blockquote><pre>
 *   
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Return {

	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Return.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_RETURN_FROM_ELEMENT = "AGDTCE00240",
	    ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_RETURN = "AGDTCE00250", 
		ASTROGRIDERROR_UNABLE_TO_MAP_CATALOG_UCD_TO_COLUMN_HEADING = "AGDTCE00250";
		
	private static final String	
		SQL_DELETE_LOWER = "delete",
		SQL_DELETE = "DELETE",
		SQL_UPDATE_LOWER = "update",
		SQL_UPDATE = "UPDATE",
		SQL_INSERT_LOWER = "insert",
		SQL_INSERT = "INSERT" ;
		
    private List
        fields = new ArrayList() ;
        
    private Catalog
        catalog ;
        
	private Operation 
		 operation ;        
        
    public Return( Element returnElement, Catalog catalog ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Return(Element): entry") ;
		   		
		try {

			NodeList
			   nodeList = returnElement.getElementsByTagName( RunJobRequestDD.FIELD_ELEMENT ) ;		   
			   
			Element
				fieldElement ;
				
			this.catalog = catalog;
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				fieldElement = (Element) nodeList.item(i) ;
				if( fieldElement.getTagName().equals( RunJobRequestDD.FIELD_ELEMENT ) ) {
					fields.add( new Field( fieldElement, catalog ) ) ;
				} // end of if
				else  {
					; // JBL Note: What do I do here?
				}
				
			} // end for
	
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_RETURN_FROM_ELEMENT ) ;
			logger.error( message.toString(), ex ) ;
			throw new QueryException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Return(Element): exit") ;   	
		}   	
    }  // end of Return(Element)
     

/**
 * Returns the Return part of the SQL query
 * 
 * @return String
 */	
	public String toSQLString() {
		
        StringBuffer
		    buffer = new StringBuffer(64) ;  	
		
		try {
			
		    Field 
		        field = null ;
		    buffer.append(" ");
		    
            Iterator
                iterator = this.getFields() ;
		    
		    while ( iterator.hasNext() ){
		    	
		    	field = (Field)iterator.next() ;

				if ( field.getType().equals(RunJobRequestDD.FIELD_TYPE_PASSTHROUGH ) ) {
					buffer
					    .append( " " )
				        .append( nameParse( field.getName() ) )
				        .append( " " ) ;
				}
				else { 
		    	
		    	    if ( field.getType().equals(RunJobRequestDD.FIELD_TYPE_UCD)) {
					    buffer.append(getColumnHeading( catalog, field.getName()) );
		    	    }
		    	    else if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_COLUMN)) {
		    		    buffer.append(getColumnHeading( catalog, field.getName()) );
		    	    }
		    	
		            buffer.append(", ");	
				 } // end of else		  
		    } // end of while
		
		    buffer.delete(buffer.length()-2,buffer.length());
		}
		
		catch( Exception ex) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_RETURN ) ;
			logger.error( message.toString(), ex ) ;   		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Return(Element): exit") ;   	
		} 
		
		return buffer.toString() ;
    	
	} // end of toSQLString()

	
/**
 * Returns a <code>String</code> value of the column heading for a specific catalog, table
 * and UCD (unified content descriptor) combination. The mapping is detailed in the 
 * <code>ASROGRID_datasetconfig.properties</code> file. The format of the mapping key is:
 * <catalog>.<table>.<UCD> 
 *
 * @see             org.astrogrid.datacenter.ASROGRID_datasetconfig.properties
 * @param catalog	catalogue that the query will run against
 * @param ucd		Universal Column Descriptor
 * @return			<code>String</code> columnHeading 
 */
	private String getColumnHeading( Catalog catalog, String UCD ) {
		if( TRACE_ENABLED ) logger.debug( "getColumnHeading(): entry") ;
		String 
			columnHeading = "" ;
		StringBuffer
			buffer = new StringBuffer(64) ; 
		try { 
			 
            // If no tables assosciated with catalog assume table name same as catalog... 
			if ( catalog.getNumberTables() <= 0 ) {  
				buffer
				    .append( catalog.getName() )
				    .append( "." )
				    .append( catalog.getName() )
				    .append( "." )
				    .append( UCD ) ;
				logger.debug("Return: getColumnHeading(): key: "+buffer.toString().toUpperCase() );				
				columnHeading = DatasetAgent.getProperty( buffer.toString().toUpperCase() ) ;					
			}
			else {
				
				Iterator
				   iterator = catalog.getTables() ;
				Table
				   table = null ;
			
			    while( iterator.hasNext() ) { 
			       table = (Table)iterator.next() ;
				   buffer
				       .append( catalog.getName() )
				       .append( "." )
				       .append( table.getName().toUpperCase() )
				       .append( "." )
				       .append( UCD );	
				   logger.debug("Return: getColumnHeading(): key: "+buffer.toString().toLowerCase().toUpperCase() );
				   columnHeading = DatasetAgent.getProperty( buffer.toString().toUpperCase() ) ;
				   if (columnHeading.length() > 0 ) // break as soon as column heading found
				      break; 
				   buffer.delete( 0,buffer.length() ) ;
			    } // end of while
			
			} // end else
			
		} 
		catch (Exception ex) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_MAP_CATALOG_UCD_TO_COLUMN_HEADING ) ;
			logger.error( message.toString(), ex ) ;
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "getColumnHeading(): exit") ;
		}
		return (columnHeading=="")?UCD:columnHeading;
	} // end of getColumnHeading()
	
	/**
	 * Having the field type PASSTHROUGH opens up the Datacenter to the possibility of a 
	 * user entering valid, but malicious, SQL queries including DELETE, UPDATE and INSERT 
	 * statements. Although the query builder should prevent this happening nameParse() 
	 * is included as a cursory check against this happening. If name contains DELETE, UPDATE,
	 * INSERT an empty string is returned.
	 * 
	 * @param name
	 * @return returnString
	 */	
	private String nameParse(String name) {
		
		String returnString = name ;
		
		if  ( ( name.indexOf( SQL_DELETE ) != -1 ) || ( name.indexOf( SQL_DELETE_LOWER) != -1 ) || 
			( name.indexOf( SQL_INSERT ) != -1 ) || ( name.indexOf( SQL_INSERT_LOWER ) != -1 ) ||
			( name.indexOf( SQL_UPDATE ) != -1 ) || ( name.indexOf( SQL_UPDATE_LOWER ) != -1) ) {
			
			logger.debug( "Attempt to enter malicious SQL using type PASSTHROUGH in Field" ) ;
			returnString = "" ;
		}
		 
		return returnString ;
	} //end of nameParse	
	
	public Iterator getFields() { return this.fields.iterator() ; }
	public boolean addField( Field field ) { return fields.add( field ); }
	public Field removeField( int index ) { return (Field)fields.remove( index ) ; }
	public Field getField( int index ) { return (Field)this.fields.get( index ) ; }

} // end of Class
