/*$Id: DBJobFactoryImpl.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.exolab.castor.xml.CastorException;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.sql.DataSource;

/** Implememntation of JobFacotry, where jobs are stored in a database.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 *
 */
public class DBJobFactoryImpl extends AbstractJobFactoryImpl {

    

    
    /** Construct a  database-bascked job factory
     * 
     * @param ds datasource to connect to db
     * @param sql sql commands object to use to interact with db.
     */
    public DBJobFactoryImpl(DataSource ds,SqlCommands sql) {
        super();
        log.info("Database Job Factory");
        this.datasource = ds;
        this.sql = sql;
        presenceCheck = new PresenceCheck();
    }
    

    
    /** constructor that uses default sql commands */
    public DBJobFactoryImpl(DataSource ds) {
        this(ds,new DefaultSqlCommands());
    }
    
    protected final DataSource datasource;
    protected final SqlCommands sql;
    protected final PresenceCheck presenceCheck ;

    

    /**
     * @see org.astrogrid.jes.job.JobFactory#createJob(org.astrogrid.jes.job.SubmitJobRequest)
     */
    public Job createJob(SubmitJobRequest req) throws JobException {
        final JobImpl j = super.buildJob(req);
        (new DBHelper(sql.getInsertSQL()) {

            Object dbMethod(PreparedStatement ps) throws SQLException, CastorException, IOException {
                ps.setString(1,j.getId().toString());
                ps.setString(2,j.getUserId());
                ps.setString(3,j.getCommunity());
                StringWriter sw = new StringWriter();
                j.getWorkflow().marshal(sw);
                sw.close();
                ps.setString(4,sw.toString());
                ps.executeUpdate();
                return null;
            }
        }).run();
        return j;
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#findJob(java.lang.String)
     */
    public Job findJob(final JobURN urn) throws JobException {

        return (Job)(new DBHelper(sql.getRetrieveSQL()) {            
            Object dbMethod(PreparedStatement ps) throws SQLException, CastorException, NotFoundException {
                ps.setString(1,urn.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Workflow wf = Workflow.unmarshalWorkflow(rs.getCharacterStream(1));
                    return new JobImpl(wf);
                } else {
                    throw new NotFoundException("Could not find job " + urn);
                }
            }
          }).run();
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#findUserJobs(java.lang.String, java.lang.String, java.lang.String)
     */
    public Iterator findUserJobs(final String userid, final String community, String jobListXML) throws JobException {
        final Collection c = new ArrayList();
        (new DBHelper(sql.getListSQL()) {

            Object dbMethod(PreparedStatement ps) throws SQLException, CastorException, IOException {
                ps.setString(1,userid.trim());
                ps.setString(2,community.trim());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Workflow wf = Workflow.unmarshalWorkflow(rs.getCharacterStream(1));
                    c.add(new JobImpl(wf));
                }
                return null;
            }
        }).run();
        return c.iterator();
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#deleteJob(org.astrogrid.jes.job.Job)
     */
    public JobURN deleteJob(final Job job) throws JobException {
        presenceCheck.check(job.getId());
        (new DBHelper(sql.getDeleteSQL()) {

            Object dbMethod(PreparedStatement ps) throws SQLException {
                ps.setString(1,job.getId().toString());
                ps.execute();
                return null;
            }
        }).run();
        return job.getId();
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#updateJob(org.astrogrid.jes.job.Job)
     */
    public void updateJob(final Job job) throws JobException {
        presenceCheck.check(job.getId());
        final JobImpl j = (JobImpl)job;
        (new DBHelper(sql.getUpdateSQL()) {

            Object dbMethod(PreparedStatement ps) throws SQLException , CastorException, IOException{
                ps.setString(1,job.getUserId());
                ps.setString(2,job.getCommunity());
                StringWriter sw = new StringWriter();
                j.getWorkflow().marshal(sw);
                sw.close();
                ps.setString(3,sw.toString());
                ps.setString(4,job.getId().toString());                
                ps.execute();
                return null;
            }
        }).run();
        
    }
    /** application of db helper -verifies a particular job is present in the db */ 
    private class PresenceCheck extends DBHelper {
        public PresenceCheck() {
            super( sql.getRetrieveSQL());
        }
        protected String jobURN;                
        Object dbMethod(PreparedStatement ps) throws SQLException, JobException {
                    ps.setString(1,jobURN);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotFoundException("job urn " + jobURN + " not found");
                    }
                    return null;
                }
        public void check(JobURN urn) throws JobException {
                    this.jobURN = urn.toString();
                    this.run();
                }
    }
    /** helper class - abstract away all the mucky business of jdbc, and ensures everything is deallocated cleanly. */ 
    abstract class DBHelper{
         public DBHelper(String sql) {
             this.sql = sql;
         }
         private final String sql;
        abstract Object dbMethod(PreparedStatement ps) throws JobException,SQLException, CastorException, IOException;
        public final Object run() throws JobException {
            Connection conn = null;
            PreparedStatement stmnt = null;
            try {
                conn = datasource.getConnection();              
                stmnt = conn.prepareStatement(sql);
                return this.dbMethod(stmnt);
            } catch (SQLException e) {
                log.error("Database Exception",e);
                throw new JobException("Store Failure",e);
            } catch(CastorException e) {
                log.error("Marshalling Exception",e);
                throw new JobException("Marshalling Failure",e);
            } catch (IOException e) {
                log.error("IOExcepiton",e);
                throw new JobException("Marshalling Failure",e);
            } finally {
                if (stmnt != null) {
                    try {
                        stmnt.close();
                    } catch (SQLException e) {
                        log.warn("SQL exception on close statement",e);
                    }
                }
                if (conn != null){
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        log.warn("SQL exception on close connection",e);                        
                    }
                }
            }
        }
    }
}


/* 
$Log: DBJobFactoryImpl.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.6  2004/02/19 13:40:09  nw
updated to fit new interfaces

Revision 1.1.2.5  2004/02/17 16:48:41  nw
updated to throw exceptions when jobs are not found

Revision 1.1.2.4  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.3  2004/02/17 10:58:38  nw
altered to implement cut down facade interface, matched with types
generated by wsdl2java

Revision 1.1.2.2  2004/02/12 12:54:47  nw
worked in inversion of control pattern - basically means that
components have to be assembled, rather than self-configuring
from properties in config files. so easier to test each component in isolation

Revision 1.1.2.1  2004/02/12 01:14:01  nw
castor implementation of jes object model
 
*/