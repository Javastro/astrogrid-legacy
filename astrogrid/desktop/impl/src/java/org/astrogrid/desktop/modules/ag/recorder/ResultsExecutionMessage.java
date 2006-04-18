/*$Id: ResultsExecutionMessage.java,v 1.4 2006/04/18 23:25:46 nw Exp $
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

import java.util.Date;

import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;

/** specialized subclass of execution message - just used internally for storing resultts.
 *  - not to be transported over the wire to client.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Nov-2005
 *
 */
public class ResultsExecutionMessage extends ExecutionMessage {
    static final long serialVersionUID = 12345689123456789L;
    /** Construct a new ResultsExecutionMessage
     * @param source
     * @param arg1
     * @param phase
     * @param arg3
     * @param arg4
     */
    public ResultsExecutionMessage(String source,  Date arg3, ResultListType results) {
        super(source, "information", ExecutionInformation.COMPLETED, arg3, "Results");
        this.results =results;
    }
    private final ResultListType results;
    public ResultListType getResults() {
        return results;
    }
}

/* 
$Log: ResultsExecutionMessage.java,v $
Revision 1.4  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.3.34.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.3  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.2  2005/11/10 16:28:26  nw
added result display to vo lookout.

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/