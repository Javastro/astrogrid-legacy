/*
 * $Id: TemplateEditorPanel.java,v 1.1 2003/08/25 18:36:13 mch Exp $
 *
 * See end for log
 */

package org.astrogrid.ace.client;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import org.astrogrid.ui.*;
import org.astrogrid.fits.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.common.myspace.MySpaceResolver;
import org.astrogrid.intensity.Passband;
import org.astrogrid.log.Log;
import org.astrogrid.tools.xml.XmlOutput;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A subclass of JPanel that provides a complete tabbed-pane type
 * dialog box with all the configuration parameters needed for the
 * AVO Demo. (This class can be used anywhere a JPanel would
 * normally go, allowing flexible placement/display.)
 *
 * Used by AVODemoDialog.java to display the tabs for a dialog box...
 *
 * @author Alan Maxwell
 */
public class TemplateEditorPanel extends JTabbedPane
{

   // -- AJWM's MAIN -------------------------------------------------------------

   // This main is needed to test the returned XML string!

   public static void main(String[] args)
   {
      final JFrame frame = new JFrame("AVODemoDialog - Test");
      frame.setSize(300, 300);
      frame.setLocation(100, 100);

      frame.addWindowListener(
         new WindowAdapter()
         {
            public void windowClosing(WindowEvent we) {
               System.exit(0);
            }
         }
      );

      final TemplateEditorPanel templateEditorPanel = new TemplateEditorPanel();
      final JTextPane output = new JTextPane();


      JButton goButton = new JButton("Test AVODemo Dialog Panel >>");
      goButton.addActionListener( new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                  JDialog dialog = new JDialog(frame, "AVODemoDialogPanel - Test", true);

                  dialog.setSize(480,520);
                  dialog.getContentPane().add(templateEditorPanel);
                  dialog.setVisible(true);

                  output.setText(templateEditorPanel.toXmlString());
               }
            });

      frame.getContentPane().setLayout(new BorderLayout());
      frame.getContentPane().add(new JScrollPane(output), BorderLayout.CENTER);
      frame.getContentPane().add(goButton, BorderLayout.SOUTH);
      frame.setVisible(true);
   };

   // ----------------------------------------------------------------------------

   // -- MCH's MAIN --------------------------------------------------------------
   /*

    // This main does not allow easy testing of the XML strings...

    public static void main(String[] args)
    {
    JDialog dialog = new JDialog( (Frame) null, "AVODemoDialogPanel - Test", true);

    dialog.setSize(480,520);
    dialog.getContentPane().add(
    new TemplateEditorPanel()
    );
    dialog.setVisible(true);
    };
    */
   // ----------------------------------------------------------------------------

   public static final int DEF_BUTTON_HEIGHT = 22;
   public static final int DEF_BUTTON_WIDTH = 100;

   public static final int DEF_TEXTFIELD_HEIGHT = 20;
   public static final int DEF_TEXTFIELD_WIDTH = 200;

   public static final Dimension DEF_BUTTON_DIMENSION =
      new Dimension(DEF_BUTTON_WIDTH, DEF_BUTTON_HEIGHT);
   public static final Dimension DEF_TEXTFIELD_DIMENSION =
      new Dimension (DEF_TEXTFIELD_WIDTH, DEF_TEXTFIELD_HEIGHT);
   public static final Dimension DEF_HALF_TEXTFIELD_DIMENSION =
      new Dimension ((DEF_TEXTFIELD_WIDTH / 2) - 20, DEF_TEXTFIELD_HEIGHT);

   public static final int DEF_PADDING = 3;

   public static final Insets DEF_INSETS =
      new Insets(DEF_PADDING, DEF_PADDING, DEF_PADDING, DEF_PADDING);

   public static final Insets DEF_VSPACE_INSETS =
      new Insets(DEF_PADDING + DEF_TEXTFIELD_HEIGHT, DEF_PADDING, DEF_PADDING, DEF_PADDING);

   //set automatically public static final String DEF_SET_FILE_BUTTON_TEXT = "..";
   //set automatically public static final String DEF_SET_FILE_BUTTON_TOOLTIP_TEXT =
   //  "Choose a filename...";

   GridBagConstraints constraints = new GridBagConstraints();

   //=== IMAGE TAB ===========================================================
   JTextField imageTextField = null;
   JSetFileButton imageChooserButton = null;
   JPasteButton imagePasteButton = null;
   JButton imageHeaderButton = null;

   JCheckBox dualImageToggle = null;
   JTextField dualImageTextField = null;
   JSetFileButton dualImageChooserButton = null;
   JPasteButton dualImagePasteButton = null;

   ButtonGroup radiobuttongroupDetectType = null;
   JRadioButton radiobuttonDetectTypeCCD = null;
   JRadioButton radiobuttonDetectTypePHOTO = null;

   //JComboBox passbandUcdPicker = null;
   public PassbandSpecifierPanel passbandPanel = null;

   JButton imageHeaderViewerBtn = null;

   boolean imageFixed = false; //set if the image has been 'fixed' to a certain valude

   // == EXTRACTION TAB ======================================================

   JLabel labelFlagImage = null;
   JTextField textFlagImage = null;

   JLabel labelDetMinArea = null;
   JIntegerField integertextDetMinArea = null;

   JLabel labelDetThresh = null;
   JDoubleField doubletextDetThresh1 = null;
   JDoubleField doubletextDetThresh2 = null;

   JLabel labelAnalysisThresh = null;
   JDoubleField doubletextAnalysisThresh1 = null;
   JDoubleField doubletextAnalysisThresh2 = null;

   JCheckBox checkApplyFilter = null;
   JComboBox textFilterName = null;

   JLabel labelDeblendThresh = null;
   JIntegerField integertextDeblendThresh = null;

   JLabel labelDeblendMinCont = null;
   JDoubleField doubletextDeblendMinCont = null;

   JCheckBox checkCleaning = null;
   JDoubleField doubletextCleaning = null;

   JLabel labelMaskType = null;
   JPanel panelMaskType = null;
   ButtonGroup radiobuttongroupMaskType = null;
   JRadioButton radiobuttonMaskTypeCORRECT = null;
   JRadioButton radiobuttonMaskTypeBLANK = null;
   JRadioButton radiobuttonMaskTypeNONE = null;

   // == PHOTOMETRY TAB ======================================================
   JLabel labelPhotApertures = null;
   JIntegerField inttextPhotApertures = null;

   JLabel labelPhotAutoParams = null;
   JDoubleField doubletextPhotAutoParams1 = null;
   JDoubleField doubletextPhotAutoParams2 = null;

   JLabel labelSatLevel = null;
   JDoubleField doubletextSatLevel = null;

   JLabel labelMagZero = null;
   JDoubleField doubletextMagZero = null;

   JLabel labelMagGamma = null;
   JDoubleField doubletextMagGamma = null;

   JLabel labelGain = null;
   JDoubleField doubletextGain = null;

   JLabel labelPixelScale = null;
   JDoubleField doubletextPixelScale = null;

   // == STAR_GALAXY_SEPARATION TAB ======================================================
   JLabel labelSeeingFWHM = null;
   JDoubleField doubletextSeeingFWHM = null;

   JLabel labelStarNNWName = null;
   JTextField textStarNNWName = null;

   // == BACKGROUND TAB ======================================================
   JLabel labelBackSize = null;
   JIntegerField integertextBackSize1 = null;
   JIntegerField integertextBackSize2 = null;

   JLabel labelBackFilter = null;
   JIntegerField integertextBackFilter1 = null;
   JIntegerField integertextBackFilter2 = null;

   JLabel labelBackPhotoType = null;
   JPanel panelBackPhotoType = null;
   ButtonGroup radiobuttongroupBackPhotoType = null;
   JRadioButton radiobuttonBackPhotoTypeGLOBAL = null;
   JRadioButton radiobuttonBackPhotoTypeLOCAL = null;

   JLabel labelBackPhotoThick = null;
   JIntegerField inttextBackPhotoThick = null;

   JCheckBox checkApplyPixFlag = null;
   JTextField textApplyPixFlag = null;

   // == OUTPUT TAB =====================================================
   JLabel  labelCheckImageType = null;
   JComboBox comboCheckImageType = null;

   JMutableList listOutputColumns = null;
   JMutableList listOutputColumnsSelected = null;

   JButton buttonOutputColumnsADD = null;
   JButton buttonOutputColumnsREMOVE = null;

   Vector obligatoryOutputColumns = new Vector();

   static final String [] itemsOutputColumns =
   {
      "FLUX_ISO",
         "MAG_ISO",
         "MAGERR_ISO",
         "FLUX_ISOCOR",
         "FLUXERR_ISOCOR",
         "MAG_ISOCOR",
         "MAGERR_ISOCOR",
         //         "FLUX_APER(-)",
         //         "FLUXERR_APER(-)",
         //         "MAG_APER(-)",
         //         "MAGERR_APER(-)",
         "MAG_AUTO",
         "MAGERR_AUTO",
         "FLUX_AUTO",
         "FLUXERR_AUTO",
         "FLUX_BEST",
         "FLUXERR_BEST",
         "MAG_BEST",
         "MAGERR_BEST",
         "KRON_RADIUS",
         "BACKGROUND",
         "THRESHOLD",
         "MU_THRESHOLD",
         "FLUX_MAX",
         "MU_MAX",
         "ISOAREA_IMAGE",
         "ISOAREA_WORLD",
         "XMIN_IMAGE",
         "YMIN_IMAGE",
         "XMAX_IMAGE",
         "YMAX_IMAGE",
         "X_WORLD",
         "Y_WORLD",
         "ALPHA_SKY",
         "DELTA_SKY",
         "ALPHA_J2000",
         "DELTA_J2000",
         "ALPHA_B1950",
         "DELTA_B1950",
         "X2_IMAGE",
         "Y2_IMAGE",
         "XY_IMAGE",
         "X2_WORLD",
         "Y2_WORLD",
         "XY_WORLD",
         "CXX_IMAGE",
         "CYY_IMAGE",
         "CXY_IMAGE",
         "CXX_WORLD",
         "CYY_WORLD",
         "CXY_WORLD",
         "A_IMAGE",
         "B_IMAGE",
         "A_WORLD",
         "B_WORLD",
         "THETA_IMAGE",
         "THETA_WORLD",
         "THETA_SKY",
         "THETA_J2000",
         "THETA_B1950",
         "ELONGATION",
         "ELLIPTICITY",
         "ERRX2_IMAGE",
         "ERRY2_IMAGE",
         "ERRXY_IMAGE",
         "ERRX2_WORLD",
         "ERRY2_WORLD",
         "ERRXY_WORLD",
         "ERRCXX_IMAGE",
         "ERRCYY_IMAGE",
         "ERRCXY_IMAGE",
         "ERRCXX_WORLD",
         "ERRCYY_WORLD",
         "ERRCXY_WORLD",
         "ERRA_IMAGE",
         "ERRB_IMAGE",
         "ERRA_WORLD",
         "ERRB_WORLD",
         "ERRTHETA_IMAGE",
         "ERRTHETA_WORLD",
         "ERRTHETA_SKY",
         "ERRTHETA_J2000",
         "ERRTHETA_B1950",
         "FWHM_IMAGE",
         "FWHM_WORLD",
         "ISO0",
         "ISO1",
         "ISO2",
         "ISO3",
         "ISO4",
         "ISO5",
         "ISO6",
         "ISO7",
         //         "IMAFLAGS_ISO(-)",
         //         "NIMAFLAGS_ISO(-)",
         "CLASS_STAR",
         //         "VIGNET(-,-)"
   };


   // == ADVANCED TAB =====================================================
   JLabel labelMemObjStack = null;
   JIntegerField inttextMemObjStack = null;

   JLabel labelMemPixStack = null;
   JIntegerField inttextMemPixStack = null;

   JLabel labelMemBufSize = null;
   JIntegerField inttextMemBufSize = null;

   JLabel labelVerboseType = null;
   JPanel panelVerboseType = null;
   ButtonGroup radiobuttongroupVerboseType = null;
   JRadioButton radiobuttonVerboseTypeNORMAL = null;
   JRadioButton radiobuttonVerboseTypeQUIET = null;
   JRadioButton radiobuttonVerboseTypeFULL = null;



   /**
    * Constructs a JPanel with a set of tabbed pages depicting all of the options
    * needed for the AVO Demo...(options are initialised to be blank or a
    * sensible default if a checkbox/button).
    *
    * @author Alan Maxwell
    */
   public TemplateEditorPanel()
   {
      // Call the JPanel's constructor first...
      super();

      // Setup constraints that are common to all items...
      constraints.insets = DEF_INSETS;
      constraints.ipadx = DEF_PADDING;
      constraints.ipady = DEF_PADDING;

      // Now we put together the user interface...

      // == IMAGE TAB ======================================================
      //describes the image
      JPanel imagePanel = newPanel();
      constraints.gridy = 0;

      // image to catalog
      setLabelConstraints(constraints);
      imagePanel.add(new JLabel("Image to extract", JLabel.RIGHT), constraints);

      imageTextField = new JTextField();
      imageTextField.setToolTipText("Pixel image that will be examined for objects and measured");
      /* not entirely happy the whole updateFromImage thing by the user
       imageTextField.addKeyListener(new KeyListener() {

       public void keyReleased(KeyEvent e)  { }
       public void keyTyped(KeyEvent e)    { }

       // Invoked when a key has been pressed.
       public void keyPressed(KeyEvent e)
       {
       if (e.getKeyCode() == e.VK_ENTER)
       {
       updateFromImageHeader();
       }
       }
       });
       /**/

      setEntryConstraints(constraints);
      imagePanel.add(imageTextField, constraints);

      imageChooserButton = new JSetFileButton();
      imageChooserButton.addChoosableFilter(new ExtensionFileFilter(new String[] {"fits", "FITS"},"FITS files"));
      imageChooserButton.setTextField(imageTextField);
      setControlConstraints(constraints);
      imagePanel.add(imageChooserButton, constraints);

      //adds another action listener, which should be called after the image text
      //field has been updated, to update the image information
      //beware that this might be called *before* the text field is updated
      /*
      imageChooserButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e)
               {
                  if (imageChooserButton.getChosenFile() != null)
                  {
                     setImage(imageChooserButton.getChosenFile().getAbsolutePath());
                  }
               }
            });
       */

      imagePasteButton = new JPasteButton(imageTextField);
      constraints.gridx++;
      imagePanel.add(imagePasteButton, constraints);

      imageHeaderButton = new JButton("Ex");
      imageHeaderButton.setToolTipText("Auto-examines FITS header");
      imageHeaderButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e)
               {
                  setImage(imageTextField.getText());
               }
            });
      constraints.gridx = constraints.RELATIVE;
      imagePanel.add(imageHeaderButton, constraints);

