/*
 * $Id: AbstractTestForApplications.java,v 1.1 2004/05/17 12:37:31 pah Exp $
 * 
 * Created on 11-May-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.integration;

import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.scripting.Service;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-May-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class AbstractTestForApplications extends AbstractTestForIntegration {

   /**
    * @param arg0
    */
   public AbstractTestForApplications(String arg0) {
      super(arg0);
      // TODO Auto-generated constructor stub
   }

   protected void setUp() throws Exception {
        super.setUp();
        List apps = ag.getApplications();
        assertNotNull(apps);
        assertTrue("no application servers found",apps.size() > 0);
        serv = findRequiredService(apps.iterator());        
        delegate = (CommonExecutionConnectorClient)serv.createDelegate();
    }

   protected Service serv;

   protected CommonExecutionConnectorClient delegate;

   protected Service findRequiredService(Iterator apps) {
        while( apps.hasNext() ) { // find the correct one
            Service s = (Service)apps.next();
            if (s.getDescription().indexOf("command-line") != -1) {
                return s;
            }           
        }
        fail("failed to find command-line service");
        // never reached.
        return null;
    }

   protected String applicationName() {
        return TESTAPP2;
    }

}
