/*$Id: RemoteProcessStrategy.java,v 1.1 2005/11/10 12:05:43 nw Exp $
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

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;

import java.net.URI;
import java.util.Map;

/** interface to something that knows how to handle a certain flavour of remote process.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Nov-2005
 *
 */
public interface RemoteProcessStrategy {
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
    
}


/* 
$Log: RemoteProcessStrategy.java,v $
Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/