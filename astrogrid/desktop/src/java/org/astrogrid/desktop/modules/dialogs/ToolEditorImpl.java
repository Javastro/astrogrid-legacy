/*$Id: ToolEditorImpl.java,v 1.3 2005/06/23 09:08:26 nw Exp $
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

import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.dialogs.ToolEditor;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-May-2005
 *
 */
public class ToolEditorImpl implements ToolEditor {

    /** Construct a new ToolEditorImpl
     * 
     */
    public ToolEditorImpl(Myspace myspace, Applications apps) {
        super();
        this.myspace = myspace;
        this.apps = apps;
    }
    private final Myspace myspace;
    private final Applications apps;
    

    /**
     * @throws WorkflowInterfaceException
     * @see org.astrogrid.acr.dialogs.ToolEditor#editTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public Tool editTool(Tool t) throws WorkflowInterfaceException {
        ApplicationDescription desc = apps.getApplicationDescription(t.getName());
        ParameterEditorDialog d = new ParameterEditorDialog(myspace,null,null,true);
        d.populate(t,desc);
        d.show();
        d.requestFocus();
        return d.getTool();
    }

}


/* 
$Log: ToolEditorImpl.java,v $
Revision 1.3  2005/06/23 09:08:26  nw
changes for 1.0.3 release

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:32  nw
first release of application launcher
 
*/