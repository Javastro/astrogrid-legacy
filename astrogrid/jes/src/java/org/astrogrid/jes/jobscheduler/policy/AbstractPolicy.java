/*$Id: AbstractPolicy.java,v 1.1 2004/03/05 16:16:23 nw Exp $
 * Created on 05-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.policy;

import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.util.JesFunctions;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Mar-2004
 *
 */
public abstract class AbstractPolicy implements Policy {
    protected static final Log logger = LogFactory.getLog("Policy");
    
    /** Construct a new AbstractPolicy
     * 
     */
    public AbstractPolicy() {
        super();
    }

    /** inefficient to do this on every call, will need to refactor later*/
    protected void registerFunctions(Workflow wf) {
        wf.addFunctions(JesFunctions.FUNCTIONS);
    }

}


/* 
$Log: AbstractPolicy.java,v $
Revision 1.1  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade
 
*/