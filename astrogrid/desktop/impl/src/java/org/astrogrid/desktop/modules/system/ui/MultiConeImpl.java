/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.ivoa.VosiAvailabilityBean;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.AbstractProcessMonitor;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
import org.astrogrid.desktop.modules.dialogs.ConfirmDialog;
import org.astrogrid.desktop.modules.dialogs.RegistryGoogleInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ivoa.VosiInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.actions.BulkCopyWorker;
import org.astrogrid.desktop.modules.ui.actions.CopyCommand;
import org.astrogrid.desktop.modules.ui.actions.MessagingScavenger;
import org.astrogrid.desktop.modules.ui.actions.MultiConeActivity;
import org.astrogrid.desktop.modules.ui.actions.SimpleDownloadActivity;
import org.astrogrid.desktop.modules.ui.actions.ViewInBrowserActivity;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.comp.IndeterminateProgressIndicator;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker;
import org.astrogrid.desktop.modules.ui.taskrunner.LocalFileUploadAssistant;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.swing.EventComboBoxModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 15, 20094:30:06 PM
 */
public class MultiConeImpl extends UIComponentImpl {

    private final ResourceChooserInternal fileChooser;
    private final RegistryGoogleInternal regChooser;
    private final ExecutionTracker tracker;
    private final JPanel trackerPanel;
    private final JSplitPane rightPane;
    private final RemoteProcessManagerInternal rpm;
    private final VosiInternal vosi;
    private final ApplicationsInternal apps;
    private final RegistryInternal reg;
    
    private final Action findTable ;
    private final Action execute;
    private final Action suggest ;
    private final Action chooseService ;
    private final JComboBox raCol;
    private final JComboBox decCol;
    private final JTextField srField;
    private final JRadioButton allButton;
    private final JRadioButton bestButton;
    private final JComboBox capability;
    
    private final Model model ;
    private final EventList<String> raColList;
    private final EventList<String> decColList;
    private final IndeterminateProgressIndicator indicator;
    private final JComboBox format;
    
    // data model
    
    public static String SERVICE_PROPERTY = "service";
    public static String TABLE_PROPERTY = "table";
    public static String VALID_PROPERTY = "valid";
    private class Model  {
        private ConeService service;
        private URI serviceURL;
        private URI tableURL;
        private final EventList<ConeCapability> capabilities = new BasicEventList<ConeCapability>();
        
        /**
         * @return eventlist that will be maintained with the capabilities of the current service.
         */
        public EventList<ConeCapability> getCapabilities() {
            return this.capabilities;
        }

        boolean valid;
        
        /** check we've got correct fields. fires validation event */
        private void validate() {
            setValid(tableURL != null
                && (service != null || serviceURL != null)
           //too ropey     && raCol.getSelectedItem() != null
          //      && decCol.getSelectedItem() != null
          //      && srField.getText() != null
                );
        }
        
        
        /**
         * @return the valid
         */
        public boolean isValid() {
            return this.valid;
        }
        /**
         * @param valid the valid to set
         */
        public void setValid(final boolean valid) {
            final boolean previous = this.valid;
            this.valid = valid;
            support.firePropertyChange(VALID_PROPERTY,previous,this.valid);
        }
        /**
         * @return the service
         */
        public ConeService getService() {
            return this.service;
        }
        /** Set service to a coneservice.
         * fires the Service property.
         * @param service the service to set
         */
        public void setService(final ConeService service) {
            final ConeService previous = this.service;
            this.service = service;
            this.serviceURL = service.getId();
            capabilities.clear();
            for (final Capability cap : service.getCapabilities()) {
                if (cap instanceof ConeCapability) {
                    capabilities.add((ConeCapability)cap);
                }
            }
            capability.setSelectedIndex(0);
            capability.setEnabled(capabilities.size() > 1);
            support.firePropertyChange(SERVICE_PROPERTY,previous, service);
            validate();
        }
        

