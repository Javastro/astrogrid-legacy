/*$Id: EgsoQuerierPlugin.java,v 1.3 2004/09/06 21:36:15 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.sec.querier;

import java.io.IOException;
import javax.xml.rpc.ServiceException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.VotableResults;
import org.astrogrid.datacenter.sec.secdelegate.egso.EgsoDelegate;
import org.w3c.dom.Document;

/** Datacenter querier that performs queries against SEC webservice.
 * @author Kevin Benson kmb@mssl.ucl.ac.uk
 */
public class EgsoQuerierPlugin extends QuerierPlugin {
   
   protected EgsoDelegate delegate;

   /** @todo check configuration for endpoint setting before settling with default */
   public EgsoQuerierPlugin(Querier querier) throws ServiceException {
      super(querier);
      delegate = new EgsoDelegate();
   }
   
   /* (non-Javadoc)
    * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#doQuery(java.lang.Object, java.lang.Class)
    */
   public void askQuery() throws IOException {
      EgsoQueryMaker translator = new EgsoQueryMaker();
      EgsoQuery query = translator.getEgsoQuery(querier.getQuery());
      Document results = query.doDelegateQuery(delegate);
      processResults(new VotableResults(results, null));
   }
}


/*
 $Log: EgsoQuerierPlugin.java,v $
 Revision 1.3  2004/09/06 21:36:15  mch
 Factored out VotableResults

 Revision 1.2  2004/07/07 14:32:54  KevinBenson
 Few small changes because I had it referencing "cds" at the moment.

 Revision 1.1  2004/07/07 09:17:40  KevinBenson
 New SEC/EGSO proxy to query there web service on the Solar Event Catalog

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
