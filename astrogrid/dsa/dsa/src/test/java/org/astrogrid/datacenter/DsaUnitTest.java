package org.astrogrid.datacenter;

import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.test.SampleStarsPlugin;

/**
 * A JUnit-3 test for a DSA class.
 *
 * @author Guy Rixon
 */
public abstract class DsaUnitTest extends TestCase {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    // Set the location for the job database.
    SimpleConfig.setProperty("datacenter.cache.directory", "target");

    // Make an empty database for jobs.
    Job.initialize();

    // Make a dummy science-database.
    SampleStarsPlugin.initConfig();

    // Load the metadoc specified by the plug-in.
    // If this is not done, the test may use an inappropriate
    // metadoc left over from a previous test.
    TableMetaDocInterpreter.clear();
    TableMetaDocInterpreter.initialize();
  }

}