        /**
         * @return the serviceURL
         */
        public URI getServiceURL() {
            return this.serviceURL;
        }
        /** Sets service to some other kind of servuce URL.
         * zaps service, and capabilties.
         * @param serviceURL the serviceURL to set
         */
        public void setServiceURL(final URI serviceURL) {
            final URI previous = this.serviceURL;
            this.serviceURL = serviceURL;
            this.service = null;
            this.capabilities.clear();
            support.firePropertyChange(SERVICE_PROPERTY,previous, serviceURL);
            validate();
        }
        /**
         * @return the tableURL
         */
        public URI getTableURL() {
            return this.tableURL;
        }
        /**
         * @param uri the tableURL to set
         */
        public void setTableURL(final URI uri) {
            final URI previous = this.tableURL;
            this.tableURL = uri;
            support.firePropertyChange(TABLE_PROPERTY,previous, uri);          
            validate();
        }
    
        
        
        PropertyChangeSupport support = new SwingPropertyChangeSupport(this);
        /**
         * @param listener
         * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
         */
        public void addPropertyChangeListener(final PropertyChangeListener listener) {
            this.support.addPropertyChangeListener(listener);
        }
        /**
         * @param propertyName
         * @param listener
         * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
         */
        public void addPropertyChangeListener(final String propertyName,
                final PropertyChangeListener listener) {
            this.support.addPropertyChangeListener(propertyName, listener);
        }
        /**
         * @param listener
         * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
         */
        public void removePropertyChangeListener(final PropertyChangeListener listener) {
            this.support.removePropertyChangeListener(listener);
        }
        /**
         * @param propertyName
         * @param listener
         * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
         */
        public void removePropertyChangeListener(final String propertyName,
                final PropertyChangeListener listener) {
            this.support.removePropertyChangeListener(propertyName, listener);
        }
        
    }

