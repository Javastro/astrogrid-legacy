/*$Id: ManagingExecutionController.java,v 1.2 2007/01/29 11:11:36 nw Exp $
 * Created on 11-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.astrogrid.applications.manager.ExecutionController;

/** extension to standard executioin controller, with more management functions.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 11-Nov-2005
 *
 */
public interface ManagingExecutionController extends ExecutionController {
    /** delete all record of this job
     * 
     * @param execId a job - must have already completed 
     */
    void delete(String execId);

}


/* 
$Log: ManagingExecutionController.java,v $
Revision 1.2  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.1  2005/11/11 17:53:27  nw
added cea polling to lookout.
 
*/