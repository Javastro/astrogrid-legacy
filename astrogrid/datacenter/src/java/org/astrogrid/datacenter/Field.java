/*
 * @(#)Field.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */
package org.astrogrid.datacenter;

import org.apache.log4j.Logger;
import org.w3c.dom.* ;

/**
 * The <code>Field</code> class represents a field within an SQL Query.
 * <p>
 * Some example text. For example:
 * <p><blockquote><pre>
 *     
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.Query
 * @since   AstroGrid 1.2
 */
public class Field {

	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Field.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_dosomething = "AGDTCE00???" ;	
		
	private String
	   name ,
	   type ,
	   value;


    public Field( Element fieldElement ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Field(): enter") ; 	
		   	
		try {		 
		    name = fieldElement.getAttribute( JobDocDescriptor.FIELD_NAME_ATTR ) ;
		    type = fieldElement.getAttribute( JobDocDescriptor.FIELD_TYPE_ATTR ) ;
		    NodeList nodeList = fieldElement.getChildNodes();
			for (int iNode = 0; iNode < nodeList.getLength(); iNode++) {
				Node ndChild = nodeList.item(iNode);
				if (ndChild.getNodeType() == Node.TEXT_NODE) {
					value = ndChild.getNodeValue() ;					
				} // end op if
			} // end of for
		} // end of try
		finally {
			if( TRACE_ENABLED ) logger.debug( "Field(): exit") ; 			 
		}
		
    } // end of Field()
    

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	   
} // end of class Field
