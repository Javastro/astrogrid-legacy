/*$Id: SybaseQuerier.java,v 1.7 2003/09/17 14:51:30 nw Exp $
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

import java.io.IOException;

import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;


/** DatabaseQuerier implementation for Sybase.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Sep-2003
 * @todo fill tis in.
 */
public class SybaseQuerier extends SqlQuerier
{
    /**
     * @param ds
     * @throws DatabaseAccessException
     */
   public SybaseQuerier() throws DatabaseAccessException, IOException
   {
        super();
   }

   /**
    * Also starts Sybase jdbc driver... hardcoded, don't like this!
    * @todo - check reason for hardcoded driver - MCH
    *
   public void startDrivers() throws DatabaseAccessException
   {
      //new com.sybase.jdbc2.jdbc.SybDriver(); //compile-time check
      try
      {
            Class.forName("com.sybase.jdbc2.jdbc.SybDriver").newInstance();
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
    /**/

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.sql.SqlQuerier#createQueryTranslator()
     */
    protected QueryTranslator createQueryTranslator() {
        return new SybaseQueryTranslator();
    }

}


/*
$Log: SybaseQuerier.java,v $
Revision 1.7  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.6  2003/09/15 11:34:32  mch
made startDrivers() static, now must be called from application layer

Revision 1.5  2003/09/08 19:15:46  mch
Workspace constructor now throws IOException

Revision 1.4  2003/09/07 18:56:42  mch
Moved ADQL package dependency to QueryTranslator only

Revision 1.3  2003/09/05 13:21:08  nw
added hsqlDb querier
updated others to accept properties in constructor

Revision 1.2  2003/09/04 14:40:37  nw
fixed db driver name

Revision 1.1  2003/09/04 09:23:16  nw
added martin's query results implementation
abstract functionality from mysqlQuerier, places in SqlQuerier.
Added implementation classes for different db flavouors

*/
