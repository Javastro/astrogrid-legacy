/*$Id: RemoteProcessListener.java,v 1.6 2008/09/25 16:02:04 nw Exp $
 * Created on 08-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.net.URI;
import java.util.EventListener;
import java.util.Map;

/** Listener interface to execution of remote processes,
 *
 * Can be used over javarmi by clients that wish to be notified of progress of remote processes

 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 08-Nov-2005
 * @xmlrpc not accessible. - can't support callbacks / listener interfaces.
 *
 */
public interface RemoteProcessListener extends EventListener{
    /** called to notify when the status of the remote process changes
     * 
     * @param executionId id of the remote process in question
     * @param newStatus one of the constants in {@link ExecutionInformation}
     */
    public void statusChanged(URI executionId,String newStatus);
    /** called to notify when a new message is received from the remote process
     * 
     * @param executionId id of the remote process
     * @param msg message received
     */
    public void messageReceived(URI executionId, ExecutionMessage msg);
    /** called to notify that results have been received from the remote process
     * 
     * @param executionId id of the remote process
     * @param results a map of result-name <-> result value.
     */
    public void resultsReceived(URI executionId, Map results);
}


/* 
$Log: RemoteProcessListener.java,v $
Revision 1.6  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.5  2007/07/17 17:04:58  nw
altered so that it extends the listener marker interface.

Revision 1.4  2007/01/24 14:04:44  nw
updated my email address

Revision 1.3  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.2  2005/11/11 10:09:01  nw
improved javadoc

Revision 1.1  2005/11/10 12:13:52  nw
interface changes for lookout.
 
*/