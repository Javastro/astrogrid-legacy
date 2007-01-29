/*$Id: ToolEditEvent.java,v 1.3 2007/01/29 11:11:37 nw Exp $
 * Created on 06-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.editors.model;

import java.util.EventObject;

import org.astrogrid.acr.dialogs.ToolEditor;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.editors.AbstractToolEditorPanel;

/** event object fired by tool editors - source will be an instance of the {@link ToolEditor} interface
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 06-Sep-2005
 *
 */
public class ToolEditEvent extends EventObject {

    /** Construct a new ToolEditEvent
     * @param source - the tool model that has generated this event.
     */
    public ToolEditEvent(ToolModel source) {
        super(source);
    }
    
    /**
     *  Construct a new ToolEditEvent for a parameter change
     * @param source the editor that has caused the panel to change
     * @param pv the parameter that has changed.
     */
    public ToolEditEvent(AbstractToolEditorPanel source, ParameterValue pv) {
        super(source);
        this.pv = pv;
    }
    private ParameterValue pv;
    
    /** return the changed parameter value, if this was a parameter-related event , otherwise null */
    public ParameterValue getChangedParameter(){
        return pv;
    }

}


/* 
$Log: ToolEditEvent.java,v $
Revision 1.3  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.56.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/