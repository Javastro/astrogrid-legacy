/*
 * $Id: CutoutPlugin.java,v 1.5 2004/11/03 00:17:56 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.cutout;

import java.io.IOException;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.DefaultPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.status.QuerierComplete;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.query.Query;

/**
 * An example plugin skeleton
 *
 * @author M Hill
 */

public class CutoutPlugin extends DefaultPlugin {
   
   
   public CutoutPlugin() throws IOException
   {
   }
   
   /** Does Query    */
   public void askQuery(Account user, Query query, Querier querier) throws IOException {
      querier.setStatus(new QuerierQuerying(querier.getStatus()));
      
         
//       make image...
         
      
      querier.setStatus(new QuerierComplete(querier.getStatus()));

   
   
   }
   
   /** Returns just the number of matches rather than the list of matches */
   public long getCount(Account user, Query query, Querier querier) throws IOException {
            throw new UnsupportedOperationException("Not done yet");
   }
   
   
 
}

/*
 $Log: CutoutPlugin.java,v $
 Revision 1.5  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.4.6.1  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.4  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.3.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.3  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.2  2004/10/01 18:04:58  mch
 Some factoring out of status stuff, added monitor page

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.1  2004/08/05 13:38:40  mch
 Added example plugin skeleton


 */




