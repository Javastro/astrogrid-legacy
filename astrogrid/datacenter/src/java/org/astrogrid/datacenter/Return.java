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
	    ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_RETURN = "AGDTCE00250", 
		ASTROGRIDERROR_UNABLE_TO_MAP_CATALOG_UCD_TO_COLUMN_HEADING = "AGDTCE00250";
		
    private Field []
        fields ;
        
    private Table []
        tables ;
        
    private Catalog
        catalog ;
        
    public Return( Element returnElement, Catalog catalog ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Return(Element): entry") ;
		   		
		try {

			NodeList
			   nodeList = returnElement.getElementsByTagName( JobDocDescriptor.FIELD_ELEMENT ) ;
			   
			Element
				fieldElement ;
				
			fields = new Field[ nodeList.getLength() ];
			this.catalog = catalog;
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				fieldElement = (Element) nodeList.item(i) ;
				if( fieldElement.getTagName().equals( JobDocDescriptor.FIELD_ELEMENT ) ) {
					fields[i] = new Field( fieldElement, catalog ) ;
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
		        field[] = this.fields;
		    buffer.append(" ");
		    for (int i = 0; i < field.length; i++){
		    	if (field[i].getType().equals(JobDocDescriptor.FIELD_TYPE_UCD)) {
					buffer.append(getColumnHeading(catalog,field[i].getName()));
		    	}
		    	else {
		    		buffer.append(field[i].getName());
		    	}
		        buffer.append(", ");			  
		    } // end of for
		
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
 * <code>ASROGRID_datasetconfig.properties</code> file. The frmat of the mapping key is:
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
			columnHeading = "";
		StringBuffer
			buffer = new StringBuffer(64) ; 
		try {	
			tables = catalog.getTables();  
			if (tables.length <= 0) {  // if no tables assosciated with catalog assume table name same as catalog
				buffer.append(catalog.getName());
				buffer.append(".");
				buffer.append(catalog.getName());
				buffer.append(".");
				buffer.append(UCD);
				logger.debug("Return: getColumnHeading(): key: "+buffer.toString());				
				columnHeading = DatasetAgent.getProperty( buffer.toString() ) ;					
			} // end of if
			for (int i=0 ; i < tables.length ; i++) { 
				buffer.append(catalog.getName());
				buffer.append(".");
				buffer.append(tables[i].getName().toUpperCase());
				buffer.append(".");
				buffer.append(UCD);	
				logger.debug("Return: getColumnHeading(): key: "+buffer.toString());
				columnHeading = DatasetAgent.getProperty( buffer.toString() ) ;
				if (columnHeading.length() > 0) // break as soon as column heading found
				    break; 
				buffer.delete(0,buffer.length());
			} // end of for
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

} // end of Class
