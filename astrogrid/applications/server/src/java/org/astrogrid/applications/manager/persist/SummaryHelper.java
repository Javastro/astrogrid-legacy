/*$Id: SummaryHelper.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 17-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.persist;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.InputListType;

/**
 * Helper class for with methods for building execution summaries from applications 
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
 *
 */
public class SummaryHelper {
     private SummaryHelper(){
     }
      
public static ExecutionSummaryType summarize(String execID, Application app) {
     ExecutionSummaryType summary = new ExecutionSummaryType();
        summary.setApplicationName(app.getApplicationDescription().getName());
        summary.setExecutionId(execID);
        summary.setStatus(app.getStatus().toExecutionPhase());
        summary.setInputList(new InputListType());
        summary.getInputList().setInput(app.getInputParameters());
        summary.setResultList(app.getResult());
    return summary;
}
 }

/* 
$Log: SummaryHelper.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/