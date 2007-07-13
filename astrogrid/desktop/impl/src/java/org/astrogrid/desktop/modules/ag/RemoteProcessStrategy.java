/*$Id: RemoteProcessStrategy.java,v 1.5 2007/07/13 23:14:53 nw Exp $
 * Created on 08-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.util.Map;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.system.ScheduledTask;
import org.w3c.dom.Document;

/** interface to something that knows how to handle a certain flavour of remote process.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 08-Nov-2005
 *
 */
public interface RemoteProcessStrategy{
    boolean canProcess(URI execId) ;
    /** dual-use method, for efficiency.
     * returns null if can't process. If can process, return value is used for snitching - extract some kind of name from the document
     * 
     *  @return the application being invoked / workflow name / somesuch.*/
    String canProcess(Document doc);
    
    
    public ProcessMonitor submit(Document doc) throws ServiceException, SecurityException, NotFoundException,
    InvalidArgumentException;
    
    public ProcessMonitor submitTo(Document doc,URI service) throws ServiceException, SecurityException, NotFoundException,
    InvalidArgumentException;


    
}


/* 
$Log: RemoteProcessStrategy.java,v $
Revision 1.5  2007/07/13 23:14:53  nw
Complete - task 1: task runner

Complete - task 54: Rewrite remoteprocess framework

Revision 1.4  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.3  2006/05/26 15:23:45  nw
implemented snitching.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.34.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/