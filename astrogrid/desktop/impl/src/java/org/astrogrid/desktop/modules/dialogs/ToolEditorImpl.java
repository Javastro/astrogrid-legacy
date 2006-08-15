/*$Id: ToolEditorImpl.java,v 1.11 2006/08/15 10:21:14 nw Exp $
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

import java.awt.Component;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.editors.ToolEditorPanelFactory;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

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
    public ToolEditorImpl(
    		List panelFactories
            ,ResourceChooserInternal rChooser
            ,Registry reg           
            , Configuration conf, HelpServerInternal help, UIInternal ui
            , ApplicationsInternal apps, MyspaceInternal myspace, BrowserControl browser) {
        super(); 
        dialog = new ToolEditorDialog(panelFactories,rChooser,apps,myspace,reg,conf,help,ui,browser);
        //dialog.setSize(600,425);
        dialog.pack();      
        
        this.apps = apps;
        this.myspace =myspace;
    }
    
    public ToolEditorImpl(ToolEditorPanelFactory panelFactory         
            , Configuration conf, HelpServerInternal help, UIInternal ui
            , ApplicationsInternal apps, MyspaceInternal myspace) {
        super(); 
        dialog = new ToolEditorDialog(panelFactory,conf,help,ui);
        //dialog.setSize(600,425);
        dialog.pack();      
        
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
        CeaApplication desc = apps.getCeaApplication(uri);
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

    public Tool editToolWithDescription(Tool t, CeaApplication desc,Component comp) {        
        dialog.populate(t,desc);
        dialog.setLocationRelativeTo(comp);
        dialog.setVisible(true);
        dialog.toFront();
        dialog.requestFocus();
        return dialog.getTool();
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
    /** variant of {@link #selectAndBuildTool} that only allows CEA apps to be selected */
    public Tool selectAndBuildCEATool(Component comp) throws ServiceException {
        dialog.setLocationRelativeTo(comp);
        dialog.nextDisplayShowCEAOnly();
        dialog.setVisible(true);
        return dialog.getTool();   
    }



}


/* 
$Log: ToolEditorImpl.java,v $
Revision 1.11  2006/08/15 10:21:14  nw
added constructor that specifies tool editor to use.upgraded to use new reg model.

Revision 1.9  2006/06/27 19:11:52  nw
fixed to filter on cea apps when needed.

Revision 1.8  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.7.30.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.7.30.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.7  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.6.2.1  2005/11/23 04:49:27  nw
attempted to improve dialogue behaviour

Revision 1.6  2005/11/11 10:08:18  nw
cosmetic fixes

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