/*$Id: CompositeToolEditorPanel.java,v 1.6 2005/11/10 16:28:26 nw Exp $
 * Created on 08-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.dialogs.ResourceChooser;
import org.astrogrid.acr.ivoa.Adql074;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.ui.ApplicationLauncherImpl;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.Lookout;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.w3c.dom.Document;

//import sun.security.krb5.internal.p;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

/** Tool Editor Panel that composites together a bunch of other ones, and determines which
 * to show.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2005
 *
 */
public class CompositeToolEditorPanel extends AbstractToolEditorPanel {
    
    protected final class ExecuteAction extends AbstractAction {

        public ExecuteAction() {
            super("Execute !", IconHelper.loadIcon("run_tool.gif"));
            this.putValue(SHORT_DESCRIPTION,"Execute this application");
            this.setEnabled(toolModel.getTool() != null);
            toolModel.addToolEditListener(new ToolEditAdapter() {
                public void toolSet(ToolEditEvent te) {
                    setEnabled(toolModel.getTool() != null);
                }
                public void toolCleared(ToolEditEvent te) {
                    setEnabled(false);
                }                
            });
        }

        public void actionPerformed(ActionEvent e) {
            
            final Tool tOrig = getToolModel().getTool();
            (new BackgroundWorker(parent,"Executing..") {

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
                    URI chosen = (URI)JOptionPane.showInputDialog(CompositeToolEditorPanel.this
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
                    ResultDialog rd = new ResultDialog(CompositeToolEditorPanel.this,"ExecutionId : " + o);
                    //rd.show();                   
                    lookout.show();
                }
                
            }).start();
        }
        
    }
    
    protected final class NewAction extends AbstractAction {
        public NewAction() {
            super("New",IconHelper.loadIcon("newfile_wiz.gif"));
            this.putValue(SHORT_DESCRIPTION,"Create a new tool");
            this.setEnabled(true);
        }
        public void actionPerformed(ActionEvent e) {
            int code = JOptionPane.showConfirmDialog(CompositeToolEditorPanel.this,"Discard current tool?","Are you sure?",JOptionPane.OK_CANCEL_OPTION);
            if (code == JOptionPane.OK_OPTION) {
                getToolModel().clear();
            }
        }
    }
    protected final class OpenAction extends AbstractAction {

        public OpenAction() {
            super("Open",IconHelper.loadIcon("file_obj.gif"));
            this.putValue(SHORT_DESCRIPTION,"Load tool document from storage");
        }        

        public void actionPerformed(ActionEvent e) {
            final URI u = chooser.chooseResourceWithParent("Select tool document to load",true,true, true,CompositeToolEditorPanel.this);
            if (u == null) {
                return;
            }                   
                (new BackgroundWorker(parent,"Opening tool definition") {
                    private ApplicationInformation newApp;
                    private InterfaceBean newInterface;
                    protected Object construct() throws Exception {
                        Reader fr = new InputStreamReader(myspace.getInputStream(u));
                       Tool t = Tool.unmarshalTool(fr);
                       fr.close();
                       newApp = apps.getApplicationInformation(new URI("ivo://" + t.getName()));                       
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
                       
                    }
                    protected void doFinished(Object o) {
                        getToolModel().populate((Tool)o,newApp);
                    }
                }).start();
            }
            
        
    }

 
    protected final class SaveAction extends AbstractAction {
 
        public SaveAction() {
            super("Save",IconHelper.loadIcon("fileexport.png"));
            this.putValue(SHORT_DESCRIPTION,"Save tool document");
            this.setEnabled(toolModel.getTool() != null);
            toolModel.addToolEditListener(new ToolEditAdapter() {
                public void toolSet(ToolEditEvent te) {
                    setEnabled(toolModel.getTool() != null);
                }
                public void toolCleared(ToolEditEvent te) {
                    setEnabled(false);
                }                
            });
        }

        public void actionPerformed(ActionEvent e) {
            final URI u = chooser.chooseResourceWithParent("Save Tool Document",true,true, true,CompositeToolEditorPanel.this);
            if (u == null) {
                return;
            }
                (new BackgroundWorker(parent,"Saving tool definition") {

                    protected Object construct() throws Exception {
                        Tool t = getToolModel().getTool();
                        Writer w = new OutputStreamWriter(myspace.getOutputStream(u));
                        t.marshal(w);
                        w.close();
                        return null;
                    }
                }).start();            
            
        }
    }
  
    
    /** the controller -composites together the various editors, 
     *  listens to tool change events, and enables / disables components depending on theiir applicability */
    private class Controller extends ToolEditAdapter {
  

        public Controller() {
            // setup the state.
            enableApplicable(toolModel.getTool(),toolModel.getInfo());
        }

