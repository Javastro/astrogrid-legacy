/*$Id: MetadataTest.java,v 1.1 2004/10/08 15:52:18 mch Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.integration.serverside;

import java.io.IOException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.queriers.test.SampleStarsPlugin;
import org.astrogrid.registry.RegistryException;

/**
 * Test metadata integration stuff
 *
 */
public class MetadataTest extends TestCase {


   /** Tests push to registry */
   public void testPush() throws RegistryException, IOException  {
      
      //make sure we have the right plugin for the sample stars
      SampleStarsPlugin.initConfig();

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
Revision 1.1  2004/10/08 15:52:18  mch
More tests for Registry push etc

Revision 1.3  2004/10/07 10:48:03  mch
Reintroduced push test

Revision 1.2  2004/09/09 11:04:05  mch
Removed test for the moment

Revision 1.1  2004/09/08 20:06:11  mch
Added metadat push test

 
*/

