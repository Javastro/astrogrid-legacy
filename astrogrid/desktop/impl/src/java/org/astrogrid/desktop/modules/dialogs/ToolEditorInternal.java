/*$Id: ToolEditorInternal.java,v 1.3 2006/04/18 23:25:44 nw Exp $
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

import java.awt.Component;
import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.dialogs.ToolEditor;
import org.astrogrid.workflow.beans.v1.Tool;

/** extension of the tool editor interface with some more handy methods.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Aug-2005
 *
 */
public interface ToolEditorInternal extends ToolEditor {
    /** prompt the user to edit a tool document
     * <p>
     * Internal variant on {@link ToolEditor#edit(org.w3c.dom.Document)}
     * @param t tool document (containing a query to a datacenter) to edit.
     * @param comp ui component to center this dialog on.
     * @return edited copy of this document
     * @throws InvalidArgumentException if the parameter document is malformed - ie.is not a tool
     * @throws ServiceException if any other exception occurs during editing (e.g. failure to communicate to registry)
     * 
     * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
     *
     */
    public Tool editTool(Tool t,Component comp) throws  InvalidArgumentException;

    /** prompt the user to edit a tool document stored at a remote location
     * <p>
     * Internal variant of {@link ToolEditor#editStored(java.net.URI)}
     * @param toolLocation uri reference to the document (http://, ftp:// , ivo://)
     * @param comp component to center this dialog on
     * @return an edited copy of this document
     * @throws InvalidArgumentException if the parameter document is malformed or inaccessible
     * @throws ServiceException if any other exception occurs during editing.
     */
    Tool editStoredTool(URI toolLocation, Component comp) throws InvalidArgumentException, ServiceException;
    
    /** prompt the user to select a datacenter and construct a query against it.
     * <p>
     * Internal variant of {@link ToolEditor#selectAndBuild()}
     * @return a new tool document
     * @param comp ui component to center this dialog on.     
     * @throws ServiceException if any exception occurs during editng.
     */
    Tool selectAndBuildTool(Component comp) throws ServiceException;
    

    /**
     * Internal variant on {@link ToolEditor#editWithDescription(org.w3c.dom.Document, org.astrogrid.acr.astrogrid.ApplicationInformation)}
     * @param t
     * @param desc
     * @return
     */
    public Tool editToolWithDescription(Tool t, ApplicationInformation desc,Component comp) ;
        

}


/* 
$Log: ToolEditorInternal.java,v $
Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.56.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.2  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/