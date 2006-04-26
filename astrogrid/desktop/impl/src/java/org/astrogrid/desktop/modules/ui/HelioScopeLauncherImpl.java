/*$Id: HelioScopeLauncherImpl.java,v 1.10 2006/04/26 15:56:54 nw Exp $
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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.astrogrid.StapInformation;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.HelioScope;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.scope.DalProtocol;
import org.astrogrid.desktop.modules.ui.scope.DalProtocolManager;
import org.astrogrid.desktop.modules.ui.scope.HyperbolicVizualization;
import org.astrogrid.desktop.modules.ui.scope.ImageLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.QueryResultSummarizer;
import org.astrogrid.desktop.modules.ui.scope.SaveNodesButton;
import org.astrogrid.desktop.modules.ui.scope.StapProtocol;
import org.astrogrid.desktop.modules.ui.scope.VizModel;
import org.astrogrid.desktop.modules.ui.scope.Vizualization;
import org.astrogrid.desktop.modules.ui.scope.VizualizationManager;
import org.astrogrid.desktop.modules.ui.scope.VotableLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.WindowedRadialVizualization;
import org.freixas.jcalendar.JCalendarCombo;
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


/** Implementation of the HelioScope launcher
 * 
 * @todo tidy up scrappy get position code - in particular, report errors from simbad correctly - at moment,
 * if simbad service is down, user is told 'you must enter a name known to simbad' - which is very misleading.
 * @todo hyperbolic doesn't always update to display nodes-to-download as yellow. need to add a redraw in somewhere. don't want to redraw too often though.
 */
