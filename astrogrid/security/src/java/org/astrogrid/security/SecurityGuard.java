package org.astrogrid.security;

/**
 * Access to the security credentials pertaining web-service operations.
 *
 * This is a Java-bean class in which the properties are various
 * credentials for secured messaging. It is a standard way, within its
 * package, of passing credentials within the same JVM.
 *
 * Applications may use this class directly, but are more likely to
 * use one of the two subclasses {@link ClientSecurityGuard} and
 * {@link ServiceSecurityGuard}. The latter two classes add methods
 * to interact with JAX-RPC handler-chains and thus to use the
 * credentials in SOAP messages.
 *
 * @see ClientSecurityGuard
 * @see ServiceSecurityGuard
 *
 * @author Guy Rixon
 */
public class SecurityGuard {


  /**
   * The username to go into the messages.
   */
  protected String username;

  /**
   * The clear-text password to go into the messages.
   */
  protected String password;

  /**
   * Flag indicating whether the passwsord should be hashed.
   * True means hash the password; false means leave the
   * password in clear text.
   */
  private boolean hashPassword = false;


  /**
   * Sets the username property.
   *
   * @param name the user-name
   */
  public void setUsername (String name) {
    this.username = name;
  }

  /**
   * Returns the username property.
   *
   * @return the user-name (may be null if the property is not set)
   */
  public String getUsername () {
    return this.username;
  }


  /**
   * Sets the password property.
   *
   * @param name the password in clear text
   */
  public void setPassword (String pass) {
    this.password = pass;
  }

  /**
   * Returns the password property.
   *
   * @return the password (may be null if the property is not set)
   */
   public String getPassword () {
     return this.password;
   }


  /**
   * Sets whether the password is to be hashed.
   */
  public void setPasswordHashing (boolean hashed) {
    this.hashPassword = hashed;
  }

  /**
   * Returns whether the password is to be hashed.
   */
  public boolean isPasswordHashing () {
    return this.hashPassword;
  }

}
