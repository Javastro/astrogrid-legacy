package org.astrogrid.community.server.policy.manager;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import net.ivoa.vospace.v11.type.NodeType;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.vospace.v11.client.exception.ConnectionException;
import org.astrogrid.vospace.v11.client.exception.ContainerException;
import org.astrogrid.vospace.v11.client.exception.DuplicateException;
import org.astrogrid.vospace.v11.client.exception.LinkException;
import org.astrogrid.vospace.v11.client.exception.NotFoundException;
import org.astrogrid.vospace.v11.client.exception.PermissionException;
import org.astrogrid.vospace.v11.client.exception.RecursiveLinkException;
import org.astrogrid.vospace.v11.client.exception.RequestException;
import org.astrogrid.vospace.v11.client.exception.ResolverException;
import org.astrogrid.vospace.v11.client.exception.ResponseException;
import org.astrogrid.vospace.v11.client.exception.ServiceException;
import org.astrogrid.vospace.v11.client.node.Node;
import org.astrogrid.vospace.v11.client.node.NodeImpl;
import org.astrogrid.vospace.v11.client.node.NodeTypeEnum;
import org.astrogrid.vospace.v11.client.security.SecurityGuardResolver;
import org.astrogrid.vospace.v11.client.system.SystemDelegate;
import org.astrogrid.vospace.v11.client.transfer.export.ExportTransferResponse;
import org.astrogrid.vospace.v11.client.transfer.inport.InportTransferResponse;
import org.astrogrid.vospace.v11.client.vosrn.Vosrn;

/**
 * A Mock delegate for a VOSpace system.
 * This is a fixture for testing the VOSpace adaptor-class in the community.
 * The fixture does almost nothing; the only functional method is
 * {@link AbstractDelegate#create(NodeTypeEnum,URI).
 *
 * @author Guy Rixon
 */
class MockVoSpaceSystemDelegate implements SystemDelegate {

  public SecurityGuard guard() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Node create(NodeTypeEnum type, URI uri) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, DuplicateException, PermissionException {
    return new MockVoSpaceNode(uri);
  }

  public Node create(NodeTypeEnum arg0, Vosrn arg1) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, DuplicateException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Node create(NodeTypeEnum arg0, URI arg1, URI arg2) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, DuplicateException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Node create(NodeTypeEnum arg0, Vosrn arg1, URI arg2) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, DuplicateException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Node select(URI arg0) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Node select(Vosrn arg0) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void delete(URI arg0) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void delete(Vosrn arg0) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Iterable<Node> iterable(URI arg0) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Iterable<Node> iterable(Vosrn arg0) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void refresh(NodeImpl arg0) throws ConnectionException, ServiceException, RequestException, ResponseException, LinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public NodeImpl node(NodeType arg0) throws ResponseException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public InportTransferResponse inport(NodeTypeEnum arg0, URI arg1, URI arg2) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public InportTransferResponse inport(NodeTypeEnum arg0, Vosrn arg1, URI arg2) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public ExportTransferResponse export(URI arg0, URI arg1) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public ExportTransferResponse export(Vosrn arg0, URI arg1) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public InputStream read(URI arg0) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public InputStream read(Vosrn arg0) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public InputStream read(URI arg0, URI arg1) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public InputStream read(Vosrn arg0, URI arg1) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public OutputStream write(URI arg0) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public OutputStream write(Vosrn arg0) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public OutputStream write(NodeTypeEnum arg0, URI arg1, URI arg2) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public OutputStream write(NodeTypeEnum arg0, Vosrn arg1, URI arg2) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public NodeImpl copy(Vosrn arg0, NodeTypeEnum arg1, Vosrn arg2) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public NodeImpl move(Vosrn arg0, NodeTypeEnum arg1, Vosrn arg2) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public SecurityGuardResolver security() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public InportTransferResponse inport(URI arg0) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public InportTransferResponse inport(Vosrn arg0) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public InportTransferResponse inport(NodeTypeEnum arg0, URI arg1) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public InportTransferResponse inport(NodeTypeEnum arg0, Vosrn arg1) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, RecursiveLinkException, ContainerException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public ExportTransferResponse export(URI arg0) throws URISyntaxException, ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public ExportTransferResponse export(Vosrn arg0) throws ResolverException, ConnectionException, ServiceException, RequestException, ResponseException, LinkException, ContainerException, NotFoundException, PermissionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
