/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.TapCapability;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
import org.astrogrid.desktop.modules.dialogs.ConfirmDialog;
import org.astrogrid.desktop.modules.dialogs.RegistryGoogleInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.ui.RetriableBackgroundWorker;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.MonitoringInputStream;
import org.astrogrid.desktop.modules.ui.TaskRunnerInternal;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.actions.MessagingScavenger;
import org.astrogrid.desktop.modules.ui.actions.RevealFileActivity;
import org.astrogrid.desktop.modules.ui.actions.SimpleDownloadActivity;
import org.astrogrid.desktop.modules.ui.actions.ViewInBrowserActivity;
import org.astrogrid.desktop.modules.ui.comp.EventListDropDownButton;
import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker.ShowDetailsEvent;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker.ShowDetailsListener;
import org.astrogrid.desktop.modules.votech.VoMonInternal;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.votech.VoMonBean;
import org.w3c.dom.Document;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.FunctionList.Function;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Implementation of the task runner.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 200712:30:46 PM
 */
public class TaskRunnerImpl extends UIComponentImpl implements TaskRunnerInternal, ShowDetailsListener, UIComponentWithMenu{
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory.getLog(TaskRunnerImpl.class);
	public final static String POPULATE = "populate";
	public final static String REFRESH = "refresh";
	public final static String HALT = "halt";
    private final Registry reg;
	
    /**
	 * @param context
	 * @throws HeadlessException
	 */
	public TaskRunnerImpl(final UIContext context, final ApplicationsInternal apps
	        ,final RemoteProcessManagerInternal rpmi,final ResourceChooserInternal rci
	        ,final RegistryGoogleInternal regChooser,final TypesafeObjectBuilder builder
	        ,final VoMonInternal vomon
	        ,final Registry reg) throws HeadlessException {
		super(context,"Task Runner","window.taskrunner");
        this.reg = reg;
		logger.info("Constructing new TaskRunner");
        this.rpmi = rpmi;
		this.fileChooser = rci;
		this.regChooser = regChooser;
        this.vfs = rci.getVFS();
        this.vomon = vomon;
		this.apps = apps;

	      // form panel
        pForm = builder.createTaskParametersForm(this);
        
        // processes panel
        this.tracker = builder.createExecutionTracker(this);
        tracker.addShowDetailsListener(this);
        trackerPanel = tracker.getPanel();
        trackerPanel.setBackground(Color.WHITE);
        trackerPanel.setBorder(BorderFactory.createEmptyBorder());
        // tasks pane.

		
        // menubar
		final UIComponentMenuBar menuBar = new UIComponentMenuBar(this) {

		    @Override
            protected void constructAdditionalMenus() {
                final JMenu taskMenu = new JMenu("Task");
                taskMenu.setMnemonic(KeyEvent.VK_T);
                /* can't get at these reliably.
                    .windowOperation(toolbar.getExecuteAction())
                    ;
                    .separator()
                    .componentOperation("Refresh",REFRESH,KeyStroke.getKeyStroke(KeyEvent.VK_R,UIComponentMenuBar.MENU_KEYMASK))
                    .componentOperation("Halt",HALT,KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD,UIComponentMenuBar.MENU_KEYMASK))
                    .componentOperation("Delete",UIComponentMenuBar.EditMenuBuilder.DELETE,KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,UIComponentMenuBar.MENU_KEYMASK))
                    ;*/
                // define the toolbar here too.
                toolbar = new ExecutingTaskRunnerToolbar(taskMenu);
                add(taskMenu);
                
                final MenuBuilder rmb = new MenuBuilder("Result",KeyEvent.VK_R);
                rmb
                    .windowOperation(tracker.getActs().getActivity(ViewInBrowserActivity.class))
                    .windowOperation(tracker.getActs().getActivity(SimpleDownloadActivity.class))
                    .windowOperation(tracker.getActs().getActivity(RevealFileActivity.class))
                      ;
                tracker.getActs().getActivity(MessagingScavenger.class).addTo(rmb.getMenu());
                add(rmb.create());
            }
            @Override
            protected void populateEditMenu(final EditMenuBuilder emb) {
                emb  
                    .cut()
                    .copy()
                    .paste()
                    .selectAll()
                    .separator();
                // register the editor action as a listener here - by now everything it depends upon will have been instantiated.
                pForm.getBottomPane().addComponentListener(showHideFullEditor);
                pForm.getInterfaceCombo().addItemListener(showHideFullEditor);
                emb
                    .windowOperation(showHideFullEditor)
                    .submenu(contextMenu) //enable this on show / hide of lower window
                    .separator()
                    .windowOperation(reset)
                    //.componentOperation("Populate from Task",POPULATE,null) // can't attach.
                    ;
            }
            @Override
            protected void populateFileMenu(final FileMenuBuilder fmb) {
		        fmb
		            .windowOperation(newTask)
		            .windowOperation(open)
		            .windowOperation(save)
		            .windowOperation(saveAs)
		        ;
		        fmb.closeWindow();
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
		rightPane.setDividerLocation(250);
		rightPane.setDividerSize(7);
		rightPane.setBorder(BorderFactory.createEmptyBorder());
		rightPane.setResizeWeight(0.7);
		rightPane.setPreferredSize(new Dimension(200,600));
		pForm.getBottomPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(final ComponentEvent e) {
                    tasksScroll.setVisible(true);
                    rightPane.setDividerLocation(0.5);     
                    rightPane.setDividerSize(7);                    
            }
            
            @Override
            public void componentShown(final ComponentEvent e) {
                tasksScroll.setVisible(false);
                rightPane.setDividerLocation(1.0);
                rightPane.setDividerSize(0);
            }
		});
		pForm.setToolbar(toolbar);
		pForm.setRightPane("Execute",rightPane);
		
		// construct a flip panel to keep both together.
		flipper = new FlipPanel();
		flipper.add(pForm,PARAMETER_VIEW);
		adqlOnlyForm = new JPanel(new BorderLayout());	
		flipper.add(adqlOnlyForm,ADQL_ONLY_VIEW);
		getMainPanel().add(flipper,BorderLayout.CENTER);
		clearStorageLocation();
		setIconImage(IconHelper.loadIcon("applaunch16.png").getImage());
		this.setSize(900,600);
		logger.info("New TaskRunner - Completed");
	}
	
