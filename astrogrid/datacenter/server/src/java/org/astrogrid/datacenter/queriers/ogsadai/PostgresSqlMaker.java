/*$Id: PostgresSqlMaker.java,v 1.11 2004/06/25 10:55:43 mch Exp $
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
            throw new RuntimeException("Translation phase failed",t);
        }
      return (String) intermediateRep;
     }
     catch (QueryException se) {
        throw new IllegalArgumentException("Bad ADQL/XML"+se);
     }

   }
}


/*
$Log: PostgresSqlMaker.java,v $
Revision 1.11  2004/06/25 10:55:43  mch
More detailed exception reporting to translation failure

Revision 1.10  2004/04/16 16:45:23  eca
*** empty log message ***

Revision 1.9  2004/04/16 16:42:37  eca
Updated to call ADQLv06Utils - a class specific to ADQL v06. This will only
be called by the Postgres-specific query translator.

Revision 1.2  2004/04/02 03:27:49  eca
Fixed package declaration to include "deprecated".

Revision 1.1  2004/04/02 03:09:52  eca
Created "deprecated" folder to hold old version of PostgresSqlMaker.
Just in case.

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

