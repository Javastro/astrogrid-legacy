/*$Id: AbstractTestInstallation.java,v 1.15 2004/03/12 20:11:09 mch Exp $
 * Created on 19-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter;

import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.metadata.MetadataServer;
import org.astrogrid.datacenter.query.QueryState;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** Abstract base class that captures commonality between top level unit test and installation test.
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Sep-2003
 *
 */
public abstract class AbstractTestInstallation extends ServerTestCase {

    /**
     * Constructor for AbstractTestInstallation.
     * @param arg0
     */
    public AbstractTestInstallation(String arg0) {
        super(arg0);
    }

    /** set up connection properties. will use defaults defined as constants in this class,
          * unless system properties are defined (under the corresponding keys).
          */
    protected void setUp() throws Exception{
        super.setUp();
        Call.initialize(); // registers new connction handlers.
        try {
       serviceURL = new URL (System.getProperty(SERVICE_URL_KEY,SERVICE_URL_DEFAULT)); // trailing / is important here.
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail(SERVICE_URL_KEY + " is not a correctly formatted URL: " + e.getMessage());
        }
       queryFile = new File(System.getProperty(QUERY_FILE_KEY,QUERY_FILE_DEFAULT));
    }

    protected URL serviceURL;
    
    protected File queryFile;

    public final static String SERVICE_URL_KEY = "datacenter.test.installation.base.url";

    public final static String SERVICE_URL_DEFAULT = "http://localhost:8080/axis/services/AxisDataServer";


    public final static String QUERY_FILE_KEY = "datacenter.test.installation.query.file";

    public final static String QUERY_FILE_DEFAULT = "query.xml";

    public void testDisplaySettings() {
        System.out.println("Running with these settings: (adjust by runnng with -Dkey=value");
        System.out.println(SERVICE_URL_KEY + "=" + serviceURL.toString());
        System.out.println(QUERY_FILE_KEY + "=" + queryFile.getPath());

    }
    
    public void testGetMetatdata() throws Throwable{
        try {
           MetadataServer server = new MetadataServer();
           Document metadata = server.getMetadata();
           assertNotNull(metadata);
           assertIsMetadata(metadata);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}


/*
$Log: AbstractTestInstallation.java,v $
Revision 1.15  2004/03/12 20:11:09  mch
It05 Refactor (Client)

Revision 1.14  2004/03/09 02:02:06  mch
MetadataServer now returns Document - test server not delegate

Revision 1.13  2004/03/08 15:58:26  mch
Fixes to ensure old ADQL interface works alongside new one and with old plugins

Revision 1.12  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

Revision 1.11  2004/03/04 23:43:48  mch
Fixes for tests that broke with changes to config

Revision 1.10  2004/02/16 23:07:04  mch
Moved DummyQueriers to std server and switched to AttomConfig

Revision 1.9  2004/01/16 13:30:57  nw
got final test working

Revision 1.8  2004/01/15 16:35:24  nw
fixed failing test

Revision 1.7  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.6.10.1  2004/01/08 09:43:41  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.6  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.5  2003/11/27 17:28:09  nw
finished plugin-refactoring

Revision 1.4  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.

Revision 1.3  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.2  2003/11/17 15:42:03  mch
Package movements

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.8  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.7  2003/10/14 13:07:12  nw
moved to common subproject

Revision 1.6  2003/09/26 11:02:35  nw
added new method to test getting metadata

Revision 1.5  2003/09/25 03:18:35  nw
finished the integration / installation test.

Revision 1.4  2003/09/25 01:20:37  nw
got non-blocking test working, down to last status code returned. need to check this.

Revision 1.3  2003/09/24 21:14:01  nw
fixed to match behaviour of server.
 - non blocking test still fails with transport problems

Revision 1.2  2003/09/19 15:13:27  nw
got non-blocking query test working a bit more.
not finished.

Revision 1.1  2003/09/19 12:02:37  nw
Added top level test - runs integration tests against an inprocess db and inprocess axis.
 
*/
