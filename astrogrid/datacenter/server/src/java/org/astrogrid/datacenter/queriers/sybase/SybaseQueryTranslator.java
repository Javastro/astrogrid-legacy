/*$Id: SybaseQueryTranslator.java,v 1.4 2004/01/13 00:33:14 nw Exp $
 * Created on 03-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.sybase;

import org.astrogrid.datacenter.adql.generated.ArchiveTable;
import org.astrogrid.datacenter.queriers.sql.AdqlQueryTranslator;

/** ADQL translator to Sybase SQL.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Sep-2003
 * @todo fill this in - reverse engineer rules from old Query implementation.
 */
public class SybaseQueryTranslator extends AdqlQueryTranslator {
    /** template for this expression
     *
      transliterated from old query.Operation_Cone
     bit worried about the free vairables - where does UDEC come from? */
    public static final String CIRCLE_TEMPLATE = "( ((2 * ASIN(SQRT(POW(SIN({1}-UDEC)/2),2) + COS(UDEC) + POW(SIN({0}-URA)/2),2)) < {2} )";

    /**
     * @todo find out what this should be.
     *  This should now work in the superclass - MCH, 15 Dec 2003
    *
    public void visit(Circle c) {

        String exp = MessageFormat.format( CIRCLE_TEMPLATE
            , new Object[]{new Double(c.getRa().getValue()),new Double(c.getDec().getValue()), new Double(c.getRadius().getValue()) });
        stack.top().add(SEARCH,exp);
    }
    */
   
    /** according to old code, sybase uses '..' as separator between database and table name.
     *  (usually, this is just '.')
     */
    public void visit(ArchiveTable t) {
        StringBuffer buff = new StringBuffer()
          .append(t.getArchive())
          .append("..")
          .append(t.getName())
          .append(" AS ")
          .append(t.getAliasName());
        stack.top().add(TABLE,buff);
    }
}


/*
$Log: SybaseQueryTranslator.java,v $
Revision 1.4  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.3.4.1  2004/01/08 09:43:40  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.3  2003/12/15 18:11:47  mch
Fixed Circle SQL

Revision 1.2  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.

Revision 1.1  2003/11/14 00:38:30  mch
Code restructure

Revision 1.2  2003/09/10 09:59:14  nw
added bits to the query translators

Revision 1.1  2003/09/04 09:23:16  nw
added martin's query results implementation
abstract functionality from mysqlQuerier, places in SqlQuerier.
Added implementation classes for different db flavouors
 
*/
