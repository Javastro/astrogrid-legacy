// AVODemoDialogPanel v1.2
// Alan Maxwell
//
// Version History
//
// 1.2: 11 Nov 2002
//      Added the functionality to LOAD the parameters from and XML
//      file to 'pre-populate' the dialog's selections with default values.
// 1.1: 07 Nov 2002
//      Added the ability to request all the dialog's parameters as
//      a result XML file.
// 1.0: 05 Nov 2002
//      Initial dialog panel version, presents a JPanel with a tabbed
//      type selection of pages with various options.
//

package org.astrogrid.ace.client;

import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.astrogrid.ui.JIntegerField;
import org.astrogrid.ui.JDoubleField;
import org.astrogrid.ui.JCommaIntegerField;
import org.astrogrid.ui.JCommaDoubleField;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import org.astrogrid.ui.ToggleButtonControllerListener;

import org.astrogrid.ui.ExtensionFileFilter; // this needs to be moved
import org.astrogrid.log.Log;

/**
 * AVODemoDialogPanel.java
 *
 * A subclass of JPanel that provides a complete tabbed-pane type
 * dialog box with all the configuration parameters needed for the
 * AVO Demo. (This class can be used anywhere a JPanel would
 * normally go, allowing flexible placement/display.)
 *
 * Used by AVODemoDialog.java to display the tabs for a dialog box...
 *
 * @author Alan Maxwell
 */
public class TemplateEditorPanel extends JTabbedPane implements ActionListener
{
   public static final String SAVEAS_CMD = "SaveAs";
   public static final String SAVE_CMD = "Save";
   public static final String LOAD_CMD = "Load";

   private JFileChooser chooser = new JFileChooser();

   public static final ExtensionFileFilter votFileFilter = new ExtensionFileFilter(new String[] {"xml","vot"},"XML/VOTable");

// -- MAIN --------------------------------------------------------------------

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

    JButton goButton = new JButton("Test AVODemo Dialog Panel >>");
    goButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        JDialog dialog = new JDialog(frame, "AVODemoDialogPanel - Test", true);

        dialog.setSize(450,520);
        dialog.getContentPane().add(
          new TemplateEditorPanel()
        );
        dialog.setVisible(true);
      }
    });

    frame.getContentPane().setLayout(new FlowLayout());
    frame.getContentPane().add(goButton);
    frame.setVisible(true);
  };

