/*$Id: Job.java,v 1.6 2003/08/21 14:55:15 nw Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.job;

import java.util.Date;
import org.astrogrid.datacenter.config.Configurable;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public interface Job extends Configurable {
        public static final String STATUS_INITIALIZED = "INITIALIZED",
    // Newly created but not yet running
        STATUS_RUNNING = "RUNNING", // Currently executing
        STATUS_COMPLETED = "COMPLETED", // Completed OK
    STATUS_IN_ERROR = "ERROR"; // Something bad happened


    public abstract void informJobMonitor();
    public abstract String getStatus();
    public abstract void setStatus(String status);
    /**  Job URN */
    public abstract String getId();
    public abstract String getName();
    public abstract void setName(String name);
    public abstract Date getDate();
    public abstract void setDate(Date date);
    public abstract String getUserId();
    public abstract void setUserId(String name);
    public abstract String getCommunity();
    public abstract void setCommunity(String community);
    public abstract String getJobMonitorURL();
    public abstract void setJobMonitorURL(String url);
    public abstract JobStep getJobStep();
    public abstract void setJobStep(JobStep jobStep);
    public abstract String getComment();
    public abstract void setComment(String comment);
    public abstract Object getImplementation();
}

/* 
$Log: Job.java,v $
Revision 1.6  2003/08/21 14:55:15  nw
refactored Job abstract class into an interface - didn't contain anything but abstract methods and constants,
and allows implementor to use own choice of base type.
 
*/