/*$Id: ToolEditListener.java,v 1.1 2005/09/12 15:21:16 nw Exp $
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

import java.util.EventListener;

/** listener interface, for notification of tool edit events
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Sep-2005
 *
 */
public interface ToolEditListener extends EventListener {
    /** fired when the tool to be edited has been set */
    void toolSet(ToolEditEvent te);
    
    /** fired when a parameter value / indirect attribute is altered.*/
    void parameterChanged(ToolEditEvent te);
    
    /** fired when an optional / repeated parameter is added */
    void parameterAdded(ToolEditEvent te);
    /** fired when an optional / repeated parameter is removed */
    void parameterRemoved(ToolEditEvent te);
    /** fired when the tool being edited has had major structural alterations made to it */
    void toolChanged(ToolEditEvent te);
    /** fired when the tool being edited is cleared */
    void toolCleared(ToolEditEvent te);
}


/* 
$Log: ToolEditListener.java,v $
Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/