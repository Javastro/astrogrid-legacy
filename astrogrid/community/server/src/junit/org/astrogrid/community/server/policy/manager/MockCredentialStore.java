package org.astrogrid.community.server.policy.manager;

import java.security.GeneralSecurityException;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.astrogrid.community.server.sso.CredentialStore;
import org.astrogrid.security.SecurityGuard;

/**
 *
 * @author Guy Rixon
 */
public class MockCredentialStore extends CredentialStore {

  public MockCredentialStore(DatabaseConfiguration config) throws GeneralSecurityException {
    super(config);
  }

  @Override
  public SecurityGuard getCredentials(String userName, String password) {
    return new SecurityGuard();
  }

}
