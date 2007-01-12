/*$Id: ParameterizedWorkflowLauncherImpl.java,v 1.15 2007/01/12 13:20:04 nw Exp $
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

import java.awt.FlowLayout;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.stream.XMLInputFactory;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.acr.ui.ParameterizedWorkflowLauncher;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ToolEditorInternal;
import org.astrogrid.desktop.modules.ivoa.CacheFactory;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Implementaton of the parameterized workflow launcher component.
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 * @todo adjust to use process manager, instead of jes.
 *
 */
public class ParameterizedWorkflowLauncherImpl implements ParameterizedWorkflowLauncher {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ParameterizedWorkflowLauncherImpl.class);    
    
    public ParameterizedWorkflowLauncherImpl(Community community,Lookout monitor, Jobs jobs,MyspaceInternal vos,ApplicationsInternal apps, ToolEditorInternal editor, ResourceChooserInternal chooser,List templateURLs, CacheFactory cacheFac, HelpServerInternal help) throws IOException, SAXException{ 
        URL[] list = new URL[templateURLs.size()];
        Iterator i = templateURLs.iterator();
        for (int ix = 0; i.hasNext(); ix++) {
            list[ix] = new URL((String)i.next());
        }
    templates = loadWorkflows(cacheFac.getManager().getCache(CacheFactory.PW_CACHE),list);        
    this.community = community;
    this.editor = editor;
    this.monitor = monitor;
    this.jobs =jobs;
    this.apps = apps;
    this.vos = vos;
    this.chooser = chooser;
    this.help = help;
    
}
    protected final HelpServerInternal help;
    protected final ApplicationsInternal apps;
    protected final MyspaceInternal vos;
    protected final ParameterizedWorkflowTemplate[] templates;
    protected final Jobs jobs;
    protected final Community community;
    protected final Lookout monitor;
    protected final ResourceChooserInternal chooser;
    protected final ToolEditorInternal editor;
    
    public void run() {

        
        ParameterizedWorkflowTemplate wft = chooseTemplate();
        if (wft == null) {
            return;
        }
        Writer writer = null;
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
        String options[] = {"Yes (and submit)", "No (and submit)", "Yes (don't submit)", };
        int value = JOptionPane.showOptionDialog(
            null,
            "Do you want to save a copy of the workflow you just built? (Optional)",
            "Save a copy",
            JOptionPane.YES_NO_OPTION, // Need something  
              JOptionPane.QUESTION_MESSAGE,
            null, // Use default icon for message type
            options,
            options[0]);
        if (value == 0) {
            URI u = chooser.chooseResource("Save a copy of this workflow",true);
            if (u != null ) {
                writer = new OutputStreamWriter(vos.getOutputStream(u));
                wf.marshal(writer);
            }
            Document doc = XMLUtils.newDocument();
            Marshaller.marshal(wf,doc);
            URI id = jobs.submitJob(doc);
            monitor.show(); // brings monitor to the front, if not already there.
        } else if (value == 1) {
            Document doc = XMLUtils.newDocument();
            Marshaller.marshal(wf,doc);
            URI id = jobs.submitJob(doc);
            monitor.show(); // brings monitor to the front, if not already there.
        } else if (value == 2) {
            URI u = chooser.chooseResource("Save a copy of this workflow",true);
            if (u != null ) {
                writer = new OutputStreamWriter(vos.getOutputStream(u));
                wf.marshal(writer);
            }
        }
        /**         
         if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,"Do you want to save a copy of the workflow you just built? (Optional)","Save a copy",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)) {
            URI u = chooser.chooseResource("Save a copy of this workflow",true);
            if (u != null ) {
                writer = new OutputStreamWriter(vos.getOutputStream(u));
                wf.marshal(writer);
            }
        }
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(wf,doc);
        URI id = jobs.submitJob(doc);
        monitor.show(); // brings monitor to the front, if not already there.
        */

        } catch (Exception e) {
            logger.warn("Failed",e);
            UIComponentImpl.showError(null,"Failed",e);
            
        } finally {
        	if (writer != null) {
        		try {
        			writer.close();
        		} catch (IOException ignored) {
        		}
        	}
        }
    }
    
    
    /** construct a list of workflow documents from an array of urls , silently handle any errors.*/
    protected ParameterizedWorkflowTemplate[] loadWorkflows(Ehcache cache,URL[] arr) {
        XMLInputFactory fac = XMLInputFactory.newInstance();
        List wfts= new ArrayList(arr.length); // using a list, rather than array, incase we end up with less than we expected..
        for (int i = 0; i < arr.length; i++) {
            final URL url = arr[i];
            /* suspect htis was causing a problem - dunno how.
            Element e = cache.get(url);
            if (e != null) {
            	wfts.add(e.getValue());
            } else {
            */
			try {
                Serializable s = new ParameterizedWorkflowTemplate(fac,url.openStream());
               // cache.put(new Element(url,s));
                wfts.add(s);
            } catch (Exception ex) {
                logger.warn(url + " couldn't be parsed",ex);
            }
            //}
        }
        return (ParameterizedWorkflowTemplate[])wfts.toArray(new ParameterizedWorkflowTemplate[wfts.size()]);
    }
    

    
    /** prompt user in some way to choose a template 
     * retruns null to indicate a cancelled operaiton.*/
    protected  ParameterizedWorkflowTemplate chooseTemplate() {
    	JPanel panel = new JPanel();
    	panel.setLayout(new FlowLayout(FlowLayout.LEADING));
    	panel.add(new JLabel("Choose a workflow to run"));
    	JButton b = help.createHelpButton("userInterface.scienceWorkflows");
    	panel.add(b);
        return (ParameterizedWorkflowTemplate) JOptionPane.showInputDialog(null
        		,panel
        		, "Workflow Chooser"
        		,JOptionPane.QUESTION_MESSAGE
        		,null
        		,templates
        		,templates.length >  0 ? templates[0] : null);
    }
    

    



    

}


/* 
$Log: ParameterizedWorkflowLauncherImpl.java,v $
Revision 1.15  2007/01/12 13:20:04  nw
made sure every ui app has a help menu.

Revision 1.14  2006/11/09 12:08:33  nw
final set of changes for 2006.4.rc1

Revision 1.13  2006/10/23 14:14:47  pjn3
Bug# 1882
Option to save workflow without submitting added

Revision 1.12  2006/09/01 15:09:03  nw
made paramterized workflow configure from the wiki again.

Revision 1.11  2006/08/31 21:31:37  nw
minor tweaks and doc fixes.

Revision 1.10  2006/08/15 10:04:54  nw
migrated from old to new registry models.

Revision 1.9  2006/07/20 12:32:34  nw
removed jobid popup.

Revision 1.8  2006/06/27 10:35:30  nw
findbugs tweaks

Revision 1.7  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.6.34.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.6.34.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.6  2005/11/11 10:08:18  nw
cosmetic fixes

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