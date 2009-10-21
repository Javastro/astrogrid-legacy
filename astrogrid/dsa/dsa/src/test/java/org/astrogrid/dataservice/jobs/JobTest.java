package org.astrogrid.dataservice.jobs;

import java.sql.Timestamp;
import java.util.List;
import junit.framework.TestCase;
import org.astrogrid.cfg.ConfigFactory;
import org.exolab.castor.jdo.PersistenceException;

/**
 * JUnit-3 tests for {@link org.astrogrid.dataservice.jobs.Job}.
 *
 * @author Guy Rixon
 */
public class JobTest extends TestCase {

  /**
   * Sets the database location.
   *
   * @throws Exception
   */
  @Override
  public void setUp() throws Exception {
    ConfigFactory.getCommonConfig().setProperty("datacenter.cache.directory", "target");
  }
  

  public void testConfigureJdo() throws Exception {
    Job.initialize();
  }

  public void testAddAndLoadJob() throws Exception {
    Job.initialize();
    Job j1 = new Job();
    j1.setId("test");
    j1.setPhase("QUEUED");
    j1.add();

    Job j2 = Job.load("test");
    assertNotNull(j2);
    assertEquals("test", j2.getId());
    assertEquals("QUEUED", j2.getPhase());
  }

  public void testOpenSaveAndLoad() throws Exception {
    Job.initialize();
    Job j1 = new Job();
    j1.setId("test");
    j1.setPhase("QUEUED");
    j1.add();

    Job j2 = Job.open("test");
    assertNotNull(j2);
    assertEquals("test", j2.getId());
    assertEquals("QUEUED", j2.getPhase());

    j2.setPhase("COMPLETED");
    j2.save();

    Job j3 = Job.load("test");
    assertNotNull(j3);
    assertEquals("test", j3.getId());
    assertEquals("COMPLETED", j3.getPhase());
  }

  public void testDelete() throws Exception {
    Job.initialize();

    Job j1 = new Job();
    j1.setId("j1");
    j1.setPhase("QUEUED");
    j1.add();

    Job.delete("j1");
    try {
      Job.load("j1");
      fail("Shouldn't be able to load a job after it is deleted.");
    }
    catch (PersistenceException e) {
      // Expected.
    }

  }

  public void testList() throws Exception {
    Job.initialize();

    Job j1 = new Job();
    j1.setId("j1");
    j1.setPhase("QUEUED");
    j1.add();

    Job j2 = new Job();
    j2.setId("j2");
    j2.setPhase("COMPLETED");
    j2.add();

    List<Job> l = Job.list();
    assertNotNull(l);
    assertEquals(2, l.size());
    assertEquals("j1", l.get(0).getId());
    assertEquals("j2", l.get(1).getId());
  }

  public void testPurge() throws Exception {
    Job.initialize();

    // First job expired an hour ago.
    Job j1 = new Job();
    j1.setId("old");
    j1.setPhase("COMPLETED");
    j1.setDestructionTime(new Timestamp(System.currentTimeMillis() - 3600000));
    j1.add();

    // Second job expires an hour from now.
    Job j2 = new Job();
    j2.setId("new");
    j2.setPhase("QUEUED");
    j2.setDestructionTime(new Timestamp(System.currentTimeMillis() + 3600000));
    j2.add();

    // Purge, hoping to lose only the first job.
    Job.purge();

    List<Job> jobs = Job.list();
    assertNotNull(jobs);
    assertEquals(1, jobs.size());
    assertEquals("new", jobs.get(0).getId());
  }

}
