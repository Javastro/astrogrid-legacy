/*
 * $Id: RegistryBaseTestCase.java,v 1.1 2004/05/07 15:32:36 pah Exp $
 * 
 * Created on 07-May-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.registry.integration;

import java.util.List;

import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.scripting.Astrogrid;
import org.astrogrid.scripting.Service;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 07-May-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class RegistryBaseTestCase extends TestCase {

   protected RegistryService delegate;
   protected final static String AUTHORITY_ID = "org.astrogrid.localhost";
   protected final static String RESOURCE_KEY="dummy/entry";

   protected Service service;
   protected RegistryAdminService adminDelegate;

   /**
    * @param arg0
    */
   public RegistryBaseTestCase(String arg0) {
      super(arg0);

   }

   protected void setUp() throws Exception {
      Astrogrid ag = Astrogrid.getInstance();
      List regList = ag.getRegistries();
      assertNotNull(regList);
      assertTrue("no registry query endpoints", regList.size() > 0);
      service = (Service)regList.get(0);
      delegate = (RegistryService)service.createDelegate();
      assertNotNull(delegate);

      regList = ag.getRegistryAdmins();
      assertNotNull(regList);
      assertTrue("no registry admin endpoints", regList.size() > 0);
      service = (Service)regList.get(0);
      adminDelegate = (RegistryAdminService)service.createDelegate();
      assertNotNull(adminDelegate);

   }

}
