/*$Id: JobManager.java,v 1.2 2004/02/25 10:57:43 nw Exp $
 * Created on 24-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.jes;

import java.util.Iterator;

/** Wrapper around the legacy implememntation of {@link Job}.
 * Hides details, so we can substitute a different implementation later.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public class JobManager {
    /** Construct a new JobManager
     * 
     */
    protected JobManager() {
        super();
    }
    public static final JobManager getManager() {
        return new JobManager(); // inefficient, but will do for now..
    }
    
    public boolean cancelJob(WorkflowJob job) {
       return  Job.cancelJob(job);
    }
    public boolean deleteJob(String userid,String community, String name) {
        return Job.deleteJob(userid,community,name);
    }
    
    public WorkflowJob readJob(String userid,String community,String name) {
        return Job.readJob(userid,community,name);
    }
    
    public Iterator readJobList(String userid,String community, String communitySnippet,String filter) {
        return Job.readJobList(userid,community,communitySnippet,filter);
    }
}


/* 
$Log: JobManager.java,v $
Revision 1.2  2004/02/25 10:57:43  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.1  2004/02/24 13:10:49  nw
class that provides the static functionality of Job
 
*/