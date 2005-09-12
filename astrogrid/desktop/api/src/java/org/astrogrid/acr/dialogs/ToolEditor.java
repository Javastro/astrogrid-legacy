/*$Id: ToolEditor.java,v 1.4 2005/09/12 15:21:43 nw Exp $
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

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;

import org.w3c.dom.Document;

import java.net.URI;

/** Service Interface to the CEA Tool Editor Dialogue
 * <p>
 * Can be used to construct calls to CEA Applications, Databases and collections.
 * Later will extend to construct calls to other VO services.
 * <p>
 * Displays the content of a Tool document, augmented with data about this application loaded from
 * the registry. Enables user to edit input ad output parameters (including using the {@link org.astrogrid.acr.dialogs.ResourceChooser}
 * dialogue to select indirect parameters.
 * <p>
 * <img src="doc-files/pw-params.png">
 * @service dialogs.toolEditor
 * @see <a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/Workflow.html#element_tool">Tool Document Schema-Documentation</a>
 * @see <a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/AGParameterDefinition.html#type_parameter">Value Parameter Element Schema-Documentation</a>
 * @see <a href="http://www.astrogrid.org/viewcvs/astrogrid/workflow-objects/schema/">XSD Schemas</a>
 * @see <a href="doc-files/example-tool.xml"> Example Tool Document</a>
 * @author Noel Winstanley nw@jb.man.ac.uk 16-May-2005
 * @todo add options later to just display tool document, without ability to edit.
 * @see org.astrogrid.acr.astrogrid.Applications
 *
 */
public interface ToolEditor {
    /** Prompt the user to edit a tool document
     * @param t document conforming to Tool schema
     * @return an edited copy of this document
     * @throws InvalidArgumentException if the document passed in is not schema-valid
     * @throws ServiceException if any other failure occurs during editing.
     * @xmlrpc takes a xml document string as a parameter, and returns another xml document string
     */
    Document   edit(Document t) throws InvalidArgumentException, ServiceException;

    /** Prompt the user to edit a tool document
     * @param t document conforming to Tool schema
     * @param desc description of the application this tool document is to be built for (rather than querying registry)
     * @return an edited copy of this document
     * @throws InvalidArgumentException if the document passed in is not schema-valid
     * @throws ServiceException if any other failure occurs during editing.
     * @xmlrpc takes a xml document string as a parameter, and returns another xml document string
     */    
    public Document editWithDescription(Document t,ApplicationInformation desc) throws InvalidArgumentException, ServiceException ;

    /** prompt the user to edit a tool document stored elsewhere
     * @param documentLocation location the tool document is stored at (http://, ftp://, ivo://)
     * 
     * @return edited copy of this document
     * @throws InvalidArgumentException if the document location cannot be accessed, or does not contain a document
     * @throws ServiceException if any other exception occurs during editing (e.g. failure to communicate with registry)
     */
    Document editStored(URI documentLocation) throws InvalidArgumentException, ServiceException;
    
    /** prompt the user to select a VO service (application, datacenter, or something else) and construct a query against it.
     * @return a new tool document
     * @throws ServiceException if any exception occurs during editng.
     */
    Document selectAndBuild() throws ServiceException;
    
    
}


/* 
$Log: ToolEditor.java,v $
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