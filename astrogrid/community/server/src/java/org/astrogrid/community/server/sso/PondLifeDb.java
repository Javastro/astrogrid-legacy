package org.astrogrid.community.server.sso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.astrogrid.config.SimpleConfig;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;

/**
 * A database utility for testing.
 * Executing the {@link #initialize} method of this class configures the
 * the other commuity-service classes to work with the community
 * ivo://pond/community containing a user called frog whose password is
 * croakcroak. The HSQL/Castor database called org.astrogrid.community.database
 * is initialized accordingly.
 *
 * @author Guy Rixon
 */
public class PondLifeDb {
  
  public void initialize() throws MappingException, 
                                  IOException, 
                                  DatabaseNotFoundException, 
                                  PersistenceException {

    CommunityConfiguration config = new CommunityConfiguration();
    
    // Specify the community identity. Other software will use this to
    // generate the database keys; i.e. user frog is keyed in the database
    // by ivo://pond/frog.
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.ident",
                                            "pond");
    
    // Specify the configuration file for the database. Other software will
    // read this property when starting the database engine.
    // Copying the mapping files out of the jar is an act of desparation.
    // The comsuming software should be able to read them straight from the
    // jar, but this seems to fail.
    copyResourceToFile("/account-servlet-test.xml", ".", "account-servlet-test.xml");
    File f1 = new File(".", "account-servlet-test.xml");
    URL u2 = f1.toURL();
    config.setDatabaseConfigurationUrl(u2);
    copyResourceToFile("/astrogrid-community-mapping.xml", ".", "astrogrid-community-mapping.xml");
    
    // Initialize the database with the account frog.
    DatabaseConfiguration dbc = new DatabaseConfiguration("org.astrogrid.community.database", u2);
    URL u3 = this.getClass().getResource("/frog-account.sql");
    dbc.resetDatabaseTables(u3);
    
    // Specify the directory where credentials are kept.
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.myproxy",
                                            ".");
    
    // Copy in credentials files for the account frog.
    copyResourceToFile("/frog-certificate.pem", "./frog", "certificate.pem");
    copyResourceToFile("/frog-key.pem",         "./frog", "key.pem");
  }
  
  /**
   * Copies a named resource from the classpath to a named file.
   */
  private void copyResourceToFile(String resourceName,
                                  String directoryName,
                                  String fileName) throws IOException {
    InputStream is = this.getClass().getResourceAsStream(resourceName);
    assert is != null;
    File d = new File(directoryName);
    d.mkdirs();
    File f = new File(d, fileName); 
    OutputStream os = new FileOutputStream(f);
    while(true) {
      int c = is.read();
      if (c == -1) {
        break;
      }
      else {
        os.write(c);
      }
    }
    os.close();
    is.close();
  }
  
}
