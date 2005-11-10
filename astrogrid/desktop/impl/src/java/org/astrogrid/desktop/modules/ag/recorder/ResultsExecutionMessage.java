/*$Id: ResultsExecutionMessage.java,v 1.1 2005/11/10 12:05:43 nw Exp $
 * Created on 09-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.recorder;

import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;

import java.util.Date;
import java.util.Map;

/** specialized subclass of execution message - just used internally for storing resultts.
 *  - not to be transported over the wire to client.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Nov-2005
 *
 */
public class ResultsExecutionMessage extends ExecutionMessage {

    /** Construct a new ResultsExecutionMessage
     * @param source
     * @param arg1
     * @param phase
     * @param arg3
     * @param arg4
     */
    public ResultsExecutionMessage(String source,  Date arg3, Map results) {
        super(source, "information", ExecutionInformation.COMPLETED, arg3, "Results");
        this.results =results;
    }
    private final Map results;
    public Map getResults() {
        return results;
    }

}


/* 
$Log: ResultsExecutionMessage.java,v $
Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/