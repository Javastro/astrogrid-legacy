/*
 * $Id: FitsQuerier.java,v 1.2 2003/11/27 00:52:58 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.fits;

import java.net.URL;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.spi.BaseQuerierSPI;
import org.astrogrid.datacenter.queriers.spi.QuerierSPI;


/**
 *
 *
 * @author M Hill
 */

public class FitsQuerier extends BaseQuerierSPI implements QuerierSPI
{

   /**
    * locates all the fits files in this dataset that overlap the given
    * circular region
    */
   public URL[] findFitsInCone(double ra, double dec, double sr)
   {
      return null;
   }
   
   /** Querier implemenation - Updates the status and does the query (by calling the abstract
    * queryDatabase() overridden by subclasses) and returns the results.
    * Use by both synchronous (blocking) and asynchronous (threaded) querying
    */
   public QueryResults doQuery(Object o ,Class type) throws DatabaseAccessException {
      // TODO
      return null;
   }

/* (non-Javadoc)
 * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#getPluginInfo()
 */
public String getPluginInfo() {
    return "FITS Querier";
}

}

/*
$Log: FitsQuerier.java,v $
Revision 1.2  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.

Revision 1.1  2003/11/25 18:50:06  mch
Abstracted Querier from DatabaseQuerier

 */

