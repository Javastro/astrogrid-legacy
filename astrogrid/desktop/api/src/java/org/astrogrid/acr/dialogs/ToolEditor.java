/*$Id: ToolEditor.java,v 1.2 2005/08/12 08:45:15 nw Exp $
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

import org.w3c.dom.Document;

/** Service Interface to the CEA Application Editor Dialogue
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
     * @xmlrpc takes a xml document string as a parameter, and returns another xml document string
     */
    Document   edit(Document t) throws InvalidArgumentException;

}


/* 
$Log: ToolEditor.java,v $
Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:33  nw
first release of application launcher
 
*/