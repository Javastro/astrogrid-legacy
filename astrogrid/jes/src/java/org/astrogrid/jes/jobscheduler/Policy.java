/*$Id: Policy.java,v 1.4 2004/03/04 01:57:35 nw Exp $
 * Created on 18-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.workflow.beans.v1.Workflow;


import java.util.Iterator;

/** Interface for a component that describes a scheduling policy - i.e. determines the sequence steps are executed, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
 *
 */
public interface Policy {
    public ExecutionPhase calculateJobStatus( Workflow job );
    public Iterator calculateDispatchableCandidates( Workflow job ) ;
}


/* 
$Log: Policy.java,v $
Revision 1.4  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:28:14  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:37:10  nw
abstracted the scheduling rules part of the job scheduler
this component can be swapped with others.
determines which steps get scheduled next.
 
*/