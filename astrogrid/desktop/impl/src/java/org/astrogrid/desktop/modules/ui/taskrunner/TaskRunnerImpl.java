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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;
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
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
import                     org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.system.ui.ArMainWindow;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContributionBuilder;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TaskRunnerInternal;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.actions.BuildQueryActivity;
import org.astrogrid.desktop.modules.ui.comp.EventListDropDownButton;
import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.comp.MyTitledBorder;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker;
import org.astrogrid.desktop.modules.votech.VoMonImpl;
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
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Implementation of the task runner.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 200712:30:46 PM
 */
public class TaskRunnerImpl extends UIComponentImpl implements TaskRunnerInternal, MouseListener{
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
                executionServers.addAll((List)result);
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
	                    			}
	                    		}
	                    	}
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
                    } catch (ACRException x) {
                        // catching exceptions here - don't want them to propagate
                        // (and so be reported as a popup dialogue
                        // and these exceptions from the monitor will
                        // be messaged back to the tracker anyhow.
                        logger.warn("ServiceException",x);
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
	            this.setEnabled(true);
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
	private static final Icon PIN_ICON = IconHelper.loadIcon("pin16.gif");
	/** list of servers that provide this application - should contain Service objects */
	private final EventList executionServers =  new BasicEventList();;
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
		
		JMenu executeMenu =new JMenu("Execute");
		fileMenu.add(executeMenu);
	      // function that maps a service to an 'Execute' menu operation */
        FunctionList.Function executionBuilderFunction = new FunctionList.Function() {
            public Object evaluate(Object sourceValue) {
                return new ExecuteTaskMenuItem((Service)sourceValue);
            }
        };
        new EventListMenuManager(new FunctionList(executionServers,executionBuilderFunction),executeMenu, false);

		
		fileMenu.add(new JSeparator());
		fileMenu.add( new CloseAction());		
		menuBar.add(fileMenu);

		JMenu editMenu = new JMenu("Edit");
		editMenu.add(reset);
		editMenu.add(chooseApp);
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
		pForm = builder.createTaskParametersForm(this,this);
		
		// help pane
		infoPane = new ResourceDisplayPane();
		infoPane.setBorder(BorderFactory.createEmptyBorder());
        infoPane.setPreferredSize(new Dimension(130,100));
        
        

		// processes panel
		this.tracker = builder.createExecutionTracker(this);
		trackerPanel = tracker.getPanel();
        trackerPanel.addMouseListener(this);
		trackerPanel.setBackground(Color.WHITE);
		trackerPanel.setBorder(BorderFactory.createEmptyBorder());
		// tasks pane.

		// toolbar
		PanelBuilder toolbar = new PanelBuilder(new FormLayout(
				"2dlu,d,200dlu,10dlu,right:d,d,10dlu:grow,d,2dlu" // cols
				,"d" // rows
				));
		CellConstraints cc = new CellConstraints();
		JButton appButton = new JButton(chooseApp);
		// show icon only.
		appButton.setText(null);
		int col = 2;
		toolbar.add(appButton,cc.xy(col++,1));
		toolbar.add(pForm.getResourceLabel(),cc.xy(col++,1));
		col++;
		toolbar.addLabel("Interface:",cc.xy(col++,1));
		toolbar.add(pForm.getInterfaceCombo(),cc.xy(col++,1));
		col++;
		
		// drop-down 'execute' button
		final EventListDropDownButton execButton = new EventListDropDownButton("Unavailable",IconHelper.loadIcon("run16.png")
                ,new FunctionList(executionServers, executionBuilderFunction)
                ,false);
		execButton.setEnabled(false);
		// enable / disable various bits of the exec button, depending on what is available.
		executionServers.addListEventListener(new ListEventListener() {
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
		});
		toolbar.add(execButton,cc.xy(col++,1));
		
		
		// finish it all off.
		final JScrollPane infoScroll = new JScrollPane(infoPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		infoScroll.setBorder(MyTitledBorder.createLined("Information"));        
		final JScrollPane execScroll = new JScrollPane(trackerPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		execScroll.setBorder(BorderFactory.createEtchedBorder());

		
		final JScrollPane tasksScroll = new JScrollPane(tracker.getTaskPane(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tasksScroll.setBorder(BorderFactory.createEmptyBorder());
		
		bottomFlipPanel = new FlipPanel();
        bottomFlipPanel.add(infoScroll,INFORMATION);
		bottomFlipPanel.add(tasksScroll,TASKS);
		
		final JSplitPane rightPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT
				,execScroll
				,bottomFlipPanel);
		rightPane.setDividerLocation(300);
		rightPane.setDividerSize(7);
		rightPane.setBorder(BorderFactory.createEmptyBorder());
		rightPane.setResizeWeight(0.7);
		rightPane.setPreferredSize(new Dimension(200,600));
		
		// hide info pane when bottom pane appears.
		pForm.getBottomPane().addPropertyChangeListener("collapsed",new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (! pForm.getBottomPane().isCollapsed()) {
                    bottomFlipPanel.setVisible(false);
                    rightPane.setDividerLocation(1.0);
                } else {
                    bottomFlipPanel.setVisible(true);
                    rightPane.setDividerLocation(0.5);
                }               
            }		    
		});
		
		// abit of a hack at the moment - not the nicest way to build a form */
		pForm.add(toolbar.getPanel(),cc.xyw(1,1,5));
		pForm.add(rightPane,cc.xy(5,3));
		JPanel pane = getMainPanel();
		pane.add(pForm,BorderLayout.CENTER);
		this.setContentPane(pane);
		this.setTitle("Task Runner");
		setIconImage(IconHelper.loadIcon("applaunch16.png").getImage());
		logger.info("New TaskRunner - Completed");
	}
	
	//private final DescriptionsPanel descriptions;
	protected final TaskParametersForm pForm;
	private final ResourceDisplayPane infoPane;
    private final JPanel trackerPanel;
    private final FlipPanel bottomFlipPanel;

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
		this.executionServers.clear();
		final URI appId = cea.getId();
		(new ListServicesWorker(appId)).start();
		
		selectStartingInterface(cea);
		display(cea);
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
		display(cea);
	}
	/**
	 * @param cea
	 */
	protected void display(CeaApplication cea) {
		// don't want to display this now.infoPane.display(cea);
		setTitle("Task Runner - " + cea.getTitle());
	}

	public Object create() {
		// deliberately not implemented. will work out how to do this later.
		return null;
	}

	// callback to display help in infoPane.
	public void mouseEntered(MouseEvent e) {
		JComponent comp = (JComponent)e.getSource();
		if (comp == trackerPanel) {
		    bottomFlipPanel.show(TASKS);
		} else {
		    bottomFlipPanel.show(INFORMATION);
		}
		if (! pinnedHelp) {
	      AbstractTaskFormElement t = (AbstractTaskFormElement)comp.getClientProperty(AbstractTaskFormElement.class);
		    displayParameterHelp(t);
		}
		//else {
		    /** dunno if I want to display this -- too cramped.
			Resource resource = pForm.getCurrentResource();
			// show defalt information.
			infoPane.display(resource);
			*/
		//}
	}
	
	private boolean pinnedHelp = false;
	
    /** show help for a parameter.
     * @param comp
     */
    private void displayParameterHelp(AbstractTaskFormElement t) {
  		if (t != null) {
			HtmlBuilder sb = new HtmlBuilder();
			final ParameterBean d = t.getDescription();
            sb.h2(d.getUiName());
			sb.append(d.getDescription()).append("<p>");
	        if (d.getType() != null) {
	            sb.br().append("Type : ").append(d.getType());
	        }
	        if (d.getUcd() != null) {
	            sb.br().append("UCD : ").append(d.getUcd());
	        }
	        if (d.getUnits() != null) {
	            sb.br().append("Units : ").append(d.getUnits());
	        }			
			infoPane.setText(sb.toString());
			infoPane.setCaretPosition(0);
		}
    }
// these mouse events ignored.
	public void mouseClicked(MouseEvent e) {
	    JComponent comp = (JComponent)e.getSource();
        AbstractTaskFormElement t = (AbstractTaskFormElement)comp.getClientProperty(AbstractTaskFormElement.class);
        if (t != null && comp.equals(t.getLabel())) {
            pinnedHelp = !pinnedHelp; // pin if was previously unpinned, and vise-versa
            t.getLabel().setIcon(pinnedHelp ? PIN_ICON : null);
            displayParameterHelp(t);
        }
	}


	
	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}

