/*$Id: HsqlQuerier.java,v 1.3 2003/09/08 16:34:31 mch Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.hsql;

import java.util.Properties;

import javax.sql.DataSource;

import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;

/**
 * Hypersonic SQL interface.
 *
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Sep-2003
 * @author M Hill
 *
 */
public class HsqlQuerier extends SqlQuerier {

    public HsqlQuerier() throws DatabaseAccessException {
        super();
    }

   /**
    * Also starts Hypersonic jdbc driver... hardcoded, don't like this!
    * @todo - check reason for hardcoded driver - MCH
    */
   public void startDrivers() throws DatabaseAccessException
   {
      //new com.sybase.jdbc2.jdbc.SybDriver(); //compile-time check
      try
      {
         Class.forName("org.hsqldb.jdbcDriver").newInstance();
      }
      catch (IllegalAccessException e)
      {
         throw new DatabaseAccessException(e,"JDBC Driver error: " + e.toString());
      }
      catch (InstantiationException e)
      {
         throw new DatabaseAccessException(e, "JDBC Driver error: " + e.toString());
      }
      catch (ClassNotFoundException e)
      {
         throw new DatabaseAccessException(e, "JDBC Driver error: " + e.toString());
      }

      //start usual config ones
      super.startDrivers();
   }
}


/*
$Log: HsqlQuerier.java,v $
Revision 1.3  2003/09/08 16:34:31  mch
Added documentation

Revision 1.2  2003/09/07 18:56:42  mch
Moved ADQL package dependency to QueryTranslator only

Revision 1.1  2003/09/05 13:21:08  nw
added hsqlDb querier
updated others to accept properties in constructor

*/
