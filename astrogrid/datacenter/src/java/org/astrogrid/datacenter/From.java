/*
 * @(#)From.java	1.2 
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.datacenter;

import org.apache.log4j.Logger;
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
		ASTROGRIDERROR_COULD_NOT_dosomething = "AGDTCE00???" ;
	
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
				catalogElement = (Element) nodeList.item(i) ;
				if( catalogElement.getTagName().equals( JobDocDescriptor.OP_ELEMENT ) ) {
					getCatalogs() [i]= new Catalog( catalogElement ) ;
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
			if( TRACE_ENABLED ) logger.debug( "From(Element): exit") ;   	
		}
		   
	} // end of From( Element )
	

	public String toSQLString() {
    	
		StringBuffer
		   buffer = new StringBuffer(64) ;
    	   
		buffer
		  .append( "" )
		  .append( "" );
    	   
		return buffer.toString() ;
    	
	} // end of toSQLString()	


	public void setCatalogs(Catalog[] catalogs) {
		this.catalogs = catalogs;
	}

	public Catalog[] getCatalogs() {
		return catalogs;
	}		   

} // end of class From 