// ----------------------------------------------------------------------------

  public static final int DEF_BUTTON_HEIGHT = 22;
  public static final int DEF_BUTTON_WIDTH = 100;

  public static final int DEF_TEXTFIELD_HEIGHT = 20;
  public static final int DEF_TEXTFIELD_WIDTH = 200;

  public static final Dimension DEF_BUTTON_DIMENSION =
    new Dimension(DEF_BUTTON_WIDTH, DEF_BUTTON_HEIGHT);
  public static final Dimension DEF_TEXTFIELD_DIMENSION =
    new Dimension (DEF_TEXTFIELD_WIDTH, DEF_TEXTFIELD_HEIGHT);

  public static final int DEF_PADDING = 3;

  public static final Insets DEF_INSETS =
    new Insets(DEF_PADDING, DEF_PADDING, DEF_PADDING, DEF_PADDING);

  public static final Insets DEF_VSPACE_INSETS =
    new Insets(DEF_PADDING + DEF_TEXTFIELD_HEIGHT, DEF_PADDING, DEF_PADDING, DEF_PADDING);

  GridBagConstraints constraints = new GridBagConstraints();

  // All the form variables we need access to:
  // == CATALOGUE TAB =======================================================
  JLabel labelCatName = null;
  JTextField textCatName = null;

  JLabel labelCatType = null;
  JList listCatType = null;
  JScrollPane listCatTypeScroll = null;

  JLabel labelParamName = null;
  JTextField textParamName = null;

  // == EXTRACTION TAB ======================================================
  JLabel labelDetectType = null;
  JPanel panelDetectType = null;
  ButtonGroup radiobuttongroupDetectType = null;
  JRadioButton radiobuttonDetectTypeCCD = null;
  JRadioButton radiobuttonDetectTypePHOTO = null;

  JLabel labelFlagImage = null;
  JTextField textFlagImage = null;

  JLabel labelDetMinArea = null;
  JIntegerField integertextDetMinArea = null;

  JLabel labelDetThresh = null;
  JCommaDoubleField commadoubletextDetThresh = null;

  JLabel labelAnalysisThresh = null;
  JCommaDoubleField commadoubletextAnalysisThresh = null;

  JCheckBox checkApplyFilter = null;
  JTextField textFilterName = null;

  JLabel labelDeblendThresh = null;
  JDoubleField doubletextDeblendThresh = null;

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
  JCommaDoubleField commadoubletextPhotAutoParams = null;

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
  JCommaIntegerField commaintegertextBackSize = null;

  JLabel labelBackFilter = null;
  JCommaIntegerField commaintegertextBackFilter = null;

  JLabel labelBackPhotoType = null;
  JPanel panelBackPhotoType = null;
  ButtonGroup radiobuttongroupBackPhotoType = null;
  JRadioButton radiobuttonBackPhotoTypeGLOBAL = null;
  JRadioButton radiobuttonBackPhotoTypeLOCAL = null;

  JLabel labelBackPhotoThick = null;
  JIntegerField inttextBackPhotoThick = null;

  // == CHECK_IMAGE TAB =====================================================
  JLabel  labelCheckImageType = null;
  JList listCheckImageType = null;
  JScrollPane listCheckImageTypeScroll = null;

  JLabel  labelCheckImageName = null;
  JTextField textCheckImageName = null;

  // == MEMORY TAB =====================================================
  JLabel labelMemObjStack = null;
  JIntegerField inttextMemObjStack = null;

  JLabel labelMemPixStack = null;
  JIntegerField inttextMemPixStack = null;

  JLabel labelMemBufSize = null;
  JIntegerField inttextMemBufSize = null;

  // == MISC TAB =====================================================

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

     /*
    // == CATALOGUE TAB =======================================================
    // Create all items for the panel...
    labelCatName = new JLabel("Catalogue Name:  ");
    textCatName = new JTextField("");
    textCatName.setToolTipText(
      "The filename for the output catalogue"
    );
    sizeComponent(textCatName, DEF_TEXTFIELD_DIMENSION);

    labelCatType = new JLabel("Catalogue Type:  ");
    String[] itemsCatType =
      { "NONE", "ASCII_HEAD", "ASCII", "FITS_1.0", "FITS_LDAC" };
    listCatType = new JList(itemsCatType);
    listCatType.setSelectedIndex(0);
    listCatType.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listCatTypeScroll = new JScrollPane(listCatType);
    sizeComponent(
      listCatTypeScroll,
      new Dimension (DEF_TEXTFIELD_WIDTH, DEF_TEXTFIELD_HEIGHT * 5)
    );

    labelParamName = new JLabel("Parameters File Name:  ");
    textParamName = new JTextField("");
    textParamName.setToolTipText(
      "The filename of the source catalogue"
    );
    sizeComponent(textParamName, DEF_TEXTFIELD_DIMENSION);

    // Now assemble the contents into the tabbed panel...
    JPanel panelContentsCatalogue = new JPanel(new GridBagLayout());

    addGB(
      panelContentsCatalogue, labelCatName,
      0, 0, GridBagConstraints.EAST, true
    );
    addGB(
      panelContentsCatalogue, textCatName,
      1, 0, GridBagConstraints.WEST, true
    );
    addGB(
      panelContentsCatalogue, labelCatType,
      0, 1, GridBagConstraints.NORTHEAST, true
    );
    addGB(
      panelContentsCatalogue, listCatTypeScroll,
      1, 1, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsCatalogue, labelParamName,
      0, 2, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsCatalogue, textParamName,
      1, 2, GridBagConstraints.WEST, true
    );

    JPanel panelCatalogue = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelCatalogue.add(panelContentsCatalogue);
    // ------------------------------------------------------------------------
      */

    // == EXTRACTION TAB ======================================================
    // Create all items for the panel...
    labelDetectType = new JLabel("Detection Type:  ");
    panelDetectType = new JPanel(new FlowLayout(FlowLayout.LEFT));
    radiobuttongroupDetectType = new ButtonGroup();
      radiobuttonDetectTypeCCD =
        new JRadioButton("CCD");
      radiobuttonDetectTypeCCD.setActionCommand("CCD");
      radiobuttongroupDetectType.add(radiobuttonDetectTypeCCD);
    panelDetectType.add(radiobuttonDetectTypeCCD);
      radiobuttonDetectTypePHOTO =
        new JRadioButton("Photo");
      radiobuttonDetectTypePHOTO.setActionCommand("PHOTO");
      radiobuttongroupDetectType.add(radiobuttonDetectTypePHOTO);
    panelDetectType.add(radiobuttonDetectTypePHOTO);
    radiobuttongroupDetectType.setSelected(
      radiobuttonDetectTypeCCD.getModel(), true
    );

    labelFlagImage = new JLabel("Flag Image:  ");
    textFlagImage = new JTextField("");
    textFlagImage.setToolTipText(
      "The filename of an input FLAG-image"
    );
    sizeComponent(textFlagImage, DEF_TEXTFIELD_DIMENSION);

    labelDetMinArea = new JLabel("Detect Min. Area:  ");
    integertextDetMinArea = new JIntegerField();
    integertextDetMinArea.setToolTipText(
      "Minimum number of pixels above threshold"
    );
    sizeComponent(integertextDetMinArea, DEF_TEXTFIELD_DIMENSION);

    labelDetThresh = new JLabel("Detect Threshold:  ");
    commadoubletextDetThresh = new JCommaDoubleField();
    commadoubletextDetThresh.setToolTipText(
      "<sigmas> or <threshold>, <ZP> in mag.arcsec-2"
    );
    sizeComponent(commadoubletextDetThresh, DEF_TEXTFIELD_DIMENSION);

    labelAnalysisThresh = new JLabel("Analysis Threshold:  ");
    commadoubletextAnalysisThresh = new JCommaDoubleField();
    commadoubletextAnalysisThresh.setToolTipText(
      "<sigmas> or <threshold>, <ZP> in mag.arcsec-2"
    );
    sizeComponent(commadoubletextAnalysisThresh, DEF_TEXTFIELD_DIMENSION);

    checkApplyFilter = new JCheckBox("  Apply Detection Filter:", true);
    checkApplyFilter.setActionCommand("APPLYFILTER");
      ToggleButtonControllerListener listenerApplyFilter =
        new ToggleButtonControllerListener();
    checkApplyFilter.addActionListener(listenerApplyFilter);
    textFilterName = new JTextField("");
    textFilterName.setToolTipText(
      "The filename of a detection filter to apply"
    );
    sizeComponent(textFilterName, DEF_TEXTFIELD_DIMENSION);
    // Auto-hide of text field, dependant on check box!
    listenerApplyFilter.addComponent(textFilterName);

    labelDeblendThresh = new JLabel("Deblending # Sub-Thresholds:  ");
    doubletextDeblendThresh = new JDoubleField();
    doubletextDeblendThresh.setToolTipText(
      "Number of deblending sub-thresholds"
    );
    sizeComponent(doubletextDeblendThresh, DEF_TEXTFIELD_DIMENSION);

    labelDeblendMinCont = new JLabel("Deblending Min. Contrast:  ");
    doubletextDeblendMinCont = new JDoubleField();
    doubletextDeblendMinCont.setToolTipText(
      "Minimum contrast parameter for deblending"
    );
    sizeComponent(doubletextDeblendMinCont, DEF_TEXTFIELD_DIMENSION);

    checkCleaning = new JCheckBox("  Clean with Efficiency:", true);
    checkCleaning.setActionCommand("CLEANING");
      ToggleButtonControllerListener listenerCleaning =
        new ToggleButtonControllerListener();
    checkCleaning.addActionListener(listenerCleaning);
    doubletextCleaning = new JDoubleField();
    doubletextCleaning.setToolTipText(
      "Cleaning efficiency for spurious detections"
    );
    sizeComponent(doubletextCleaning, DEF_TEXTFIELD_DIMENSION);

    // Auto-hide of text field, dependant on check box!
    listenerCleaning.addComponent(doubletextCleaning);

    labelMaskType = new JLabel("Mask Type:  ");
    panelMaskType = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelMaskType.setToolTipText(
      "Type of detection MASKing"
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
      panelContentsExtraction, labelDetectType,
      0, 0, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsExtraction, panelDetectType,
      1, 0, GridBagConstraints.WEST, false
    );

    addGB(
      panelContentsExtraction, labelFlagImage,
      0, 1, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsExtraction, textFlagImage,
      1, 1, GridBagConstraints.WEST, false
    );

    addGB(
      panelContentsExtraction, labelDetMinArea,
      0, 2, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsExtraction, integertextDetMinArea,
      1, 2, GridBagConstraints.WEST, false
    );

    addGB(
      panelContentsExtraction, labelDetThresh,
      0, 3, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsExtraction, commadoubletextDetThresh,
      1, 3, GridBagConstraints.WEST, false
    );

    addGB(
      panelContentsExtraction, labelAnalysisThresh,
      0, 4, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsExtraction, commadoubletextAnalysisThresh,
      1, 4, GridBagConstraints.WEST, false
    );

    addGB(
      panelContentsExtraction, checkApplyFilter,
      0, 5, GridBagConstraints.EAST, true
    );

    addGB(
      panelContentsExtraction, textFilterName,
      1, 5, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsExtraction, labelDeblendThresh,
      0, 6, GridBagConstraints.EAST, true
    );

    addGB(
      panelContentsExtraction, doubletextDeblendThresh,
      1, 6, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsExtraction, labelDeblendMinCont,
      0, 7, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsExtraction, doubletextDeblendMinCont,
      1, 7, GridBagConstraints.WEST, false
    );

    addGB(
      panelContentsExtraction, checkCleaning,
      0, 8, GridBagConstraints.EAST, true
    );

    addGB(
      panelContentsExtraction, doubletextCleaning,
      1, 8, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsExtraction, labelMaskType,
      0, 9, GridBagConstraints.EAST, true
    );

    addGB(
      panelContentsExtraction, panelMaskType,
      1, 9, GridBagConstraints.WEST, true
    );

    JPanel panelExtraction = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelExtraction.add(panelContentsExtraction);
    // ------------------------------------------------------------------------


    // == PHOTOMETRY TAB ======================================================
    // Create all items for the panel...
    labelPhotApertures = new JLabel("Photo Aperture(s):  ");
    inttextPhotApertures = new JIntegerField();
    inttextPhotApertures.setToolTipText(
      "MAG_APER aperture diameter(s) in pixels"
    );
    sizeComponent(inttextPhotApertures, DEF_TEXTFIELD_DIMENSION);

    labelPhotAutoParams = new JLabel("Photo Auto Parameters:  ");
    commadoubletextPhotAutoParams = new JCommaDoubleField();
    commadoubletextPhotAutoParams.setToolTipText(
      "MAG_AUTO parameters: <Kron_fact>, <min_radius>"
    );
    sizeComponent(commadoubletextPhotAutoParams, DEF_TEXTFIELD_DIMENSION);

    labelSatLevel = new JLabel("Saturation Level:  ");
    doubletextSatLevel = new JDoubleField();
    doubletextSatLevel.setToolTipText(
      "Level (in ADUs) at which arises saturation"
    );
    sizeComponent(doubletextSatLevel, DEF_TEXTFIELD_DIMENSION);

    labelMagZero = new JLabel("Magnitude Zero-point:  ");
    doubletextMagZero = new JDoubleField();
    sizeComponent(doubletextMagZero, DEF_TEXTFIELD_DIMENSION);

    labelMagGamma = new JLabel("Gamma of Emulsion:  ");
    doubletextMagGamma = new JDoubleField();
    doubletextMagGamma.setToolTipText(
      "Gamma of emulsion (for photographic scans)"
    );
    sizeComponent(doubletextMagGamma, DEF_TEXTFIELD_DIMENSION);

    labelGain = new JLabel("Detector Gain:  ");
    doubletextGain = new JDoubleField();
    doubletextGain.setToolTipText(
      "Detector gain in e-/ADU"
    );
    sizeComponent(doubletextGain, DEF_TEXTFIELD_DIMENSION);

    labelPixelScale = new JLabel("Pixel Scale:  ");
    doubletextPixelScale = new JDoubleField();
    doubletextPixelScale.setToolTipText(
      "Size of pixel in arcsec (0 = use FITS info)"
    );
    sizeComponent(doubletextPixelScale, DEF_TEXTFIELD_DIMENSION);

    // Now assemble the contents into the tabbed panel...
    JPanel panelContentsPhotometry = new JPanel(new GridBagLayout());

    addGB(
      panelContentsPhotometry, labelPhotApertures,
      0, 0, GridBagConstraints.EAST, true
    );

    addGB(
      panelContentsPhotometry, inttextPhotApertures,
      1, 0, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsPhotometry, labelPhotAutoParams,
      0, 1, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsPhotometry, commadoubletextPhotAutoParams,
      1, 1, GridBagConstraints.WEST, false
    );

    addGB(
      panelContentsPhotometry, labelSatLevel,
      0, 2, GridBagConstraints.EAST, true
    );

    addGB(
      panelContentsPhotometry, doubletextSatLevel,
      1, 2, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsPhotometry, labelMagZero,
      0, 3, GridBagConstraints.EAST, true
    );

    addGB(
      panelContentsPhotometry, doubletextMagZero,
      1, 3, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsPhotometry, labelMagGamma,
      0, 4, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsPhotometry, doubletextMagGamma,
      1, 4, GridBagConstraints.WEST, false
    );

    addGB(
      panelContentsPhotometry, labelGain,
      0, 5, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsPhotometry, doubletextGain,
      1, 5, GridBagConstraints.WEST, false
    );

    addGB(
      panelContentsPhotometry, labelPixelScale,
      0, 6, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsPhotometry, doubletextPixelScale,
      1, 6, GridBagConstraints.WEST, false
    );

    JPanel panelPhotometry = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelPhotometry.add(panelContentsPhotometry);
    // ------------------------------------------------------------------------


    // == STAR_GALAXY_SEPARATION TAB ======================================================
    // Create all items for the panel...
    labelSeeingFWHM = new JLabel("Seeing FWHM:  ");
    doubletextSeeingFWHM = new JDoubleField();
    doubletextSeeingFWHM.setToolTipText(
      "Stellar FWHM (in arcsec)"
    );
    sizeComponent(doubletextSeeingFWHM, DEF_TEXTFIELD_DIMENSION);

    labelStarNNWName = new JLabel("Star Neural-Net Name:  ");
    textStarNNWName = new JTextField();
    textStarNNWName.setToolTipText(
      "The filename of the Neural-Network_Weight table"
    );
    sizeComponent(textStarNNWName, DEF_TEXTFIELD_DIMENSION);

    // Now assemble the contents into the tabbed panel...
    JPanel panelContentsStar_Galaxy_Separation = new JPanel(new GridBagLayout());

    addGB(
      panelContentsStar_Galaxy_Separation, labelSeeingFWHM,
      0, 0, GridBagConstraints.EAST, true
    );

    addGB(
      panelContentsStar_Galaxy_Separation, doubletextSeeingFWHM,
      1, 0, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsStar_Galaxy_Separation, labelStarNNWName,
      0, 1, GridBagConstraints.EAST, true
    );

    addGB(
      panelContentsStar_Galaxy_Separation, textStarNNWName,
      1, 1, GridBagConstraints.WEST, true
    );

    JPanel panelStar_Galaxy_Separation = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelStar_Galaxy_Separation.add(panelContentsStar_Galaxy_Separation);

    // ------------------------------------------------------------------------


    // == BACKGROUND TAB ======================================================
    // Create all items for the panel...
    labelBackSize = new JLabel("Background mesh:  ");
    commaintegertextBackSize = new JCommaIntegerField();
    commaintegertextBackSize.setToolTipText(
      "<size> or <width>, <height>"
    );
    sizeComponent(commaintegertextBackSize, DEF_TEXTFIELD_DIMENSION);

    labelBackFilter = new JLabel("Background filter:  ");
    commaintegertextBackFilter = new JCommaIntegerField();
    commaintegertextBackSize.setToolTipText(
      "<size> or <width>, <height>"
    );
    sizeComponent(commaintegertextBackFilter, DEF_TEXTFIELD_DIMENSION);

    labelBackPhotoType = new JLabel("Photo Type:  ");
    panelBackPhotoType = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelBackPhotoType.setToolTipText(
      "Background photo type"
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
      "Thickness of the background LOCAL annulus"
    );
    sizeComponent(inttextBackPhotoThick, DEF_TEXTFIELD_DIMENSION);

    // Now assemble the contents into the tabbed panel...
    JPanel panelContentsBackground = new JPanel(new GridBagLayout());

    addGB(
      panelContentsBackground, labelBackSize,
      0, 0, GridBagConstraints.EAST, true
    );

    addGB(
      panelContentsBackground, commaintegertextBackSize,
      1, 0, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsBackground, labelBackFilter,
      0, 1, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsBackground, commaintegertextBackFilter,
      1, 1, GridBagConstraints.WEST, false
    );

    addGB(
      panelContentsBackground, labelBackPhotoType,
      0, 2, GridBagConstraints.EAST, true
    );

    addGB(
      panelContentsBackground, panelBackPhotoType,
      1, 2, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsBackground, labelBackPhotoThick,
      0, 3, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsBackground, inttextBackPhotoThick,
      1, 3, GridBagConstraints.WEST, false
    );

    JPanel panelBackground = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelBackground.add(panelContentsBackground);

    // ------------------------------------------------------------------------


    // == CHECK_IMAGE TAB =====================================================
    // Create all items for the panel...

    labelCheckImageType = new JLabel("Check Image Type:  ");
    String[] itemsCheckImageType =
      {
        "NONE", "MINIBACKGROUND", "BACKGROUND", "-BACKGROUND", "OBJECTS",
        "-OJBECTS", "SEGMENTATION", "APERTURES", "FILTERED"
      };
    listCheckImageType = new JList(itemsCheckImageType);
    listCheckImageType.setSelectedIndex(0);
    listCheckImageType.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listCheckImageTypeScroll = new JScrollPane(listCheckImageType);
    sizeComponent(
      listCheckImageTypeScroll,
      new Dimension (DEF_TEXTFIELD_WIDTH, DEF_TEXTFIELD_HEIGHT * 9)
    );

    labelCheckImageName = new JLabel("Check Image Name:  ");
    textCheckImageName = new JTextField();
    textCheckImageName.setToolTipText(
      "The filename of the check image"
    );
    sizeComponent(textCheckImageName, DEF_TEXTFIELD_DIMENSION);

    // Now assemble the contents into the tabbed panel...
    JPanel panelContentsCheck_Image = new JPanel(new GridBagLayout());

    addGB(
      panelContentsCheck_Image, labelCheckImageType,
      0, 0, GridBagConstraints.NORTHEAST, true
    );

    addGB(
      panelContentsCheck_Image, listCheckImageTypeScroll,
      1, 0, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsCheck_Image, labelCheckImageName,
      0, 1, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsCheck_Image, textCheckImageName,
      1, 1, GridBagConstraints.WEST, false
    );

    JPanel panelCheck_Image = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelCheck_Image.add(panelContentsCheck_Image);

    // ------------------------------------------------------------------------


    // == MEMORY TAB =====================================================
    // Create all items for the panel...

    labelMemObjStack = new JLabel("Object Stack:  ");
    inttextMemObjStack = new JIntegerField();
    inttextMemObjStack.setToolTipText(
      "Number of objects in object stack"
    );
    sizeComponent(inttextMemObjStack, DEF_TEXTFIELD_DIMENSION);

    labelMemPixStack = new JLabel("Pixel Stack:  ");
    inttextMemPixStack = new JIntegerField();
    inttextMemPixStack.setToolTipText(
      "Number of pixels in pixel stack"
    );
    sizeComponent(inttextMemPixStack, DEF_TEXTFIELD_DIMENSION);

    labelMemBufSize = new JLabel("Buffer Size:  ");
    inttextMemBufSize = new JIntegerField();
    inttextMemBufSize.setToolTipText(
      "Number of lines in buffer"
    );
    sizeComponent(inttextMemBufSize, DEF_TEXTFIELD_DIMENSION);

    // Now assemble the contents into the tabbed panel...
    JPanel panelContentsMemory = new JPanel(new GridBagLayout());

    addGB(
      panelContentsMemory, labelMemObjStack,
      0, 0, GridBagConstraints.NORTHEAST, true
    );

    addGB(
      panelContentsMemory, inttextMemObjStack,
      1, 0, GridBagConstraints.WEST, true
    );

    addGB(
      panelContentsMemory, labelMemPixStack,
      0, 1, GridBagConstraints.NORTHEAST, false
    );

    addGB(
      panelContentsMemory, inttextMemPixStack,
      1, 1, GridBagConstraints.WEST, false
    );

    addGB(
      panelContentsMemory, labelMemBufSize,
      0, 2, GridBagConstraints.NORTHEAST, false
    );

    addGB(
      panelContentsMemory, inttextMemBufSize,
      1, 2, GridBagConstraints.WEST, false
    );

    JPanel panelMemory = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelMemory.add(panelContentsMemory);

    // ------------------------------------------------------------------------


    // == MISC TAB =====================================================
    // Create all items for the panel...

    labelVerboseType = new JLabel("Verbose Type:  ");
    panelVerboseType = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelVerboseType.setToolTipText(
      "Verbosity"
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
    JPanel panelContentsMisc = new JPanel(new GridBagLayout());

    addGB(
      panelContentsMisc, labelVerboseType,
      0, 0, GridBagConstraints.EAST, false
    );

    addGB(
      panelContentsMisc, panelVerboseType,
      1, 0, GridBagConstraints.WEST, false
    );

    JPanel panelMisc = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelMisc.add(panelContentsMisc);

    // ------------------------------------------------------------------------


    // Assemble all the above panels into a nice tabbed display
//    JTabbedPane tabbedPane = new JTabbedPane();
    JTabbedPane tabbedPane = this;  //So we can get direct access to the panes from outside - MCH

    tabbedPane.setTabPlacement(JTabbedPane.TOP);

//    tabbedPane.addTab("Catalogue", panelCatalogue);
    tabbedPane.addTab("Extraction", panelExtraction);
    tabbedPane.addTab("Photometry", panelPhotometry);
    tabbedPane.addTab("Separation", panelStar_Galaxy_Separation);
    tabbedPane.addTab("Background", panelBackground);
    tabbedPane.addTab("Check Image", panelCheck_Image);
    tabbedPane.addTab("Memory", panelMemory);
    tabbedPane.addTab("Misc.", panelMisc);

    // Use a BorderLayout to make the tabbedPane fill the area...
    //this.setLayout(new BorderLayout());
    //this.add(tabbedPane, BorderLayout.CENTER);
  };

