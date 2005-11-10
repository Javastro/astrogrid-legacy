/*$Id: StatusChangeExecutionMessage.java,v 1.1 2005/11/10 12:05:43 nw Exp $
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

import org.astrogrid.acr.astrogrid.ExecutionMessage;

import java.util.Date;

/** internal subtype of execution message - not to be passed across the wire.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Nov-2005
 *
 */
public class StatusChangeExecutionMessage extends ExecutionMessage {

    /** Construct a new StatusChangeExecutionMessage
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */
    public StatusChangeExecutionMessage(String source, String phase, Date arg3) {
        super(source,"information",phase, arg3, "Status changed to " + phase);
    }

}


/* 
$Log: StatusChangeExecutionMessage.java,v $
Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/