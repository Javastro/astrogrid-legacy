package org.astrogrid.community.server.policy.manager;

import java.net.URI;
import java.util.Date;
import org.astrogrid.vospace.v11.VOS11Const;
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
import org.astrogrid.vospace.v11.client.property.Property;
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

  public void refresh() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public URI link() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Iterable<Node> nodes() throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Iterable<Property> properties() throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Property property(VOS11Const arg0) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Property property(URI arg0) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void property(URI arg0, String arg1) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Date created() throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Date modified() throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public long size() throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
