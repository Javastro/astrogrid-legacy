/*
 * @(#)Return.java	1.0 
 *
 * AstroGrid Copyright notice.
 * 
 */
package org.astrogrid.datacenter;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;
import org.w3c.dom.* ;

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
	    ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_RETURN = "AGDTCE00250" ;
		
    private Field []
        fields ;
        
    public Return( Element returnElement ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Return(Element): entry") ;
		   		
		try {

			NodeList
			   nodeList = returnElement.getElementsByTagName( JobDocDescriptor.FIELD_ELEMENT ) ;
			   
			Element
				fieldElement ;
				
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
		        field[] = this.fields;
		    buffer.append(" ");
		
		    for (int i = 0; i < field.length; i++){
		        buffer.append(field[i].getName());
		        buffer.append(", ");			  
		    } // end of for
		
		    buffer.deleteCharAt(buffer.lastIndexOf(","));
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
	

} // end of Class
