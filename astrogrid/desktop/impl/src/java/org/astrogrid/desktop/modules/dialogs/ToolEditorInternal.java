/*$Id: ToolEditorInternal.java,v 1.1 2005/08/25 16:59:58 nw Exp $
 * Created on 24-Aug-2005
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
import org.astrogrid.acr.dialogs.ToolEditor;
import org.astrogrid.workflow.beans.v1.Tool;

import java.awt.Component;
import java.net.URI;
import java.net.URISyntaxException;

/** extension of the tool editor interface with some more handy methods.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Aug-2005
 *
 */
public interface ToolEditorInternal extends ToolEditor {
    public Tool editTool(Tool t,Component comp) throws  InvalidArgumentException;
        

    /**
     * @param t
     * @param desc
     * @return
     */
    public Tool editToolWithDescription(Tool t, ApplicationInformation desc,Component comp) ;
        

}


/* 
$Log: ToolEditorInternal.java,v $
Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/