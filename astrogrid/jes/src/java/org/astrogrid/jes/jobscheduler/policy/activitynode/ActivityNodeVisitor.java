/*$Id: ActivityNodeVisitor.java,v 1.1 2004/04/21 16:38:48 nw Exp $
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

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public interface ActivityNodeVisitor {
    void visit(FlowActivityNode node);
    void visit(SequenceActivityNode node);
    void visit(StepActivityNode node);
}


/* 
$Log: ActivityNodeVisitor.java,v $
Revision 1.1  2004/04/21 16:38:48  nw
object model to make interrogating a workflow structure easier
 
*/