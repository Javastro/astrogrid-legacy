/*$Id: JobsInternal.java,v 1.4 2007/06/18 16:27:15 nw Exp $
 * Created on 06-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.workflow.beans.v1.Workflow;

/**
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 06-Sep-2005
 *
 */
public interface JobsInternal extends Jobs {
    /** submit a workflow document for execution
     * 
     * an internal variant of {@link Jobs#submitJob(org.w3c.dom.Document)} 
     * */
    public URI submitWorkflow(Workflow wf) throws InvalidArgumentException, ServiceException;
    /** create a new, initialized workflow document
     *  
     * an internal variant of {@link Jobs#createJob()}
     * @throws ServiceException*/
    public Workflow createWorkflow() throws ServiceException;
}


/* 
$Log: JobsInternal.java,v $
Revision 1.4  2007/06/18 16:27:15  nw
javadoc

Revision 1.3  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.56.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/