package org.astrogrid.security.jaas;

import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import org.astrogrid.community.client.security.service.SecurityServiceDelegate;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.resolver.security.service.SecurityServiceResolver;
import org.astrogrid.security.AccountName;
import org.astrogrid.security.NonceToken;
import org.astrogrid.store.Ivorn;


/**
 * A JAAS log-in module implementing AstroGrid's nonce-token protocol.
 * protocol. This class is called by the JAAS framework when an application
 * calls a LoginContext that is configured for NonceTokens. Application code
 * should not need to call this class directly (except for unit tests).
 *
 * @author Guy Rixon
 */
public class NonceTokenCheck implements LoginModule {

  /**
   * The identity and a priori credentials of the party
   * to be authenticated.
   */
  Subject subject;

  /**
   * The authenticated identity of the party.
   */
  AccountName account;


  /**
   * States the subject of the authentication and sets
   * shared authentication state (no shared state is used
   * in this implementation.
   *
   * @param subject contains identity and credentials for the
   * party to be authenticated
   * @param callback not used with NonceTokens
   * @param sharedState not used with NonceTokens
   * @param options not used in NonceTokens
   */
  public void initialize (Subject         subject,
                          CallbackHandler callback,
                          Map             sharedState,
                          Map             options) {
    this.subject = subject;
  }


  /**
   * Authenticates the subject set at initialization.
   * If authentication succeeds, the NonceToken in the Subject
   * is updated.
   *
   * @return true if the authentication succeeded,
   * or false if this LoginModule should be ignored
   * @throws LoginException if authentication fails
   */
  public boolean login () throws LoginException {

    // Extract the original token from the context.
    // No tokens means no authentication.
    // Treat multiple tokens as an error, for simplicity.
    NonceToken oldToken = null;
    NonceToken newToken = null;
    Set tokens = this.subject.getPrivateCredentials(NonceToken.class);
    if (tokens.size() == 0) {
      throw new LoginException("No nonce tokens were presented.");
    }
    else if (tokens.size() > 1) {
      throw new LoginException("Too many nonce tokens were presented.");
    }
    else {
      oldToken = (NonceToken) tokens.iterator().next();
      this.account = new AccountName(oldToken.getAccount());
    }

    // Check the original token in the community service.
    // This can cause several types of exception to be thrown.
    try {
      Ivorn accountId = new Ivorn(oldToken.getAccount());
      SecurityServiceResolver ssr = new SecurityServiceResolver();
      SecurityServiceDelegate ssd = ssr.resolve(accountId);
      System.out.println("Existing token: " + oldToken.toString());
      newToken = new NonceToken(ssd.checkToken(oldToken));
      System.out.println("Returned token: " + newToken.toString());
    }
    catch (CommunitySecurityException e1) {
      throw new FailedLoginException("Authentication with nonce token failed; "
                                   + "nonce is invalid");
    }
    catch (CommunityIdentifierException e2) {
          throw new FailedLoginException("Authentication with nonce token failed; "
                                       + "account identifier is invalid");
    }
    catch (Exception e3) {
      throw new LoginException("Could not validate the nonce token; " +
                               "problems with the community service: " +
                               e3);
    }

    // Store the new token in the Subject; it replaces the old token.
    assert(newToken != null);
    Set credentials = this.subject.getPrivateCredentials();
    credentials.remove(oldToken);
    credentials.add(newToken);
    return true;
  }


  /**
   * Executes phase 2 of the log-in process. Extracts
   * the account name from the token and stores it as a principal
   * in the Subject.
   */
  public boolean commit () throws LoginException {
    this.subject.getPrincipals().add(this.account);
    return true;
  }


  /**
   * Executes phase 2 of the log-in process. There is nothing
   * to do in phase 2 for NonceTokens.
   */
  public boolean abort () throws LoginException {
    return true;
  }


  /**
   * Logs out the subject.  This has no meaning for
   * NonceTokens.
   */
  public boolean logout () throws LoginException {
    return true;
  }


}
