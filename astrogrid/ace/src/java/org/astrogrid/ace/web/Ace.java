/*
   $Id: Ace.java,v 1.1 2003/08/25 18:36:20 mch Exp $

   Date        Author      Changes
   29 Oct 2002 M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.ace.web;
import org.astrogrid.ace.service.*;
import org.astrogrid.ace.sextractor.*;

import java.util.Vector;
import org.w3c.dom.Element;
import org.astrogrid.common.service.*;
import java.io.IOException;
import org.astrogrid.tools.xml.EasyDomLoader;

import org.astrogrid.log.Log;

/**
 * The application class that runs the Astronomical Extraction Service
 *.  Called by Axis to run the application; includes managing instances, etc.
 * We assume that there is one instance of Ace created for one call to the
 * Extraction service
 *
 * @version %I%
 * @author M Hill
 */

public class Ace implements ApplicationService
{

   public Ace()
   {
   }


   /**
    * The basic http-like get-return method - This blocks until SExtractor
    * completes, and returns a DOM-element containing the results.
    * At the moment, it returns just the votable, not the full results
    * document.
    */
   public synchronized Element runApplication(Element xmlNode) throws IOException
   {
      //get the parameters out of the given configuration xml element
      ParameterExtractor extractor = new ParameterExtractor(AceContext.getInstance());
      AceParameterBundle bundle = (AceParameterBundle) extractor.loadRootBundle(xmlNode);

      try
      {
         //create unique ID and temporary working directory
         TempDirManager tdm = new TempDirManager(
            AceContext.getInstance().getWorkingParent().getPath()+"/"
         );
         String tempDir = tdm.mkDir();

         //create instance of executor
         SExtractorExecutor executor = new SExtractorExecutor(
            ""+AceContext.getInstance().getApplicationPath(),
            AceContext.getInstance().getWorkingParent()+"/"+tempDir+"/",    //temp working dir
            AceContext.getInstance().getWorkingParent()+"/"+tempDir+"/",     //output
            AceContext.getInstance().getCommonDir().getPath()+"/"        //where to find common files
         );
         
         executor.setBundle(bundle);

         //execute - wait for completion
         executor.runProgram();

         //parse results
         Element results = EasyDomLoader.loadElement(executor.getVotResultsFilename());

         //add extra data - eg timestamps etc

         //tidy up - remove temporary directory
         //tdm.rmDir(tempDir);
         
         //return results
         return results;
      }
      catch (IOException ioe)
      {
         //return failure
         Log.logError("(Ace) ACE Service Error",ioe);
         //throw exception to be passed to client
         throw ioe;
         
      }
   }

   /**
    * Test harness for MCH's machine
    */
   public static void main(String[] args) throws IOException
   {
      net.mchill.log.Log.addHandler(new net.mchill.log.Log2Console());
      net.mchill.log.Log.addHandler(new net.mchill.log.Log2File(AceContext.getInstance().getWorkingParent()+"serverlog.txt"));

      Element domNode = org.astrogrid.test.ConfigElementLoader.loadElement(
         "/fannich/mch/sex/konastest.xml"
      );

      //set image?
//         "/fannich/mch/sex/r229192_red.fits"
         //"http://www.ast.cam.ac.uk/~rgm/mirrors/archive.stsci.edu/pub/hlsp/goods/h_s1b01sciv05_img.fits"
//      );
      
      Ace ace = new Ace();
   
      Element results = ace.runApplication(domNode);

      org.astrogrid.tools.xml.DomDumper.dumpNode(results, System.out);
   }

}

