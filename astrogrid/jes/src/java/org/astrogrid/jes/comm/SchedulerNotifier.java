/*$Id: SchedulerNotifier.java,v 1.4 2004/03/05 16:16:23 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.comm;

import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** Component used to notify the scheduler that a job is on its way.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 *
 */
public interface SchedulerNotifier {
    public void scheduleNewJob(JobURN urn) throws Exception;
    public void resumeJob(JobIdentifierType id, MessageType mt) throws Exception;
}


/* 
$Log: SchedulerNotifier.java,v $
Revision 1.4  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.3  2004/02/19 13:33:17  nw
removed rough scheduler notifier, replaced with one
using SOAP delegate.

added in-memory concurrent notifier

Revision 1.1.2.2  2004/02/17 12:25:39  nw
improved javadocs for classes

Revision 1.1.2.1  2004/02/12 12:55:21  nw
factored out component used by web services to notify the scheduler. added to IoC pattern
 
*/