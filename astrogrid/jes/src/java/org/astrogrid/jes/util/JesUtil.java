/*$Id: JesUtil.java,v 1.8 2004/11/29 20:00:24 clq2 Exp $
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
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

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
  
    /** return an iterator of all job steps in the workflow
     * 
     * @param wf workflow
     * @return non-null iterator of step objects
     * @see org.astrogrid.workflow.beans.v1.Step
     */    
    public static Iterator getJobSteps(Workflow wf) {
        wf.addFunctions(JesFunctions.FUNCTIONS);
        return wf.findXPathIterator("//*[jes:isStep()]");
    }
    /** extract the jobURN portion of a job identifier
     * @see #createJobId(org.astrogrid.workflow.beans.v1.execution.JobURN, String)
     * @param id job identifier
     * @return the urn portion of the identifier.
     */
    public static org.astrogrid.workflow.beans.v1.execution.JobURN extractURN(JobIdentifierType id) {
         if (id == null) {
             return null;
         }
         int pos = id.getValue().lastIndexOf('#');
         org.astrogrid.workflow.beans.v1.execution.JobURN result = new org.astrogrid.workflow.beans.v1.execution.JobURN();
         result.setContent(id.getValue().substring(0,pos));
         return result;
     }

    /** extract the xpath portion of a job identifier
     * @see #createJobId(org.astrogrid.workflow.beans.v1.execution.JobURN, String)
     * @param id job identifier 
     * @return xpath portion of the identifier
     * @todo maybe rename, if scripting extension takes off - as its not an xpath any more.
     */
     public static String extractXPath(JobIdentifierType id) {
         if (id == null) {
             return null;
         }
         int pos = id.getValue().lastIndexOf('#');
         return id.getValue().substring(pos + 1);
     }
    
    /** create a job identifier - for passing out into an application contorller - from a jobURN and xpath of the step to execute */
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

 /** convert between castor and axis representations of the same schema object */
    public static JobURN castor2axis(org.astrogrid.workflow.beans.v1.execution.JobURN jobURN) {
        if (jobURN == null) {
            return null;
        }
        return new JobURN(jobURN.getContent());
    }

/** extract mesasges from a chain of exceptions 
 * */
public static String getMessageChain(Throwable  e) {
    StringBuffer buff = new StringBuffer();
    Throwable next = e;
    boolean first = true;
    while (next != null) {
        if (!first) {
            buff.append("\n caused by \n");
        }
        first = false;
        
        buff.append(next.getClass().getName());
        buff.append(" ");
        buff.append(next.getMessage());
        next = next.getCause();
    }
    return buff.toString();        
}  
   

}


/* 
$Log: JesUtil.java,v $
Revision 1.8  2004/11/29 20:00:24  clq2
jes-nww-714

Revision 1.7.100.3  2004/11/25 23:48:13  nw
formatting

Revision 1.7.100.2  2004/11/25 23:43:07  nw
bugfix

Revision 1.7.100.1  2004/11/25 23:34:34  nw
improved error messages reported from jes

Revision 1.7  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.6  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.5  2004/03/18 16:41:57  pah
moved the axis2castor stuff to the common project under beans package

Revision 1.4  2004/03/15 23:45:07  nw
improved javadoc

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