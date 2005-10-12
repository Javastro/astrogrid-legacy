/*$Id: ToolEditorImpl.java,v 1.5 2005/10/12 13:30:10 nw Exp $
 * Created on 16-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.picocontainer.PicoContainer;
import org.w3c.dom.Document;

import java.awt.Component;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/** Implementation of the ToolEditor component.
 * Displays a {@link org.astrogrid.desktop.modules.dialogs.editors.BasicToolEditorPanel} in a dialogue 
 *  @author Noel Winstanley nw@jb.man.ac.uk 16-May-2005
 *
 */
public class ToolEditorImpl implements ToolEditorInternal {
   private final Applications apps;
    private final ToolEditorDialog dialog;
    private final MyspaceInternal myspace;    

    /** Construct a new ToolEditorImpl
     * 
     */
    public ToolEditorImpl( PicoContainer pico, Configuration conf, HelpServerInternal help, UIInternal ui, Applications apps, MyspaceInternal myspace) {
        super();
        dialog = new ToolEditorDialog(pico, conf,help,ui);
        dialog.setSize(500,600);
        //dialog.pack();      
        
        this.apps = apps;
        this.myspace =myspace;
    }
    

    /**
     * @throws InvalidArgumentException
     * @throws WorkflowInterfaceException
     * @see org.astrogrid.acr.dialogs.ToolEditor#editTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public Document edit(Document doc) throws InvalidArgumentException {
        try {
        Tool t = (Tool)Unmarshaller.unmarshal(Tool.class,doc);        
        Tool t1 = editTool(t,null);
        if (t1 == null) {
            return null;
        }
        Document doc1 = XMLUtils.newDocument();
        Marshaller.marshal(t1,doc1);
        return doc1;
        } catch (Exception e) {
            throw new InvalidArgumentException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.dialogs.ToolEditor#editStored(java.net.URI)
     */
    public Document editStored(URI arg0) throws InvalidArgumentException, ServiceException {
        Tool t = editStoredTool(arg0,null);
        if (t == null) {
            return null;
        }
        try {
        Document doc1 = XMLUtils.newDocument();
        Marshaller.marshal(t,doc1);
        return doc1;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    public Tool editStoredTool(URI toolLocation, Component comp) throws InvalidArgumentException, ServiceException {
        Tool t = null;
        try {
            InputStream is = myspace.getInputStream(toolLocation);
            t = Tool.unmarshalTool(new InputStreamReader(is));
        } catch (Exception e) {
            throw new InvalidArgumentException(e);
        } 
        return editTool(t,comp);
    }
    
    /**
     * @param t
     * @return
     * @throws URISyntaxException
     * @throws ServiceException
     * @throws NotFoundException
     * @throws InvalidArgumentException
     */
    public Tool editTool(Tool t,Component comp) throws InvalidArgumentException {
        try {
        URI uri = new URI(t.getName().startsWith("ivo://") ? t.getName() : "ivo://" + t.getName());
        ApplicationInformation desc = apps.getApplicationInformation(uri);
        Tool t1 = editToolWithDescription(t, desc,comp);
        return t1;
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (ServiceException e) {
            throw new InvalidArgumentException(e);
        } catch (NotFoundException e) {
            throw new InvalidArgumentException(e);
        } 
    }

    /**
     * @param t
     * @param desc
     * @return
     */
    public Tool editToolWithDescription(Tool t, ApplicationInformation desc,Component comp) {        
        dialog.populate(t,desc);
        dialog.setLocationRelativeTo(comp);
        dialog.setVisible(true);      
        return dialog.getTool();
    }

    public Document editWithDescription(Document doc,ApplicationInformation desc) throws InvalidArgumentException{
        try {
            Tool t = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
            Tool t1 = editToolWithDescription(t,desc,null);
            if (t1 == null) {
                return null;
            }
            Document doc1 = XMLUtils.newDocument();            
            Marshaller.marshal(t1,doc1);
            return doc1;
            } catch (Exception e) {
                throw new InvalidArgumentException(e);
            }
        }        

    /**
     * @see org.astrogrid.acr.dialogs.ToolEditor#selectAndBuild()
     */
    public Document selectAndBuild() throws ServiceException {
        Tool t1 = selectAndBuildTool(null);
        if (t1 == null) {
            return null;
        }
        try {
            Document doc1 = XMLUtils.newDocument();
            Marshaller.marshal(t1,doc1);
            return doc1;
            } catch (Exception e) {
                throw new ServiceException(e);
            }
    }

    /**
     * @see org.astrogrid.desktop.modules.dialogs.ToolEditorInternal#selectAndBuildTool(java.awt.Component)
     */
    public Tool selectAndBuildTool(Component comp) throws ServiceException {
        dialog.setLocationRelativeTo(comp);
        dialog.setVisible(true);
        return dialog.getTool();   
    }



}


/* 
$Log: ToolEditorImpl.java,v $
Revision 1.5  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.4.6.1  2005/10/12 09:21:38  nw
added java help system

Revision 1.4  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.4  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/06/23 09:08:26  nw
changes for 1.0.3 release

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:32  nw
first release of application launcher
 
*/