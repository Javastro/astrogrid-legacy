/*
 * $Id: WarehouseQueryTranslator.java,v 1.1 2003/11/19 17:33:40 kea Exp $ 
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
**/

package org.astrogrid.warehouse.queriers.ogsadai;

import org.astrogrid.datacenter.queriers.sql.SqlQueryTranslator;

/** ADQL Translator for OGSA-DAI SQL
 * @author Kona Andrews kea@ast.cam.ac.uk 
 */
public class WarehouseQueryTranslator extends SqlQueryTranslator {

  public WarehouseQueryTranslator() {
  }
}

/* 
$Log: WarehouseQueryTranslator.java,v $
Revision 1.1  2003/11/19 17:33:40  kea
Initial Querier functionality for integration with the database.
This compiles and sort-of runs at the command-line;  however, we
currently have no means of getting the OGSA-DAI results into the
format expected by the datacenter.  (They currently get dumped to
/tmp/WS_OGSA_OUTPUT).

*/
