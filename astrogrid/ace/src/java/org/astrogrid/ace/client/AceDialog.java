/*
 $Id: AceDialog.java,v 1.1.1.1 2003/08/25 18:36:04 mch Exp $

 Date         Author      Changes
 1 Nov 2002   M Hill      Created

 (c) Copyright...
 */

package org.astrogrid.ace.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.astrogrid.log.*;
import org.astrogrid.ui.*;
import java.io.*;
import java.util.*;
import java.net.URL;
import org.astrogrid.tools.votable.*;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import org.astrogrid.tools.xml.EasyDomLoader;
import org.astrogrid.common.myspace.*;
import org.astrogrid.common.client.*;
import org.astrogrid.ace.service.ServerSettings;
import org.astrogrid.ace.utils.NativeToXmlImpl;

//import org.astrogrid.ui.help.*;
/**
 * Offers the user an opportunity to edit the extraction criteria, select
 * the ACE server, etc.
 * This is a modal box - use wasCancelled() to test for whether the Cancel
 * button was pressed (or the window was closed); if not, use
 * getExtractionCriteria() and getAceServer() to retrieve what the use has
 * selected.
 *
 *
 * @author M Hill
 */

public class AceDialog extends JDialog implements ActionListener, AceConsumer
{

   JComboBox aceSelector = null;
   private JButton installedConfigEditBtn = null;
   private JButton serverListEditBtn = null;
      //JComboBox spaceSelector = null;
   JButton okButton = null;
   JButton cancelButton = null;

   private JButton openButton = null;
   private JButton saveButton = null;
   private JButton saveAsButton = null;
   private JButton editRawButton = null;
   //private JHelpButton helpButton = null;
   private JFileChooser configChooser = new JFileChooser();

   TemplateEditorPanel templateEditor = null;

   //MySpaceServers myspaceServers = new MySpaceServers();

   boolean isCancelled = true;

   Thread spawnedExtract = null;

   Element configDom = null;

   //name of config file being edited
   String configFilename = "";

   //name of config file to use for extraction. This is used to store the actual
   //entries in the field, which may have been edited since loading from/saving
   //to configFilename
   public static final String TEMP_CONFIG = "config.xml.$$$";

   /**
    * Initializes the dialog.
    */
   public AceDialog(Frame owner, boolean modal)
   {
      super(owner, "Astronomy Catalogue Extractor", modal);

      getContentPane().setLayout(new BorderLayout());

      //put tabbed config editor in centre
      templateEditor = new TemplateEditorPanel();
      getContentPane().add(templateEditor, BorderLayout.CENTER);

      //toolbar at top
      openButton = new JButton(IconFactory.getIcon("Open"));
      if (openButton.getIcon() == null) openButton.setText("Open");
      openButton.setToolTipText("Opens configuration file");
      openButton.addActionListener(this);

      saveButton = new JButton(IconFactory.getIcon("Save"));
      if (saveButton.getIcon() == null) saveButton.setText("Save");
      saveButton.setToolTipText("Saves current configuration to file");
      saveButton.addActionListener(this);

      saveAsButton = new JButton(IconFactory.getIcon("SaveAs"));
      if (saveAsButton.getIcon() == null) saveAsButton.setText("SaveAs");
      saveAsButton.setToolTipText("Saves current configuration to given file");
      saveAsButton.addActionListener(this);

      editRawButton = new JButton(IconFactory.getIcon("Edit"));
      if (editRawButton.getIcon() == null) editRawButton.setText("Edit");
      editRawButton.setToolTipText("Edit configuration file in text editor");
      editRawButton.addActionListener(this);

      /*
      helpButton = new JHelpButton("sex",null); //IconFactory.getIcon("Help"));
      helpButton.setToolTipText("Displays SExtractor manual");
      helpButton.addActionListener(this);

      //set up help
      if (!Help.mainBrowserSet())
      {
         Help.setMainBrowser(new JHelpBrowser());
      }
      Help.setRedirect("sex","http://www.eso.org/science/eis/eis_doc/sex2/sex2html/sex2_doc.html");
       */

      JToolBar toolbar = new JToolBar();
      toolbar.add(openButton);
      toolbar.add(saveButton);
      toolbar.add(saveAsButton);
      toolbar.addSeparator();
      toolbar.add(editRawButton);
      toolbar.addSeparator();
//      toolbar.add(helpButton); not working yet
      getContentPane().add(toolbar, BorderLayout.NORTH);

      // which ACE server to use - bottom left
      JPanel aceServerPanel = new JPanel();
      aceServerPanel.add(new JLabel("ACE Server", JLabel.RIGHT));
      aceSelector = new JComboBox(AceServerList.getServers());
      aceSelector.addActionListener(this);
      aceServerPanel.add(aceSelector);

      serverListEditBtn = new JButton(IconFactory.getIcon("Edit List"));
      if (serverListEditBtn.getIcon() == null) serverListEditBtn.setText("Edit List");
      serverListEditBtn.setToolTipText("Edit list of ACE servers");
      serverListEditBtn.addActionListener(this);
      aceServerPanel.add(serverListEditBtn);

      installedConfigEditBtn = new JButton(IconFactory.getIcon("Edit"));
      if (installedConfigEditBtn.getIcon() == null) installedConfigEditBtn.setText("Edit");
      installedConfigEditBtn.setToolTipText("Edit installed extraction server");
      installedConfigEditBtn.addActionListener(this);
      aceServerPanel.add(installedConfigEditBtn);

      // buttons - bottom rightish
      okButton = new JButton("Extract");
      okButton.addActionListener(this);
      cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(this);

      JPanel buttonPanel = new JPanel(new FlowLayout());
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);

