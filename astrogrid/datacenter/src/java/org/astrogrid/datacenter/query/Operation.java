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

import java.util.ArrayList ;
import java.util.List ;
import java.util.Iterator ;


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
public class Operation {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Operation.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_dosomething = "AGDTCE00???" ;	
		
    private String
        name = null ;
		
	private List
	    fields = new ArrayList() ;
	    
	private List
	    subservientOperations = new ArrayList() ;
	    
	private Catalog
	    catalog;    
	    
	public Operation( Element operationElement , Catalog catalog) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Operation(Element): entry") ;  
		 		
		try {
			
			setName(operationElement.getAttribute( RunJobRequestDD.OP_NAME_ATTR )) ;
			
			NodeList
			   nodeList = operationElement.getElementsByTagName( RunJobRequestDD.OP_ELEMENT ) ;

			   
			Element
				opElement,
				fieldElement ;
				
			this.catalog = catalog;				
				
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				opElement = (Element) nodeList.item(i) ;				
				if( opElement.getTagName().equals( RunJobRequestDD.OP_ELEMENT ) ) {
					subservientOperations.add ( new Operation( opElement , catalog) ) ;
				}
				else  {
					; // JBL Note: What do I do here?
				}
				
			} // end for		
			
			nodeList = operationElement.getElementsByTagName( RunJobRequestDD.FIELD_ELEMENT ) ;
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				fieldElement = (Element) nodeList.item(i) ;
				if( fieldElement.getTagName().equals( RunJobRequestDD.FIELD_ELEMENT ) ) {
					fields.add( new Field( fieldElement, catalog ) ) ;						
				}
				else  {
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
			if( TRACE_ENABLED ) logger.debug( "Operation(Element): exit") ;   	
		}
		
	} // end of Operation( Element )
	

	public Iterator getFields() { return this.fields.iterator() ; }
	public boolean addField( Field field ) { return this.fields.add( field ); }
	public Field removeField( int index ) { return (Field)this.fields.remove( index ) ; }
	public Field getField( int index ) { return (Field)this.fields.get( index ) ; }
	public int getNumberFields() { return this.fields.size() ; }

	public Iterator getOperations() { return this.subservientOperations.iterator() ; }
	public boolean addOperation( Operation operation ) { return this.subservientOperations.add( operation ); }
	public Operation removeOperation( int index ) { return (Operation)this.subservientOperations.remove( index ) ; }
	public Operation getOperation( int index ) { return (Operation)this.subservientOperations.get( index ) ; }
	public int getNumberOperations() { return this.subservientOperations.size() ; }

	public void setName(String name) { this.name = name; }
	public String getName() { return name; }
	
	public Catalog getCatalog() { return catalog; }
	
} // end of class Operation
