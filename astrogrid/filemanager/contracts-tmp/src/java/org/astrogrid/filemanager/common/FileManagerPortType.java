/**
 * FileManagerPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.filemanager.common;

public interface FileManagerPortType extends java.rmi.Remote {

    // dumy method, work around for a wsdl2java bug, forces classes to be
    // generated
    public void dummyMessageWorkAroundForAxis(org.astrogrid.filemanager.common.Ivorn ignored, org.astrogrid.filemanager.common.TransferInfo ignoredToo) throws java.rmi.RemoteException;

    // access a node in the tree
    public org.astrogrid.filemanager.common.Node[] getNode(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.astrogrid.filemanager.common.BundlePreferences hints) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;

    // Add a new account
    public org.astrogrid.filemanager.common.Node[] addAccount(org.astrogrid.filemanager.common.AccountIdent ident, org.astrogrid.filemanager.common.BundlePreferences hints) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.DuplicateNodeFault, org.astrogrid.filemanager.common.FileManagerFault;

    // get node for an account
    public org.astrogrid.filemanager.common.Node[] getAccount(org.astrogrid.filemanager.common.AccountIdent ident, org.astrogrid.filemanager.common.BundlePreferences hints) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;

    // Retreive Ivorn for this service
    public org.astrogrid.filemanager.common.Ivorn getIdentifier() throws java.rmi.RemoteException;

    // delete a node
    public org.astrogrid.filemanager.common.Node[] delete(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;

    // read contents of a node
    public org.astrogrid.filemanager.common.TransferInfo readContent(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;

    // write contents of a node
    public org.astrogrid.filemanager.common.TransferInfo writeContent(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;

    // write contents of a node
    public org.astrogrid.filemanager.common.TransferInfo appendContent(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;

    // write a url resource into the contents of this node
    public org.astrogrid.filemanager.common.Node[] copyURLToContent(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.apache.axis.types.URI url) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;

    // write contents of a node to a url resource
    public void copyContentToURL(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.astrogrid.filemanager.common.TransferInfo url) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;
    public org.astrogrid.filemanager.common.Node[] move(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.astrogrid.filemanager.common.NodeIvorn newParent, org.astrogrid.filemanager.common.NodeName newNodeName, org.apache.axis.types.URI newLocation) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.DuplicateNodeFault, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;
    public org.astrogrid.filemanager.common.Node[] copy(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.astrogrid.filemanager.common.NodeIvorn newParent, org.astrogrid.filemanager.common.NodeName newNodeName, org.apache.axis.types.URI newLocation) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.DuplicateNodeFault, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;

    // check this node for changes.
    public org.astrogrid.filemanager.common.Node[] refresh(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.astrogrid.filemanager.common.BundlePreferences hints) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;

    // add a new node
    public org.astrogrid.filemanager.common.Node[] addNode(org.astrogrid.filemanager.common.NodeIvorn parentIvorn, org.astrogrid.filemanager.common.NodeName newNodeName, org.astrogrid.filemanager.common.NodeTypes nodeType) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.DuplicateNodeFault, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault;
}
