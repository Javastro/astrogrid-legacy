/*
 * $Id: SssImagePlugin.java,v 1.2 2006/06/15 16:50:10 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.impl.roe;

import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.Vector;
import org.astrogrid.dataservice.queriers.DefaultPlugin;
import org.astrogrid.dataservice.queriers.ImageResults;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.UrlListResults;
import org.astrogrid.dataservice.queriers.status.QuerierQuerying;
import org.astrogrid.query.Query;
import org.astrogrid.query.keyword.KeywordMaker;
import org.astrogrid.query.returns.ReturnImage;
import org.astrogrid.query.returns.ReturnSimple;
import org.astrogrid.geom.Angle;

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
   
   //requires waveband=1,2,3,etc   public static final String SSS_URL = "http://www-wfau.roe.ac.uk/~sss/cgi-bin/sss_aladin_pix.cgi?";
   public static final String SSS_URL = "http://www-wfau.roe.ac.uk/~sss/cgi-bin/sss_topcat_pix.cgi?";
   
   public SssImagePlugin() {
   }
   
   /** Constructs a list of URLs suitable for accessing the images that correspond to the given query
    */
   public void askQuery(Principal user, Query query, Querier querier) throws IOException {

      //extract query to specific keys
      KeywordMaker maker = new KeywordMaker(query);

      querier.setStatus(new QuerierQuerying(querier.getStatus(), maker.toString()));

      Angle ra = Angle.parseAngle(maker.getRequiredValue(maker.RA_KEYWORD));
      Angle dec = Angle.parseAngle(maker.getRequiredValue(maker.DEC_KEYWORD));
      Angle radius = Angle.parseAngle(maker.getRequiredValue(maker.RADIUS_KEYWORD));
      String waveband = maker.getValue("WAVEBAND");
      if (maker.getRequiredValue(maker.RADIUS_KEYWORD).toString().trim().equals("0")) {
         radius = new Angle(0.1);//default to 0.1 deg
      }
      
      if (radius.asArcMins()>7.5) {
         throw new IllegalArgumentException("Maximum Radius is 7.5 arc minutes; Radius given was "+radius.asArcMins()+" arcmins (="+radius.asDegrees()+" deg)");
      }

      Angle imageWidth = Angle.fromDegrees(radius.asDegrees()*2);
      Angle imageHeight = imageWidth;
      
      
      Vector urls = new Vector();
      //build urls to images depending on if there are any at that wavelength.
      /*
      for (int waveband = 0; waveband < 5; waveband++) {
         if (isCovered(ra, dec, waveband)) {
            urls.add(SSS_URL+"ra="+ra.asDegrees()+"&dec="+dec.asDegrees()+"&mime-type=image/x-gfits&x="+imageWidth.asArcMins()+"&y="+imageHeight.asArcMins()+"&waveband="+waveband);
         }
      }
       */

      String redImage = SSS_URL+"ra="+ra.asDegrees()+"&dec="+dec.asDegrees()+"&mime-type=image/x-gfits&x="+imageWidth.asArcMins()+"&y="+imageHeight.asArcMins()+"&waveband=red";
      String blueImage = SSS_URL+"ra="+ra.asDegrees()+"&dec="+dec.asDegrees()+"&mime-type=image/x-gfits&x="+imageWidth.asArcMins()+"&y="+imageHeight.asArcMins()+"&waveband=blue";
      
      if (query.getResultsDef().getFormat().equals(ReturnImage.FITS)) {
         //send the results back to the caller direct
         String imageUrl = blueImage;
         if ((waveband!=null) && (waveband.trim().toLowerCase().equals("red"))) {
            imageUrl = redImage;
         }
         ImageResults results = new ImageResults(querier, new URL(imageUrl));
         results.send(query.getResultsDef(), user);
      }
      else {
         //assume it's a table
         
         //Use the 'red' and 'blue' waveband form which has whole sky coverage and so is much easier
         urls.add(SSS_URL+"ra="+ra.asDegrees()+"&dec="+dec.asDegrees()+"&mime-type=image/x-gfits&x="+imageWidth.asArcMins()+"&y="+imageHeight.asArcMins()+"&waveband=red");
         urls.add(SSS_URL+"ra="+ra.asDegrees()+"&dec="+dec.asDegrees()+"&mime-type=image/x-gfits&x="+imageWidth.asArcMins()+"&y="+imageHeight.asArcMins()+"&waveband=blue");
         
         
         UrlListResults results = new UrlListResults(querier, (String[]) urls.toArray(new String[] {} ));
         results.send(query.getResultsDef(), user);
      }
   }
   
   /** Returns true if the given point is covered by the survey at the given
    * waveband */
   public boolean isCovered(Angle ra, Angle dec, int waveband) {
      return true;
   }
   

   /** Returns just the number of matches rather than the list of matches. Just does
    * an askQuery and counts the results */
   public long getCount(Principal user, Query query, Querier querier) throws IOException {
      return getCountFromResults(user, query, querier);
   }

   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return new String[] { ReturnSimple.VOTABLE, ReturnSimple.CSV, ReturnSimple.HTML, ReturnImage.FITS};
   }
   
   
}






