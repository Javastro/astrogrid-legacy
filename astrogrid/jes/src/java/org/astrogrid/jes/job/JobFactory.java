/*$Id: JobFactory.java,v 1.8 2004/02/27 00:46:03 nw Exp $
 * Created on 09-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.job;

import org.astrogrid.jes.types.v1.JobURN;

import java.util.Iterator;
/** Interface to a creation / persistence component for jobs.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Feb-2004
 *
 */
public interface JobFactory {
    void begin();
    boolean end(boolean bCommit) throws JobException;
    Job createJob(SubmitJobRequest req) throws JobException;
    //Iterator findJobsWhere(String queryString) throws JobException;
    Job findJob(JobURN urn) throws JobException;
    /** 
     * 
     * @param userid
     * @param community
     * @param jobListXML obsolete - ignored. remove.
     * @return iterator of job objects.
     * @throws JobException
     */
    public Iterator findUserJobs(String userid, String community, String jobListXML) throws JobException;

    JobURN deleteJob(Job job) throws JobException;
    void updateJob(Job job) throws JobException;
}
/* 
$Log: JobFactory.java,v $
Revision 1.8  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.7.2.5  2004/02/19 13:34:23  nw
cut out useless classes,
moved constants into generated code.

Revision 1.7.2.4  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.7.2.3  2004/02/12 01:16:08  nw
analyzed code, stripped interfaces of all unused methods.

Revision 1.7.2.2  2004/02/09 18:28:57  nw
remomved refernece to org.w3c.dom.Document in object interfaces.

Revision 1.7.2.1  2004/02/09 12:39:06  nw
isolated existing datamodel.
refactored to extract interfaces from all current datamodel classes in org.astrogrid.jes.job.
moved implementation of interfaces to org.astrogrid.jes.impl
refactored so services reference interface rather than current implementation
 
*/