/**
 * A simple method to force a JComponent to become a specific size.
 *
 * @author Alan Maxwell
 */
  public static void sizeComponent(JComponent item, Dimension dimension)
  {
    item.setMinimumSize(dimension);
    item.setMaximumSize(dimension);
    item.setPreferredSize(dimension);
  };

/**
 * A simple method to easily add a Component to a (GridBagLayout)
 * Container - note that if the container does not have a GridBagLayout
 * set as its layout manager, this method will update the layout manager.
 *
 * @author Alan Maxwell
 */
  public void addGB(Container cont, Component comp, int x, int y,
                           int anchor, boolean spaceAbove)
  {
    if ((cont.getLayout() instanceof GridBagLayout) == false)
      cont.setLayout(new GridBagLayout());
    {
      if (spaceAbove == true)
      {
        constraints.insets = DEF_VSPACE_INSETS;
      }
      else
      {
        constraints.insets = DEF_INSETS;
      };

      constraints.anchor = anchor;
      constraints.gridx = x; constraints.gridy = y;
      cont.add(comp, constraints);
    };
  };

/**
 * Returns the FIRST 'text' child element (as a String) of the provided
 * Element XML node.
 *
 * Returns null if the element cannot be found or if there is an error,
 * or no child 'text' element.
 *
 * @author Alan Maxwell
 */
  protected String getXmlElementValueText(Element anElement)
  {
    NodeList tempNodeList = null;
    Node tempNode = null;

    if (anElement == null)
    {
      return null;
    };

    tempNodeList = anElement.getChildNodes();

    if (tempNodeList.getLength() < 1)
    {
      return null;
    };

    // Out of the child nodes, find the first 'text' node...
    for (int nodeLoop = 0; nodeLoop < tempNodeList.getLength(); nodeLoop++ )
    {
      tempNode = tempNodeList.item(nodeLoop);

      if (tempNode.getNodeType() == Node.TEXT_NODE)
      {
        return tempNode.getNodeValue();
      };
    };

    // If we get here, we didn't find a child 'text' node...
    return null;
  };

