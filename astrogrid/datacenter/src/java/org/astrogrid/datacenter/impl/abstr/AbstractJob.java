/*$Id: AbstractJob.java,v 1.1 2003/08/22 10:35:02 nw Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.impl.abstr;

import java.util.Date;

import org.astrogrid.datacenter.config.ConfigurableImpl;
import org.astrogrid.datacenter.job.Job;
import org.astrogrid.datacenter.job.JobStep;

/** abstract implementation of a Job. Provides implementation for all getter/setter pairs - no brainer.
 * only method left abstract from the Job interface is {@link #informJobMonitor}
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public abstract class AbstractJob extends ConfigurableImpl implements Job {

    protected boolean dirty = false;

    protected String comment = "";

    protected String jobMonitorURL = "";

    protected String userId = "";

    protected String community = "";

    protected String name = "";

    protected String jobURN = "";
    
    protected String status = "";

    protected Date date = new Date();

    protected JobStep jobStep;

    public abstract void informJobMonitor();

    public String getId() {	return jobURN ;	}

    public void setId(String jobURN) { this.jobURN = jobURN ;}

    public void setName(String name) { this.name = name; }

    public String getName() { return this.name; }

    public Date getDate() { return this.date ; }

    public void setDate(Date date) { this.date = date ; }

    public void setCommunity(String community) { this.community = community; }

    public String getCommunity() { return community; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getUserId() { return userId; }

    public void setJobMonitorURL(String jobMonitorURL) { this.jobMonitorURL = jobMonitorURL; }

    public String getJobMonitorURL() { return jobMonitorURL; }

    public void setJobStep(JobStep jobStep) { this.jobStep = jobStep; }

    public JobStep getJobStep() { return jobStep; }

    public String getComment() { return comment ; }

    public void setComment(String comment) { this.comment = comment ; }

    public String getStatus() {
    	return this.status;
    }

    public void setStatus(String status) {
    	if( this.status != null )
    	    dirty = true ;
        this.status = status ;
    }

  
}


/* 
$Log: AbstractJob.java,v $
Revision 1.1  2003/08/22 10:35:02  nw
refactored job and job step into interface, abstract base class and implementation
 
*/