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
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.ui.ArMainWindow;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContributionBuilder;
import org.astrogrid.desktop.modules.ui.TaskRunnerInternal;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.actions.BuildQueryActivity;
import org.astrogrid.desktop.modules.ui.comp.EventListDropDownButton;
import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker.ShowDetailsEvent;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker.ShowDetailsListener;
import org.astrogrid.desktop.modules.votech.VoMonInternal;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.votech.VoMonBean;
import org.w3c.dom.Document;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
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
	/** worker which lists the services that provide the current application
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Aug 2, 200712:43:15 AM
     */
    private final class ListServicesWorker extends BackgroundOperation {

        private final URI appId;

        private ListServicesWorker(URI appId) {
            super("Listing task providers");
            this.appId = appId;
        }

        protected Object construct() throws Exception {
            Service[] services = apps.listServersProviding(this.appId);
            final int sz = services.length;
            logger.debug("resolved app to " + sz + " servers");
            List l = Arrays.asList(services);
            switch(sz) {
                case 0:
                    return ListUtils.EMPTY_LIST;
                case 1:
                    return Collections.singletonList(services[0]);
                default:
                // more than one provider. Lets do a shuffle to promote a bit 
                // of load balancing (taking into account vomon status too).
                
                // first split into 'up' and 'down'
                    List up = new ArrayList();
                    List down = new ArrayList();
                    List unknown = new ArrayList();
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

        protected void doFinished(Object result) {
            if (result != null) {
                toolbar.executionServers.addAll((List)result);
            }
        }
    }
    // action classes - open, save, clear, chooseApp, execute, close.
	/** open a new tool document */
	protected final class OpenAction extends AbstractAction {

        public OpenAction() {
            super("Open",IconHelper.loadIcon("fileopen16.png"));
            this.putValue(SHORT_DESCRIPTION,"Load task document from storage");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
        }        

        public void actionPerformed(ActionEvent e) {

            final URI u = fileChooser.chooseResourceWithParent("Select tool document to load",true,true,true,TaskRunnerImpl.this);
            if (u == null) {
                return;
            }                   
                (new BackgroundOperation("Opening task document") {
                    private CeaApplication newApp;
                    private InterfaceBean newInterface;
                    protected Object construct() throws Exception {
                    	Reader fr = null;
                    	try {
                        fr = new InputStreamReader(vfs.resolveFile(u.toString()).getContent().getInputStream());
                       Tool t = Tool.unmarshalTool(fr);
                       
                       newApp = apps.getCeaApplication(new URI("ivo://" + t.getName()));                       
                       InterfaceBean[] candidates = newApp.getInterfaces();
                       for (int i = 0; i < candidates.length; i++) {
                           if (candidates[i].getName().equalsIgnoreCase(t.getInterface().trim())) {
                               newInterface  = candidates[i];
                           }
                       }
                       if (newInterface == null) {
                           throw new IllegalArgumentException("Cannot find interface " + t.getInterface() + " in registry entry for " + t.getName());
                       }
                       return t;
                    	} finally {
                    		if (fr != null) {
                    			try {
                    				fr.close();
                    			} catch (IOException ignored) {
                    			    //meh
                    			}
                    		}
                    	}
                    }
                    protected void doFinished(Object o) {
                    	buildForm((Tool)o,newInterface.getName(),newApp);
                    }
                }).start();
            }
            
    }	
	/** save the tool document */
	 protected final class SaveAction extends AbstractAction {
		 
	        public SaveAction() {
	            super("Save",IconHelper.loadIcon("filesave16.png"));
	            this.putValue(SHORT_DESCRIPTION,"Save task document");
	            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
	        }

	        public void actionPerformed(ActionEvent e) {
	            final URI u = fileChooser.chooseResourceWithParent("Save Task Document",true,true, true,TaskRunnerImpl.this);
	            if (u == null) {
	                return;
	            }
	            final Tool t = pForm.getTool();
	                (new BackgroundOperation("Saving document") {

	                    protected Object construct() throws Exception {
	                    	Writer w = null;
	                    	try {
	                    	    w = new OutputStreamWriter(vfs.resolveFile(u.toString()).getContent().getOutputStream());
	                    		// can't output using this.	                    		w = new OutputStreamWriter(u.toURL().openConnection().getOutputStream());
	                        t.marshal(w);                       
	                        return null;
	                    	} finally {
	                    		if (w != null) {
	                    			try {
	                    				w.close();
	                    			} catch (IOException ignored) {
	                    			    //meh
	                    			}
	                    		}
	                    	}
	                    }
	                    protected void doFinished(Object result) {
	                        parent.showTransientMessage("Saved task document","");
	                    }
	                }).start();            
	        }
	    }	

	 
	/** execute the application
	 * 
	 *  A menu item that is build around a service object.
	 *  */
	 protected final class ExecuteTaskMenuItem extends JMenuItem implements ActionListener {

	     private final Service service;

        public ExecuteTaskMenuItem(Service service) {	         
	         this.service = service;
            setIcon(vomon.suggestIconFor(service)); 
	         setText("Execute @  " + service.getTitle());
	         setToolTipText(vomon.getTooltipInformationFor(service));
	         addActionListener(this);
	     }

	     public void actionPerformed(ActionEvent e) {
	         final Tool tOrig = pForm.getTool();
	         (new BackgroundOperation("Executing @ " + service.getTitle()) {
	             protected Object construct() throws ParserConfigurationException, MarshalException, ValidationException, InvalidArgumentException, ServiceException, NotFoundException  {
	                 logger.debug("Executing");
	                 Document doc = XMLUtils.newDocument();
	                 Marshaller.marshal(tOrig,doc);
	                 // ok. looks like a goer - lets create a monitor.
	                 logger.debug("Created monitor");
	                 ProcessMonitor monitor = rpmi.create(doc);
	                 // start tracking it - i.e. display it in the ui.
	                 // we do this early, even before we start it to show some visual progress.
	                 tracker.add(monitor);
	                 
	                 // start it off
                     try {
                        monitor.start(service.getId());
                    } catch (final ACRException x) {
                        // catching exceptions here - don't want them to propagate
                        // (and so be reported as a popup dialogue
                        // and these exceptions from the monitor will
                        // be messaged back to the tracker anyhow.
                        logger.warn("ServiceException",x);
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                parent.showTransientError("Unable to execute task",ExceptionFormatter.formatException(x));
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
	  
	  /** reset back to oroginal form contents */
	    protected final class ResetAction extends AbstractAction {
	        public ResetAction() {
	            super("Reset",IconHelper.loadIcon("filenew16.png"));
	            this.putValue(SHORT_DESCRIPTION,"Reset form");
	            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
	            this.setEnabled(true);
	        }
	        public void actionPerformed(ActionEvent e) {
	        	// achieve this by 
	        	JComboBox ic = pForm.getInterfaceCombo();
	        	int ix = ic.getSelectedIndex();
	        	ic.setSelectedIndex(-1);
	        	ic.setSelectedIndex(ix);
	        }
	    }
	    
	    /** choose a new applicatino */
	    protected final class ChooseApplicationAction extends AbstractAction {
	        public ChooseApplicationAction() {
	            super("Change task...",IconHelper.loadIcon("clearright16.png"));
	            this.putValue(SHORT_DESCRIPTION,"Select a different task to run");
	            //this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
	            this.setEnabled(false); // as not implemented at present.
	        }
	        public void actionPerformed(ActionEvent e) {
	            JOptionPane.showMessageDialog(TaskRunnerImpl.this,"not implemented");
	            
	        	//@fixme requires the registryGoogle interface to be reimplemented
	        	// with some finer control.
	        	//regChooser.selectResources("Choose someting for now",true);
	        }
	    }	    
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory.getLog(TaskRunnerImpl.class);
    private static final String INFORMATION = "info";
    private static final String TASKS = "tasks";
	private final Action open;
	private final Action save;
	private final Action reset;
	private final Action chooseApp;


	protected final ApplicationsInternal apps;
	private final ResourceChooserInternal fileChooser;
	private final RegistryGoogle regChooser;
	private final ExecutionTracker tracker;
	private final TypesafeObjectBuilder builder;
    private final FileSystemManager vfs;
    private final RemoteProcessManagerInternal rpmi;
    private final VoMonInternal vomon;

	/**
	 * @param context
	 * @throws HeadlessException
	 */
	public TaskRunnerImpl(UIContext context, ApplicationsInternal apps,RemoteProcessManagerInternal rpmi,ResourceChooserInternal rci,RegistryGoogle regChooser,UIContributionBuilder menuBuilder, TypesafeObjectBuilder builder, FileSystemManager vfs, VoMonInternal vomon) throws HeadlessException {
		super(context);
        this.rpmi = rpmi;
		this.fileChooser = rci;
        this.builder = builder;
		this.regChooser = regChooser;
        this.vfs = vfs;
        this.vomon = vomon;
		logger.info("Constructing new TaskRunner");
		this.apps = apps;
		
		this.open = new OpenAction();
		this.save = new SaveAction(); 
		this.reset = new ResetAction();
		this.chooseApp = new ChooseApplicationAction();
		
		this.setSize(900,600);
		getContext().getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.taskRunner");
		
		// menu bar
		JMenuBar menuBar =new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.add(new JSeparator());

		// execution related stuff.
		JMenu executeMenu =new JMenu("Execute");
		fileMenu.add(executeMenu);
		
		fileMenu.add(new JSeparator());
		fileMenu.add( new CloseAction());		
		menuBar.add(fileMenu);

		editMenu = new JMenu("Edit");
        editMenu.add(reset);
		editMenu.add(chooseApp);
		editMenu.addSeparator(); // context-sensitive stuff comes after this.
		menuBar.add(editMenu);
		
        menuBuilder.populateWidget(menuBar,this,ArMainWindow.MENUBAR_NAME);
            int sz = menuBar.getComponentCount();
            
            JMenu help = menuBar.getMenu(sz-1);
            help.insertSeparator(0);
            JMenuItem sci = new JMenuItem("Task / Query Runner: Introduction");
            getContext().getHelpServer().enableHelpOnButton(sci,"runner.intro");
            help.insert(sci,0);         		
		
		menuBar.add(getContext().createWindowMenu(this),sz-1); // insert before help menu.

		setJMenuBar(menuBar);
				
		// form panel
		pForm = builder.createTaskParametersForm(this);
        
		// processes panel
		this.tracker = builder.createExecutionTracker(this);
		tracker.addShowDetailsListener(this);
		trackerPanel = tracker.getPanel();
		trackerPanel.setBackground(Color.WHITE);
		trackerPanel.setBorder(BorderFactory.createEmptyBorder());
		// tasks pane.

		// toolbar
		toolbar = new ExecutingTaskRunnerToolbar(pForm,chooseApp,executeMenu);
	
		// finish it all off.
 
		final JScrollPane execScroll = new JScrollPane(trackerPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		execScroll.setBorder(BorderFactory.createEtchedBorder());

		
		final JScrollPane tasksScroll = new JScrollPane(tracker.getTaskPane(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tasksScroll.setBorder(BorderFactory.createEmptyBorder());

		
		final JSplitPane rightPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT
				,execScroll
				,tasksScroll);
		rightPane.setDividerLocation(250);
		rightPane.setDividerSize(7);
		rightPane.setBorder(BorderFactory.createEmptyBorder());
		rightPane.setResizeWeight(0.7);
		rightPane.setPreferredSize(new Dimension(200,600));
		pForm.getBottomPane().addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                tasksScroll.setVisible(false);
                rightPane.setDividerLocation(1.0);
                rightPane.setDividerSize(0);
            }
            
            public void componentHidden(ComponentEvent e) {
                    tasksScroll.setVisible(true);
                    rightPane.setDividerLocation(0.5);     
                    rightPane.setDividerSize(7);                    
            }
		});
		pForm.setToolbar(toolbar);
		pForm.setRightPane("Execute",rightPane); 
		//
		JPanel pane = getMainPanel();
		pane.add(pForm,BorderLayout.CENTER);
		this.setTitle("Task Runner");
		setIconImage(IconHelper.loadIcon("applaunch16.png").getImage());
		logger.info("New TaskRunner - Completed");
	}
	
	
	/** simple toolbar for taskrunner */;
	public static class TaskRunnerToolbar extends JPanel {
	    protected final PanelBuilder builder;
        protected final CellConstraints cc;

        /**
         * 
         */
        public TaskRunnerToolbar(TaskParametersForm pForm, Action chooseAppAction) {
            builder = new PanelBuilder(new FormLayout(
                    "2dlu,d,200dlu,10dlu,right:d,d,10dlu:grow,d,2dlu" // cols
                    ,"d" // rows
                    ),this);
            cc = new CellConstraints();
            JButton appButton = new JButton(chooseAppAction);
            // show icon only.
            appButton.setText(null);
            //FIXME - temporarily set to invisible, as not implemented.
            appButton.setVisible(chooseAppAction.isEnabled());
            int col = 2;
            builder.add(appButton,cc.xy(col++,1));
            builder.add(pForm.getResourceLabel(),cc.xy(col++,1));
            col++;
            builder.addLabel("Interface:",cc.xy(col++,1));
            builder.add(pForm.getInterfaceCombo(),cc.xy(col++,1));
        }
	}
	/** extended toolbar which adds an 'execute' button - not static, as more tightly integrated into the taskrunner */
	public class ExecutingTaskRunnerToolbar extends TaskRunnerToolbar implements ListEventListener {
	    /** list of servers that provide this application - should contain Service objects */
	    private final EventList executionServers =  new BasicEventList();
	    /** function that creates a menu item from a service */
	    private final FunctionList.Function executionBuilderFunction = new FunctionList.Function() {
	        public Object evaluate(Object sourceValue) {
	            return new ExecuteTaskMenuItem((Service)sourceValue);
	        }
	    };
	    
	    private final EventListDropDownButton execButton;
	    
	    public ExecutingTaskRunnerToolbar(TaskParametersForm pform, Action chooseAppAction, JMenu executeMenu) {
	        super(pform,chooseAppAction);
	        // function that maps a service to an 'Execute' menu operation */
	        new EventListMenuManager(new FunctionList(executionServers,executionBuilderFunction),executeMenu, false);
	        execButton = new EventListDropDownButton("Unavailable",IconHelper.loadIcon("run16.png")
	                ,new FunctionList(executionServers, executionBuilderFunction)
	                ,false);
	        execButton.setEnabled(false);
	        executionServers.addListEventListener(this);
	        builder.add(execButton,cc.xy(8,1));     
	    }
	    // enable / disable various bits of the exec button, depending on what is available.
	    public void listChanged(ListEvent listChanges) {
	        while (listChanges.hasNext()) {
	            listChanges.next();
	            if (executionServers.isEmpty()) {
	                execButton.setEnabled(false);
	                execButton.getMainButton().setText("Unavailable");
	            }else {
	                execButton.setEnabled(true);
	                execButton.getMainButton().setText("Execute!");                                  
	            }
	        }
	    }

	}
	
	//private final DescriptionsPanel descriptions;
	protected final TaskParametersForm pForm;
    private final JPanel trackerPanel;
    private final JMenu editMenu;
    private ExecutingTaskRunnerToolbar toolbar;

	public void invokeTask(Resource r) {
		buildForm(r);
	}
	public void buildForm(Resource r) {
		CeaApplication cea = null;
		if (r instanceof CeaApplication) {
			cea = (CeaApplication)r;
		} else {
			// try to find a 'synthetic' cea application.
			try {
				//@@todo causes a long pause - do this on bg threa somehow.
			    // however, only happens for non cea-apps. (cone, etc)
				cea = apps.getCeaApplication(r.getId());
			} catch (ACRException x) {
				logger.error("ServiceException",x);
				//should report an error somewhere.
				return;
			}
		}
		
		// start off a background process to find a list of servers that execute this app.
		toolbar.executionServers.clear();
		final URI appId = cea.getId();
		(new ListServicesWorker(appId)).start();
		
		selectStartingInterface(cea);
		setTitle("Task Runner - " + cea.getTitle());
	}
    /**
     * @param cea
     */
    protected void selectStartingInterface(CeaApplication cea) {
        // now that's working in the background, work out what we should be building a form for.
		String name = BuildQueryActivity.findNameOfFirstNonADQLInterface(cea);
		if (name != null) {
		    pForm.buildForm(name,cea);
		} else { // show what we've got then
		    pForm.buildForm(cea);
		}
    }
	
	public void buildForm(Tool t,String interfaceName,Resource r) {
		CeaApplication cea = null;
		if (r instanceof CeaApplication) {
			cea = (CeaApplication)r;
		} else {
			// try to find a 'synthetic' cea application.
			try {
				//@todo - should probably do this in a bg thread.
				cea = apps.getCeaApplication(r.getId());
			} catch (ACRException x) {
				logger.error("ServiceException",x);
				//@todo report a fault somewherte..
				return;
			}
		}
		pForm.buildForm(t,interfaceName,cea);
		setTitle("Task Runner - " + cea.getTitle());
	}
	public Object create() {
		// deliberately not implemented. will work out how to do this later.
		return null;
	}
	
// show details event listener - a callback from the task monitor.	
    public void showDetails(ShowDetailsEvent e) {
        ProcessMonitor monitor = e.getMoitor();
        if (monitor instanceof ProcessMonitor.Advanced) {
            Tool tool = ((ProcessMonitor.Advanced)monitor).getInvocationTool();
            // now need to take a copy of this tool, and load this copy into the editor.
            //can't use original, otherwise edits to the form will alter the original - rewriting history.
            // copy the tool object by marshalling to and from xml.
            Writer sw = null;
            Reader r = null;                  
            try {
                sw = new StringWriter();
                tool.marshal(sw);
                r = new StringReader(sw.toString());
                tool = Tool.unmarshalTool(r);
                CeaApplication newApp =pForm.getModel().currentResource();
                buildForm(tool,tool.getInterface(),newApp);
            } catch (CastorException x) {
                logger.error("MarshalException",x);
           } finally {
                if (sw != null) {
                    try {
                        sw.close();
                    } catch (IOException x) {
                        // ignored
                    }
                }
                if (r != null) {
                    try {
                        r.close();
                    } catch (IOException x) {
                        //ignored
                    }
                }
            }
        }
    }
    public JMenu getContextMenu() {
        return editMenu;
    }
}