public class HelioScopeLauncherImpl extends UIComponentImpl 
    implements HelioScope, ActionListener, PlasticListener, PlasticWrapper {
   
/** extends the plastic mesagehandler to display new buttons */
    protected class HelioscopePlasticMessageHandler extends ApplicationRegisteredPlasticMessageHandler {

        private HelioscopePlasticMessageHandler(UIComponent parent, PlasticWrapper wrapper, Container container) {
            super(parent, wrapper, container);
        }

        // define this method to control what happens when a new application registers.
        protected Component[] buildComponents(URI applicationId, String name, String description, URL iconURL, URI[] messages) {
            List results= new ArrayList();
            if (ArrayUtils.contains(messages,CommonMessageConstants.VOTABLE_LOAD_FROM_URL)) {
                results.add(
                        new VotableLoadPlasticButton(applicationId,name,description,iconURL,vizModel.getSelectionFocusSet(),
                                HelioScopeLauncherImpl.this,HelioScopeLauncherImpl.this)                         
                        );
            }
            if (ArrayUtils.contains(messages,CommonMessageConstants.FITS_LOAD_FROM_URL)) {                    
                results.add(
                        new ImageLoadPlasticButton(applicationId,name,description, iconURL,vizModel.getSelectionFocusSet(),
                                HelioScopeLauncherImpl.this,HelioScopeLauncherImpl.this)                           
                        );
            }                
            return (Component[])results.toArray(new Component[results.size()]);
            
        }
    }


    public static final int TOOLTIP_WRAP_LENGTH = 50;
    
    /**
     * Commons Logger for this class
     */
    static final Log logger = LogFactory.getLog(HelioScopeLauncherImpl.class);

    //Various gui components.
    private JTextField posText;           
    private JButton reFocusTopButton;   
    private JButton clearButton;
    private JTextField regionText;
    private FlipButton submitButton;
    private JCalendarCombo startCal;
    private JCalendarCombo endCal;

    // set of data retrieval components 
    private final DalProtocolManager protocols;
    
    //shared data model, selections, etc.
    private final VizModel vizModel;
    // set of vizualization components
    private final VizualizationManager vizualizations;

    // used to generate a new plastic id for each HelioScope window
    private static int UNQ_ID = 0;
    private final URI myPlasticID;
    // list of plastic messasges HelioScope will accept.
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
    
    private final Sesame ses;
    
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
    public HelioScopeLauncherImpl(UIInternal ui, Configuration conf, HelpServerInternal hs,  
                                  MyspaceInternal myspace, ResourceChooserInternal chooser, Registry reg, 
                                  Stap stap,Sesame ses,/* Community comm,*/ PlasticHubListener hub) throws URISyntaxException {
        super(conf,hs,ui);
        
        this.ses = ses;               
        // configure data protcols
        protocols = new DalProtocolManager();
        protocols.add(new StapProtocol(this,reg,stap));
        // create the shared model
        vizModel = new VizModel(protocols);
        // create the vizualizations
        vizualizations = new VizualizationManager(vizModel);
        vizualizations.add( new WindowedRadialVizualization(vizualizations));
        vizualizations.add(new HyperbolicVizualization(vizualizations));

        dynamicButtons.add(new SaveNodesButton(vizModel.getSelectionFocusSet(),this,chooser,myspace));
        //dynamicButtons.add(new VOSpecButton(vizModel.getSelectionFocusSet(),this));
        
        // plastic setup
        this.hub = hub;
        // generates a new name each time.
        String appName = "HelioScope-" + UNQ_ID++;
       // standard message handler.
        String logoUrl = "";
        String description = logoUrl;
		  this.plasticHandler = new StandardHandler(appName,description,"ivo://org.astrogrid/helioscope", logoUrl, PlasticListener.CURRENT_VERSION);
        // message handler for application add and  applcation remove messages.
        ApplicationRegisteredPlasticMessageHandler dynamicButtonHandler = new HelioscopePlasticMessageHandler(this, this, dynamicButtons);
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
        this.setTitle("HelioScope");
        getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.helioscopeLauncher");
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
        		query();
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
        }

        logger.debug("actionPerformed(ActionEvent) - exit actionPerformed");
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
     * Extracts out the dec of a particular position in the form of a ra,dec
     * @param position
     * @return
     */    
    private double getDEC() {
        return Double.NaN;
    }

    /**
     * Extracts out the ra of a particular position in the form of a ra,dec
     * @param position
     * @return
     * @todo refactor -report error to user - or prevent invalid input in the first place.
     */
    private double getRA() {
        return Double.NaN;
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
    
    private JCheckBox formatTimeSeriesCheck;
    private JCheckBox formatGraphicCheck;
    
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
                     if (ri instanceof StapInformation) {  //nasty little hack for now. - later find a way of getting this info from the protocol object..
                         sb.append("Stap");
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
        searchPanel.setBorder(new TitledBorder("1. Search"));
        
        startCal = new JCalendarCombo(JCalendarCombo.DISPLAY_DATE|JCalendarCombo.DISPLAY_TIME,true);
        startCal.setNullAllowed(false);
        startCal.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        startCal.setEditable(true);
        Calendar setStartCal = startCal.getCalendar();
        setStartCal.set(2000,0,1,0,0,0);
        startCal.setDate(setStartCal.getTime());

        endCal = new JCalendarCombo(JCalendarCombo.DISPLAY_DATE|JCalendarCombo.DISPLAY_TIME,true);
        endCal.setNullAllowed(false);
        endCal.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        endCal.setEditable(true);
        Calendar setEndCal = endCal.getCalendar();
        setEndCal.set(2000,0,2,0,0,0);
        endCal.setDate(setEndCal.getTime());
        
        
        posText = new JTextField();
        /*
        posText.setToolTipText("Place position or object name e.g. 120,0 (in decimal degrees and no spaces) or M11");
        posText.setAlignmentX(LEFT_ALIGNMENT);
        posText.setColumns(10);
        posText.setMaximumSize(posText.getPreferredSize());   
        */
        regionText = new JTextField();
        /*
        regionText.setToolTipText("Enter region size e.g. 0.1 in decimal degrees");
        regionText.setAlignmentX(LEFT_ALIGNMENT);
        regionText.setColumns(10);
        regionText.setMaximumSize(regionText.getPreferredSize());
        */
        searchPanel.add(new JLabel("Start Date&Time: "));
        searchPanel.add(startCal);

        searchPanel.add(new JLabel("End Date&Time: "));
        searchPanel.add(endCal);
        
        /*
        searchPanel.add(new JLabel("----Optional----"));
        searchPanel.add(new JLabel("Position/Object: "));
        searchPanel.add(posText);
        searchPanel.add(new JLabel("Region: "));
        searchPanel.add(regionText);
        */
        /*
        for (Iterator i = protocols.iterator(); i.hasNext(); ) {
            DalProtocol p = (DalProtocol)i.next();
            searchPanel.add(p.getCheckBox());
        }
        */
        
        formatTimeSeriesCheck = new JCheckBox("Time Series");
        formatGraphicCheck = new JCheckBox("Graphic");
        formatTimeSeriesCheck.setSelected(true);
        formatGraphicCheck.setSelected(true);
        JPanel checkPanel = new JPanel();
        checkPanel.add(formatTimeSeriesCheck);
        checkPanel.add(formatGraphicCheck);
        searchPanel.add(checkPanel);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
        scopeMain.getInputMap(scopeMain.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter,"search");
        scopeMain.getActionMap().put("search",new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                HelioScopeLauncherImpl.this.actionPerformed(e);
            }
        });                
        submitButton = new FlipButton();
        submitButton.addActionListener(this);

searchPanel.add(submitButton);    
        
        // start of tree navigation buttons - maybe add more here later.
        JPanel navPanel = new JPanel();      
        navPanel.setLayout(new BoxLayout(navPanel,BoxLayout.Y_AXIS));
        navPanel.setMinimumSize(searchPanel.getMinimumSize());
        navPanel.setMaximumSize(searchPanel.getMaximumSize());        
        navPanel.setPreferredSize(searchPanel.getPreferredSize());
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
        
        final FocusSet sel = vizModel.getSelectionFocusSet();
        sel.addFocusListener(new FocusListener() {
            public void focusChanged(FocusEvent arg0) {
                clearButton.setEnabled(sel.size() > 0);
            }
        });
        navPanel.add(clearButton);
        
        // make these buttons all the same width - I know clear button is the biggest.
        submitButton.setMaximumSize(clearButton.getPreferredSize());
        reFocusTopButton.setMaximumSize(clearButton.getPreferredSize());
        
        // start of consumer buttons.
        JScrollPane sp =new JScrollPane(dynamicButtons);
        sp.setBorder(new TitledBorder("3. Process"));
                
        // assemble it all together.
        JPanel bothTop = new JPanel();
        bothTop.setLayout(new BoxLayout(bothTop,BoxLayout.Y_AXIS));
        bothTop.add(searchPanel);        
        bothTop.add(navPanel);
        scopeMain.add(bothTop,BorderLayout.NORTH);;
        scopeMain.add(sp,BorderLayout.CENTER);   

        //scopeMain.setPreferredSize(new Dimension().
        return scopeMain;
    }
    
    /**
     * Checks to see if the regiion is in the form of ra,dec which will need to be parsed.
     * @return
     * @todo refactor
     */
    private boolean needsParsedRegion() {
        String region = regionText.getText().trim();
        String expression = "-?\\d+\\.?\\d*,-?\\d+\\.?\\d*";
        return region.matches(expression);
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
                return null;//getPosition();
            }
            protected void doFinished(Object o) {
                //String position = (String)o;
                final Calendar startStapCal = startCal.getCalendar();
                final Calendar endStapCal = endCal.getCalendar();
                
                if (startStapCal.after(endStapCal)) {
                    showError("Your Start date/time must be before the end date/time");
                    return;
                }
                
                if(!formatTimeSeriesCheck.isSelected() && !formatGraphicCheck.isSelected()) {
                    showError("You much have Time Series and/or Graphics checked when doing a search");
                    return;
                }
                    
                //setStatusMessage(position);
                clearTree();
                reFocusTopButton.setEnabled(true);
                
                //  @todo refactor this string-munging methods.                
                final double ra = getRA();
                final double dec = getDEC();
                final String region = regionText.getText().trim();
                final double raSize = Double.NaN; //needsParsedRegion() ?  getRA(region) : Double.parseDouble(region);
                final double decSize = Double.NaN;  //needsParsedRegion() ? getDEC(region) : raSize;
                for (Iterator i = protocols.iterator(); i.hasNext(); ) {
                    final DalProtocol p =(DalProtocol)i.next();
//                    if (p.getCheckBox().isSelected()) {
                        (new BackgroundOperation("Searching for " + p.getName() + " Services") {
                            protected Object construct() throws Exception {
                                return p.listServices();
                            }
                            protected void doFinished(Object result) {
                                ResourceInformation[] services = (ResourceInformation[])result;
                                logger.info(services.length + " " + p.getName() + " services found");
                                
                                if(formatTimeSeriesCheck.isSelected() && formatGraphicCheck.isSelected()) {
                                    p.setPrimaryNodeLabel("Time Series/Images");
                                }
                                else if(formatTimeSeriesCheck.isSelected()) {
                                    p.setPrimaryNodeLabel("Time Series");
                                }
                                else if(formatGraphicCheck.isSelected()) {
                                    p.setPrimaryNodeLabel("Images");
                                }                                
                                for (int i = 0; i < services.length; i++) {
                                    if (services[i].getAccessURL() != null) {
                                        setProgressMax(getProgressMax()+1); // should give a nice visual effect.
                                        if(formatTimeSeriesCheck.isSelected() && formatGraphicCheck.isSelected()) {
                                            p.createRetriever(services[i],startStapCal,endStapCal, ra,dec,raSize,decSize).start();
                                        }
                                        else if(formatTimeSeriesCheck.isSelected()) {
                                            p.createRetriever(services[i],startStapCal,endStapCal, ra,dec,raSize,decSize,"TIME_SERIES").start();
                                        }
                                        else if(formatGraphicCheck.isSelected()) {
                                            p.createRetriever(services[i],startStapCal,endStapCal, ra,dec,raSize,decSize,"GRAPHICS").start();
                                        }
                                       // (new SiapRetrieval(HelioScopeLauncherImpl.this,siaps[i],vizModel,nodeSizingMap,siap,ra,dec,raSize,decSize)).start();
                                    }
                                }                            
                            }                            
                        }).start();
  //                  }
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
$Log: HelioScopeLauncherImpl.java,v $
Revision 1.10  2006/04/26 15:56:54  nw
added 'halt query' and 'halt all tasks' functinaltiy.

Revision 1.9  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.8  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.7  2006/03/31 15:20:56  nw
removed work-around, due to new version of plastic library

Revision 1.6  2006/03/24 10:30:15  KevinBenson
new checkboxes on heliosope for the Format, and the ability to query by Format
for stap services on helioscope

Revision 1.5  2006/03/17 09:15:42  KevinBenson
minor change on stapretrieval to show only the startdate for the valNode and for
helioscope to make the combobox editable

Revision 1.4  2006/03/16 18:12:56  jdt
Some bug fixes, and the next version of the plastic library.

Revision 1.3  2006/03/16 10:02:34  KevinBenson
small label changed

Revision 1.2  2006/03/16 09:16:00  KevinBenson
usually comment/clean up type changes such as siap to stap. Plus setting date&time values
to a previous date

Revision 1.1  2006/03/13 14:55:09  KevinBenson
New first draft of helioscope and the stap spec protocol

 
*/