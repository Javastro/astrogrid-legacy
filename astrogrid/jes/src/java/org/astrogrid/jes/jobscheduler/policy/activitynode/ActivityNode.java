/*$Id: ActivityNode.java,v 1.2 2004/07/01 21:15:00 nw Exp $
 * Created on 21-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.policy.activitynode;

import org.astrogrid.workflow.beans.v1.AbstractActivity;

/** Interface to a composite that provides a uniform view over a workflow step tree.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public interface ActivityNode {
    public static final int STEP = 1;
    public static final int SEQUENCE = 2;
    public static final int FLOW = 3;
    
    int getNodeType();
    ActivityNode getParent();
    ActivityNode[] getChildren();
    
    AbstractActivity getWrappedWorkflowObject();
    
    
    void accept(ActivityNodeVisitor visitor);
    
    // add more interrogators here.
    
}


/* 
$Log: ActivityNode.java,v $
Revision 1.2  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.1  2004/04/21 16:38:48  nw
object model to make interrogating a workflow structure easier
 
*/