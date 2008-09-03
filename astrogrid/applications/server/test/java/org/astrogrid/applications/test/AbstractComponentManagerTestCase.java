/*
 * $Id: AbstractComponentManagerTestCase.java,v 1.2 2008/09/03 14:19:08 pah Exp $
 * 
 * Created on 03-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponentContainer;
import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.registry.RegistryUploader;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.contracts.SchemaMap;
import org.astrogrid.test.AstrogridAssert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

/**
 * Defines what is common to component manager tests. It uses spring configuration to fill the container. 
 * Tests using this should also include a @ContextConfiguration(locations=) with the location of a spring configuration file that 
 * configures the plugable components. See the cecspringTest.xml file for an example of what needs to be configured.
 * 
 *
 * @author Paul Harrison (pharriso@eso.org) 03-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"/org/astrogrid/applications/component/ceaspring.xml"}) 
public abstract class AbstractComponentManagerTestCase extends AbstractJUnit4SpringContextTests {

 
   /**
    * Logger for this class
    */
   protected static final Log logger = LogFactory
            .getLog(AbstractComponentManagerTestCase.class);
   
   @BeforeClass
   public static void beforeClass(){
       
   }

   @Before
   public void setUp() throws Exception {
       manager = CEAComponentContainer.getInstance();
   }

   @After
   public void tearDown() throws Exception {
        //nothing to do at the moment
    }
   
   protected  CEAComponents manager;

   @Test
   public void testGetController() {
        assertNotNull(manager.getExecutionController());
    }
   @Test
   public void testGetRegistryEntry() throws CeaException {
       MetadataService metadataService = manager.getMetadataService();
       assertNotNull(metadataService);
       Document regentry = metadataService.returnRegistryEntry();
       assertNotNull(regentry);
       XMLUtils.PrettyDocumentToStream(regentry, System.out);
       AstrogridAssert.assertSchemaValid(regentry, "VOResources", SchemaMap.ALL);
     }
   @Test
   public void testGetAppliationEntry() throws CeaException {
       MetadataService metadataService = manager.getMetadataService();
       assertNotNull(metadataService);
       String ivorns[] = metadataService.getApplicationIvorns();
       for (int i = 0; i < ivorns.length; i++) {
	   Document regentry = metadataService.getApplicationDescription(ivorns[i]);
	   assertNotNull(regentry);
	   AstrogridAssert.assertSchemaValid(regentry, "Resource", SchemaMap.ALL);
       }
   }

   @Test
   public void testGetServerDescription() throws CeaException {
       MetadataService metadataService = manager.getMetadataService();
       assertNotNull(metadataService);
       Document regentry = metadataService.getServerDescription();
       assertNotNull(regentry);
       AstrogridAssert.assertSchemaValid(regentry, "Resource", SchemaMap.ALL);
     }

   public void testGetAppNames() throws CeaException {
       MetadataService metadataService = manager.getMetadataService();
       assertNotNull(metadataService);
       String[] regentry = metadataService.getApplicationIvorns();
       assertNotNull(regentry);
       assertTrue("must have at least one app defined", regentry.length > 0);
     }

   
   @Test
   public void testGetQuerier() {
       QueryService queryService = manager.getQueryService();
      assertNotNull(queryService);
     }
   
   @Test
   public void testGetUploader() {
       RegistryUploader serv =  manager.getRegistryUploaderService();
       assertNotNull(serv);
   }
      
   @Test
   public void testInformation() {
        System.out.println(manager.information());
    }
   
   @Test
   public void testGetSuite() {
       junit.framework.Test ts = manager.getSuite();
       assertNotNull(ts);
   }

}


/*
 * $Log: AbstractComponentManagerTestCase.java,v $
 * Revision 1.2  2008/09/03 14:19:08  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.3  2008/09/03 12:22:55  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1.2.2  2008/08/29 07:28:31  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/05/01 15:22:47  pah
 * updates to tool
 *
 * Revision 1.1.2.5  2008/04/08 14:45:10  pah
 * Completed move to using spring as container for webapp - replaced picocontainer
 *
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 *
 * Revision 1.1.2.4  2008/04/04 15:46:08  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 * Revision 1.1.2.3  2008/04/01 13:50:08  pah
 * http service also passes unit tests with new jaxb metadata config
 *
 * Revision 1.1.2.2  2008/03/27 13:34:36  pah
 * now producing correct registry documents
 *
 * Revision 1.1.2.1  2008/03/26 17:15:38  pah
 * Unit tests pass
 *
 * Revision 1.1  2008/02/12 12:10:55  pah
 * build with 1.0 registry and filemanager clients
 *
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