      //status bar
//      StatusBar statusBar = new StatusBar();
//      net.mchill.log.Log.addHandler(statusBar.getLogHandler());   //hmmm v dubious

      //assemble above
      JPanel bottomPanel = new JPanel(new BorderLayout());
      bottomPanel.add(aceServerPanel, BorderLayout.WEST);
      bottomPanel.add(buttonPanel, BorderLayout.EAST);
//      bottomPanel.add(statusBar, BorderLayout.SOUTH);

      getContentPane().add(bottomPanel, BorderLayout.SOUTH);

      validate(); //relayout etc

      if (UserOptions.getDialogSize() != null)
      {
         setSize(UserOptions.getDialogSize());
      }
      else
      {
         setSize(800,400);
      }
      if (UserOptions.getDialogLocation() != null)
      {
         setLocation(UserOptions.getDialogLocation());
      }

      configChooser.addChoosableFileFilter(new ExtensionFileFilter(
         new String[] {"sex", "param"},  "SExtractor-native configuration files"
      ));
      configChooser.addChoosableFileFilter(new ExtensionFileFilter(
         new String[] {"ace", "xml", "XML"},   "ACE xml configuration files"
      ));

      //don't catch enter presses, as some input fields might want to use them,
      // and this is quite a heavy event to kick off accidently
      new org.astrogrid.ui.EscEnterListener(this, null, cancelButton);
   }



   /**
    * Sets the location, size, etc from the user options
    */
   public void loadUserOptions()
   {
      if ((UserOptions.getLastTemplateFilename() != null)
          && (UserOptions.getLastTemplateFilename().length() >0))
      {
         //this is chuking exceptions for some reason: configChooser.setCurrentDirectory(new File(UserOptions.getLastTemplateFilename()));
         loadConfig(UserOptions.getLastTemplateFilename());
      }

      if (UserOptions.getLastAceServer() != null)
      {
         aceSelector.setSelectedItem(UserOptions.getLastAceServer());
      }

   }

   /**
    * Sets the user options to the location, size, field entries, etc
    */
   public void storeUserOptions()
   {
      UserOptions.setDialog(getSize(), getLocation());

      UserOptions.setLastTemplateFilename(configFilename);
      UserOptions.setLastAceServer((String) aceSelector.getSelectedItem());
   }

   /**
    * Action perfromed - button pressed, etc
    */
   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource() == okButton)
      {
         storeUserOptions();
         if (isInputValid())
         {
            isCancelled = false;

            /*
            //kick off extraction - dodgy check to see if we're running with
            //Aladdin or not
            if (!templateEditor.isImageFixed())
            {
               //standalone mode
               Thread t = new StandaloneAce(
                  getExtractionCriteria(),
                  getAceServer()
               );
               t.start();
            }
            else
            {
               //aladin mode - just hide
               setVisible(false);
            }
             */
            setVisible(false);
         }
      }
      else if (e.getSource() == cancelButton)
      {
         isCancelled = true;
         setVisible(false);
      }
      else if (e.getSource() == openButton)
      {
         if (configChooser.showDialog(this, "Open") == JFileChooser.APPROVE_OPTION)
         {
            loadConfig(""+configChooser.getSelectedFile());
         }
      }
      else if (e.getSource() == saveAsButton)
      {
         if (configChooser.showDialog(this, "Save") == JFileChooser.APPROVE_OPTION)
         {
            templateEditor.saveXmlFile(configChooser.getSelectedFile());
            setConfigFilename(""+configChooser.getSelectedFile());
         }
      }
      else if (e.getSource() == saveButton)
      {
         templateEditor.saveXmlFile(new File(configFilename));
      }
      else if (e.getSource() == editRawButton)
      {
         TextEditor textEditor = new TextEditor(this, "Editor");
         textEditor.setModal(true);
         textEditor.setText(templateEditor.toXmlString());
         textEditor.show();
         if (!textEditor.wasCancelled())
         {
            templateEditor.loadXmlString(textEditor.getText());
         }
      }
      else if (e.getSource() == aceSelector)
      {
         installedConfigEditBtn.setEnabled(
            AceServerList.isLocal( (String) aceSelector.getSelectedItem())
         );
      }
      else if (e.getSource() == serverListEditBtn)
      {
         TextEditor textEditor = new TextEditor(this, "Editor");
         textEditor.setModal(true);

         File f = new File(AceServerList.FILENAME);
         if (!f.exists())
         {
            AceServerList.setSoftwiredDefaults();
            AceServerList.store();
         }

         textEditor.loadFile(new File(AceServerList.FILENAME));
         textEditor.show();
         if (!textEditor.wasCancelled())
         {
            textEditor.saveFile(new File(AceServerList.FILENAME));
            try
            {
               AceServerList.load();
            }
            catch (IOException ioe)
            {
               Log.logError("Could not reload config", ioe);
            }
            aceSelector.setModel(new DefaultComboBoxModel(AceServerList.getServers()));
         }
      }
      else if (e.getSource() == installedConfigEditBtn)
      {
         TextEditor textEditor = new TextEditor(this, "Editor");
         textEditor.setModal(true);

         File f = new File(ServerSettings.FILE_NAME);
         if (!f.exists())
         {
            ServerSettings.setDefaults();
            ServerSettings.getInstance().store();
         }

         textEditor.loadFile(new File(ServerSettings.FILE_NAME));
         textEditor.show();
         if (!textEditor.wasCancelled())
         {
            textEditor.saveFile(new File(ServerSettings.FILE_NAME));
            try
            {
               ServerSettings.getInstance().load();
            }
            catch (IOException ioe)
            {
               Log.logError("Could not reload config", ioe);
            }
         }
      }
   }

   /**
    * Set configuration file.  sets title.
    */
   public void setConfigFilename(String filename)
   {
      setTitle("ACE "+filename);

      configFilename = filename;
   }

   /**
    * load configuration file - Loads up fields, sets title, etc
    */
   public void loadConfig(String filename)
   {
      try
      {
         //look at type
         if (filename.toLowerCase().endsWith(".sex"))
         {
            //file is native SExtractor configuration file
            String xmlFilename = filename+".xml.$$$";
            Log.trace("Converting native (sex) SExtractor '"+filename+"' to '"+xmlFilename+"'...");

            FileWriter xmlWriter = new FileWriter(xmlFilename);
            NativeToXmlImpl.startXml(xmlWriter);
            NativeToXmlImpl.makeSexXml(new FileReader(filename), xmlWriter);
            NativeToXmlImpl.endXml(xmlWriter);

            Log.trace("...done convert");

            filename = xmlFilename;
         }
         else if (filename.toLowerCase().endsWith(".param"))
         {
            //file is native SExtractor param (output column) file
            String xmlFilename = filename+".xml.$$$";
            Log.trace("Converting native (param) SExtractor '"+filename+"' to '"+xmlFilename+"'...");

            NativeToXmlImpl.makeParamXml(new FileReader(filename), new FileWriter(xmlFilename));

            Log.trace("...done convert");

            filename = xmlFilename;
         }

         // don't actually validate on reading as we may
         //be loading parts of a file


         //load into template editor
         templateEditor.loadXmlFile(new File(filename));

      }
      catch (IOException e)
      {
         Log.logWarning(null,"Could not load '"+filename+"' ",e);
         return;
      }

      //update UI
      setConfigFilename(filename);
   }


   /**
    * Was cancelled
    */
   public boolean wasCancelled()
   {
      return isCancelled;
   }

   /**
    * Is input valid.  Write out options and validate against schewa
    */
   protected boolean isInputValid()
   {
      try
      {
         createConfigDom();
      }
      catch (IOException ioe)
      {
         Log.logWarning(null,"Input Fields generate invalid extraction config file",ioe);
         JOptionPane.showMessageDialog(
            this,
            "The XML file generated from these entries generates an error:\n"+ioe,
            "Input Error",
            JOptionPane.ERROR_MESSAGE
         );
         return false;
      }

      //check that myspace/ace servers are compatible



      return true;
   }

   /**
    * Returns the results of all the editing - a DOM doc fitting the ACE input
    * schema with the values set from the fields
    */
   public Element getExtractionCriteria()
   {
      try
      {
//       if (configDom == null) do it anyway for the moment so we don't have to worry about changes resetting it
         {
            configDom = createConfigDom();
         }
      }
      catch (IOException ioe)
      {
         Log.logError("Could not create criteria from entries",ioe);
      }
      return configDom;
   }


   /**
    * Creates and returns the configuration that has been entered into the dialog as
    * a Dom document.  All file locations are as given - no effort is made
    * to check to see if they are visible to the service.  The file is
    * validated against the schema
    */
   protected Element createConfigDom() throws IOException
   {
      /**
      //for the moment, work off the template until Alan's save is OK
      org.astrogrid.xmlutils.XmlValidatorIfc validator = new org.astrogrid.xmlutils.XmlValidatorXercesImpl();
      try
      {
         validator.validate(""+configFilename);
      }
      catch (SAXException e)
      {
         IOException ioe = new IOException("Invalid temp config file '"+configFilename+": "+e);
         ioe.setStackTrace(e.getStackTrace());
         throw ioe;
      }
      return EasyDomLoader.loadElement(configFilename);
       **/

      //need to validate it, so save it first to the temp location
      File tempFile = new File(TEMP_CONFIG);
      templateEditor.saveXmlFile(tempFile);

      //validate it
      Log.trace("Validating '"+tempFile+"'...");
      org.astrogrid.xmlutils.XmlValidatorXercesImpl validator = new org.astrogrid.xmlutils.XmlValidatorXercesImpl();
      try
      {
         validator.validate(""+tempFile);
      }
      catch (SAXException e)
      {
         IOException ioe = new IOException("WebService document invalid '"+tempFile+": "+e);
         ioe.setStackTrace(e.getStackTrace());  //java v1.4+
         //ioe.fillInStackTrace(); //java v1.3
         throw ioe;
      }
      Log.trace("...config file valid");

      //load template config file as dom
      return EasyDomLoader.loadElement(""+tempFile);
      /**/
   }


   /** Returns the ace server string corresponding to the key in the server list
    */
   public String getAceServer()
   {
      return (String) aceSelector.getSelectedItem();
   }

   /**
    * Exposes the template editor panel
    */
   public TemplateEditorPanel getTemplateEditor()
   {
      return templateEditor;
   }

   private static ImageIcon loadIcon(String icon)
   {
      //path is subdirectory of this package, called images
      URL url = AceDialog.class.getResource("./images/"+icon+".gif");
      if (url != null)
         return new ImageIcon(Toolkit.getDefaultToolkit().getImage(url));

      return null;
   }

   public void consumeAceResults(InputStream results)
   {
      try
      {
         JVotBox votBox = new JVotBox(null);
         votBox.getVotController().loadVot(results);
         votBox.show();
      }
      catch (IOException ioe)
      {
         Log.logError("Loading "+results, ioe);
      }
   }


   /**
    * Test harness and standalone client operation
    */
   public static void main(String [] args)
   {
      org.astrogrid.ui.Splash splash = new org.astrogrid.ui.Splash(
         "ACE","Astronomy Catalogue Extractor", "v0.3",
         Color.blue.darker().darker(), Color.yellow,
         null, "MCH KEA AJWM");

      net.mchill.log.Log.addHandler(new net.mchill.log.Log2Console());
      net.mchill.log.Log.addHandler(new net.mchill.log.Log2File("client.log"));
      net.mchill.log.ui.Log2Popup popup = new net.mchill.log.ui.Log2Popup();
//      popup.setLocation(AceClientOptions.getDialogLocation().x- (AceClientOptions.getDialogSize().width/2),
//                        AceClientOptions.getDialogLocation().y- (AceClientOptions.getDialogSize().height/2));
      Point p = UserOptions.getDialogLocation();
      if (p != null)
      {
         popup.setLocation(p.x, p.y);
      }

      popup.addFilter(new net.mchill.log.SeverityFilter(net.mchill.log.Severity.ERROR));
      net.mchill.log.Log.addHandler(popup);

      AceDialog sd = new AceDialog(null, true);
      sd.loadUserOptions();

      //      sd.fixImageUrl("http://aladin.u-strasbg.fr/java/alapre-test.pl?-c=name+cdfs&out=image&fmt=JPEG&resolution=STAND&qual=GOODS+WFI-B99+____");

      splash.dispose();

      sd.show();

      if (sd.wasCancelled())
      {
         Log.trace("...cancelled");
      }
      else
      {
        Log.trace("Spawning Extraction Thread...");

      //should set up a JVotBox as a consumer really...
      Vot2InputPipe pipe = new org.astrogrid.ace.aladin.AladinPiper(sd, sd.getTemplateEditor().passbandPanel);

      //spawn a thread to run the extraction - auto starts
      ClientExtractorThread extractorThread =
         new ClientExtractorThread(
            sd.getExtractionCriteria(),
            sd.getAceServer(),
            pipe
         );

      extractorThread.start();
      }
   }



}



/*
 $Log: AceDialog.java,v $
 Revision 1.1.1.1  2003/08/25 18:36:04  mch
 Reimported to fit It02 source structure

 Revision 1.20  2003/07/11 10:42:08  mch
 Better trapping & reporting of invalid XML webdoc

 Revision 1.19  2003/07/02 19:18:49  mch
 Fix for initial size being v small

 Revision 1.18  2003/06/26 19:14:04  mch
 Passband stuff added

 Revision 1.17  2003/06/18 15:54:43  mch
 Reintegrated with Aladin on JDK1.4, removed circular dependency

 Revision 1.16  2002/12/21 12:09:20  mch
 Added button to edit server config

 Revision 1.15  2002/12/18 12:58:19  mch
 Imported from MCHs laptop after Dec AVO Demo meeting in Cambridge

 Revision 1.14  2002/12/15 12:58:21  mch
 Removed help

 Revision 1.13  2002/12/14 20:17:55  mch
 Removed auto load of user options

 Revision 1.12  2002/12/13 16:35:07  mch
 added help, more validation

 Revision 1.11  2002/12/09 22:49:08  mch
 New threaded Aladin interface

 Revision 1.10  2002/12/06 16:33:32  mch
 Testing Log keyword

 */


