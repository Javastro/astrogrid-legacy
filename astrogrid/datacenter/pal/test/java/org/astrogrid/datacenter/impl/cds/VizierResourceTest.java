/*$Id: VizierResourceTest.java,v 1.2 2004/11/09 17:42:22 mch Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.impl.cds;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.impl.cds.VizierResourcePlugin;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.util.DomHelper;
import org.xml.sax.SAXException;

/**
 * Test vizier proxy resource generator
 *
 */
public class VizierResourceTest extends TestCase {
   
   public void setUp() {
      SimpleConfig.getSingleton().setProperty(VoDescriptionServer.AUTHID_KEY, "org.astrogrid.test");
      SimpleConfig.getSingleton().setProperty(VoDescriptionServer.RESKEY_KEY, "VizierTest");
   }
   
   public void testQueryable() throws IOException, ParserConfigurationException, SAXException {
      String s = VoDescriptionServer.VODESCRIPTION_ELEMENT+new VizierResourcePlugin().getQueryable()+VoDescriptionServer.VODESCRIPTION_ELEMENT_END;
      DomHelper.newDocument(s);
   }
   
   public void testRdbms() throws IOException, ParserConfigurationException, SAXException {
      String s = VoDescriptionServer.VODESCRIPTION_ELEMENT+new VizierResourcePlugin().getRdbmsResource()+VoDescriptionServer.VODESCRIPTION_ELEMENT_END;
      DomHelper.newDocument(s);
   }
   
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(new TestSuite(VizierResourceTest.class));
   }
}


/*
 $Log: VizierResourceTest.java,v $
 Revision 1.2  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.1  2004/11/08 14:26:41  mch
 Added resource tests

 Revision 1.1  2004/11/03 05:20:50  mch
 Moved datacenter deployment tests out of standard tests

 */
