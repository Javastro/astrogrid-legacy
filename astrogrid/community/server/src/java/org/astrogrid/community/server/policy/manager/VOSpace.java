package org.astrogrid.community.server.policy.manager ;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.sso.CredentialStore;
import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.delegate.NodeDelegate;
import org.astrogrid.filemanager.resolver.NodeDelegateResolver;
import org.astrogrid.filemanager.resolver.NodeDelegateResolverImpl;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.store.Ivorn;
import org.astrogrid.vospace.v11.client.node.Node;
import org.astrogrid.vospace.v11.client.node.NodeTypeEnum;
import org.astrogrid.vospace.v11.client.policy.NodePolicyProperty;
import org.astrogrid.vospace.v11.client.system.SystemDelegate;
import org.astrogrid.vospace.v11.client.system.SystemDelegateResolver;
import org.astrogrid.vospace.v11.client.system.SystemDelegateResolverImpl;


/**
 * Manages the VOSpace associated with a user account.
 *
 */
public class VOSpace {

  private static Log log = LogFactory.getLog(VOSpace.class);

  /**
   * The delegate for the FileManager service.
   * This is used when the VOSpace only has the (old) MySpace interface.
   */
  protected NodeDelegate mySpaceDelegate;

  /**
   * The delegate for the VOSpace service.
   */
  protected SystemDelegate vospaceDelegate;
  
  /**
   * The delegate for the registry service.
   */
  protected RegistryService registry;

  /**
   * The hack to pass the credentials into the VOSpace delegate.
   */
  protected final CredentialResolver credentialResolver;

  /**
   * The facade for the cache of user credentials.
   */
  protected CredentialStore credentialStore;

  /**
   * Constructs a VOSpace manager, forcing it to discover the configured
   * VOSpace-service.
   */
  public VOSpace() throws CommunityServiceException {
    // Create a registry delegate using the configuration to discover
    // the registry service.
    this(RegistryDelegateFactory.createQueryv1_0());
  }

  /**
   * Constructs a VOSpace manager, using a given MySpace delegate.
   * The delegate is already associated with the VOSpace service of choice.
   */
  public VOSpace(NodeDelegate delegate) {
    mySpaceDelegate = delegate;
    credentialResolver = new CredentialResolver();
  }

  /**
   * Constructs a VOSpace manager using a given VOSpace delegate.
   */
  public VOSpace(SystemDelegate     delegate,
                 CredentialResolver resolver) throws CommunityServiceException {
    vospaceDelegate = delegate;
    credentialResolver = resolver;
  }

  /**
   * Constructs a VOSpace manager given a regstry delegate,
   * The delegate allows the VOSpace manager to look up the configured
   * VOSpace service and to construct from it either a FileManager delegate
   * of a VOSpace delegate.
   */
  public VOSpace(RegistryService registry) throws CommunityServiceException {
    this.registry = registry;
    credentialResolver = new CredentialResolver();
    URI u = new CommunityConfiguration().getVoSpaceIvorn();
    if (u.getScheme().equals("vos")) {
      createVoSpaceDelegate();
    }
    else if (u.getScheme().equals("ivo")) {
      createMySpaceDelegate(uriToIvorn(u));
    }
    else {
      throw new CommunityServiceException(
          "Can't find the VOSpace. Please specify a VOSpace folder or a MySpace service."
      );
    }
  }

    
    /**
     * Allocates home space in MySpace for an Account.
     * If a home space for the given account already exists then it is 
     * destroyed and a new, empty space is created.
     *
     * @param account The AccountData to update.
     * @throws CommunityServiceException If the service is unable to allocate the space.
     */
    public String allocateSpace(String        accountIvorn,
                                String        userName,
                                SecurityGuard credentials) throws CommunityServiceException {
      assert accountIvorn != null;
      assert userName != null;
      log.debug("Allocating VOSpace for" + userName);

      String homeSpaceLocation = null;

      try {

        // Call FileManager client to create the space.
        if (mySpaceDelegate != null) {
          AccountIdent fmAccount = new AccountIdent(accountIvorn);
          FileManagerNode node = mySpaceDelegate.addAccount(fmAccount);
          homeSpaceLocation = node.getMetadata().getNodeIvorn().toString();
        }

        // Call the VOSpace delegate to create the space. This makes the
        // new space private to the community member.
        else if (vospaceDelegate != null) {
          URI u1 = new CommunityConfiguration().getVoSpaceIvorn();
          URI u2 = new URI(String.format("%s/%s", u1, userName));
          log.debug(String.format("New home-space is at %s", u2));
          synchronized(credentialResolver) {
            credentialResolver.setCurrent(credentials);
            Node n =
                vospaceDelegate.create(NodeTypeEnum.TREE_NODE, 
                                       u2,
                                       NodePolicyProperty.PRIVATE);
            homeSpaceLocation = n.uri().toString();
          }

        }

        log.info("Home space for account " +
                 userName +
                 " has been created at " +
                 homeSpaceLocation);
        return homeSpaceLocation;
      }
      catch (Exception ouch) {
        log.error(ouch.getMessage());
        throw new CommunityServiceException("Unable to create VoSpace",
                                            ouch);
      }
    
  }


  protected void createVoSpaceDelegate() throws CommunityServiceException {
    if (registry == null) {
      throw new CommunityServiceException(
          "Can't look up the VOSpace service: "+
          "no registry delegate is available."
      );
    }

    SystemDelegateResolver resolver = new SystemDelegateResolverImpl(registry);
    vospaceDelegate = resolver.resolve(credentialResolver);
  }

  protected void createMySpaceDelegate(Ivorn i) throws CommunityServiceException {
    try {
      NodeDelegateResolver resolver = new NodeDelegateResolverImpl(null);
      mySpaceDelegate = resolver.resolve(i);
    }
    catch (Exception e) {
      throw new CommunityServiceException(e);
    }
  }

  /**
   * Creates an IVORN object from the IVORN as URI.
   *
   * @param u The IVORN as a URI
   * @return The IVORN as an object.
   * @throws RuntimeException If the URI is not a valid IVORN.
   */
  protected Ivorn uriToIvorn(URI u) {
    try {
      return new Ivorn(u.toString());
    } catch (URISyntaxException ex) {
      throw new RuntimeException(u + " is not a valid IVORN");
    }
  }
  
}
