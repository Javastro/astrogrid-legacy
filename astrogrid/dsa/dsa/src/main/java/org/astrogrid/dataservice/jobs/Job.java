package org.astrogrid.dataservice.jobs;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.castor.jdo.conf.Driver;
import org.castor.jdo.util.JDOConfFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.MappingException;

/**
 *
 * @author Guy Rixon
 */
public class Job {
  private static Log log = LogFactory.getLog(Job.class);

  private static final String DRIVER = "org.hsqldb.jdbcDriver";
  private static final String USERNAME = "sa";
  private static final String PASSWORD = "";
  private static final String MAPPING = "jdo-mapping.xml";
  private static final String DATABASE = "jobs";
  private static final String ENGINE = "hsql";

  /**
   * A loaded JDO-configuration. From this, connections to the database
   * may be obtained.
   */
  private static JDOManager jdo = null;

  /**
   * An open connection to the database.
   * This may be used to record changes to the object.
   */
  private Database db;

  private String id;

  /**
   * The time at which execution of the query started. This may be some time
   * after the query was released for execution by the client.
   */
  private Timestamp startTime;

  /**
   * The time at which processing of the query finished (whether the processing
   * completed, failed or was aborted).
   */
  private Timestamp endTime;

  /**
   * The time at which the query and its results should be forgotten.
   */
  private Timestamp destruction;

  /**
   * The phase of execution, using labels defined in the UWS standard.
   */
  private String phase;

  /**
   * A human-readable error-message.
   */
  private String error;

  /**
   * The text of the query to be executed.
   */
  private String query;

  /**
   * The URI for the destination of the results of the job.
   */
  private String destination;

  /**
   * The format in which to write the query results.
   */
  private String format;

  /**
   * A label for the interface that created this job.
   * Plausible values are "TAP/UWS", "TAP/synchronous",
   * "cone-search", "CEC".
   */
  String source;

  public static void setJdoManager(JDOManager m) {
    jdo = m;
  }

  /**
   * Gets a manager for the database in the configured directory.
   *
   * @return The database manager.
   */
  public static JDOManager getJdoManager() {
    String directory = (String)
        SimpleConfig.getSingleton().getProperty("datacenter.cache.directory");
    return getJdoManager(directory);
  }

  /**
   * Gets a manager for the database in a given directory.
   *
   * @param directory The directory (absolute path or relative to CWD).
   *
   * @return The database manager.
   */
  public static JDOManager getJdoManager(String directory) {
    String dbUri = String.format("jdbc:hsqldb:%s/jobs", directory);

    Driver driver = JDOConfFactory.createDriver(DRIVER, dbUri, USERNAME, PASSWORD);
    String  mapping = Job.class.getResource(MAPPING).toString();
    org.castor.jdo.conf.Database dbConf =
        JDOConfFactory.createDatabase(DATABASE, ENGINE, driver, mapping);
    try {
      JDOManager.loadConfiguration(JDOConfFactory.createJdoConf(dbConf), null);
      return JDOManager.createInstance(DATABASE);
    } catch (MappingException e) {
      throw new RuntimeException(e);
    }
  
  }

  /**
   * Initializes the database, creating empty tables.
   *
   * @throws PersistenceException If Castor fails.
   * @throws SQLException If JDBC fails.
   */
  public static void initialize() throws PersistenceException, SQLException {
    if (jdo == null) {
      jdo = getJdoManager();
    }
    Database db = jdo.getDatabase();
    db.begin();
    try {
      String drop   = "DROP TABLE jobs IF EXISTS;";
      db.getJdbcConnection().createStatement().execute(drop);
      String create = "CREATE TABLE jobs (" +
          "id VARCHAR NOT NULL, " +
          "phase VARCHAR NOT NULL, " +
          "destructionTime TIMESTAMP, " +
          "startTime TIMESTAMP, " +
          "endTime TIMESTAMP, " +
          "source VARCHAR, " +
          "errorMessage VARCHAR, " +
          "query VARCHAR, " +
          "destination VARCHAR," +
          "format VARCHAR, " +
          "PRIMARY KEY (id));";
      db.getJdbcConnection().createStatement().execute(create);
      db.commit();
    }
    finally {
      if (db.isActive()) {
        db.rollback();
      }
      db.close();
    }
  }

