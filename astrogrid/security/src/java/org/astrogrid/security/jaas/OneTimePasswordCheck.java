package org.astrogrid.security.jaas;

import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import org.astrogrid.community.client.security.service.SecurityServiceDelegate;
import org.astrogrid.community.client.security.service.SecurityServiceMockDelegate;
import org.astrogrid.security.AccountName;
import org.astrogrid.security.NonceToken;


/**
 * A JAAS log-in module implementing AstroGrid's nonce-token protocol.
 * protocol. This class is called by the JAAS framework when an application
 * calls a LoginContext that is configured for NonceTokens. Application code
 * should not need to call this class directly (except for unit tests).
 *
 * @author Guy Rixon
 */
public class OneTimePasswordCheck implements LoginModule {

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
    Set tokens = this.subject.getPrivateCredentials(NonceToken.class);
    if (tokens.size() == 0) {
      throw new LoginException("No AstroGrid tokens were presented.");
    }
    else if (tokens.size() > 1) {
      throw new LoginException("Too many AstroGrid tokens were presented.");
    }
    else {
      oldToken = (NonceToken) tokens.iterator().next();
      tokens.clear();   // Remove the old token from the Subject.
    }

    // Locate the comunity service that issued the token.
    // @TODO use a proper delegate factory and set
    // the location of the service.
    SecurityServiceDelegate ssd = new SecurityServiceMockDelegate();

    // Check the original token in the community service.
    // If a new token comes back, then the original was valid
    // and the authentication succeeds; otherwise, authentication
    // fails.
    NonceToken newToken = new NonceToken(ssd.checkToken(oldToken));

    if (newToken != null) {
      tokens.add(newToken); // Put the new token back into the Subject.
      this.account = new AccountName(newToken.getAccount());
      return true;
    }
    else {
      throw new LoginException("The AstroGrid token is invalid");
    }
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