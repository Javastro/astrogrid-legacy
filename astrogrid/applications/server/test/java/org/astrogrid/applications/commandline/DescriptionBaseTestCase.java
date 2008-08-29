/*
 * $Id: DescriptionBaseTestCase.java,v 1.1 2008/08/29 07:28:27 pah Exp $
 * 
 * Created on 04-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import java.net.URL;

import org.astrogrid.applications.test.AbstractComponentManagerTestCase;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"/ceaspringTest.xml"}) 
public abstract class DescriptionBaseTestCase extends AbstractComponentManagerTestCase {


    /**
     * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
     * @version $Name:  $
     * @since iteration6
     */
    public interface TestAppInfo {
        public abstract String getAppName();
        public abstract String getInterfaceName();
    };
    
    protected TestAppInfo appInfo = new TestAppInfo()
    {

        
        private final String appName = TestAppConst.TESTAPP_NAME;
        public String getAppName() {
            return appName;
        }
	public String getInterfaceName() {
	    return TestAppConst.MAIN_INTERFACE;
	}
 
    };
 
   @Before
   public void setUp() throws Exception {
      super.setUp();
      appInfo =setupApplication();
      TESTAPPNAME = appInfo.getAppName();
    }

   protected abstract TestAppInfo setupApplication();

 
   protected String TESTAPPNAME;

   
}
