/*$Id: StatusChangeExecutionMessage.java,v 1.3 2006/04/18 23:25:46 nw Exp $
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

import org.astrogrid.acr.astrogrid.ExecutionMessage;

/** internal subtype of execution message - not to be passed across the wire.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Nov-2005
 *
 */
public class StatusChangeExecutionMessage extends ExecutionMessage {
    static final long serialVersionUID = 12345689123456789L;
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
Revision 1.3  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.2.34.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.2  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/