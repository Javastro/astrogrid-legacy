/*
 * $Id: HttpMetadataServiceTest.java,v 1.3 2006/01/09 17:52:36 clq2 Exp $
 * 
 * Created on 14-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.exolab.castor.xml.CastorException;

import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.http.test.TestRegistryQuerier;
import org.astrogrid.applications.manager.DefaultMetadataService;
import org.astrogrid.applications.manager.DefaultMetadataService.URLs;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.registry.beans.v10.cea.CeaServiceType;
import org.astrogrid.registry.beans.v10.resource.Resource;
import org.astrogrid.registry.beans.v10.wsinterface.VOResources;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pharriso@eso.org) 14-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class HttpMetadataServiceTest extends TestCase {

   private HttpMetadataService metadataService;

   private HttpApplicationDescriptionLibrary httpApplicationDescriptionLibrary;

   public static void main(String[] args) {
      junit.textui.TestRunner.run(HttpMetadataServiceTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      URLs urls = new DefaultMetadataService.URLs() {

         public URL getRegistryTemplate() {
            return EmptyCEAComponentManager.class
                  .getResource("/CEARegistryTemplate.xml");
         }

         public URL getServiceEndpoint() {
            try {
               return new URL("http://locahost/metadatatest");
            } catch (MalformedURLException e) {
               fail("test endpoint for service not specified properly");

            }
            return null;
         }
      };
      IdGen id = new InMemoryIdGen();
      DefaultProtocolLibrary lib = new DefaultProtocolLibrary();
      TestAuthority resol = new TestAuthority();
      ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(
            id, lib, resol);
      httpApplicationDescriptionLibrary = new HttpApplicationDescriptionLibrary(
            new TestRegistryQuerier(null), env);

      metadataService = new HttpMetadataService(
            httpApplicationDescriptionLibrary, urls);
   }
   
   public void testGetMetadata() throws Exception {
      VOResources desc = metadataService.getVODescription();
      assertNotNull("metadata is null", desc);
      assertEquals("should only be one resource",1, desc.getResourceCount());
      Resource resource = desc.getResource(0);
      assertTrue("resource should be CeaService" ,resource instanceof CeaServiceType);
      CeaServiceType ceaservice = (CeaServiceType) resource;
      assertTrue("should manage at least one application", ceaservice.getManagedApplications().getApplicationReferenceCount() > 0);
   }

}

/*
 * $Log: HttpMetadataServiceTest.java,v $
 * Revision 1.3  2006/01/09 17:52:36  clq2
 * gtr_1489_apps
 *
 * Revision 1.2.34.1  2005/12/22 13:56:03  gtr
 * Refactored to match the other kinds of CEC.
 *
 * Revision 1.2  2005/07/05 08:26:56  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.2.1  2005/06/14 09:49:32  pah
 * make the http cec only register itself as a ceaservice - do not try to reregister any cea applications that it finds
 *
 */
