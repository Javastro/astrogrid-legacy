/*
 * $Id: CutoutPlugin.java,v 1.1 2004/09/28 15:02:13 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.cutout;

import java.io.IOException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.status.QuerierComplete;
import org.astrogrid.datacenter.queriers.status.QuerierError;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.Query;

/**
 * An example plugin skeleton
 *
 * @author M Hill
 */

public class CutoutPlugin extends QuerierPlugin {
   
   
   public CutoutPlugin(Querier querier) throws IOException
   {
      super(querier);
   }
   
   /** Subclasses override this method to carry out the query.
    * Used by both synchronous (blocking) and asynchronous (threaded) querying
    * through processQuery. Should run the query and send the results although
    * the parent has methods to help with this.  The plugin should have everyting
    * tidied up and discarded as nec before returning - there is no close() method
    */
   public void askQuery() throws IOException {
      querier.setStatus(new QuerierQuerying(querier));
      
      Query query = querier.getQuery();

      if (query instanceof ConeQuery) {
         
//       make image...
         
      }
      else {
         RuntimeException t = new IllegalArgumentException("This plugin supports only cone queries");
         querier.setStatus(new QuerierError(querier, "", t));
         throw t;
      }
      
      querier.setStatus(new QuerierComplete(querier));

   
   
   }
   
   
   
 
}

/*
 $Log: CutoutPlugin.java,v $
 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.1  2004/08/05 13:38:40  mch
 Added example plugin skeleton


 */




