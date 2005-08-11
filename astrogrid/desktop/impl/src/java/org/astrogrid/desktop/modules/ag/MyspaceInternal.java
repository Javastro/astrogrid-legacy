/*$Id: MyspaceInternal.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 02-Aug-2005
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
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.filemanager.client.FileManagerNode;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Aug-2005
 *
 */
public interface MyspaceInternal extends Myspace {

    /**
     * @param ivorn
     * @return
     */
    public FileManagerNode node(URI ivorn) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException ;

    public InputStream getInputStream(URI uri) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException;
    public OutputStream getOutputStream(URI ui) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException;
    
}


/* 
$Log: MyspaceInternal.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/