/*
 * @(#)Criteria.java	1.2 
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.datacenter;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;
import org.w3c.dom.* ;

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
		ASTROGRIDERROR_COULD_NOT_dosomething = "AGDTCE00???" ;
	
	private Operation 
	   operation ;
	   
	public Criteria( Element criteriaElement ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Criteria(Element): entry") ;   		
		try {

			NodeList
			   nodeList = criteriaElement.getChildNodes() ;
			Element
			    operationElement ;
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				operationElement = (Element) nodeList.item(i) ;
				if( operationElement.getTagName().equals( JobDocDescriptor.OP_ELEMENT ) ) {
					setOperation(new Operation( operationElement )) ;
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
			if( TRACE_ENABLED ) logger.debug( "Criteria(Element): exit") ;   	
		}
		   
	} // end of Criteria( Element )
	
	   
	public String toSQLString() {
    	
		StringBuffer
		   buffer = new StringBuffer(64) ;
    	   
		buffer
		  .append( "" )
		  .append( "" );
    	   
		return buffer.toString() ;
    	
	} // end of toSQLString()


	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public Operation getOperation() {
		return operation;
	}	
		
} // end of class Criteria 
