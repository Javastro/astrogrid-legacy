/*
 * @(#)Field.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.query;

import java.util.Iterator;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.i18n.AstroGridMessage;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.Query
 * @since   AstroGrid 1.2
 */
public class Field extends SQLComponent  implements Operand
{
   private static final String
      ASTROGRIDERROR_UNKNOWN_FIELD_TYPE_ENCOUNTERED = "AGDTCE00430" ;

   private static final String
      TYPE_UCD      = new String( "UCD" ).intern() ,
      TYPE_COLUMN   = new String( "COLUMN" ).intern() ,
      TYPE_PASSTHROUGH = new String( "PASSTHROUGH" ).intern() ,
      TYPE_CONSTANT = new String( "CONSTANT" ).intern(),
      SQL_DELETE_LOWER = "delete",
      SQL_DELETE = "DELETE",
      SQL_UPDATE_LOWER = "update",
      SQL_UPDATE = "UPDATE",
      SQL_INSERT_LOWER = "insert",
      SQL_INSERT = "INSERT" ;

   private String
      name  = null,
      type  = null,
      value = "" ;

   private Catalog
       catalog = null ;


    public Field( Element fieldElement, Catalog catalog ) throws QueryException {
      Log.trace( "Field(): enter") ;

      try {
         this.catalog = catalog;

          this.type = fieldElement.getAttribute( AdqlTags.FIELD_TYPE_ATTR ) ;

          if (this.type.equals( TYPE_PASSTHROUGH ) ) {
            this.name = nameParse( fieldElement.getAttribute( AdqlTags.FIELD_NAME_ATTR ).trim() ) ;
          }
          else {
             this.name = fieldElement.getAttribute( AdqlTags.FIELD_NAME_ATTR ).trim() ;
          }

         if( type != null )
            type = type.trim().toUpperCase() ;

          // Anything not defined as a column, UCD or PASSTHROUGH is assumed to be a constant...
         if( type == null  ||  type.equals( "" ) ) {
             type = TYPE_CONSTANT ;
         }
         else if(  type.equals( TYPE_COLUMN )  ||  type.equals( TYPE_UCD ) || type.equals( TYPE_PASSTHROUGH ) ) {
                // We grab the internalized representation from the string pool for performance reasons...
            type = type.intern() ;
         }
         // Whoops! Some non-existent type has been used...
         else {
            AstroGridMessage
               message = new AstroGridMessage( ASTROGRIDERROR_UNKNOWN_FIELD_TYPE_ENCOUNTERED
                                                 , this
                                                 , type ) ;
            Log.logError( message.toString() ) ;
            throw new QueryException( message ) ;
         }

          NodeList nodeList = fieldElement.getChildNodes();

         for( int iNode = 0; iNode < nodeList.getLength(); iNode++ ) {
            Node ndChild = nodeList.item(iNode);

            if (ndChild.getNodeType() == Node.TEXT_NODE) {
               value = ndChild.getNodeValue().trim() ;
               break ;
            } // end op if

         } // end of for

      } // end of try
      finally {
         Log.trace( "Field(): exit") ;
      }

    } // end of Field()


