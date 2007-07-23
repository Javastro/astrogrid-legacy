/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
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

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.ArMainWindow;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContributionBuilder;
import org.astrogrid.desktop.modules.ui.TaskRunnerInternal;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.actions.BuildQueryActivity;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Implementation of the task runner.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 200712:30:46 PM
 */
public class TaskRunnerImpl extends UIComponentImpl implements TaskRunnerInternal, MouseListener{
	// action classes - open, save, clear, chooseApp, execute, close.
	/** open a new tool document */
	protected final class OpenAction extends AbstractAction {

        public OpenAction() {
            super("Open",IconHelper.loadIcon("fileopen16.png"));
            this.putValue(SHORT_DESCRIPTION,"Load task document from storage");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
        }        

        public void actionPerformed(ActionEvent e) {

            final URI u = fileChooser.chooseResourceWithParent("Select tool document to load",true,true, true,TaskRunnerImpl.this);
            if (u == null) {
                return;
            }                   
                (new BackgroundOperation("Opening task document") {
                    private CeaApplication newApp;
                    private InterfaceBean newInterface;
                    protected Object construct() throws Exception {
                    	Reader fr = null;
                    	try {
                        fr = new InputStreamReader(u.toURL().openStream());
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
	                    		//@todo refactor myspace away.
	                    		w = new OutputStreamWriter(u.toURL().openConnection().getOutputStream());
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
	/** execute the application */
	  protected final class ExecuteAction extends AbstractAction {

	        public ExecuteAction() {
	            super("Execute !", IconHelper.loadIcon("run16.png"));
	            this.putValue(SHORT_DESCRIPTION,"Execute this application");
	            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
	        }

			public void actionPerformed(ActionEvent e) {
				final Tool tOrig = pForm.getTool();
            final String securityMethod = null; //@todo getToolModel().getSecurityMethod();
            logger.info("Security method = " + securityMethod);
            (new BackgroundOperation("Executing..") {

                protected Object construct() throws Exception {
                    logger.debug("Executing");
                    Document doc = XMLUtils.newDocument();
                    Marshaller.marshal(tOrig,doc);
                    /*if (securityMethod != null) {
                      logger.info("Setting security instruction on the tool document");
                      ProcessingInstruction p 
                          = doc.createProcessingInstruction("CEA-strategy-security", 
                                                            securityMethod);
                      doc.appendChild(p);
                    }*/
                    
                    Service[] services = apps.listServersProviding(new URI("ivo://" + tOrig.getName()));
                    //@todo use vomon to direct choice - use same technique as for login dialogue.
                    logger.debug("resolved app to " + services.length + " servers");
                    URI execId;
                    if (services.length == 0) {// no providing servers found.
                    	return null;
                    } else if (services.length > 1) {
                        URI[] names = new URI[services.length];
                        for (int i = 0 ; i < services.length; i++) {
                            names[i] = services[i].getId(); //@todo should be titile here really.
                        }
                    URI chosen = (URI)JOptionPane.showInputDialog(TaskRunnerImpl.this
                            ,"More than one CEA server provides this application - please choose one"
                            ,"Choose Server"
                            ,JOptionPane.QUESTION_MESSAGE
                            ,null
                            , names
                            ,names[0]);
                    	execId = apps.submitTo(doc,chosen);
                    } else {
                    	execId  =  apps.submitTo(doc,services[0].getId());
                    }
                    return execId;
                }
                
                protected void doFinished(Object o) {
                	if (o == null) { // unable to launch the app
                		JOptionPane.showMessageDialog(TaskRunnerImpl.this,"<b>Failed to launch task</b><br>Could not find any servers that provide the task: " + tOrig.getName()
                				,"Cannot launch task",JOptionPane.ERROR_MESSAGE);
                	} else {
                		tracker.add((URI)o);
                	}
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
	        	//@fixme requires the registryGoogle interface to be reimplemented
	        	// with some finer control.
	        	regChooser.selectResources("Choose someting for now",true);
	        }
	    }	    
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory.getLog(TaskRunnerImpl.class);
	private final Action execute;
	private final Action open;
	private final Action save;
	private final Action reset;
	private final Action chooseApp;
	
	protected final ApplicationsInternal apps;
	private final ResourceChooserInternal fileChooser;
	private final RegistryGoogle regChooser;
	private final ExecutionTracker tracker;
	private final TypesafeObjectBuilder builder;

	/**
	 * @param context
	 * @throws HeadlessException
	 */
	public TaskRunnerImpl(UIContext context, ApplicationsInternal apps,ResourceChooserInternal rci,RegistryGoogle regChooser,ExecutionTracker tracker, UIContributionBuilder menuBuilder, TypesafeObjectBuilder builder) throws HeadlessException {
	   
		super(context);
		this.fileChooser = rci;
        this.builder = builder;
		this.regChooser = regChooser;
		logger.info("Constructing new TaskRunner");
		this.apps = apps;
		
		this.execute = new ExecuteAction();
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
		fileMenu.add(execute);
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
		this.tracker = tracker;
		JPanel processes = tracker.getPanel();
		processes.setBackground(Color.WHITE);
		processes.setBorder(BorderFactory.createEmptyBorder());

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
		final JButton execButton = new JButton(execute);
		toolbar.add(execButton,cc.xy(col++,1));
		
		
		// finish it all off.
		final JScrollPane infoScroll = new JScrollPane(infoPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		infoScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY)
                ,"Information"
                ,TitledBorder.LEFT
                ,TitledBorder.TOP
                ,UIConstants.SMALL_DIALOG_FONT
                ,Color.GRAY
                ));        
		final JScrollPane execScroll = new JScrollPane(processes,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		execScroll.setBorder(BorderFactory.createEtchedBorder());
		
		final JSplitPane rightPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT
				,execScroll
				,infoScroll);
		rightPane.setDividerLocation(300);
		rightPane.setDividerSize(7);
		rightPane.setBorder(BorderFactory.createEmptyBorder());
		rightPane.setResizeWeight(0.7);
		rightPane.setPreferredSize(new Dimension(200,600));
		
		// hide info pane when bottom pane appears.
		pForm.getBottomPane().addPropertyChangeListener("collapsed",new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (! pForm.getBottomPane().isCollapsed()) {
                    infoScroll.setVisible(false);
                    rightPane.setDividerLocation(1.0);
                } else {
                    infoScroll.setVisible(true);
                    rightPane.setDividerLocation(0.5);
                }               
            }		    
		});
		
		// abit of a hack at the moment - not the nicest way to build a form */
		pForm.add(toolbar.getPanel(),cc.xyw(1,1,5));
		//@todo add in an execution button bar here?
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
			    System.err.println("finding app");
				cea = apps.getCeaApplication(r.getId());
			} catch (ACRException x) {
				logger.error("ServiceException",x);
				//@todo report a fault somewherte..
				return;
			}
		}
		
		String name = BuildQueryActivity.findNameOfFirstNonADQLInterface(cea);
		if (name != null) {
		    pForm.buildForm(name,cea);
		} else { // show what we've got then
		    pForm.buildForm(cea);
		}
		display(cea);
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
		infoPane.display(cea);
		setTitle("Task Runner - " + cea.getTitle());
	}

	public Object create() {
		// deliberately not implemented. will work out how to do this later.
		return null;
	}

	// callback to display help in infoPane.
	public void mouseEntered(MouseEvent e) {
		JComponent comp = (JComponent)e.getSource();
		AbstractTaskFormElement t = (AbstractTaskFormElement)comp.getClientProperty(AbstractTaskFormElement.class);
		if (t != null) {
			HtmlBuilder sb = new HtmlBuilder();
			sb.append("<html><body>");
			sb.h2(t.getDescription().getUiName());
			sb.append(t.getDescription().getDescription());
			sb.append("</body></html>");
			infoPane.setText(sb.toString());
			infoPane.setCaretPosition(0);
		} else {
			Resource resource = pForm.getCurrentResource();
			// show defalt information.
			infoPane.display(resource);
		}
	}
// these mouse events ignored.
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}