	private final String PARAMETER_VIEW = "parameter.view";
	private final String ADQL_ONLY_VIEW = "adql.view";
	
	
	   
    /** instruct just the adql editor to show, and no other
     * input widgets.
     * @param b if true, show adql editor only.
     */
    public void showADQLOnly(final boolean b) {
        if (adqlOnly == b) { // nothing to do.
            return;
        } else {
            adqlOnly = b;
        }

        if (b) {
            final AdqlTextFormElement adqlFormElement = pForm.getAdqlFormElement();
           // find the adql editor, and relocate it
            pForm.getBottomPane().removeAll();
            adqlOnlyForm.add(adqlFormElement.getEditorPanel(),BorderLayout.CENTER);
            //toolbar
            pForm.remove(toolbar);
            adqlOnlyForm.add(toolbar,BorderLayout.NORTH);
            
            // find the right-pane, and relocated it
            pForm.remove(rightPane);
            adqlOnlyForm.add(rightPane,BorderLayout.EAST);
            
            flipper.show(ADQL_ONLY_VIEW);
        } else {
            adqlOnlyForm.removeAll();
            // adql editor
            final AdqlTextFormElement adqlFormElement = pForm.getAdqlFormElement();
            if (adqlFormElement != null) {
                pForm.getBottomPane().add(adqlFormElement.getEditorPanel());
            }
            //toobar
            pForm.setToolbar(toolbar);
            //right-pane
            pForm.setRightPane("Execute",rightPane);
            
            flipper.show(PARAMETER_VIEW);
        }        
    }
    
    private boolean adqlOnly = false;
    

    
	private final JMenu contextMenu = new JMenu("Query extras") {
        {
            setEnabled(false);
        }
        @Override
        public void setEnabled(final boolean b) {
            super.setEnabled(b);
            setVisible(b);
        } 
    };	
	 private final ResourceChooserInternal fileChooser;
	 private final RegistryGoogleInternal regChooser;
	 private final RemoteProcessManagerInternal rpmi;
	 private URI storageLocation;
	 protected ExecutingTaskRunnerToolbar toolbar;   
	 
	
	  final ExecutionTracker tracker;
	    
	    private final JPanel trackerPanel;	    
	private final FileSystemManager vfs;
	private final VoMonInternal vomon;
	protected final ApplicationsInternal apps;
	//private final DescriptionsPanel descriptions;
	protected final TaskParametersForm pForm;
	final Action newTask = new NewTaskAction();
	final Action open = new OpenAction();


