/*
 $Id: StandaloneAce.java,v 1.1.1.1 2003/08/25 18:36:06 mch Exp $

 (c) Copyright...
 */

package org.astrogrid.ace.client;

import java.io.*;
import javax.swing.JOptionPane;
import org.w3c.dom.Element;

import org.astrogrid.ace.client.ClientExtractorThread;
import org.astrogrid.tools.xml.DomDumper;
import org.astrogrid.log.Log;
import org.astrogrid.common.myspace.*;
import org.astrogrid.tools.votable.JVotBox;

/**
 * A thread that carries out the negotiating with the service.  Brings up
 * a VotBox to show the results
 */

public class StandaloneAce implements VotConsumer
{

   public StandaloneAce()
   {
      super();
   }

   public void run()
   {
      Log.trace("Creating dialog...");
      AceDialog sd = new AceDialog(null, true);

      sd.loadUserOptions();

      sd.show();

      if (sd.wasCancelled())
      {
         Log.trace("...cancelled");
         return;
      }

      Log.trace("Spawning Extraction Thread...");
      //spawn a thread to run the extraction - auto starts
      ClientExtractorThread extractorThread =
         new ClientExtractorThread(
            sd.getExtractionCriteria(),
            sd.getAceServer(),
            this
         );
      
      extractorThread.start();
   }

   /**
    * Called by the extractor thread when the results have been produced
    */
   public void consumeAceResults(Element results)
   {
      try
      {
         if (results == null)
         {
            throw new IOException("Null results");
         }
         
            JVotBox resultsBox = new JVotBox(null);

               //show results - painful as we can't yet pass a DOM to JVot,
               //so we have to dump it & reload it
               Log.logInfo("Preparing results for display, please wait...");
               DomDumper.dumpNode(results, new BufferedOutputStream(new FileOutputStream("temp.vot")));
               resultsBox.getVotController().loadVot(new BufferedInputStream(new FileInputStream("temp.vot")));
                resultsBox.show();
               Log.logInfo("...done");
      }

         catch (IOException ioe)
         {
            Log.logError("Could not consume results",ioe);

            JOptionPane.showMessageDialog(null, "Error in consuming results: "+ioe,
                                       "IO Failure", JOptionPane.ERROR_MESSAGE);
         }

   }

   /**
    * Test harness / standalone runner
    */
   public static void main(String[] args) throws Exception
   {
      StandaloneAce ace = new StandaloneAce();
      ace.run();
   }
   

}

/*
$Log: StandaloneAce.java,v $
Revision 1.1.1.1  2003/08/25 18:36:06  mch
Reimported to fit It02 source structure

Revision 1.1  2003/06/18 16:01:41  mch
Removing circular dependency on Aladin, tidying up threading

 */

