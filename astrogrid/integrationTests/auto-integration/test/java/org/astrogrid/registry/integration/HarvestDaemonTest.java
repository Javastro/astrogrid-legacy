/*$Id: HarvestDaemonTest.java,v 1.2 2004/09/03 07:47:41 KevinBenson Exp $
 * Created on 19-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.registry.integration;

import junit.framework.TestCase;
import java.net.*;
import org.astrogrid.config.Config;

/**
 * @author Brian McIlwrath bkm@star.rl.ac.uk 19-Aug-2004
 *
 */
public class HarvestDaemonTest extends TestCase {

	Config conf;

    /**
     * Constructor for HarvestDaemonTest.
     * @param arg0
     */
    public HarvestDaemonTest(String arg0) {
        super(arg0);
    }

    public void testDaemon() throws Exception {
       String temp = conf.getString("org.astrogrid.registry.query.endpoint");
       String harvestEndpoint = null;
       if(temp != null)
           harvestEndpoint = temp.substring(0,temp.indexOf("services")) + "RegistryHarvest";

       assertNotNull("Cannot find the daemon endpoint property", harvestEndpoint);
       HttpURLConnection con =
              (HttpURLConnection) new URL(harvestEndpoint).openConnection();
       con.setRequestMethod("HEAD");
//     BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//     String str;
//     while ((str =br.readLine()) != null)
//        System.out.println(str);
       assertTrue("Cannot make URL connection to Daemon", con.getResponseCode() == HttpURLConnection.HTTP_OK);
    }

    public void setUp() throws Exception {
       super.setUp();

	   if(conf == null)
	      conf = org.astrogrid.config.SimpleConfig.getSingleton();
    }
}
