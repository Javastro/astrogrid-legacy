/*
 * $Id: AbstractTestForCEA.java,v 1.5 2004/11/24 19:49:22 clq2 Exp $
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

import org.apache.log4j.Logger;

import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.scripting.Service;

import java.util.Iterator;
import java.util.List;

/** Abstract class for all cea server tests - inplements provides a delegate pointing to the required server.
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-May-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class AbstractTestForCEA extends AbstractTestForIntegration {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(AbstractTestForCEA.class);

   /**
    * @param searchSring - key to search in service list for endpoint to communicate to.
    */
   public AbstractTestForCEA(String searchString,String arg0) {
      super(arg0);
      this.searchString = searchString;
   }

    /** string to search in service list for */
    protected final String searchString;
    /** searches for service endpoint*/
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
		logger.debug("setUp() - Creating delegate for server with end point:"+serv.getEndpoint());
        delegate = (CommonExecutionConnectorClient)serv.createDelegate();
        
    }
   /** record of service the test will connect to - initialized in {@link #setUp()} */
   protected Service serv;
/** delegate to service - configured in {@link #setUp()}*/
   protected CommonExecutionConnectorClient delegate;

}
