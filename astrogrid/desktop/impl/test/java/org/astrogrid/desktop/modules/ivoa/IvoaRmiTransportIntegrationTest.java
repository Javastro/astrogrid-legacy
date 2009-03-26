/*$Id: IvoaRmiTransportIntegrationTest.java,v 1.5 2009/03/26 18:01:20 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ivoa;

import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.ExternalRegistry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.TestingFinder;
import org.astrogrid.desktop.modules.system.RmiTransportIntegrationTest;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** additional tests of rmi transport for new objects itnroduced by this module.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class IvoaRmiTransportIntegrationTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
            super.setUp();
            reg = (new TestingFinder()).find();
            WebServer serv = (WebServer)reg.getService(WebServer.class);
            assertNotNull(serv);
            
        }
        protected ACR reg;
        @Override
        protected void tearDown() throws Exception {
        	super.tearDown();
        	reg = null;
        }


    // verify that resource objects can be successfully serialized over rmi 
    // little odd- as they're dynamically generated proxies.
    // as this is an integration test, bot a system test, do this by getting ar to parse
    // in a passed-in xml document, and return the resource objects it contains
    public void testResourceProxyObject() throws Exception {
    	InputStream is = RmiTransportIntegrationTest.class.getResourceAsStream("multiple.xml");
    	assertNotNull(is);
    	Document d = DomHelper.newDocument(is);
     	ExternalRegistry er = (ExternalRegistry)this.reg.getService(ExternalRegistry.class);
    	Resource[] res = er.buildResources(d);
    	assertNotNull(res);
    	assertEquals(3,res.length);
    	assertTrue(res[0] instanceof Service);
    	assertTrue(res[1] instanceof CeaApplication);
    }
    


    public static Test suite() {
        return new ARTestSetup(new TestSuite(IvoaRmiTransportIntegrationTest.class));
    }
}


/* 
$Log: IvoaRmiTransportIntegrationTest.java,v $
Revision 1.5  2009/03/26 18:01:20  nw
added override annotations

Revision 1.4  2008/04/23 11:32:43  nw
marked as needing tests.

Revision 1.3  2007/03/08 17:43:50  nw
first draft of voexplorer

Revision 1.2  2007/01/29 10:42:28  nw
tidied.

Revision 1.1  2007/01/23 20:07:32  nw
fixes to use subclass of finder, and to work in a hub setting.

Revision 1.5  2007/01/23 11:53:37  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.4  2007/01/09 16:12:20  nw
improved tests - still need extending though.

Revision 1.3  2006/08/31 21:07:58  nw
testing of transport of rtesource beans.

Revision 1.2  2006/08/15 10:31:09  nw
tests related to new registry objects.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/