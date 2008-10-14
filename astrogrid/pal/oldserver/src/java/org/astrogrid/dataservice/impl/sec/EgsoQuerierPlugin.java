/*$Id: EgsoQuerierPlugin.java,v 1.2 2008/10/14 12:28:51 clq2 Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.impl.sec;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.Principal;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import org.astrogrid.dataservice.impl.sec.SEC_Port;
import org.astrogrid.dataservice.impl.sec.SEC_Service;
import org.astrogrid.dataservice.impl.sec.SEC_ServiceLocator;
import org.astrogrid.dataservice.queriers.DefaultPlugin;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierPluginException;
import org.astrogrid.dataservice.queriers.VotableDomResults;
import org.astrogrid.tableserver.jdbc.AdqlSqlMaker;
import org.astrogrid.dataservice.queriers.status.QuerierQuerying;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.xml.sax.SAXException;

/** Datacenter querier that performs queries against SEC webservice.
 * @author Kevin Benson kmb@mssl.ucl.ac.uk
 * @author mch
 */
public class EgsoQuerierPlugin extends DefaultPlugin {
   
   public final static String SEC_URL = "http://radiosun.ts.astro.it/sec/sec_server.php";
 
   /** WSDL-generated binding to the service */
   protected SEC_Port secPort;
   
   public EgsoQuerierPlugin() throws ServiceException, MalformedURLException {
      SEC_Service service = new SEC_ServiceLocator();
      secPort = service.getSECPort(new URL(SEC_URL));
   }
   
   /** Called by the querier plugin mechanism to do the query.
    * The EGSO service takes an SQL string and returns a VOTable.
    */
   public void askQuery(Principal user, Query query, Querier querier) throws IOException, QueryException {

      //convert query to SQL
      //StdSqlMaker ssm = new StdSqlMaker();
      //String sql = ssm.makeSql(query);
      AdqlSqlMaker asm = new AdqlSqlMaker();
      String sql = asm.makeSql(query);

      querier.setStatus(new QuerierQuerying(querier.getStatus(), sql));
      
      try {
         //call SEC - results come back as a complete String
         String resultsVot = secPort.sql(sql);
         VotableDomResults results = new VotableDomResults(querier, resultsVot);
         if (!aborted) {
            results.send(query.getResultsDef(), querier.getUser());
         }
      }
      catch (RemoteException e) {
         throw new QuerierPluginException(e+" from PAL to EGSO, Submitting '"+sql+"' to EGSO service at "+SEC_URL,e);
      }
      catch (SAXException e) {
         throw new QuerierPluginException(e+" parsing results from submitting '"+sql+"' to EGSO service at "+SEC_URL,e);
      }
      catch (ParserConfigurationException e) {
         throw new QuerierPluginException(e+", Server Configuration Error",e);
      }
   }
   
   /** Returns just the number of matches rather than the list of matches */
   public long getCount(Principal user, Query query, Querier querier) throws IOException, QueryException {
      //convert query to SQL
      //StdSqlMaker ssm = new StdSqlMaker();
      //String sql = ssm.makeCountSql(query);
      AdqlSqlMaker asm = new AdqlSqlMaker();
      String sql = asm.makeCountSql(query);

      querier.setStatus(new QuerierQuerying(querier.getStatus(), sql));

      try {
         //call SEC - results come back as a complete String
         String resultsVot = secPort.sql(sql);
         VotableDomResults results = new VotableDomResults(querier, resultsVot);
         if (!aborted) {
            throw new UnsupportedOperationException("Not done yet");
         }
         return -1;
      }
      catch (RemoteException e) {
         throw new QuerierPluginException(e+" from PAL to EGSO, Submitting '"+sql+"' to EGSO service at "+SEC_URL,e);
      }
      catch (SAXException e) {
         throw new QuerierPluginException(e+" parsing results from submitting '"+sql+"' to EGSO service at "+SEC_URL,e);
      }
      catch (ParserConfigurationException e) {
         throw new QuerierPluginException(e+", Server Configuration Error",e);
      }
   }
   
   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return VotableDomResults.listFormats();
   }
   
   
   
}


/*
 $Log: EgsoQuerierPlugin.java,v $
 Revision 1.2  2008/10/14 12:28:51  clq2
 PAL_KEA_2804

 Revision 1.1.2.1  2008/10/14 10:13:16  kea
 Moving SEC stuff into oldserver.

 Revision 1.3  2006/06/15 16:50:10  clq2
 PAL_KEA_1612

 Revision 1.2.92.2  2006/04/21 11:54:05  kea
 Changed QueryException from a RuntimeException to an Exception.

 Revision 1.2.92.1  2006/04/19 13:57:31  kea
 Interim checkin.  All source is now compiling, using the new Query model
 where possible (some legacy classes are still using OldQuery).  Unit
 tests are broken.  Next step is to move the legacy classes sideways out
 of the active tree.

 Revision 1.2  2005/03/10 15:13:48  mch
 Seperating out fits, table and xdb servers

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.5.2.3  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.5.2.2  2004/11/24 20:59:37  mch
 doc fixes and added slinger browser

 Revision 1.5.2.1  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.5  2004/11/12 13:49:12  mch
 Fix where keyword maker might not have had keywords made

 Revision 1.4  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.3.6.1  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.3  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.2.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.2  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.1  2004/10/05 16:10:43  mch
 Merged with PAL

 Revision 1.5  2004/09/29 13:39:01  mch
 Removed obsolete ADQL 0.5

 Revision 1.4  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

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
