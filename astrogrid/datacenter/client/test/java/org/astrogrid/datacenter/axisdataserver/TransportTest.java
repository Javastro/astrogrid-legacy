/*$Id: TransportTest.java,v 1.2 2003/11/18 14:27:39 nw Exp $
 * Created on 16-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.axisdataserver;

import java.net.URL;

import org.apache.axis.client.AdminClient;
import org.apache.axis.client.Call;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/** Unit test to verify that transport mechanism (i.e. serializing using Castor, within axis) functions correctly
 *  
 * The wsdl2java tool generates a  client test case. We subclass this, and initialize a mock server for the test client to connect to.
 * This test just exercises the  transfer of parameters to and from the server. 
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Nov-2003
 *
 */
public class TransportTest extends AxisDataServerServiceTestCase {

    /** 
     * Constructor for TransportTest.
     * @param arg0
     */
    public TransportTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TransportTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        Call.initialize(); // bit of magic - registers the 'local:' URL protocol
       this.serviceURL = new URL("local:///AxisDataServer");
      // this.serviceURL = new URL("http://localhost:8060/axis/services/AxisDataServer");
      Marshaller.enableDebug = true;
     // set up the axis server
        String[] args = {"-l",
                         "local:///AdminService",
                         "generated/wsdd/test-deploy.wsdd"}; // fix this.
        AdminClient.main(args);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}


/* 
$Log: TransportTest.java,v $
Revision 1.2  2003/11/18 14:27:39  nw
code to test the serialization and deserialization mechanism

Revision 1.1  2003/11/17 12:12:28  nw
first stab at mavenizing the subprojects.
 
*/