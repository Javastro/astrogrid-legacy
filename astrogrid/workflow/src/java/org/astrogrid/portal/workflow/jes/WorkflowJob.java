/*$Id: WorkflowJob.java,v 1.2 2004/02/25 10:57:43 nw Exp $
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
import java.util.Date;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public interface WorkflowJob {
    public abstract void setName(String name);
    public abstract String getName();
    public abstract void setUserid(String userid);
    public abstract String getUserid();
    public abstract void setCommunity(String community);
    public abstract String getCommunity();
    public abstract void setDescription(String description);
    public abstract String getDescription();
    public abstract String getJobid();
    public abstract void setTimestamp(Date timestamp);
    public abstract Date getTimestamp();
    public abstract void setStatus(String status);
    public abstract String getStatus();
}
/* 
$Log: WorkflowJob.java,v $
Revision 1.2  2004/02/25 10:57:43  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.1  2004/02/24 13:11:11  nw
interface to the data object portion of Job
 
*/