/*$Id: JobScheduler.java,v 1.1 2004/03/05 16:16:23 nw Exp $
 * Created on 05-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.comm;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
/** Interface any scheduler must implement
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Mar-2004
 *
 */
public interface JobScheduler {
    public abstract void scheduleNewJob(JobURN jobURN) throws Exception;
            public abstract void resumeJob(JobIdentifierType id, org.astrogrid.jes.types.v1.cea.axis.MessageType info) throws Exception;
}