package org.astrogrid.dataservice.service.cea;

import java.io.Writer;
import junit.framework.TestCase;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.jobs.ResultFile;

/**
 * JUnit-3 tests for {@link org.astrogrid.dataservice.service.cea.DatacenterExecutionHistory}.
 * @author Guy Rixon
 */
public class DatacenterExecutionHistoryTest extends TestCase {

  public void testDirectResult() throws Exception {
    SimpleConfig.setProperty("datacenter.cache.directory", "target");

    DatacenterExecutionHistory sut = new DatacenterExecutionHistory();

    // Write a new job into the job database.
    Job.initialize();
    Job job = new Job();
    job.setId("direct");
    job.setPhase("COMPLETED");
    job.setDestination(null); // Output parameter is "direct".
    job.add();

    // Make a results file for this job.
    ResultFile result = new ResultFile(job.getId());
    result.createNewFile();
    assertTrue(result.exists());
    Writer w = result.openWriter();
    w.append("foo");
    w.close();

    // Look up the job and check that it is correctly reported.
    ExecutionSummaryType summary = sut.getApplicationFromArchive(job.getId());
    assertNotNull(summary);
    assertTrue(summary.isValid());
    assertEquals(ExecutionPhase.COMPLETED, summary.getStatus());
    assertEquals(job.getId(), summary.getExecutionId());
    ResultListType results = summary.getResultList();
    assertNotNull(results);
    assertEquals(1, results.getResultCount());
    assertEquals("Result", results.getResult(0).getName());
    assertEquals("foo", results.getResult(0).getValue());
  }

  public void testInDirectResult() throws Exception {
    SimpleConfig.setProperty("datacenter.cache.directory", "target");

    DatacenterExecutionHistory sut = new DatacenterExecutionHistory();

    // Write a new job into the job database.
    Job.initialize();
    Job job = new Job();
    job.setId("indirect");
    job.setPhase("COMPLETED");
    job.setDestination("vos://some.where!in/vospace"); // Output parameter is "indirect".
    job.add();

    // Make a results file for this job.
    ResultFile result = new ResultFile(job.getId());
    result.createNewFile();
    assertTrue(result.exists());

    // Look up the job and check that it is correctly reported.
    ExecutionSummaryType summary = sut.getApplicationFromArchive(job.getId());
    assertNotNull(summary);
    assertTrue(summary.isValid());
    assertEquals(ExecutionPhase.COMPLETED, summary.getStatus());
    assertEquals(job.getId(), summary.getExecutionId());
    ResultListType results = summary.getResultList();
    assertNotNull(results);
    assertEquals(1, results.getResultCount());
    assertEquals("Result", results.getResult(0).getName());
    assertEquals("vos://some.where!in/vospace", results.getResult(0).getValue());
  }

}
