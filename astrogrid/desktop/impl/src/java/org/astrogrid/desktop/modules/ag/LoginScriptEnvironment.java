package org.astrogrid.desktop.modules.ag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.security.auth.Subject;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.CommunityPasswordResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.store.Ivorn;

/** Implementation of ScriptEnvironment that enforces login --
 * a bit crap, but will have to do until we have proper security.
 * @todo move to separate subprtoject
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Dec-2004
 *
 */
public class LoginScriptEnvironment implements ScriptEnvironment {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(LoginScriptEnvironment.class);

    /** Construct a new LoginScriptEnvironment
     * @throws RegistryException
     * @throws CommunityIdentifierException
     * @throws CommunityResolverException
     *
     */
    LoginScriptEnvironment(String name,
                           String community,
                           String password,
                           String trustAnchors)
        throws CommunityResolverException,
               CommunityServiceException,
               CommunitySecurityException,
               CommunityIdentifierException,
               RegistryException {

     // Establish grid credentials by logging on at a MyProxy service.
     // Not all communities have these services yet; trying to use a
     // non-existant MyProxy service causes a CommunityResolverException.
     // In this case, use the older method of logging on to a web service
     // at the community.
     UserIvorn userIvorn = new UserIvorn(community, name, "");
     HomeIvorn homeIvorn = new HomeIvorn(community, name, name + "/"); 
     Subject subject;
     CommunityPasswordResolver security = new CommunityPasswordResolver(); 
     try {
       logger.info("Logging in using MyProxy...");
       subject = security.checkPassword(userIvorn.toString(),
                                        password,
                                        48*3600, // duration of validity in seconds
                                        trustAnchors);
     }
     catch (CommunityResolverException e) {
       logger.info("No MyProxy service found; logging in using the AG web-service...");
       security.checkPassword(userIvorn.toString(),
                              password);
       subject = new Subject(); // empty of credentials
     }
     this.guard = new SecurityGuard(subject);

     this.guard.getSubject().getPrincipals().add(userIvorn);
     this.guard.getSubject().getPrincipals().add(homeIvorn);
     this.guard.getSubject().getPrivateCredentials().add(password);
   }

    /**
     * The stored credentials.
     */
    private final SecurityGuard guard;

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getUserIvorn()
     */
    public Ivorn getUserIvorn() {
      return (UserIvorn)this.guard.getFirstPrincipal(UserIvorn.class);
    }

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getHomeIvorn()
     */
    public Ivorn getHomeIvorn() {
      return (HomeIvorn)this.guard.getFirstPrincipal(HomeIvorn.class);
    }

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getPassword()
     */
    public String getPassword() {
      return (String)this.guard.getFirstPrivateCredential(String.class);
    }

  /**
   * Gets the cryptographic credentials obtained when the user logged in.
   *
   * @return The credentials.
   */
   public SecurityGuard getSecurityGuard() {
     return this.guard;
   }

}