/*$Id: AllTests.java,v 1.3 2005/07/05 08:27:01 clq2 Exp $
 * Created on 17-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.applications.commandline.digester;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
 *
 */
public class AllTests {
   public static void main(String[] args) {
      junit.textui.TestRunner.run(AllTests.class);
   }
   public static Test suite() {
      TestSuite suite = new TestSuite(
         "Test for org.astrogrid.applications.commandline.digester");
      //$JUnit-BEGIN$
      suite.addTestSuite(CommandLineDescriptionsLoaderTest.class);
      suite.addTestSuite(RegistryEntryBuilderTest.class);
      //$JUnit-END$
      return suite;
   }
}

/* 
 $Log: AllTests.java,v $
 Revision 1.3  2005/07/05 08:27:01  clq2
 paul's 559b and 559c for wo/apps and jes

 Revision 1.2.166.1  2005/06/09 08:47:32  pah
 result of merging branch cea_pah_559b into HEAD

 Revision 1.2.152.1  2005/06/02 14:57:28  pah
 merge the ProvidesVODescription interface into the MetadataService interface

 Revision 1.2  2004/07/01 11:07:59  nw
 merged in branch
 nww-itn06-componentization

 Revision 1.1.2.1  2004/07/01 01:43:39  nw
 final version, before merge
 
 */