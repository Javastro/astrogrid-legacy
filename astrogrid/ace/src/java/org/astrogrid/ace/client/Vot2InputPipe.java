/**
 * $Id: Vot2InputPipe.java,v 1.1 2003/08/25 18:36:13 mch Exp $
 *
 * @author M Hill
 */

package org.astrogrid.ace.client;

import java.io.*;
import org.astrogrid.common.service.TempDirManager;
import org.astrogrid.log.Log;
import org.astrogrid.tools.xml.DomDumper;
import org.w3c.dom.Element;

/**
 * Takes ACE server output (a DOM element) and passes on as an input stream
 */
public class Vot2InputPipe implements VotConsumer
{
   AceConsumer aceConsumer;
   
   public Vot2InputPipe(AceConsumer givenConsumer)
   {
      this.aceConsumer = givenConsumer;
   }
   
   public void consumeAceResults(Element votResults)
   {
      Log.trace("piping results to stream...");

      // old way - create physical file
      //generate unique handle for temporary file
      try
      {
         String uniqueFilename = TempDirManager.generateUniqueHandle("","results.vot");
         
         File resultsFile = new File(uniqueFilename+"results.vot");
         OutputStream out = new FileOutputStream(resultsFile);
         Log.trace("...stream is file at "+resultsFile.toURL()+", dumping...");
         
         DomDumper.dumpNode(votResults, out);
         
         Log.trace("...calling stream consumer...");
         
         aceConsumer.consumeAceResults(new FileInputStream(resultsFile));

         // new way - pipe
      /* tricky - we need to thread it so that we can dump once the consumer
       * has started reading
       *
      PipedOutputStream pipeOut = new PipedOutputStream();
      PipedInputStream pipeIn = new PipedInputStream(pipeOut);
      
      DomDumper.dumpNode(votResults, pipeOut);
            
      Log.trace("...results piped to stream");

       aceConsumer.consumeAceResults(pipeIn); //hmmm
       */
      }
      catch (IOException ioe)
      {
         Log.logError("Failed to convert results DOM to stream", ioe);
      }

      
   }
}

/*
$Log: Vot2InputPipe.java,v $
Revision 1.1  2003/08/25 18:36:13  mch
*** empty log message ***

Revision 1.2  2003/06/26 19:14:59  mch
Passband stuff added

Revision 1.1  2003/06/18 16:01:41  mch
Removing circular dependency on Aladin, tidying up threading

 */