	final Action reset = new ResetAction();
	final Action save = new SaveAction();
	final Action saveAs = new SaveAsAction();
	protected final ShowHideFullEditorAction showHideFullEditor = new ShowHideFullEditorAction();
	
	// @future - maybe pass this in, and populate through contribution?
	private final TweaksSelector tweakSelector = new TweaksSelector();
	protected ProtocolSpecificTweaks tweaks;
    private final JSplitPane rightPane;
    private final FlipPanel flipper;
    private final JPanel adqlOnlyForm;
	
	public void buildForm(final Resource r) {
	    toolbar.executionServers.clear();	    
	    tweaks = tweakSelector.findTweaks(r);
	    tweaks.buildForm(this);
		updateWindowTitle();
		
	}
	
    public void edit(final FileObject o) {
        loadToolDocument(o);
    }

    public void buildForm(final Tool t,final Resource r) {
        toolbar.executionServers.clear();
        tweaks = tweakSelector.findTweaks(r);
        tweaks.buildForm(t,this);
	}
    public Object create() {
		// deliberately not implemented. will work out how to do this later.
		return null;
	}
    public JMenu getContextMenu() {
        return contextMenu;
    }

	public void invokeTask(final Resource r) {
		buildForm(r);
	}
	
	
	/** simple toolbar for taskrunner */;
	// show details event listener - a callback from the task monitor.	
    public void showDetails(final ShowDetailsEvent e) {
        final ProcessMonitor monitor = e.getMoitor();
        if (monitor instanceof ProcessMonitor.Advanced) {
            // now need to take a copy of this tool, and load this copy into the editor.
            //can't use original, otherwise edits to the form will alter the original - rewriting history.
            // copy the tool object by marshalling to and from xml.
            (new BackgroundWorker(this,"Loading Parameters") {
                private Tool nTool;
                private Resource newRes;
                @Override
                protected Object construct() throws Exception {
                    final Tool tool = ((ProcessMonitor.Advanced)monitor).getInvocationTool();
                    Writer sw = null;
                    Reader r = null;                  
                    try {
                        sw = new StringWriter();
                        tool.marshal(sw);
                        r = new StringReader(sw.toString());
                        nTool = Tool.unmarshalTool(r);                        
                        newRes= reg.getResource(new URI("ivo://" + tool.getName()));
                        return null; // all passed back in member variables.
                   } finally {
                      IOUtils.closeQuietly(sw);
                      IOUtils.closeQuietly(r);
                    }                    
                }
                @Override
                protected void doFinished(final Object result) {
                    buildForm(nTool,newRes);

                }
            }).start();
          
        }
    }
	private void clearStorageLocation() {
	     storageLocation = null;
	     updateWindowTitle();
	 }
	
	private URI getStorageLocation() {
	     return storageLocation;
	 }
    private void setStorageLocation(final URI u) {
	     storageLocation = u;
	     updateWindowTitle();
	 }
    
    /** update window title, based on whether there's a curent resource, and/or a storage location */
    private void updateWindowTitle() {
        final CeaApplication res = pForm.getModel().currentResource();
        final String loc = storageLocation == null ? "untitled" : storageLocation.toString();
        if (res == null) {
            setTitle("Task Runner - " + loc)   ;
        } else {
            setTitle("Task Runner for " + res.getTitle() + " - " + loc);
        }
    }
    

