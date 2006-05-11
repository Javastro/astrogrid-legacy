/*$Id: AstroScopeLauncherImpl.java,v 1.40 2006/05/11 11:40:59 KevinBenson Exp $
 * Created on 12-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.SiapInformation;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.acr.nvo.ConeInformation;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.comp.PositionTextField;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;
import org.astrogrid.desktop.modules.ui.scope.DalProtocol;
import org.astrogrid.desktop.modules.ui.scope.DalProtocolManager;
import org.astrogrid.desktop.modules.ui.scope.HyperbolicVizualization;
import org.astrogrid.desktop.modules.ui.scope.ImageLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.QueryResultSummarizer;
import org.astrogrid.desktop.modules.ui.scope.Retriever;
import org.astrogrid.desktop.modules.ui.scope.SaveNodesButton;
import org.astrogrid.desktop.modules.ui.scope.SiapProtocol;
import org.astrogrid.desktop.modules.ui.scope.SsapProtocol;
import org.astrogrid.desktop.modules.ui.scope.VOSpecButton;
import org.astrogrid.desktop.modules.ui.scope.VizModel;
import org.astrogrid.desktop.modules.ui.scope.Vizualization;
import org.astrogrid.desktop.modules.ui.scope.VizualizationManager;
import org.astrogrid.desktop.modules.ui.scope.VotableLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.WindowedRadialVizualization;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;

import com.l2fprod.common.swing.JButtonBar;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;



/** Implementation of the Datascipe launcher
 * 
 * @todo tidy up scrappy get position code - in particular, report errors from simbad correctly - at moment,
 * if simbad service is down, user is told 'you must enter a name known to simbad' - which is very misleading.
 * @todo hyperbolic doesn't always update to display nodes-to-download as yellow. need to add a redraw in somewhere. don't want to redraw too often though.
 * @todo again a position stuff with scaling doing a lot of converting string to double that could get rid of later.
 */
