/*$Id: QueryEditorInternal.java,v 1.1 2005/09/05 11:08:39 nw Exp $
 * Created on 02-Sep-2005
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
import org.astrogrid.acr.dialogs.QueryEditor;
import org.astrogrid.workflow.beans.v1.Tool;

import org.w3c.dom.Document;

import java.awt.Component;
import java.net.URI;

/** Extra methos for query editor, that return Tools, rather than documents - this internal
 * representation is nicer for the workflow builder to handle.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
 *
 */
public interface QueryEditorInternal extends QueryEditor {
    /** prompt the user to edit a tool document
     * @param t tool document (containing a query to a datacenter) to edit.
     * @param comp ui component to center this dialog on.
     * @return edited copy of this document
     * @throws InvalidArgumentException if the parameter document is malformed - ie.is not a tool
     * @throws ServiceException if any other exception occurs during editing (e.g. failure to communicate to registry)
     * 
     * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
     *
     */
    Tool editTool(Tool t,Component comp) throws InvalidArgumentException, ServiceException;
    
    /** prompt the user to edit a tool document stored at a remote location
     * 
     * @param toolLocation uri reference to the document (http://, ftp:// , ivo://)
     * @param comp component to center this dialog on
     * @return an edited copy of this document
     * @throws InvalidArgumentException if the parameter document is malformed or inaccessible
     * @throws ServiceException if any other exception occurs during editing.
     */
    Tool editStoredTool(URI toolLocation, Component comp) throws InvalidArgumentException, ServiceException;
    /** prompt the user to select a datacenter and construct a query against it.
     * @return a new tool document
     * @param comp ui component to center this dialog on.     
     * @throws ServiceException if any exception occurs during editng.
     */
    Tool selectAndBuildTool(Component comp) throws ServiceException;
}


/* 
$Log: QueryEditorInternal.java,v $
Revision 1.1  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs
 
*/