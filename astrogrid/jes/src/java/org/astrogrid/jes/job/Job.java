/*$Id: Job.java,v 1.11 2004/03/03 01:13:42 nw Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.util.Date;
import java.util.Iterator;
/** Interface the services require to a job.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Feb-2004
 *
 */
public interface Job {

    public abstract JobURN getId();
    public abstract String getName();
    //public abstract void setName(String name);
    public abstract String getDescription();
   // public abstract void setDescription(String description);
    public abstract Date getDate();
    //public abstract void setDate(Date date);
    public abstract String getUserId();
   // public abstract void setUserId(String name);
    public abstract String getCommunity();
   // public abstract void setCommunity(String community);
    public abstract String getGroup();
    public abstract void setGroup(String group);
    public abstract String getToken();
    //public abstract void setToken(String token);
    //public abstract Object getImplementation();
    public abstract Iterator getJobSteps();
    //public abstract boolean addJobStep(JobStep jobStep);
    //public abstract boolean removeJobStep(JobStep jobStep);
    //public abstract void setDocumentXML(String docXML);
    public abstract String getDocumentXML();
    public abstract void setStatus(ExecutionPhase status);
    public abstract ExecutionPhase getStatus();
}
/* 
$Log: Job.java,v $
Revision 1.11  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.10  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.9.2.4  2004/02/19 13:34:23  nw
cut out useless classes,
moved constants into generated code.

Revision 1.9.2.3  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.9.2.2  2004/02/12 01:16:08  nw
analyzed code, stripped interfaces of all unused methods.

Revision 1.9.2.1  2004/02/09 12:39:06  nw
isolated existing datamodel.
refactored to extract interfaces from all current datamodel classes in org.astrogrid.jes.job.
moved implementation of interfaces to org.astrogrid.jes.impl
refactored so services reference interface rather than current implementation
 
*/