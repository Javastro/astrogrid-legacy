/* $Id: DummyJobFactoryImpl.java,v 1.1 2003/10/29 16:42:52 jdt Exp $
 * Created on 29-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.impl;

import java.util.Iterator;
import java.util.ListIterator;

import org.w3c.dom.Document;

import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.JobFactory;

/**
 * @author jdt
 *
 * A dummy implementation of a JobFactory to allow us to test the JobController
 */
public class DummyJobFactoryImpl implements JobFactory {

  /* (non-Javadoc)
   * @see org.astrogrid.jes.job.JobFactory#begin()
   */
  public void begin() {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.astrogrid.jes.job.JobFactory#end(boolean)
   */
  public boolean end(boolean bCommit) throws JobException {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see org.astrogrid.jes.job.JobFactory#createJob(org.w3c.dom.Document, java.lang.String)
   */
  public Job createJob(Document jobDoc, String jobXML) throws JobException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.astrogrid.jes.job.JobFactory#findJobsWhere(java.lang.String)
   */
  public Iterator findJobsWhere(String queryString) throws JobException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.astrogrid.jes.job.JobFactory#findJob(java.lang.String)
   */
  public Job findJob(String jobURN) throws JobException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.astrogrid.jes.job.JobFactory#findUserJobs(java.lang.String, java.lang.String, java.lang.String)
   */
  public ListIterator findUserJobs(
    String userid,
    String community,
    String jobListXML)
    throws JobException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.astrogrid.jes.job.JobFactory#deleteJob(org.astrogrid.jes.job.Job)
   */
  public String deleteJob(Job job) throws JobException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.astrogrid.jes.job.JobFactory#updateJob(org.astrogrid.jes.job.Job)
   */
  public void updateJob(Job job) throws JobException {
    // TODO Auto-generated method stub

  }

}

/*
*$Log: DummyJobFactoryImpl.java,v $
*Revision 1.1  2003/10/29 16:42:52  jdt
*Initial commit of some JobController test files, and mods to the config file
*so that they get picked up.
*
*/