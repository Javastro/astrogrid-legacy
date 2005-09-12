/*$Id: AbstractToolEditorPanel.java,v 1.1 2005/09/12 15:21:16 nw Exp $
 * Created on 06-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.dialogs.ToolEditor;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.workflow.beans.v1.Tool;

import org.w3c.dom.Document;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;

/** Abstract implementation of a tool editor
 *  - provdes event notifcation mechanisms, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Sep-2005
 *
 */
public abstract class AbstractToolEditorPanel extends JPanel {


    public AbstractToolEditorPanel(ToolModel tm) {
        this.toolModel = tm;
    }
    
    public AbstractToolEditorPanel() {
        this(new ToolModel());
    }
    protected final ToolModel toolModel;
    public ToolModel getToolModel() {
        return toolModel;
    }

    protected boolean editable = true;
    /** name for editable property */
    public static final String EDITABLE_PROPERTY = "editable";
    
    
    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        boolean old = this.editable;
        this.editable = editable;
        super.firePropertyChange(EDITABLE_PROPERTY,old,editable);
    }
    
    /** abstract method - retuns true if this tool editor is happy to edit this kind of tool 
     * @param t the tool to edit (may be null to indicate no tool selected )
     * @param info description of this tool.
     * 
     * @return true if the editor is happy to handle this kind of tool
     */
     
    public abstract boolean isApplicable(Tool t, ApplicationInformation info); 
    
    
}


/* 
$Log: AbstractToolEditorPanel.java,v $
Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/