    /** load a tool document from storage
     * @param o - either a vfs.FileObject, or a URI
     */
    private void loadToolDocument(final Object o) {
        (new BackgroundOperation("Opening task document",Thread.MAX_PRIORITY) {
            private Resource newRes;            
            private FileObject fo;
            @Override
            protected Object construct() throws Exception {
            	Reader fr = null;
            	try {
            	    if (o instanceof URI) {
            	        fo = vfs.resolveFile(o.toString());
            	    } else if (o instanceof FileObject) {
            	        fo = (FileObject)o;
            	    }
            	    reportProgress("Resolved file");
            	    final MonitoringInputStream mis = MonitoringInputStream.create(this,fo,MonitoringInputStream.ONE_KB);
            	    fr = new InputStreamReader(mis);
               final Tool t = Tool.unmarshalTool(fr);
               reportProgress("Loaded file contents");
               
               
               newRes =reg.getResource(new URI("ivo://" + t.getName()));      
               reportProgress("Found associated registry resource");
               reportProgress("Completed");
               return t;
            	} finally {
            		IOUtils.closeQuietly(fr);
            	}
            }
            @Override
            protected void doFinished(final Object o) {
            	buildForm((Tool)o,newRes);
            	try {
                    setStorageLocation(new URI(StringUtils.replace(fo.getName().getURI().trim()," ","%20")));
                } catch (final URISyntaxException x) {
                    logger.error("URISyntaxException",x);
                    // unlikely.
                }
            }
        }).start();
    }
    /** toolbar pane - appears at the top of the window */
    public static class TaskRunnerToolbar extends JPanel {
	    /**
         * 
         */
        public TaskRunnerToolbar(final TaskParametersForm pForm, final Action chooseAppAction) {
            builder = new PanelBuilder(new FormLayout(
                    "2dlu,d,200dlu,10dlu,right:d,d,10dlu:grow,d,2dlu" // cols
                    ,"d" // rows
                    ),this);
            cc = new CellConstraints();
            final JButton appButton = new JButton(chooseAppAction);
            // show icon only.
            appButton.setText(null);
            int col = 2;
            builder.add(appButton,cc.xy(col++,1));
            builder.add(pForm.getResourceLabel(),cc.xy(col++,1));
            col++;
            builder.addLabel("Interface:",cc.xy(col++,1));
            builder.add(pForm.getInterfaceCombo(),cc.xy(col++,1));
        }
        protected final PanelBuilder builder;

        protected final CellConstraints cc;
	}

