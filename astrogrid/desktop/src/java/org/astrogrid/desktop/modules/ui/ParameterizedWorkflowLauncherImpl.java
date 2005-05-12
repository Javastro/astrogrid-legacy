/*$Id: ParameterizedWorkflowLauncherImpl.java,v 1.3 2005/05/12 15:37:37 clq2 Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.dialogs.ResourceChooser;
import org.astrogrid.acr.system.UI;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.acr.ui.ParameterizedWorkflowLauncher;
import org.astrogrid.desktop.modules.dialogs.ParameterEditorDialog;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserDialog;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.scripting.Toolbox;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
public class ParameterizedWorkflowLauncherImpl implements Runnable, ParameterizedWorkflowLauncher {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ParameterizedWorkflowLauncherImpl.class);

    /** Construct a new ParameterizedWorkflowLauncher
     * @throws MalformedURLException
     * @throws IOException
     * @throws SAXException
     * 
     */
    public ParameterizedWorkflowLauncherImpl(Community community, JobMonitor monitor, Myspace vos,UI ui) throws MalformedURLException, IOException, SAXException {
        this(community,monitor,vos,ui,new URL(DEFAULT_INDEX_URL));       
    }
    
    public static final String DEFAULT_INDEX_URL = "http://wiki.astrogrid.org/pub/Astrogrid/ParameterizedWorkflows/index.xml";

    public ParameterizedWorkflowLauncherImpl(Community community,JobMonitor monitor,Myspace vos,UI ui,URL indexURL) throws IOException, SAXException{ 
        URL[] list = getWorkflowList(indexURL);
    templates = loadWorkflows(list);        
    this.community = community;
    this.monitor = monitor;
    this.ui = ui;
    this.vos = vos;
}
       
    protected final Myspace vos;
    protected final WorkflowTemplate[] templates;
    protected final Community community;
    protected final UI ui;
    protected final JobMonitor monitor;
    
    public void run() {
        // forces login to happen first.. - otherwise we get too many logins at once.
        getAstrogrid();
        
        WorkflowTemplate wft = chooseTemplate();
        if (wft == null) {
            return;
        }

        Tool t = editParameters(wft);
        if (t == null) {
            return;
        }
        try {
        Workflow wf = wft.instantiate(community.getEnv().getAccount(),t);
        URI u = ResourceChooserDialog.chooseResource(vos,"Save a copy of this workflow",true);
        if (u != null && u.getScheme() != null) {
            Writer writer = null;
            
            if (u.getScheme().equals("ivo")) {
                Ivorn ivo = new Ivorn(u.toString());
                FileManagerNode target = null;
               if (vos.exists(ivo)) {
                   target = vos.node(ivo);
               } else {
                   target = vos.createFile(ivo);
               }
               OutputStream os = target.writeContent();
               writer = new OutputStreamWriter(os);               
            } 
            if (u.getScheme().equals("file")) {
                File f = new File(u);
                writer = new FileWriter(f);
            }
            wf.marshal(writer);
            writer.close();
        }
        JobURN id = submit(wf);
        JOptionPane.showMessageDialog(null,"New job created : Job ID is \n" + id.getContent(),"Job Submitted",JOptionPane.INFORMATION_MESSAGE);
        monitor.show(); // brings monitor to the front, if not already there.
        monitor.refresh();

        } catch (Exception e) {
            logger.warn("Failed",e);
            JOptionPane.showMessageDialog(null,e,"Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    

    /** access a list of URLs from somewhere, each of which points to a workflow document 
     * @throws IOException
     * @throws SAXException
     * @throws MalformedURLException*/
    public URL[] getWorkflowList(URL indexURL) throws IOException, SAXException {
        InputStream is = indexURL.openStream();
        indexDigester.clear();
        List l = new ArrayList() {
            public boolean add(Object o) {
                try {
                    return super.add(new URL(o.toString().trim()));
                } catch (MalformedURLException e) {
                    logger.error("Didn't recognize that one " + e);
                    return false;
                }
            }
        };
        
        indexDigester.push(l);
        indexDigester.parse(is);
        logger.debug(l);
        return (URL[])l.toArray(new URL[]{});
    }
    
    protected final Digester indexDigester = new Digester() {{
        addCallMethod("*/workflow-template","add",1,new Class[]{String.class});
        addCallParam("*/workflow-template",0);
    }};
    
    
    
    /** construct a list of workflow documents from an array of urls , silently handle any errors.*/
    protected WorkflowTemplate[] loadWorkflows(URL[] arr) {
        List wfts= new ArrayList(arr.length); // using a list, rather than array, incase we end up with less than we expected..
        for (int i = 0; i < arr.length; i++) {
            try {
                wfts.add(new WorkflowTemplate(arr[i].openStream()));
            } catch (Exception e) {
                logger.warn(arr[i] + " couldn't be parsed",e);
            }
        }
        return (WorkflowTemplate[])wfts.toArray(new WorkflowTemplate[]{});
    }
    

    
    /** prompt user in some way to choose a template 
     * retruns null to indicate a cancelled operaiton.*/
    public  WorkflowTemplate chooseTemplate() {
        return (WorkflowTemplate) JOptionPane.showInputDialog(null,"Choose a template workflow", "Template Chooser",JOptionPane.QUESTION_MESSAGE,null,templates,templates[0]);
    }
    
    
    /** prompt user in some way to fill in fields in the template 
     * returns null to indicate canceled operation.*/
    protected Tool editParameters(WorkflowTemplate wft) {
        ParameterEditorDialog editor = new ParameterEditorDialog(wft.getDesc(),null);
        editor.show();
        return editor.getTool();
    }
    
  
    /** submit a job to jes.
     * @param wf
     * @return
     * @throws WorkflowInterfaceException
     */
    private JobURN submit(Workflow wf) throws WorkflowInterfaceException {
       return getAstrogrid().getWorkflowManager().getJobExecutionService().submitWorkflow(wf);
    }    
    
    /**
     * @return
     */
    private Toolbox getAstrogrid() {
        return community.getEnv().getAstrogrid();
    }

    

}


/* 
$Log: ParameterizedWorkflowLauncherImpl.java,v $
Revision 1.3  2005/05/12 15:37:37  clq2
nww 1111

Revision 1.2.8.1  2005/05/09 14:51:02  nw
renamed to 'myspace' and 'workbench'
added confirmation on app exit.

Revision 1.2  2005/04/27 13:42:40  clq2
1082

Revision 1.1.2.2  2005/04/25 16:41:29  nw
added file relocation (move data between stores)

Revision 1.1.2.1  2005/04/25 11:18:50  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 10:53:46  nw
tidied up a little.
added 'save workflow' function

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.3  2005/04/04 16:43:48  nw
made frames remember their previous positions.
synchronized guiLogin, so only one login box ever comes up.
made refresh action in jobmonitor more robust

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/29 16:10:59  nw
integration with the portal

Revision 1.1.2.1  2005/03/23 14:36:18  nw
got pw working
 
*/