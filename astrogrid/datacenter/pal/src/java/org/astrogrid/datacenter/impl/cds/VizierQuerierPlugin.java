/*$Id: VizierQuerierPlugin.java,v 1.8 2004/11/08 02:59:13 mch Exp $
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
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.impl.cds.generated.vizier.VizieR;
import org.astrogrid.datacenter.impl.cds.generated.vizier.VizieRService;
import org.astrogrid.datacenter.impl.cds.generated.vizier.VizieRServiceLocator;
import org.astrogrid.datacenter.impl.cds.vizier.VizierUnit;
import org.astrogrid.datacenter.impl.cds.vizier.VizierWavelength;
import org.astrogrid.datacenter.queriers.DefaultPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.VotableDomResults;
import org.astrogrid.datacenter.queriers.VotableInResults;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.query.Query;
import org.xml.sax.SAXException;

/** Datacenter querier SPI that performs queries against CDS Vizier webservice.
 * <p>
 * The Vizier SOAP services take the following arguments:
 *   String target - name of an object, or RA DEC, eg "12.0 23.2" in decimal degrees (can also handle sexagesimal but let's ignore that...)
 *   double radius - in units given by:
 *   double units  - not sure what's valid. @see VizierUnits for enuemeration
 *   String text - no idea.  Some kind of keyword?
 *   String wavelength - not sure what's valid.  @see VizierWavelengths for enumeration
 *
 * @see http://cdsweb.u-strasbg.fr/cdsws/vizierAccess.gml
 * @author M Hill
 */
public class VizierQuerierPlugin extends DefaultPlugin  {
   
   /** default constructor */
   public VizierQuerierPlugin() {
      super();
   }
   
   /* (non-Javadoc)
    * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#doQuery(java.lang.Object, java.lang.Class)
    */
   public void askQuery(Account user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus()));

      //extract query to specific keys
      KeywordMaker maker = new KeywordMaker();
      Hashtable keywords = maker.makeKeywords(query);

      String text = (String) keywords.get("TEXT");
      String r = (String) keywords.get(KeywordMaker.RADIUS_KEYWORD);
      if (r == null) {
         throw new IllegalArgumentException(KeywordMaker.RADIUS_KEYWORD+" must be specified in query");
      }
      double radius = 0;
      try {
         radius = Double.parseDouble(r);
      }
      catch (NumberFormatException nfe) {
         throw new IllegalArgumentException(KeywordMaker.RADIUS_KEYWORD+" has non-numeric value "+r);
      }
      
      String u = (String) keywords.get("UNIT");
      if (u == null) {
         throw new IllegalArgumentException("UNIT must be specified in query");
      }
      VizierUnit unit = VizierUnit.getFor(u);
      
      String w = (String) keywords.get("WAVELENGTH");
      VizierWavelength wavelength = null;
      if (w != null) { wavelength = VizierWavelength.getFor(w); }
      
      String target = (String) keywords.get("TARGET");
      String ra = (String) keywords.get(KeywordMaker.RA_KEYWORD);
      String dec = (String) keywords.get(KeywordMaker.DEC_KEYWORD);
      if (  ((ra == null) && (dec != null)) || ((ra != null) && (dec == null))  ) {
         throw new IllegalArgumentException("RA is "+ra+" but DEC is "+dec);
      }
      if ( (target == null) && (ra == null) ) {
         throw new IllegalArgumentException("TARGET or RA + DEC or CIRCLE must be specified in query");
      }
      if ( (target != null) && (ra != null) ) {
         throw new IllegalArgumentException("Don't specify both TARGET and RA + DEC in query");
      }
      if (target == null) {
         if (!dec.startsWith("-") && !dec.startsWith("+")) {
            dec = "+"+dec; //add sign
         }
         
         target = ra+" "+dec;
      }
      
      try {

         /* SOAP access - no good for large datasets but required for now if ra/dec given */
         if (target.indexOf(" ")>-1) {
            VizieRService service = new VizieRServiceLocator();
            VizieR vizier = service.getVizieR(new URL("http://cdsws.u-strasbg.fr/axis/services/VizieR"));
            String response;
            if (wavelength == null) {
               System.out.println("Calling vizier via SOAP...");
               response = vizier.cataloguesData(target, radius, unit.toString(), text);
            }
            else {
               System.out.println("Calling vizier (with wavelength) via SOAP...");
               response = vizier.cataloguesData(target, radius, unit.toString(), text, wavelength.toString());
            }
            System.out.println("...vizier responded");
            if (!aborted) {
               if (response == null) {
                  throw new DatacenterException("Vizier returned null");
               }
               VotableDomResults results = new VotableDomResults(querier, response);
               results.send(query.getResultsDef(), user);
            }
         }
         else {
            //but this times out....
            String url = "http://cdsws.u-strasbg.fr/axis/services/VizieR?method=cataloguesData&target="+target+"&radius="+radius+"&unit="+unit+"&text="+text;
            if (wavelength != null) {
               url = url + "&wavelength="+wavelength;
            }
            System.out.println("Calling vizier via Url "+url+"...");
            URLConnection connection = new URL(url).openConnection();
            VotableInResults results = new VotableInResults(querier, new URL(url).openStream());
            System.out.println("...vizier responded");
            results.send(query.getResultsDef(), user);
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
   
   
}


/*
 $Log: VizierQuerierPlugin.java,v $
 Revision 1.8  2004/11/08 02:59:13  mch
 Fixes to connect to Vizier

 Revision 1.7  2004/11/07 14:10:14  mch
 add pos sign to dec if not signed

 Revision 1.6  2004/11/03 05:14:33  mch
 Bringing Vizier back online

 Revision 1.5  2004/11/03 00:31:17  mch
 PAL_MCH Candidate 2 merge

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


