/*$Id: ToolEditorImpl.java,v 1.1 2005/08/11 10:15:00 nw Exp $
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
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.dialogs.ToolEditor;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

import java.net.URI;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-May-2005
 *
 */
public class ToolEditorImpl implements ToolEditor {

    /** Construct a new ToolEditorImpl
     * 
     */
    public ToolEditorImpl(MyspaceInternal myspace, ApplicationsInternal apps) {
        super();
        this.myspace = myspace;
        this.apps = apps;
    }
    private final MyspaceInternal myspace;
    private final ApplicationsInternal apps;
    

    /**
     * @throws InvalidArgumentException
     * @throws WorkflowInterfaceException
     * @see org.astrogrid.acr.dialogs.ToolEditor#editTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public Document edit(Document doc) throws InvalidArgumentException {
        try {
        Tool t = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
        ApplicationDescription desc = apps.getApplicationDescription(new URI(t.getName()));
        ParameterEditorDialog d = new ParameterEditorDialog(myspace,null,null,true);
        d.populate(t,desc);
        d.show();
        d.requestFocus();
        Document doc1 = XMLUtils.newDocument();
        Marshaller.marshal(d.getTool(),doc1);
        return doc1;
        } catch (Exception e) {
            throw new InvalidArgumentException(e);
        }
    }

}


/* 
$Log: ToolEditorImpl.java,v $
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