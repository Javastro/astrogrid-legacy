/*$Id: RemoteProcessStrategy.java,v 1.2 2006/04/18 23:25:44 nw Exp $
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
 * extends scheduled task - assumption is that each process strategy will need to 
 * poll / check progress on a regiular basis - this is handled from the scheduler.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Nov-2005
 *
 */
public interface RemoteProcessStrategy extends ScheduledTask {
    boolean canProcess(URI execId) ;
    boolean canProcess(Document doc);
    
    public URI submit(Document doc) throws ServiceException, SecurityException, NotFoundException,
    InvalidArgumentException;
    
    public URI submitTo(Document doc,URI service) throws ServiceException, SecurityException, NotFoundException,
    InvalidArgumentException;

    public void halt(URI arg0) throws NotFoundException, InvalidArgumentException,
    ServiceException, SecurityException;
    
    public void delete(URI arg0) throws NotFoundException, ServiceException, SecurityException;

    public Map getLatestResults(URI arg0) throws ServiceException, SecurityException, NotFoundException,
    InvalidArgumentException;
    
    /** cause whatever happens in the scheduled task thread to happen again, as soon
     * as possible 
     * (typically used in 'refresh'
     *
     */  
    public void triggerUpdate();

    
}


/* 
$Log: RemoteProcessStrategy.java,v $
Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.34.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/