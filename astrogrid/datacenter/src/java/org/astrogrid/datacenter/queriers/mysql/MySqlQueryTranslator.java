/*$Id: MySqlQueryTranslator.java,v 1.2 2003/09/03 13:47:30 nw Exp $
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

import org.astrogrid.datacenter.adql.QOM;
import org.astrogrid.datacenter.adql.generated.*;
import org.astrogrid.datacenter.queriers.sql.SqlQueryTranslator;
import org.astrogrid.datacenter.queriers.TranslationFrame;
/** ADQL Translator for MySQL dialect SQL
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Aug-2003

    @todo override rule for CIRCLE, at least.
 */
public class MySqlQueryTranslator extends SqlQueryTranslator {
 
} // end class


/* 
$Log: MySqlQueryTranslator.java,v $
Revision 1.2  2003/09/03 13:47:30  nw
improved documentaiton.
split existing MySQLQueryTranslator into a vanilla-SQL
version, and MySQL specific part.

Revision 1.1  2003/09/02 14:46:34  nw
MySQL implementaiton of an SQL translation
 
*/