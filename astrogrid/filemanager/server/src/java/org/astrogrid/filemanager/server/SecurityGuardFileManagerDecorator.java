/*$Id: SecurityGuardFileManagerDecorator.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
 * Created on 17-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.server;

import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.FileManagerPortType;
import org.astrogrid.filemanager.common.Ivorn;
import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.common.TransferInfo;

import org.apache.axis.types.URI;

import java.rmi.RemoteException;

/** placeholder class - later will handle the operation of security guard before delegating onto internal implementation.
 * @todo later - add calls to security guard as needed.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2005
 *
 */
public class SecurityGuardFileManagerDecorator implements FileManagerPortType {

    /** Construct a new SecutiryGuardFileManagerDecorator
     * 
     */
    public SecurityGuardFileManagerDecorator(FileManagerPortType fm) {
        super();
        this.fm = fm;
    }
    
    protected final FileManagerPortType fm;

    public Node[] addAccount(AccountIdent ident, BundlePreferences hints) throws RemoteException,
            DuplicateNodeFault, FileManagerFault {
        return this.fm.addAccount(ident, hints);
    }
    public Node[] addNode(NodeIvorn parentIvorn, NodeName newNodeName, NodeTypes nodeType)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {
        return this.fm.addNode(parentIvorn, newNodeName, nodeType);
    }
    public TransferInfo appendContent(NodeIvorn nodeIvorn) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        return this.fm.appendContent(nodeIvorn);
    }
    public Node[] copy(NodeIvorn nodeIvorn, NodeIvorn newParent, NodeName newNodeName, URI newLocation)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {
        return this.fm.copy(nodeIvorn, newParent, newNodeName, newLocation);
    }
    public void copyContentToURL(NodeIvorn nodeIvorn, TransferInfo url) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        this.fm.copyContentToURL(nodeIvorn, url);
    }
    public Node[] copyURLToContent(NodeIvorn nodeIvorn, URI url) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        return this.fm.copyURLToContent(nodeIvorn, url);
    }
    public Node[] delete(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        return this.fm.delete(nodeIvorn);
    }
    public void dummyMessageWorkAroundForAxis(Ivorn ignored, TransferInfo ignoredToo)
            throws RemoteException {
        this.fm.dummyMessageWorkAroundForAxis(ignored, ignoredToo);
    }
    public Node[] getAccount(AccountIdent ident, BundlePreferences hints) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        return this.fm.getAccount(ident, hints);
    }
    public Ivorn getIdentifier() throws RemoteException {
        return this.fm.getIdentifier();
    }
    public Node[] getNode(NodeIvorn nodeIvorn, BundlePreferences hints) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        return this.fm.getNode(nodeIvorn, hints);
    }
    public Node[] move(NodeIvorn nodeIvorn, NodeIvorn newParent, NodeName newNodeName, URI newLocation)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {
        return this.fm.move(nodeIvorn, newParent, newNodeName, newLocation);
    }
    public TransferInfo readContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        return this.fm.readContent(nodeIvorn);
    }
    public Node[] refresh(NodeIvorn nodeIvorn, BundlePreferences hints) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        return this.fm.refresh(nodeIvorn, hints);
    }
    public TransferInfo writeContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        return this.fm.writeContent(nodeIvorn);
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SecurityGuardFileManagerDecorator:");
        buffer.append(" fm: ");
        buffer.append(fm);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: SecurityGuardFileManagerDecorator.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:36  nw
split code inito client and server projoects again.

Revision 1.1.2.1  2005/03/01 15:07:35  nw
close to finished now.

Revision 1.1.2.2  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/18 15:50:14  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)
 
*/