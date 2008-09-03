/*
 * $Id: TestRealRegistryQuerierImpl.java,v 1.3 2008/09/03 14:18:38 pah Exp $
 * 
 * Created on 07-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.http.registry;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;
import org.astrogrid.applications.description.registry.RegistryQueryLocator;
import org.astrogrid.applications.http.test.TestRegistryLocator;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;

import junit.framework.TestCase;

/**
 * Test against a real registry. This is not intended to be run as unit test (hence name means will not be run by maven) - just to investigate latest registry behaviour. Is really an integration test.
 * @author Paul Harrison (pharriso@eso.org) 07-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class TestRealRegistryQuerierImpl extends TestCase {

   public static void main(String[] args) {
      junit.textui.TestRunner.run(TestRealRegistryQuerierImpl.class);
   }

   private static class RealRegistry implements RegistryQueryLocator{
      /* (non-Javadoc)
       * @see org.astrogrid.applications.description.registry.RegistryQueryLocator#getClient()
       */
      public RegistryService getClient() throws RegistryException {
         try {
            return RegistryDelegateFactory.createQuery(new URL("http://localhost:8080/astrogrid-registry-SNAPSHOT/services/RegistryQuery"));
         } catch (MalformedURLException e) {
           throw new RegistryException("cannot creat delegare", e);
         }
      }
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

   public void testListApplications() throws RegistryException {
      
      RegistryQuerierImpl rq = new RegistryQuerierImpl(new RealRegistry());
      List l = rq.listApplications();
      assertTrue("did not find any applications", !l.isEmpty());
   }
   
   public void testGetApplications() throws IOException
   {
      RegistryQuerierImpl rq = new RegistryQuerierImpl(new RealRegistry());
      Collection l = rq.getHttpApplications();
      assertNotNull(l);
      assertTrue("did not find any applicattions", !l.isEmpty());
      for (Iterator iter = l.iterator(); iter.hasNext();) {
         CeaHttpApplicationDefinition app = (CeaHttpApplicationDefinition) iter.next();
         
      }
   }

}


/*
 * $Log: TestRealRegistryQuerierImpl.java,v $
 * Revision 1.3  2008/09/03 14:18:38  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.2.86.2  2008/08/02 13:32:32  pah
 * safety checkin - on vacation
 *
 * Revision 1.2.86.1  2008/04/01 13:50:08  pah
 * http service also passes unit tests with new jaxb metadata config
 *
 * Revision 1.2  2005/07/05 08:27:01  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/06/08 22:10:46  pah
 * make http applications v10 compliant
 *
 */
