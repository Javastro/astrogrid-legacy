/*
 * @(#)Table.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.job;

import org.apache.log4j.Logger;
import org.astrogrid.i18n.*;
import org.astrogrid.jes.jobcontroller.*;
import org.astrogrid.Configurator ;
import org.w3c.dom.* ;

/**
 * The <code>Table</code> class represents ...
 * <p>
 * Introductory text.... For example:
 * <p><blockquote><pre>
 *     
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.job.jes.Query
 * @since   AstroGrid 1.2
 */
public class Table {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Table.class ) ;
        
    private final static String 
        SUBCOMPONENT_NAME = Configurator.getClassName( Table.class );                 
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_TABLE = "AGJESE00620" ;
	
    public static final String
        TABLE_FOR_ADQL_BASED_DATACENTER = "TableForADQLBasedDatacenter_Iteration3" ;
    
	private String 
	   name ;
	   
	private Catalog
	   parent ;
	
	   
	public Table( Catalog parent ) {
		setParent( parent ) ;
	}
	
	   
	public Table( Catalog parent, Element tableElement ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "Table(Element): entry") ; 
		
		setParent(parent) ;  
				
		try {
			setName(tableElement.getAttribute( SubmissionRequestDD.TABLE_NAME_ATTR )) ;			
		}
		catch( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_TABLE 
                                              , SUBCOMPONENT_NAME ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Table(Element): exit") ;   	
		}
		   
	} // end of Table( Element )


	public void setName(String name) { this.name = name.trim(); }
	public String getName() { return name.trim(); }

	public void setParent(Catalog parent) { this.parent = parent; }
	public Catalog getParent() { return parent; }	   


} // end of class Table 
