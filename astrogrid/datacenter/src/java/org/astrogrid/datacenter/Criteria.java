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
	   
	// Templates for the SQL queries   
	public static final String
		OP_NAME_AVERAGE_TEMPLATE 	= "( AVE({0}) )",
		OP_NAME_CONE_TEMPLATE 		= "(DEGREES(ACOS(SIN(RADIANS(DEC)) * SIN(RADIANS( {0} )) + COS(RADIANS(DEC)) * COS(RADIANS( {0})) " +
										"COS(RADIANS( {1} ))) < {2} )))" ,
		OP_NAME_DIFFERENCE 			= " DIF" ,
		OP_NAME_EQUALS_TEMPLATE 	= "( {0} = {1} )" ,
		OP_NAME_GT_TEMPLATE 		= "( {0} > {1} )" ,
		OP_NAME_GTE_TEMPLATE 		= "( {0} >= {1} )" ,
		OP_NAME_IN_TEMPLATE 		= " IN " ,
		OP_NAME_LT_TEMPLATE 		= "( {0} < {1} )" ,
		OP_NAME_LTE_TEMPLATE 		= "( {0} <= {1} )" ,
		OP_NAME_NOT_TEMPLATE 		= " NOT " ,
		OP_NAME_OR_TEMPLATE			= " OR ";
	   
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
	
/**
 * Returns the criteria part of the sql string from the Criteria object
 * 
 * @return String buffer.toString()
 */	   
	public String toSQLString() {
    	
		StringBuffer
		    buffer = new StringBuffer(64) ;		    
		Operation 
		    operation = this.getOperation();		    
		Operation[] 
		    subserviantOperations = operation.getOperations();
		    
		if (subserviantOperations.length <= 1) {
			// TODO - single operation
			logger.debug("single op");
		}		
		    
		
			
		for (int i=0; i < subserviantOperations.length; i++ ) {			
			Operation subserviantOperation = subserviantOperations[i];
			String subOpName = subserviantOperation.getName();
				
			if ( subOpName.equals( JobDocDescriptor.OP_NAME_AVERAGE )) {
				;
			}
			else if ( subOpName.equals( JobDocDescriptor.OP_NAME_CONE)) {
				Object[] inserts = new Object[3];
				Field fields[] = subserviantOperation.getFields();
				for (int k = 0; k < fields.length; k++) {
					if (fields[k].getName().equals("RA"))
					    inserts[0] = fields[k].getValue();
					if (fields[k].getName().equals("DEC"))
						inserts[1] = fields[k].getValue();
					if (fields[k].getName().equals("RADIUS"))
						inserts[2] = fields[k].getValue();
				}
				buffer.append(MessageFormat.format( OP_NAME_CONE_TEMPLATE, inserts ));
			}
			else if ( subOpName.equals( JobDocDescriptor.OP_NAME_DIFFERENCE)) {
				;
			}
			else if ( subOpName.equals( JobDocDescriptor.OP_NAME_EQUALS)) {
				Object[] inserts = new Object[2];
				Field fields[] = subserviantOperation.getFields();
				inserts[0] = fields[0].getName();
				inserts[1] = fields[1].getValue();
				buffer.append(MessageFormat.format( OP_NAME_EQUALS_TEMPLATE, inserts )); 
			}
			else if ( subOpName.equals( JobDocDescriptor.OP_NAME_GT)) {
				Object[] inserts = new Object[2];
				Field fields[] = subserviantOperation.getFields();
				inserts[0] = fields[0].getName();
				inserts[1] = fields[1].getValue();
				buffer.append(MessageFormat.format( OP_NAME_GT_TEMPLATE, inserts ));
			}
			else if ( subOpName.equals( JobDocDescriptor.OP_NAME_GTE)) {
				Object[] inserts = new Object[2];
				Field fields[] = subserviantOperation.getFields();
				inserts[0] = fields[0].getName();
				inserts[1] = fields[1].getValue();
				buffer.append(MessageFormat.format( OP_NAME_GTE_TEMPLATE, inserts ));
			}
			else if ( subOpName.equals( JobDocDescriptor.OP_NAME_IN)) {
				Object[] inserts = new Object[2];
				Field fields[] = subserviantOperation.getFields();
				inserts[0] = fields[0].getName();
				inserts[1] = fields[1].getName();
				buffer.append(MessageFormat.format( OP_NAME_IN_TEMPLATE, inserts ));
			}
			else if ( subOpName.equals( JobDocDescriptor.OP_NAME_LT)) {
				Object[] inserts = new Object[2];
				Field fields[] = subserviantOperation.getFields();
				inserts[0] = fields[0].getName();
				inserts[1] = fields[1].getValue();
				buffer.append(MessageFormat.format( OP_NAME_LT_TEMPLATE, inserts ));
			}
			else if ( subOpName.equals( JobDocDescriptor.OP_NAME_LTE)) {
				Object[] inserts = new Object[2];
				Field fields[] = subserviantOperation.getFields();
				inserts[0] = fields[0].getName();
				inserts[1] = fields[1].getValue();
				buffer.append(MessageFormat.format( OP_NAME_LTE_TEMPLATE, inserts ));
			}
			else if ( subOpName.equals( JobDocDescriptor.OP_NAME_NOT)) {
				;
			}
			else if ( subOpName.equals( JobDocDescriptor.OP_NAME_OR)) {
				;
			}
			else {
				//TODO handle unsupported queries
				logger.debug("Unsupported query");
			}
			
			buffer.append(" AND ");
			
		} // end of if
			
    	 buffer.delete(buffer.length()-4, buffer.length());
    	   
		return buffer.toString() ;
    	
	} // end of toSQLString()


	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public Operation getOperation() {
		return operation;
	}	
		
} // end of class Criteria 
