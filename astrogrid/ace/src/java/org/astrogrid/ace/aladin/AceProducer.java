/*
 $Id: AceProducer.java,v 1.1 2003/08/25 18:36:02 mch Exp $

 */

package org.astrogrid.ace.aladin;

import java.awt.Dialog;
import java.awt.Frame;
import java.net.URL;
import javax.swing.JOptionPane;
import org.astrogrid.ace.client.AceConsumer;
import org.astrogrid.ace.client.AceDialog;
import org.astrogrid.ace.client.ClientExtractorThread;
import org.astrogrid.ace.client.Vot2InputPipe;
import org.astrogrid.log.Log;

/**
 * Created  by AceConsumer implementations, such as Aladin.  It
 * asks the user to input extraction criteria, then spawns a thread to do
 * the actual extraction process.  Once that is complete, the thread passes
 * the results to the given AceConsumer, via a piped consumer that takes the
 * DOM element returned by the web service and converts it into an InputStream
 *
 * @author M Hill
 */

public class AceProducer
{
   AceConsumer aceConsumer;
   AceDialog sd;

   /** Class initialisation code - sets up one off logging info
    */
   static {
      net.mchill.log.Log.addHandler(new net.mchill.log.Log2Console());
      net.mchill.log.Log.addHandler(new net.mchill.log.Log2File("aceClient.log"));
      net.mchill.log.ui.Log2Popup popup = new net.mchill.log.ui.Log2Popup();
      popup.addFilter(new net.mchill.log.SeverityFilter(net.mchill.log.Severity.ERROR));
      net.mchill.log.Log.addHandler(popup);
   }

   public AceProducer(URL imageUrl, AceConsumer givenConsumer)
   {
      this.aceConsumer = givenConsumer;
      Frame ownerFrame = null;
      if (givenConsumer instanceof Frame)
      {
         ownerFrame = (Frame) givenConsumer;
      }

      //outputs the version so we can doublecheck
      Log.trace("Starting $Id: AceProducer.java,v 1.1 2003/08/25 18:36:02 mch Exp $...");

      Log.trace("Creating dialog...");

      try
      {
         Dialog pleaseWaitBox = new JOptionPane("Please wait...").createDialog(ownerFrame, "Starting ACE");
         pleaseWaitBox.setModal(false);
         pleaseWaitBox.show();

         sd = new AceDialog(ownerFrame, true);  //gives proper focus, modality, etc

         sd.getTemplateEditor().fixImage(""+imageUrl);
         sd.getTemplateEditor().setObligatoryOutputColumns(new String[] {
                  "X_WORLD",
                     "Y_WORLD",
                     "MAG_AUTO",
                     "MAGERR_AUTO",
                     "FLUX_AUTO",
                     "FLUXERR_AUTO"
               });

         Log.trace("Loading options...");

         sd.loadUserOptions();

         pleaseWaitBox.hide();   //close

         runDialog();
      }
      catch (Throwable th)
      {
         Log.logError("Failed to start ACE", th);
      }
   }


   /**
    * Given the location of the image, brings up the Ace Client dialog
    * box so the user can specify extraction parameters, etc, and if Extract
    * was pressed spawns a thread that calls the
    * extraction service,
    */

   public void runDialog()
   {
      sd.show();

      if (sd.wasCancelled())
      {
         Log.trace("...cancelled");
      }
      else
      {
         spawnExtraction();
      }
   }

   public void spawnExtraction()
   {

      Log.trace("Spawning Extraction Thread...");

      //set up a pipe consumer to absorb the results and convert them
      //to a stream
      Vot2InputPipe pipe = new AladinPiper(aceConsumer, sd.getTemplateEditor().passbandPanel);

      //spawn a thread to run the extraction - auto starts
      ClientExtractorThread extractorThread =
         new ClientExtractorThread(
         sd.getExtractionCriteria(),
         sd.getAceServer(),
         pipe
      );

      extractorThread.start();

   }

   /**
    * Test harness
    */
   public static void main(String[] args) throws Exception
   {

      cds.aladin.Aladin.main(args);

      //      new AceProducer(new URL("http://aladin.u-strasbg.fr/java/alapre-test.pl?-c=name+cdfs&out=image&fmt=JPEG&resolution=STAND&qual=GOODS+WFI-B99+____"), null);
   }
}

/*
 $Log: AceProducer.java,v $
 Revision 1.1  2003/08/25 18:36:02  mch
 *** empty log message ***

 Revision 1.5  2003/07/11 10:40:51  mch
 minor comment tidy up

 Revision 1.4  2003/07/03 18:08:34  mch
 better messaging and user info when run from Aladin

 Revision 1.3  2003/06/26 19:12:55  mch
 Added AladinPiper

 Revision 1.2  2003/06/20 11:49:45  mch
 Removed redundant classes

 Revision 1.1  2003/06/18 15:59:20  mch
 Introdueced to remove circular dependency with Aladin

 */