/**
 * Returns the FIRST 'text' child Element (as a String)
 * belonging to the FIRST matching (tagName) XML Element falling
 * within the sub-tree having 'anElement' as its root.
 *
 * Asking for getXmlElementValue(Element:animals, "monkey") would return:
 *    "Bubbles"
 * When provided with the following XML:
 *    <animals>
 *      <monkey>
 *        Bubbles
 *      </monkey>
 *    </animals>
 *
 * Returns null if the element cannot be found or if there is an error.
 *
 * @author Alan Maxwell
 */
  protected String getXmlElementValue(Element anElement, String tagName)
  {
    NodeList tempNodeList = null;
    Node tempNode = null;

    tempNodeList = anElement.getElementsByTagName(tagName);

    if (tempNodeList.getLength() < 1)
    {
      return null; // Can't find nodes, return null (not empty string)
    };

    // We have found at least one matching element, now get all of
    // the child elements belonging to that one node (or first if more)...
    return getXmlElementValueText((Element) tempNodeList.item(0));
  };

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
 */
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
    };

    return result;
  };

/**
 * Populates the tabbed pages with values taken from the provided XML File!
 *
 * @author Alan Maxwell
 */
  public void loadXml(File xmlFile)
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setIgnoringElementContentWhitespace(true);
      dbf.setNamespaceAware(true);
      DocumentBuilder builder = dbf.newDocumentBuilder();
      Document doc = builder.parse(xmlFile);

      loadXml(doc);
    }
    catch (Exception e)
    {
      System.err.println(
        "WARNING: AVODemoDialogPanel: loadXml(File): \n" +
        "         Cannot load XML from provided file!"
      );
      // Do nothing with exceptions, perhaps later flag on GUI somehow???
    }
  };

