package org.astrogrid.security.jaas;

import java.util.Hashtable;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;


/**
 * Configuration class for use with the LoginContext/LoginModule
 * part of Java Authentication and Authorization System (JAAS).
 *
 * An object of this class returns a hard-coded configuration
 * telling JAAS to use AstroGrid's One-Time-Password (OTP)
 * authentication system.
 *
 * @author Guy Rixon
 */
public class SimpleLoginConfiguration extends Configuration {

  /**
   * States which log-in modules are needed for a named application.
   *
   * @param name The application name
   * @return The list of modules required
   */
  public AppConfigurationEntry[]
      getAppConfigurationEntry (String appName) {
    AppConfigurationEntry a
        = new AppConfigurationEntry("org.astrogrid.security.jaas.OneTimePasswordCheck",
                                    LoginModuleControlFlag.SUFFICIENT,
                                    new Hashtable());
    return new AppConfigurationEntry[] {a};
  }


  /**
   * Refreshes the configuration. No action is needed as the
   * configuration is hard-coded.
   */
  public void refresh() {}
}