    /**
     * @param context
     * @param name
     * @param helpId
     * @throws HeadlessException
     */
    public MultiConeImpl(final UIContext context
            , final ActivityFactory activityBuilder
            , final TypesafeObjectBuilder uiBuilder
            , final ApplicationsInternal apps
            , final RemoteProcessManagerInternal rpm
            , final VosiInternal vosi
            , final RegistryInternal reg
            , final ResourceChooserInternal fileChooser
            , final RegistryGoogleInternal regChooser
            )
            throws HeadlessException {
        super(context, "Multi Position Search", "window.multicone");
        this.apps = apps;
        this.rpm = rpm;
        this.vosi = vosi;
        this.reg = reg;
        logger.info("Constructing new MultiCone");        
        this.fileChooser = fileChooser;
        this.regChooser = regChooser;
        
        // construct model
        model = new Model();
        // construct actions
        findTable = new FindTableAction();
        execute = new ExecuteAction();       
        suggest = new SuggestAction();
        chooseService = new ChooseService();
        
       // create input form.
        final FormLayout layout = new FormLayout(
                "2dlu,right:max(40dlu;min),3dlu,fill:100dlu,1dlu,m,5dlu,fill:240px:grow,2dlu"
                ,"p,p,top:max(15dlu;min),p,p,p,p,bottom:max(20dlu;p),p,p,bottom:max(20dlu;p),p,top:max(15dlu;min),top:max(20dlu;p),max(30dlu;p)");
        final PanelBuilder builder = new PanelBuilder(layout);
        final CellConstraints cc = new CellConstraints();
        int row = 1;
        final int labelCol = 2;
        final int fieldCol = 4;
        final int assistCol = 6;
        
        builder.addSeparator("Positions",cc.xyw(labelCol,row,5));
        row++;
        
        builder.addLabel("Table URL",cc.xy(labelCol,row));                
        final JTextField tableField = new JTextField();
        tableField.setEditable(false);
        model.addPropertyChangeListener(TABLE_PROPERTY,new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent evt) { 
                tableField.setText(model.getTableURL() == null ? "" : model.getTableURL().toString());
            }
        });
       
        tableField.setToolTipText("A file containing a table of positions. Format may be VOTable or CSV");
        tableField.setFont(UIConstants.SMALL_DIALOG_FONT);
        builder.add(tableField,cc.xy(fieldCol,row));
        final JButton findTableButton = new JButton(findTable);
        findTableButton.setText("");
        builder.add(findTableButton,cc.xy(assistCol,row));
        row++;
        
        final JScrollBar tableScroll = new JScrollBar(JScrollBar.HORIZONTAL);        
        final BoundedRangeModel brm = tableField.getHorizontalVisibility();
        tableScroll.setModel(brm);
        builder.add(tableScroll,cc.xy(fieldCol,row));
        row++;
        
        
        format = new JComboBox(new String[]{"(auto)","VOTable","CSV","ASCII","FITS","FITS-plus","colfits-plus","colfits-basic","TST","IPAC","WDC"});
        format.setToolTipText("The format of the input table");
        format.setSelectedIndex(0);
        
        builder.addLabel("Format",cc.xy(labelCol,row));
        builder.add(format,cc.xy(fieldCol,row));
        
        row++;

        // this is the underlying model for the ra combo box. Keeps items sorted
        // and ensures that things matching 'ra' are on top.
        raColList = new SortedList<String>(new BasicEventList<String>()
                , new SearchTermFavoringComparator<String>(new Matcher<String>() {

                    public boolean matches(final String item) {
                        return StringUtils.startsWithIgnoreCase(item,"ra");
                    }
                })
                );
        raCol = new JComboBox(new EventComboBoxModel(raColList));
        raCol.setEditable(true);
        raCol.setToolTipText("The name of the RA column in the Table");

        // ditto for dec
        decColList = new SortedList<String>(new BasicEventList<String>()
                , new SearchTermFavoringComparator<String>(new Matcher<String>() {

                    public boolean matches(final String item) {
                        return StringUtils.startsWithIgnoreCase(item,"dec");
                    }
                })
                );        
        decCol = new JComboBox(new EventComboBoxModel(decColList));
        decCol.setEditable(true);
        decCol.setToolTipText("The name of the Dec column in the Table");

        
        builder.addLabel("RA Column",cc.xy(labelCol,row));
        builder.add(raCol,cc.xy(fieldCol,row));
        
        final JButton suggestButton = new JButton(suggest);
        suggestButton.setText("");
        indicator = new IndeterminateProgressIndicator();
        indicator.setDisplayedWhenStopped(false);
        suggestButton.add(indicator);
        builder.add(suggestButton,cc.xy(assistCol,row));
        row++;
        
        builder.addLabel("Dec Column",cc.xy(labelCol,row));
        builder.add(decCol,cc.xy(fieldCol,row));
        row++;

        srField = new JTextField();
        srField.setText("0.01");
        srField.setToolTipText("Search radius in decimal degrees, or name of a column in the Table");

        builder.addLabel("Search Radius",cc.xy(labelCol,row));
        builder.add(srField,cc.xy(fieldCol,row));
        row++;
        
        builder.addSeparator("Output",cc.xyw(labelCol,row,5));
        row++;
        
        allButton = new JRadioButton("All");
        allButton.setToolTipText("For each search position, return all matches within search radius");
        bestButton = new JRadioButton("Best");
        bestButton.setToolTipText("For each search position, return the best single match");
        final ButtonGroup group = new ButtonGroup();
        group.add(allButton);
        group.add(bestButton);
        allButton.setSelected(true);
        builder.addLabel("Match",cc.xy(labelCol,row));
        builder.add(allButton,cc.xy(fieldCol,row));
        row++;
        builder.add(bestButton,cc.xy(fieldCol,row));
        row++;
        

        builder.addSeparator("Service",cc.xyw(labelCol,row,5));
        row++;
        
        builder.addLabel("IVOA-ID",cc.xy(labelCol,row));
        final JTextField ivornField = new JTextField();
        ivornField.setFont(UIConstants.SMALL_DIALOG_FONT);
        ivornField.setToolTipText("The IVOA-ID of the service to match against");
        ivornField.setEditable(false);
        model.addPropertyChangeListener(SERVICE_PROPERTY,new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent evt) {
                ivornField.setText( model.getServiceURL() == null ? "" : model.getServiceURL().toString());
            }
        });
      
        
        builder.add(ivornField,cc.xy(fieldCol,row));
        final JButton serviceButton = new JButton(chooseService);
        serviceButton.setText("");
        builder.add(serviceButton,cc.xy(assistCol,row));
        row++;
        
        final JScrollBar ivoScroll = new JScrollBar(JScrollBar.HORIZONTAL);        
        final BoundedRangeModel brm1 = ivornField.getHorizontalVisibility();
        ivoScroll.setModel(brm1);
        builder.add(ivoScroll,cc.xy(fieldCol,row));
        row++;
                
        builder.addLabel("Capability",cc.xy(labelCol,row));
        final EventComboBoxModel<ConeCapability> capModel = new EventComboBoxModel<ConeCapability>(model.getCapabilities());        
        capability = new JComboBox(capModel);
        capability.setEnabled(false);
        capability.setRenderer(new BasicComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(final JList list,
                    final Object value, final int index, final boolean isSelected,
                    final boolean cellHasFocus) {
                if (value == null) {
                    return super.getListCellRendererComponent(list, value, index, isSelected,
                            cellHasFocus);                    
                }
                final ConeCapability cap = (ConeCapability)value;   
                return super.getListCellRendererComponent(list, cap.getDescription() == null ? "unnamed" : cap.getDescription(), index, isSelected,
                        cellHasFocus);
            }
            
        });
        
        capability.setToolTipText("Some services have more than one cone-search capability. Select which capability to query here");
        builder.add(capability,cc.xy(fieldCol,row));
        
        row++;
        final JButton execButton = new JButton(execute);
        builder.add(execButton,cc.xyw(labelCol,row,5));
        
        
        // processes panel.
        // cribbed from TaskRunnrImpl
        this.tracker = uiBuilder.createExecutionTracker(this);
        //@todo re-poopulate UI / model from Tool document.
        //involves working out what reg resource belongs to a particular endpoint, etc.
