/*
 * $Id: LocalApplicationControllerDelegateTest.java,v 1.5 2004/04/14 13:19:48 pah Exp $
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class LocalApplicationControllerDelegateTest extends AbstractDelegateTestCase {

   /**
    * Constructor for ApplicationControllerDelegateTest.
    * @param arg0
    */

   // "http://localhost:8080/astrogrid-applications/services/ApplicationControllerService";
   public LocalApplicationControllerDelegateTest(String arg0) {
      super(arg0);
      InputStream is =
         LocalApplicationControllerDelegateTest.class.getResourceAsStream(
            "/wsdd/CommonExecutionConnector-deploy.wsdd");
      assertNotNull(is);
      FileWriter pw;
      File deployFile = null;
      try {
         deployFile = File.createTempFile("CEC", ".wsdd");
         pw = new FileWriter(deployFile);
         BufferedReader fr = new BufferedReader(new InputStreamReader(is));
         String line;
         while ((line = fr.readLine()) != null) {
            pw.write(line);
         }
         pw.close();
      }
      catch (IOException e1) {

         e1.printStackTrace();
      }
      endpoint = "local:///CommonExecutionConnectorService";
      String[] args = { "-d", "-l", endpoint, deployFile.getAbsolutePath()};
      try {
         org.apache.axis.client.AdminClient.main(args);
      }
      catch (RuntimeException e) {
         // just try to carry on anyway.....
         e.printStackTrace();
      }
      System.out.println("created local service");
   }

   public static void main(String[] args) {
      //TODO set the endpoint from here?
      junit.textui.TestRunner.run(LocalApplicationControllerDelegateTest.class);
   }

}
