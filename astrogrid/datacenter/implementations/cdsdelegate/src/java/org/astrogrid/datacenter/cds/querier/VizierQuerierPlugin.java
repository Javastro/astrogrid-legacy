/*$Id: VizierQuerierPlugin.java,v 1.2 2004/03/14 04:14:20 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.cds.querier;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.xml.rpc.ServiceException;
import org.astrogrid.datacenter.cdsdelegate.vizier.VizierDelegate;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.w3c.dom.Document;

/** Datacenter querier SPI that performs queries against CDS Vizier webservice.
 * @see AdqlVizierTranslator
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Nov-2003
 */
public class VizierQuerierPlugin extends QuerierPlugin {
   
   protected VizierDelegate delegate;

   /** @todo check configuration for endpoint setting before settling with default */
   public VizierQuerierPlugin(Querier querier) throws ServiceException {
      super(querier);
      delegate = new VizierDelegate();
   }
   
   /* (non-Javadoc)
    * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#doQuery(java.lang.Object, java.lang.Class)
    */
   public void askQuery() throws IOException {
      
      VizierQueryMaker translator = new VizierQueryMaker();
      VizierQuery query = translator.getVizierQuery(querier.getQuery());

      query.doDelegateQuery(delegate, querier.getResultsTarget().resolveWriter(querier.getUser()));
   }
   
   
}


/*
 $Log: VizierQuerierPlugin.java,v $
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
