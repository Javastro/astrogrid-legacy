/*$Id: NodeIterator.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
 * Created on 11-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.client;

import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeNotFoundFault;

import java.rmi.RemoteException;
import java.util.Iterator;


/**
 * An iterator for child nodes.
 *<p>
 *Supports the standard methods of {@link java.util.Iterator} (including remove) and adds a strongly-typed {@link #nextNode()} method, that
 *returns checked exceptions. The {@link java.util.Iterator#next()} method wraps these as runtime exceptions to work around restrictions in the method type.
 *  @modified nww - moved out into new file, as then becomes easier to refer to this type in client code.
 */
public interface NodeIterator extends Iterator  {

    /**
     * Get the next node in the iteration.
     * @throws RemoteException
     * 
     * @throws NodeNotFoundException
     *             If the node is no longer in the server database (this can
     *             happen if another client deletes the node after this
     *             iterator was created).
     * @throws FileManagerServiceException
     *             If a problem occurs when handling the request.
     *  
     */
    public FileManagerNode nextNode() throws NodeNotFoundFault,
            FileManagerFault, RemoteException;

}

/* 
$Log: NodeIterator.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.4  2005/03/01 15:07:30  nw
close to finished now.

Revision 1.1.2.3  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.2  2005/02/18 15:50:15  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)

Revision 1.1.2.1  2005/02/11 14:27:52  nw
refactored, split out candidate classes.
 
*/