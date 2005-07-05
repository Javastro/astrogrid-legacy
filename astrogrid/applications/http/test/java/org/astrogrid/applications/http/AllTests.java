/*$Id: AllTests.java,v 1.5 2005/07/05 08:26:56 clq2 Exp $
 * Created on 29-Julyish-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.applications.http;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * Auto generated from Eclipse
 * @author jdt/Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class AllTests {
   public static void main(String[] args) {
      junit.textui.TestRunner.run(AllTests.class);
   }
   public static Test suite() {
      TestSuite suite = new TestSuite(
         "Test for org.astrogrid.applications.http");
      //$JUnit-BEGIN$
      suite.addTestSuite(HttpApplicationCEAServerTest.class);
      suite.addTestSuite(HttpApplicationProviderTest.class);
      suite.addTestSuite(HttpApplicationDescriptionTest.class);
      suite.addTestSuite(HttpApplicationDescriptionLibraryTest.class);
      suite.addTestSuite(HttpServiceClientTest.class);
      suite.addTestSuite(HttpMetadataServiceTest.class);
      //$JUnit-END$
      suite.addTest(org.astrogrid.applications.http.registry.AllTests.suite());
      return suite;
   }
}
/* 
 $Log: AllTests.java,v $
 Revision 1.5  2005/07/05 08:26:56  clq2
 paul's 559b and 559c for wo/apps and jes

 Revision 1.4.114.2  2005/06/14 09:49:32  pah
 make the http cec only register itself as a ceaservice - do not try to reregister any cea applications that it finds

 Revision 1.4.114.1  2005/06/09 08:47:32  pah
 result of merging branch cea_pah_559b into HEAD

 Revision 1.4.100.1  2005/06/08 22:10:45  pah
 make http applications v10 compliant

 Revision 1.4  2004/09/01 15:42:26  jdt
 Merged in Case 3

 Revision 1.1.2.3  2004/08/15 10:58:19  jdt
 more test updates

 Revision 1.1.2.2  2004/07/30 16:59:50  jdt
 limping along.


 
 */