/*$Id: PostgresSqlMaker.java,v 1.6 2004/03/30 16:21:24 eca Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.ogsadai;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.spi.Translator;
import org.astrogrid.datacenter.queriers.sql.StdSqlMaker;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Element;
import org.astrogrid.datacenter.queriers.ogsadai.AdqlQueryTranslator;
/**
 * Produced Postgres-specific SQL
 */
public class PostgresSqlMaker extends StdSqlMaker {

   private static final Log log = LogFactory.getLog(PostgresSqlMaker.class);

   /**
    * Constructs an SQL statement for the given ADQL
    */
   public String fromAdql(AdqlQuery query) {
      //should use appropriate xslt, but use deprecated stuff for now

      try {
        Element queryBody = query.toDom().getDocumentElement();
        Translator trans = new AdqlQueryTranslator();
        // do the translation
        Object intermediateRep = null;
        Class expectedType = null;
        try { // don't trust it.
            intermediateRep = trans.translate(queryBody);
            expectedType = trans.getResultType();
            if (! expectedType.isInstance(intermediateRep)) { // checks result is non-null and the right type.
                throw new DatabaseAccessException("Translation result " + intermediateRep.getClass().getName() + " not of expected type " + expectedType.getName());
            }
        } catch (Throwable t) {
            throw new RuntimeException("Translation phase failed:" + t.getMessage());
        }
        return convertDecToDecl((String) intermediateRep);
     }
     catch (QueryException se) {
        throw new IllegalArgumentException("Bad ADQL/XML"+se);
     }

   }

  /**
   * Adjust SQL query (temporary kludge): datacenter default ADQL->SQL
   * translator produces DEC as column name where we need DECL.
   * @return  String holding adjusted SQL query
    * MCH: this was caused by hardcoding the cone translator. This should now be
    * set in config using the SqlQueryMaker.CONE_SEARCH_* keys.
   */
  protected String convertDecToDecl(String inString) {
    String sqlString = inString;
    while (true) {
      String newString;
      int index = sqlString.indexOf("t.DEC");
      int index2 = sqlString.indexOf("t.DECL");
      if (index != -1) {
        if (index != index2) {  //DEC - change to decl
          newString =
             sqlString.substring(0,index) +
             "t.decl" +
             sqlString.substring(index+5);
        }
        else {  //DECL - change to decl
          newString =
             sqlString.substring(0,index) +
             "t.decl" +
             sqlString.substring(index+6);
        }
      }
      else {
        break;  //No more occurrences of t.DEC
      }
      sqlString = newString;
    }
    return sqlString;
  }
   
}


/*
$Log: PostgresSqlMaker.java,v $
Revision 1.6  2004/03/30 16:21:24  eca
Updated ogsadai Postgres-optimized query translator, updated class
references in PostgresSqlMaker.

30/03/04 ElizabethAuden

Revision 1.5  2004/03/24 15:57:31  kea
Updated Javadocs etc.

Revision 1.4  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.3  2004/03/12 05:03:23  mch
Removed unused code

Revision 1.2  2004/03/12 05:01:22  mch
Changed doc

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor
 
*/

