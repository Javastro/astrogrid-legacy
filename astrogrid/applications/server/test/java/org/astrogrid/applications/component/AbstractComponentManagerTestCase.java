/*
 * $Id: AbstractComponentManagerTestCase.java,v 1.5 2006/01/10 11:26:52 clq2 Exp $
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
import org.astrogrid.applications.component.EmptyCEAComponentManager.VerifyRequiredComponents;
import org.astrogrid.applications.description.registry.RegistryUploader;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
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
       setupConfigComponentManager();
       manager = createManager();
       manager.start();
         
    }

    protected void tearDown() throws Exception {
        manager.stop();
    }
   
   protected CEAComponentManager manager;

   public void testIsValid() {
        manager.getContainer().verify();
    }

   public void testVerifyRequiredComponents() {
        assertNotNull(manager.getContainer().getComponentInstanceOfType(EmptyCEAComponentManager.VerifyRequiredComponents.class));
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
   abstract protected void setupConfigComponentManager() ;
   abstract protected CEAComponentManager createManager();
   public void testInformation() {
        System.out.println(manager.information());
    }
   
   public void testGetSuite() {
       Test ts = manager.getSuite();
       assertNotNull(ts);
   }

   /**
    * 
    */
   public static void basicConfig() {
      URL registryURL = JavaClassCEAComponentManagerTest.class.getResource("/CEARegistryTemplate.xml");
         assertNotNull(registryURL);
        SimpleConfig.getSingleton().setProperty(EmptyCEAComponentManager.REGISTRY_TEMPLATE_URL,registryURL.toString());
         SimpleConfig.getSingleton().setProperty(EmptyCEAComponentManager.SERVICE_ENDPOINT_URL,"http://astrogrid.unit.test");
   }

}


/*
 * $Log: AbstractComponentManagerTestCase.java,v $
 * Revision 1.5  2006/01/10 11:26:52  clq2
 * rolling back to before gtr_1489
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
