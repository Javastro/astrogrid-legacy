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
package org.astrogrid.datacenter.query;

import org.apache.log4j.Logger;

import org.astrogrid.i18n.AstroGridMessage;
import org.w3c.dom.Element;

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
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Table {

   private static final boolean
      TRACE_ENABLED = true ;

   private static Logger
      logger = Logger.getLogger( Table.class ) ;

   private static final String
      ASTROGRIDERROR_COULD_NOT_CREATE_TABLE_FROM_ELEMENT = "AGDTCE00440" ;

   private String
      name ;

   public Table( Element tableElement ) throws QueryException {
      if( TRACE_ENABLED ) logger.debug( "Table(Element): entry") ;

      try {
         setName( tableElement.getFirstChild().getNodeValue().trim() ) ;
      }
      catch( Exception ex ) {
         AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_TABLE_FROM_ELEMENT
                                              , this);
         logger.error( message.toString(), ex ) ;
         throw new QueryException( message, ex );
      }
      finally {
         if( TRACE_ENABLED ) logger.debug( "Table(Element): exit") ;
      }

   } // end of Table( Element )


   public void setName(String name) { this.name = name; }
   public String getName() { return name; }

} // end of class Table
