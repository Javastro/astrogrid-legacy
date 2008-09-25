/*$Id: ToolEditor.java,v 1.9 2008/09/25 16:02:00 nw Exp $
 * Created on 16-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.dialogs;

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Applications;
import org.w3c.dom.Document;

/** AR Service: Dialogue that allows user to select a remote application or database and fill in invocation parameters.
 *
 *The output of the dialogue is a <i>Tool Document</i>, suitable for passing to the {@link Applications} service for execution.

 * @service dialogs.toolEditor
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 16-May-2005
 * @todo add options later to just display tool document, without ability to edit.
 * @see org.astrogrid.acr.astrogrid.Applications
 *
 */
public interface ToolEditor {
    /** Prompt the user to edit a tool document.
     * @param t document conforming to Tool schema
     * @return an edited copy of this document, or null if the user pressed cancel.
     * @throws InvalidArgumentException if the document passed in is not schema-valid
     * @throws ServiceException if any other failure occurs during editing.
     * @xmlrpc takes a xml document string as a parameter, and returns another xml document string
     */
    Document   edit(Document t) throws InvalidArgumentException, ServiceException;


    /** Prompt the user to edit a tool document.
     * @param documentLocation location the tool document is stored at (http://, ftp://, ivo://)
     * 
     * @return edited copy of this document, or null if the user pressed cancel.
     * @throws InvalidArgumentException if the document location cannot be accessed, or does not contain a document
     * @throws ServiceException if any other exception occurs during editing (e.g. failure to communicate with registry)
     */
    Document editStored(URI documentLocation) throws InvalidArgumentException, ServiceException;
    
    /** prompt the user to select a remote application or database and construct an invocation/query against it.
     * @return a new tool document
     * @throws ServiceException if any exception occurs during editng.
     */
    Document selectAndBuild() throws ServiceException;
    
    
}


/* 
$Log: ToolEditor.java,v $
Revision 1.9  2008/09/25 16:02:00  nw
documentation overhaul

Revision 1.8  2007/03/08 17:47:12  nw
updated interfaces.

Revision 1.7  2007/01/24 14:04:44  nw
updated my email address

Revision 1.6  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.5  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.4  2005/09/12 15:21:43  nw
added stuff for adql.

Revision 1.3  2005/08/25 16:59:44  nw
1.1-beta-3

Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:33  nw
first release of application launcher
 
*/