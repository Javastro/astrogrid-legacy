/*$Id: JesFunctions.java,v 1.1 2004/03/05 16:16:24 nw Exp $
 * Created on 05-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.util;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.workflow.beans.v1.Step;

import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Functions;
import org.apache.commons.jxpath.Pointer;


/** A class of jxpath functions,used to calculate policy decisions */
public class JesFunctions {
    public static final Functions FUNCTIONS = new ClassFunctions(JesFunctions.class,"jes");
    
    public static boolean isPendingStep(ExpressionContext ctxt) {
        Pointer p = ctxt.getContextNodePointer();
        if (p == null) {
            return false;
        }
        Object o = p.getValue();
        if (! (o instanceof Step)) {
            return false;
        }
        Step s = (Step)o;
        int count = s.getStepExecutionRecordCount();
        if (count  == 0) {
            return true;
        } else {
            return s.getStepExecutionRecord(count-1).getStatus().getType() == ExecutionPhase.PENDING_TYPE;
        }
    }
    /** returns true if the current node is a step */
    public static boolean isStep(ExpressionContext ctxt) {
        Pointer p = ctxt.getContextNodePointer();            
        if (p  == null) {
            return false;
        }
        return p.getValue() instanceof Step;
    }
    public static int execCount(ExpressionContext ctxt) {
        Pointer p = ctxt.getContextNodePointer();
        if (p == null) {
            return 0;
        }
        Step step = (Step)p.getValue();
        if (step == null) {
            return 0;
        }
        return step.getStepExecutionRecordCount();
    }
    
    public static String latestStatus(ExpressionContext ctxt) {
        Pointer p = ctxt.getContextNodePointer();
        if (p == null) {
            return "null";
        } 
        Step step = (Step)p.getValue();
        if (step == null) {
            return "null";
        }
        int pos = step.getStepExecutionRecordCount();
        if (pos == 0) {
            return ExecutionPhase.PENDING.toString();
        } else {
            return step.getStepExecutionRecord(pos-1).getStatus().toString();
    }
    }
}

/* 
$Log: JesFunctions.java,v $
Revision 1.1  2004/03/05 16:16:24  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade
 
*/