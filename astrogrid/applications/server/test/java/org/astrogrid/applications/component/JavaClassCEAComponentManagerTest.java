/*$Id: JavaClassCEAComponentManagerTest.java,v 1.5 2006/03/11 05:57:54 clq2 Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.apps.AppsCEAComponentManager;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.config.SimpleConfig;

import java.io.FileNotFoundException;
import java.net.URL;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Jun-2004
 *
 */
public class JavaClassCEAComponentManagerTest extends AbstractComponentManagerTestCase {
   /**
     * Constructor for JavaClassCEAComponentManagerTest.
     * @param arg0
     */
    public JavaClassCEAComponentManagerTest(String arg0) {
        super(arg0);
    }
    protected CEAComponentManager createManager() {
        return new JavaClassCEAComponentManager();
    }


    public  void setupConfigComponentManager() {
        basicConfig();
                
    }    
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
Revision 1.5  2006/03/11 05:57:54  clq2
roll back to before merged apps_gtr_1489, tagged as rolback_gtr_1489

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