/*$Id: Dispatcher.java,v 1.6 2004/08/03 16:31:25 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler;

import org.astrogrid.jes.JesException;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
/** Interface to a component that dispatches (executes) a job step.
 * This may be done by communicating with a remote service, such as an application controller, to by other means. 
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 *
 */
public interface Dispatcher {
    /** execute the tool entry defined within a job step.
     * <p>
     * Implementations should not alter the workflow and step objects - recording execution information is the responsibility of the scheduler engine. 
     * @param wf workflow document the step is from
     * @param js the job step to execute
     * @param tool the tool to use to perform the call.
     * @throws JesException
     */
    void dispatchStep(Workflow wf, Step js,Tool tool) throws JesException;
}


/* 
$Log: Dispatcher.java,v $
Revision 1.6  2004/08/03 16:31:25  nw
simplified interface to dispatcher and locator components.
removed redundant implementations.

Revision 1.5  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.4  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.3  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.3  2004/02/27 00:28:14  nw
rearranging code

Revision 1.1.2.2  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.1  2004/02/12 12:56:39  nw
factored out two components, concerned with accessing tool details
and talking with the application controller
 
*/