        public void toolChanged(ToolEditEvent te) {
            enableApplicable(toolModel.getTool(),toolModel.getInfo());
        }
        public void toolCleared(ToolEditEvent te) {
            enableApplicable(null,null);
        }        
        
        public void toolSet(ToolEditEvent te) {
            enableApplicable(toolModel.getTool(), toolModel.getInfo());
        }
               
        
        private void enableApplicable(Tool t, ApplicationInformation info) {
                    int firstApplicable = -1;
                    for (int i = 0; i < views.length; i++ ) {
                        int pos = tabPane.indexOfComponent(views[i]);

                        if (views[i].isApplicable(t, info)) {
                            tabPane.setEnabledAt(pos,true);
                            if (firstApplicable == -1) {
                                firstApplicable = pos;
                            }                    
                        } else {
                            tabPane.setEnabledAt(pos,false);
                        }
                    }
                    tabPane.setSelectedIndex(firstApplicable);
                }                   
    }
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CompositeToolEditorPanel.class);
    protected final ApplicationsInternal apps;
    protected final ResourceChooserInternal chooser;
    protected final MyspaceInternal myspace;
    protected final Lookout lookout;
   protected final UIComponent parent;
    protected final  JTabbedPane tabPane;
    protected final AbstractToolEditorPanel[] views;
   

    public CompositeToolEditorPanel(UIComponent parent, PicoContainer pico) {
        this(parent,false,pico);
    }
    
    /** constructor when being used as an app. */
    public CompositeToolEditorPanel(UIComponent parent, boolean allApps, PicoContainer pico) {        
        this.parent = parent;      
        this.chooser = (ResourceChooserInternal)pico.getComponentInstanceOfType(ResourceChooserInternal.class);
        this.apps = (ApplicationsInternal)pico.getComponentInstanceOfType(ApplicationsInternal.class);
        this.myspace = (MyspaceInternal)pico.getComponentInstanceOfType(MyspaceInternal.class);
        this.lookout =(Lookout) pico.getComponentInstanceOfType(Lookout.class);     
        tabPane = new JTabbedPane();
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPane.setTabPlacement(SwingConstants.LEFT);
        
        MutablePicoContainer builder = new DefaultPicoContainer(pico);
        builder.registerComponentInstance(this.getToolModel());
        builder.registerComponentInstance(parent);
        builder.registerComponentImplementation(DatacenterToolEditorPanel.class);
        builder.registerComponentImplementation(BasicToolEditorPanel.class);
        builder.registerComponentImplementation(RawXMLToolEditorPanel.class);       
        builder.registerComponentImplementation(ToolInformationPanel.class);
        builder.registerComponentImplementation(ChooseAToolEditorPanel.class);
        builder.registerComponentInstance(new Boolean(allApps));
        builder.start();
        AbstractToolEditorPanel information =(AbstractToolEditorPanel)builder.getComponentInstance(ToolInformationPanel.class);
        AbstractToolEditorPanel chooser = (AbstractToolEditorPanel)builder.getComponentInstance(ChooseAToolEditorPanel.class);
        AbstractToolEditorPanel datacenter =(AbstractToolEditorPanel)builder.getComponentInstance(DatacenterToolEditorPanel.class);

        tabPane.addTab("Query",datacenter);       
        AbstractToolEditorPanel basic =(AbstractToolEditorPanel)builder.getComponentInstance(BasicToolEditorPanel.class);
        tabPane.addTab("Parameter",basic);       
        AbstractToolEditorPanel xml = (AbstractToolEditorPanel)builder.getComponentInstance(RawXMLToolEditorPanel.class);
        tabPane.addTab("XML",xml);
        tabPane.addTab("Info",information);        
        tabPane.addTab("Chooser",chooser);
                
        JToolBar tb = new JToolBar();
        tb.setRollover(true);
        tb.setFloatable(false);
        tb.add(new NewAction());
        tb.add(new OpenAction());
        tb.add(new SaveAction());
        if (lookout != null) {
            tb.add(new ExecuteAction());
        }
        this.setLayout(new BorderLayout());
        this.add(tb,BorderLayout.NORTH);
        this.add(tabPane,BorderLayout.CENTER);

        views = new AbstractToolEditorPanel[] {datacenter,basic,xml,information,chooser};
        this.getToolModel().addToolEditListener(new Controller());
    }

    /** able to handle everything */
    public boolean isApplicable(Tool t, ApplicationInformation info) {
        return true;
    }
}


/* 
$Log: CompositeToolEditorPanel.java,v $
Revision 1.6  2005/11/10 16:28:26  nw
added result display to vo lookout.

Revision 1.5  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.4  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.6.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.3  2005/10/07 12:12:21  KevinBenson
resorted back to adding to the ResoruceChooserInterface a new method for selecting directories.
And then put back the older one.

Revision 1.2  2005/10/04 20:43:38  KevinBenson
set it to "false" for selecting directories on the local file system.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/