public class AstroScopeLauncherImpl extends UIComponentImpl 
    implements AstroScope, ActionListener, PlasticListener, PlasticWrapper {
   
/** extends the plastic mesagehandler to display new buttons */
    protected class AstroscopePlasticMessageHandler extends ApplicationRegisteredPlasticMessageHandler {

        private AstroscopePlasticMessageHandler(UIComponent parent, PlasticWrapper wrapper, Container container) {
            super(parent, wrapper, container);
        }

        // define this method to control what happens when a new application registers.
        protected Component[] buildComponents(URI applicationId, String name, String description, URL iconURL, URI[] messages) {
            List results= new ArrayList();
            if (ArrayUtils.contains(messages,CommonMessageConstants.VOTABLE_LOAD_FROM_URL)) {
                results.add(
                        new VotableLoadPlasticButton(applicationId,name,description, iconURL,vizModel.getSelectionFocusSet(),
                                AstroScopeLauncherImpl.this,AstroScopeLauncherImpl.this)                         
                        );
            }
            if (ArrayUtils.contains(messages,CommonMessageConstants.FITS_LOAD_FROM_URL)) {                    
                results.add(
                        new ImageLoadPlasticButton(applicationId,name,description,iconURL,vizModel.getSelectionFocusSet(),
                                AstroScopeLauncherImpl.this,AstroScopeLauncherImpl.this)                           
                        );
            }                
            return (Component[])results.toArray(new Component[results.size()]);
            
        }
    }


    public static final int TOOLTIP_WRAP_LENGTH = 50;
    
    /**
     * Commons Logger for this class
     */
    static final Log logger = LogFactory.getLog(AstroScopeLauncherImpl.class);

    //Various gui components.
    private PositionTextField posText;           
    private JButton reFocusTopButton;   
    private JButton clearButton;
    private JButton switchButton;
    JRadioButton degreesRadio;
    JRadioButton sexaRadio;
    private JComboBox historyCombo;
    private PositionTextField regionText;
    private FlipButton submitButton;
           
    //name resolver.
    private final Sesame ses;

    // set of data retrieval components 
    private final DalProtocolManager protocols;
    
    //shared data model, selections, etc.
    private final VizModel vizModel;
    // set of vizualization components
    private final VizualizationManager vizualizations;

    // used to generate a new plastic id for each astroscope window
    private static int UNQ_ID = 0;
    private final URI myPlasticID;
    // list of plastic messasges astroscope will accept.
    private static List  acceptedMessages = new ArrayList() {
        {//initializer block.
        this.add(CommonMessageConstants.ECHO);
        this.add(CommonMessageConstants.GET_IVORN);
        this.add(CommonMessageConstants.GET_NAME);
        this.add(CommonMessageConstants.GET_VERSION);
        //this.add(CommonMessageConstants.GET_ICON); not yet.
        this.add(HubMessageConstants.APPLICATION_REGISTERED_EVENT);
        this.add(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT);
        }
    };
           
    
    private JButtonBar dynamicButtons = new JButtonBar(JButtonBar.VERTICAL);
    private final PlasticHubListener hub;
    private final MessageHandler plasticHandler;// handles incoming plastic messages
    /**
     * 
     * @param ui
     * @param conf
     * @param hs
     * @param myspace
     * @param chooser
     * @param reg
     * @param rci
     * @param siap
     * @param cone
     * @throws URISyntaxException 
     */
    public AstroScopeLauncherImpl(UIInternal ui, Configuration conf, HelpServerInternal hs,  
                                  MyspaceInternal myspace, ResourceChooserInternal chooser, Registry reg, 
                                  Siap siap, Cone cone, Ssap ssap,Sesame ses, /*Community comm,*/ PlasticHubListener hub) throws URISyntaxException {
        super(conf,hs,ui);
        
        this.ses = ses;               
        // configure data protcols
        protocols = new DalProtocolManager();
        protocols.add(new SiapProtocol(this,reg,siap));
        protocols.add(new SsapProtocol(this,reg,ssap));
        protocols.add(new ConeProtocol(this,reg,cone));
        // create the shared model
        vizModel = new VizModel(protocols);
        // create the vizualizations
        vizualizations = new VizualizationManager(vizModel);
        vizualizations.add( new WindowedRadialVizualization(vizualizations));
        vizualizations.add(new HyperbolicVizualization(vizualizations));

        dynamicButtons.add(new SaveNodesButton(vizModel.getSelectionFocusSet(),this,chooser,myspace));
        dynamicButtons.add(new VOSpecButton(vizModel.getSelectionFocusSet(),this));
        
        // plastic setup
        this.hub = hub;
        // generates a new name each time.
        String appName = "AstroScope-" + UNQ_ID++;
       // standard message handler.
        this.plasticHandler = new StandardHandler(appName,"","ivo://org.astrogrid/astroscope",null, PlasticListener.CURRENT_VERSION); //JDT TODO add a logo
        // message handler for application add and  applcation remove messages.
        ApplicationRegisteredPlasticMessageHandler dynamicButtonHandler = new AstroscopePlasticMessageHandler(this, this, dynamicButtons);
        this.plasticHandler.setNextHandler(dynamicButtonHandler);
        // register with the hub
        myPlasticID = hub.registerRMI(appName, acceptedMessages,this );
        // inspect what applications have already registered .
        //get what's currently registered there.
        List ids = hub.getRegisteredIds();
        for (Iterator i = ids.iterator(); i.hasNext(); ) {
            URI id = (URI)i.next();
            if (! id.equals(myPlasticID) ) { // no point looking at my own navel,
                dynamicButtonHandler.interrogatePlasticApp(id);
            }
        }
        
        // build the ui.  
        this.setSize(1000,707); // same proportions as A4,    
                   
//        this.setSize(700, 700);  
        JPanel pane = getMainPanel();
        JPanel searchPanel = makeSearchPanel();
        searchPanel.setMaximumSize(searchPanel.getSize());
        pane.add(searchPanel,BorderLayout.WEST);
        pane.add(makeCenterPanel(),BorderLayout.CENTER);
        this.setContentPane(pane);
        this.setTitle("AstroScope");
        getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.astroscopeLauncher");
        setIconImage(IconHelper.loadIcon("search.gif").getImage());
    }
    

    /**
     * Various action statements for when buttons are clicked.
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        logger.debug("actionPerformed(ActionEvent) - entered actionPerformed");
        if(source == submitButton) {
        	if (submitButton.isDoingSearch()) {
        		logger.debug("halting");
        		haltQuery();
        	} else {
        		logger.debug("querying");
        		currentlyDegrees=true;
            degreesRadio.setEnabled(true);
            sexaRadio.setEnabled(true);
            degreesRadio.setSelected(true);
        		query();
            String histVal = posText.getText() + "_" + regionText.getText();
            historyCombo.insertItemAt(histVal,2);
            String histKey = getConfiguration().getKey("astroscope.history");          
            if(histKey != null) {
                String []hist = histKey.split(";");
                if(hist.length < 20) {
                    histVal += ";" + StringUtils.join(hist,";");                 
                }else {
                    hist[(hist.length - 1)] = "";
                    histVal += ";" + StringUtils.join(hist,";");
                }
            }
             getConfiguration().setKey("astroscope.history",histVal);
         }
        	submitButton.flip();
        }else if(source == reFocusTopButton) {
            vizualizations.refocusMainNodes();
            vizualizations.reDrawGraphs();
        }else if (source == clearButton) {
            FocusSet set = vizModel.getSelectionFocusSet();
            for (Iterator i = set.iterator(); i.hasNext(); ) {
                TreeNode n = (TreeNode)i.next();
                //i.remove();
                n.setAttribute("selected","false");
            }
            set.clear();
            vizualizations.reDrawGraphs();
        }else if (degreesRadio.isEnabled() && (source == degreesRadio || source == sexaRadio)) {
            toggleAndConvertNodes(vizModel.getRootNode());
            vizualizations.reDrawGraphs();
            String regTemp = null;
            if(currentlyDegrees && source == sexaRadio) { // factor these actions out into separate methods.
                currentlyDegrees = false;
                posText.setText(posText.getPositionSexagesimal());
                regTemp = regionText.hasFullRegion() ?  String.valueOf((regionText.getRADegrees() * 3600)) + "," + String.valueOf((regionText.getDECDegrees() * 3600)) :
                          String.valueOf((Double.parseDouble(regionText.getText()) * 3600));
                regionText.setText(Retriever.scaleValue(regTemp,6));
            } else if(!currentlyDegrees && source == degreesRadio) {
                currentlyDegrees = true;
                posText.setText(Retriever.scaleValue(String.valueOf(posText.getRADegrees()),6) + "," + Retriever.scaleValue(String.valueOf(posText.getDECDegrees()),6));
                regTemp = regionText.hasFullRegion() ?  String.valueOf((regionText.getRADegrees()/3600)) + "," + String.valueOf((regionText.getDECDegrees()/3600)) :
                    String.valueOf((Double.parseDouble(regionText.getText())/3600));
                regionText.setText(Retriever.scaleValue(regTemp,6));
            }
        }else if (source == historyCombo) {
            if(historyCombo.getSelectedIndex() == 0) {
                posText.setEnabled(true);
                regionText.setEnabled(true);            
            }else if(historyCombo.getSelectedIndex() == 1) {
                posText.setEnabled(true);
                regionText.setEnabled(true);
                historyCombo.removeAllItems();
                historyCombo.insertItemAt("Use Inputs",0);
                historyCombo.insertItemAt("Clear History",1);
                historyCombo.setSelectedIndex(0);
            }else {
                String histVal = (String) historyCombo.getSelectedItem();
                String []hist = histVal.split("_");
                posText.setEnabled(false);
                regionText.setEnabled(false);
                posText.setText(hist[0]);
                regionText.setText(hist[1]);
            }
        }
        logger.debug("actionPerformed(ActionEvent) - exit actionPerformed");
    }
    
    private boolean currentlyDegrees = true;
    
    private void toggleAndConvertNodes(TreeNode nd) {
        String ndVal = null;  
        String posVal = null;
        boolean foundOffset = false;
        if((ndVal = nd.getAttribute(Retriever.OFFSET_ATTRIBUTE)) != null) {
            foundOffset = true;
            double val;
            if(currentlyDegrees) {
                val = Double.parseDouble(ndVal) * 3600;                
            } else {
                val = Double.parseDouble(ndVal);
            }
            nd.setAttribute(Retriever.LABEL_ATTRIBUTE,Retriever.scaleValue(String.valueOf(val),6));
            nd.setAttribute(Retriever.TOOLTIP_ATTRIBUTE,String.valueOf(val));
            for(int i = 0;i < nd.getChildCount();i++) {
                TreeNode childNode = nd.getChild(i);
                //should be a position string.
                ndVal = childNode.getAttribute(Retriever.LABEL_ATTRIBUTE);
                if(currentlyDegrees) {
                    posVal = PositionTextField.getRASexagesimal(ndVal) + "," + PositionTextField.getDECSexagesimal(ndVal);
                }else {
                    posVal = Retriever.scaleValue(String.valueOf(PositionTextField.getRADegrees(ndVal)),6) + "," + Retriever.scaleValue(String.valueOf(PositionTextField.getDECDegrees(ndVal)),6);
                }
                childNode.setAttribute(Retriever.LABEL_ATTRIBUTE,posVal);
                childNode.setAttribute(Retriever.TOOLTIP_ATTRIBUTE,posVal);
            }
        }//if
        for(int i = 0;i < nd.getChildCount();i++) {
            if(!foundOffset)
                toggleAndConvertNodes(nd.getChild(i));
        }
        
    }
    
    /** removes previous results, just leaving the skeleton */
    private void clearTree() {
        // reset selection too.
        vizModel.clear();    
        vizualizations.refocusMainNodes(); 
        setProgressMax(1);
        setProgressValue(0);
    }
         
        
    /**
     * Makes the Center Panel for the GUI.  The main look is is the display graph which is the 
     * majority of the center, and a series of save buttons below it.  A series of 
     * createInitialDisplay type methods are here to try and switch to see the best display.
     * 
     * @return
     */
    private JPanel makeCenterPanel() {
        
        
        JTabbedPane alternatives = new JTabbedPane();
        for (Iterator i = vizualizations.iterator(); i.hasNext();) {
            Vizualization v = (Vizualization)i.next();
            alternatives.addTab(v.getName(),v.getDisplay());
        }
        
        alternatives.addTab("Services",makeServicesPanel());
 
        JPanel wrapPanel = new JPanel();
        wrapPanel.setLayout(new BoxLayout(wrapPanel,BoxLayout.Y_AXIS));
        wrapPanel.add(alternatives);                
        //wrapPanel.add(southCenterPanel);
        return wrapPanel;        
    }
    
    /** panel containing summary of search results */
    private JPanel makeServicesPanel() {
        
        /*
         * TableSorter sorter = new TableSorter(new MyTableModel()); //ADDED THIS
JTable table = new JTable(sorter);             //NEW
sorter.setTableHeader(table.getTableHeader()); //ADDED THIS
         */
        
            JPanel servicesPanel = new JPanel();
            TableSorter sorter = new TableSorter(protocols.getQueryResultTable()); //ADDED THIS
            //final JTable table = new JTable(protocols.getQueryResultTable());
            final JTable table = new JTable(sorter);             //NEW
            sorter.setTableHeader(table.getTableHeader()); //ADDED THIS
            JScrollPane scroll = new JScrollPane(table);
            table.setPreferredScrollableViewportSize(new Dimension(700,550));
            TableColumnModel tcm = table.getColumnModel();
            final TableColumn riColumn = tcm.getColumn(0);
            riColumn.setPreferredWidth(150);
            // decorate the renderer for resource information (objects) to display a nice tooltip, etc.
            riColumn.setCellRenderer(new TableCellRenderer() {
                private final TableCellRenderer original = table.getDefaultRenderer(ResourceInformation.class);
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel c = (JLabel)original.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                    if (value instanceof ResourceInformation) {
                        ResourceInformation ri = (ResourceInformation)value;
                        c.setText(ri.getTitle());
                        c.setToolTipText(mkToolTip(ri));
                    }
                    return c;
                }
                /** builds a tool tip from a resource information object 
                 * @todo refactor so it works in a general way - not hardcoded.
                 * */
                 private String mkToolTip(ResourceInformation ri) {
                     StringBuffer sb = new StringBuffer();
                     sb.append("<html>");
                     sb.append(ri.getId());      
                     sb.append("<br>Service Type: ");
                     if (ri instanceof SiapInformation) {  //nasty little hack for now. - later find a way of getting this info from the protocol object..
                         sb.append("Image");
                     } else if (ri instanceof ConeInformation) {
                         sb.append("Catalogue");
                     } else { // no special type for ssap at the moment, and have to assume that any other ri is a ssap
                         sb.append("Spectra");
                     }
                     sb.append("</html>");
                     //@todo extend ResourceInformation to contain curation details, add these in here.
                     return sb.toString();
                 }                
            });            
            final TableColumn countColumn = tcm.getColumn(1);
            countColumn.setPreferredWidth(60);
            countColumn.setMaxWidth(60);
            servicesPanel.add(new JScrollPane(table));
            // decorate the default renderer for integers to display something special for -1
           countColumn.setCellRenderer(new TableCellRenderer() {
                private final TableCellRenderer original = table.getDefaultRenderer(Integer.class);
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel c = (JLabel)original.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                    if (value instanceof Integer && ((Integer)value).intValue() ==  QueryResultSummarizer.ERROR) {
                        c.setText("<html><font color='red'>ERROR</font></html>");
                    }
                    return c;
                }                
            });
           TableColumn msgColumn = tcm.getColumn(2);
           msgColumn.setPreferredWidth(50);
           msgColumn.setCellRenderer(new TableCellRenderer() {
               private final TableCellRenderer original = table.getDefaultRenderer(String.class);
               public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                   JLabel c = (JLabel)original.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                   String s = value.toString();
                   if (s != null && s.length() > 0) {
                       c.setToolTipText("<html>" + WordUtils.wrap(s,50,"<br>",false) + "</html>");
                   }
                   return c;
               }
           });        
        return servicesPanel;
    }
    

    
    
    /**
     * Creates the left/WEST side of the GUI.  By creating a small search panel at the top(north-west).  Then
     * let the rest of the panel be a JTree for the selected data.
     * @return JPanel consisting of the query gui and custom controls typically placedo on the WEST side of the main panel.
     */
    private JPanel makeSearchPanel() {
        JPanel scopeMain = new JPanel();
        scopeMain.setLayout(new BorderLayout());
    
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.Y_AXIS));
        //searchPanel.setLayout(new GridLayout(10,1));
        searchPanel.setBorder(new TitledBorder("1. Search"));
        
        posText = new PositionTextField(ses);
        posText.setToolTipText("Object name (3c273) or Position (187.27,+2.05 or 12:29:06.00,+02:03:08.60)");
        posText.setAlignmentX(LEFT_ALIGNMENT);
        posText.setColumns(10);
        //posText.setMaximumSize(posText.getPreferredSize());   
        
        regionText = new PositionTextField();
        regionText.setToolTipText("Search radius (0.008333 degs or 30.00\")");
        regionText.setAlignmentX(LEFT_ALIGNMENT);
        regionText.setColumns(10);
        //regionText.setMaximumSize(regionText.getPreferredSize());
        
        searchPanel.add(new JLabel("Position/Object: "));
        searchPanel.add(posText);
        searchPanel.add(new JLabel("Search Radius: "));
        searchPanel.add(regionText);
        
        degreesRadio = new JRadioButton("Degrees");        
        sexaRadio = new JRadioButton("Sexagesimal");
        ButtonGroup group = new ButtonGroup();
        group.add(degreesRadio);
        group.add(sexaRadio);        
        degreesRadio.addActionListener(this);
        degreesRadio.setEnabled(false);
        sexaRadio.addActionListener(this);
        sexaRadio.setEnabled(false);
        searchPanel.add(degreesRadio);
        searchPanel.add(sexaRadio);
        
        
        for (Iterator i = protocols.iterator(); i.hasNext(); ) {
            DalProtocol p = (DalProtocol)i.next();
            searchPanel.add(p.getCheckBox());
        }
        
        searchPanel.add(new JLabel("Previous searches:"));
        String histCheck = getConfiguration().getKey("astroscope.history");
        String []hist = histCheck != null ? histCheck.split(";") : new String[] {"Use Inputs"};
        if(histCheck != null)
            historyCombo = new JComboBox(histCheck.split(";"));
        else
            historyCombo = new JComboBox();        
        historyCombo.setAlignmentX(LEFT_ALIGNMENT);
        //historyCombo.setMaximumSize(new Dimension(150,50));                
        historyCombo.insertItemAt("Use Inputs",0);
        historyCombo.insertItemAt("Clear History",1);
        historyCombo.setSelectedIndex(0);
        historyCombo.addActionListener(this);        
        searchPanel.add(historyCombo);
        
        //searchPanel.add(testComboPanel);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
        scopeMain.getInputMap(scopeMain.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter,"search");
        scopeMain.getActionMap().put("search",new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                AstroScopeLauncherImpl.this.actionPerformed(e);
            }
        });         

        submitButton = new FlipButton();
        submitButton.addActionListener(this);
        //submitButton.
              
    	
        searchPanel.add(submitButton);
        
        
        //historyCombo.setLocation((int)submitButton.getLocation().getX(),(int)historyCombo.getLocation().getY());
        
        // start of tree navigation buttons - maybe add more here later.
        JPanel navPanel = new JPanel();      
        navPanel.setLayout(new BoxLayout(navPanel,BoxLayout.Y_AXIS)); 
        navPanel.setBorder(new TitledBorder("2. Navigate"));
        
        reFocusTopButton = new JButton("Go to Top");
        reFocusTopButton.setIcon(IconHelper.loadIcon("top.png"));
        reFocusTopButton.setToolTipText("Focus display to 'Search Results' ");
        reFocusTopButton.addActionListener(this);
        reFocusTopButton.setEnabled(false);             
        navPanel.add(reFocusTopButton);
        
        clearButton = new JButton("Clear Selection");
        clearButton.setIcon(IconHelper.loadIcon("editclear.png"));
        clearButton.setToolTipText("Clear selected nodes");
        clearButton.setEnabled(false);
        clearButton.addActionListener(this);
        
        
        /*
        switchButton = new JButton("Toggle Coords");
        switchButton.setIcon(IconHelper.loadIcon("toggle.gif"));
        switchButton.setToolTipText("Toggle between Degrees and Sexagesimal coordinates");
        switchButton.setEnabled(false);
        //switchButton.setMaximumSize(clearButton.getMaximumSize());
        switchButton.addActionListener(this);
        */       
        
        final FocusSet sel = vizModel.getSelectionFocusSet();
        sel.addFocusListener(new FocusListener() {
            public void focusChanged(FocusEvent arg0) {
                clearButton.setEnabled(sel.size() > 0);
            }
        });
        navPanel.add(clearButton);
        //navPanel.add(degreesRadio);
        //navPanel.add(sexaRadio);
        
        // make these buttons all the same width - I know clear button is the biggest.
        submitButton.setMaximumSize(clearButton.getPreferredSize());
        reFocusTopButton.setMaximumSize(clearButton.getPreferredSize());
        
        /*
        navPanel.setMinimumSize(navPanel.getPreferredSize());
        navPanel.setMaximumSize(navPanel.getPreferredSize());
        searchPanel.setMinimumSize(navPanel.getPreferredSize());
        */
        
        // start of consumer buttons.
        JScrollPane sp =new JScrollPane(dynamicButtons);
        sp.setBorder(new TitledBorder("3. Process"));
        searchPanel.setMaximumSize(sp.getMaximumSize());
        navPanel.setMaximumSize(sp.getMaximumSize());
                
        // assemble it all together.
        JPanel bothTop = new JPanel();
        bothTop.setLayout(new BoxLayout(bothTop,BoxLayout.Y_AXIS));
        bothTop.add(searchPanel);
        bothTop.add(navPanel);
        bothTop.setMaximumSize(sp.getMaximumSize());
        scopeMain.add(bothTop,BorderLayout.NORTH);
        scopeMain.add(sp,BorderLayout.CENTER);           
        return scopeMain;
    }
    
    /**
     * method: query
     * description: Queries the registry for sia and conesearch types and begins working on them.
     *
     *
     */
    private void query() {
        logger.debug("query() - inside query method)");        
        (new BackgroundOperation("Checking Position") {
            protected Object construct() throws Exception {
                return posText.getPositionDegrees();
            }
            protected void doFinished(Object o) {
                String position = (String)o;
                if (position == null) {
                    showError("Could not parse position\nYou must enter RA,DEC or name of object known to SIMBAD");
                    return;
                }
                setStatusMessage(position);
                clearTree();
                reFocusTopButton.setEnabled(true);
                degreesRadio.setEnabled(true);
                sexaRadio.setEnabled(true);
                
                //  @todo refactor this string-munging methods.                
                final double ra = PositionTextField.getRADegrees(position);
                final double dec = PositionTextField.getDECDegrees(position);                
                final String region = regionText.getText().trim();
                
                final double raSize = regionText.hasFullRegion() ?  
                        posText.isSexagesimal() ? PositionTextField.getRADegrees(region)/3600 : PositionTextField.getRADegrees(region) : 
                        posText.isSexagesimal() ? Double.parseDouble(region)/3600 : Double.parseDouble(region);
                final double decSize= regionText.hasFullRegion() ? 
                        posText.isSexagesimal() ? PositionTextField.getDECDegrees(region)/3600 : raSize : raSize;

                if(posText.isSexagesimal())
                    regionText.setText(regionText.hasFullRegion() ? String.valueOf(raSize) + "," + String.valueOf(decSize) : String.valueOf(raSize));
                
                posText.setText(Retriever.scaleValue(String.valueOf(PositionTextField.getRADegrees(position)),6) + 
                        "," + Retriever.scaleValue(String.valueOf(PositionTextField.getDECDegrees(position)),6));
                if(raSize == decSize) 
                    regionText.setText(Retriever.scaleValue(String.valueOf(raSize),6));
                else
                    regionText.setText(Retriever.scaleValue(String.valueOf(raSize),6) + "," + Retriever.scaleValue(String.valueOf(decSize),6));
                
                for (Iterator i = protocols.iterator(); i.hasNext(); ) {
                    final DalProtocol p =(DalProtocol)i.next();
                    if (p.getCheckBox().isSelected()) {
                        (new BackgroundOperation("Searching for " + p.getName() + " Services") {
                            protected Object construct() throws Exception {
                                return p.listServices();
                            }
                            protected void doFinished(Object result) {
                                ResourceInformation[] services = (ResourceInformation[])result;
                                logger.info(services.length + " " + p.getName() + " services found");
                                for (int i = 0; i < services.length; i++) {
                                    if (services[i].getAccessURL() != null) {
                                        setProgressMax(getProgressMax()+1); // should give a nice visual effect.
                                        p.createRetriever(services[i], null,null,ra,dec,raSize,decSize).start();
                                       // (new SiapRetrieval(AstroScopeLauncherImpl.this,siaps[i],vizModel,nodeSizingMap,siap,ra,dec,raSize,decSize)).start();
                                    }
                                }                            
                            }                            
                        }).start();
                    }
                }

            }
        }).start();
    }
    
    private void haltQuery() {
    	super.getHaltAllButton().doClick();
    	setStatusMessage("Halted");
    	setProgressValue(getProgressMax());
    }

    /// PLASTIC stuff below here 

    
    // implementation of the plastic listener interface
    // callback when message is _received_ from plastic - delegates straight to handler.
    public Object perform(URI sender, URI message, List args) {
        return plasticHandler.perform(sender,message,args);
    }

    // implementation of te plastic wrapper interface;
    public PlasticHubListener getHub() {
        return hub;
    }
    public URI getPlasticId() {
        return myPlasticID;
    }





	

  
}