   /**
    * Returns a <code>String</code> value of the column heading for a specific catalog, table
    * and UCD (unified content descriptor) combination. The mapping is detailed in the
    * <code>ASROGRID_datasetconfig.properties</code> file. The format of the mapping key is:
    * <catalog>.<table>.<UCD>
    *
    * @see             org.astrogrid.datacenter.ASROGRID_datasetconfig.properties
    * @return        <code>String</code> columnHeading
    */
      private String getColumnHeading() {
         Log.trace( "Field.getColumnHeading(): entry") ;

         if( this.type == TYPE_COLUMN )
             return this.getName() ;

         String
            columnHeading = "",
            catalogName = "",
            tableName = "";

         StringBuffer
            buffer = new StringBuffer(64) ;

         try {

                // if no tables associated with catalog assume table name same as catalog...
            if ( catalog.getNumberTables() <= 0) {

               buffer
                  .append( catalog.getName() )
                  .append( "." )
                  .append( catalog.getName() )
                  .append( "." )
                  .append( this.getName() );
               Log.logDebug( "Criteria: getColumnHeading(): key: " + buffer.toString().toUpperCase() ) ;
               columnHeading = SimpleConfig.getProperty( buffer.toString().toUpperCase()) ;

            }
            else {

               Table
                  table = null ;
               Iterator
                  it = catalog.getTables() ;

               while( it.hasNext() ) {
                  table = (Table)it.next() ;
                  buffer
                     .append( catalog.getName() )
                     .append( "." )
                     .append( table.getName().toUpperCase() )
                     .append( "." )
                     .append( this.getName() );
                  Log.logDebug("Criteria: getColumnHeading(): key: "+buffer.toString().toUpperCase() );
                  columnHeading = SimpleConfig.getProperty( buffer.toString().toUpperCase());
                  if ( columnHeading.length() > 0 ) // break as soon as column heading found
                     break;
                  buffer.delete( 0, buffer.length() ) ;
               } // end of while

            } // end else

         }
//       catch (Exception ex) {
//          Message
//             message = new Message( ASTROGRIDERROR_UNABLE_TO_MAP_CATALOG_UCD_TO_COLUMN_HEADING ) ;
//          Log.logError( message.toString(), ex ) ;
//       }
         finally {
            Log.trace( "Field.getColumnHeading(): exit") ;
         }

         return ( columnHeading == "" )  ?  this.getName()  :  columnHeading ;

      } // end of getColumnHeading()


   public void setName(String name) { this.name = name; }
   public String getName() { return name; }

   public void setType(String type) { this.type = type; }
   public String getType() { return type; }

   public String getValue() { return value; }
   public Catalog getCatalog() { return catalog; }


   public String toSQLString() {
      Log.trace( "Field.toSQLString(): entry") ;

      String
         retValue = null ;

      // JBL Note: BEWARE!!!
      // The following relies upon the String.intern() function and
      // the internal pool of strings for performance reasons...
      try {
         if( this.type == TYPE_COLUMN ) {
            retValue = this.getName() ;
         }
         else if( this.type == TYPE_UCD ) {
            retValue = this.getColumnHeading() ;
         }
         else if( this.type == TYPE_CONSTANT ) {
            retValue = this.getValue() ;
         }
         else {
            ; // It should be impossible to get here!
            Log.logDebug( "We have an errant type in Field" ) ;
         }
      }
      finally {
         Log.trace( "Field.toSQLString(): exit") ;
      }

      return retValue ;

   } // end of toSQLString()


    /**
     * Having the field type PASSTHROUGH opens up the Datacenter to the possibility of a
     * user entering valid, but malicious, SQL queries including DELETE, UPDATE and INSERT
     * statements. Although the query builder should prevent this happening nameParse()
     * is included as a cursory check against this happening. If name contains DELETE, UPDATE,
     * INSERT an empty string is returned.
     *
     * @param name
     * @return returnString
     */
   private String nameParse(String name) {

      String returnString = name ;

      if  ( ( name.indexOf( SQL_DELETE ) != -1 ) || ( name.indexOf( SQL_DELETE_LOWER) != -1 ) ||
            ( name.indexOf( SQL_INSERT ) != -1 ) || ( name.indexOf( SQL_INSERT_LOWER ) != -1 ) ||
            ( name.indexOf( SQL_UPDATE ) != -1 ) || ( name.indexOf( SQL_UPDATE_LOWER ) != -1) ) {

         Log.logDebug( "Attempt to enter malicious SQL using type PASSTHROUGH in Field" ) ;
         returnString = "" ;
      }

      return returnString ;
   } //end of nameParse

} // end of class Field
