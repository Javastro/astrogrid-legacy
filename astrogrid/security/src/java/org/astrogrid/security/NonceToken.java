package org.astrogrid.security;

import java.net.URISyntaxException;
import org.astrogrid.community.client.security.service.SecurityServiceDelegate;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.resolver.CommunityTokenResolver;
import org.astrogrid.community.resolver.security.service.SecurityServiceResolver;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;

/**
 * Trivial extension of
 * org.astrogrid.community.common.security.data.SecurityToken.
 * This class just changes the name, so as to be more specific
 * about what kind of token is represented.
 *
 * A NonceToken composes an account name, which must be expressed
 * as an IVORN and a nonce.
 *
 * @author Guy Rixon
 */
public class NonceToken extends SecurityToken {

  /**
   * Obtains a nonce token by signing on to the community service.
   * This requires the single-sign-on (SSO) account-name and
   * password.
   *
   * @param accountName the SSO account-name
   * @param password the SSO password
   * @return the nonce token
   * @throws InvalidCredentialException if the SSO password is rejected
   * @throws InvalidAccountException if the SSO account is rejected
   * @throws AuthenticatorUnavailable if the community service fails
   */
  public static NonceToken signOn (String accountName,
                                   String password)
        throws AuthenticatorUnavailableException,
  		       InvalidAccountException,
		       InvalidCredentialException {
	try {
	  SecurityServiceResolver ssr = new SecurityServiceResolver();
	  SecurityServiceDelegate ssd = ssr.resolve(new Ivorn(accountName));
	  SecurityToken token = ssd.checkPassword(accountName, password);
	  return new NonceToken(token);
    }
    catch (URISyntaxException use) {
      throw new InvalidAccountException("The account name was rejected", use);
    }
    catch (CommunityIdentifierException cie) {
      throw new InvalidAccountException("The account name was rejected", cie);
    }
    catch (CommunitySecurityException cse) {
      throw new InvalidCredentialException("The password was rejected", cse);
    }
    catch (Exception e) {
	  throw new AuthenticatorUnavailableException(
		            "Authentication failed because the community managing " +
		            "the account is not reachable",
		            e
		        );
    }
  }


  /**
   * Constructs a NonceToken from a given string.
   * The string must follow the conventions of the
   * AstroGrid community component.
   *
   * @param token the nonce, including both nonce and account name
   */
  public NonceToken (String token) {
    super(token);
  }

  /**
   * Constructs a NonceToken from a pair of strings.
   * The strings must follow the conventions of the
   * AstroGrid community component.
   *
   * @param account the account name
   * @param nonce   the nonce
   */
  public NonceToken (String account, String nonce) {
    super(account, nonce);
  }


  /**
   * Constructs a NonceToken by cloning a token
   * obtained from the community service.
   *
   * @param t the community's original token
   */
  public NonceToken (SecurityToken t) {
    super();
    this.setAccount(t.getAccount());
    this.setToken(t.getToken());
    this.setStatus(t.getStatus());
  }


  /**
   * Validates this nonce token.  Validation involves
   * sending the token to the community service that issued it.
   * If the validation succeeds, the community issues a new nonce
   * to the given account and this object extracts and stores the
   * new nonce.  If the validation fails, then an exception is thrown.
   *
   * @throws AuthenticatorUnavailableException if the community or registry fail
   * @throws InvalidAccountException if the account name is rejected
   * @throws InvalidCredentialException if the nonce is rejected
   *
   */
  public void validate () throws AuthenticatorUnavailableException,
                                 InvalidAccountException,
                                 InvalidCredentialException {
    try {


      // Validate this token.  An exception is thrown here if
      // validation fails.
      System.out.println("Validating token: " + this.toString());
      CommunityTokenResolver resolver = new CommunityTokenResolver();
      SecurityToken newToken = resolver.checkToken(this);

      // Update the nonce. Leave the account name unchanged.
      this.setToken(newToken.getToken());
    }
    catch (CommunityIdentifierException cie) {
      throw new InvalidAccountException("The account name was rejected", cie);
    }
    catch (CommunitySecurityException cse) {
      throw new InvalidCredentialException("The password was rejected", cse);
    }
    catch (Exception e) {
  	  throw new AuthenticatorUnavailableException(
  	  	            "Authentication failed because the community managing " +
  		            "the account is not reachable",
  		            e
  		        );
    }
  }


  /**
   * Create an extra nonce token. The creation process validates the
   * existing nonce and thus replaces it.  If this validation fails,
   * then an exception is thrown .
   *
   * @throws AuthenticatorUnavailableException if the community or registry fail
   * @throws InvalidAccountException if the account name is rejected
   * @throws InvalidCredentialException if the nonce is rejected
   *
   */
  public NonceToken split () throws AuthenticatorUnavailableException,
                                    InvalidAccountException,
                                    InvalidCredentialException {
    try {

      // Split this token.  An exception is thrown here if
      // validation fails.
      System.out.println("Existing token: " + this.toString());
      CommunityTokenResolver resolver = new CommunityTokenResolver();
      Object[] o = resolver.splitToken(this, 2);
      assert(o.length == 2);
      assert(o[0] instanceof SecurityToken);
      assert(o[1] instanceof SecurityToken);

      // Update the nonce. Leave the account name unchanged.
      this.setToken(((SecurityToken) o[0]).getToken());

      // Return the second token as a nonce token.
      return new NonceToken((SecurityToken) o[1]);
    }
    catch (CommunityIdentifierException cie) {
	  throw new InvalidAccountException("The account name was rejected", cie);
	}
	catch (CommunitySecurityException cse) {
	  throw new InvalidCredentialException("The password was rejected", cse);
	}
	catch (Exception e) {
	  throw new AuthenticatorUnavailableException(
	    	        "Authentication failed because the community managing " +
	  		        "the account is not reachable",
	  		        e
	  		    );
    }
  }
}