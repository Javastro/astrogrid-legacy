/*$Id: JobStep.java,v 1.20 2004/03/03 01:13:42 nw Exp $
 * Created on 09-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.job;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;


/** Single execution step of a job.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Feb-2004
 *
 */
public interface JobStep {


    public static final String JOINCONDITION_TRUE = "true", JOINCONDITION_FALSE = "false", JOINCONDITION_ANY = "any";
   // public abstract void setName(String name);
   // public abstract String getName();
   // public abstract void setParent(Job parent);
    public abstract Job getParent();
  //  public abstract void setStepNumber(int stepNumber);
   // public abstract void setStepNumber(Integer stepNumber);
    public abstract int getStepNumber();
    public abstract void setStatus(ExecutionPhase status);
    public abstract ExecutionPhase getStatus();
    public abstract void setComment(String comment);
    //public abstract String getComment();
    //JBL added iteration 3
   // public abstract void setSequenceNumber(int sequenceNumber);
   // public abstract void setSequenceNumber(Integer sequenceNumber);
    public abstract int getSequenceNumber();
    //JBL added iteration 3
   // public abstract void setJoinCondition(String joinCondition);
    public abstract String getJoinCondition();
   // public abstract void setTool(Tool tool);
    public abstract Tool getTool();
    /**
     * @return
     */
    public abstract String getComment();
}
/* 
$Log: JobStep.java,v $
Revision 1.20  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.19  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.18.2.5  2004/02/19 13:34:23  nw
cut out useless classes,
moved constants into generated code.

Revision 1.18.2.4  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.18.2.3  2004/02/12 01:16:08  nw
analyzed code, stripped interfaces of all unused methods.

Revision 1.18.2.2  2004/02/09 18:28:57  nw
remomved refernece to org.w3c.dom.Document in object interfaces.

Revision 1.18.2.1  2004/02/09 12:39:06  nw
isolated existing datamodel.
refactored to extract interfaces from all current datamodel classes in org.astrogrid.jes.job.
moved implementation of interfaces to org.astrogrid.jes.impl
refactored so services reference interface rather than current implementation
 
*/