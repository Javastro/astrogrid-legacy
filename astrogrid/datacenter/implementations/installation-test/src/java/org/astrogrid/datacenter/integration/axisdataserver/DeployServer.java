/*$Id: DeployServer.java,v 1.1 2003/11/18 12:14:14 nw Exp $
 * Created on 16-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.integration.axisdataserver;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.axis.client.AdminClient;
import org.apache.axis.utils.Options;
/** little utility that just fires the correct wsdd at the web server.
 *  -- wraps around classes supplied by axis - hence accepts all parameters (port, host, password, etc) that
 * original {@link org.apache.axis.client.AdminClient} does.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Sep-2003
 *
 */
public class DeployServer {
    public static final String SERVER_DEPLOY_WSDD="/wsdd/AxisDataServer-deploy.wsdd";
  public static final void main(String[] args) {
      try {
      Options opts = new Options(args); 
      AdminClient client = new AdminClient();
      InputStream is = DeployServer.class.getResourceAsStream(SERVER_DEPLOY_WSDD);
      if (is == null) {
          System.out.println("Could not load resource " + SERVER_DEPLOY_WSDD);
          System.exit(-1);
      }
      String result = client.process(opts,is);
      System.out.println("Result: " + result);
      } catch (MalformedURLException e) {
          System.out.println("URL is malformed: " + e.getMessage());
      } catch (Exception e) {
          e.printStackTrace();
          System.out.println("Could not process deployment descriptor: " + e.getMessage());
      }
  }
}


/* 
$Log: DeployServer.java,v $
Revision 1.1  2003/11/18 12:14:14  nw
mavenized installation-test

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.2  2003/09/17 14:53:02  nw
tidied imports

Revision 1.1  2003/09/16 12:49:51  nw
integration / installation testing
 
*/