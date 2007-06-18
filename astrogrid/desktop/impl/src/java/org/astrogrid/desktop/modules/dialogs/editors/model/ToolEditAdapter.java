/*$Id: ToolEditAdapter.java,v 1.3 2007/06/18 17:02:44 nw Exp $
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
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 08-Sep-2005
 *
 */
public class ToolEditAdapter implements ToolEditListener {

    /** Construct a new ToolEditAdapter
     * 
     */
    public ToolEditAdapter() {
        super();
    }


    public void toolSet(ToolEditEvent te) {
    }


    public void parameterChanged(ToolEditEvent te) {
    }


    public void parameterAdded(ToolEditEvent te) {
    }


    public void parameterRemoved(ToolEditEvent te) {
    }


    public void toolChanged(ToolEditEvent te) {
    }


    public void toolCleared(ToolEditEvent te) {
    }

}


/* 
$Log: ToolEditAdapter.java,v $
Revision 1.3  2007/06/18 17:02:44  nw
javadoc fixes.

Revision 1.2  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/