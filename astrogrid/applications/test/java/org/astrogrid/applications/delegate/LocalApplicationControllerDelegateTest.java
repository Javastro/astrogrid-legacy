/*
 * $Id: LocalApplicationControllerDelegateTest.java,v 1.3 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 08-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.delegate;


/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class LocalApplicationControllerDelegateTest extends AbstractDelegateTest {

   /**
    * Constructor for ApplicationControllerDelegateTest.
    * @param arg0
    */
   
   // "http://localhost:8080/astrogrid-applications/services/ApplicationControllerService";
   public LocalApplicationControllerDelegateTest(String arg0) {
      super(arg0);
      endpoint = "local:///ApplicationControllerService";
      String[] args = {"-d","-l",endpoint, "/data/work/astrogrid/src/applications/generated/wsdd/ApplicationControllerService/deploy.wsdd"}; 
      org.apache.axis.client.AdminClient.main(args); 

      
   }

   public static void main(String[] args) {
      //TODO set the endpoint from here?
      junit.textui.TestRunner.run(LocalApplicationControllerDelegateTest.class);
   }

}
