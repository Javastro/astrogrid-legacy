/*
 * $Id: RegistryUploaderTest.java,v 1.4 2004/04/01 13:54:54 pah Exp $
 * 
 * Created on 24-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.registry;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import org.astrogrid.applications.description.DescriptionBaseTestCase;
import org.astrogrid.applications.manager.externalservices.RegistryAdminFromConfig;
import org.astrogrid.applications.manager.externalservices.RegistryAdminLocator;
import org.astrogrid.applications.manager.externalservices.RegistryFromConfig;
import org.astrogrid.applications.manager.externalservices.RegistryQueryLocator;
import org.astrogrid.applications.manager.externalservices.ServiceNotFoundException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.query.RegistryService;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 24-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryUploaderTest extends RegEntryBaseTestCase {

   /**
    * Constructor for RegistryUploaderTest.
    * @param arg0
    */
   public RegistryUploaderTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(RegistryUploaderTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

   final public void testWrite() throws MarshalException, ValidationException, ServiceNotFoundException, RegistryException {
      VODescription regentry = builder.makeEntry();
      assertNotNull(regentry);
      RegistryAdminLocator reglocator = new RegistryAdminFromConfig(config);
      assertNotNull(reglocator);
      RegistryUploader reguploader = new RegistryUploader(regentry, reglocator);
      assertNotNull(reguploader);
      reguploader.write();
      
      // try to get the entries back...
      String selectQuery = "<query><selectionSequence>" +
      "<selection item='searchElements' itemOp='EQ' value='Resource'/>" +
      "<selectionOp op='$and$'/>" +
      "<selection item='@*:type' itemOp='EQ' value='CeaApplicationType'/>"  +
      "<selectionOp op='OR'/>" +
      "<selection item='@*:type' itemOp='EQ' value='CeaServiceType'/>"  +
      "</selectionSequence></query>";

          
           RegistryQueryLocator rqloc = new RegistryFromConfig(config);
           RegistryService rquery = rqloc.getClient();
           VODescription result = rquery.submitQueryString(selectQuery);
           assertNotNull(result);


      
   }

}
