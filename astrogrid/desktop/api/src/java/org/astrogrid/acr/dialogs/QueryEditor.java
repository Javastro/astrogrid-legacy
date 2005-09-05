/*$Id: QueryEditor.java,v 1.1 2005/09/05 11:09:19 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.dialogs;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;

import java.net.URI;

/** Service Interface to the Query Editor component.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
 *
 */
public interface QueryEditor {
    /** prompt the user to edit a tool document
     * @param t tool document (containing a query to a datacenter) to edit.
     * @return edited copy of this document
     * @throws InvalidArgumentException if the parameter document is malformed - ie.is not a tool
     * @throws ServiceException if any other exception occurs during editing (e.g. failure to communicate to registry)
     * 
     * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
     *
     */
    Document edit(Document t) throws InvalidArgumentException, ServiceException;
    
    /** prompt the user to edit a tool document stored elsewhere
     * @param documentLocation location the tool document is stored at (http://, ftp://, ivo://)
     * 
     * @return edited copy of this document
     * @throws InvalidArgumentException if the document location cannot be accessed, or does not contain a document
     * @throws ServiceException if any other exception occurs during editing (e.g. failure to communicate with registry)
     */
    Document editStored(URI documentLocation) throws InvalidArgumentException, ServiceException;
    
    /** prompt the user to select a datacenter and construct a query against it.
     * @return a new tool document
     * @throws ServiceException if any exception occurs during editng.
     */
    Document selectAndBuild() throws ServiceException;
    
    
}


/* 
$Log: QueryEditor.java,v $
Revision 1.1  2005/09/05 11:09:19  nw
added interfaces for registry and query dialogs
 
*/