/**
 * Populates the tabbed pages with values taken from the provided XML String!
 *
 * @author Alan Maxwell
 */
  public void loadXml(String xmlString)
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setIgnoringElementContentWhitespace(true);
      dbf.setNamespaceAware(true);
      DocumentBuilder builder = dbf.newDocumentBuilder();
      Document doc = builder.parse(xmlString);

      loadXml(doc);
    }
    catch (Exception e)
    {
      System.err.println(
        "WARNING: AVODemoDialogPanel: loadXml(String): \n" +
        "         Cannot load XML from provided string!"
      );
      // Do nothing with exceptions, perhaps later flag on GUI somehow???
    }
  };

/**
 * Populates the tabbed pages with values taken from the provided XML Document!
 *
 * @author Alan Maxwell
 */
  public void loadXml(Document xmlDoc)
  {
    Element docRoot = (Element) xmlDoc.getDocumentElement();

    String returnValue = null;
    String [] returnValues = null;

    // Go through the XML extracting the config information...

    returnValue = getXmlElementValue(docRoot, "CATALOG_NAME");
    if (returnValue != null)
    {
      textCatName.setText(returnValue);
    };

    returnValue = getXmlElementValue(docRoot, "CATALOG_TYPE");
    if (returnValue != null)
    {
      listCatType.setSelectedValue(returnValue, true);
    };

    returnValue = getXmlElementValue(docRoot, "PARAMETERS_NAME");
    if (returnValue != null)
    {
      textParamName.setText(returnValue);
    };

    returnValue = getXmlElementValue(docRoot, "DETECT_TYPE");
    if (returnValue != null)
    {
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
    };

    returnValues = getXmlElementArgs(docRoot, "FLAG_IMAGE");
    if (returnValues != null)
    {
      if (returnValues.length > 0)
      {
        textFlagImage.setText(returnValues[0]);
      };

      for (int loop = 1; loop < returnValues.length ; loop++ )
      {
        textFlagImage.setText(
          textFlagImage.getText() + ", " + returnValues[loop]
        );
      };
    };

    returnValue = getXmlElementValue(docRoot, "DETECT_MINAREA");
    if (returnValue != null)
    {
      integertextDetMinArea.setText(returnValue);
    };

    returnValues = getXmlElementArgs(docRoot, "DETECT_THRESH");
    if (returnValues != null)
    {
      if (returnValues.length > 1)
      {
        commadoubletextDetThresh.setText(
          returnValues[0] + ", " + returnValues[1]
        );
      }
      else if (returnValues.length > 0)
      {
        commadoubletextDetThresh.setText(
          returnValues[0]
        );
      };
    };

    returnValues = getXmlElementArgs(docRoot, "ANALYSIS_THRESH");
    if (returnValues != null)
    {
      if (returnValues.length > 1)
      {
        commadoubletextAnalysisThresh.setText(
          returnValues[0] + ", " + returnValues[1]
        );
      }
      else if (returnValues.length > 0)
      {
        commadoubletextAnalysisThresh.setText(
          returnValues[0]
        );
      };
    };

    returnValue = getXmlElementValue(docRoot, "FILTER");
    if (returnValue != null)
    {
      checkApplyFilter.setSelected(returnValue.equalsIgnoreCase("true"));
      // Use 'doClick()' twice to toggle the button and fire an 'actionEvent'
      // note that 'setSelected() does NOT fire this event!
      checkApplyFilter.doClick();
      checkApplyFilter.doClick();
    };

    returnValue = getXmlElementValue(docRoot, "FILTER_NAME");
    if (returnValue != null)
    {
      textFilterName.setText(returnValue);
    };

    returnValue = getXmlElementValue(docRoot, "DEBLEND_NTHRESH");
    if (returnValue != null)
    {
      doubletextDeblendThresh.setText(returnValue);
    };

    returnValue = getXmlElementValue(docRoot, "DEBLEND_MINCONT");
    if (returnValue != null)
    {
      doubletextDeblendMinCont.setText(returnValue);
    };

    returnValue = getXmlElementValue(docRoot, "CLEAN");
    if (returnValue != null)
    {
      checkCleaning.setSelected(returnValue.equalsIgnoreCase("true"));
      // Use 'doClick()' twice to toggle the button and fire an 'actionEvent'
      // note that 'setSelected() does NOT fire this event!
      checkCleaning.doClick();
      checkCleaning.doClick();
    };

    returnValue = getXmlElementValue(docRoot, "CLEAN_PARAM");
    if (returnValue != null)
    {
      doubletextCleaning.setText(returnValue);
    };

    returnValue = getXmlElementValue(docRoot, "MASK_TYPE");
    if (returnValue != null)
    {
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
      "<sex:SExtractorDoc \n" +
      "  xmlns:sex=\"http://www.astrogrid.org/namespace/SExtractor_2_2_2\" \n" +
      "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
      "  xsi:schemaLocation=\"http://astrogrid.cam.ac.uk/namespace/Schemas/SExtractor_2_2_2.xsd\"> \n"
    );

    // Generate content from UI dialog elements:
    xmlResult.append(
      "  <CATALOGUE_NAME> \n" +
      "    <arg>" + textCatName.getText() + "</arg> \n" +
      "  </CATALOGUE_NAME> \n\n"
    );

    xmlResult.append(
      "  <CATALOGUE_TYPE> \n" +
      "    <arg>" + listCatType.getSelectedValue() + "</arg> \n" +
      "  </CATALOGUE_TYPE> \n\n"
    );

    xmlResult.append(
      "  <PARAMETERS_NAME> \n" +
      "    <arg>" + textParamName.getText() + "</arg> \n" +
      "  </PARAMETERS_NAME> \n\n"
    );

    xmlResult.append(
      "  <DETECT_TYPE> \n" +
      "    <arg>" +
        radiobuttongroupDetectType.getSelection().getActionCommand() +
        "</arg> \n" +
      "  </DETECT_TYPE> \n\n"
    );

    xmlResult.append(
      "  <FLAG_IMAGE> \n" +
      "    <arg>" + textFlagImage.getText() + "</arg> \n" +
      "  </FLAG_IMAGE> \n\n"
    );

    xmlResult.append(
      "  <DETECT_MINAREA> \n" +
      "    <arg>" + integertextDetMinArea.getText() + "</arg> \n" +
      "  </DETECT_MINAREA> \n\n"
    );

    xmlResult.append(
      "  <DETECT_THRESH> \n" +
      "    <arg>" + commadoubletextDetThresh.getText(0) + "</arg> \n"
    );

    if (commadoubletextDetThresh.getText(1).length() > 0)
    {
      xmlResult.append(
        "    <arg>" + commadoubletextDetThresh.getText(1) + "</arg> \n"
      );
    };

    xmlResult.append(
      "  </DETECT_THRESH> \n\n"
    );

    xmlResult.append(
      "  <ANALYSIS_THRESH> \n" +
      "    <arg>" + commadoubletextAnalysisThresh.getText(0) + "</arg> \n"
    );

    if (commadoubletextAnalysisThresh.getText(1).length() > 0)
    {
      xmlResult.append(
        "    <arg>" + commadoubletextAnalysisThresh.getText(1) + "</arg> \n"
      );
    };

    xmlResult.append(
      "  </ANALYSIS_THRESH> \n\n"
    );

    xmlResult.append(
      "  <FILTER> \n" +
      "    <arg>"
    );

    if (checkApplyFilter.isSelected())
    {
      xmlResult.append("true");
    }
    else
    {
      xmlResult.append("false");
    };

    xmlResult.append(
      "</arg> \n" +
      "  </FILTER> \n\n"
    );

    xmlResult.append(
      "  <FILTER_NAME> \n" +
      "    <arg>" + textFilterName.getText() + "</arg> \n" +
      "  </FILTER_NAME> \n\n"
    );

    xmlResult.append(
      "  <DEBLEND_NTHRESH> \n" +
      "    <arg>" + doubletextDeblendThresh.getText() + "</arg> \n" +
      "  </DEBLEND_NTHRESH> \n\n"
    );

    xmlResult.append(
      "  <DEBLEND_MINCONT> \n" +
      "    <arg>" + doubletextDeblendMinCont.getText() + "</arg> \n" +
      "  </DEBLEND_MINCONT> \n\n"
    );

    xmlResult.append(
      "  <CLEAN> \n" +
      "    <arg>"
    );

    if (checkCleaning.isSelected())
    {
      xmlResult.append("true");
    }
    else
    {
      xmlResult.append("false");
    };

    xmlResult.append(
      "</arg> \n" +
      "  </CLEAN> \n\n"
    );

    xmlResult.append(
      "  <CLEAN_PARAM> \n" +
      "    <arg>" + doubletextCleaning.getText() + "</arg> \n" +
      "  </CLEAN_PARAM> \n\n"
    );

    xmlResult.append(
      "  <MASK_TYPE> \n" +
      "    <arg>" +
        radiobuttongroupMaskType.getSelection().getActionCommand() +
        "</arg> \n" +
      "  </MASK_TYPE> \n\n"
    );

    xmlResult.append(
      "  <PHOT_APERTURES> \n" +
      "    <arg>" + inttextPhotApertures.getText() + "</arg> \n" +
      "  </PHOT_APERTURES> \n\n"
    );

    xmlResult.append(
      "  <PHOT_AUTOPARAMS> \n" +
      "    <arg>" + commadoubletextPhotAutoParams.getText(0) + "</arg> \n"
    );

    if (commadoubletextPhotAutoParams.getText(1).length() > 0)
    {
      xmlResult.append(
        "    <arg>" + commadoubletextPhotAutoParams.getText(1) + "</arg> \n"
      );
    };

    xmlResult.append(
      "  </PHOT_AUTOPARAMS> \n\n"
    );

    xmlResult.append(
      "  <SATUR_LEVEL> \n" +
      "    <arg>" + doubletextSatLevel.getText() + "</arg> \n" +
      "  </SATUR_LEVEL> \n\n"
    );

    xmlResult.append(
      "  <MAG_ZEROPOINT> \n" +
      "    <arg>" + doubletextMagZero.getText() + "</arg> \n" +
      "  </MAG_ZEROPOINT> \n\n"
    );

    xmlResult.append(
      "  <MAG_GAMMA> \n" +
      "    <arg>" + doubletextMagGamma.getText() + "</arg> \n" +
      "  </MAG_GAMMA> \n\n"
    );

    xmlResult.append(
      "  <GAIN> \n" +
      "    <arg>" + doubletextGain.getText() + "</arg> \n" +
      "  </GAIN> \n\n"
    );

    xmlResult.append(
      "  <PIXEL_SCALE> \n" +
      "    <arg>" + doubletextPixelScale.getText() + "</arg> \n" +
      "  </PIXEL_SCALE> \n\n"
    );

    xmlResult.append(
      "  <SEEING_FWHM> \n" +
      "    <arg>" + doubletextSeeingFWHM.getText() + "</arg> \n" +
      "  </SEEING_FWHM> \n\n"
    );

    xmlResult.append(
      "  <STARNNW_NAME> \n" +
      "    <arg>" + textStarNNWName.getText() + "</arg> \n" +
      "  </STARNNW_NAME> \n\n"
    );

    xmlResult.append(
      "  <BACK_SIZE> \n" +
      "    <arg>" + commaintegertextBackSize.getText(0) + "</arg> \n"
    );

    if (commaintegertextBackSize.getText(1).length() > 0)
    {
      xmlResult.append(
        "    <arg>" + commaintegertextBackSize.getText(1) + "</arg> \n"
      );
    };

    xmlResult.append(
      "  </BACK_SIZE> \n\n"
    );

    xmlResult.append(
      "  <BACK_FILTERSIZE> \n" +
      "    <arg>" + commaintegertextBackFilter.getText(0) + "</arg> \n"
    );

    if (commaintegertextBackFilter.getText(1).length() > 0)
    {
      xmlResult.append(
        "    <arg>" + commaintegertextBackFilter.getText(1) + "</arg> \n"
      );
    };

    xmlResult.append(
      "  </BACK_FILTERSIZE> \n\n"
    );

    xmlResult.append(
      "  <BACKPHOTO_TYPE> \n" +
      "    <arg>" +
       radiobuttongroupBackPhotoType.getSelection().getActionCommand() +
        "</arg> \n" +
      "  </BACKPHOTO_TYPE> \n\n"
    );

    xmlResult.append(
      "  <BACKPHOTO_THICK> \n" +
      "    <arg>" + inttextBackPhotoThick.getText() + "</arg> \n" +
      "  </BACKPHOTO_THICK> \n\n"
    );

    xmlResult.append(
      "  <CHECKIMAGE_TYPE> \n" +
      "    <arg>" + listCheckImageType.getSelectedValue() + "</arg> \n" +
      "  </CHECKIMAGE_TYPE> \n\n"
    );

    xmlResult.append(
      "  <CHECKIMAGE_NAME> \n" +
      "    <arg>" + textCheckImageName.getText() + "</arg> \n" +
      "  </CHECKIMAGE_NAME> \n\n"
    );

    xmlResult.append(
      "  <MEMORY_OBJSTACK> \n" +
      "    <arg>" + inttextMemObjStack.getText() + "</arg> \n" +
      "  </MEMORY_OBJSTACK> \n\n"
    );

    xmlResult.append(
      "  <MEMORY_PIXSTACK> \n" +
      "    <arg>" + inttextMemPixStack.getText() + "</arg> \n" +
      "  </MEMORY_PIXSTACK> \n\n"
    );

    xmlResult.append(
      "  <MEMORY_BUFSIZE> \n" +
      "    <arg>" + inttextMemBufSize.getText() + "</arg> \n" +
      "  </MEMORY_BUFSIZE> \n\n"
    );

    xmlResult.append(
      "  <VERBOSE_TYPE> \n" +
      "    <arg>" +
        radiobuttongroupVerboseType.getSelection().getActionCommand() +
        "</arg> \n" +
      "  </VERBOSE_TYPE> \n\n"
    );

    xmlResult.append(
      "</sex:SExtractorDoc> \n"
    );

    return xmlResult.toString();
  };

