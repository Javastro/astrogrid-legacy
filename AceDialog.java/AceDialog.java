/*
 AceDaliog.java

 Date         Author      Changes
 1 Nov 2002   M Hill      Created

 (c) Copyright...
 */

package org.astrogrid.ace.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.astrogrid.ui.*;
import net.mchill.log.ui.*;
import java.io.*;
import java.util.*;
import java.net.URL;
import org.astrogrid.tools.votable.*;

import org.astrogrid.ace.web.Ace;
import org.w3c.dom.Element;


import org.astrogrid.common.myspace.*;
import org.astrogrid.common.client.*;

/**
 * ...
 * A bit of a 'heavy' class at the moment... Could probably do with separating
 * out the extraction spawn process
 *
 *
 * @author M Hill
 */

public class AceDialog extends JDialog implements ItemListener, ActionListener, Runnable
{

   JTextField imageSelector = null;
   JSetFileButton imageChooserButton = null;

   JTextField configSelector = null;
   JComboBox aceSelector = null;
   JComboBox myspaceSelector = null;
   JButton okButton = null;
   JButton cancelButton = null;
   //   TemplateEditorPanel templatePanel;

   AceServers aceServers = new AceServers();
   MySpaceServers myspaceServers = new MySpaceServers();

   Element results = null;

   boolean isCancelled = true;

   Thread spawnedExtract = null;

   //   TemplateEditor templateEditor;

   /**
    * Initializes the dialog.
    */
   public AceDialog(Frame owner)
   {
      super(owner, "Astrogrid Catalogue Extractor");

      getContentPane().setLayout(new BorderLayout());

      JTabbedPane templatePanel = newTemplatePanel();
      JPanel summaryHolder = new JPanel(new BorderLayout());
      summaryHolder.add(newSummaryPanel(), BorderLayout.NORTH);
      templatePanel.add(summaryHolder, "Image", 0);
      getContentPane().add(templatePanel, BorderLayout.CENTER);

      JButton openButton = new JButton(IconFactory.getIcon("Open"));
      openButton.setToolTipText("Opens configuration file");
      openButton.addActionListener(this);

      JButton saveButton = new JButton(IconFactory.getIcon("Save"));
      saveButton.setToolTipText("Saves current configuration to file");
      saveButton.addActionListener(this);

      JButton saveAsButton = new JButton(IconFactory.getIcon("SaveAs"));
      saveAsButton.setToolTipText("Saves current configuration to given file");
      saveAsButton.addActionListener(this);

      JToolBar toolbar = new JToolBar();
      toolbar.add(openButton,0);
      toolbar.add(saveButton,1);
      toolbar.add(saveAsButton,2);
      toolbar.addSeparator();
      getContentPane().add(toolbar, BorderLayout.NORTH);

      StatusBar statusBar = new StatusBar();
      net.mchill.log.Log.addHandler(statusBar.getLogHandler());   //hmmm v dubious

      okButton = new JButton("Extract");
      okButton.addActionListener(this);
      cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(this);

      JPanel buttonPanel = new JPanel(new FlowLayout());
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);

      JPanel bottomPanel = new JPanel(new BorderLayout());
      bottomPanel.add(buttonPanel, BorderLayout.NORTH);
      bottomPanel.add(statusBar, BorderLayout.SOUTH);

      getContentPane().add(bottomPanel, BorderLayout.SOUTH);

      validate(); //relayout etc

      if (AceClientOptions.getDialogSize() != null)
      {
         setSize(AceClientOptions.getDialogSize());
      }
      if (AceClientOptions.getDialogLocation() != null)
      {
         setLocation(AceClientOptions.getDialogLocation());
      }

      new org.astrogrid.ui.EscEnterListener(this, okButton, cancelButton);
   }

   /**
    * Constructs the summary panel, which includes file locations,
    * server addresses, et
    */
   public JPanel newSummaryPanel()
   {
      FlexiGridLayout flexilayout = new FlexiGridLayout(4);
      flexilayout.setHGap(2);
      flexilayout.setVGap(10);
//      GridLayout2 flexilayout = new GridLayout2(0,4,2,10);

      JPanel summaryPanel = new JPanel(flexilayout);
      summaryPanel.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));

      // which ACE server to use
      aceSelector = new JComboBox(aceServers.getServers());
      aceSelector.addItemListener(this);
      summaryPanel.add(new JLabel("ACE Server", JLabel.RIGHT));
      summaryPanel.add(aceSelector);
      summaryPanel.add(new JButton("Add"));
      summaryPanel.add(new JLabel());  // place holder

      // where image is locally
      imageSelector = new JTextField();
      imageChooserButton = new JSetFileButton();
      imageChooserButton.setTextField(imageSelector);
      imageChooserButton.setFilter(new ExtensionFileFilter(new String[] {"fits", "FITS"},"FITS files"));
      summaryPanel.add(new JLabel("Image to extract", JLabel.RIGHT));
      summaryPanel.add(imageSelector);
      summaryPanel.add(imageChooserButton);
      summaryPanel.add(new JPasteButton(imageSelector));
