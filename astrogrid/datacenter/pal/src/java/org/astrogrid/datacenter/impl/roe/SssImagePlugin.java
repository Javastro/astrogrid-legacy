/*
 * $Id: SssImagePlugin.java,v 1.2 2004/11/11 23:23:29 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.impl.roe;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.impl.cds.KeywordMaker;
import org.astrogrid.datacenter.queriers.DefaultPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.UrlListResults;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.sky.Angle;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.util.DomHelper;
import org.xml.sax.SAXException;

/**
 * A plugin that returns URLs based on the query suitable for accessing the
 * SuperCOSMOS Sky Survey images.  This is mostly to provide SIAP-compliant
 * access, though obviously also you get the other interfaces for free.
 * <p>
 * The image server is a cutout server here:
 * http://www-wfau.roe.ac.uk/~sss/cgi-bin/sss_aladin_pix.cgi?
 *  @author M Hill
 */

public class SssImagePlugin extends DefaultPlugin {
   
   public static final String SSS_URL = "http://www-wfau.roe.ac.uk/~sss/cgi-bin/sss_aladin_pix.cgi?";
   
   public SssImagePlugin() {
   }
   
   /** Constructs a list of URLs suitable for accessing the images that correspond to the given query
    */
   public void askQuery(Account user, Query query, Querier querier) throws IOException {
      querier.setStatus(new QuerierQuerying(querier.getStatus()));

      //extract query to specific keys
      KeywordMaker maker = new KeywordMaker();
      maker.makeKeywords(query);

      Angle ra = Angle.parseAngle(maker.getRequiredValue(maker.RA_KEYWORD).toString());
      Angle dec = Angle.parseAngle(maker.getRequiredValue(maker.DEC_KEYWORD).toString());
      Angle imageWidth = Angle.parseAngle(maker.getRequiredValue(maker.RADIUS_KEYWORD).toString());
      Angle imageHeight = Angle.parseAngle(maker.getRequiredValue(maker.RADIUS_KEYWORD).toString());
//    String waveband = maker.getValue("WAVEBAND").toString();
//    if (waveband == null) {
//       waveband = "1";
//    }
      if (imageWidth.asArcMins()>15) {
         throw new IllegalArgumentException("Maximum Radius is 15 arc minutes");
      }
      
      //build urls to images depending on if there are any at that wavelength.
      //hmmm actually for now just assume there is everywhere
      Vector urls = new Vector();
      for (int waveband = 0; waveband < 5; waveband++) {
         if (isCovered(ra, dec, waveband)) {
            urls.add(SSS_URL+"ra="+ra.asDegrees()+"&dec="+dec.asDegrees()+"&mime-type=image/x-gfits&x="+imageWidth.asArcMins()+"&y="+imageHeight.asArcMins()+"&waveband="+waveband);
         }
      }
      
      UrlListResults results = new UrlListResults(querier, (String[]) urls.toArray(new String[] {} ));
      results.send(query.getResultsDef(), user);
   }
   
   /** Returns true if the given point is covered by the survey at the given
    * waveband */
   public boolean isCovered(Angle ra, Angle dec, int waveband) {
      return true;
   }
   

   /** Returns just the number of matches rather than the list of matches. Just does
    * an askQuery and counts the results */
   public long getCount(Account user, Query query, Querier querier) throws IOException {
      return getCountFromResults(user, query, querier);
   }

   
}




