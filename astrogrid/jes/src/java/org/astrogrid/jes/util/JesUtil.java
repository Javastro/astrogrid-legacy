/*$Id: JesUtil.java,v 1.3 2004/03/09 14:23:12 nw Exp $
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

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.axis._Account;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.LogLevel;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import java.util.Calendar;
import java.util.Iterator;

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
        if (jobURN == null ) {
            return null;
        }
        org.astrogrid.workflow.beans.v1.execution.JobURN result = new org.astrogrid.workflow.beans.v1.execution.JobURN();
        result.setContent(jobURN.toString());
        return result;
    }

    /**
     * @see org.astrogrid.jes.job.BeanFacade#castor2axis(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public static JobURN castor2axis(org.astrogrid.workflow.beans.v1.execution.JobURN jobURN) {
        if (jobURN == null) {
            return null;
        }
        return new JobURN(jobURN.getContent());
    }  

    public static org.astrogrid.workflow.beans.v1.execution.JobURN extractURN(JobIdentifierType id) {
        if (id == null) {
            return null;
        }
        int pos = id.getValue().lastIndexOf('#');
        org.astrogrid.workflow.beans.v1.execution.JobURN result = new org.astrogrid.workflow.beans.v1.execution.JobURN();
        result.setContent(id.getValue().substring(0,pos));
        return result;
    }

    public static String extractXPath(JobIdentifierType id) {
        if (id == null) {
            return null;
        }
        int pos = id.getValue().lastIndexOf('#');
        return id.getValue().substring(pos + 1);
    }
    

    public static JobIdentifierType createJobId(org.astrogrid.workflow.beans.v1.execution.JobURN urn, String xpath) {
        JobIdentifierType id = new JobIdentifierType();
        id.setValue(urn.getContent() + "#" + xpath);
        return id;
    }
    
    /** gets most recent record step. if none present, will insert one */
    public static StepExecutionRecord getLatestOrNewRecord(Step s) {
        int count = s.getStepExecutionRecordCount();
        if (count ==0) {
            StepExecutionRecord rec = new StepExecutionRecord();
            s.addStepExecutionRecord(rec);
            return rec;            
        } else {
            return s.getStepExecutionRecord(count-1);
        }
    }
    
    
    public static org.astrogrid.applications.beans.v1.cea.castor.MessageType axis2castor(MessageType mt) {
        if (mt == null) {
            return null;
        }
        org.astrogrid.applications.beans.v1.cea.castor.MessageType result = new org.astrogrid.applications.beans.v1.cea.castor.MessageType();
        result.setContent(mt.getContent());
        result.setPhase(axis2castor(mt.getPhase()));
        result.setLevel(axis2castor(mt.getLevel()));
        result.setSource(mt.getSource());
        Calendar cal =  mt.getTimestamp();
        if (cal != null) {
            result.setTimestamp(cal.getTime());
        }
        return result;
    }

    public static org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel axis2castor(LogLevel level) {
        if (level == null) {
            return null;
        } else {
            return org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel.valueOf(level.getValue());
        }
    }
    
    public static org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase axis2castor(ExecutionPhase phase) {
        if (phase == null) {
            return null;
        } else {
            return org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase.valueOf(phase.getValue());
        }
    }
    
    /*-----*/
    /** at moment this is a replication of the current duff jes behaviour - all job steps are stripped out, no matter the inner structure of the document
     *recursion can be quite inefficient, but don't care as this is only temporary behaviour. 
     *@return a list of Step objects
     */
    
    public static Iterator getJobSteps(Workflow wf) {
        wf.addFunctions(JesFunctions.FUNCTIONS);
        return wf.findXPathIterator("//*[jes:isStep()]");
    }


    /**
     * @param arg0
     * @return
     */
    public static Account axis2castor(_Account arg0) {
        Account result = new Account();
        result.setCommunity(arg0.getCommunity().getValue());
        result.setName(arg0.getName().getValue());
        return result;
    }
}


/* 
$Log: JesUtil.java,v $
Revision 1.3  2004/03/09 14:23:12  nw
integrated new JobController wsdl interface

Revision 1.2  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.1  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects
 
*/