/*
 * $Id: CutoutPlugin.java,v 1.3 2004/10/06 21:12:17 mch Exp $
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
import org.astrogrid.datacenter.query.SimpleQueryMaker;
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
      querier.setStatus(new QuerierQuerying(querier.getStatus()));
      
      Query query = querier.getQuery();

         
//       make image...
         
      
      querier.setStatus(new QuerierComplete(querier.getStatus()));

   
   
   }
   
   
   
 
}

/*
 $Log: CutoutPlugin.java,v $
 Revision 1.3  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.2  2004/10/01 18:04:58  mch
 Some factoring out of status stuff, added monitor page

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.1  2004/08/05 13:38:40  mch
 Added example plugin skeleton


 */




