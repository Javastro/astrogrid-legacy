/* $Id: JobControllerTest.java,v 1.1 2003/10/29 16:42:52 jdt Exp $
 * Created on 29-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.jobcontroller;

//import org.astrogrid.AstroGridException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.TestCase;

/**
 * Test class for the JobController
 * @author jdt
 *
 */
public class JobControllerTest extends TestCase {

  /**
   * Constructor for JobController.
   * @param arg0 name of test
   */
  public JobControllerTest(final String arg0) {
    super(arg0);
  }
  /**
   * Launch the JUnit text gui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(JobControllerTest.class);
  }

  /**
   * Testcase setup
   * @throws Exception Propagates exceptions from super.setUp() 
   * @see TestCase#setUp()
   */
  protected final void setUp() throws Exception {
    super.setUp();
    goodJobXML = getResourceAsString("submit-job.xml", this.getClass());
  }

  /**
   * Testcase cleanup
   * @throws Exception Propagates exceptions from super.tearDown() 
   * @see TestCase#tearDown()
   */
  protected final void tearDown() throws Exception {
    super.tearDown();
  }

  /**
   * Check that we get a sensible exception back when you do something 
   * really unreasonable.
   * (currently picking up bug #12 as already logged by Jeff)
   * 
   * [currently will not compile since the method doesn't declare any exceptions
   * @TODO - check the rationale behind this and modify test as appropriate]
   */
  public final void testThisShouldFailHorribly() {
    JobController jobController = new JobController();
    //    try {
    jobController.submitJob("Load of garbage");
    fail("Expect an Astrogrid exception here");
    //    } catch (AstroGridException expected) {
    //      return;
    //    }
  }

  /**
   *  A string of valid XML to submit as a job
   *  
   */
  private String goodJobXML;
  /**
   * Try submitting a valid Job
   * @TODO - put some tests here
   */
  public final void testSubmitJob() {
    JobController jobController = new JobController();
    String result = jobController.submitJob(goodJobXML);
  }
  /**
   * load a resource file into a string - pinched from datacenter test code
   * @param resource file to be found
   * @param callee a class in the same directory as the resource we're after
   * @return contents of that file as a string
   * @throws IOException if something goes pear shaped trying to find the file
   * @TODO refactor this away
   */
  private static String getResourceAsString(
    final String resource,
    final Class callee)
    throws IOException {
    InputStream is = callee.getResourceAsStream(resource);
    assertNotNull(is);
    String script = streamToString(is);
    return script;
  }
  /**
   * Takes the contents of an InputStream and turns it into a string
   * @param is said InputStream
   * @return said String
   * @throws IOException if there's a problem with the InputStream
   */
  private static String streamToString(final InputStream is)
    throws IOException {
    BufferedReader r = new BufferedReader(new InputStreamReader(is));
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    String line = null;
    while ((line = r.readLine()) != null) {
      pw.println(line);
    }
    pw.close();
    r.close();
    String str = sw.toString();
    assertNotNull(str);
    return str;
  }
}

/*
*$Log: JobControllerTest.java,v $
*Revision 1.1  2003/10/29 16:42:52  jdt
*Initial commit of some JobController test files, and mods to the config file
*so that they get picked up.
*
*/