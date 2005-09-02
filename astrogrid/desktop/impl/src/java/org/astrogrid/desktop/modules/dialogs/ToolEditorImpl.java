/*$Id: ToolEditorImpl.java,v 1.3 2005/09/02 14:03:34 nw Exp $
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
import org.astrogrid.acr.dialogs.ToolEditor;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

import java.awt.Component;
import java.net.URI;
import java.net.URISyntaxException;

/** Implementation of the ToolEditor component.
 * Displays a {@link org.astrogrid.desktop.modules.dialogs.ParametersPanel} in a dialogue 
 *  @author Noel Winstanley nw@jb.man.ac.uk 16-May-2005
 *
 */
public class ToolEditorImpl implements ToolEditorInternal {

    /** Construct a new ToolEditorImpl
     * 
     */
    public ToolEditorImpl(Applications apps,ResourceChooserInternal chooser) {
        super();
        dialog = new ParameterEditorDialog(chooser);
        dialog.pack();
        dialog.setSize(600,400);        
        this.apps = apps;
    }
    private final ParameterEditorDialog dialog;
    private final Applications apps;
    

    /**
     * @throws InvalidArgumentException
     * @throws WorkflowInterfaceException
     * @see org.astrogrid.acr.dialogs.ToolEditor#editTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public Document edit(Document doc) throws InvalidArgumentException {
        try {
        Tool t = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
        Tool t1 = editTool(t,null);
        Document doc1 = XMLUtils.newDocument();
        Marshaller.marshal(t1,doc1);
        return doc1;
        } catch (Exception e) {
            throw new InvalidArgumentException(e);
        }
    }

    public Document editWithDescription(Document doc,ApplicationInformation desc) throws InvalidArgumentException{
        try {
            Tool t = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
            Tool t1 = editToolWithDescription(t,desc,null);
            Document doc1 = XMLUtils.newDocument();            
            Marshaller.marshal(t1,doc1);
            return doc1;
            } catch (Exception e) {
                throw new InvalidArgumentException(e);
            }
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



}


/* 
$Log: ToolEditorImpl.java,v $
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