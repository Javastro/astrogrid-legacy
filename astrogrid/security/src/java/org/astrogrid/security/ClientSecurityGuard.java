package org.astrogrid.security;

import java.util.List;
import java.util.Hashtable;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.Service;
import org.astrogrid.community.client.security.service.SecurityServiceDelegate;
import org.astrogrid.community.resolver.security.service.SecurityServiceResolver;
import org.astrogrid.store.Ivorn;

/**
 * Access to the security tokens pertaining to the client side of
 * web-service operations.
 *
 * A client application may call this class to add security tokens to
 * outgoing requests to a SOAP service. A web-service implementation
 * cannot use this class to manipulate tokens passed to it by a client.
 * However, if service A wishes to act as a client of subsidiary service
 * B, then A may use this class. See the package description for more
 * details.
 *
 * A client should create one instance of this class for each identity
 * it needs to use. E.g., if the client wants to invoke one service in
 * the name of an end-user and a second in the the name of a community,
 * then it needs two Guards.  However, if the client wants to apply
 * the same credentials to many services, then it only needs one Guard.
 * If some of the credentials change between invocations of services
 * an existing guard should be reused and reprogrammed. Guards are
 * created using the default, no-argument constructor.
 *
 * To set the guard on a web-service endpoint, the client must obtain
 * a JAX-RPC javax.xml.rpc.Service representing the endpoint and then
 * call {@link mountGuard}, passing the Service as a
 * parameter. The client must do this only once for any given Service.
 * This method sets up the JAX-RPC message-handlers that embed the
 * credentials in the outgoing message.
 *
 * The client sets the credentials by setting Java-bean properties of
 * the SecurityGuard (i.e. the parent class). After the guard is set,
 * each outgoing message is augmented with credentials matching the
 * current settings in the Guard.
 *
 * The convenience method {@link signOn} implements single sign-on to
 * a network using an initial user-name and password. This method signs
 * on to some portal that supplies the credentials for subsequent access
 * to services; these credentials are then stored in the properties of
 * the security guard. Currently, the credentials are a {@link NonceToken}
 * from the AstroGrid community service.
 *
 * @see {@link ClientCredentialHandler}
 * @see {@link SecurityGuard}
 * @see javax.xml.rpc.Service
 *
 * @author Guy Rixon
 */
public class ClientSecurityGuard extends SecurityGuard {

  /**
   * Sets a guard on a web-service endpoint.
   * After this method completes, all messages to that
   * endpoint carry security credentials.
   * This sets up the message handlers for the endpoint;
   * this must be done exactly once.
   *
   * @param service the endpoint to be guarded
   * @param port the QName for the port to be guarded
   */
  public void mountGuard (Service service, QName port) {

    // Create the configuration for the handler. This is always the
    // same and applies to any number of web-service port-types.
    Hashtable config = new Hashtable();
    config.put("Subject", this.getSubject());
    QName[] handlers = new QName[] {};
    HandlerInfo info = new HandlerInfo(ClientCredentialHandler.class,
                                       config,
                                       handlers);

    // Get the JAX-RPC handler-chain for the port and add the
    // credentials handler from this package.
    HandlerRegistry reg = service.getHandlerRegistry();
    System.out.println("Setting the credential handler for " + port);
    List chain = reg.getHandlerChain(port);
    chain.add(info);
  }


  /**
   * Signs on to a network, retrieving credentials for subsequent use
   * of that network. Signs on with the current values of the Username
   * and Password properties; updates the Username and Password properties.
   *
   * @throws Exception if the sign-on fails
   */
  public void signOn () throws Exception {
    String   u = this.getUsername();
    Password p = this.getPassword();
    System.out.println("ClientSecurityGuard.signOn(): signing on to the grid"
                     + " with account name "
                     + u);
    try {
      Ivorn accountId = new Ivorn(this.getUsername());
      SecurityServiceResolver ssr = new SecurityServiceResolver();
      SecurityServiceDelegate ssd = ssr.resolve(accountId);

      NonceToken t
          = new NonceToken(ssd.checkPassword(u, p.getPlainPassword()));
      this.setNonceToken(t);
    }
    catch (Exception e) {
      throw new Exception("Failed to sign on to the grid as " + u, e);
    }
  }

}
