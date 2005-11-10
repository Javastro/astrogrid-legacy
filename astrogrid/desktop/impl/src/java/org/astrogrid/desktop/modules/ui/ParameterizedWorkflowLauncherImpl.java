/*$Id: ParameterizedWorkflowLauncherImpl.java,v 1.5 2005/11/10 12:06:19 nw Exp $
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

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.system.UI;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.acr.ui.ParameterizedWorkflowLauncher;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.dialogs.ToolEditorInternal;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/** Implementaton of the parameterized workflow launcher component.
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
public class ParameterizedWorkflowLauncherImpl implements ParameterizedWorkflowLauncher {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ParameterizedWorkflowLauncherImpl.class);

    /** Construct a new ParameterizedWorkflowLauncher, using default index 
     * @throws MalformedURLException
     * @throws IOException
     * @throws SAXException
     * 
     */
    public ParameterizedWorkflowLauncherImpl(Community community, Lookout monitor, Jobs jobs,MyspaceInternal vos,ApplicationsInternal apps, ToolEditorInternal editor,ResourceChooserInternal chooser) throws MalformedURLException, IOException, SAXException {
        this(community,monitor,jobs,vos,apps,editor, chooser,new URL(DEFAULT_INDEX_URL));       
    }
    
    /** url of pw workflow index */
    public static final String DEFAULT_INDEX_URL = "http://wiki.astrogrid.org/pub/Astrogrid/ParameterizedWorkflows/index.xml";
/** construct a new launcher, specifying the index url to use */
    public ParameterizedWorkflowLauncherImpl(Community community,Lookout monitor, Jobs jobs,MyspaceInternal vos,ApplicationsInternal apps, ToolEditorInternal editor, ResourceChooserInternal chooser,URL indexURL) throws IOException, SAXException{ 
        URL[] list = getWorkflowList(indexURL);
    templates = loadWorkflows(list);        
    this.community = community;
    this.editor = editor;
    this.monitor = monitor;
    this.jobs =jobs;
    this.apps = apps;
    this.vos = vos;
    this.chooser = chooser;
}
       
    protected final ApplicationsInternal apps;
    protected final MyspaceInternal vos;
    protected final ParameterizedWorkflowTemplate[] templates;
    protected final Jobs jobs;
    protected final Community community;
    protected final Lookout monitor;
    protected final ResourceChooserInternal chooser;
    protected final ToolEditorInternal editor;
    
    public void run() {
        // force login.
        community.getUserInformation();
        
        ParameterizedWorkflowTemplate wft = chooseTemplate();
        if (wft == null) {
            return;
        }

        try {
            Tool t = apps.createTemplateTool("default",wft.getDesc());
            t = editor.editToolWithDescription(t,wft.getDesc(),null);       
        if (t == null) {
            return;
        }
            Account acc = new Account();
            acc.setCommunity(community.getUserInformation().getCommunity());
            acc.setName(community.getUserInformation().getName());
        Workflow wf = wft.instantiate(acc,t);
        if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,"Do you want to save a copy of the workflow you just built? (Optional)","Save a copy",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)) {
            URI u = chooser.chooseResource("Save a copy of this workflow",true);
            if (u != null ) {
                Writer writer = new OutputStreamWriter(vos.getOutputStream(u));
                wf.marshal(writer);
                writer.close();
            }
        }
       // @todo check this works - otherwise will need to go via a string reader. 
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(wf,doc);
        URI id = jobs.submitJob(doc);
        ResultDialog rd = new ResultDialog(null,"Workflow Submitted \nJob ID is \n" + id);
        rd.show();
        monitor.show(); // brings monitor to the front, if not already there.

        } catch (Exception e) {
            logger.warn("Failed",e);
            UIComponent.showError(null,"Failed",e);
            
        }
    }
    

    /** access a list of URLs from somewhere, each of which points to a workflow document 
     * @throws IOException
     * @throws SAXException
     * @throws MalformedURLException*/
    protected URL[] getWorkflowList(URL indexURL) throws IOException, SAXException {
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
    protected ParameterizedWorkflowTemplate[] loadWorkflows(URL[] arr) {
        List wfts= new ArrayList(arr.length); // using a list, rather than array, incase we end up with less than we expected..
        for (int i = 0; i < arr.length; i++) {
            try {
                wfts.add(new ParameterizedWorkflowTemplate(arr[i].openStream()));
            } catch (Exception e) {
                logger.warn(arr[i] + " couldn't be parsed",e);
            }
        }
        return (ParameterizedWorkflowTemplate[])wfts.toArray(new ParameterizedWorkflowTemplate[]{});
    }
    

    
    /** prompt user in some way to choose a template 
     * retruns null to indicate a cancelled operaiton.*/
    protected  ParameterizedWorkflowTemplate chooseTemplate() {
        return (ParameterizedWorkflowTemplate) JOptionPane.showInputDialog(null,"Choose a template workflow", "Template Chooser",JOptionPane.QUESTION_MESSAGE,null,templates,templates[0]);
    }
    

    



    

}


/* 
$Log: ParameterizedWorkflowLauncherImpl.java,v $
Revision 1.5  2005/11/10 12:06:19  nw
early draft of volookout

Revision 1.4  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.8  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.7  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.6  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.5  2005/06/08 14:51:59  clq2
1111

Revision 1.2.8.2  2005/06/02 14:34:33  nw
first release of application launcher

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