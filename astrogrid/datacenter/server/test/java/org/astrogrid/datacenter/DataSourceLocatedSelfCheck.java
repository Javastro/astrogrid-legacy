package org.astrogrid.datacenter;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import junit.framework.TestCase;

/* $Id: DataSourceLocatedSelfCheck.java,v 1.1 2004/01/29 21:55:26 anoncvs Exp $
 * Created on 29-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */

/**
 * @author jdt
 *
 * Unit test for checking an installation - checks DataSource available.
 * Note this test is only required for those DataCenters which rely on a DataSource
 * object being available.
 * <p>
 * not intended for use during development - hence the different naming convention.
 */
public class DataSourceLocatedSelfCheck extends TestCase {

  /**
   * Constructor for DataSourceLocatedSelfCheck.
   * @param arg0 name
   */
  public DataSourceLocatedSelfCheck(final String arg0) {
    super(arg0);
  }

  /**
  * JUnit test CanGetDatabaseConnection
  * Check that we can get a DataSource from JNDI, followed by a Connection
  * Written by jdt, 29-Jan-2004
  */
  public final void testCanGetDatabaseConnection()
    throws NamingException, SQLException {

    Context initCtx = new InitialContext();
    Context envCtx = (Context) initCtx.lookup("java:comp/env");
    String dsJNDIname = "jdbc/pal-datasource";
    DataSource ds = (DataSource) envCtx.lookup(dsJNDIname);
    Connection conn = ds.getConnection();
    conn.close();

  }

}

/*
*$Log: DataSourceLocatedSelfCheck.java,v $
*Revision 1.1  2004/01/29 21:55:26  anoncvs
*Name change.  Gawd I've got tests on the brain.  It IS a test, but not as we know it.
*
*Revision 1.1  2004/01/29 18:33:06  jdt
*InitialCommit
*
*/