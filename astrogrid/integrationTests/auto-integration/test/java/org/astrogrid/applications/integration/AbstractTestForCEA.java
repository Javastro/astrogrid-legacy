/*
 * $Id: AbstractTestForCEA.java,v 1.1 2004/07/01 11:43:33 nw Exp $
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

import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.scripting.Service;

import java.util.Iterator;
import java.util.List;

/** Abstract class for all cea server tests - provides a delegate pointing to the required server.
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-May-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class AbstractTestForCEA extends AbstractTestForIntegration {

   /**
    * @param arg0
    */
   public AbstractTestForCEA(String searchString,String arg0) {
      super(arg0);
      this.searchString = searchString;
   }

    /** string to search in service list for */
    protected final String searchString;

   protected void setUp() throws Exception {
        super.setUp();
        List apps = ag.getApplications();
        assertNotNull(apps);
        assertTrue("no application servers found",apps.size() > 0);
        Iterator apps1 = apps.iterator();
        while(apps1.hasNext()) {
            Service s = (Service)apps1.next();
            if (s.getDescription().indexOf(searchString) != -1) {
                serv = s;
            }
        }
        if (serv == null) {
            fail("failed to find service matching '" + searchString + "'");
        }      
        delegate = (CommonExecutionConnectorClient)serv.createDelegate();
    }

   protected Service serv;

   protected CommonExecutionConnectorClient delegate;

}
