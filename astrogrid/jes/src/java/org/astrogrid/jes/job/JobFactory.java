/*$Id: JobFactory.java,v 1.11 2004/03/15 01:31:12 nw Exp $
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


import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.util.Iterator;
/** Interface to a creation / persistence component for jobs.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Feb-2004
 *
 */
public interface JobFactory {
    /** start a transaction.
     * @deprecated - not needed, and poorly supported. code in a way as not to require rollback instead
     *
     */
    void begin();
    /** end a transaction
     * 
     * @param bCommit whether to rollback or not.
     * @return dunno
     * @throws JobException
     * @deprecated - not needed. poorly supported. don't rely on rollback functionality being there.
     */
    boolean end(boolean bCommit) throws JobException;
    /** create a new workflow from a submission request
     * @todo refactor away - doesn't serve any purpose any more.
     * @param req abstract request type
     * @return concrete workflow object
     * @throws JobException
     */
    Workflow createJob(SubmitJobRequest req) throws JobException;
    /** retreive a job by unque id
     * 
     * @param urn unique identifier for the job
     * @return associated workflow object
     * @throws JobException on general error
     * @throws NotFoundException if the workflow associated with the unique id cannot be found.
     * @throws DuplicateFoundException if the unique id has more than one workflow associated with it (unlikely, at least for current implementations)
     */
    Workflow findJob(JobURN urn) throws JobException, NotFoundException, DuplicateFoundException;

    /** return list of jobs for a user
     * @todo change to a strongly-typed return type - Workflow[] for example.
     * @param acc defines the user to search for jobs for
     * @return iterator of <tt>Workflow</tt> objects: never null, may be empty
     * @throws JobException
     */
    public Iterator findUserJobs(Account acc) throws JobException;
    /** remove a workflow document from the store 
     * @todo change to accepting jobURN instead
     * @param job document to remove
     * @throws JobException
     */
    void deleteJob(Workflow job) throws JobException;
    /** write an updated workflow document back to the store
     * 
     * @param job - workflow document, containing the unique JobURN.
     * @throws JobException
     */
    void updateJob(Workflow job) throws JobException;
}
/* 
$Log: JobFactory.java,v $
Revision 1.11  2004/03/15 01:31:12  nw
jazzed up javadoc

Revision 1.10  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.9  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

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