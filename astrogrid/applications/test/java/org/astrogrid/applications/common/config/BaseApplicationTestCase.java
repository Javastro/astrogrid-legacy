/*
 * $Id: BaseApplicationTestCase.java,v 1.1 2003/12/31 00:56:17 pah Exp $
 * 
 * Created on 30-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.common.config;

import java.util.Vector;

import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class BaseApplicationTestCase extends BaseDBTestCase {

   protected MySpaceClient mySpaceManager;
   protected User user;

   /**
    * 
    */
   public BaseApplicationTestCase() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param name
    */
   public BaseApplicationTestCase(String name) {
      super(name);
      // TODO Auto-generated constructor stub
   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      Vector servers = null;
      super.setUp();
      mySpaceManager = ApplicationControllerConfig.getInstance().getMySpaceManager();
      user = new User();
      String userId = user.getAccount();
      String communityId = user.getGroup();
      String credential = user.getToken();
      mySpaceManager.createUser(userId, communityId, credential, servers);
      mySpaceManager.saveDataHolding(userId, communityId, credential, "testInFile", "This is some test contents for myspace", "data", mySpaceManager.OVERWRITE);
      
      
   }
   

}
