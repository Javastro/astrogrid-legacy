/*$Id: AsuQuerierPlugin.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.impl.cds;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.security.Principal;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.queriers.DefaultPlugin;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierPluginFactory;
import org.astrogrid.dataservice.queriers.RawPipeResults;
import org.astrogrid.dataservice.queriers.status.QuerierQuerying;
import org.astrogrid.io.MonitoredInputStream;
import org.astrogrid.io.StreamProgressListener;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.sql.SqlParser;
import org.astrogrid.slinger.targets.WriterTarget;
import org.xml.sax.SAXException;

/** Datacenter querier SPI that performs queries against ASU-compatible services -
 * which might only be CDS's Vizier data at the moment.
 * <p>
 * @see http://vizier.u-strasbg.fr/doc/asu.html
 * <p>
 * @author M Hill
 */
public class AsuQuerierPlugin extends DefaultPlugin
{
   //note bug on Vizier that -mime doesn't seem to work at "http://vizier.u-strasbg.fr/cgi-bin/VizieR" - just returns html
   //every time.  Sebastian says use the appropriate script
   public final static String DEFAULT_ASU_URL = "http://vizier.u-strasbg.fr/cgi-bin/votable";
   
  
   /** default constructor */
   public AsuQuerierPlugin() throws IOException {
      super();
   }
   
   /* Runs Query on an ASU server
    */
   public void askQuery(Principal user, Query query, final Querier querier) throws IOException {

      String stemurl = ConfigFactory.getCommonConfig().getString("datacenter.asuplugin.dataurl", DEFAULT_ASU_URL);

      AsuTwigMaker twigmaker = new AsuTwigMaker();
      query.acceptVisitor(twigmaker);
      
      String url = stemurl+twigmaker.getAsuTwig();
      
      querier.setStatus(new QuerierQuerying(querier.getStatus(), url));
      querier.getStatus().addDetail("Calling vizier via Url "+url+"...");
      
      log.info("Querying using "+url);
      
//    URLConnection connection = new URL(url).openConnection();
      InputStream in = new URL(url).openStream();
      //create quick little stream listener to update status as the stream runs
      StreamProgressListener listener = new StreamProgressListener() {
         public void setStreamProgress(long bytesRead) {
            querier.getStatus().setProgress(bytesRead);
         }
         public void setStreamClosed() { }
      };
      
      in = new MonitoredInputStream(in, 1000, listener);
      
      RawPipeResults results = new RawPipeResults(querier, in, query.getResultsDef().getFormat());
      
      /*
      //try each of the vizier services until one works
      QueryResults results = null;
      int site=0;
      while ((results == null) && (site<mirrorUrls.length) && (!aborted)) {
         try {
//            results = useSoap(mirrorUrls[site], querier, target, radius, unit, text, wavelength);
            results = useHttp(mirrorUrls[site], querier, target, radius, unit, text, wavelength);
         }
         catch (IOException ioe) {
            querier.getStatus().addDetail(ioe+" accessing "+mirrorUrls[site]);
         }
         site++;
      }
       */
      if (aborted) return;
      
      if (results != null) {
         results.send(query.getResultsDef(), user);
      } else {
         throw new IOException("No results from any of the Vizier services (see query status for details)\n");
      }

   }
   
   
   /** Returns just the number of matches rather than the list of matches. Since there's no way to do this yet
    * directly with Vizier (I don't think?), we just do a normal query then count the rows */
   public long getCount(Principal user, Query query, Querier querier) throws IOException {
      String stemurl = ConfigFactory.getCommonConfig().getString("datacenter.asuplugin.dataurl", DEFAULT_ASU_URL);

      AsuTwigMaker twigmaker = new AsuCountTwigMaker();
      query.acceptVisitor(twigmaker);
      
      String url = stemurl+twigmaker.getAsuTwig();
      
      querier.setStatus(new QuerierQuerying(querier.getStatus(), url));
      querier.getStatus().addDetail("Calling vizier via Url "+url+"...");
      
      log.info("Querying using "+url);
      
//    URLConnection connection = new URL(url).openConnection();
      DataInputStream in = new DataInputStream(new URL(url).openStream());
      
      /*
      //try each of the vizier services until one works
      QueryResults results = null;
      int site=0;
      while ((results == null) && (site<mirrorUrls.length) && (!aborted)) {
         try {
//            results = useSoap(mirrorUrls[site], querier, target, radius, unit, text, wavelength);
            results = useHttp(mirrorUrls[site], querier, target, radius, unit, text, wavelength);
         }
         catch (IOException ioe) {
            querier.getStatus().addDetail(ioe+" accessing "+mirrorUrls[site]);
         }
         site++;
      }
       */
      return in.readLong();
   }

   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return new String[] { MimeTypes.HTML, MimeTypes.FITS, MimeTypes.VOTABLE, MimeTypes.TSV };
   }

   
   
   
   /** Quick & dirty tester
    *
    */
   public static void main(String[] args) throws IOException, QueryException, ParserConfigurationException, IOException, SAXException
   {
      ConfigFactory.getCommonConfig().setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY,"org.astrogrid.datacenter.impl.cds.AsuQuerierPlugin");

      {
         StringWriter sw = new StringWriter();
         Query query = SqlParser.makeQuery(
            "SELECT * FROM ELAIS WHERE CIRCLE('J2000',7.5,-42,1)",
            new WriterTarget(sw),
            "VOTABLE");
         Querier querier = Querier.makeQuerier(LoginAccount.ANONYMOUS, query, "dirtytest");
         querier.ask();
         System.out.println(sw.toString());
      }

      {
         StringWriter sw = new StringWriter();
         Query query = SimpleQueryMaker.makeConeQuery(20,30,6, new WriterTarget(sw));
         Querier querier = Querier.makeQuerier(LoginAccount.ANONYMOUS, query, "dirtytest");
         querier.ask();
         System.out.println(sw.toString());
      }
      
      {
         StringWriter sw = new StringWriter();
         Query query = SqlParser.makeQuery(
            "SELECT ELAIS.Jmag, ELAIS.Kmag FROM ELAIS WHERE CIRCLE('J2000',7.5,-42,1) AND ((ELAIS.Jmag >= 5 OR ELAIS.Kmag > 5) )",//OR ELAIS.COOKED LIKE 'TRUE')",
            new WriterTarget(sw),
            "VOTABLE");
         Querier querier = Querier.makeQuerier(LoginAccount.ANONYMOUS, query, "dirtytest");
         querier.ask();
   
         System.out.println(sw.toString());
      }
   }
}


/*
 $Log: AsuQuerierPlugin.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/20 15:23:08  kea
 Checking old sources in in oldserver directory (rather than just
 deleting them, might still be useful).

 Revision 1.3  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.2.16.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.2  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.4  2005/01/13 18:57:31  mch
 Fixes to metadata mostly

 Revision 1.1.2.3  2004/12/10 12:37:13  mch
 Cone searches to look in metadata, lots of metadata interpreterrs

 */