//    imageHeaderViewerBtn = new JButton("View");
//    imageHeaderViewerBtn.setToolTipText("View FITS header");


      //---- Dual Image mode -----
      constraints.gridy++;  //new line
      setLabelConstraints(constraints);

      dualImageToggle = new JCheckBox("Dual Image mode", false);
      //      constraints.anchor = constraints.EAST;
      imagePanel.add(dualImageToggle, constraints);

      dualImageToggle.addActionListener( new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                  setDualImageMode(dualImageToggle.isSelected());
               }
            });

      setEntryConstraints(constraints);
      dualImageTextField = new JTextField();
      dualImageTextField.setToolTipText("In dual image mode, fluxes will be returned for THIS image, for objects found in the ABOVE image");
      imagePanel.add(dualImageTextField, constraints);

      dualImageChooserButton = new JSetFileButton();
      dualImageChooserButton.setTextField(dualImageTextField);
      dualImageChooserButton.addChoosableFilter(new ExtensionFileFilter(new String[] {"fits", "FITS"},"FITS files"));
      setControlConstraints(constraints);
      imagePanel.add(dualImageChooserButton, constraints);

      dualImagePasteButton = new JPasteButton(dualImageTextField);
      constraints.gridx = constraints.RELATIVE;
      imagePanel.add(dualImagePasteButton, constraints);

      /*
       // header viewer button - reads & displays image header if it's FITS, and
       // updates other input fields if poss
       constraints.gridy++;  //new line

       imageHeaderViewerBtn = new JButton("Examine Header");
       setControlConstraints(constraints);
       constraints.gridwidth = constraints.REMAINDER;
       imagePanel.add(imageHeaderViewerBtn, constraints);

       imageHeaderViewerBtn.addActionListener( new ActionListener()
       {
       public void actionPerformed(ActionEvent ae)
       {
       readImageHeader();
       }
       }
       );
       */
      setDualImageMode(false);

      // what type is it - ccd or photo
      constraints.gridy++;  //new line
      setControlConstraints(constraints);
      constraints.gridwidth=constraints.REMAINDER;
      JPanel panelDetectType = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelDetectType.setToolTipText("Type of detection image [DETECT_TYPE]");

      radiobuttongroupDetectType = new ButtonGroup();

      radiobuttonDetectTypeCCD = new JRadioButton("CCD");
      radiobuttonDetectTypeCCD.setToolTipText(panelDetectType.getToolTipText());
      radiobuttonDetectTypeCCD.setActionCommand("CCD");
      radiobuttongroupDetectType.add(radiobuttonDetectTypeCCD);
      panelDetectType.add(radiobuttonDetectTypeCCD);

      radiobuttonDetectTypePHOTO = new JRadioButton("Photo");
      radiobuttonDetectTypePHOTO.setToolTipText(panelDetectType.getToolTipText());
      radiobuttonDetectTypePHOTO.setActionCommand("PHOTO");
      radiobuttongroupDetectType.add(radiobuttonDetectTypePHOTO);
      panelDetectType.add(radiobuttonDetectTypePHOTO);

      radiobuttongroupDetectType.setSelected(
         radiobuttonDetectTypeCCD.getModel(), true
      );

      setEntryConstraints(constraints);
      imagePanel.add(panelDetectType, constraints);

      //what wavelength band.  At the moment, pick from a list of UCDs - later,
      //we might want to specify wavelength/frequency and bandwidth; later still,
      //filter charecteristics.  These may be derived from the image...
      constraints.gridy++;

      setLabelConstraints(constraints);
      //      constraints.anchor = constraints.NORTHEAST;
      imagePanel.add(new JLabel("Passband", JLabel.RIGHT),constraints);

      passbandPanel = new PassbandSpecifierPanel();
      setEntryConstraints(constraints);
      constraints.gridheight = 2;
      constraints.insets = new Insets(0,8,0,5); //don't know why we need this here...
      imagePanel.add(passbandPanel, constraints);
      constraints.insets = null;

      /**
       passbandUcdPicker = new JComboBox(new String[]  {
       "PHOT_MAG",
       "PHOT_MAG_U",
       "PHOT_MAG_B",
       "PHOT_MAG_V",
       "PHOT_MAG_R",
       "PHOT_MAG_I",
       "PHOT_MAG_J",
       "PHOT_MAG_K",
       });
       setEntryConstraints(constraints);
       imagePanel.add(passbandUcdPicker, constraints);
       passbandUcdPicker.setSelectedItem("PHOT_MAG");
       **/

      // ------------------------------------------------------------------------


      // == EXTRACTION TAB ======================================================
      // Create all items for the panel...

      labelFlagImage = new JLabel("Flag Image:  ");
      textFlagImage = new JTextField("");
      textFlagImage.setToolTipText(
         "The filename of an input flag image [FLAG_IMAGE]"
      );
      sizeComponent(textFlagImage, DEF_TEXTFIELD_DIMENSION);

      JSetFileButton fbFlagImage = new JSetFileButton(textFlagImage);

      labelDetMinArea = new JLabel("Detect Min. Area:  ");
      integertextDetMinArea = new JIntegerField();
      integertextDetMinArea.setToolTipText(
         "Minimum number of pixels above threshold [DETECT_MINAREA]"
      );
      sizeComponent(integertextDetMinArea, DEF_TEXTFIELD_DIMENSION);

      labelDetThresh = new JLabel("Detect Threshold:  ");

      doubletextDetThresh1 = new JDoubleField();
      doubletextDetThresh1.setToolTipText(
         "<sigmas> or <threshold> [DETECT_THRESH]"
      );
      sizeComponent(doubletextDetThresh1, DEF_HALF_TEXTFIELD_DIMENSION);

      doubletextDetThresh2 = new JDoubleField();
      doubletextDetThresh2.setToolTipText(
         "<ZP> in mag.arcsec-2 [DETECT_THRESH]"
      );
      sizeComponent(doubletextDetThresh2, DEF_HALF_TEXTFIELD_DIMENSION);

      labelAnalysisThresh = new JLabel("Analysis Threshold:  ");
      doubletextAnalysisThresh1 = new JDoubleField();
      doubletextAnalysisThresh1.setToolTipText(
         "<sigmas> or <threshold> [ANALYSIS_THRESH]"
      );
      sizeComponent(doubletextAnalysisThresh1, DEF_HALF_TEXTFIELD_DIMENSION);
      doubletextAnalysisThresh2 = new JDoubleField();
      doubletextAnalysisThresh2.setToolTipText(
         "<ZP> in mag.arcsec-2 [ANALYSIS_THRESH]"
      );
      sizeComponent(doubletextAnalysisThresh2, DEF_HALF_TEXTFIELD_DIMENSION);

      checkApplyFilter = new JCheckBox("  Apply Detection Filter:", true);
      checkApplyFilter.setActionCommand("APPLYFILTER");
      ToggleButtonControllerListener listenerApplyFilter =
         new ToggleButtonControllerListener();
      checkApplyFilter.addActionListener(listenerApplyFilter);
      checkApplyFilter.setToolTipText(
         "Apply filter On[Y]/Off[N] [FILTER]"
      );
      String [] filterNames = {
         "default.conv", "block_3x3.conv", "gauss_1.5_3x3.conv", "gauss_2.0_3x3.conv",
            "gauss_2.0_5x5.conv", "gauss_2.5_5x5.conv", "gauss_3.0_5x5.conv",
            "gauss_3.0_7x7.conv", "gauss_4.0_7x7.conv", "gauss_5.0_9x9.conv",
            "mexhat_1.5_5x5.conv", "mexhat_2.0_7x7.conv", "mexhat_2.5_7x7.conv",
            "mexhat_3.0_9x9.conv", "mexhat_4.0_9x9.conv", "mexhat_5.0_11x11.conv",
            "tophat_1.5_3x3.conv", "tophat_2.0_3x3.conv", "tophat_2.5_3x3.conv",
            "tophat_3.0_3x3.conv", "tophat_4.0_5x5.conv", "tophat_5.0_5x5.conv"
      };

      textFilterName = new JComboBox(filterNames);
      textFilterName.setEditable(true);
      textFilterName.setToolTipText(
         "The filename of a detection filter to apply [FILTER_NAME]"
      );
      textFilterName.setSelectedItem("");
      sizeComponent(textFilterName, DEF_TEXTFIELD_DIMENSION);
      // Auto-hide of text field, dependant on check box!
      listenerApplyFilter.addComponent(textFilterName);

      JSetFileButton fbFilterName = new JSetFileButton();
      fbFilterName.setComboBox(textFilterName);
      listenerApplyFilter.addComponent(fbFilterName);

      labelDeblendThresh = new JLabel("Deblending # Sub-Thresholds:  ");
      integertextDeblendThresh = new JIntegerField();
      integertextDeblendThresh.setToolTipText(
         "Number of deblending sub-thresholds [DEBLEND_NTHRESH]"
      );
      sizeComponent(integertextDeblendThresh, DEF_TEXTFIELD_DIMENSION);

      labelDeblendMinCont = new JLabel("Deblending Min. Contrast:  ");
      doubletextDeblendMinCont = new JDoubleField();
      doubletextDeblendMinCont.setToolTipText(
         "Minimum contrast parameter for deblending [DEBLEND_MINCONT]"
      );
      sizeComponent(doubletextDeblendMinCont, DEF_TEXTFIELD_DIMENSION);

      checkCleaning = new JCheckBox("  Clean with Efficiency:", true);
      checkCleaning.setActionCommand("CLEANING");
      ToggleButtonControllerListener listenerCleaning =
         new ToggleButtonControllerListener();
      checkCleaning.addActionListener(listenerCleaning);
      checkCleaning.setToolTipText(
         "Apply cleaning On[Y]/Off[N] [CLEAN]"
      );
      doubletextCleaning = new JDoubleField();
      doubletextCleaning.setToolTipText(
         "Cleaning efficiency for spurious detections [CLEAN_PARAM]"
      );
      sizeComponent(doubletextCleaning, DEF_TEXTFIELD_DIMENSION);

      // Auto-hide of text field, dependant on check box!
      listenerCleaning.addComponent(doubletextCleaning);

      labelMaskType = new JLabel("Mask Type:  ");
      panelMaskType = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelMaskType.setToolTipText(
         "Type of detection MASKing [MASK_TYPE]"
      );
      radiobuttongroupMaskType = new ButtonGroup();
      radiobuttonMaskTypeCORRECT =
         new JRadioButton("Correct");
      radiobuttonMaskTypeCORRECT.setActionCommand("CORRECT");
      radiobuttonMaskTypeCORRECT.setToolTipText(
         panelMaskType.getToolTipText()
      );
      radiobuttongroupMaskType.add(radiobuttonMaskTypeCORRECT);
      panelMaskType.add(radiobuttonMaskTypeCORRECT);
      radiobuttonMaskTypeBLANK =
         new JRadioButton("Blank");
      radiobuttonMaskTypeBLANK.setActionCommand("BLANK");
      radiobuttonMaskTypeBLANK.setToolTipText(
         panelMaskType.getToolTipText()
      );
      radiobuttongroupMaskType.add(radiobuttonMaskTypeBLANK);
      panelMaskType.add(radiobuttonMaskTypeBLANK);
      radiobuttonMaskTypeNONE =
         new JRadioButton("None");
      radiobuttonMaskTypeNONE.setActionCommand("NONE");
      radiobuttonMaskTypeNONE.setToolTipText(
         panelMaskType.getToolTipText()
      );
      radiobuttongroupMaskType.add(radiobuttonMaskTypeNONE);
      panelMaskType.add(radiobuttonMaskTypeNONE);
      radiobuttongroupMaskType.setSelected(
         radiobuttonMaskTypeCORRECT.getModel(), true
      );

      // Now assemble the contents into the tabbed panel...
      JPanel panelContentsExtraction = new JPanel(new GridBagLayout());

      addGB(
         panelContentsExtraction, labelFlagImage,
         0, 1, GridBagConstraints.EAST, false, 1
      );

      addGB(
         panelContentsExtraction, textFlagImage,
         1, 1, GridBagConstraints.WEST, false, 2
      );

      addGB(
         panelContentsExtraction, fbFlagImage,
         3, 1, GridBagConstraints.WEST, false, 1
      );

      addGB(
         panelContentsExtraction, labelDetMinArea,
         0, 2, GridBagConstraints.EAST, false, 1
      );

      addGB(
         panelContentsExtraction, integertextDetMinArea,
         1, 2, GridBagConstraints.WEST, false, 2
      );

      addGB(
         panelContentsExtraction, labelDetThresh,
         0, 3, GridBagConstraints.EAST, false, 1
      );

      addGB(
         panelContentsExtraction, doubletextDetThresh1,
         1, 3, GridBagConstraints.WEST, false, 1
      );

      addGB(
         panelContentsExtraction, doubletextDetThresh2,
         2, 3, GridBagConstraints.WEST, false, 1
      );

      addGB(
         panelContentsExtraction, labelAnalysisThresh,
         0, 4, GridBagConstraints.EAST, false, 1
      );

      addGB(
         panelContentsExtraction, doubletextAnalysisThresh1,
         1, 4, GridBagConstraints.WEST, false, 1
      );

      addGB(
         panelContentsExtraction, doubletextAnalysisThresh2,
         2, 4, GridBagConstraints.WEST, false, 1
      );

      addGB(
         panelContentsExtraction, checkApplyFilter,
         0, 5, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsExtraction, textFilterName,
         1, 5, GridBagConstraints.WEST, true, 2
      );

      addGB(
         panelContentsExtraction, fbFilterName,
         3, 5, GridBagConstraints.WEST, true, 1
      );

      addGB(
         panelContentsExtraction, labelDeblendThresh,
         0, 6, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsExtraction, integertextDeblendThresh,
         1, 6, GridBagConstraints.WEST, true, 2
      );

      addGB(
         panelContentsExtraction, labelDeblendMinCont,
         0, 7, GridBagConstraints.EAST, false, 1
      );

      addGB(
         panelContentsExtraction, doubletextDeblendMinCont,
         1, 7, GridBagConstraints.WEST, false, 2
      );

      addGB(
         panelContentsExtraction, checkCleaning,
         0, 8, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsExtraction, doubletextCleaning,
         1, 8, GridBagConstraints.WEST, true, 2
      );

      addGB(
         panelContentsExtraction, labelMaskType,
         0, 9, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsExtraction, panelMaskType,
         1, 9, GridBagConstraints.WEST, true, 2
      );

      JPanel panelExtraction = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelExtraction.add(panelContentsExtraction);
      // ------------------------------------------------------------------------


      // == PHOTOMETRY TAB ======================================================
      // Create all items for the panel...
      labelPhotApertures = new JLabel("Photo Aperture(s):  ");
      inttextPhotApertures = new JIntegerField();
      inttextPhotApertures.setToolTipText(
         "MAG_APER aperture diameter(s) in pixels [PHOT_APERTURES]"
      );
      sizeComponent(inttextPhotApertures, DEF_TEXTFIELD_DIMENSION);

      labelPhotAutoParams = new JLabel("Photo Auto Parameters:  ");
      doubletextPhotAutoParams1 = new JDoubleField();
      doubletextPhotAutoParams1.setToolTipText(
         "MAG_AUTO parameter: <Kron_fact> [PHOT_AUTOPARAMS]"
      );
      sizeComponent(doubletextPhotAutoParams1, DEF_HALF_TEXTFIELD_DIMENSION);
      doubletextPhotAutoParams2 = new JDoubleField();
      doubletextPhotAutoParams2.setToolTipText(
         "MAG_AUTO parameter: <min_radius> [PHOT_AUTOPARAMS]"
      );
      sizeComponent(doubletextPhotAutoParams2, DEF_HALF_TEXTFIELD_DIMENSION);

      labelSatLevel = new JLabel("Saturation Level:  ");
      doubletextSatLevel = new JDoubleField();
      doubletextSatLevel.setToolTipText(
         "Level (in ADUs) at which arises saturation [SATUR_LEVEL]"
      );
      sizeComponent(doubletextSatLevel, DEF_TEXTFIELD_DIMENSION);

      labelMagZero = new JLabel("Magnitude Zero-point:  ");
      doubletextMagZero = new JDoubleField();
      doubletextMagZero.setToolTipText(
         "Megnitude zero-point [MAG_ZEROPOINT]"
      );
      sizeComponent(doubletextMagZero, DEF_TEXTFIELD_DIMENSION);

      labelMagGamma = new JLabel("Gamma of Emulsion:  ");
      doubletextMagGamma = new JDoubleField();
      doubletextMagGamma.setToolTipText(
         "Gamma of emulsion (for photographic scans) [MAG_GAMMA]"
      );
      sizeComponent(doubletextMagGamma, DEF_TEXTFIELD_DIMENSION);

      labelGain = new JLabel("Detector Gain:  ");
      doubletextGain = new JDoubleField();
      doubletextGain.setToolTipText(
         "Detector gain in e-/ADU [GAIN]"
      );
      sizeComponent(doubletextGain, DEF_TEXTFIELD_DIMENSION);

      labelPixelScale = new JLabel("Pixel Scale:  ");
      doubletextPixelScale = new JDoubleField();
      doubletextPixelScale.setToolTipText(
         "Size of pixel in arcsec (0 = use FITS info) [PIXEL_SCALE]"
      );
      sizeComponent(doubletextPixelScale, DEF_TEXTFIELD_DIMENSION);

      // Now assemble the contents into the tabbed panel...
      JPanel panelContentsPhotometry = new JPanel(new GridBagLayout());

      addGB(
         panelContentsPhotometry, labelPhotApertures,
         0, 0, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsPhotometry, inttextPhotApertures,
         1, 0, GridBagConstraints.WEST, true, 2
      );

      addGB(
         panelContentsPhotometry, labelPhotAutoParams,
         0, 1, GridBagConstraints.EAST, false, 1
      );

      addGB(
         panelContentsPhotometry, doubletextPhotAutoParams1,
         1, 1, GridBagConstraints.WEST, false, 1
      );

      addGB(
         panelContentsPhotometry, doubletextPhotAutoParams2,
         2, 1, GridBagConstraints.WEST, false, 1
      );

      addGB(
         panelContentsPhotometry, labelSatLevel,
         0, 2, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsPhotometry, doubletextSatLevel,
         1, 2, GridBagConstraints.WEST, true, 2
      );

      addGB(
         panelContentsPhotometry, labelMagZero,
         0, 3, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsPhotometry, doubletextMagZero,
         1, 3, GridBagConstraints.WEST, true, 2
      );

      addGB(
         panelContentsPhotometry, labelMagGamma,
         0, 4, GridBagConstraints.EAST, false, 1
      );

      addGB(
         panelContentsPhotometry, doubletextMagGamma,
         1, 4, GridBagConstraints.WEST, false, 2
      );

      addGB(
         panelContentsPhotometry, labelGain,
         0, 5, GridBagConstraints.EAST, false, 1
      );

      addGB(
         panelContentsPhotometry, doubletextGain,
         1, 5, GridBagConstraints.WEST, false, 2
      );

      addGB(
         panelContentsPhotometry, labelPixelScale,
         0, 6, GridBagConstraints.EAST, false, 1
      );

      addGB(
         panelContentsPhotometry, doubletextPixelScale,
         1, 6, GridBagConstraints.WEST, false, 2
      );

      JPanel panelPhotometry = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelPhotometry.add(panelContentsPhotometry);
      // ------------------------------------------------------------------------


      // == STAR_GALAXY_SEPARATION TAB ======================================================
      // Create all items for the panel...
      labelSeeingFWHM = new JLabel("Seeing FWHM:  ");
      doubletextSeeingFWHM = new JDoubleField();
      doubletextSeeingFWHM.setToolTipText(
         "Stellar Full-Width-Half-Maximum (in arcsec) [SEEING_FWHM]"
      );
      sizeComponent(doubletextSeeingFWHM, DEF_TEXTFIELD_DIMENSION);

      labelStarNNWName = new JLabel("Neural Net Weight File:  ");
      textStarNNWName = new JTextField();
      textStarNNWName.setToolTipText(
         "The filename of the Neural-Network_Weight table [STARNNW_NAME]"
      );
      sizeComponent(textStarNNWName, DEF_TEXTFIELD_DIMENSION);

      JSetFileButton fbStarNNWName = new JSetFileButton(textStarNNWName);

      // Now assemble the contents into the tabbed panel...
      JPanel panelContentsStar_Galaxy_Separation = new JPanel(new GridBagLayout());

      addGB(
         panelContentsStar_Galaxy_Separation, labelSeeingFWHM,
         0, 0, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsStar_Galaxy_Separation, doubletextSeeingFWHM,
         1, 0, GridBagConstraints.WEST, true, 2
      );

      addGB(
         panelContentsStar_Galaxy_Separation, labelStarNNWName,
         0, 1, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsStar_Galaxy_Separation, textStarNNWName,
         1, 1, GridBagConstraints.WEST, true, 2
      );

      addGB(
         panelContentsStar_Galaxy_Separation, fbStarNNWName,
         3, 1, GridBagConstraints.WEST, true, 1
      );

      JPanel panelStar_Galaxy_Separation = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelStar_Galaxy_Separation.add(panelContentsStar_Galaxy_Separation);

      // ------------------------------------------------------------------------


      // == BACKGROUND TAB ======================================================
      // Create all items for the panel...
      labelBackSize = new JLabel("Background mesh:  ");
      integertextBackSize1 = new JIntegerField();
      integertextBackSize1.setToolTipText(
         "<size> or <width> [BACKSIZE]"
      );
      sizeComponent(integertextBackSize1, DEF_HALF_TEXTFIELD_DIMENSION);
      integertextBackSize2 = new JIntegerField();
      integertextBackSize2.setToolTipText(
         "<height> [BACKSIZE]"
      );
      sizeComponent(integertextBackSize2, DEF_HALF_TEXTFIELD_DIMENSION);

      labelBackFilter = new JLabel("Background filter:  ");
      integertextBackFilter1 = new JIntegerField();
      integertextBackFilter1.setToolTipText(
         "<size> or <width> [BACK_FILTERSIZE]"
      );
      sizeComponent(integertextBackFilter1, DEF_HALF_TEXTFIELD_DIMENSION);
      integertextBackFilter2 = new JIntegerField();
      integertextBackFilter2.setToolTipText(
         "<height> [BACK_FILTERSIZE]"
      );
      sizeComponent(integertextBackFilter2, DEF_HALF_TEXTFIELD_DIMENSION);

      labelBackPhotoType = new JLabel("Photo Type:  ");
      panelBackPhotoType = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelBackPhotoType.setToolTipText(
         "Background photo type [BACKPHOTO_TYPE]"
      );
      radiobuttongroupBackPhotoType = new ButtonGroup();
      radiobuttonBackPhotoTypeGLOBAL =
         new JRadioButton("Global");
      radiobuttonBackPhotoTypeGLOBAL.setActionCommand("GLOBAL");
      radiobuttonBackPhotoTypeGLOBAL.setToolTipText(
         panelBackPhotoType.getToolTipText()
      );
      radiobuttongroupBackPhotoType.add(radiobuttonBackPhotoTypeGLOBAL);
      panelBackPhotoType.add(radiobuttonBackPhotoTypeGLOBAL);
      radiobuttonBackPhotoTypeLOCAL =
         new JRadioButton("Local");
      radiobuttonBackPhotoTypeLOCAL.setActionCommand("LOCAL");
      radiobuttonBackPhotoTypeLOCAL.setToolTipText(
         panelBackPhotoType.getToolTipText()
      );
      radiobuttongroupBackPhotoType.add(radiobuttonBackPhotoTypeLOCAL);
      panelBackPhotoType.add(radiobuttonBackPhotoTypeLOCAL);
      radiobuttongroupBackPhotoType.setSelected(
         radiobuttonBackPhotoTypeGLOBAL.getModel(), true
      );

      labelBackPhotoThick = new JLabel("Background thickness:  ");
      inttextBackPhotoThick = new JIntegerField();
      inttextBackPhotoThick.setToolTipText(
         "Thickness of the background LOCAL annulus [BACKPHOTO_THICK]"
      );
      sizeComponent(inttextBackPhotoThick, DEF_TEXTFIELD_DIMENSION);

      checkApplyPixFlag = new JCheckBox("  Apply Pixel Flag:", true);
      checkApplyPixFlag.setActionCommand("PIXELFLAG");
      ToggleButtonControllerListener listenerApplyPixFlag =
         new ToggleButtonControllerListener();
      checkApplyPixFlag.addActionListener(listenerApplyPixFlag);
      checkApplyPixFlag.setToolTipText(
         "Apply Pixel Flag On[Y]/Off[N] [**PARAMETER_NAME_HERE**]"
      );
      textApplyPixFlag = new JTextField();
      textApplyPixFlag.setToolTipText(
         "**TOOLTIP TEXT TO BE DEFINED HERE** [**PARAMETER_NAME_HERE**]"
      );
      // Link this text field with one on the Extraction tab...
      textApplyPixFlag.setDocument(
         textFlagImage.getDocument()
      );
      sizeComponent(textApplyPixFlag, DEF_TEXTFIELD_DIMENSION);

      // Auto-hide of text field, dependant on check box!
      listenerApplyPixFlag.addComponent(textApplyPixFlag);

      // Now assemble the contents into the tabbed panel...
      JPanel panelContentsBackground = new JPanel(new GridBagLayout());

      addGB(
         panelContentsBackground, labelBackSize,
         0, 0, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsBackground, integertextBackSize1,
         1, 0, GridBagConstraints.WEST, true, 1
      );

      addGB(
         panelContentsBackground, integertextBackSize2,
         2, 0, GridBagConstraints.WEST, true, 1
      );

      addGB(
         panelContentsBackground, labelBackFilter,
         0, 1, GridBagConstraints.EAST, false, 1
      );

      addGB(
         panelContentsBackground, integertextBackFilter1,
         1, 1, GridBagConstraints.WEST, false, 1
      );

      addGB(
         panelContentsBackground, integertextBackFilter2,
         2, 1, GridBagConstraints.WEST, false, 1
      );

      addGB(
         panelContentsBackground, labelBackPhotoType,
         0, 2, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsBackground, panelBackPhotoType,
         1, 2, GridBagConstraints.WEST, true, 2
      );

      addGB(
         panelContentsBackground, labelBackPhotoThick,
         0, 3, GridBagConstraints.EAST, false, 1
      );

      addGB(
         panelContentsBackground, inttextBackPhotoThick,
         1, 3, GridBagConstraints.WEST, false, 2
      );

      addGB(
         panelContentsBackground, checkApplyPixFlag,
         0, 4, GridBagConstraints.EAST, true, 1
      );
      addGB(
         panelContentsBackground, textApplyPixFlag,
         1, 4, GridBagConstraints.WEST, true, 2
      );

      JPanel panelBackground = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelBackground.add(panelContentsBackground);

      // ------------------------------------------------------------------------

      // == OUTPUT TAB ======================================================
      // Create all items for the panel...

      labelCheckImageType = new JLabel("Check Image Type:  ");
      String[] itemsCheckImageType =
      {
         "NONE", "MINIBACKGROUND", "BACKGROUND", "-BACKGROUND", "OBJECTS",
            "-OJBECTS", "SEGMENTATION", "APERTURES", "FILTERED"
      };
      comboCheckImageType = new JComboBox(itemsCheckImageType);
      comboCheckImageType.setSelectedIndex(0);
      comboCheckImageType.setToolTipText(
         "[CHECKIMAGE_TYPE]"
      );
      sizeComponent(comboCheckImageType, DEF_TEXTFIELD_DIMENSION);

      JLabel labelOutput = new JLabel("Output Columns:  ");

      JLabel labelOutputColumns = new JLabel("Available");
      listOutputColumns = new JMutableList();
      JScrollPane scrollOutputColumns =
         new JScrollPane(listOutputColumns);

      JLabel labelOutputColumnsSelected = new JLabel("Selected");
      listOutputColumnsSelected = new JMutableList();
      JScrollPane scrollOutputColumnsSelected =
         new JScrollPane(listOutputColumnsSelected);

      resetListOutputColumns();

      sizeComponent(
         scrollOutputColumns,
         new Dimension(
                      listOutputColumns.getPreferredSize().width + 30,
                      DEF_TEXTFIELD_HEIGHT * 12
                   )
      );
      sizeComponent(
         scrollOutputColumnsSelected,
         scrollOutputColumns.getPreferredSize()
      );

      buttonOutputColumnsADD = new JButton("Add  >>");
      buttonOutputColumnsADD.addActionListener( new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                  int [] availableIndices =
                     listOutputColumns.getSelectedIndices();

                  // Copy selected object(s) to the 'selected' column
                  for ( int indexLoop = 0;
                       indexLoop < availableIndices.length;
                       indexLoop++ )
                  {
                     listOutputColumnsSelected.getContents().addElement(
                        listOutputColumns.getContents().elementAt(
                                                                          availableIndices[indexLoop]
                                                                       )
                     );
                  };

                  // Remove them from the 'available' column, note MUST GO BACKWARDS!!
                  // (DUE TO DELETING OBJECTS - so it doesn't affect indices)...
                  for ( int indexLoop = availableIndices.length - 1;
                       indexLoop >= 0;
                       indexLoop-- )
                  {
                     listOutputColumns.getContents().removeElementAt(
                        availableIndices[indexLoop]
                     );
                  };
               }
            });

      buttonOutputColumnsREMOVE = new JButton("<<  Remove");
      buttonOutputColumnsREMOVE.addActionListener( new ActionListener() {
               public void actionPerformed(ActionEvent ae) {
                  int [] selectedIndices =
                     listOutputColumnsSelected.getSelectedIndices();

                  // Remove them from the 'available' column, note MUST GO BACKWARDS!!
                  // (DUE TO DELETING OBJECTS - so it doesn't affect indices)...
                  for ( int indexLoop = selectedIndices.length - 1;
                       indexLoop >= 0;
                       indexLoop-- )
                  {
                     //check it's removable (ie it's not in the obligatory list)
                     if (
                        !obligatoryOutputColumns.contains(
                           listOutputColumnsSelected.getContents().elementAt(
                                                            selectedIndices[indexLoop]
                                                         )
                        )
                     )
                     {
                        listOutputColumnsSelected.getContents().removeElementAt(
                           selectedIndices[indexLoop]
                        );
                     }
                  };

                  // Now clear and replace the contents of the available list,
                  // this is to preserve the original ORDER! Only adds items
                  // if they are not in the selected list...
                  listOutputColumns.getContents().clear();

                  for ( int indexLoop = 0;
                       indexLoop < itemsOutputColumns.length;
                       indexLoop++ )
                  {
                     if (
                        listOutputColumnsSelected.getContents().indexOf(
                           itemsOutputColumns[indexLoop]
                        ) == -1
                     )
                     {
                        listOutputColumns.getContents().addElement(
                           itemsOutputColumns[indexLoop]
                        );
                     }
                  }
               }
            });

      // Now assemble the contents into the tabbed panel...
      JPanel panelContentsOutput = new JPanel(new GridBagLayout());

      addGB(
         panelContentsOutput, labelCheckImageType,
         0, 0, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsOutput, comboCheckImageType,
         1, 0, GridBagConstraints.WEST, true, 2
      );

      addGB(
         panelContentsOutput, labelOutputColumns,
         1, 1, GridBagConstraints.SOUTH, true, 1
      );

      addGB(
         panelContentsOutput, labelOutputColumnsSelected,
         2, 1, GridBagConstraints.SOUTH, true, 1
      );

      addGB(
         panelContentsOutput, labelOutput,
         0, 2, GridBagConstraints.NORTHEAST, false, 1
      );

      addGB(
         panelContentsOutput, scrollOutputColumns,
         1, 2, GridBagConstraints.CENTER, false, 1
      );

      addGB(
         panelContentsOutput, scrollOutputColumnsSelected,
         2, 2, GridBagConstraints.CENTER, false, 1
      );

      addGB(
         panelContentsOutput, buttonOutputColumnsADD,
         1, 3, GridBagConstraints.WEST, false, 1
      );

      addGB(
         panelContentsOutput, buttonOutputColumnsREMOVE,
         2, 3, GridBagConstraints.EAST, false, 1
      );


      JPanel panelOutput = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelOutput.add(panelContentsOutput);
      // ------------------------------------------------------------------------


      // == ADVANCED TAB =====================================================
      // Create all items for the panel...

      labelMemObjStack = new JLabel("Object Stack:  ");
      inttextMemObjStack = new JIntegerField();
      inttextMemObjStack.setToolTipText(
         "Number of objects in object stack [MEMORY_OBJSTACK]"
      );
      sizeComponent(inttextMemObjStack, DEF_TEXTFIELD_DIMENSION);

      labelMemPixStack = new JLabel("Pixel Stack:  ");
      inttextMemPixStack = new JIntegerField();
      inttextMemPixStack.setToolTipText(
         "Number of pixels in pixel stack [MEMORY_PIXSTACK]"
      );
      sizeComponent(inttextMemPixStack, DEF_TEXTFIELD_DIMENSION);

      labelMemBufSize = new JLabel("Buffer Size:  ");
      inttextMemBufSize = new JIntegerField();
      inttextMemBufSize.setToolTipText(
         "Number of lines in buffer [MEMORY_BUFSIZE]"
      );
      sizeComponent(inttextMemBufSize, DEF_TEXTFIELD_DIMENSION);

      labelVerboseType = new JLabel("Verbose Type:  ");
      panelVerboseType = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelVerboseType.setToolTipText(
         "Verbosity [VERBOSE_TYPE]"
      );
      radiobuttongroupVerboseType = new ButtonGroup();
      radiobuttonVerboseTypeNORMAL =
         new JRadioButton("Normal");
      radiobuttonVerboseTypeNORMAL.setActionCommand("NORMAL");
      radiobuttonVerboseTypeNORMAL.setToolTipText(
         panelVerboseType.getToolTipText()
      );
      radiobuttongroupVerboseType.add(radiobuttonVerboseTypeNORMAL);
      panelVerboseType.add(radiobuttonVerboseTypeNORMAL);
      radiobuttonVerboseTypeQUIET =
         new JRadioButton("Quiet");
      radiobuttonVerboseTypeQUIET.setActionCommand("QUIET");
      radiobuttonVerboseTypeQUIET.setToolTipText(
         panelVerboseType.getToolTipText()
      );
      radiobuttongroupVerboseType.add(radiobuttonVerboseTypeQUIET);
      panelVerboseType.add(radiobuttonVerboseTypeQUIET);
      radiobuttonVerboseTypeFULL =
         new JRadioButton("Full");
      radiobuttonVerboseTypeFULL.setActionCommand("FULL");
      radiobuttonVerboseTypeFULL.setToolTipText(
         panelVerboseType.getToolTipText()
      );
      radiobuttongroupVerboseType.add(radiobuttonVerboseTypeFULL);
      panelVerboseType.add(radiobuttonVerboseTypeFULL);
      radiobuttongroupVerboseType.setSelected(
         radiobuttonVerboseTypeNORMAL.getModel(), true
      );

      // Now assemble the contents into the tabbed panel...
      JPanel panelContentsAdvanced = new JPanel(new GridBagLayout());

      addGB(
         panelContentsAdvanced, labelMemObjStack,
         0, 0, GridBagConstraints.NORTHEAST, true, 1
      );

      addGB(
         panelContentsAdvanced, inttextMemObjStack,
         1, 0, GridBagConstraints.WEST, true, 2
      );

      addGB(
         panelContentsAdvanced, labelMemPixStack,
         0, 1, GridBagConstraints.NORTHEAST, false, 1
      );

      addGB(
         panelContentsAdvanced, inttextMemPixStack,
         1, 1, GridBagConstraints.WEST, false, 2
      );

      addGB(
         panelContentsAdvanced, labelMemBufSize,
         0, 2, GridBagConstraints.NORTHEAST, false, 1
      );

      addGB(
         panelContentsAdvanced, inttextMemBufSize,
         1, 2, GridBagConstraints.WEST, false, 2
      );

      addGB(
         panelContentsAdvanced, labelVerboseType,
         0, 3, GridBagConstraints.EAST, true, 1
      );

      addGB(
         panelContentsAdvanced, panelVerboseType,
         1, 3, GridBagConstraints.WEST, true, 2
      );

      JPanel panelAdvanced = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelAdvanced.add(panelContentsAdvanced);

      // ------------------------------------------------------------------------

      // Assemble all the above panels into a nice tabbed display

      setTabPlacement(JTabbedPane.TOP);

      addTab("Image", imagePanel);
      addTab("Extraction", panelExtraction);
      addTab("Photometry", panelPhotometry);
      addTab("Separation", panelStar_Galaxy_Separation);
      addTab("Background", panelBackground);
      addTab("Output", panelOutput);
      addTab("Advanced", panelAdvanced);
   };


   /**
    * Overrides addTab(...) to force added panels to the top of the pane
    */
   public void addTab(String s, Component c)
   {
      JPanel p = new JPanel(new BorderLayout());
      p.add(c, BorderLayout.NORTH);
      super.addTab(s,p);
   }
   /**
    * A simple method to force a JComponent to become a specific size.
    *
    * @author Alan Maxwell
    */
   public static void sizeComponent(JComponent item, Dimension dimension)
   {
      item.setMinimumSize(dimension);
      item.setPreferredSize(dimension);
      // item.setMaximumSize(dimension);
   };

   /**
    * A simple method to easily add a Component to a (GridBagLayout)
    * Container - note that if the container does not have a GridBagLayout
    * set as its layout manager, this method will update the layout manager.
    *
    * @author Alan Maxwell
    */
   public void addGB(Container cont, Component comp, int x, int y,
                     int anchor, boolean spaceAbove, int colSpan)
   {

      if ((cont.getLayout() instanceof GridBagLayout) == false)
      {
         cont.setLayout(new GridBagLayout());
      }
      {
         if (spaceAbove == true)
         {
            constraints.insets = DEF_VSPACE_INSETS;
         }
         else
         {
            constraints.insets = DEF_INSETS;
         };

         constraints.gridwidth = colSpan;
         constraints.gridheight = 1;

         constraints.anchor = anchor;
         constraints.gridx = x;
         constraints.gridy = y;
         cont.add(comp, constraints);
      };
   };

   /**
    * A convenience routine setting constraints suitable for a label
    * (full fill, low weighting, etc)
    */
   private void setLabelConstraints(GridBagConstraints constraints)
   {
      constraints.fill = constraints.BOTH;
      constraints.gridheight = 1;
      constraints.gridwidth = 1;
      constraints.gridx = 0;
      constraints.weightx = 0;
      constraints.weighty = 0;
   }

   /**
    * A convenience routine setting constraints for a user entry component -
    * centers in its cell, fills horizontally, high weight
    */
   private void setEntryConstraints(GridBagConstraints constraints)
   {
      constraints.anchor = constraints.CENTER;
      constraints.fill = constraints.HORIZONTAL;
      constraints.gridheight = 1;
      constraints.gridwidth = 1;
      constraints.gridx = 1;
      constraints.weightx = 1;
      constraints.weighty = 0;
   }

   /**
    * A convenience routine setting constraints for a supplementary control
    * (eg button for selecting a file).  centers in its cell, does not fill,
    * low weight
    */
   private void setControlConstraints(GridBagConstraints constraints)
   {
      constraints.anchor = constraints.CENTER;
      constraints.fill = constraints.NONE;
      constraints.gridheight = 1;
      constraints.gridwidth = 1;
      constraints.gridx = 3;
      constraints.weightx = 0;
      constraints.weighty = 0;
   }

   /**
    * A convenience routine to create the standard panels used
    */
   protected JPanel newPanel()
   {
      //      GridLayout2 flexilayout = new GridLayout2(0,4,2,10);

      //FlexiGridLayout flexilayout = new FlexiGridLayout(4);
      // flexilayout.setHGap(2);
      // flexilayout.setVGap(10);

      // JPanel panel = new JPanel(flexilayout);
      // panel.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));
      // return panel;

      JPanel panel = new JPanel(new GridBagLayout());
      panel.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));
      return panel;
   }

   /**
    * sets the image to the given string (might be a path, might be a url)
    * , fixing it so that loading a new config
    * file does not change it
    */
   public void fixImage(String location)
   {
      setImage(location);
      imageTextField.setEnabled(false);
      imageChooserButton.setEnabled(false);
      imagePasteButton.setEnabled(false);
      imageFixed = true;

      setDualImageMode(false);
      dualImageToggle.setEnabled(false);
   }

   public boolean isImageFixed()
   {
      return imageFixed;
   }

   /**
    * Some output columns will be required for particular application.  For
    * example, Aladin requires X_WORLD, Y_WORLD, shape information (for
    * drawing) and the AVO demo needs magnitude information for doing SEDs.  The
    * application can set these here, so the user cannot unselect them.
    */
   public void addObligatoryOutputColumn(String columnName)
   {
      obligatoryOutputColumns.add(columnName);
      selectOutputColumn(columnName);
   }

   /**
    * Some output columns will be required for particular application.  For
    * example, Aladin requires X_WORLD, Y_WORLD, shape information (for
    * drawing) and the AVO demo needs magnitude information for doing SEDs.  The
    * application can set these here, so the user cannot unselect them.
    */
   public void setObligatoryOutputColumns(String[] columnNames)
   {
      obligatoryOutputColumns.clear();
      obligatoryOutputColumns.addAll(Arrays.asList(columnNames));
      for (int i=0;i<columnNames.length;i++)
      {
         selectOutputColumn(columnNames[i]);
      }
   }

   /**
    * Some output columns will be required for particular application.  For
    * example, Aladin requires X_WORLD, Y_WORLD, shape information (for
    * drawing) and the AVO demo needs magnitude information for doing SEDs.  The
    * application can set these here, so the user cannot unselect them.
    */
   public String[] getObligatoryOutputColumns()
   {
      return (String[]) obligatoryOutputColumns.toArray(new String[] {});

   }

   /**
    * Clears the list of selected output columns and sets it to the minimum
    * set (ie, the obligatory cols), and sets the list of possible output
    * columns to the set of all columns minus the selected ones
    */
   public void resetListOutputColumns()
   {
      if ((listOutputColumns != null) && (listOutputColumnsSelected != null))
      {
         listOutputColumns.getContents().clear();
         listOutputColumnsSelected.getContents().clear();

         for (int listLoop = 0; listLoop < obligatoryOutputColumns.size(); listLoop++)
         {
            listOutputColumnsSelected.getContents().addElement(
               obligatoryOutputColumns.elementAt(listLoop)
            );
         };

         for (int listLoop = 0; listLoop < itemsOutputColumns.length; listLoop++)
         {
            //if it's not already selected
            if (
               listOutputColumnsSelected.getContents().indexOf(
                  itemsOutputColumns[listLoop]
               ) == -1
            )
            {
               listOutputColumns.getContents().addElement(
                  itemsOutputColumns[listLoop]
               );
            };
         };
      };
   }

   /**
    * Selects the given output column - ie moves it from the possible list to
    * the selected list, if it's not already there
    */
   public void selectOutputColumn(String columnName)
   {
      //check the column name exists
      if ((listOutputColumns.getContents().indexOf(columnName) == -1)   &&
             (listOutputColumnsSelected.getContents().indexOf(columnName) == -1))
      {
         throw new IllegalArgumentException("Unknown column name '"+columnName+"'");
      }
      //move if not already moved (we don't want duplicates)
      if (listOutputColumns.getContents().indexOf(columnName) != -1)
      {
         listOutputColumns.getContents().removeElement(columnName);
         listOutputColumnsSelected.getContents().addElement(columnName);
      }
   }

   /**
    * Selects the given output column - ie moves it from the possible list to
    * the selected list, if it's not already there
    */
   public void deselectOutputColumn(String columnName)
   {
      //check the column name exists
      if ((listOutputColumns.getContents().indexOf(columnName) == -1)   &&
             (listOutputColumnsSelected.getContents().indexOf(columnName) == -1))
      {
         throw new IllegalArgumentException("Unknown column name '"+columnName+"'");
      }
      //move if not already moved (we don't want duplicates)
      if (listOutputColumns.getContents().indexOf(columnName) == -1)
      {
         listOutputColumns.getContents().addElement(columnName);
         listOutputColumnsSelected.getContents().removeElement(columnName);
      }
   }



   /**
    * Reads the image header, updating entry fields if it can
    */

   /**
    * Called when the (main) image has changed.  Note that due to the way
    * events fire, we cannot count on reading the imageTextField entry, as
    * this routine might be called before that entry is updated in the list of
    * events.  So we pass the new file as a parameter, and make use of this
    * routine to set all the correct fields from the image.
    */
   protected void setImage(String newFile)
   {
      imageTextField.setText(newFile);

      if (newFile.trim().length() == 0)
      {
         //JOptionPane.showMessageDialog(this, "No image entered");
         //empty - don't do anything.
         return;
      }

      try
      {
         FitsImage fitsImage = new FitsImage(newFile);
         fitsImage.loadHeader();

         boolean mightBeCCD = false;
         boolean mightBePhoto = false;

         //look for EIS filter
         String filterValue = fitsImage.getHeaderValue("FILTER");
         if ( (filterValue != null) && (filterValue.length() > 0))
         {
            try
            {
               Passband filterPassband = (Passband) Passband.getFor(Passband.class, ""+filterValue.charAt(0));

               passbandPanel.eisSingleBandBtn.doClick();

               passbandPanel.eisSingleBandPicker.setSelectedItem(filterPassband);
            }
            catch (IllegalArgumentException iae)
            {
               //no passband match for the given string.  Just report
               Log.logWarning("Unknown passband specified by FILTER: '"+filterValue+"'");
            }

         }
         else
         {
            //no filter value given, so look at given physical unit stuff
            passbandPanel.physicalBtn.doClick();

            String freqValue = fitsImage.getHeaderValue("CRVAL3");
            if ((freqValue != null) && (freqValue.length() > 0))
            {
               passbandPanel.obsFreqField.setText(freqValue);
            }
            String fwhmValue = fitsImage.getHeaderValue("CDELT3");
            if ((fwhmValue != null) && (fwhmValue.length() > 0))
            {
               passbandPanel.filterFwhmField.setText(fwhmValue);
            }
         }

         //these two checks should really be on 'starts with' or 'contains'...
         if (fitsImage.getHeaderValue("CCD") != null)
         {
            mightBeCCD = true;
         }

         if (fitsImage.getHeaderValue("PHOTO") != null)
         {
            mightBePhoto = true;
         }

         //if they're different, one will be true and the other false, so
         //we can make a good guess
         if (mightBePhoto != mightBeCCD)
         {
            if (mightBePhoto)
            {
               radiobuttonDetectTypePHOTO.doClick();

            }
            else
            {
               radiobuttonDetectTypeCCD.doClick();
            }
         }
      }

      catch (FileNotFoundException fnfe)
      {
         //JOptionPane.showMessageDialog(this, "File '"+imageEntry+"' not found");
         Log.logError("Could not find image '"+newFile+"'",fnfe);
      }
      catch (IOException ioe)
      {
         //JOptionPane.showMessageDialog(this, "No image entered");
         Log.logError("Could not read from image '"+newFile+"'",ioe);
      }


   }




   /**
    * Returns text value of given tag
    */
   protected String getXmlTagValue(Element rootElement, String tagName, int argNum)
   {
      if (argNum > 0)
      {
         String [] results = getXmlTagValues(rootElement, tagName);

         if ((results != null) && (results.length >= argNum))
         {
            return results[argNum - 1];
         }
      }

      return "";
   }

   /**
    * Returns (as a String array) all of the FIRST 'text' child Elements
    * belonging to all the 'arg' Elements belonging to the
    * FIRST matching (tagName) XML Element falling
    * within the sub-tree having 'anElement' as its root. (Eh?)
    *
    * Returns a zero-length array if the element cannot be found or
    * if there is an error. (AJWM: NOT a NULL value...???)
    *
    * Asking for getXmlElementArgs(Element:animals, "monkey") would return:
    *    {"1","2","3"}
    * When provided with the following XML:
    *    <animals>
    *      <monkey>
    *        <arg>1</arg>
    *        <arg>2</arg>
    *        <arg>3</arg>
    *      </monkey>
    *    </animals>
    *
    * @author Alan Maxwell
    */
   protected String [] getXmlTagValues(Element rootElement, String tagName)
   {
      NodeList tempNodeList = null;
      Node tempNode = null;

      String [] result = null;

      if (rootElement != null)
      {
         tempNodeList = rootElement.getElementsByTagName(tagName);

         if (tempNodeList.getLength() > 1)
         {
            Log.logError("More than one node matches " + tagName+", using first one");
         }

         if (tempNodeList.getLength() > 0)
         {
            tempNode = tempNodeList.item(0);
            tempNodeList = ((Element) tempNode).getElementsByTagName("arg");

            // If we found some <arg> sub nodes, process them...
            // Otherwise, see if the value has been written directly into the tag...
            if (tempNodeList.getLength() > 0)
            {
               // Got some arg nodes (this is what we hope for)
               result = new String[tempNodeList.getLength()];

               for (int argLoop = 0; argLoop < tempNodeList.getLength(); argLoop++ )
               {
                  result[argLoop] =
                     tempNodeList.item(argLoop).getFirstChild().getNodeValue().trim();

                  // AJWM: Should we be protecting against null strings?
                  if (result[argLoop] == null)
                  {
                     result[argLoop] = "";
                  };
               };

               return result;
            }
            else
            {
               // There were NO arg nodes, look for a direct value under main tag
               String tempString = tempNode.getFirstChild().getNodeValue().trim();

               if (tempString != null)
               {
                  result = new String[1];

                  result[0] = tempString;

                  return result;
               }
            }
         }
      }

      // If we get here, we haven't found what we want so return empty array...
      return new String[] { };
   }

   /**
    * Returns (as a String array) all of the FIRST 'text' child Elements
    * belonging to all the 'arg' Elements belonging to the
    * FIRST matching (tagName) XML Element falling
    * within the sub-tree having 'anElement' as its root. (Eh?)
    *
    * Returns null if the element cannot be found or if there is an error.
    *
    * Asking for getXmlElementArgs(Element:animals, "monkey") would return:
    *    {"1","2","3"}
    * When provided with the following XML:
    *    <animals>
    *      <monkey>
    *        <arg>1</arg>
    *        <arg>2</arg>
    *        <arg>3</arg>
    *      </monkey>
    *    </animals>
    *
    * @author Alan Maxwell
    *
    protected String [] getXmlElementArgs(Element anElement, String tagName)
    {
    NodeList tempNodeList = null;
    Node tempNode = null;

    tempNodeList = anElement.getElementsByTagName(tagName);

    if (tempNodeList.getLength() < 1)
    {
    return null;
    };

    // Getting here means we found an element(s) matching the provided tag name,
    // so we attempt to get any 'arg' sub elements it might (should) have...

    tempNodeList = ((Element) tempNodeList.item(0)).getElementsByTagName("arg");

    if (tempNodeList.getLength() < 1)
    {
    return null;
    };

    // If it has 1 or more child elements, load them into the returned array..

    String [] result = new String[tempNodeList.getLength()];

    for (int nodeLoop = 0; nodeLoop < tempNodeList.getLength(); nodeLoop++ )
    {
    result[nodeLoop] =
    getXmlElementValueText((Element) tempNodeList.item(nodeLoop));
    //          tempNodeList.item(nodeLoop).getFirstChild().getNodeValue();
    };

    return result;
    };

    /**
    * Convenience routine for loading fields.  Loads given text field component
    * with text from given tag
    */
   private void setTextFieldFromTag(JTextField field,
                                    Element rootElement,
                                    String tagName,
                                    int argNum)
   {
      if (field != null)
      {
         field.setText(getXmlTagValue(rootElement, tagName, argNum));
      }
   };

   private void setComboBoxFromTag(JComboBox combo,
                                   Element rootElement,
                                   String tagName,
                                   int argNum)
   {
      if (combo != null)
      {
         combo.setSelectedItem(getXmlTagValue(rootElement, tagName, argNum));
      }
   };


   private void setCheckBoxFromTag(JCheckBox check,
                                   Element rootElement,
                                   String tagName,
                                   int argNum)
   {
      if (check != null)
      {
         check.setSelected(
            getXmlTagValue(rootElement, tagName, argNum).equalsIgnoreCase("true")
         );

         check.doClick();
         check.doClick();
      }
   };

   public void setDualImageMode(boolean b)
   {
      dualImageToggle.setSelected(b);
      dualImageTextField.setEnabled(b);
      dualImageChooserButton.setEnabled(b);
      dualImagePasteButton.setEnabled(b);
   }


   /**
    * Populates the tabbed pages with values taken from the provided XML File!
    *
    * @author Alan Maxwell
    */
   public void loadXmlFile(File xmlFile) throws IOException
   {
      try
      {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         //dbf.setIgnoringElementContentWhitespace(true);
         dbf.setNamespaceAware(true);
         DocumentBuilder builder = dbf.newDocumentBuilder();
         Document doc = builder.parse(xmlFile);

         loadXmlDoc(doc);
      }
      catch (SAXException se)
      {
         IOException ioe = new IOException("Parsing error: "+se);
         //ioe.setStackTrace(e.getStackTrace());  //java v1.4+
         ioe.fillInStackTrace(); //java v1.3
         throw ioe;
      }
      catch (ParserConfigurationException pce)
      {
         IOException ioe = new IOException("Parser error: "+pce);
         //ioe.setStackTrace(e.getStackTrace());  //java v1.4+
         ioe.fillInStackTrace(); //java v1.3
         throw ioe;
      }
   };

   /**
    * Populates the tabbed pages with values taken from the provided XML String!
    *
    * @author Alan Maxwell
    */
   public void loadXmlString(String xmlString)
   {
      try
      {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         //dbf.setIgnoringElementContentWhitespace(true);
         dbf.setNamespaceAware(true);
         DocumentBuilder builder = dbf.newDocumentBuilder();
         Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

         loadXmlDoc(doc);
      }
      catch (Exception e)
      {
         Log.logError("Error parsing string '"+xmlString+"'",e);
      }
   };

   /**
    * Populates the tabbed pages with values taken from the provided XML Document!
    *
    * @author Alan Maxwell
    */
   public void loadXmlDoc(Document xmlDoc)
   {
      Element docRoot = (Element) xmlDoc.getDocumentElement();

      String returnValue = null;
      String [] returnValues = null;

      // Go through the XML extracting the config information...

      // Image tab... - don't do if the image has been 'fixed' externally
      if (!imageFixed)
      {
         setImage(getXmlTagValue(docRoot, "ImageToCatalog", 1));

         //check and see if dual image, ie imageToCatalog is different to
         //image to measure
         if (!getXmlTagValue(docRoot, "ImageToCatalog", 1).equals(getXmlTagValue(docRoot, "ImageToMeasure", 1)))
         {
            setDualImageMode(true);
            setTextFieldFromTag(dualImageTextField, docRoot, "ImageToMeasure", 1);
         }
         else
         {
            setDualImageMode(false);
         }
      }

      returnValue = getXmlTagValue(docRoot, "DETECT_TYPE", 1);
      if (returnValue.equalsIgnoreCase("PHOTO"))
      {
         radiobuttongroupDetectType.setSelected(
            radiobuttonDetectTypePHOTO.getModel(), true
         );
      }
      else
      {
         radiobuttongroupDetectType.setSelected(
            radiobuttonDetectTypeCCD.getModel(), true
         );
      };

      //at some point this will have to be done properly, but at the moment, we just store this info in a special 'for Aladin' tag
      passbandPanel.setFromIds(getXmlTagValues(docRoot, "Passband"));

      /*
       setComboBoxFromTag(passbandUcdPicker, docRoot, "Wavelength", 1);
       **/

      // Extraction tab...
      setTextFieldFromTag(textFlagImage, docRoot, "FLAG_IMAGE", 1);
      setTextFieldFromTag(integertextDetMinArea, docRoot, "DETECT_MINAREA", 1);
      setTextFieldFromTag(doubletextDetThresh1, docRoot, "DETECT_THRESH", 1);
      setTextFieldFromTag(doubletextDetThresh2, docRoot, "DETECT_THRESH", 2);
      setTextFieldFromTag(doubletextAnalysisThresh1, docRoot, "ANALYSIS_THRESH", 1);
      setTextFieldFromTag(doubletextAnalysisThresh2, docRoot, "ANALYSIS_THRESH", 2);
      setCheckBoxFromTag(checkApplyFilter, docRoot, "FILTER", 1);
      setComboBoxFromTag(textFilterName, docRoot, "FILTER_NAME", 1);
      setTextFieldFromTag(integertextDeblendThresh, docRoot, "DEBLEND_NTHRESH", 1);
      setTextFieldFromTag(doubletextDeblendMinCont, docRoot, "DEBLEND_MINCONT", 1);
      setCheckBoxFromTag(checkCleaning, docRoot, "CLEAN", 1);
      setTextFieldFromTag(doubletextCleaning, docRoot, "CLEAN_PARAM", 1);

      returnValue = getXmlTagValue(docRoot, "MASK_TYPE", 1);
      if (returnValue.equalsIgnoreCase("CORRECT"))
      {
         radiobuttongroupMaskType.setSelected(
            radiobuttonMaskTypeCORRECT.getModel(), true
         );
      }
      else if (returnValue.equalsIgnoreCase("BLANK"))
      {
         radiobuttongroupMaskType.setSelected(
            radiobuttonMaskTypeBLANK.getModel(), true
         );
      }
      else
      {
         radiobuttongroupMaskType.setSelected(
            radiobuttonMaskTypeNONE.getModel(), true
         );
      };

      // Photometry tab...
      setTextFieldFromTag(inttextPhotApertures, docRoot, "PHOT_APERTURES", 1);
      setTextFieldFromTag(doubletextPhotAutoParams1, docRoot, "PHOT_AUTOPARAMS", 1);
      setTextFieldFromTag(doubletextPhotAutoParams2, docRoot, "PHOT_AUTOPARAMS", 2);
      setTextFieldFromTag(doubletextSatLevel, docRoot, "SATUR_LEVEL", 1);
      setTextFieldFromTag(doubletextMagZero, docRoot, "MAG_ZEROPOINT", 1);
      setTextFieldFromTag(doubletextMagGamma, docRoot, "MAG_GAMMA", 1);
      setTextFieldFromTag(doubletextGain, docRoot, "GAIN", 1);
      setTextFieldFromTag(doubletextPixelScale, docRoot, "PIXEL_SCALE", 1);

      // Separation tab...
      setTextFieldFromTag(doubletextSeeingFWHM, docRoot, "SEEING_FWHM", 1);
      setTextFieldFromTag(textStarNNWName, docRoot, "STARNNW_NAME", 1);

      // Background tab...
      setTextFieldFromTag(integertextBackSize1, docRoot, "BACK_SIZE", 1);
      setTextFieldFromTag(integertextBackSize2, docRoot, "BACK_SIZE", 2);
      setTextFieldFromTag(integertextBackFilter1, docRoot, "BACK_FILTERSIZE", 1);
      setTextFieldFromTag(integertextBackFilter2, docRoot, "BACK_FILTERSIZE", 2);

      returnValue = getXmlTagValue(docRoot, "BACKPHOTO_TYPE", 1);
      if (returnValue.equalsIgnoreCase("GLOBAL"))
      {
         radiobuttongroupBackPhotoType.setSelected(
            radiobuttonBackPhotoTypeGLOBAL.getModel(), true
         );
      }
      else
      {
         radiobuttongroupBackPhotoType.setSelected(
            radiobuttonBackPhotoTypeLOCAL.getModel(), true
         );
      };
      setTextFieldFromTag(inttextBackPhotoThick, docRoot, "BACKPHOTO_THICK", 1);
      // What about "Apply Pixel Flag" checkbox and textfield?

      // Output tab...
      setComboBoxFromTag(comboCheckImageType, docRoot, "CHECKIMAGE_TYPE", 1);
      resetListOutputColumns();

      returnValues = getXmlTagValues(docRoot, "OUTPUT_COLUMNS");
      int [] selections = new int[returnValues.length];

      for (int outputLoop = 0; outputLoop < returnValues.length; outputLoop++)
      {
         selections[outputLoop] =
            listOutputColumns.getContents().indexOf(returnValues[outputLoop]);
      };
      listOutputColumns.setSelectedIndices(selections);
      buttonOutputColumnsADD.doClick();

      // Advanced tab...
      setTextFieldFromTag(inttextMemObjStack, docRoot, "MEMORY_OBJSTACK", 1);
      setTextFieldFromTag(inttextMemPixStack, docRoot, "MEMORY_PIXSTACK", 1);
      setTextFieldFromTag(inttextMemBufSize, docRoot, "MEMORY_BUFSIZE", 1);

      returnValue = getXmlTagValue(docRoot, "VERBOSE_TYPE", 1);
      if (returnValue.equalsIgnoreCase("QUIET"))
      {
         radiobuttongroupVerboseType.setSelected(
            radiobuttonVerboseTypeQUIET.getModel(), true
         );
      }
      else if (returnValue.equalsIgnoreCase("FULL"))
      {
         radiobuttongroupVerboseType.setSelected(
            radiobuttonVerboseTypeFULL.getModel(), true
         );
      }
      else
      {
         radiobuttongroupVerboseType.setSelected(
            radiobuttonVerboseTypeNORMAL.getModel(), true
         );
      };
   };

   /**
    * Returns all of the tabbed pages' options encoded as an XML String.
    *
    * @author Alan Maxwell
    */
   public String toXmlString()
   {
      StringBuffer xmlResult =
         new StringBuffer("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\n");

      xmlResult.append(
         "<ace:AceInputDoc \n" +
            "        xmlns:ace=\"http://www.astrogrid.org/namespace/AceInput-1_0\" \n" +
            "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
            "        xsi:schemaLocation=\"http://www.astrogrid.org/namespace/AceInput-1_0 \n" +
            "        http://astrogrid.ast.cam.ac.uk/namespace/AceInput-1_0.xsd\"> \n\n"
      );

      xmlResult.append(
         "<!-- Generated by TemplateEditorPanel --> \n\n"
      );

      xmlResult.append(
         makeTag("ImageToCatalog", imageTextField.getText())
      );

      if (dualImageToggle.isSelected())
      {
         xmlResult.append(
            makeTag("ImageToMeasure", dualImageTextField.getText())
         );
      }
      else
      {
         xmlResult.append(
            makeTag("ImageToMeasure", imageTextField.getText())
         );
      }

      xmlResult.append(
         makeTag(
                         "DETECT_TYPE",
                         radiobuttongroupDetectType.getSelection().getActionCommand()
                      )
      );

      //at some point this will have to be done properly, but at the moment, we just store this info in a special 'for Aladin' tag
      xmlResult.append(
         makeTag("Passband", passbandPanel.getIds())
      );

      //botch to help v0.2 server
      xmlResult.append(
         makeTag("Wavelength", "PHOT_MAG")
      );

      /*
       xmlResult.append(
       makeTag("Wavelength", (String) passbandUcdPicker.getSelectedItem())
       );
       **/

      xmlResult.append(
         makeTag("FLAG_IMAGE", textFlagImage.getText())
      );

      xmlResult.append(
         makeTag("DETECT_MINAREA", integertextDetMinArea.getText())
      );

      xmlResult.append(
         makeTag(
                         "DETECT_THRESH",
                         new String [] {doubletextDetThresh1.getText(), doubletextDetThresh2.getText()}
                      )
      );

      xmlResult.append(
         makeTag(
                         "ANALYSIS_THRESH",
                         new String [] {doubletextAnalysisThresh1.getText(), doubletextAnalysisThresh2.getText()}
                      )
      );

      if (checkApplyFilter.isSelected())
      {
         xmlResult.append(makeTag("FILTER", "true"));
      }
      else
      {
         xmlResult.append(makeTag("FILTER", "false"));
      };

      xmlResult.append(
         makeTag("FILTER_NAME", (String) textFilterName.getSelectedItem())
      );

      xmlResult.append(
         makeTag("DEBLEND_NTHRESH", integertextDeblendThresh.getText())
      );

      xmlResult.append(
         makeTag("DEBLEND_MINCONT", doubletextDeblendMinCont.getText())
      );

      if (checkCleaning.isSelected())
      {
         xmlResult.append(makeTag("CLEAN", "true"));
      }
      else
      {
         xmlResult.append(makeTag("CLEAN", "false"));
      };

      xmlResult.append(
         makeTag("CLEAN_PARAM", doubletextCleaning.getText())
      );

      xmlResult.append(
         makeTag(
                         "MASK_TYPE",
                         radiobuttongroupMaskType.getSelection().getActionCommand()
                      )
      );

      xmlResult.append(
         makeTag("PHOT_APERTURES", inttextPhotApertures.getText())
      );

      xmlResult.append(
         makeTag(
                         "PHOT_AUTOPARAMS",
                         new String [] {doubletextPhotAutoParams1.getText(), doubletextPhotAutoParams2.getText()}
                      )
      );

      xmlResult.append(
         makeTag("SATUR_LEVEL", doubletextSatLevel.getText())
      );

      xmlResult.append(
         makeTag("MAG_ZEROPOINT", doubletextMagZero.getText())
      );

      xmlResult.append(
         makeTag("MAG_GAMMA", doubletextMagGamma.getText())
      );

      xmlResult.append(
         makeTag("GAIN", doubletextGain.getText())
      );

      xmlResult.append(
         makeTag("PIXEL_SCALE", doubletextPixelScale.getText())
      );

      xmlResult.append(
         makeTag("SEEING_FWHM", doubletextSeeingFWHM.getText())
      );

      xmlResult.append(
         makeTag("STARNNW_NAME", textStarNNWName.getText())
      );

      xmlResult.append(
         makeTag(
                         "BACK_SIZE",
                         new String [] {integertextBackSize1.getText(), integertextBackSize2.getText()}
                      )
      );

      xmlResult.append(
         makeTag(
                         "BACK_FILTERSIZE",
                         new String [] {integertextBackFilter1.getText(), integertextBackFilter2.getText()}
                      )
      );

      xmlResult.append(
         makeTag(
                         "BACKPHOTO_TYPE",
                         radiobuttongroupBackPhotoType.getSelection().getActionCommand()
                      )
      );

      xmlResult.append(
         makeTag("BACKPHOTO_THICK", inttextBackPhotoThick.getText())
      );

      xmlResult.append(
         makeTag("CHECKIMAGE_TYPE", (String) comboCheckImageType.getSelectedItem())
      );

      /*    xmlResult.append(
       makeTag("CHECKIMAGE_NAME", textCheckImageName.getText())
       );*/

      String[] outputItems =
         new String[listOutputColumnsSelected.getContents().size()];

      for (
         int itemLoop = 0;
         itemLoop < listOutputColumnsSelected.getContents().size();
         itemLoop++
      )
      {
         outputItems[itemLoop] = (String)
            listOutputColumnsSelected.getContents().getElementAt(itemLoop);
      };

      xmlResult.append(
         makeTag("OUTPUT_COLUMNS", outputItems)
      );

      xmlResult.append(
         makeTag("MEMORY_OBJSTACK", inttextMemObjStack.getText())
      );

      xmlResult.append(
         makeTag("MEMORY_PIXSTACK", inttextMemPixStack.getText())
      );

      xmlResult.append(
         makeTag("MEMORY_BUFSIZE", inttextMemBufSize.getText())
      );

      xmlResult.append(
         makeTag(
                         "VERBOSE_TYPE",
                         radiobuttongroupVerboseType.getSelection().getActionCommand()
                      )
      );

      xmlResult.append(
         "</ace:AceInputDoc> \n\n"
      );

      return xmlResult.toString();
   };

   /**
    * Makes a tag from the given strings
    */
   private String makeTag(String tag, String value)
   {
      if ((value != null) && (value.length() >0))
      {
         return "  <"+tag+"> \n" +
            "    <arg>" + XmlOutput.transformSpecials(value) + "</arg> \n" +
            "  </"+tag+"> \n\n";
      }

      return "";
   }

   /**
    * makes a tag from the given strings = naughtily overloaded from above
    */
   private String makeTag(String tag, String[] values)
   {
      //don't do it if there are no values
      if ((values != null)    && (values.length >0) &&
             (values[0] != null) && (values[0].length() >0))
      {
         StringBuffer xml = new StringBuffer();
         xml.append("  <"+tag+"> \n");
         for (int i=0;i<values.length;i++)
         {
            if ((values[i] != null) && (values[i].length() > 0))
            {
               xml.append("    <arg>" + XmlOutput.transformSpecials(values[i]) + "</arg> \n");
            }
         }
         xml.append("  </"+tag+"> \n\n");
         return xml.toString();
      }

      return "";
   }

   /**
    * Surrounds an existing set of tags with a new tag
    *
    private String surroundTags(String parentTag, String[] childTags)
    {

    }
    */

   /**
    * Returns all of the tabbed pages' options encoded as an XML Document.
    *
    * Returns null if this cannot be done!
    *
    * @author Alan Maxwell
    */
   public Document toXmlDocument() throws IOException
   {
      try
      {
         String xmlString = toXmlString();

         //validate
         Log.trace("Validating entry-generated xml...");
         org.astrogrid.xmlutils.XmlValidatorXercesImpl validator = new org.astrogrid.xmlutils.XmlValidatorXercesImpl();
         validator.validate(new StringReader(xmlString));
         Log.trace("...generated xml valid");

         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         dbf.setNamespaceAware(true);
         DocumentBuilder builder = dbf.newDocumentBuilder();

         return builder.parse(xmlString);
      }
      catch (SAXException se)
      {
         IOException ioe = new IOException("XML generated from entered values is invalid: "+se);
         //ioe.setStackTrace(e.getStackTrace());  //java v1.4+
         ioe.fillInStackTrace(); //java v1.3
         throw ioe;
      }
      catch (ParserConfigurationException pce)
      {
         IOException ioe = new IOException("Parser error: "+pce);
         //ioe.setStackTrace(e.getStackTrace());  //java v1.4+
         ioe.fillInStackTrace(); //java v1.3
         throw ioe;
      }
   };

   public void saveXmlFile(File xmlFile)
   {
      try
      {
         OutputStream out = new BufferedOutputStream(new FileOutputStream(xmlFile));
         out.write(toXmlString().getBytes());
      }
      catch (IOException ioe)
      {
         Log.logError("Could not save template to '"+xmlFile+"'",ioe);
      }
   }

};

