/*$Id: ApplicationLauncherImpl.java,v 1.1 2005/08/11 10:15:00 nw Exp $
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

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.dialogs.ResourceChooser;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.acr.ui.ApplicationLauncher;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.CommunityInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ParametersPanel;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserDialog;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 12-May-2005
 *
 */
public class ApplicationLauncherImpl extends UIComponent  implements ApplicationLauncher {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ApplicationLauncherImpl.class);

	private JMenuBar menuBar = null;
	private JMenu fileMenu = null;
	private JSplitPane jSplitPane = null;  //  @jve:decl-index=0:visual-constraint="459,126"
	private JComboBox applicationChooser = null;
	private JPanel bottomPane = null;
    private JPanel topPane = null;
    private ApplicationDescription currentApplication;    
    private Interface currentInterface;
    
    private ApplicationDescription getCurrentApplication() {   
        logger.debug("Applilcation " + currentApplication.getName());
        return currentApplication;
    }
    private final static         String vr = "http://www.ivoa.net/xml/VOResource/v0.10";
    private void setCurrentApplication(ApplicationDescription descr) {
        if (descr == null) {
            logger.warn("Attempted to set to null application description - must have been an error in parsing reg entry");
            return;
        }
        logger.debug("Setting current application tp " + descr);
        this.currentApplication = descr;
        // now need to change display
        JEditorPane infoDisplay2 = getInfoDisplay();
        //infoDisplay2.setText(apps.getInfoFor(currentApplication));
        Element e = (Element)currentApplication.getOriginalVODescription().getElementsByTagNameNS(vr,"description").item(0);
        infoDisplay2.setText("<html>" 
                + e == null || e.getFirstChild() == null ? "<i>No Description available</i>" : e.getFirstChild().getNodeValue()
                + "</html>");
        infoDisplay2.setCaretPosition(0);
        
        // change interface list.
        JComboBox interfaceChooser2 = getInterfaceChooser();
        interfaceChooser2.removeAllItems();
        Interface[] interfaces = currentApplication.getInterfaces().get_interface();
        for (int i = 0; i < interfaces.length; i++) {
            interfaceChooser2.addItem(interfaces[i]);
        }        

        setCurrentInterface(null);
        
    }
    
    private void setCurrentInterface(Interface intf) {
        this.currentInterface = intf;
        if (currentInterface == null ) {
            getParametersPane().clear();
            getSaveAction().setEnabled(false);
            executeAction.setEnabled(false);
        } else {
            Tool t = getCurrentApplication().createToolFromInterface(currentInterface);
            getParametersPane().populate(t,getCurrentApplication());
            getSaveAction().setEnabled(true);
            executeAction.setEnabled(true);
        }
    }
    
	
    protected final class SaveAction extends AbstractAction {
        /**
         * Commons Logger for this class
         */
        private final Log logger = LogFactory.getLog(SaveAction.class);

        public SaveAction() {
            super("Save",IconHelper.loadIcon("fileexport.png"));
            this.putValue(SHORT_DESCRIPTION,"Save tool document to local disk");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            final URI u = chooser.chooseResource("Save Tool Document",true);
            if (u == null) {
                return;
            }
                (new BackgroundOperation("Saving tool definition") {

                    protected Object construct() throws Exception {
                        Tool t = getParametersPane().getTool();
                        Writer w = new OutputStreamWriter(myspace.getOutputStream(u));
                        t.marshal(w);
                        return null;
                    }
                }).start();            
            
        }
    }
    
    protected final class ExecuteAction extends AbstractAction {

        public ExecuteAction() {
            super("Execute !", IconHelper.loadIcon("run_tool.gif"));
            this.putValue(SHORT_DESCRIPTION,"Execute this application");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            // @todo HACK going to clone this tool document, rather than passing a reference - as it's getting butchered later on,
            // which means that you can't run the same app twice. easiest to clone the tool document for now - and find out what's going wrong later
            // it's that cursed Ivorn class really.

            
            final Tool tOrig = getParametersPane().getTool();
            (new BackgroundOperation("Executing..") {

                protected Object construct() throws Exception {
                    logger.debug("Executing");
                    Document doc = XMLUtils.newDocument();
                    Marshaller.marshal(tOrig,doc);
                    ResourceInformation[] services = apps.listProvidersOf(new URI("ivo://" + tOrig.getName())); 
                    logger.debug("resolved app to " + services.length + " servers");
                    if (services.length > 1) {
                        URI[] names = new URI[services.length];
                        for (int i = 0 ; i < services.length; i++) {
                            names[i] = services[i].getId();
                        }
                    URI chosen = (URI)JOptionPane.showInputDialog(ApplicationLauncherImpl.this
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
                    ResultDialog rd = new ResultDialog(ApplicationLauncherImpl.this,"ExecutionId : " + o);
                    rd.show();                   
                    monitor.addApplication(tOrig.getName(),(URI)o);
                    monitor.displayApplicationTab();
                    monitor.show();
                }
                
            }).start();
        }
        
    }
    
    protected final class OpenAction extends AbstractAction {
        /**
         * Commons Logger for this class
         */
        private final Log logger = LogFactory.getLog(OpenAction.class);

        public OpenAction() {
            super("Open",IconHelper.loadIcon("file_obj.gif"));
            this.putValue(SHORT_DESCRIPTION,"Load tool document from storage");
        }        

        public void actionPerformed(ActionEvent e) {
            final URI u = chooser.chooseResource("Select tool document to load",true);
            if (u == null) {
                return;
            }                   
                (new BackgroundOperation("Opening tool definition") {
                    private ApplicationDescription newApp;
                    private Interface newInterface;
                    protected Object construct() throws Exception {
                        Reader fr = new InputStreamReader(myspace.getInputStream(u));
                       Tool t = Tool.unmarshalTool(fr);
                       fr.close();
                       newApp = apps.getApplicationDescription(new URI("ivo://" + t.getName()));                       
                       Interface[] candidates = getCurrentApplication().getInterfaces().get_interface();
                       for (int i = 0; i < candidates.length; i++) {
                           if (candidates[i].getName().equalsIgnoreCase(t.getInterface().trim())) {
                               newInterface  = candidates[i];
                           }
                       }
                       if (newInterface == null) {
                           throw new IllegalArgumentException("Cannot find interface " + t.getInterface() + " in registry entry for " + t.getName());
                       }
                       return t;
                       
                    }
                    protected void doFinished(Object o) {
                        setCurrentApplication(newApp);
                        setCurrentInterface(newInterface);
                        getParametersPane().populate((Tool)o,getCurrentApplication());
                    }
                }).start();
            }
            
        
    }
    
    protected final class CloseAction extends AbstractAction {
        /**
         * Commons Logger for this class
         */
        private final Log logger = LogFactory.getLog(CloseAction.class);

        public CloseAction() {
            super("Close",IconHelper.loadIcon("exit.png"));
            this.putValue(SHORT_DESCRIPTION,"Close the Application Launcher");
        }

        public void actionPerformed(ActionEvent e) {
            hide();
            dispose();
        }
    }
        
    private Action executeAction;
    private Action openAction;
    private Action saveAction;

    private Action getExecuteAction() {
        if (executeAction == null) {
            executeAction= new ExecuteAction();
        }
        return executeAction;
    }
    
	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */    
	private JMenuBar getJJMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar() {{
			    add(getFileMenu());
            }};
		}
		return menuBar;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getFileMenu() {
		if (fileMenu == null) {
            openAction = new OpenAction();        
			fileMenu = new JMenu() {{
			    setName("File");
			    setText("File");
			    add(openAction);
			    add(getSaveAction());
			    add(getExecuteAction());
			    add(new JSeparator());
			    add(new CloseAction());
            }};
		}
		return fileMenu;
	}
	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */    
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane(){{
			    setOrientation(JSplitPane.VERTICAL_SPLIT);
                setDividerSize(5);
                setOneTouchExpandable(true);
                setDividerLocation(200);
			    setLeftComponent(getTopPane());
			    setRightComponent(getBottomPane());
            }};
		}
		return jSplitPane;
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JComboBox getApplicationChooser() {
		if (applicationChooser == null) {
			applicationChooser = new JComboBox();     
            applicationChooser.setEditable(false);
            applicationChooser.setRenderer(new ListCellRenderer() {
                JLabel l = new JLabel();
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    //@todo getting null here
                    if (value == null) {
                        l.setText("");
                    } else {
                    ApplicationInformation data = (ApplicationInformation)value;
                    String ui = data.getName() == null ? "not available" : data.getName();
                    String name = data.getId() == null ? "not available" : data.getId().toString();
                    l.setText("<html><b>" + ui+ "</b><br><font size='-1'>" + name + "</font></html>");     
                    }
                    return l;                    
                }
           });  // end cell renderer
            applicationChooser.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        final ApplicationInformation selected = (ApplicationInformation) applicationChooser.getSelectedItem(); 
                        (new BackgroundOperation("Fetching info for " + selected.getName()) {
                            protected Object construct() throws Exception {
                                return apps.getApplicationDescription(selected.getId());                           
                            }
                            protected void doFinished(Object o) {
                                if (o != null) {
                                    setCurrentApplication((ApplicationDescription)o);
                                    getExecuteAction().setEnabled(true);                               
                                    getSaveAction().setEnabled(true);
                                } else {
                                    showError(ApplicationLauncherImpl.this,"Failed to parse registry entry",null);
                                }
                            }
                        }).start();                                           
                    }
                }
            });

            (new BackgroundOperation("Populating application list") {

                protected Object construct() throws Exception {
                    ApplicationInformation[] d = apps.listFully();               
                    Arrays.sort(d,new Comparator() {
                        public int compare(Object o1, Object o2) {
                            ApplicationInformation a = (ApplicationInformation)o1;
                            ApplicationInformation b = (ApplicationInformation)o2;
                            return a.getName().compareToIgnoreCase(b.getName());
                        }
                    });
                    
                    return d;
                }
                protected void doFinished(Object o) {
                    final ApplicationInformation[] data =(ApplicationInformation[]) o;
                    for (int i = 0; i < data.length; i++) {
                        if (data[i] != null) {
                            applicationChooser.addItem(data[i]);
                        }
                    } 
                 //   setCurrentApplication((ApplicationDescription) applicationChooser.getSelectedItem());
                }
            }).start(); // end populate operation.


		}
		return applicationChooser;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private ParametersPanel getParametersPane() {
		if (parametersPane == null) {
			parametersPane = new ParametersPanel(new ResourceChooserDialog(myspace,"Choose External Resource",true,false,true));
		}
		return parametersPane;
	}
    
    private JPanel getTopPane(){
        if (topPane == null) {
            topPane = new JPanel(){{
                setLayout(new BorderLayout());
                add(getApplicationChooser(),BorderLayout.NORTH);
                add(new JScrollPane(getInfoDisplay()),BorderLayout.CENTER);
                add(getInterfaceChooser(),BorderLayout.SOUTH);
            }};
        }
        return topPane;
    }
    
    private JComboBox interfaceChooser;
    private JComboBox getInterfaceChooser() {
        if (interfaceChooser == null) {
            interfaceChooser = new JComboBox();
            interfaceChooser.setEditable(false);
            interfaceChooser.setRenderer(new ListCellRenderer() {
                JLabel l = new JLabel();
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    if (value == null) {
                        l.setText("");
                    } else {
                        Interface intf = (Interface)value;
                        l.setText(intf.getName());
                    }
                    return l ;    
                }
           });  // end cell renderer            
            interfaceChooser.setAction(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    setCurrentInterface((Interface)interfaceChooser.getSelectedItem());                   
                }
            });
        }
        return interfaceChooser;
    }
    
    
    
    private ParametersPanel parametersPane;
    private JPanel getBottomPane() {
        if (bottomPane == null) {
            bottomPane = new JPanel() {{
                setLayout(new BorderLayout());
                add(getSubmitButton(),BorderLayout.SOUTH);
                add(getParametersPane(),BorderLayout.CENTER);
            }};
        }
        return bottomPane;
    }
    private JButton submitButton;
    
    private JButton getSubmitButton(){
        if (submitButton == null) {
            submitButton = new JButton() {{
                setAction(getExecuteAction());
            }};
        }
        return submitButton;
    }
    
    private JEditorPane infoDisplay;
    private JEditorPane getInfoDisplay() {
        if (infoDisplay == null) {
            infoDisplay = new JEditorPane() {{
                setEditable(false);
                setContentType("text/html");
            }};
        }
        return infoDisplay;
    }

          public static void main(String[] args) {
             (new ApplicationLauncherImpl()).show();
    }
	/**
	 * This is the default constructor
	 */
	public ApplicationLauncherImpl() {
        super();
        this.apps = null;
        this.monitor =null;
        this.myspace = null;
        this.chooser=null;
		initialize();
	}
    
    public ApplicationLauncherImpl(ApplicationsInternal apps,JobMonitor monitor, MyspaceInternal myspace, HelpServer hs, Configuration conf, UIInternal ui,ResourceChooser chooser) {
        super(conf,hs,ui);
        this.apps = apps;
        this.monitor = monitor;
        this.myspace =myspace;
        this.chooser= chooser;
        initialize();
    }
    
    private final ApplicationsInternal apps;
    private final JobMonitor monitor;
    private final MyspaceInternal myspace;
    private final ResourceChooser chooser;
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setJMenuBar(getJJMenuBar());
		this.setSize(500,600);
		JPanel pane  = getJContentPane();
        pane.add(getJSplitPane(),BorderLayout.CENTER);
        this.setContentPane(pane);
        this.setTitle("Application Launcher");
	}

    private Action getSaveAction() {
        if (saveAction == null) {
            saveAction = new SaveAction();
        }
        return saveAction;
    }

}

/* 
$Log: ApplicationLauncherImpl.java,v $
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