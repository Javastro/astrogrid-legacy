/*$Id: MsSqlServerQueryTranslator.java,v 1.2 2004/01/15 14:49:47 nw Exp $
 * Created on 03-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.mssqlserver;

import org.astrogrid.datacenter.queriers.sybase.SybaseQueryTranslator;

/** ADQL query translator for Microsoft SQL Server.
 * <p> Extended from sybase query translator - fair bet, as SQL Server is based on Sybase.
 * <p>No other code here at the moment.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Sep-2003
 * @todo  fill this in.
 */
public class MsSqlServerQueryTranslator extends SybaseQueryTranslator {

}


/* 
$Log: MsSqlServerQueryTranslator.java,v $
Revision 1.2  2004/01/15 14:49:47  nw
improved documentation

Revision 1.1  2003/11/14 00:38:30  mch
Code restructure

Revision 1.2  2003/09/10 09:59:14  nw
added bits to the query translators

Revision 1.1  2003/09/04 09:23:16  nw
added martin's query results implementation
abstract functionality from mysqlQuerier, places in SqlQuerier.
Added implementation classes for different db flavouors
 
*/