/*
 * @(#)Operation.java   1.0
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


/**
 * The <code>Operation</code> class represents operations within an 
 * SQL query string.
 * <p>
 * Some example text. For example:
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
public abstract class Operation implements Operand {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Operation.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_OPERATION_FROM_ELEMENT = "AGDTCE00410",
	    ASTROGRIDERROR_UNSUPPORTED_SQL_OPERATION = "AGDTCE00400" ;
		
    public static final String     	
    // JBL Note: I've ignored "AVERAGE", "ANY" and "ALL" for this iteration (AG 1.2). 
    // I'm not sure, in any case, that "AVERAGE" belongs here.
        AND = "AND",
        OR  = "OR",
        NOT = "NOT",
        LESS_THAN = "LESS_THAN",
        GREATER_THAN = "GREATER_THAN",
        DIFFERENCE = "DIFFERENCE",
        CONE = "CONE",
        EQUALS = "EQUALS",
        NOT_EQUALS = "NOT_EQUALS",
        GREATER_THAN_OR_EQUALS = "GREATER_THAN_OR_EQUALS",
        LESS_THAN_OR_EQUALS = "LESS_THAN_OR_EQUALS",
        IN = "IN",
        NOT_NULL = "NOT_NULL",
        BETWEEN = "BETWEEN",
        LIKE = "LIKE" ;
	
	private String
		name = null ;
	    
	private Catalog
		catalog;    
		
		
	public static Operation createOperation( Element opElement , Catalog catalog ) throws QueryException {	
		if( TRACE_ENABLED ) logger.debug( "Operation.createOperation(): entry") ; 
		
		Operation
		    newOp = null ;
		String
		    opName = null ;
		
		try {
			
			opName = opElement.getAttribute( RunJobRequestDD.OP_NAME_ATTR ).trim().toUpperCase() ;
			
			// JBL Note: This is the nearest I've come to designing a virtual constructor,
			// which is - of course - logically impossible. With a little ingenuity I believe
			// it could accommodate most if not all of the possible SQL operations within 
			// a selection statement, including sub-queries. 
			if( opName.equals( Operation.AND) ) {
				newOp = new Operation_AND( opElement, catalog ) ;				
			}
			else if( opName.equals( Operation.OR ) ) {
				newOp = new Operation_OR( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.NOT ) ) {
				newOp = new Operation_LOGICAL_NOT( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.LESS_THAN ) ) {
				newOp = new Operation_LESS_THAN( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.GREATER_THAN ) ) {
				newOp = new Operation_GREATER_THAN( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.DIFFERENCE ) ) {
				newOp = new Operation_DIFFERENCE( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.CONE ) ) {
				newOp = new Operation_CONE( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.EQUALS ) ) {
				newOp = new Operation_EQUALS( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.GREATER_THAN_OR_EQUALS ) ) {
				newOp = new Operation_GREATER_THAN_OR_EQUALS( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.LESS_THAN_OR_EQUALS ) ) {
				newOp = new Operation_LESS_THAN_OR_EQUALS( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.IN ) ) {
				newOp = new Operation_IN( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.NOT_EQUALS ) ) {
				newOp = new Operation_NOT_EQUALS( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.NOT_NULL ) ) {
				newOp = new Operation_NOT_NULL( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.BETWEEN ) ) {
				newOp = new Operation_BETWEEN( opElement, catalog ) ;
			}
			else if( opName.equals( Operation.LIKE ) ) {
				newOp = new Operation_LIKE( opElement, catalog ) ;
			}	
			else {
				Message
	               message = new Message( ASTROGRIDERROR_UNSUPPORTED_SQL_OPERATION, opName ) ;
                logger.error( message.toString() ) ;
                throw new QueryException( message );    	
			}
			
			newOp.setName( opName ) ;
			
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Operation.createOperation(): exit") ;   	
		}
		
		return newOp ;
		
	} // end of createOperation()
	
	    
	public Operation( Element operationElement , Catalog catalog ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Operation(Element): entry") ;  
		 		
		try {
			
			this.catalog = catalog;	
			
			NodeList
			   nodeList = operationElement.getChildNodes() ;
			   
			Element
				element ;
								
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {
				
				if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
				    continue ;				
				element = (Element) nodeList.item(i) ;
								
				if( element.getTagName().equals( RunJobRequestDD.OP_ELEMENT ) ) {
					this.push( Operation.createOperation( element , catalog ) ) ;
				}
				else if( element.getTagName().equals( RunJobRequestDD.FIELD_ELEMENT ) ) {
				    this.push( new Field( element, catalog ) ) ;						
			    } 
			    else {
					; // JBL Note: What do I do here?
				}
				
			} // end for		
	
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_OPERATION_FROM_ELEMENT ) ;
			logger.error( message.toString(), ex ) ;
			throw new QueryException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Operation(Element): exit") ;   	
		}
		
	} // end of Operation( Element operationElement , Catalog catalog )


	public void setName(String name) { this.name = name; }
	public String getName() { return name; }
	
	public void setCatalog( Catalog catalog ) { this.catalog = catalog ; }
	public Catalog getCatalog() { return catalog; }
	
	public abstract void push( Operand operand ) ;
	
	
} // end of class Operation
