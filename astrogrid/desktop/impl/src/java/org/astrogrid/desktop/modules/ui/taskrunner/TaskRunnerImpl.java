/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.CompositeToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.SimplifiedAppLauncherImpl;
import org.astrogrid.desktop.modules.ui.TaskRunnerInternal;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.UIComponentImpl.CloseAction;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.ProcessingInstruction;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.JLinkButton;

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
                        fr = new InputStreamReader(myspace.getInputStream(u));
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
	                    		w = new OutputStreamWriter(myspace.getOutputStream(u));
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
                    if (securityMethod != null) {
                      logger.info("Setting security instruction on the tool document");
                      ProcessingInstruction p 
                          = doc.createProcessingInstruction("CEA-strategy-security", 
                                                            securityMethod);
                      doc.appendChild(p);
                    }
                    Service[] services = apps.listServersProviding(new URI("ivo://" + tOrig.getName()));
                    //@todo use vomon to direct choice - use same technique as for login dialogue.
                    logger.debug("resolved app to " + services.length + " servers");
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
                       return apps.submitTo(doc,chosen);
                    } else {
                    return apps.submitTo(doc,services[0].getId());
                    }
                }
                
                protected void doFinished(Object o) {
                	if (o == null) { // unable to launch the app
                		JOptionPane.showMessageDialog(TaskRunnerImpl.this,"<b>Failed to launch task</b><br>Could not find any servers that provide the task: " + tOrig.getName()
                				,"Cannot launch task",JOptionPane.ERROR_MESSAGE);
                	} else {
                		// not much point constructing this if we're not going to display it...
                		//ResultDialog rd = new ResultDialog(CompositeToolEditorPanel.this,"ExecutionId : " + o);
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
	private static final Log logger = LogFactory.getLog(TaskRunnerImpl.class);
	private final ApplicationsInternal apps;
	private final Action execute;
	private final Action open;
	private final Action save;
	private final Action reset;
	private final Action chooseApp;
	
	private final ResourceChooserInternal fileChooser;
	private final RegistryGoogle regChooser;
	private final MyspaceInternal myspace;
	private final RemoteProcessManager rpm;
	/**
	 * @param context
	 * @throws HeadlessException
	 */
	public TaskRunnerImpl(UIContext context, ApplicationsInternal apps, ResourceChooserInternal rci,RegistryGoogle regChooser,MyspaceInternal myspace, RemoteProcessManager rpm) throws HeadlessException {
		super(context);
		this.fileChooser = rci;
		this.regChooser = regChooser;
		this.myspace = myspace;
		this.rpm = rpm;
		logger.info("Constructing new TaskRunner");
		this.apps = apps;
		
		this.execute = new ExecuteAction();
		this.open = new OpenAction();
		this.save = new SaveAction();
		this.reset = new ResetAction();
		this.chooseApp = new ChooseApplicationAction();
		
		this.setSize(900,600);
		getContext().getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.taskRunner");
		JPanel pane = getMainPanel();
		pane.setBorder(BorderFactory.createEmptyBorder());

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
		
		menuBar.add(getContext().createWindowMenu(this));
		JMenu help = createHelpMenu();
		menuBar.add(help);

		setJMenuBar(menuBar);
				
		// form panel
		pForm = new TaskParametersForm(this,apps,this,fileChooser);
		
		// help pane
		infoPane = new ResourceDisplayPane();
        infoPane.setPreferredSize(new Dimension(130,100));
		infoPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY)
				,"Information"
				,TitledBorder.LEFT
				,TitledBorder.TOP
				,UIConstants.SMALL_DIALOG_FONT
				,Color.GRAY
				));        

		// processes panel
		JPanel processes = new JPanel();

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
		JSplitPane rightPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT
				,processes
				,new JScrollPane(infoPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		rightPane.setDividerLocation(300);
		rightPane.setDividerSize(7);
		rightPane.setBorder(BorderFactory.createEmptyBorder());
		rightPane.setResizeWeight(0.7);
		
		JSplitPane lrPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pForm,rightPane);
		lrPane.setDividerLocation(650);
		lrPane.setResizeWeight(0.9); //most to the left
		lrPane.setBorder(BorderFactory.createEmptyBorder());
		lrPane.setDividerSize(7);
		
		pane.add(toolbar.getPanel(),BorderLayout.NORTH);
		pane.add(lrPane,BorderLayout.CENTER);
		this.setContentPane(pane);
		this.setTitle("Task Runner");
		//@todo find an icon here.
		setIconImage(IconHelper.loadIcon("applaunch16.png").getImage());
		logger.info("New TaskRunner - Completed");
	}
	
	//private final DescriptionsPanel descriptions;
	final TaskParametersForm pForm;
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
				//@todo - should probably do this in a bg thread.
				cea = apps.getCeaApplication(r.getId());
			} catch (ACRException x) {
				logger.error("ServiceException",x);
				//@todo report a fault somewherte..
				return;
			}
		}
		pForm.buildForm(cea);
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
	private void display(CeaApplication cea) {
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
