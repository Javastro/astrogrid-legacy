/*
 * $Id: AVODemoSetup.java,v 1.3 2004/03/23 12:51:26 pah Exp $
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

import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.manager.externalservices.MySpaceFromConfig;
import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 * 
 */
public class AVODemoSetup {

   private MySpaceClient myspacedelegate;

   /**
     * Small class to indicate that we really do want to create a CeaControllerConfig
     * @author Paul Harrison (pah@jb.man.ac.uk) 19-Mar-2004
     * @version $Name:  $
     * @since iteration5
     */
    private static class ThisConfig extends CeaControllerConfig {
       public static CeaControllerConfig getInstance() {
          return CeaControllerConfig.getInstance();
       }
    }

   /**
    * 
    */
   public AVODemoSetup() {
      try {
         Vector servers = new Vector();
         servers.add("serv1");
         myspacedelegate = new MySpaceFromConfig(ThisConfig.getInstance()).getClient();
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
