/*$Id: JesUtil.java,v 1.1 2004/03/04 01:57:35 nw Exp $
 * Created on 03-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.util;

import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.LogLevel;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.ActivityContainer;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** class of static helper methods.
 * <p>
 * primarily stuff for mappinig between castor and axis object models.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Mar-2004
 *
 */
public class JesUtil {
    /** Construct a new JesUtil
     * 
     */
    private JesUtil() {
        super();
    }
  

    /**
     * @see org.astrogrid.jes.job.BeanFacade#axis2castor(org.astrogrid.jes.types.v1.JobURN)
     */
    public static org.astrogrid.workflow.beans.v1.execution.JobURN axis2castor(JobURN jobURN) {
        org.astrogrid.workflow.beans.v1.execution.JobURN result = new org.astrogrid.workflow.beans.v1.execution.JobURN();
        result.setContent(jobURN.toString());
        return result;
    }

    /**
     * @see org.astrogrid.jes.job.BeanFacade#castor2axis(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public static JobURN castor2axis(org.astrogrid.workflow.beans.v1.execution.JobURN jobURN) {
        return new JobURN(jobURN.getContent());
    }  
    /** @todo test */
    public static org.astrogrid.workflow.beans.v1.execution.JobURN extractURN(JobIdentifierType id) {
        int pos = id.getValue().lastIndexOf('#');
        org.astrogrid.workflow.beans.v1.execution.JobURN result = new org.astrogrid.workflow.beans.v1.execution.JobURN();
        result.setContent(id.getValue().substring(0,pos));
        return result;
    }
    /** @todo test */
    public static String extractXPath(JobIdentifierType id) {
        int pos = id.getValue().lastIndexOf('#');
        return id.getValue().substring(pos + 1);
    }
    
    /** @todo test this is correct */
    public static JobIdentifierType createJobId(org.astrogrid.workflow.beans.v1.execution.JobURN urn, String xpath) {
        JobIdentifierType id = new JobIdentifierType();
        id.setValue(urn.getContent() + "#" + xpath);
        return id;
    }
    /** @todo test this is correct */
    public static StepExecutionRecord getLatestRecord(Step s) {
        int last = s.getStepExecutionRecordCount();
        return s.getStepExecutionRecord(last-1);
    }
    
    
    public static org.astrogrid.applications.beans.v1.cea.castor.MessageType axis2castor(MessageType mt) {
        org.astrogrid.applications.beans.v1.cea.castor.MessageType result = new org.astrogrid.applications.beans.v1.cea.castor.MessageType();
        result.setContent(mt.getValue());
        result.setPhase(axis2castor(mt.getPhase()));
        result.setLevel(axis2castor(mt.getLevel()));
        result.setSource(mt.getSource());
        result.setTimestamp(mt.getTimestamp().getTime());
        return result;
    }

    public static org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel axis2castor(LogLevel level) {
        return org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel.valueOf(level.getValue());
    }
    
    public static org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase axis2castor(ExecutionPhase phase) {
        return org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase.valueOf(phase.getValue());
    }
    
    /*-----*/
    /** at moment this is a replication of the current duff jes behaviour - all job steps are stripped out, no matter the inner structure of the document
     *recursion can be quite inefficient, but don't care as this is only temporary behaviour. 
     *@todo update this so that workflow has meaning.
     *@return a list of Step objects
     */
    
    public static Iterator getJobSteps(Workflow wf) {
        return listAllJobSteps(wf.getSequence().getActivity()).iterator();
    }
    public static  List listAllJobSteps(AbstractActivity[] seq) {        
        List result = new ArrayList();
        for (int i = 0; i < seq.length; i++) {
            AbstractActivity ac = seq[i];
            if (ac instanceof Step) {
                result.add(ac);
            } else if (ac instanceof ActivityContainer) {
                List sublist = listAllJobSteps(((ActivityContainer)ac).getActivity());
                result.addAll(sublist);
            } 
        }
        return result;
    }
    
}


/* 
$Log: JesUtil.java,v $
Revision 1.1  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects
 
*/