  /**
   * Loads a job with the given ID from the database. A database transaction
   * remains open, so the returned object may later be ued to update the job
   * record.
   *
   * @param id
   * @return The job.
   * @throws PersistenceException If the job is not in the database.
   * @throws PersistenceException If the database is unavailable.
   */
  public static Job open(String id) throws PersistenceException {
    if (jdo == null) {
      jdo = getJdoManager();
    }
    Database db = jdo.getDatabase();
    db.begin();
    Job j = (Job) db.load(Job.class, id);
    j.db = db;
    return j;
  }

  /**
   * Loads a job with the given ID from the database. The returned object
   * may not later be ued to update the job record.
   *
   * @param id
   * @return The job.
   * @throws PersistenceException If the job is not in the database.
   * @throws PersistenceException If the database is unavailable.
   */
  public static Job load(String id) throws PersistenceException {
    if (jdo == null) {
      jdo = getJdoManager();
    }
    Database db = jdo.getDatabase();
    try {
      db.begin();
      Job j = (Job) db.load(Job.class, id);
      db.commit();
      return j;
    }
    finally {
      if (db.isActive()) {
        db.rollback();
      }
      db.close();
    }
  }

  /**
   * Deletes from the database the job with the given ID.
   *
   * @param id The identifier of the job to be deleted.
   * @throws PersistenceException If the database is not available.
   */
  public static void delete(String id) throws PersistenceException {
    if (jdo == null) {
      jdo = getJdoManager();
    }
    Database db = jdo.getDatabase();
    try {
      db.begin();
      Job j = (Job) db.load(Job.class, id);
      db.remove(j);
      db.commit();
    }
    finally {
      if (db.isActive()) {
        db.rollback();
      }
      db.close();
    }
  }

  /**
   * Lists all the jobs in the database.
   */
  public static List<Job> list() throws PersistenceException {
    if (jdo == null) {
      jdo = getJdoManager();
    }
    ArrayList<Job> l = new ArrayList<Job>();
    Database db = jdo.getDatabase();
    try {
      db.begin();
      OQLQuery q = db.getOQLQuery("SELECT j FROM org.astrogrid.dataservice.jobs.Job j");
      QueryResults r = q.execute();
      while (r.hasMore()) {
        l.add((Job) r.next());
      }
      r.close();
      q.close();
      db.commit();
      return l;
    }
    
    finally {
      if (db.isActive()) {
        db.rollback();
      }
      db.close();
    }
  }

  /**
   * Deletes from the database all jobs for which the destruction time has
   * passed.
   */
  public static void purge() throws PersistenceException {
    List<Job> jobs = Job.list();
    Date now = new Date();
    for (Job j : jobs) {
      if (j.getDestructionTime().before(now)) {
        Job.delete(j.getId());
        new ResultFile(j.getId()).delete();
      }
    }
  }

  public Job() {
    id = null;
    destruction = null;
    phase = "PENDING";
    db = null;
    startTime = null;
    endTime = null;
    source = null;
    error = null;
    query = null;
    destination = null;
    format = null;
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Timestamp getDestructionTime() {
    return destruction;
  }

  public void setDestructionTime(Timestamp d) {
    destruction = d;
  }

  public String getPhase() {
    return phase;
  }

  public void setPhase(String phase) {
    this.phase = phase;
  }

  public Timestamp getStartTime() {
    return startTime;
  }

  public void setStartTime(Timestamp d) {
    startTime = d;
  }

  public Timestamp getEndTime() {
    return endTime;
  }

  public void setEndTime(Timestamp d) {
    endTime = d;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String s) {
    source = s;
  }

  public String getErrorMessage() {
    return error;
  }

  public void setErrorMessage(String s) {
    error = s;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String q) {
    query = q;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String d) {
    destination = d;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String f) {
    format = f;
  }

  /**
   * Adds this job to the database as a new record.
   */
  public void add() throws PersistenceException {
    if (jdo == null) {
      jdo = getJdoManager();
    }
    db = jdo.getDatabase();
    try {
      db.begin();
      db.create(this);
      db.commit();
    }
    finally {
      if (db.isActive()) {
        db.rollback();
      }
      db.close();
      db = null;
    }
  }

  /**
   * Saves the state of this job to the database. This only works if
   * the object was obtained from a call to {@link #open(String)}.
   */
  public void save() throws PersistenceException {
    if (db == null) {
      throw new PersistenceException("No transaction is open for this object.");
    }
    try {
      db.commit();
    }
    finally {
      if (db.isActive()) {
        db.rollback();
      }
      db.close();
      db = null;
    }
  }

}
