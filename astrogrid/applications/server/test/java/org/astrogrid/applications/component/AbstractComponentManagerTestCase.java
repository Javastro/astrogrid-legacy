/*
 * $Id: AbstractComponentManagerTestCase.java,v 1.8 2006/03/17 17:50:58 clq2 Exp $
 * 
 * Created on 03-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.component;

import java.io.FileNotFoundException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.registry.RegistryUploader;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.BaseConfiguration;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver.StdIOType;
import org.astrogrid.config.SimpleConfig;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Defines what is common to component manager tests.
 * @author Paul Harrison (pharriso@eso.org) 03-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public abstract class AbstractComponentManagerTestCase extends TestCase {

   /**
    * 
    */
   public AbstractComponentManagerTestCase() {
      this("nothing");
      // TODO Auto-generated constructor stub
   }

   /**
    * @param arg0
    */
   public AbstractComponentManagerTestCase(String arg0) {
      super(arg0);

     
   }

   /**
    * Logger for this class
    */
   protected static final Log logger = LogFactory
            .getLog(AbstractComponentManagerTestCase.class);

   protected void setUp() throws Exception {
       manager = this.createManager();
       this.configureManager();
       manager.start();
    }

    protected void tearDown() throws Exception {
        manager.stop();
    }
   
   protected CEAComponentManager manager;

   public void testIsValid() {
        manager.getContainer().verify();
    }

   public void testGetController() {
        assertNotNull(manager.getExecutionController());
    }

   public void testGetMetaData() throws CeaException {
      MetadataService metadataService = manager.getMetadataService();
      assertNotNull(metadataService);
      Document regentry = metadataService.returnRegistryEntry();
    }

   public void testGetQuerier() {
       QueryService queryService = manager.getQueryService();
      assertNotNull(queryService);
     }
   public void testGetUploader() {
       RegistryUploader serv =  manager.getRegistryUploaderService();
       assertNotNull(serv);
   }
   abstract protected CEAComponentManager createManager();
   abstract protected void configureManager() throws Exception;
   
   public void testInformation() {
        System.out.println(manager.information());
    }
   
   public void testGetSuite() {
       Test ts = manager.getSuite();
       assertNotNull(ts);
   }

}


/*
 * $Log: AbstractComponentManagerTestCase.java,v $
 * Revision 1.8  2006/03/17 17:50:58  clq2
 * gtr_1489_cea correted version
 *
 * Revision 1.6  2006/03/07 21:45:26  clq2
 * gtr_1489_cea
 *
 * Revision 1.3.20.2  2006/01/31 21:39:07  gtr
 * Refactored. I have altered the configuration code slightly so that the JUnit tests can impose a Configuration instance to configure the tests. I have also fixed up almost all the bad tests for commandline and http.
 *
 * Revision 1.3.20.1  2005/12/18 14:48:25  gtr
 * Refactored to allow the component managers to pass their unit tests and the fingerprint JSP to work. See BZ1492.
 *
 * Revision 1.3  2005/08/10 17:45:10  clq2
 * cea-server-nww-improve-tests
 *
 * Revision 1.2.8.1  2005/07/21 18:12:38  nw
 * fixed up tests - got all passing, improved coverage a little.
 * still could do with testing the java apps.
 *
 * Revision 1.2  2005/07/05 08:27:00  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.2  2005/06/08 22:10:45  pah
 * make http applications v10 compliant
 *
 * Revision 1.1.2.1  2005/06/03 16:01:48  pah
 * first try at getting commandline execution log bz#1058
 *
 */
