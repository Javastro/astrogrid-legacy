/*$Id: ManagingExecutionControllerImpl.java,v 1.1 2007/07/13 23:14:55 nw Exp $
 * Created on 21-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import java.util.Date;
import java.util.Observable;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.ThreadPoolExecutionController;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;
import org.astrogrid.applications.manager.persist.PersistenceException;
import org.astrogrid.desktop.modules.ag.CeaHelper;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;


/** Execution controller that adds a monitor to applicatoins that inserts progress messages into 
 * our internal messaging system..
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Oct-2005
 *
 */
public class ManagingExecutionControllerImpl extends ThreadPoolExecutionController implements ManagingExecutionController {

    /** Construct a new MessagingExecutionController
     * @param arg0
     * @param arg1
     * @throws ServiceException
     */
    public ManagingExecutionControllerImpl(ApplicationDescriptionLibrary arg0, ExecutionHistory arg1, BackgroundExecutor e) throws ServiceException{
        super(arg0, arg1, new PooledExecutorAdapter(e));
    }

    public Application getApplication(String execId) {
    	if (executionHistory.isApplicationInCurrentSet(execId)) {
    		try {
				return executionHistory.getApplicationFromCurrentSet(execId);
			} catch (ExecutionIDNotFoundException x) {
				logger.error("ExecutionIDNotFoundException",x);
			} catch (PersistenceException x) {
				logger.error("PersistenceException",x);
			}
    	}
    	// fall through
    		return null;
    	
    }



    /** extends delete - so when app is deleted, is removed from file store too.
     * @see org.astrogrid.desktop.modules.background.ManagingExecutionController#delete(java.lang.String)
     */
    public void delete(String execId) {
        if (executionHistory.isApplicationInCurrentSet(execId)) {
            return; // not finished yet.
        }
        if (executionHistory instanceof ManagingFileStoreExecutionHistory) {
        	ManagingFileStoreExecutionHistory f = (ManagingFileStoreExecutionHistory)executionHistory;
        	f.delete(execId);
        }
    }
}


/* 
$Log: ManagingExecutionControllerImpl.java,v $
Revision 1.1  2007/07/13 23:14:55  nw
Complete - task 1: task runner

Complete - task 54: Rewrite remoteprocess framework

Revision 1.8  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.7  2006/08/15 10:15:34  nw
migrated from old to new registry models.

Revision 1.6  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.5.30.3  2006/04/14 02:45:01  nw
finished code.

Revision 1.5.30.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.5.30.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.5  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.4.2.1  2005/11/23 18:09:28  nw
tuned up.

Revision 1.4  2005/11/11 17:53:27  nw
added cea polling to lookout.

Revision 1.3  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.2  2005/11/10 10:46:58  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/