	/** extended toolbar which adds an 'execute' button - not static, as more tightly integrated into the taskrunner */
	public class ExecutingTaskRunnerToolbar extends TaskRunnerToolbar implements ListEventListener {
	    public ExecutingTaskRunnerToolbar(final JMenu menu) {
	        super(pForm,newTask);
	        menu.add(executeAction);
	        menu.addSeparator();
	        // function that maps a service to an 'Execute' menu operation */
	        final Function fn = new FunctionList.Function() {
	            public Object evaluate(final Object sourceValue) {
	                return new ExecuteTaskMenuItem((Service)sourceValue);
	            }
	        };
	        new EventListMenuManager( new FunctionList(executionServers,fn),menu, false);
	        execButton = new EventListDropDownButton("Unavailable",IconHelper.loadIcon("run16.png")
	                ,new FunctionList(executionServers, fn),false);
	        execButton.setEnabled(false);
	        execButton.getMainButton().setToolTipText("Execute the task: Press to execute on the first suitable server, or click the arrow to manually choose a server");
	        CSH.setHelpIDString(execButton,"task.execute");
	        executionServers.addListEventListener(this);
	        builder.add(execButton,cc.xy(8,1));     
	    }
	    private final EventListDropDownButton execButton;  
	    private final Action executeAction = new AbstractAction("Execute") {
	        {
	            setEnabled(false);
	            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_E,UIComponentMenuBar.MENU_KEYMASK));
	        }
            public void actionPerformed(final ActionEvent e) {
                execButton.getMainButton().doClick();
            }
	    };
	    
	    /** list of servers that provide this application - should contain Service objects */
	    final EventList executionServers =  new BasicEventList();

	    // enable / disable various bits of the exec button, depending on what is available.
	    public void listChanged(final ListEvent listChanges) {
	        while (listChanges.hasNext()) {
	            listChanges.next();
	            if (executionServers.isEmpty()) {
	                execButton.setEnabled(false);
	                execButton.getMainButton().setText("Unavailable");
	                executeAction.setEnabled(false);
	            }else {
	                execButton.setEnabled(true);
	                execButton.getMainButton().setText("Execute!");
	                executeAction.setEnabled(true);
	            }
	        }
	    }
        /** execute the application
	     * 
	     *  A menu item that is build around a service object.
	     *  */
	     protected final class ExecuteTaskMenuItem extends JMenuItem implements ActionListener {

	         public ExecuteTaskMenuItem(final Service service) {            
	             this.service = service;
	            setIcon(vomon.suggestIconFor(service)); 
	             setText("Execute @  " + service.getTitle());
	             setToolTipText(vomon.getTooltipInformationFor(service));
	             addActionListener(this);
	         }

	        private final Service service;

	         public void actionPerformed(final ActionEvent e) {
	             final Tool tOrig = pForm.getTool();
	             (new BackgroundOperation("Executing @ " + service.getTitle(),BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY) {
	                 @Override
                    protected Object construct() throws ParserConfigurationException, MarshalException, ValidationException, InvalidArgumentException, ServiceException, NotFoundException  {
	                     logger.debug("Executing");
	                     final Document doc = XMLUtils.newDocument();
	                     Marshaller.marshal(tOrig,doc);
	                     reportProgress("Serialized document");
	                     // ok. looks like a goer - lets create a monitor.
	                     final ProcessMonitor monitor = rpmi.create(doc);
	                     reportProgress("Created monitor");
	                     // start tracking it - i.e. display it in the ui.
	                     // we do this early, even before we start it to show some visual progress.
	                     tracker.add(monitor);
	                     
	                     // start it off
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
	                     
	                     rpmi.addMonitor(monitor); // it's running - so now can add it to the rpmi's monitor list.	                     
	                     return null;
	                 } 

	             }).start();
	         }
	     }

	}
	/** worker which lists the services that provide the current application
	 * cea specific
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Aug 2, 200712:43:15 AM
     */
    final class ListServicesWorker extends RetriableBackgroundWorker {

        public ListServicesWorker(final URI appId) {
            super(TaskRunnerImpl.this,"Listing task providers",Thread.MAX_PRIORITY);
            this.appId = appId;
        }


        @Override
        public BackgroundWorker createRetryWorker() {
            return new ListServicesWorker(appId);
        }
        
        private final URI appId;

        @Override
        protected Object construct() throws Exception {
            final Service[] services = apps.listServersProviding(this.appId);
            final int sz = services.length;
            logger.debug("resolved app to " + sz + " servers");

            switch(sz) {
                case 0:
                    return ListUtils.EMPTY_LIST;
                case 1:
                    return Collections.singletonList(services[0]);
                default:
                // more than one provider. Lets do a shuffle to promote a bit 
                // of load balancing (taking into account vomon status too).
                
                // first split into 'up' and 'down'
                    final List up = new ArrayList();
                    final List down = new ArrayList();
                    final List unknown = new ArrayList();
                    for (int i = 0; i < services.length; i++) {
                        final Service service = services[i];
                        final VoMonBean avail = vomon.checkAvailability(service.getId());
                        if (avail == null) {
                            unknown.add(service);
                        } else if  (avail.getCode() == VoMonBean.UP_CODE) {
                            up.add(service);
                        } else {
                            down.add(service);
                        }
                    }
                 // now if we've more than one 'up', shuffle the list.
                    if (up.size() > 1) {
                        Collections.shuffle(up);
                    }
                    // merge back together..
                    if (! unknown.isEmpty()) {
                       up.addAll(unknown);
                    }
                    if (! down.isEmpty()) {
                        up.addAll(down); // tack the services that are 'down' at the end.
                    }
                    return up;
            }
        }

        @Override
        protected void doFinished(final Object result) {
            if (result != null) {
                toolbar.executionServers.addAll((List)result);
            }
        }
    }
    /** choose a new applicatino */
    private final class NewTaskAction extends AbstractAction {
        public NewTaskAction() {
            super("New Task"+ UIComponentMenuBar.ELLIPSIS,IconHelper.loadIcon("clearright16.png"));
            this.putValue(SHORT_DESCRIPTION,"Select a different task to run");
            //this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_N,UIComponentMenuBar.MENU_KEYMASK));
            this.setEnabled(true); 
        }
        public void actionPerformed(final ActionEvent e) {
            clearStorageLocation();
            final Resource[] rs = regChooser.selectResourceXQueryFilterWithParent(
                    "Choose a task to run"
                    ,false // single selection
                    , "//vor:Resource[(not (@status = 'inactive' or @status='deleted')) and ( "
                    + "(@xsi:type &= '*CeaApplication') or "
                    + "( capability/@standardID = '" + TapCapability.CAPABILITY_ID + "')"
                    + ")]"
                    , TaskRunnerImpl.this
                    );

            if (rs.length > 0) {
                final Resource newResource = rs[0]; // only a single selection
                invokeTask(newResource);
            }
        }
    }
	
	// action classes - open, save, clear, chooseApp, execute, close.
	/** open a new tool document */
	private final class OpenAction extends AbstractAction {

        public OpenAction() {
            super("Open"+UIComponentMenuBar.ELLIPSIS,IconHelper.loadIcon("fileopen16.png"));
            this.putValue(SHORT_DESCRIPTION,"Load task document from storage");
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_O,UIComponentMenuBar.MENU_KEYMASK));
        }        

        public void actionPerformed(final ActionEvent e) {

            final URI u = fileChooser.chooseResourceWithParent("Select tool document to load",true,true,true,TaskRunnerImpl.this);
            if (u == null) {
                return;
            }                   
                loadToolDocument(u);
            }
            
    }
	/** reset back to oroginal form contents */
	    private final class ResetAction extends AbstractAction {
	        public ResetAction() {
	            super("Reset Form",IconHelper.loadIcon("filenew16.png"));
	            this.putValue(SHORT_DESCRIPTION,"Reset form");
	            
	            this.setEnabled(true);
	        }
	        public void actionPerformed(final ActionEvent e) {
	            ConfirmDialog.newConfirmDialog(TaskRunnerImpl.this.getComponent(),"Reset Form","Any edits will be lost. Continue?",new Runnable() {
                    public void run() {
                        final CeaApplication res = pForm.getModel().currentResource();
                        if (pForm != null && pForm.getModel() != null) {
                            final String iface = pForm.getModel().getIName();
                            if (iface != null && res != null) {
                                pForm.buildForm(iface,res);
                            }
                        }
                    }
	            }).show();
	        }
	    }
	
