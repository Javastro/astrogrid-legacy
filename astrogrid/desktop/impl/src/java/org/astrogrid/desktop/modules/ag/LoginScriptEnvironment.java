package org.astrogrid.desktop.modules.ag;

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
      Subject grid;
      CommunityPasswordResolver security = new CommunityPasswordResolver();
      
      // Collect principals etc. into a JAAS Subject for the SSO credentials.
      // - nw - moved these variable declarations up, as previously were after first reference to userivorn,
      
      UserIvorn userIvorn = new UserIvorn(community, name, "");
      HomeIvorn homeIvorn = new HomeIvorn(community, name, name + "/");     
      try {
        System.out.println("Logging in using MyProxy...");
        grid = security.checkPassword(userIvorn.toString(),
                                      password,
                                      48*3600, // duration of validity in seconds
                                      trustAnchors);
      }
      catch (CommunityResolverException e) {
        System.out.println("No MyProxy service found; logging in using the AG web-service...");
        security.checkPassword(userIvorn.toString(),
                               password);
        grid = new Subject(); // empty of credentials
      }
      this.guard = new SecurityGuard(grid);


      Subject sso = this.guard.getSsoSubject();
      sso.getPrincipals().add(userIvorn);
      sso.getPrincipals().add(homeIvorn);
      sso.getPrivateCredentials().add(password);
    }

    /**
     * The stored credentials.
     */
    private final SecurityGuard guard;

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getUserIvorn()
     */
    public Ivorn getUserIvorn() {
      return (UserIvorn)this.getFirstPrincipal(this.guard.getSsoSubject(),
                                               UserIvorn.class);
    }

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getHomeIvorn()
     */
    public Ivorn getHomeIvorn() {
      return (HomeIvorn)this.getFirstPrincipal(this.guard.getSsoSubject(),
                                               HomeIvorn.class);
    }

    /**
     * @see org.astrogrid.ui.script.ScriptEnvironment#getPassword()
     */
    public String getPassword() {
      return (String)this.getFirstPrivateCredential(this.guard.getSsoSubject(),
                                                    String.class);
    }

  /**
   * Gets the cryptographic credentials obtained when the user logged in.
   *
   * @return The credentials.
   */
   public SecurityGuard getSecurityGuard() {
     return this.guard;
   }

   /**
    * Extract the first Principal of a type from a JAAS subject.
    * This should be refactored back into SecurityGuard.
    */
   private Object getFirstPrincipal(Subject subject, Class clazz) {
     Object[] a = subject.getPrincipals(clazz).toArray();
     return (a.length > 0)? a[0] : null;
   }

   /**
    * Extract the first rivate credential of a type from a JAAS subject.
    * This should be refactored back into SecurityGuard.
    */
   private Object getFirstPrivateCredential(Subject subject, Class clazz) {
     Object[] a = subject.getPrivateCredentials(clazz).toArray();
     return (a.length > 0)? a[0] : null;
   }

}
