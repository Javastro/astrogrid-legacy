/*
 * $Id: AVODemoSetup.java,v 1.2 2004/02/09 22:43:28 pah Exp $
 * 
 * Created on 23-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.avodemo;

import java.io.IOException;
import java.util.Vector;

import org.astrogrid.applications.common.config.ApplicationControllerConfig;
import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class AVODemoSetup {

   private MySpaceClient myspacedelegate;

   /**
    * 
    */
   public AVODemoSetup() {
      try {
         Vector servers = new Vector();
         servers.add("serv1");
         myspacedelegate = ApplicationControllerConfig.getInstance().getMySpaceManager();
         User user = new User(AVODemoConstants.ACCOUNT, AVODemoConstants.GROUP, AVODemoConstants.TOKEN);
         myspacedelegate.createUser(user.getUserId(), user.getCommunity(), user.getToken(), servers);
         
         // start adding the necessary files....
      }
      catch (IOException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) {
      AVODemoSetup setup = new AVODemoSetup();
      
   }        
}
