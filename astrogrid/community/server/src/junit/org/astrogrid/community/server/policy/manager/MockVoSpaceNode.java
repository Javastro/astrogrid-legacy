package org.astrogrid.community.server.policy.manager;

import java.net.URI;
import org.astrogrid.vospace.v11.client.AbstractDelegate;
import org.astrogrid.vospace.v11.client.exception.ConnectionException;
import org.astrogrid.vospace.v11.client.exception.ContainerException;
import org.astrogrid.vospace.v11.client.exception.LinkException;
import org.astrogrid.vospace.v11.client.exception.NotFoundException;
import org.astrogrid.vospace.v11.client.exception.PermissionException;
import org.astrogrid.vospace.v11.client.exception.RecursiveLinkException;
import org.astrogrid.vospace.v11.client.exception.RequestException;
import org.astrogrid.vospace.v11.client.exception.ResolverException;
import org.astrogrid.vospace.v11.client.exception.ResponseException;
import org.astrogrid.vospace.v11.client.exception.ServiceException;
import org.astrogrid.vospace.v11.client.node.Node;
import org.astrogrid.vospace.v11.client.node.NodeTypeEnum;
import org.astrogrid.vospace.v11.client.vosrn.Vosrn;

/**
 *
 * @author Guy Rixon
 */
public class MockVoSpaceNode implements Node {

  URI uri;

  public MockVoSpaceNode(URI u) {
    uri = u;
  }

  public AbstractDelegate delegate() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public NodeTypeEnum type() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public URI uri() {
    return uri;
  }

  public Vosrn vosrn() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String name() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String path() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void refresh() throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public URI link() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Iterable<Node> nodes() throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
