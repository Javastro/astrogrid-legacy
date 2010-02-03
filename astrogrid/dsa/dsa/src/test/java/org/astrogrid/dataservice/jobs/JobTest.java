package org.astrogrid.dataservice.jobs;

import java.io.File;
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
    ConfigFactory.getCommonConfig().setProperty("datacenter.cache.directory", "target/cache");
    new File("target/cache").mkdirs();
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

    // Prove it can find the DB for itself, without help from initialize().
    Job.setJdoManager(null);
    Job j3 = new Job();
    j3.setId("retest");
    j3.setPhase("QUEUED");
    j3.add();

    Job.setJdoManager(null);
    Job j4 = Job.load("retest");
    assertNotNull(j4);
    assertEquals("retest", j4.getId());
    assertEquals("QUEUED", j4.getPhase());
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

    // Prove it can find the DB for itself, without help from initialize().
    Job.setJdoManager(null);
    j2 = Job.open("test");
    j2.setOwner("Fred");
    j2.save();

    Job j4 = Job.load("test");
    assertNotNull(j4);
    assertEquals("test", j4.getId());
    assertEquals("Fred", j4.getOwner());

    // Check the error handling.
    Job j5 = new Job();
    try {
      j5.save();
      fail("Shouldn't be able to save a job withou and open transaction.");
    }
    catch (PersistenceException e) {
      // Expected.
    }
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

    // Prove it can find the DB for itself, without help from initialize().
    Job j2 = new Job();
    j2.setId("j2");
    j2.setPhase("QUEUED");
    j2.add();

    Job.setJdoManager(null);
    Job.delete("j2");

    try {
      Job.load("j2");
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

    // Prove it can find the DB for itself, without help from initialize().
    Job.setJdoManager(null);
    l = Job.list();
    assertNotNull(l);
    assertEquals(2, l.size());
    assertEquals("j1", l.get(0).getId());
    assertEquals("j2", l.get(1).getId());
  }

  public void testListByOwner() throws Exception {
    Job.initialize();

    Job j1 = new Job();
    j1.setId("j1");
    j1.setPhase("QUEUED");
    j1.setOwner("foo");
    j1.add();

    Job j2 = new Job();
    j2.setId("j2");
    j2.setPhase("COMPLETED");
    j2.setOwner("bar");
    j2.add();

    Job j3 = new Job();
    j3.setId("j3");
    j3.setPhase("COMPLETED");
    j3.setOwner("foo");
    j3.add();

    Job j4 = new Job();
    j4.setId("j4");
    j4.setPhase("QUEUED");
    j4.setOwner("baz");
    j4.add();

    Job j5 = new Job();
    j5.setId("j5");
    j5.setPhase("QUEUED");
    // This one has no owner - its default state on construction
    j5.add();

    List<Job> l1 = Job.list("foo");
    for (Job j : l1) {
      System.out.println((j.getId() + " " + j.getOwner()));
    }
    assertNotNull(l1);
    assertEquals(3, l1.size());
    assertEquals("j1", l1.get(0).getId());
    assertEquals("j3", l1.get(1).getId());
    assertEquals("j5", l1.get(2).getId());

    List<Job> l2 = Job.list("bar");
    assertNotNull(l1);
    assertEquals(2, l2.size());
    assertEquals("j2", l2.get(0).getId());
    assertEquals("j5", l2.get(1).getId());

    List<Job> l3 = Job.list(null);
    assertNotNull(l3);
    assertEquals(1, l3.size());
    assertEquals("j5", l3.get(0).getId());
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

    // Prove it can find the DB for itself, without help from initialize().
    Job.setJdoManager(null);
    Job.purge();

    jobs = Job.list();
    assertNotNull(jobs);
    assertEquals(1, jobs.size());
    assertEquals("new", jobs.get(0).getId());
  }

}
