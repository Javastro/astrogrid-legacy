/*
 * Created on 20-Aug-2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.astrogrid.datacenter.query;

import org.astrogrid.datacenter.config.ConfigurableImpl;

/** base class for all elements of the SQL composite pattern
 * surprised they've not already got a base class, but there you go..
  */
public abstract class SQLComponent extends ConfigurableImpl {
	public abstract String toSQLString() ;
}
