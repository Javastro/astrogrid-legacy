/*
 * Created on 20-Aug-2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.astrogrid.datacenter.query;

import org.astrogrid.datacenter.config.ConfigurableImpl;

/** base class for all elements of the SQL composite pattern
  */
public abstract class SQLComponent {
   public abstract String toSQLString() ;
}