/** save the tool document 
	 * */
	 private final class SaveAction extends SaveAsAction {
		 
	        public SaveAction() {
	            super("Save");
	            this.putValue(SHORT_DESCRIPTION,"Save XML document defining task and current parameter values");
	            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_S,UIComponentMenuBar.MENU_KEYMASK));
	        }

	        @Override
            public void actionPerformed(final ActionEvent e) {
	           final URI u = getStorageLocation();
	           if (u != null) {
	               writeToolTo(u);
	           } else {
	               super.actionPerformed(e);
	           }
	        }
	    }
	 
	     /**
         * show the full adql editor.
         * behaviour - when showing an adql-containing interface, should be enabled, otherwsie it should be disabled.
         *              - in an adql-containig interface, should say 'Show' when editor isn't visible and 'Hide' otherwise.
         */
	 protected class ShowHideFullEditorAction extends AbstractAction implements ComponentListener, ItemListener{
        public ShowHideFullEditorAction() {
            super("Show Full Query Editor");
            setEnabled(false);

        }

        public void actionPerformed(final ActionEvent e) {
            pForm.setExpanded(! pForm.getBottomPane().isVisible());
        }

        public void componentHidden(final ComponentEvent e) {
            super.putValue(Action.NAME,"Show Full Query Editor");
        }

        public void componentMoved(final ComponentEvent e) {
            // ignored
        }

        public void componentResized(final ComponentEvent e) {
            // ignored
        }

        public void componentShown(final ComponentEvent e) {
            super.putValue(Action.NAME,"Hide Full Query Editor");                        
        }
        // detects when interface has changed.
        public void itemStateChanged(final ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setEnabled(pForm.getModel().isAdqlInterface());
            }
        }
      
	 }
    private class SaveAsAction extends AbstractAction {
	       public SaveAsAction() {
               super("Save As"+UIComponentMenuBar.ELLIPSIS);
               this.putValue(SHORT_DESCRIPTION,"Save XML document defining task and current parameter values");
               putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_S,UIComponentMenuBar.SHIFT_MENU_KEYMASK));
           }
           protected SaveAsAction(final String s) {
	           super(s);
	       }

           public void actionPerformed(final ActionEvent e) {
               final URI u = fileChooser.chooseResourceWithParent("Choose save location",true,true, true,TaskRunnerImpl.this);
               if (u == null) {
                   return;
               }
               setStorageLocation(u);
               writeToolTo(u);            
           }
        /**
         * @param u
         */
        protected void writeToolTo(final URI u) {
            final Tool t = pForm.getTool();
                   (new BackgroundOperation("Saving as" + u) {

                       @Override
                    protected Object construct() throws Exception {
                           Writer w = null;
                           FileObject fo = null;
                           try {
                               fo = vfs.resolveFile(u.toString());
                               reportProgress("Resolved file");
                            w = new OutputStreamWriter(fo.getContent().getOutputStream());
                            reportProgress("Opened file for writing");
                            // can't monitor this without knowing how large the tool document is..
                            t.marshal(w);                       
                            reportProgress("Wrote file");
                           return null;
                           } finally {
                               if (w != null) {
                                   try {
                                       w.close();
                                       fo.refresh();
                                   } catch (final IOException ignored) {
                                       //meh
                                   }
                               }
                           }
                       }
                       @Override
                    protected void doFinished(final Object result) {
                           parent.showTransientMessage("Saved task document","");
                       }
                   }).start();
        }
       }

}

