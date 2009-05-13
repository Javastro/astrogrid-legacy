/*
 * $Id: CutoutPlugin.java,v 1.1 2009/05/13 13:20:26 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.cutout;

import java.io.IOException;
import java.security.Principal;
import org.astrogrid.dataservice.queriers.DefaultPlugin;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.status.QuerierComplete;
import org.astrogrid.dataservice.queriers.status.QuerierQuerying;
import org.astrogrid.query.Query;

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
   public void askQuery(Principal user, Query query, Querier querier) throws IOException {
      querier.setStatus(new QuerierQuerying(querier.getStatus(), query.toString()));
      
         
//       make image...
         
      
      querier.setStatus(new QuerierComplete(querier.getStatus()));

   
   
   }
   
   /** Returns just the number of matches rather than the list of matches */
   public long getCount(Principal user, Query query, Querier querier) throws IOException {
            throw new UnsupportedOperationException("Not done yet");
   }
   
   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return new String[] { "FITS" };
   }
   
   
 
}

/*
 $Log: CutoutPlugin.java,v $
 Revision 1.1  2009/05/13 13:20:26  gtr
 *** empty log message ***

 Revision 1.2  2005/03/10 15:13:48  mch
 Seperating out fits, table and xdb servers


 */




