/*
 * $Id: FitsQuerier.java,v 1.1 2003/11/25 18:50:06 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.fits;

import java.net.URL;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QueryResults;


/**
 *
 *
 * @author M Hill
 */

public class FitsQuerier extends Querier
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
   public QueryResults doQuery() throws DatabaseAccessException {
      // TODO
      return null;
   }
   
}

/*
$Log: FitsQuerier.java,v $
Revision 1.1  2003/11/25 18:50:06  mch
Abstracted Querier from DatabaseQuerier

 */

