/*$Id: CautiousFileManagerDecorator.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
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

import org.apache.axis.AxisFault;
import org.apache.axis.types.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.rmi.RemoteException;

/**
 * Decorator for a file manager that logs method entries, catches all extraneous
 * throwables, etc. Allows the coreFileManager to concentrate on the logic, and so is a little more concise.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2005
 *  
 */
public class CautiousFileManagerDecorator implements FileManagerPortType {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CautiousFileManagerDecorator.class);

    public Node[] addAccount(AccountIdent ident, BundlePreferences hints) throws RemoteException,
            DuplicateNodeFault, FileManagerFault {
        if (logger.isDebugEnabled()) {
            logger.debug("addAccount(ident = " + ident + ", hints = " + hints + ") - start");
        }

        try {
            Node[] returnNodeArray = this.fm.addAccount(ident, hints);
            if (logger.isDebugEnabled()) {
                logger.debug("addAccount() - end");
            }
            return returnNodeArray;
        } catch (RuntimeException t) {
            logger.error("addAccount(AccountIdent, BundlePreferences)", t);

            throw AxisFault.makeFault(t);
        }
    }

    public Node[] addNode(NodeIvorn parentIvorn, NodeName newNodeName, NodeTypes nodeType)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("addNode(parentIvorn = " + parentIvorn + ", newNodeName = " + newNodeName
                    + ", nodeType = " + nodeType + ") - start");
        }

        try {
            Node[] returnNodeArray = this.fm.addNode(parentIvorn, newNodeName, nodeType);
            if (logger.isDebugEnabled()) {
                logger.debug("addNode() - end");
            }
            return returnNodeArray;
        } catch (RuntimeException t) {
            logger.error("addNode(NodeIvorn, NodeName, NodeTypes)", t);

            throw AxisFault.makeFault(t);
        }
    }

    public TransferInfo appendContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("appendContent(nodeIvorn = " + nodeIvorn + ") - start");
        }

        try {
            TransferInfo returnURI = this.fm.appendContent(nodeIvorn);
            if (logger.isDebugEnabled()) {
                logger.debug("appendContent() - end");
            }
            return returnURI;
        } catch (RuntimeException t) {
            logger.error("appendContent(NodeIvorn)", t);

            throw AxisFault.makeFault(t);
        }
    }

    public Node[] copy(NodeIvorn nodeIvorn, NodeIvorn newParent, NodeName newNodeName, URI newLocation)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("copy(nodeIvorn = " + nodeIvorn + ", newParent = " + newParent
                    + ", newNodeName = " + newNodeName + ", newLocation = " + newLocation
                    + ") - start");
        }

        try {
            Node[] returnNodeArray = this.fm.copy(nodeIvorn, newParent, newNodeName, newLocation);
            if (logger.isDebugEnabled()) {
                logger.debug("copy() - end");
            }
            return returnNodeArray;
        } catch (RuntimeException t) {
            logger.error("copy(NodeIvorn, NodeIvorn, NodeName, Ivorn)", t);

            throw AxisFault.makeFault(t);
        }
    }

    public void copyContentToURL(NodeIvorn nodeIvorn, TransferInfo url) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("copyContentToURL(nodeIvorn = " + nodeIvorn + ", url = " + url
                    + ") - start");
        }

        try {
            this.fm.copyContentToURL(nodeIvorn, url);
        } catch (RuntimeException t) {
            logger.error("copyContentToURL(NodeIvorn, URI)", t);

            throw AxisFault.makeFault(t);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("copyContentToURL() - end");
        }
    }

    public Node[] copyURLToContent(NodeIvorn nodeIvorn, URI url) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("copyURLToContent(nodeIvorn = " + nodeIvorn + ", url = " + url
                    + ") - start");
        }

        try {
            Node[] returnNodeArray = this.fm.copyURLToContent(nodeIvorn, url);
            if (logger.isDebugEnabled()) {
                logger.debug("copyURLToContent() - end");
            }
            return returnNodeArray;
        } catch (RuntimeException t) {
            logger.error("copyURLToContent(NodeIvorn, URI)", t);

            throw AxisFault.makeFault(t);
        }
    }

    public Node[] delete(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("delete(nodeIvorn = " + nodeIvorn + ") - start");
        }

        try {
            Node[] returnNodeArray = this.fm.delete(nodeIvorn);
            if (logger.isDebugEnabled()) {
                logger.debug("delete() - end");
            }
            return returnNodeArray;
        } catch (RuntimeException t) {
            logger.error("delete(NodeIvorn)", t);

            throw AxisFault.makeFault(t);
        }
    }

    public Node[] getAccount(AccountIdent ident, BundlePreferences hints) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("getAccount(ident = " + ident + ", hints = " + hints + ") - start");
        }

        try {
            Node[] returnNodeArray = this.fm.getAccount(ident, hints);
            if (logger.isDebugEnabled()) {
                logger.debug("getAccount() - end");
            }
            return returnNodeArray;
        } catch (RuntimeException t) {
            logger.error("getAccount(AccountIdent, BundlePreferences)", t);

            throw AxisFault.makeFault(t);
        }
    }

    public Ivorn getIdentifier() throws RemoteException {
        if (logger.isDebugEnabled()) {
            logger.debug("getIdentifier() - start");
        }

        try {
            Ivorn returnIvorn = this.fm.getIdentifier();
            if (logger.isDebugEnabled()) {
                logger.debug("getIdentifier() - end");
            }
            return returnIvorn;
        } catch (RuntimeException t) {
            logger.error("getIdentifier()", t);

            throw AxisFault.makeFault(t);
        }
    }

    public Node[] getNode(NodeIvorn nodeIvorn, BundlePreferences hints) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("getNode(nodeIvorn = " + nodeIvorn + ", hints = " + hints + ") - start");
        }
       if (nodeIvorn == null || nodeIvorn.getValue() == null || nodeIvorn.getValue().getFragment() == null) {
           throw new NodeNotFoundFault(nodeIvorn == null ? "" : nodeIvorn.toString());
       }
        try {
            Node[] returnNodeArray = this.fm.getNode(nodeIvorn, hints);
            if (logger.isDebugEnabled()) {
                logger.debug("getNode() - end");
            }
            return returnNodeArray;
        } catch (RuntimeException t) {
            logger.error("getNode(NodeIvorn, BundlePreferences)", t);

            throw AxisFault.makeFault(t);
        }
    }

    public Node[] move(NodeIvorn nodeIvorn, NodeIvorn newParent, NodeName newNodeName, URI newLocation)
            throws RemoteException, DuplicateNodeFault, FileManagerFault, NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("move(nodeIvorn = " + nodeIvorn + ", newParent = " + newParent
                    + ", newNodeName = " + newNodeName + ", newLocation = " + newLocation
                    + ") - start");
        }

        try {
            Node[] returnNodeArray = this.fm.move(nodeIvorn, newParent, newNodeName, newLocation);
            if (logger.isDebugEnabled()) {
                logger.debug("move() - end");
            }
            return returnNodeArray;
        } catch (RuntimeException t) {
            logger.error("move(NodeIvorn, NodeIvorn, NodeName, Ivorn)", t);

            throw AxisFault.makeFault(t);
        }
    }

    public TransferInfo readContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("readContent(nodeIvorn = " + nodeIvorn + ") - start");
        }

        try {
            TransferInfo returnURI = this.fm.readContent(nodeIvorn);
            if (logger.isDebugEnabled()) {
                logger.debug("readContent() - end");
            }
            return returnURI;
        } catch (RuntimeException t) {
            logger.error("readContent(NodeIvorn)", t);

            throw AxisFault.makeFault(t);
        }
    }

    public Node[] refresh(NodeIvorn nodeIvorn, BundlePreferences hints) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("refresh(nodeIvorn = " + nodeIvorn + ", hints = " + hints + ") - start");
        }

        try {
            Node[] returnNodeArray = this.fm.refresh(nodeIvorn, hints);
            if (logger.isDebugEnabled()) {
                logger.debug("refresh() - end");
            }
            return returnNodeArray;
        } catch (RuntimeException t) {
            logger.error("refresh(NodeIvorn, BundlePreferences)", t);

            throw AxisFault.makeFault(t);
        }
    }

    public TransferInfo writeContent(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        if (logger.isDebugEnabled()) {
            logger.debug("writeContent(nodeIvorn = " + nodeIvorn + ") - start");
        }

        try {
            TransferInfo returnURI = this.fm.writeContent(nodeIvorn);
            if (logger.isDebugEnabled()) {
                logger.debug("writeContent() - end");
            }
            return returnURI;
        } catch (RuntimeException t) {
            logger.error("writeContent(NodeIvorn)", t);

            throw AxisFault.makeFault(t);
        }

    }

    /**
     * Construct a new CautiousFileManagerDecorator
     *  
     */
    public CautiousFileManagerDecorator(FileManagerPortType fm) {
        super();
        this.fm = fm;
    }

    protected final FileManagerPortType fm;

    /**
     * @see org.astrogrid.filemanager.common.FileManagerPortType#dummyMessageWorkAroundForAxis(org.astrogrid.filemanager.common.Ivorn, org.astrogrid.filemanager.common.TransferInfo)
     */
    public void dummyMessageWorkAroundForAxis(Ivorn ignored, TransferInfo ignoredToo) throws RemoteException {
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[CautiousFileManagerDecorator:");
        buffer.append(" fm: ");
        buffer.append(fm);
        buffer.append("]");
        return buffer.toString();
    }
}

/*
 * $Log: CautiousFileManagerDecorator.java,v $
 * Revision 1.2  2005/03/11 13:37:06  clq2
 * new filemanager merged with filemanager-nww-jdt-903-943
 *
 * Revision 1.1.2.1  2005/03/01 23:43:36  nw
 * split code inito client and server projoects again.
 *
 * Revision 1.1.2.1  2005/03/01 15:07:35  nw
 * close to finished now.
 *
 * Revision 1.1.2.2  2005/02/27 23:03:12  nw
 * first cut of talking to filestore
 *
 * Revision 1.1.2.1  2005/02/18 15:50:14  nw
 * lots of changes.
 * introduced new schema-driven soap binding, got soap-based unit tests
 * working again (still some failures)
 *
 */