/*$Id: EgsoDelegateTest.java,v 1.1 2004/11/03 05:20:50 mch Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.deployed.egso;

import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import junit.framework.TestCase;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.impl.sec.EgsoQuerierPlugin;
import org.astrogrid.datacenter.impl.sec.SEC_Port;
import org.astrogrid.datacenter.impl.sec.SEC_Service;
import org.astrogrid.datacenter.impl.sec.SEC_ServiceLocator;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Exercises the method of the SEC web service
 */
public class EgsoDelegateTest extends TestCase {
   
   public EgsoDelegateTest() {
   }
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(EgsoDelegateTest.class);
   }
   
   public void testDoQuery() throws ParserConfigurationException, SAXException, IOException, ServiceException {
      String testSQL = "SELECT * FROM sgas_event WHERE nar>9500 AND nar<9600";
      SEC_Service service = new SEC_ServiceLocator();
      SEC_Port secPort = service.getSECPort(new URL(EgsoQuerierPlugin.SEC_URL));
      Document doc = DomHelper.newDocument(secPort.sql(testSQL));
      assertNotNull(doc);
      XMLUtils.PrettyDocumentToStream(doc,System.out);
      // check its a votable
      assertEquals("VOTABLE",doc.getDocumentElement().getLocalName());
   }
}


/*
 $Log: EgsoDelegateTest.java,v $
 Revision 1.1  2004/11/03 05:20:50  mch
 Moved datacenter deployment tests out of standard tests

 Revision 1.1.2.1  2004/11/02 21:51:03  mch
 Moved deployment tests here - not exactly right either

 Revision 1.1  2004/10/05 16:45:28  mch
 Moved proxy tests to integration tests from unit tests

 Revision 1.1  2004/07/07 09:17:40  KevinBenson
 New SEC/EGSO proxy to query there web service on the Solar Event Catalog

 Revision 1.2  2003/11/20 15:47:18  nw
 improved testing

 Revision 1.1  2003/11/18 11:23:49  nw
 mavenized cds delegate

 Revision 1.2  2003/11/18 11:09:24  nw
 mavenized cds delegate

 Revision 1.1  2003/10/16 10:11:45  nw
 first check in
 
 */
