/*
 * @(#)Operation.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.datacenter;

import org.apache.log4j.Logger;
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
        name ;
		
	private Field []
	    fields ;
	    
	private Operation []
	    subservientOperations ;
	    
	public Operation( Element operationElement ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Operation(Element): entry") ;  
		 		
		try {
			
			setName(operationElement.getAttribute( JobDocDescriptor.OP_NAME_ATTR )) ;
			
			NodeList
			   nodeList = operationElement.getElementsByTagName( JobDocDescriptor.OP_ELEMENT ) ;

			   
			Element
				opElement,
				fieldElement ;
				
		    subservientOperations = new Operation[ nodeList.getLength() ];

			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				opElement = (Element) nodeList.item(i) ;				
				if( opElement.getTagName().equals( JobDocDescriptor.OP_ELEMENT ) ) {
					subservientOperations[i] = new Operation( opElement ) ;
				}
				else  {
					; // JBL Note: What do I do here?
				}
				
			} // end for		
			
			nodeList = operationElement.getElementsByTagName( JobDocDescriptor.FIELD_ELEMENT ) ;
			fields = new Field[ nodeList.getLength() ];
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				fieldElement = (Element) nodeList.item(i) ;
				if( fieldElement.getTagName().equals( JobDocDescriptor.FIELD_ELEMENT ) ) {
					fields[i] = new Field( fieldElement ) ;						
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
	

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public Field[] getFields() {
		return fields;
	}

	public void setOperations(Operation[] operations) {
		this.subservientOperations = operations ;
	}

	public Operation[] getOperations() {
		return subservientOperations ;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
} // end of class Operation
