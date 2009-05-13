/*
 * $Id: ImageResults.java,v 1.1.1.1 2009/05/13 13:20:25 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.security.Principal;
import org.astrogrid.dataservice.queriers.status.QuerierProcessingResults;
import org.astrogrid.io.Piper;
import org.astrogrid.query.returns.ReturnImage;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.slinger.targets.HttpResponseTarget;

/** A container interface that holds the results of a query that is an image.
 * <p>
 *
 * @author M Hill
 */

public class ImageResults
{
 
   URL imageUrl = null;
   Querier querier = null;
   
   /** Construct with a link to the Querier that spawned these results, and a
    * URL to the image*/
   public ImageResults(Querier parentQuerier, URL aUrl) {
      this.querier = parentQuerier;
      this.imageUrl = aUrl;
   }

   /** returns the formats that this result implementation can produce (ie VOTABLE, HTML, CSV, etc) */
   public static String[] getFormats() {
      return new String[] { ReturnImage.FITS  }; //not quite true, depends on what's at the end of the URL
   }
   
   /** Looks at given format and decides which output method to use */
   protected void write(Writer out, QuerierProcessingResults statusToUpdate, ReturnSpec returns) throws IOException {
      
      assert (out != null);

   }

   /** This is a helper method for plugins; it is meant to be called
    * from the askQuery method.  It transforms the results and sends them
    * as required, updating the querier status appropriately.
    */
   public void send(ReturnSpec returns, Principal user) throws IOException {

      //we have no idea what form the original image is in, so let's ignore all
      //requests for formatting and just pipe it straight from the url to the target
      
      if (returns instanceof ReturnImage) {

         if (returns.getTarget() instanceof HttpResponseTarget) {
            //in fact, if it's an http response, forward it to the URL
            ((HttpResponseTarget) returns.getTarget()).getResponse().sendRedirect(imageUrl.toString());
         }
         else {
            InputStream in = imageUrl.openStream();
            returns.getTarget().setMimeType("image");
            OutputStream out = returns.getTarget().openOutputStream();
            
            Piper.bufferedPipe(in, out);
         }
      }
      else {
         throw new UnsupportedOperationException("Unknown return type "+returns.getClass().getName()+", specify Image");
      }
   }
   


}


