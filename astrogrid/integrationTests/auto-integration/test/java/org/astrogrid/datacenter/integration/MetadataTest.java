/*$Id: MetadataTest.java,v 1.1 2004/09/08 20:06:11 mch Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.integration;

import java.io.IOException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;

/**
 * Test metadata integration stuff
 *
 */
public class MetadataTest extends TestCase {


   /** Tests push to registry */
   public void testPush() throws RegistryException, IOException  {
      SimpleConfig.getSingleton().setProperty(
            RegistryDelegateFactory.ADMIN_URL_PROPERTY,
            "http://localhost:8080/astrogrid-registry-SNAPSHOT/services/AdminService"
      );
      String[] wentTo = VoDescriptionServer.pushToRegistry();
      assertTrue("Didn't get sent anywhere", wentTo.length>0);
   }
   
    /**/

   public static void main(String[] args) {
      junit.textui.TestRunner.run(new TestSuite(MetadataTest.class));
   }
}


/*
$Log: MetadataTest.java,v $
Revision 1.1  2004/09/08 20:06:11  mch
Added metadat push test

 
*/
