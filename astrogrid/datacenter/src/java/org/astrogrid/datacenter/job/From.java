/*
 * @(#)From.java	1.2 
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.datacenter.job;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.datasetagent.*;
import org.astrogrid.datacenter.i18n.*;
import org.w3c.dom.* ;

/**
 * The <code>From</code> class represents ...
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
public class From {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( From.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_FROM_FROM_ELEMENT = "AGDTCE00200",
	    ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_FROM = "AGDTC00210" ;
	    		
	private Catalog []
	   catalogs ;
	   
	public From( Element fromElement ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "From(Element): entry") ;
		   		
		try {

			NodeList
			   nodeList = fromElement.getChildNodes() ;
			Element
			    catalogElement ;

			setCatalogs(new Catalog[ nodeList.getLength() ]);
						   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {	
				if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
					continue ;							
				catalogElement = (Element) nodeList.item(i) ;				
				if( catalogElement.getTagName().equals( RunJobRequestDD.CATALOG_ELEMENT ) ) {
					getCatalogs() [i]= new Catalog( catalogElement ) ;
				}
				else  {
                    ; // JBL Note: What do I do here?
				}
				
			} // end for		
			
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_FROM_FROM_ELEMENT ) ;
			logger.error( message.toString(), ex ) ;
			throw new QueryException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "From(Element): exit") ;   	
		}
		   
	} // end of From( Element )
	
/**
 * Returns a String containing catalogue and table details.
 * If the table element is empty then assume owner and table name are the same
 * and the return is of the form: catalogue..catalogue.
 * If there is one table element then the return is of the form: catalogue..table
 * If there are a number of tables then the return will be a comma seperated list in the form:
 * catalogue..table1, catalogue..table2, catalogue..table3 
 */
	public String toSQLString() {
		
		StringBuffer
			buffer = new StringBuffer(64) ;
		
    	try {
            Catalog 
                catalog[] = this.catalogs;
        
        	for (int i=0; i < catalog.length; i++) {		    
        	
        		Table 
        	        table[] = catalog[i].getTables();
        	
        		if (table.length <= 0) {  // no table specified assume owner and table name are same
        			buffer.append(catalog[i].getName());
        			buffer.append("..");
        			buffer.append(catalog[i].getName());
        			buffer.append(", ");
        		}
        	
        		else {
        		
			    	for (int j=0; j < table.length; j++) {
			    		
					    buffer.append(catalog[i].getName());
				    	buffer.append("..");
				        buffer.append(table[j].getName());				
				        buffer.append(", ");
			           		
					} // end of for (int j=0; j < table.length; j++)		
        		}
    		} // end of for (int i=0; i < catalog.length; i++)
			buffer.deleteCharAt(buffer.length()-2); // remove final ", "
			
    	}
		catch( Exception ex) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_FROM ) ;
			logger.error( message.toString(), ex ) ;   		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Return(Element): exit") ;   	
		}
		
		return buffer.toString() ;
    	
	} // end of toSQLString()
	

	public void setCatalogs(Catalog[] catalogs) {
		this.catalogs = catalogs;
	}

	public Catalog[] getCatalogs() {
		return catalogs;
	}		   

} // end of class From 
