/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/security/service/Attic/SecurityServiceImplTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: gtr $</cvs:author>
 * <cvs:date>$Date: 2008/01/23 15:24:12 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceImplTestCase.java,v $
 *   Revision 1.2  2008/01/23 15:24:12  gtr
 *   Branch community-gtr-2512 is merged.
 *
 *   Revision 1.1.2.1  2008/01/23 13:26:46  gtr
 *   no message
 *
 *   Revision 1.1.2.1  2008/01/22 15:37:54  gtr
 *   Refactored so as to be free of the community-common tests - and it actually tests meaningful stuff now.
 *
 *   Revision 1.6  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.5.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.36.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.security.service ;

import junit.framework.TestCase;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.server.database.manager.DatabaseManagerImpl ;

import org.astrogrid.community.common.policy.manager.AccountManager ;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl ;

import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;
import org.astrogrid.community.server.database.configuration.DatabaseConfigurationFactory ;
import org.astrogrid.community.server.database.configuration.TestDatabaseConfigurationFactory ;
import org.astrogrid.community.server.security.manager.SecurityManagerImpl;

/**
 * A JUnit test case for our SecurityService implementation.
 *
 */
public class SecurityServiceImplTestCase extends TestCase {

  private static Log log = LogFactory.getLog(SecurityServiceImplTestCase.class);

  private DatabaseConfiguration config;
    
  public void setUp() throws Exception {
    TestDatabaseConfigurationFactory factory = 
        new TestDatabaseConfigurationFactory();
    this.config = factory.testDatabaseConfiguration();
    DatabaseManagerImpl dbm = new DatabaseManagerImpl(config);
    dbm.resetDatabaseTables();
    AccountManagerImpl am = new AccountManagerImpl(config);
    am.addAccount("ivo://frog@org.astrogrid.regtest/community");
    SecurityManagerImpl sm = new SecurityManagerImpl(config);
    sm.setPassword("ivo://frog@org.astrogrid.regtest/community", "croakcroak");
  }

  public void testPassword() throws Exception {
    SecurityServiceImpl sut = new SecurityServiceImpl(config);
    
    sut.checkPassword("ivo://frog@org.astrogrid.regtest/community", "croakcroak");
  }
}
