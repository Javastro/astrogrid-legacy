/* $Id: AllTests.java,v 1.2 2004/06/14 23:08:52 jdt Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.store;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.store.delegate.FactoryTest;
import org.astrogrid.store.delegate.LocalFileStoreTest;
import org.astrogrid.store.delegate.VoSpaceResolverTest;

/**
 */

public class AllTests {
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
   
   public static Test suite() {
      TestSuite suite = new TestSuite("Test for org.astrogrid.store");

      suite.addTest(AgslTest.suite());
      suite.addTest(MsrlTest.suite());
      
      suite.addTest(FactoryTest.suite());
      suite.addTest(LocalFileStoreTest.suite());
      suite.addTest(VoSpaceResolverTest.suite());
      
      return suite;
   }
}

/*
 $Log: AllTests.java,v $
 Revision 1.2  2004/06/14 23:08:52  jdt
 Merge from branches
 ClientServerSplit_JDT
 and
 MySpaceClientServerSplit_JDT
 
 MySpace now split into a client/delegate jar
 astrogrid-myspace-<version>.jar
 and a server/manager war
 astrogrid-myspace-server-<version>.war

 Revision 1.1.2.1  2004/06/14 22:33:21  jdt
 Split into delegate jar and server war.  
 Delegate: astrogrid-myspace-SNAPSHOT.jar
 Server/Manager: astrogrid-myspace-server-SNAPSHOT.war
 
 Package names unchanged.
 If you regenerate the axis java/wsdd/wsdl files etc you'll need
 to move some files around to ensure they end up in the client
 or the server as appropriate.
 As of this check-in the tests/errors/failures is 162/1/22 which
 matches that before the split.

 Revision 1.3  2004/03/15 17:26:18  mch
 Added Vospace resolver test

 Revision 1.2  2004/03/12 13:20:28  mch
 Moved Ivorn to common

 Revision 1.1  2004/03/01 22:35:09  mch
 Tests for StoreClient

 */
