/*$Id: ToolEditAdapter.java,v 1.1 2005/09/12 15:21:16 nw Exp $
 * Created on 08-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.editors.model;

/** Adapter for a tool edit listener - just provides empty implememntations of all methods
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2005
 *
 */
public class ToolEditAdapter implements ToolEditListener {

    /** Construct a new ToolEditAdapter
     * 
     */
    public ToolEditAdapter() {
        super();
    }

    /**
     * @see org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener#toolSet(org.astrogrid.desktop.modules.dialogs.editors.ToolEditEvent)
     */
    public void toolSet(ToolEditEvent te) {
    }

    /**
     * @see org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener#parameterChanged(org.astrogrid.desktop.modules.dialogs.editors.ToolEditEvent)
     */
    public void parameterChanged(ToolEditEvent te) {
    }

    /**
     * @see org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener#parameterAdded(org.astrogrid.desktop.modules.dialogs.editors.ToolEditEvent)
     */
    public void parameterAdded(ToolEditEvent te) {
    }

    /**
     * @see org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener#parameterRemoved(org.astrogrid.desktop.modules.dialogs.editors.ToolEditEvent)
     */
    public void parameterRemoved(ToolEditEvent te) {
    }

    /**
     * @see org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener#toolChanged(org.astrogrid.desktop.modules.dialogs.editors.ToolEditEvent)
     */
    public void toolChanged(ToolEditEvent te) {
    }

    /**
     * @see org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener#toolCleared(org.astrogrid.desktop.modules.dialogs.editors.ToolEditEvent)
     */
    public void toolCleared(ToolEditEvent te) {
    }

}


/* 
$Log: ToolEditAdapter.java,v $
Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/