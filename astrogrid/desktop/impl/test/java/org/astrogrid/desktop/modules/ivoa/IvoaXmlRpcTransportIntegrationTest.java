/*$Id: IvoaXmlRpcTransportIntegrationTest.java,v 1.5 2009/03/26 18:01:21 nw Exp $
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
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.astrogrid.Fixture;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.system.XmlRpcTransportIntegrationTest;
import org.astrogrid.io.Piper;

/** tests xmlrpc transport for new objects introduced by ivoa module.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class IvoaXmlRpcTransportIntegrationTest extends InARTestCase {

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
            super.setUp();
            client = Fixture.createXmlRpcClient(getACR());
        }
        protected XmlRpcClient client;
        @Override
        protected void tearDown() throws Exception {
        	super.tearDown();
        	client = null;
        }


    // verify that resource objects can be successfully serialized over xmlrpc.
    // as this is an integration test, bot a system test, do this by getting ar to parse
    // in a passed-in xml document, and return the resource objects it contains
    public void testResourceProxyObject() throws Exception {
    	InputStream is = XmlRpcTransportIntegrationTest.class.getResourceAsStream("multiple.xml");
    	assertNotNull(is);
    	StringWriter sw = new StringWriter();
    	Piper.pipe(new InputStreamReader(is),sw);
    	Object[] results = (Object[])client.execute("ivoa.externalRegistry.buildResources",new Object[]{sw.toString()});
    	assertNotNull(results);
    	assertEquals(3,results.length);
    	for (int i = 0; i < results.length; i++) {
    		assertTrue(results[i] instanceof Map);
    		//@future - more testing of structure here??
    	}
    }
   
    public static Test suite() {
        return new ARTestSetup(new TestSuite(IvoaXmlRpcTransportIntegrationTest.class));
    }
}


/* 
$Log: IvoaXmlRpcTransportIntegrationTest.java,v $
Revision 1.5  2009/03/26 18:01:21  nw
added override annotations

Revision 1.4  2008/08/04 16:37:21  nw
Complete - task 441: Get plastic upgraded to latest XMLRPC

Complete - task 430: upgrade to latest xmlrpc lib

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

Revision 1.2  2006/08/15 10:30:58  nw
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