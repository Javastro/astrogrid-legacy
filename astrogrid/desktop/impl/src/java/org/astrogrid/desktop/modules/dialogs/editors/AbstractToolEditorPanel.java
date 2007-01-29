/*$Id: AbstractToolEditorPanel.java,v 1.5 2007/01/29 11:11:37 nw Exp $
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

import javax.swing.JPanel;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.workflow.beans.v1.Tool;

/** Abstract implementation of a tool editor
 *  - provdes event notifcation mechanisms, etc.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 06-Sep-2005
 *
 */
public abstract class AbstractToolEditorPanel extends JPanel {

    public static final ActionType OPEN = new ActionType( "Open" ) ; 
    public static final ActionType CLOSE = new ActionType( "Close" ) ;
    public static final ActionType NEW = new ActionType( "New" ) ; 
    public static final ActionType EXECUTE = new ActionType( "Execute" ) ;
    public static final ActionType SAVE = new ActionType( "Save" ) ;

    public static final class ActionType {
        private String name ;
        private ActionType() {}
        private ActionType( String name ) {
            this.name = name ;
        }    
        public String toString() {
            return name ;
        }
    }
    
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
     
    public abstract boolean isApplicable(Tool t, CeaApplication info); 
    
    /** Method designed to be overridden.
     * 
     * If an action has potential deletorous effects, issue a suitable warning message.
     * 
     * @param actionType the action about to be performed
     * @return a suitable warning message or null ;
     */
    public String getActionWarningMessage( ActionType actionType ) {
        return null ;
    }
    
}


/* 
$Log: AbstractToolEditorPanel.java,v $
Revision 1.5  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.4  2006/11/27 18:38:09  jl99
Merge of branch workbench-jl-2022

Revision 1.3.22.1  2006/11/23 10:33:32  jl99
Attempts to deal with multiple warning messages issued by multiple panels when a user chooses an action where there may be errors somewhere in the overall tools dialog. This situation will get easier when the Query Builder reduces its set of views of a query.

Revision 1.3  2006/08/15 10:22:06  nw
migrated from old to new registry models.

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.56.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/