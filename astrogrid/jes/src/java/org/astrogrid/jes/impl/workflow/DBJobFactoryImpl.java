/*$Id: DBJobFactoryImpl.java,v 1.6 2004/03/15 01:31:12 nw Exp $
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

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Implememntation of JobFacotry, where jobs are stored in a database.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 *
 */
public class DBJobFactoryImpl extends AbstractJobFactoryImpl implements ComponentDescriptor{

    

    
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
    

    
    protected final DataSource datasource;
    protected final SqlCommands sql;
    protected final PresenceCheck presenceCheck ;

    

    /**
     * @see org.astrogrid.jes.job.JobFactory#createJob(org.astrogrid.jes.job.SubmitJobRequest)
     */
    public Workflow createJob(SubmitJobRequest req) throws JobException {
        final Workflow j = super.buildJob(req);
        (new DBHelper(sql.getInsertSQL()) {

            Object dbMethod(PreparedStatement ps) throws SQLException, CastorException, IOException {
                ps.setString(1,id(j));
                Account acc = j.getCredentials().getAccount();
                ps.setString(2,acc.getName());
                ps.setString(3,acc.getCommunity());
                StringWriter sw = new StringWriter();
                j.marshal(sw);
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
    public Workflow findJob(final JobURN urn) throws JobException {

        return (Workflow)(new DBHelper(sql.getRetrieveSQL()) {            
            Object dbMethod(PreparedStatement ps) throws SQLException, CastorException, NotFoundException {
                ps.setString(1,urn.getContent());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return Workflow.unmarshalWorkflow(rs.getCharacterStream(1));
                } else {
                    throw new NotFoundException("Could not find job " + urn);
                }
            }
          }).run();
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#findUserJobs(java.lang.String, java.lang.String, java.lang.String)
     */
    public Iterator findUserJobs(final Account acc) throws JobException {
        final Collection c = new ArrayList();
        (new DBHelper(sql.getListSQL()) {

            Object dbMethod(PreparedStatement ps) throws SQLException, CastorException, IOException {                
                ps.setString(1, acc.getName());
                ps.setString(2,acc.getCommunity());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Workflow wf = Workflow.unmarshalWorkflow(rs.getCharacterStream(1));
                    c.add(wf);
                }
                return null;
            }
        }).run();
        return c.iterator();
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#deleteJob(org.astrogrid.jes.job.Job)
     */
    public void deleteJob(final Workflow job) throws JobException {
        presenceCheck.check(job);
        (new DBHelper(sql.getDeleteSQL()) {

            Object dbMethod(PreparedStatement ps) throws SQLException {
                ps.setString(1,id(job));
                ps.execute();
                return null;
            }
        }).run();
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#updateJob(org.astrogrid.jes.job.Job)
     */
    public void updateJob(final Workflow j) throws JobException {
        presenceCheck.check(j);
        (new DBHelper(sql.getUpdateSQL()) {

            Object dbMethod(PreparedStatement ps) throws SQLException , CastorException, IOException{
                Account acc = j.getCredentials().getAccount();
                ps.setString(1,acc.getName());
                ps.setString(2,acc.getCommunity());
                StringWriter sw = new StringWriter();
                j.marshal(sw);
                sw.close();
                ps.setString(3,sw.toString());
                ps.setString(4,id(j));                
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
        public void check(Workflow wf) throws JobException {
                    this.jobURN = id(wf);
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
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "database-backed job factory";
    }



    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "persists job records in a database table";
    }



    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {        
        TestSuite suite  = new TestSuite("Tests for DB Job Factory");
        suite.addTestSuite(InstallationTest.class);
        return suite;    
    }
    /** verify that we can see database trhough datasource */
    private class InstallationTest extends TestCase {
        
        public InstallationTest(String s) {
            super(s);
        }
        
        public void testDatasource() throws Exception {
            Connection conn = datasource.getConnection();
            assertNotNull(conn);
            conn.close();
        }
        
        public void testSQL() throws Exception {
            Connection conn = datasource.getConnection();
            PreparedStatement stmnt = conn.prepareStatement(sql.getDeleteSQL());
            stmnt.close();
            stmnt = conn.prepareStatement(sql.getInsertSQL());
            stmnt.close();
             stmnt = conn.prepareStatement(sql.getListSQL());
            stmnt.close();
             stmnt = conn.prepareStatement(sql.getRetrieveSQL());
            stmnt.close();                                    
            stmnt = conn.prepareStatement(sql.getUpdateSQL());
            stmnt.close();
        }
        
             
    }
}


/* 
$Log: DBJobFactoryImpl.java,v $
Revision 1.6  2004/03/15 01:31:12  nw
jazzed up javadoc

Revision 1.5  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.4.4.1  2004/03/07 20:41:59  nw
altered to look in component manager factory for implementations

Revision 1.4  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.3  2004/03/03 01:13:41  nw
updated jes to work with regenerated workflow object model

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