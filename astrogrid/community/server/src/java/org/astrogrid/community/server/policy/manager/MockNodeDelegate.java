package org.astrogrid.community.server.policy.manager;

import org.apache.axis.types.URI;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.delegate.AxisNodeWrapper;
import org.astrogrid.filemanager.client.delegate.NodeDelegate;
import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.common.Ivorn;
import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.common.TransferInfo;

/**
 * Mock implementation of NodeDelegate, for unit testing of community service.
 * 
 *
 * @author Guy Rixon
 */
public class MockNodeDelegate implements NodeDelegate {
  
  /**
   * Constructs a MockNodeDelegate.
   */
  public MockNodeDelegate() {
  }

  public FileManagerNode addAccount(AccountIdent accountIdent) {
    Node node = new Node();
    node.setIvorn(new NodeIvorn("ivo://org.astrogrid.ficticious/vospace#/foo"));
    return new AxisNodeWrapper(node, this);
  }

  public FileManagerNode addNode(NodeIvorn nodeIvorn, NodeName nodeName, NodeTypes nodeTypes) {
    return null;
  }

  public TransferInfo appendContent(NodeIvorn nodeIvorn) {
    return null;
  }

  public FileManagerNode copy(NodeIvorn nodeIvorn, NodeIvorn nodeIvorn0, NodeName nodeName, URI uRI) {
    return null;
  }

  public void copyContentToURL(NodeIvorn nodeIvorn, TransferInfo transferInfo) {
  }

  public void copyURLToContent(NodeIvorn nodeIvorn, URI uRI) {
  }

  public void delete(NodeIvorn nodeIvorn) {
  }

  public FileManagerNode getAccount(AccountIdent accountIdent) {
    Node node = new Node();
    return new AxisNodeWrapper(node, this);
  }

  public Ivorn getIdentifier() {
    return null;
  }

  public FileManagerNode getNode(NodeIvorn nodeIvorn) {
    return null;
  }

  public FileManagerNode getNodeIgnoreCache(NodeIvorn nodeIvorn) {
    return null;
  }

  public void move(NodeIvorn nodeIvorn, NodeIvorn nodeIvorn0, NodeName nodeName, URI uRI) {
  }

  public TransferInfo readContent(NodeIvorn nodeIvorn) {
    return null;
  }

  public void transferCompleted(NodeIvorn nodeIvorn) {
  }

  public TransferInfo writeContent(NodeIvorn nodeIvorn) {
    return null;
  }

  public void clearCache() {
  }
  
}
