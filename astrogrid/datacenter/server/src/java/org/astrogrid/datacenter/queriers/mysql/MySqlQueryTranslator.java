/*$Id: MySqlQueryTranslator.java,v 1.3 2003/11/28 16:10:30 nw Exp $
 * Created on 29-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.mysql;

import org.astrogrid.datacenter.adql.generated.Circle;
import org.astrogrid.datacenter.queriers.sql.AdqlQueryTranslator;
/** ADQL Translator for MySQL dialect SQL
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Aug-2003
 */
public class MySqlQueryTranslator extends AdqlQueryTranslator {
    
    
 
    /** 
     * @todo need to find how to implement this for mysql - is it the same as
     * the cone search in old query code?
     */
    public void visit(Circle c) {
        super.visit(c);
    }


} // end class


/* 
$Log: MySqlQueryTranslator.java,v $
Revision 1.3  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.2  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.4  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.3  2003/09/10 09:59:14  nw
added bits to the query translators

Revision 1.2  2003/09/03 13:47:30  nw
improved documentaiton.
split existing MySQLQueryTranslator into a vanilla-SQL
version, and MySQL specific part.

Revision 1.1  2003/09/02 14:46:34  nw
MySQL implementaiton of an SQL translation
 
*/