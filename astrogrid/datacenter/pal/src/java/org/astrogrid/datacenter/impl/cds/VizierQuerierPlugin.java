/*$Id: VizierQuerierPlugin.java,v 1.4 2004/11/03 00:17:56 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.impl.cds;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.impl.cds.vizier.VizierDelegate;
import org.astrogrid.datacenter.queriers.DefaultPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.VotableDomResults;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.query.Query;
import org.xml.sax.SAXException;

/** Datacenter querier SPI that performs queries against CDS Vizier webservice.
 * @see AdqlVizierTranslator
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Nov-2003
 */
public class VizierQuerierPlugin extends DefaultPlugin  {
   
   

  /** Key specifying which Vizier catalogue to query. */
   public static final String CATALOGUE_NAME =
     "datacenter.vizier.querier.catalogueName";

   /** delegate */
   VizierDelegate delegate;
   

   /** @todo check configuration for endpoint setting before settling with default */
   public VizierQuerierPlugin() throws ServiceException {
      super();
   }
   
   /* (non-Javadoc)
    * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#doQuery(java.lang.Object, java.lang.Class)
    */
   public void askQuery(Account user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus()));

      try {
         delegate = new VizierDelegate();

       public java.lang.String cataloguesData(java.lang.String target, double radius, java.lang.String unit, java.lang.String text, java.lang.String wavelength) throws java.rmi.RemoteException {

         VizierQueryMaker translator = new VizierQueryMaker();
         VizierQuery vquery = translator.getVizierQuery(query);
   
         String votableDoc = vquery.askQuery(delegate);
         
         if (!aborted) {
            VotableDomResults results = new VotableDomResults(querier, votableDoc);
            results.send(query.getResultsDef(), Account.ANONYMOUS);
         }

      }
      catch (ServiceException e) {
         throw new IOException("Could not connect to Vizier: "+e);
      }
      catch (SAXException e) {
         throw new DatacenterException("XML Error in Vizier results: "+e,e);
      }
      catch (ParserConfigurationException e) {
         throw new DatacenterException("Server not configured properly: "+e,e);
      }
      
   }
   
   /** Returns just the number of matches rather than the list of matches */
   public long getCount(Account user, Query query, Querier querier) throws IOException {
      // TODO
      throw new UnsupportedOperationException("Todo");
      
   }
   /**
    * Returns the VOResource element of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public String getVoResource() throws IOException {
      try {
         VizierDelegate delegate = new VizierDelegate();
         String votableHeader = delegate.getAllMetadata();
         //do something to it to make it a Resource
         //@todo
         //return it
         return votableHeader;
      }
      catch (ServiceException e) {
         throw new IOException("Could not connect to Vizier:"+e);
      }

   }
   
   
}


/*
 $Log: VizierQuerierPlugin.java,v $
 Revision 1.4  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.3.6.1  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.3  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.2.4.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.2  2004/10/05 20:26:43  mch
 Prepared for better resource metadata generators

 Revision 1.1  2004/10/05 19:19:18  mch
 Merged CDS implementation into PAL

 Revision 1.5  2004/09/29 18:45:55  mch
 Bringing Vizier into line with new(er) metadata stuff

 Revision 1.4  2004/08/14 14:35:42  acd
 Fix the cone search in the Vizier Proxy.

 Revision 1.4  2004/08/13 16:50:00  acd
 Added static final String METADATA.

 Revision 1.3  2004/08/12 17:31:00  acd
 Added static final String CATALOGUE_NAME.

 Revision 1.2  2004/03/14 04:14:20  mch
 Wrapped output target in TargetIndicator

 Revision 1.1  2004/03/13 23:40:59  mch
 Changes to adapt to It05 refactor

 Revision 1.6  2003/12/09 16:25:08  nw
 wrote plugin documentation

 Revision 1.5  2003/12/01 16:50:11  nw
 first working tested version

 Revision 1.4  2003/11/28 19:12:16  nw
 getting there..

 Revision 1.3  2003/11/25 11:14:51  nw
 upgraded to new service interface

 Revision 1.2  2003/11/20 15:47:18  nw
 improved testing

 Revision 1.1  2003/11/18 11:23:49  nw
 mavenized cds delegate

 Revision 1.1  2003/11/18 11:10:05  nw
 mavenized cds delegate
 
 */
