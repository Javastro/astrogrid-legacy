/*
 $Id: ClientExtractorThread.java,v 1.1.1.1 2003/08/25 18:36:05 mch Exp $
 */

package org.astrogrid.ace.client;

import java.io.*;

import javax.swing.ProgressMonitor;
import javax.swing.JOptionPane;

import org.w3c.dom.Element;

import org.astrogrid.ace.client.AceServerList;
import org.astrogrid.ace.web.Ace;

import org.astrogrid.common.client.ServiceClient;
import org.astrogrid.common.service.TempDirManager;
import org.astrogrid.tools.xml.DomDumper;

import org.astrogrid.ui.UserCancelledException;

import org.astrogrid.log.Log;

import org.astrogrid.common.myspace.*;

/**
 * Takes extraction criteria, and carries out the extraction on a remote or
 * local extractor as required, returning results to the given consumer.
 * Displays a progress box
 */

public class ClientExtractorThread extends Thread
{
   protected Element extractionCriteria;
   protected String aceServer;
   protected ProgressMonitor progressMonitor;
   protected VotConsumer votConsumer;
//   protected Element results;

   public ClientExtractorThread(Element givenExtractionCriteria, String givenAceServer, VotConsumer givenConsumer)
   {
      this.extractionCriteria = givenExtractionCriteria;
      this.aceServer = givenAceServer;
      this.votConsumer = givenConsumer;

      progressMonitor = new ProgressMonitor(
            null,
            "Extracting catalogue...",
            "",
            0,3
      );

      progressMonitor.setMillisToDecideToPopup(0);
      progressMonitor.setMillisToPopup(0);

   }

   /**
    * Carries out extraction, whether local or remote. keeping the results
    * as an XML tree (see getResults)
    */
   public void run()
   {
      progressMonitor.setNote("Preparing to extract");
      progressMonitor.setProgress(0);

      try
      {
         if (progressMonitor.isCanceled())
            throw new UserCancelledException();

         Element results = null;

         if (AceServerList.isLocal(aceServer))
         {
            Ace ace = new Ace();

            progressMonitor.setNote("Running local Extractor");
            progressMonitor.setProgress(1);

            results = ace.runApplication(extractionCriteria);
         }
         else
         {
            //publish image(s) etc to myspace (this should automatically do nothing if nothing needs done)
            //             publishFiles(configDom);

            progressMonitor.setNote("Calling ACE server");
            progressMonitor.setProgress(1);

            //call service
            Log.logInfo("Calling ACE server at "+aceServer+", please wait...");

            ServiceClient client = new ServiceClient(AceServerList.getUrl(aceServer));

            if (progressMonitor.isCanceled())
               throw new UserCancelledException();

            results = client.httpPost(extractionCriteria);
         }

            if (progressMonitor.isCanceled())
               throw new UserCancelledException();

            progressMonitor.setNote("Examining Results");
            progressMonitor.setProgress(2);

            votConsumer.consumeAceResults(results);
      }
      catch (IOException ioe)
      {
         Log.logError("Service Failed",ioe);

         JOptionPane.showMessageDialog(null, "ACE Service Failed: "+ioe,
                                       "ACE Failure", JOptionPane.ERROR_MESSAGE);
      }
      catch (UserCancelledException uce)
      {
         Log.logInfo("User Cancelled");
      }
      catch (Throwable e) //report any other messages
      {
         Log.logError("Application Error",e);

         JOptionPane.showMessageDialog(null, "ACE Application Error: "+e,
                                       "ACE Application Failure", JOptionPane.ERROR_MESSAGE);
      }

      progressMonitor.setProgress(progressMonitor.getMaximum()+1);   //close

   }

   /**
    * Looks through DOM for files that might need publishing to a public area
    * visible to the web service.  Updates the references within the DOM to
    * point to these new public areas
    *
   protected void publishFiles(Element configDom) throws IOException
   {
      //Look through all the files given and make sure they will be visible
      //to the service.
      MySpaceClient myspace = myspaceServers.getMySpaceClient((String) spaceSelector.getSelectedItem());
      myspace.connect();

      publishAFile(configDom, "ImageToMeasure", myspace);
      publishAFile(configDom, "ImageToCatalog", myspace);

      //disconnect from myspace
      myspace.disconnect();
   }

   protected void publishAFile(Element configDom, String tag, MySpaceClient mySpace) throws IOException
   {
      Log.logInfo("Resolving public access for '"+tag+"', please wait...");

      Element node = (Element) configDom.getElementsByTagName(tag).item(0);

      if (node == null)
      {
         Log.logInfo("...no tag found");
         return;
      }

      node = (Element) node.getElementsByTagName("arg").item(0);

      String givenPath = node.getFirstChild().getNodeValue();

      //publish image(s) etc to myspace (this should automatically do nothing if nothing needs done)
      String publicPath = mySpace.publicise("test",givenPath);

      Log.logInfo("...resolved to "+publicPath);
      node.setNodeValue(publicPath);
   }
    /**/

}

/*
$Log: ClientExtractorThread.java,v $
Revision 1.1.1.1  2003/08/25 18:36:05  mch
Reimported to fit It02 source structure

Revision 1.4  2003/07/11 10:42:44  mch
Thread now stops if user presses cancel

Revision 1.3  2003/07/02 19:19:23  mch
Catches & reports any throwable error

Revision 1.2  2003/06/26 19:14:37  mch
Minor change to progress monitor

Revision 1.1  2003/06/18 16:01:41  mch
Removing circular dependency on Aladin, tidying up threading

*/

