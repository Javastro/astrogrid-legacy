/*$Id: JavaClassCEAComponentManagerTest.java,v 1.4 2008/09/03 14:19:08 pah Exp $
 * Created on 10-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.component;


import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.test.AbstractComponentManagerTestCase;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 11 Jun 2008
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"/cecspringTest.xml"}) 
public class JavaClassCEAComponentManagerTest extends AbstractComponentManagerTestCase {
   /**
     * Constructor for JavaClassCEAComponentManagerTest.
     * @param arg0
     */
       
       
    public void testUseQuerier() {
       QueryService queryService = manager.getQueryService();
      assertNotNull(queryService);
      //test something about the querier.
      logger.info("there should be an exception thrown next");
      try {
         queryService.getLogFile("any", ApplicationEnvironmentRetriver.StdIOType.out);
         fail("The log file fetcher should not work for a javaClass app");
   
      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (CeaException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
     }

}


/* 
$Log: JavaClassCEAComponentManagerTest.java,v $
Revision 1.4  2008/09/03 14:19:08  pah
result of merge of pah_cea_1611 branch

Revision 1.3.2.2  2008/08/02 13:33:41  pah
safety checkin - on vacation

Revision 1.3.2.1  2008/05/13 15:14:08  pah
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708

Revision 1.3  2008/02/12 12:10:56  pah
build with 1.0 registry and filemanager clients

Revision 1.2  2007/02/19 16:20:21  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:29:18  gtr
no message

Revision 1.6  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.4  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.3.38.1  2006/01/31 21:39:07  gtr
Refactored. I have altered the configuration code slightly so that the JUnit tests can impose a Configuration instance to configure the tests. I have also fixed up almost all the bad tests for commandline and http.

Revision 1.3  2005/07/05 08:27:00  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.2.172.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.2.158.1  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/