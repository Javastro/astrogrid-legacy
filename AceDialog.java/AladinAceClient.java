/*
   AladinAceClient.java

   Date       Author      Changes
   4 Nov 02   M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.ace.client;

import java.net.URL;
import java.io.*;

/**
 * The ACE client for the aladin tool.
 * Instantiated by Aladin - results are fed back as a callback to the given
 * 'Aladin' class, once an extraction has been successfully completed.
 * <p>
 * Instructions from Francois Bonnarel from CDS as to how it will be used:
 * Within Aladin the call to your class should be
 * <pre>
 *    new SextractorWS(Url urlImage,Aladin a)
 * </pre>
 *  (of course you can use whatever class name you want instead of
 * SextractorWS), where urlImage is the URL of the cut out or the image on which
 * we want to reextract.
 * <p>
 * The result plane will be created by (in your class):
 * <pre>
 *    a.execCommand("get Local(urlofResult,Planename,Origin)");
 * </pre>
 * Where a  is the instance of the class Aladin provided as a parameter above,
 * and urlofResult should be the URL of the output VOTABLE with the extracted
 * catalog
 *
 * @version %I%
 * @author M Hill
 */
   
public class AladinAceClient
{
   
   /**
    * Constructor
    */
   public AladinAceClient(URL imageUrl,cds.aladin.Aladin a)
   {
      String results = extract(imageUrl);

      if (results != null)
      {
         a.execCommand("get Local("+results+",Planename,Origin)");
      }
   }
   /**/
   
   /**
    * Given the location of the image, brings up the Ace Client dialog
    * box so the user can specify extraction parameters, etc, calls the
    * extraction service, then returns a URL to the VOTable of results.  The
    * return value will be null if the user cancels the operation or there
    * has been a fault in attempting the extraction.
    *
    * If possible, this will act as the future interface to Aladdin; the
    * Aladdin code will be as follows:
    * <pre>
    *  URL results = AladinAceClient.extract(imageUrl);
    *
    *  if (results != null)
    *  {
    *     a.execCommand("get Local("+results+",Planename,Origin)");
    *  }
    * </pre>
    * No instance needs to be created and there is no double package
    * dependency.  Later it might be an idea to create plugin interfaces...
    */

   public synchronized static String extract(URL imageUrl)
   {
      AceDialog sd = new AceDialog(null);
      
      sd.fixImageUrl(""+imageUrl);
      
      sd.show();

      if (sd.wasCancelled())
      {
         sd.dispose();
         return null;
      }

      
      try
      {
         OutputStream out = new FileOutputStream("results.vot");
         org.astrogrid.tools.xml.DomDumper.dumpNode(sd.getResults(), out);
         sd.dispose();

         return "file://results.vot";
      }
      catch (IOException ioe)
      {
         org.astrogrid.log.Log.logError("Failed to store results",ioe);
      }
      return null;
   }
   
   
   /**
    * Test harness - runs Aladin
    */
   public static void main(String[] args)
   {
      cds.aladin.Aladin.main(args);
   }
}

