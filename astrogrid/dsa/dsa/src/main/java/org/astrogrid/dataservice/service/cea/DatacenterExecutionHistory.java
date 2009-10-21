package org.astrogrid.dataservice.service.cea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.InputListType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;
import org.astrogrid.applications.manager.persist.PersistenceException;
import org.astrogrid.applications.manager.persist.InMemoryExecutionHistory;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.jobs.ResultFile;

/**
 * A hybrid {@code ExecutionHistory} linking CEA jobs with the UWS
 * job-database. Running jobs are recorded in memory; archived jobs are
 * recorded on disc. When jobs are archived, the {@code Application} instances
 * are discarded and any information requests for those jobs are filled
 * from the job-database.
 *
 * @author Guy Rixon
 */
public class DatacenterExecutionHistory extends InMemoryExecutionHistory {

  /**
   * Removes the job from the in-memory cache of current executions. The job is
   * then implicitly in the disc cache of finished executions.
   *
   * @param jobID The job identifer, matching the CEA execution-identifier.
   * @see org.astrogrid.applications.manager.persist.ExecutionHistory#moveApplicationFromCurrentSetToArchive(java.lang.String)
   */
  @Override
  public void moveApplicationFromCurrentSetToArchive(String jobId) {
    currentSet.remove(jobId);
  }

  /**
   * Supplies a summary of an archived job.
   *
   * @param jobId The job identifier.
   * @return The summary
   * @throws ExecutionIDNotFoundException If the job is not in the job database.
   * @throws PersistenceException If the job's results file cannot be read.
   */
  @Override
  public ExecutionSummaryType getApplicationFromArchive(String jobId) throws ExecutionIDNotFoundException,
                                                                             PersistenceException {
    Job job;
    try {
      job = Job.load(jobId);
    } catch (org.exolab.castor.jdo.PersistenceException ex) {
      throw new ExecutionIDNotFoundException(String.format("Job %s is not in the database", jobId));
    }

    // Get a representation of the Result parameter, which represents the
    // table of results from the query. If the parameter has been sent somewhere
    // else, then its value is the URI for that place. Otherwise, force the
    // bit-pattern of the table into a string (ugh!) and put that string
    // as the parameter value, expecting it to written in-line in the
    // status message (double ugh!).
    ParameterValue p = new ParameterValue();
    p.setName("Result");
    if (job.getDestination() == null) {
      StringWriter sw = new StringWriter();
      try {
        ResultFile f = new ResultFile(jobId);
        BufferedReader r = new BufferedReader(new FileReader(f));
        while (true) {
          int c = r.read();
          if (c == -1) {
            break;
          }
          else {
            sw.append((char) c);
          }
        }
      }
      catch (IOException e) {
        throw new org.astrogrid.applications.manager.persist.PersistenceException(e.getMessage());
      }
      p.setIndirect(false);
      p.setValue(sw.toString());
    }
    else {
      p.setIndirect(true);
      p.setValue(job.getDestination());
    }
    ResultListType results = new ResultListType();
    results.addResult(p);

    ExecutionSummaryType summary = new ExecutionSummaryType();
    summary.setApplicationName(""); // TODO: fill this bit in.
    summary.setExecutionId(jobId);
    summary.setStatus(ceaExecutionPhase(job.getPhase()));
    summary.setInputList(new InputListType());
    summary.setResultList(results);

    return summary;
  }

  /**
   * Supplies the CEA execution-phase matching a given UWS phase.
   *
   * @param uwsPhase The phase title in UWS.
   * @return The CEA phase (never null; UNKNOWN if no match).
   */
  private ExecutionPhase ceaExecutionPhase(String uwsPhase) {
    if ("PENDING".equals(uwsPhase)) {
      return ExecutionPhase.PENDING;
    }
    else if ("QUEUED".equals(uwsPhase)) {
      return ExecutionPhase.INITIALIZING;
    }
    else if ("RUNNING".equals(uwsPhase)) {
      return ExecutionPhase.RUNNING;
    }
    else if ("COMPLETED".equals(uwsPhase)) {
      return ExecutionPhase.COMPLETED;
    }
    else if ("ERROR".equals(uwsPhase)) {
      return ExecutionPhase.ERROR;
    }
    if ("ABORTED".equals(uwsPhase)) {
      return ExecutionPhase.ERROR;
    }
    else {
      return ExecutionPhase.UNKNOWN;
    }
  }
}
