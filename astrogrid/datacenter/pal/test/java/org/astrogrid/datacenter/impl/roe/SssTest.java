/*$Id: SssTest.java,v 1.1 2004/11/11 20:42:50 mch Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.impl.roe;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.slinger.targets.TargetMaker;
import org.xml.sax.SAXException;

/**
 * Test vizier proxy resource generator
 *
 */
public class SssTest extends TestCase {
   
   Account testAccount = new Account("SssTest", "localhost", null);
   
   public void setUp() {
      SimpleConfig.getSingleton().setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY, SssImagePlugin.class.getName());
   }
   
   public void testSouth() throws IOException, ParserConfigurationException, SAXException, Throwable {
      StringWriter sw = new StringWriter(); //although we throw away the results
      DataServer server = new DataServer();
      server.askQuery(testAccount,
                      SimpleQueryMaker.makeConeQuery(30,-80,0.1, TargetMaker.makeIndicator(sw), ReturnTable.VOTABLE), this);
      String results = sw.toString();
      System.out.println(sw.toString());
   }
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(new TestSuite(SssTest.class));
   }
}


/*
 $Log: SssTest.java,v $
 Revision 1.1  2004/11/11 20:42:50  mch
 Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

 Revision 1.2  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.1  2004/11/08 14:26:41  mch
 Added resource tests

 Revision 1.1  2004/11/03 05:20:50  mch
 Moved datacenter deployment tests out of standard tests

 */
