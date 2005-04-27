/*$Id: Jobs.java,v 1.2 2005/04/27 13:42:41 clq2 Exp $
 * Created on 18-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.astrogrid;

import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.net.URL;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2005
 *
 */
public interface Jobs {
    public abstract JobURN[] list() throws WorkflowInterfaceException;
    public WorkflowSummaryType[] listSummaries() throws WorkflowInterfaceException;

    public abstract Workflow getJob(JobURN jobURN) throws WorkflowInterfaceException;


    public abstract String getJobSummary(JobURN jobURN) throws WorkflowInterfaceException;


    public abstract void cancelJob(JobURN jobURN) throws WorkflowInterfaceException;

  
    public abstract void deleteJob(JobURN jobURN) throws WorkflowInterfaceException;

 
    public abstract JobURN submitJob(Workflow workflow) throws WorkflowInterfaceException;

    public JobURN submitJobFile(URL workflowURL) throws WorkflowInterfaceException, MarshalException, ValidationException, IOException ;
        
}

/* 
 $Log: Jobs.java,v $
 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:11  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.3  2005/04/05 11:42:15  nw
 added 'submit' and 'summary' methods

 Revision 1.1.2.2  2005/03/22 12:04:03  nw
 working draft of system and ag components.
 
 */