/**
 * Returns all of the tabbed pages' options encoded as an XML Document.
 *
 * Returns null if this cannot be done!
 *
 * @author Alan Maxwell
 */
  public Document toXmlDocument()
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setIgnoringElementContentWhitespace(true);
      dbf.setNamespaceAware(true);
      DocumentBuilder builder = dbf.newDocumentBuilder();

      return builder.parse(toXmlString());
    }
    catch (Exception e)
    {
      // If we get any problems making the document, return null
      return null;
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

   /**
    * Carries out actions based on button or menu/etc events.  This means that
    * a UI that contains this component can create controls with the commands
    * given by this component (eg LOAD_CMD) and set this as the action listener,
    * eg:
    * <pre>
    *     JButton loadButton = new JButton("Load");
    *     loadButton.setActionCommand(TemplateEditorPanel.LOAD_CMD);
    *     loadButton.addActionListener(templateEditor);
    *     toolbar.add(loadButton);
    * </pre>
    */
   public void actionPerformed(java.awt.event.ActionEvent e)
   {
      if (e.getActionCommand().equals(SAVEAS_CMD))
      {
         if (chooser.showSaveDialog(this) == chooser.APPROVE_OPTION)
         {
            saveXmlFile(chooser.getSelectedFile());
         }
      }

      if (e.getActionCommand().equals(LOAD_CMD))
      {
         if (chooser.showOpenDialog(this) == chooser.APPROVE_OPTION)
         {
            loadXml(chooser.getSelectedFile());
         }
      }
   }

};