/* 
$Log: AstroScopeLauncherImpl.java,v $
Revision 1.40  2006/05/11 11:40:59  KevinBenson
small change on a label

Revision 1.39  2006/05/11 10:02:45  KevinBenson
added history to astro and helioscope.  Along with tweaks to alignment and borders
And changing decimal places to 6 degrees.

Revision 1.38  2006/04/26 15:56:54  nw
added 'halt query' and 'halt all tasks' functinaltiy.

Revision 1.36  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.35  2006/03/31 15:20:56  nw
removed work-around, due to new version of plastic library

Revision 1.34  2006/03/16 18:12:56  jdt
Some bug fixes, and the next version of the plastic library.

Revision 1.32  2006/02/27 12:20:50  nw
improved plastic integration

Revision 1.31  2006/02/24 15:25:57  nw
plasticization of astroscope

Revision 1.29  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer

Revision 1.28  2006/02/02 14:50:49  nw
refactored into separate components.

Revision 1.27  2005/12/02 17:05:08  nw
replaced dom-style parsing of votables with sax-style. faster, better for memory

Revision 1.26  2005/12/02 13:42:48  nw
changed to use system pooled executor, re-worked background processes

Revision 1.25  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.24  2005/11/15 19:39:07  nw
merged in improvements from release branch.

Revision 1.23.2.2  2005/11/15 19:34:19  nw
improvements to saving, threading, clustering.

Revision 1.23.2.1  2005/11/15 13:26:23  nw
fixed astroscope.
worked on javahelp

Revision 1.23  2005/11/11 10:27:41  nw
minor changes.

Revision 1.22  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.21  2005/11/10 14:55:15  KevinBenson
minor tweaks plus javahelp is there now

Revision 1.20  2005/11/10 14:18:52  KevinBenson
minor fixes to highlight other displays on selects. and fous on nodes

Revision 1.19  2005/11/10 12:18:27  KevinBenson
small tweaks

Revision 1.18  2005/11/10 10:43:13  KevinBenson
minor change on the haversine formula

Revision 1.17  2005/11/09 16:05:55  KevinBenson
minor change to add a "Go to Top" button.

Revision 1.16  2005/11/09 14:10:44  KevinBenson
removed some statemetns that were not needed

Revision 1.15  2005/11/09 14:06:52  KevinBenson
minor changes for clearTree to refocus in the center.  And fix an expression on the position

Revision 1.14  2005/11/08 15:03:56  KevinBenson
minor changes on sizing

Revision 1.13  2005/11/07 16:25:05  KevinBenson
added some clustering to it. so there is an offset and some clustered child nodes as well.

Revision 1.12  2005/11/04 17:49:52  nw
reworked selection and save datastructures.

Revision 1.11  2005/11/04 14:09:12  nw
improved error handling in getPosition,
started looking at image preview.

Revision 1.10  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.9  2005/11/03 11:56:49  KevinBenson
added a new astroscope cluster

Revision 1.8  2005/11/02 17:29:56  KevinBenson
fixed scrollpane

Revision 1.7  2005/11/02 09:50:11  KevinBenson
should have Noel's 2 minor fixes.  Plus a couple of additions for buttons and node selections

Revision 1.5  2005/11/01 14:40:20  KevinBenson
Some small changes to have hyperbolic working with selections and saving to myspace

Revision 1.3  2005/10/31 16:13:51  KevinBenson
added hyperbolic in there, plus the saving to myspace area.

Revision 1.2  2005/10/31 12:49:38  nw
rehashed downloading mechanism,
put in a bunch of sample vizualizations.

Revision 1.1  2005/10/26 15:53:15  KevinBenson
new astroscope being added into the workbench.

Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.2.2.2  2005/10/10 18:12:36  nw
merged kev's datascope lite.

Revision 1.2  2005/10/10 12:09:45  KevinBenson
small change to the astroscope to show browser and vospace when the user hits okay

Revision 1.1  2005/10/04 20:46:48  KevinBenson
new datascope launcher and change to module.xml for it.  Vospacebrowserimpl changes to handle file copies to directories on import and export

Revision 1.4  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.3  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:33  nw
first release of application launcher
 
*/