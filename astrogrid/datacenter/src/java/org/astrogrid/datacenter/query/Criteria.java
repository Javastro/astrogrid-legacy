/*
 * @(#)Criteria.java   1.0
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

import java.text.MessageFormat;

/**
 * The <code>Criteria</code> class represents ...
 * <p>
 * Introductory text.... For example:
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
public class Criteria {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Criteria.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_CRITERIA_ELEMENT = "AGDTCE00220",
		ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_CRITERIA = "AGDTCE00230", 
	    ASTROGRIDERROR_UNABLE_TO_MAP_CATALOG_UCD_TO_COLUMN_HEADING = "AGDTCE00240";		
	
	private Operation 
	   operation ;

	private Catalog 
	    catalog;
	   
	// Templates for the SQL queries   
	public static final String
		OP_NAME_AVERAGE_TEMPLATE 	= "( AVG({0}) )" ,
//		OP_NAME_CONE_TEMPLATE 		= "(DEGREES(ACOS(SIN(RADIANS(DEC)) * SIN(RADIANS( {0} )) + COS(RADIANS(DEC)) * COS(RADIANS( {0})) " +
//										"COS(RADIANS( {1} ))) < {2} )))" ,
        OP_NAME_CONE_TEMPLATE 		= "( ((2 * ASIN(SQRT(POW(SIN({1}-UDEC)/2),2) + COS(UDEC) + POW(SIN({0}-URA)/2),2)) < {2} )" ,										
		OP_NAME_DIFFERENCE 			= " DIF " ,
		OP_NAME_EQUALS_TEMPLATE 	= "( {0} = {1} )" ,
		OP_NAME_GT_TEMPLATE 		= "( {0} > {1} )" ,
		OP_NAME_GTE_TEMPLATE 		= "( {0} >= {1} )" ,
		OP_NAME_IN_TEMPLATE 		= " IN " ,
		OP_NAME_LT_TEMPLATE 		= "( {0} < {1} )" ,
		OP_NAME_LTE_TEMPLATE 		= "( {0} <= {1} )" ,
		OP_NAME_NOT_TEMPLATE 		= " (!{0}) " ,
		OP_NAME_OR_TEMPLATE			= " OR ";
	   
	public Criteria( Element criteriaElement, Catalog catalog ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Criteria(Element): entry") ;   		
		try {

			NodeList
			   nodeList = criteriaElement.getChildNodes() ;
			Element
			    operationElement ;
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {
				if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
					continue ;							
				operationElement = (Element) nodeList.item(i) ;
				if( operationElement.getTagName().equals( RunJobRequestDD.OP_ELEMENT ) ) {
					setOperation(new Operation( operationElement , catalog)) ;
				}
				else  {
                    ; // JBL Note: What do I do here?
				}
				
			} // end for		
			
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_CRITERIA_ELEMENT ) ;
			logger.error( message.toString(), ex ) ;
			throw new QueryException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Criteria(Element): exit") ;   	
		}		   
	} // end of Criteria( Element )

	
/**
 * Returns a <code>String</code> representing the criteria part of the 
 * sql string from the <code>Criteria</code> object.
 * 
 * @return String buffer.toString()
 */	   
	public String toSQLString() {		
		if( TRACE_ENABLED ) logger.debug( "Criteria.toSQLString(): entry") ;
   		
		StringBuffer
		    buffer = new StringBuffer(64) ;		    
		Operation 
		    operation = this.getOperation();
		    
		Object[] 
			inserts ;
			
		List
            opsList = null ;
                 
        Iterator
		    subserviantOperations = operation.getOperations(),
		    fields = null;
		    
		Field
		    field = null ;
		    
		if ( operation.getNumberOperations() <= 1 ) {
			
			opsList = new ArrayList() ;
			opsList.add( operation ) ;
			subserviantOperations = opsList.iterator() ;
			
		} // end of if	
		    
		try {	
				
			while( subserviantOperations.hasNext() ) {		    					
			
		  //  for (int i=0; i < subserviantOperations.length; i++ ) {			
			    Operation 
			        subserviantOperation = (Operation) subserviantOperations.next() ;
			    String 
			        subOpName = subserviantOperation.getName();
			    catalog = subserviantOperation.getCatalog();

				if (subOpName.equals(RunJobRequestDD.OP_NAME_NOT)) {
					buffer.append(" !");
					continue ;
					/*
					Operation[] 
					    tempOperation = subserviantOperation.getOperations();
					subserviantOperations[i] = tempOperation[0];
					subserviantOperation = subserviantOperations[i];
					subOpName = subserviantOperation.getName();		
					*/				
				} //end of not

				
			    if ( subOpName.equals( RunJobRequestDD.OP_NAME_AVERAGE )) {
			    	
					inserts = new Object[1];
					fields = subserviantOperation.getFields();
					field = (Field)fields.next() ;
					if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_UCD)) {
					    inserts[0] = getColumnHeading( catalog,field.getName() ) ;	
					}
					else if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_COLUMN)) {
						inserts[0] = field.getName() ;
					}
					buffer.append(MessageFormat.format( OP_NAME_AVERAGE_TEMPLATE, inserts ));
						
			    } // end of else if AVERAGE
			    
			    
			    else if ( subOpName.equals( RunJobRequestDD.OP_NAME_CONE)) {
			    	
				    inserts = new Object[3] ;
				    fields = subserviantOperation.getFields();
				    while( fields.hasNext() ) {
				    	field = (Field)fields.next() ;
					    if (field.getName().equals("RA"))
					        inserts[0] = field.getValue();
					    if (field.getName().equals("DEC"))
						    inserts[1] = field.getValue();
					    if (field.getName().equals("RADIUS"))
						    inserts[2] = field.getValue();
				    } // end of while
				    buffer.append(MessageFormat.format( OP_NAME_CONE_TEMPLATE, inserts ));
				    
			    } // end of else if CONE
			    
			    
			    else if ( subOpName.equals( RunJobRequestDD.OP_NAME_DIFFERENCE)) {
			    	
					inserts = new Object[1];
					fields = subserviantOperation.getFields();
					field = (Field)fields.next() ;
					if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_UCD)) {
					inserts[0] = getColumnHeading(catalog,field.getName());	
					}
					else if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_COLUMN)) {
						inserts[0] = field.getName();
					}
					buffer.append(MessageFormat.format( OP_NAME_DIFFERENCE, inserts ));
												
			    } // end of else if DIFFERENCE
			    
			    
			    else if ( subOpName.equals( RunJobRequestDD.OP_NAME_EQUALS)) {
			    	
				    inserts = new Object[2];
				    fields = subserviantOperation.getFields();
				    field = (Field)fields.next() ;				    
				    if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_UCD)) {
						inserts[0] = getColumnHeading(catalog,field.getName());	
				    }
				    else if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_COLUMN)) {
						inserts[0] = field.getName();
				    }
				    inserts[1] = ((Field)fields.next()).getValue();
				    buffer.append(MessageFormat.format( OP_NAME_EQUALS_TEMPLATE, inserts )); 
			    } // end of else if EQUALS
			    
			    
			    else if ( subOpName.equals( RunJobRequestDD.OP_NAME_GT)) {
			    	
				    inserts = new Object[2];
				    fields = subserviantOperation.getFields();
				    field = (Field)fields.next() ;
				    inserts[0] = field.getName();
					if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_UCD)) {
					    inserts[0] = getColumnHeading(catalog,field.getName());	
					}
					else if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_COLUMN)) {
						inserts[0] = field.getName();
					}
				    inserts[1] = ((Field)fields.next()).getValue();
				    buffer.append(MessageFormat.format( OP_NAME_GT_TEMPLATE, inserts ));
				    
			    } // end of else if GT
			    
			    
			    else if ( subOpName.equals( RunJobRequestDD.OP_NAME_GTE)) {
				    
				    inserts = new Object[2];
				    fields = subserviantOperation.getFields();
				    field = (Field)fields.next() ;
					if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_UCD)) {
					    inserts[0] = getColumnHeading(catalog,field.getName());	
					}
					else if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_COLUMN)) {
						inserts[0] = field.getName();
					}
				    inserts[1] = ((Field)fields.next()).getValue();
				    buffer.append(MessageFormat.format( OP_NAME_GTE_TEMPLATE, inserts ));
				    
			    } // end of else if GTE
			    
			    
			    else if ( subOpName.equals( RunJobRequestDD.OP_NAME_IN)) {
				    
                    inserts = new Object[2];
				    fields = subserviantOperation.getFields();
				    inserts[0] = ((Field)fields.next()).getName() ;
				    inserts[1] = ((Field)fields.next()).getName() ;
				    buffer.append(MessageFormat.format( OP_NAME_IN_TEMPLATE, inserts ));
				    
			    } // end of else if IN
			    
			    
			    else if ( subOpName.equals( RunJobRequestDD.OP_NAME_LT)) {
			    	
				    inserts = new Object[2];
				    fields = subserviantOperation.getFields();
					field = (Field)fields.next() ;
					
					if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_UCD)) {
					    inserts[0] = getColumnHeading(catalog,field.getName());	
					}
					else if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_COLUMN)) {
						inserts[0] = field.getName();
					}
				    inserts[1] = ((Field)fields.next()).getValue();
				    buffer.append(MessageFormat.format( OP_NAME_LT_TEMPLATE, inserts ));
				    
			    } // end of else if LT
			    
			    
			    else if ( subOpName.equals( RunJobRequestDD.OP_NAME_LTE)) {
				    
				    inserts = new Object[2];
				    fields = subserviantOperation.getFields();
				    field = (Field)fields.next() ;
				    
					if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_UCD)) {
					    inserts[0] = getColumnHeading(catalog,field.getName());	
					}
					else if (field.getType().equals(RunJobRequestDD.FIELD_TYPE_COLUMN)) {
						inserts[0] = field.getName();
					}
				    inserts[1] = ((Field)fields.next()).getValue();
				    buffer.append(MessageFormat.format( OP_NAME_LTE_TEMPLATE, inserts ));
				    
			    } // end of else if LTE
			    
			    
			    else {
			    	
					Message
						message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_CRITERIA, subOpName ) ;
					logger.error( message.toString() ) ;
				    throw new UnsupportedOperationException( message.toString() ) ;
				    
			    } // end of else
			    
			    buffer.append(" ");
			    buffer.append(operation.getName());
			    buffer.append(" ");
			
		    } // end while
		    			
			buffer.delete(buffer.length()-(operation.getName().length() + 2), buffer.length()); // remove trailing operation
		    
   		} // end of try
		finally {
			if( TRACE_ENABLED ) logger.debug( "Criteria.toSQLString(): exit") ;
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
	private String getColumnHeading(Catalog catalog, String UCD) {
		if( TRACE_ENABLED ) logger.debug( "getColumnHeading(): entry") ;
		String 
			columnHeading = "",
			catalogName = "",
			tableName = "";
		StringBuffer
			buffer = new StringBuffer(64) ; 
		try {	

//			if no tables assosciated with catalog assume table name same as catalog... 
			if ( catalog.getNumberTables() <= 0) {  
				buffer
				    .append(catalog.getName())
				    .append(".")
				    .append(catalog.getName())
				    .append(".")
				    .append(UCD);
				logger.debug("Criteria: getColumnHeading(): key: "+buffer.toString().toUpperCase() );
				columnHeading = DatasetAgent.getProperty( buffer.toString().toUpperCase() ) ;					
			} 
			else {
				
				Table
				    table = null ;			    
				Iterator
				    it = catalog.getTables() ;
			
			    while( it.hasNext() ) { 
			    	table = (Table)it.next() ;
				    buffer
				       .append(catalog.getName())
				       .append(".")
				       .append(table.getName().toUpperCase())
				       .append(".")
				       .append(UCD);					
				    logger.debug("Criteria: getColumnHeading(): key: "+buffer.toString().toUpperCase() );
				    columnHeading = DatasetAgent.getProperty( buffer.toString().toUpperCase() ) ;				
				    if (columnHeading.length() > 0) // break as soon as column heading found
				       break; 
				    buffer.delete(0,buffer.length());
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

	public void setOperation(Operation operation) { this.operation = operation; }
	public Operation getOperation() { return operation; }	
		
} // end of class Criteria 
