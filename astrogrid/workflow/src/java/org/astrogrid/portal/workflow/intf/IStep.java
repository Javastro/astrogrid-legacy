/*$Id: IStep.java,v 1.2 2004/02/25 10:57:43 nw Exp $
 * Created on 24-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public interface IStep extends XMLSerializable{
    /**
     * Sets this step's name
     */
    public abstract void setName(String name);
    /**
     * Gets this step's name
     */
    public abstract String getName();
    /**
     * Sets this step's join condition
     */
    public abstract void setJoinCondition(JoinCondition joinCondition);
    /**
     * Gets this step's join condition
     */
    public abstract JoinCondition getJoinCondition();
    /**
     * Tester for the condition JoinCondition.TRUE
     */
    //public abstract boolean isJoinConditionTrue();
    /**
     * Tester for the condition JoinCondition.FALSE
     */
    //public abstract boolean isJoinConditionFalse();
    /**
     * Tester for the condition JoinCondition.ANY
     */
   // public abstract boolean isJoinConditionAny();
    /**
     * Sets this step's Tool
     */
    public abstract void setTool(ITool tool);
    /**
     * Gets this step's Tool
     */
    public abstract ITool getTool();

     /** Sets this step's description
     */
        public abstract void setDescription(String description); /**
         * Gets this step's description
         */
        public abstract String getDescription(); /**
         * Sets this step's stepnumber
         */
        public abstract void setStepNumber(int stepNumber); /**
         * Gets this step's stepnumber
         */
        public abstract int getStepNumber(); /**
         * Sets this step's sequence number
         */
        public abstract void setSequenceNumber(int sequenceNumber); /**
         * Gets this step's sequence number
         */
        public abstract int getSequenceNumber();
}
               
                /* 
                $Log: IStep.java,v $
                Revision 1.2  2004/02/25 10:57:43  nw
                merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

                Revision 1.1.2.2  2004/02/25 10:50:07  nw
                tidied up interfaces

                Revision 1.1.2.1  2004/02/24 15:35:46  nw
                extracted public interface from each implementation class.
                altered types to reference interface rather than implementation whever possible.
                added factory and manager facade at the front
 
                */