//      summaryPanel.add(new JLabel());

      // where configuration template is
      configSelector = new JTextField();
      JSetFileButton configChooserButton = new JSetFileButton();
      configChooserButton.setTextField(configSelector);
      configChooserButton.setFilter(new ExtensionFileFilter(new String[] {"xml", "XML"},"ACE xml configuration files"));
      summaryPanel.add(new JLabel("Extraction Parameter Template", JLabel.RIGHT), BorderLayout.WEST);
      summaryPanel.add(configSelector, BorderLayout.CENTER);
      summaryPanel.add(configChooserButton);
      summaryPanel.add(new JPasteButton(configSelector));
//      summaryPanel.add(new JLabel());

      // where myspace/public access stuff is located
      myspaceSelector = new JComboBox(myspaceServers.getServers());
      summaryPanel.add(new JLabel("MySpace Server", JLabel.RIGHT));
      summaryPanel.add(myspaceSelector);
      summaryPanel.add(new JButton("Add"));
      summaryPanel.add(new JLabel());

      //populate with defaults
      //    if (AceClientConfig.getImageDirectory() != null)
      //      {
      //         imageChooserButton.getChooser().setCurrentDirectory(new File(AceClientConfig.getImageDirectory()));
      //      }
      if (AceClientOptions.getLastImageFilename() != null)
      {
         imageChooserButton.getChooser().setCurrentDirectory(new File(AceClientOptions.getLastImageFilename()));
         imageSelector.setText(AceClientOptions.getLastImageFilename());
      }

      if (AceClientOptions.getLastTemplateFilename() != null)
      {
         configChooserButton.getChooser().setCurrentDirectory(new File(AceClientOptions.getLastTemplateFilename()));
         configSelector.setText(AceClientOptions.getLastTemplateFilename());
      }

      if (AceClientOptions.getLastAceServer() != null)
      {
         aceSelector.setSelectedItem(AceClientOptions.getLastAceServer());
      }

      return summaryPanel;
   }


   /**
    * Constructs the template panel, for editing extraction parameters
    */
   public JTabbedPane newTemplatePanel()
   {
      TemplateEditorPanel templatePanel = new TemplateEditorPanel();

      return templatePanel;
   }

   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource() == okButton)
      {
         if (isInputValid())
         {
            spawnExtract();
         }
      }
      else if (e.getSource() == cancelButton)
      {
         isCancelled = true;
         hide();
      }
   }

   /**
    * Fix image url - used by eg Aladin where the image is being
    * supplied - ie the user cannot change it.
    */
   public void fixImageUrl(String url)
   {
      imageSelector.setText(url);
      imageSelector.setEnabled(false);
      imageChooserButton.setEnabled(false);
      myspaceSelector.setSelectedItem(MySpaceServers.URL_READONLY);
      myspaceSelector.setEnabled(false);

   }

   /**
    * Was cancelled
    */
   protected boolean wasCancelled()
   {
      return isCancelled;
   }

   /**
    * Is input valid
    */
   protected boolean isInputValid()
   {
      //whether or not we cancel, lets store box size and position
      AceClientOptions.setDialog(getSize(), getLocation());


      //check if image file exists
      //      File imagef = new File(imageSelector.getText());
      //      if (!imagef.exists())
      //      {
      //... error box
      //       log.Log.logWarning("Image file does not exist");
      //         return false;
      //      }

      //check if configuration file exists
      File conf = new File(configSelector.getText());
      if (!conf.exists())
      {
         //... error box
         org.astrogrid.log.Log.logWarning("Configuration file does not exist");
         return false;
      }

      //chect that myspace/ace servers are compatible



      return true;
   }

   /**
    * Called when the OK button is pressed with valid data. Carries out the
    * extraction on a separate thread.
    */
   protected void spawnExtract()
   {
      if (spawnedExtract == null)
      {
         spawnedExtract = new Thread(this);
         spawnedExtract.start();
      }
   }

   /**
    * Called when the OK button is pressed with valid data. Carries out the
    * extraction on a separate thread.
    */
   public void run()
   {
      try  // we must catch all exceptions here so that they get reported
      {
         okButton.setSelected(true);
         okButton.setEnabled(false);
         org.astrogrid.log.Log.logInfo("Spawned Extraction process; please wait...");
         AceClientOptions.setLastImageFilename(getImagePath());
         AceClientOptions.setLastTemplateFilename(getConfigTemplatePath());
         AceClientOptions.setLastAceServer((String) aceSelector.getSelectedItem());

         if (aceServers.isInstalled((String) aceSelector.getSelectedItem()))
         {
            runLocalSextractor();
         }
         else
         {
            runAceWebService();
         }
         if (results != null)
         {
            try
            {
               org.astrogrid.log.Log.logInfo("Preparing results for display, please wait...");
               org.astrogrid.tools.xml.DomDumper.dumpNode(getResults(), new BufferedOutputStream(new FileOutputStream("temp.vot")));
               JVotBox resultsBox = new JVotBox(null);
               resultsBox.getVotTable().loadVot(new BufferedInputStream(new FileInputStream("temp.vot")));
               resultsBox.show();
               org.astrogrid.log.Log.logInfo("Extraction complete");
            }
            catch (IOException ioe)
            {
               org.astrogrid.log.Log.logError("Could not interpret results",ioe);
            }
         }
         okButton.setSelected(false);
         okButton.setEnabled(true);
         spawnedExtract = null;
      }
      catch (Exception e)
      {
         org.astrogrid.log.Log.logError("Failed to run extraction",e);
      }
   }

   protected void runLocalSextractor()
   {
      Element domNode = org.astrogrid.test.ConfigElementLoader.loadElement(
         getConfigTemplatePath()
      );

      Ace ace = new Ace();

      results = ace.runApplicationQuickFix(domNode, getImagePath() );
   }

   protected void runAceWebService()
   {
      try
      {
         org.astrogrid.log.Log.logInfo("Resolving public access to image '"+myspaceSelector.getSelectedItem()+"', please wait...");
         //publish image to myspace (this should automatically do nothing if nothing needs done)
         MySpaceClient myspace = myspaceServers.getMySpaceClient((String)myspaceSelector.getSelectedItem());
         myspace.connect();
         String imagePath = myspace.publicise("test",getImagePath());
         myspace.disconnect();

         //load template config file as dom
         org.astrogrid.log.Log.logInfo("Assembling extraction parameters, please wait...");
         Element configDom = org.astrogrid.tools.xml.EasyDomLoader.loadElement(getConfigTemplatePath());

         //set image name
         Element imageNode = (Element) configDom.getElementsByTagName("ImageToMeasure").item(0);
         imageNode.setNodeValue(imagePath);

         //call service
         org.astrogrid.log.Log.logInfo("Calling ACE server at "+aceSelector.getSelectedItem()+", please wait...");
         ServiceClient client = new ServiceClient(getAceUri());
         Element results = client.httpPost(configDom);

      }
      catch (IOException ioe)
      {
         org.astrogrid.log.Log.logError("Failed to run Ace Server",ioe);
      }
   }
   /** Returns the path to the local copy of the selected image
    */
   public String getImagePath()
   {
      return imageSelector.getText();
   }

   /** Returns the path to the template configuration file
    */
   public String getConfigTemplatePath()
   {
      return configSelector.getText();
   }

   /** Returns the ace server
    */
   public String getAceUri()
   {
      return (String) aceServers.getUrl((String) aceSelector.getSelectedItem());
   }

   /**
    * One of the combo boxes has changed
    */
   public void itemStateChanged(ItemEvent e)
   {
      if (e.getSource() == aceSelector)
      {
         if ((e.getItem() == AceServers.INSTALLED) && (e.getStateChange() == e.SELECTED))
         {
            myspaceSelector.setSelectedItem(AceServers.LOCAL);
            myspaceSelector.setEnabled(false);
         }
         else
         {
            myspaceSelector.setEnabled(true);
         }
      }
   }

   public Element getResults()
   {
      return results;
   }

   private static ImageIcon loadIcon(String icon)
   {
      //path is subdirectory of this package, called images
      URL url = AceDialog.class.getResource("./images/"+icon+".gif");
      if (url != null)
         return new ImageIcon(Toolkit.getDefaultToolkit().getImage(url));

      return null;
   }

   /**
    * Test harness and standalone client operation
    */
   public static void main(String [] args)
   {
      org.astrogrid.ui.Splash s = new org.astrogrid.ui.Splash(
         "ACE","Astrogrid Catalogue Extractor", "v0.1",
         Color.blue.darker().darker(), Color.yellow,
         null, "MCH KEA AJWM");

      net.mchill.log.Log.addHandler(new net.mchill.log.Log2Console());
      net.mchill.log.Log.addHandler(new net.mchill.log.Log2File("client.log"));
      net.mchill.log.ui.Log2Popup popup = new net.mchill.log.ui.Log2Popup();
      popup.addFilter(new net.mchill.log.SeverityFilter(net.mchill.log.Severity.WARNING));
      net.mchill.log.Log.addHandler(popup);

      AceDialog sd = new AceDialog(null);

      JVotBox resultsBox = new JVotBox(null);
      //      sd.fixImageUrl("http://aladin.u-strasbg.fr/java/alapre-test.pl?-c=name+cdfs&out=image&fmt=JPEG&resolution=STAND&qual=GOODS+WFI-B99+____");

      s.dispose();

      do
      {
         sd.show();

         if (!sd.wasCancelled())
         {
            //show results - painful as we can't yet pass a DOM to JVot
            try
            {
               org.astrogrid.log.Log.logInfo("Preparing results for display, please wait...");
               org.astrogrid.tools.xml.DomDumper.dumpNode(sd.getResults(), new BufferedOutputStream(new FileOutputStream("temp.vot")));
               resultsBox.getVotTable().loadVot(new BufferedInputStream(new FileInputStream("temp.vot")));
               resultsBox.show();
            }
            catch (IOException ioe)
            {
               org.astrogrid.log.Log.logError("Could not reload VOTable",ioe);
            }
         }
      } while (!sd.wasCancelled());

   }



}

