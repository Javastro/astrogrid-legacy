/*$Id: JesIntegrationTest.java,v 1.1 2004/03/16 17:48:34 nw Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.delegate.JobController;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.scripting.Service;

import java.util.List;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class JesIntegrationTest extends AbstractTestForIntegration {
    /**
     * Constructor for JesIntegrationTest.
     * @param arg0
     */
    public JesIntegrationTest(String arg0) throws Exception{
        super(arg0);
    }
    protected void setUp() throws Exception {
        super.setUp();
        List apps = ag.getJes();
        assertNotNull(apps);
        assertTrue(apps.size() > 0);
        serv = (Service)apps.get(0);
        delegate = (JobController)serv.createDelegate();
    }
    protected Service serv;
    protected JobController delegate;
    
    public void testReadJobList() throws Exception{
        Account acc = ag.getObjectHelper().createAccount("noel","jodrell");
        JobSummary[] arr = delegate.readJobList(acc);
        assertNotNull(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i].getName());
        }
    }
    

}


/* 
$Log: JesIntegrationTest.java,v $
Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/