//        this.tracker.addShowDetailsListener(new ExecutionTracker.ShowDetailsListener() {
//
//            public void showDetails(final ShowDetailsEvent e) {
//                final ProcessMonitor monitor = e.getMoitor();
//                if (monitor instanceof ProcessMonitor.Advanced) {
//                    (new BackgroundWorker<Void>(MultiConeImpl.this,"Loading Parameters") {
//
//                        @Override
//                        protected Void construct() throws Exception {
//                            final Tool tool = ((ProcessMonitor.Advanced)monitor).getInvocationTool();
//                            
//                            return null;
//                        }
//                    }).start();
//                    
//                }
//            }
//        });
        this.trackerPanel = tracker.getPanel();
        trackerPanel.setBackground(Color.WHITE);
        trackerPanel.setBorder(BorderFactory.createEmptyBorder());
        
        final UIComponentMenuBar menuBar = new UIComponentMenuBar(this) {

            @Override
            protected void populateEditMenu(final EditMenuBuilder emb) {
                emb.cut().copy().paste().selectAll();
            }

            @Override
            protected void populateFileMenu(final FileMenuBuilder fmb) {
                fmb
                    .windowOperation(findTable)
                    .windowOperation(suggest)
                    .windowOperation(chooseService)
                    .windowOperationWithIcon(execute)
                    .separator()
                    ;
                    fmb.closeWindow()
                    ;
            }
            @Override
            protected void constructAdditionalMenus() {
                // cribbed from taskrunner.
                final MenuBuilder rmb = new MenuBuilder("Result",KeyEvent.VK_R);
                rmb
                    .windowOperation(tracker.getActs().getActivity(ViewInBrowserActivity.class))
                    .windowOperation(tracker.getActs().getActivity(SimpleDownloadActivity.class))
                    .windowOperation(tracker.getActs().getActivity(MultiConeActivity.class))
                      ;
                tracker.getActs().getActivity(MessagingScavenger.class).addTo(rmb.getMenu());
                add(rmb.create());                
            }
        };
        setJMenuBar(menuBar);
        
        // assemble the window.
        final JScrollPane execScroll = new JScrollPane(trackerPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        execScroll.setBorder(BorderFactory.createEtchedBorder());
        
        final JScrollPane tasksScroll = new JScrollPane(tracker.getTaskPane(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tasksScroll.setBorder(BorderFactory.createEmptyBorder());
        rightPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT
                ,execScroll
                ,tasksScroll);
        rightPane.setDividerLocation(150);
        rightPane.setDividerSize(7);
        rightPane.setBorder(BorderFactory.createEmptyBorder());
        rightPane.setResizeWeight(0.7);
        rightPane.setPreferredSize(new Dimension(250,400));    
        
        // stick this all in the far side of the panel.
        builder.add(rightPane,cc.xywh(8,1,1,row)); // row is currently at bottom of panel, so gives max extent.
        
        final JPanel pane = getMainPanel();
        pane.add(builder.getPanel(),BorderLayout.CENTER);
        this.pack();
        this.setTitle("Multi Position Search");      
        setIconImage(IconHelper.loadIcon("multicone16.png").getImage());   
        logger.info("New MultiCone - Completed");        
    }

    public void multiCone(final ConeService s) {
        model.setService(s);
    }

    public void multiCone(final URI file) {
        model.setTableURL(file);        
    }
    
    /** comparator that favours things that match a matcher, else just compares them.
     * 
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Apr 17, 20091:30:50 AM
     */
    private static class SearchTermFavoringComparator<A extends Comparable> implements Comparator<A> {
        /**
         * @param matcher
         */
        public SearchTermFavoringComparator(final Matcher<A> matcher) {
            super();
            this.matcher = matcher;
        }
        private final Matcher<A> matcher;
        public int compare(final A a, final A b) {
            final boolean aMatches = matcher.matches(a);
            final boolean bMatches = matcher.matches(b);
            if (aMatches) {
                if (bMatches) {
                    return a.compareTo(b);
                } else {
                    return -1;
                }
            } else if (bMatches) {
                return 1;
            } else {
                return a.compareTo(b);
            }
        }
    }
    
    
    /**  choose the table to load */
    private class FindTableAction extends AbstractAction {
        /**
         * 
         */
        public FindTableAction() {
            super("Choose Table" + UIComponentMenuBar.ELLIPSIS,IconHelper.loadIcon("fileopen16.png"));
            putValue(Action.SHORT_DESCRIPTION,"Select a table of positions using a file browser dialogue");
        }
        public void actionPerformed(final ActionEvent e) {
            final URI uri = fileChooser.chooseResourceWithParent("Select a table of positions",true,true,true,MultiConeImpl.this);
            if (uri != null) {
                model.setTableURL(uri);
                raColList.clear();
                decColList.clear();
            }
            
        }
    }
    private final Preferences prefs = Preferences.userNodeForPackage(MultiConeImpl.class);
    private static final String FIRST_RUN_KEY = "multicone.isFirstRun";
    
    private void offerToRelocate() {
        final boolean loggedIn = getContext().getLoggedInModel().isEnabled();
        final boolean firstRun = prefs.getBoolean(FIRST_RUN_KEY,true);
        if ( ! loggedIn || firstRun) {
            ConfirmDialog.newConfirmDialog(this,"Upload this file to VO Workspace?"
                    , "<html>You have selected a file on a local disk. Local disks cannot be accessed by remote applications."
                    +"<br>Do you want to have this file uploaded to your VO Workspace, where it will be accessible?"
                    + (! loggedIn ? "<br>(This will require you to login)" :"")
                    , new Runnable() {
                public void run() {
                    if (firstRun) {
                        prefs.putBoolean(FIRST_RUN_KEY,false);
                    }                    
                    relocateFile();
                }
            }
            ).setVisible(true);
        } else {
            relocateFile();            
        }              
    }
    
    private void relocateFile() {
        final URI u = model.getTableURL();
        final CopyCommand cmd = new CopyCommand(u);
        showTransientMessage("Uploading","Copying " + u);
        new BulkCopyWorker(fileChooser.getVFS(),this,workingDir,new CopyCommand[]{cmd}){            
            @Override
            protected void doFinished(final Object result) {
                super.doFinished(result);
                if (! cmd.failed()) {                  
                    try {
                        final URI newU =new URI (StringUtils.replace(cmd.getDestination().getURI().trim()," ","%20"));
                        model.setTableURL(newU);
                        execute.actionPerformed(null); // call back into the 'execute' action - and try again.
                    } catch (final URISyntaxException e) {
                        showTransientError("File was uploaded, but then something failed","Navigate to " + workingDir + " and select the file manually");
                    }
                }            
            }
        }.start();

        return;        
    }
    
    private final static URI workingDir = URI.create("workspace:///multiquery-working/");

    
    /** execute the multi-cone
     * listens to 'validate' on the model to see whether to enable or not. */
    private class ExecuteAction extends AbstractAction implements PropertyChangeListener{
        /**
         * 
         */
        public ExecuteAction() {
            super("Start Search!",IconHelper.loadIcon("run16.png"));
            setEnabled(false);
            model.addPropertyChangeListener(VALID_PROPERTY,this);
        }
        public void actionPerformed(final ActionEvent e) {
            // copy values from model.                       
            final URI tableURL = model.getTableURL();
            // check that tableURL is CEA-accessible. if not, offer to move, and bug out.
            if (!LocalFileUploadAssistant.isCeaAccessible(tableURL)) {
                offerToRelocate();
                return; 
            }
            if (raCol.getSelectedItem() == null || decCol.getSelectedItem() == null) {
                showTransientError("Incomplete Form","You must provide values for RA Column and Dec Column");
                return;
            }
            final String ra = raCol.getSelectedItem().toString();
            final String dec = decCol.getSelectedItem().toString();
            final String sr = srField.getText();
            final boolean all = allButton.isSelected();
            final String fmt = format.getSelectedItem().toString();
            final URI serviceEndpoint;
            final String title;
            final String description;
            if (model.getService() == null) {
                serviceEndpoint = model.getServiceURL();
                title = "URL-based Service";
                description = "Multi Cone Catalog Query against " + serviceEndpoint;
            } else {
                final ConeCapability cap = (ConeCapability)capability.getSelectedItem();
                serviceEndpoint = cap.getInterfaces()[0].getAccessUrls()[0].getValueURI();
                title = model.getService().getTitle();            
                description = "Multi Cone Catalog Query :" 
                + (model.getCapabilities().size() > 1 
                        ?  cap.getDescription()
                        : ""
                            );
            }
           (new BackgroundWorker<Void>(MultiConeImpl.this,"Launching Multi Search") {
               private Tool constructTool(final CeaApplication stilts) {
                   // create a template tool
                   reportProgress("Creating Invocation Document");                   
                   final Tool tool = apps.createTemplateTool(IFACE_NAME,stilts);
                   final ParameterValue[] values = tool.getInput().getParameter();
                   // loop through each of the parameters,setting the ones we care about.
                   for (final ParameterValue pv : values) {
                       final String name = pv.getName();
                       if (name.equals("coneskymatch_in")) {
                           pv.setIndirect(true);
                           pv.setValue(tableURL.toString());
                       } else if (name.equals("coneskymatch_ofmt")) {
                           pv.setValue("votable-tabledata");
                       } else if (name.equals("coneskymatch_ra")) {
                           pv.setValue(ra);
                       } else if (name.equals("coneskymatch_dec")) {
                           pv.setValue(dec);
                       } else if (name.equals("coneskymatch_sr")) {
                           pv.setValue(sr);
                       } else if (name.equals("coneskymatch_find")) {
                           pv.setValue(all ? "all" : "best");
                       } else if (name.equals("coneskymatch_parallel")) {
                           pv.setValue("3"); // try three queries in parallel.
                       } else if (name.equals("coneskymatch_erract")) {
                           // default is abort. wonder if 'retry' would be better.
                           // would like to find out if there's a retry-n option
                       } else if (name.equals("coneskymatch_verb")) {
                           pv.setValue("3");
                       } else if (name.equals("coneskymatch_serviceurl")) {
                           pv.setValue(serviceEndpoint.toString());
                       } else if (name.equals("coneskymatch_ifmt")) {
                           pv.setValue(fmt);                       
                       } else if (name.equals("coneskymatch_icmd") || name.equals("coneskymatch_ocmd")) {
                           // remove these optional parameters, as having blank values screws up the service.
                           tool.getInput().removeParameter(pv);
                       }
                   }
                   return tool;
               }
            @Override
            protected Void construct() throws Exception {
                // find the application
                reportProgress("Finding Application");
                final CeaApplication stilts = apps.getCeaApplication(APPLICATION_ID);

                // build the invoation tool document
                final Tool tool = constructTool(stilts);

                // find the best service
                reportProgress("Finding Execution Service");                                
                final CeaService service = findBestService(stilts);
                
                // ok. send it off for execution. - Copied from TaskRunnerImpl (for now)
                logger.debug("Executing");
                reportProgress("Serializing document");
                final Document doc = XMLUtils.newDocument();
                Marshaller.marshal(tool,doc);
                
                // ok. looks like a goer - lets create a monitor.
                final ProcessMonitor monitor = rpm.create(doc);
                if (monitor instanceof AbstractProcessMonitor) {
                    final AbstractProcessMonitor am = (AbstractProcessMonitor)monitor;
                    am.setName(title);
                    am.setDescription(description);
                }               
                reportProgress("Created monitor");
                // start tracking it - i.e. display it in the ui.
                // we do this early, even before we start it to show some visual progress.
                tracker.add(monitor);

                // start it off
                reportProgress("Starting Execution on " + service.getId());
                try {
                    monitor.start(service.getId());
                    reportProgress("Started task");
                } catch (final ACRException x) {
                    // catching exceptions here - don't want them to propagate
                    // (and so be reported as a popup dialogue
                    // and these exceptions from the monitor will
                    // be messaged back to the tracker anyhow.
                    logger.warn("ServiceException",x);
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            parent.showTransientError("Unable to execute task - see transcript for details",ExceptionFormatter.formatException(x));
                        }

                    });
                    return null; // bail out.
                } 

                rpm.addMonitor(monitor); // it's running - so now can add it to the rpmi's monitor list.   
                return null;
            }
            /** Find the best service to execute on.
             * prefer one that has UWS, but also prefer one that's up according to vosi.
             * @param stilts
             * @return
             * @throws ServiceException
             * @throws NotFoundException
             * @throws InvalidArgumentException
             */
            private CeaService findBestService(final CeaApplication stilts)
                    throws ServiceException, NotFoundException,
                    InvalidArgumentException {
                final List<CeaService> sList = new ArrayList<CeaService>();
              
                // populate this list.
                for (final CeaService s : (CeaService[])apps.listServersProviding(stilts.getId())) {
                    try {
                        final VosiAvailabilityBean bean = vosi.checkAvailability(s.getId());
                        if (bean.isAvailable()) {
                            sList.add(s);
                        }                                       
                    } catch (final InvalidArgumentException ignored) {
                        // no vosi capability - so add it, just incase.
                       sList.add(s);
                    }
                    
                }
                // first item will now be the most suitable.
                if (sList.isEmpty()) {
                    throw new ServiceException("No Available CEA Servers - can't proceed");
                }
                                    
                CeaService service = sList.get(0); // set a suitable default.
                // if there's more than one service, scan through for a better one.
                // i.e. one providing UWS.
                if (sList.size() > 1) {
                   for (final CeaService ceaService : sList) {
                        if (null != ceaAlsoHasUwsInterface(ceaService)) {
                            service = ceaService;
                            break;
                        }
                        
                    }
                }
                return service;
            }
                
          
           }).start();
        }
        public void propertyChange(final PropertyChangeEvent evt) {
            setEnabled(model.isValid());
        }        
    }
    
    /** will return true if this cea server also doubles as UWS 
     * returns the endpoint of the UWS backend, (or null, if none).
     * */
    public static URI ceaAlsoHasUwsInterface(final CeaService service) {
        final Interface[] interfaces = service.findCeaServerCapability().getInterfaces();
        for (final Interface i : interfaces) {
            if (i.getType().contains("UWS-PA")) {
                return i.getAccessUrls()[0].getValueURI();
            }
        }
        return null;
    }
        
    public static final URI APPLICATION_ID = URI.create("ivo://uk.ac.starlink/stilts");
    public static final String IFACE_NAME = "coneskymatch";
    
    /** inspect the selected file, and suggest column names
     * Becomes enabled when tableURL is non-null in model. */
    private class SuggestAction extends AbstractAction implements PropertyChangeListener {
        public SuggestAction() {
            super("Suggest Column Names",IconHelper.loadIcon("wizard16.png"));
            putValue(Action.SHORT_DESCRIPTION,"Suggest column names by inspecting the selected table");
            setEnabled(false);
            model.addPropertyChangeListener(TABLE_PROPERTY,this);
            
        }
        public void actionPerformed(final ActionEvent e) {   
            final URI tableURI = model.getTableURL();
            suggest.setEnabled(false); // as it's already running.
            indicator.startAnimation();
            (new BackgroundWorker<List<String>>(MultiConeImpl.this,"Examining the table columns") {
                {
                    setTransient(true);
                }
                @Override
                protected List<String> construct() throws Exception {
                    final List<String> cols = new ArrayList<String>();     
                    final StarTableFactory factory = new StarTableFactory();
                    // need to use vfs to convert the URI into a URI.
                    // if I just use URI.toURL(), or new URL(uri.toString())
                    //I get a parse error from URL, with nulls in it.
                    // using vfs works fine - with normal files, and also stranger things like myspace and sftp
                    // would imagine it will work with vospace too.
                    final URL u = fileChooser.getVFS().resolveFile(tableURI.toString()).getURL();
                    final StarTable starTable = factory.makeStarTable(u,format.getSelectedItem().toString());
                    for (int i = 0; i < starTable.getColumnCount(); i++) {
                        final ColumnInfo columnInfo = starTable.getColumnInfo(i);
                        cols.add(columnInfo.getName());
                    }
                    return cols;
                }
                @Override
                protected void doFinished(final List<String> cols) {
                    raColList.clear();
                    decColList.clear();
                    if (cols.size() > 0) {
                        raColList.addAll(cols);
                        raCol.setSelectedIndex(0);             
                        decColList.addAll(cols);
                        decCol.setSelectedIndex(0);
                    }
                }
                @Override               
                protected void doAlways() {
                   indicator.stopAnimation();
                   suggest.setEnabled(true);
                }
            }).start();
        }
        public void propertyChange(final PropertyChangeEvent evt) {
            setEnabled(evt.getNewValue() != null);
        }
    }
    
        
    
    /** choose the service to query */
    private class ChooseService extends AbstractAction {
        /**
         * 
         */
        public ChooseService() {
            super("Choose service" + UIComponentMenuBar.ELLIPSIS,IconHelper.loadIcon("search16.png"));
            putValue(Action.SHORT_DESCRIPTION,"Select a service to match against using a voexplorer dialogue");
          
        }
        public void actionPerformed(final ActionEvent e) {
            final Resource[] resources = regChooser.selectResourcesFullUI("Select a Service to match against (filtering to only catalog cone search services)",false,new Matcher<Resource>() {

                public boolean matches(final Resource item) {
                    return item instanceof ConeService;
                }
            },MultiConeImpl.this);
            
            if (resources.length == 0) {
                return;
            }
            final Resource resource = resources[0];
            if (resource instanceof ConeService) { // otherwise ignore (should be prevented by dialogue anyhow)
                model.setService((ConeService)resources[0]);
            }
            
        }
    }


}