// Alan Maxwell
//
// Version History
//
// $Log: TemplateEditorPanel.java,v $
// Revision 1.1  2003/08/25 18:36:13  mch
// *** empty log message ***
//
// Revision 1.17  2003/07/11 10:43:46  mch
// Fix for examining FITS headers; also better reporting on invalid web config files
//
// Revision 1.16  2003/07/03 18:12:00  mch
// Moved fits header-reading code out to fits.FitsImage
//
// Revision 1.15  2003/06/26 19:14:59  mch
// Passband stuff added
//
// Revision 1.14  2003/06/18 15:56:43  mch
// Dual Image field added
//
// Revision 1.13  2003/01/16 22:20:38  mch
// Fix for double quotes in fits headers
//
// Revision 1.12  2003/01/10 14:17:17  mch
// Removed vectored output columns
//
// Revision 1.11  2002/12/21 12:10:35  mch
// Made compatible with Java 1.3
//
// Revision 1.10  2002/12/18 12:58:19  mch
// Imported from MCHs laptop after Dec AVO Demo meeting in Cambridge
//
// Revision 1.8  2002/12/14 20:18:22  mch
// Fix to run under Java 1.3, fix for special characters
//
// Revision 1.7  2002/12/13 16:36:01  mch
// XML load/store
//
// Revision 1.6  2002/12/12 11:47:17  mch
// Added edit and help button
//
// Revision 1.5  2002/12/09 22:49:47  mch
// UI & XML fixes
//
// Revision 1.4  2002/12/09 18:53:19  mch
// UI changes
//
// Revision 1.3  2002/12/09 10:28:17  mch
// Added keyword
//
// Revision 1.2  2002/12/09 10:27 mch
// Added ImageTab from AceDialog, logging, some XML helper methods.
//
// 0.1.6 : 12 Dec 2002
//       The class now correctly reads in all (known) XML tags, either with
//       or without the <args> sub tag (preferably WITH for correctness and
//       sets the UI accordingly. It correctly outputs the XML too now, with
//       neater helper routines added.
// 0.1.5 : 09 Dec 2002
//       Corrected the writing out of the XML, use 'makeTag()' function to
//       remove lots of the code repetition and make it easier to change
//       the output in future. The code no longer outputs EMPTY tags and
//       all tags enclose their values in sub '<arg>' tags...
// 0.1.4 : 21 Nov 2002
//       Alan Maxwell implemented Martin's recommendations for UI alterations
//       as in e-mail to Alan Maxwell dated 20 November 2002.
// 0.1.3 : 14 Nov 2002
//       Martin Hill changed a fair bit of the functionality, including
//       subclassing from JTabbedPane instead of JPanel... (CVS v1.1)
// 0.1.2 : 11 Nov 2002
//       Added the functionality to LOAD the parameters from and XML
//       file to 'pre-populate' the dialog's selections with default values.
// 0.1.1 : 07 Nov 2002
//       Added the ability to request all the dialog's parameters as
//       a result XML file.
// 0.1.0 : 05 Nov 2002
//       Initial dialog panel version, presents a JPanel with a tabbed
//       type selection of pages with various options.
//


