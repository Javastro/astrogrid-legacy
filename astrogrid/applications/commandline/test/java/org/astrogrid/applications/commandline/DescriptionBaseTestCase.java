/*
 * $Id: DescriptionBaseTestCase.java,v 1.3 2004/09/23 22:44:23 pah Exp $
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

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class DescriptionBaseTestCase extends TestCase {


    /**
     * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
     * @version $Name:  $
     * @since iteration6
     */
    public interface TestAppInfo {
        public abstract String getAppName();

        public abstract String getConfigFileName();
    };
    
    protected TestAppInfo appInfo = new TestAppInfo()
    {

        
        private final String appName = TestAppConst.TESTAPP_NAME;
        private final String configFileName = "/TestApplicationConfig.xml";
        public String getAppName() {
            return appName;
        }
        public String getConfigFileName() {
            return configFileName;
        }

    };
   /**
    * 
    */
   public DescriptionBaseTestCase() {
      super();
   }

   /**
    * @param arg0
    */
   public DescriptionBaseTestCase(String arg0) {
      super(arg0);
   }
   
   public DescriptionBaseTestCase(TestAppInfo info, String arg0)
   {
       super(arg0);
       appInfo = info;
   }


   protected void setUp() throws Exception {
      super.setUp();      
      inputFile = this.getClass().getResource(appInfo.getConfigFileName());
      assertNotNull("application config file not found:", inputFile);
      int i = appInfo.getAppName().indexOf("/");
      
      if (i != -1) {
         TESTAPPNAME = appInfo.getAppName().substring(i+1);
      }
      else
      {
         TESTAPPNAME = appInfo.getAppName();
      }
   }


   protected URL inputFile;

   protected String TESTAPPNAME;

   
}
