/*$Id: AbstractTestInstallation.java,v 1.4 2003/11/27 00:52:58 nw Exp $
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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.axisdataserver.types._query;
import org.astrogrid.datacenter.delegate.AdqlQuerier;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.datacenter.delegate.Metadata;
import org.astrogrid.datacenter.query.QueryStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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
/* unimplemented
    public void testGetRegistryMetadata() {
        AdqlQuerier del = createDelegate();
        try {
            Element result = del.getVoRegistryMetadata();
            assertNotNull(result);
            assertEquals("Registry Metadata not in expected format","DatacenterMetadata",result.getLocalName());
            System.out.println("Registry Metadata:");
            System.out.println(XMLUtils.ElementToString(result));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not get registryMetaData: " + e.getMessage());
        }
    }
 */    
    
    public void testGetMetatdata() {
        AdqlQuerier del = createDelegate();
        try {
            Metadata result = del.getMetadata();
            assertNotNull(result);
            Document d = result.getDocument();             
             assertIsMetadata(d);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not get metadata:" + e.getMessage());
        }
    }

    /** do a standard (blocking) query for each query file found */
    public void testDoBlockingQuery() throws Exception {
        AdqlQuerier del = createDelegate();
        FileProcessor fp = new FileProcessor() {
            protected void processStream(AdqlQuerier del, InputStream is) throws Exception{
                doQuery(del,is);
            }
        };
        fp.findFiles(del);
    }

    /**do a nonblocking query for each file found */
    public void testDoNonblockingQuery() throws Exception{
        AdqlQuerier del = createDelegate();
        FileProcessor fp = new FileProcessor() {
            protected void processStream(AdqlQuerier del, InputStream is) throws Exception{
                doNonBlockingQuery(del,is);
            }
        };
        fp.findFiles(del);
    }

    /** helper method to create a datacenter delegate, based on properties initialized in {@link setUp} */
    protected AdqlQuerier createDelegate() {

        System.out.println("Connecting to datacenter service at " + serviceURL.toString());
        AdqlQuerier del = null;
        try {
            del = DatacenterDelegateFactory.makeAdqlQuerier( serviceURL.toString()) ;// pity it can't take a URL
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception while creating delegate: " + e.getMessage());
        } catch (ServiceException e) {
            e.printStackTrace();
            fail("Could not access service: " + e.getMessage());
        }
        assertNotNull("Could not create delegate",del);
        return del;
    }

    /** helper method to do a query */
    protected void doQuery(AdqlQuerier del, InputStream is) throws Exception {
       
       Select adql = Select.unmarshalSelect(new InputStreamReader(is));
       DatacenterResults result = del.doQuery(AdqlQuerier.VOTABLE, adql);
        assertNotNull("Result of query was null",result);
//        assertEquals("Result of query not in expected format","DatacenterResults",result.getLocalName());
//        System.out.println("Results for blocking query");
//        System.out.println(XMLUtils.ElementToString(result));
    
    }



    /** helper test method to do a non-blockinig query */
    protected void doNonBlockingQuery(AdqlQuerier del, InputStream is) throws Exception {

        Select adql = Select.unmarshalSelect(new InputStreamReader(is));
        DatacenterQuery query = del.makeQuery(adql);
        // check the response document.
        assertNotNull("query creation response document is null",query);
//        assertEquals("Query creation response document not in expected format","QueryCreated",queryIdDocument.getLocalName());
//        Element queryIdEl = (Element)queryIdDocument.getElementsByTagName("QueryId").item(0) ;
        assertNotNull("Query response document has not ID",query.getId());
//        String queryId = queryIdEl.getChildNodes().item(0).getNodeValue();
//        assertNotNull("query ID is null",queryId);
        System.out.println("Non blocking QueryID:" + query.getId());
               
        QueryStatus stat = query.getStatus();
        assertNotNull("status is null",stat);
        assertEquals("status code is not as expected",QueryStatus.CONSTRUCTED,stat);
                      
        URL notifyURL = new URL("http://www.nobody.there.com");
        query.registerWebListener(notifyURL);
                 
        query.start();
        try { // bit ad-hoc really- depend on latency?
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            fail("Been interrupted");
        }
        stat = query.getStatus();
        assertNotNull("status is null",stat);
        assertEquals("status code not as expected",QueryStatus.FINISHED,stat);
        
        DatacenterResults result = query.getResultsAndClose();
        assertNotNull("result of query is null",result);
        System.out.println("Results for Non-blocking query");
//        System.out.println(XMLUtils.ElementToString(result));
        // doesn't seem to return the document anymore - instead returns pointer to them.
//        assertEquals("Result of query not in expected format","DatacenterResults",result.getLocalName());
  
    }
    
    /** abstract class that captures the pattern of scanning for query files
     * implement processStream to do something once query files are found
     * @author Noel Winstanley nw@jb.man.ac.uk 17-Sep-2003
     *
     */
   protected abstract class FileProcessor {
       /** extender-defined method that consumes the files that are found */
    protected abstract void processStream(AdqlQuerier del,InputStream is) throws Exception;
    /** run a series of sample queries through the service
     *  <p>
     *  examines value of {@link #QUERY_FILE_KEY}, searches for input files in following order
     * <ol>
     * <li>a directory named ${QUERY_FILE_KEY} - uses each *.xml file within it as an input
     * <li>a file named ${QUERY_FILE_KEY} - uses this as the single input
     * <li>a resource on classpath named ${QUERY_FILE_KEY} - uses this as the single input.
     * @throws Exception */
    public void findFiles(AdqlQuerier del) throws Exception{
        if (queryFile.exists()) { // on local file system.
            if (queryFile.isFile()) {
                System.out.println ("Taking VOQL query from local file: " + queryFile.getPath());
                try {
                   this.processStream(del,new FileInputStream(queryFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    fail("Could not find file: " + e.getMessage());
                }
            } else if (queryFile.isDirectory()) {
                System.out.println("Taking VOQL queries from files in local directory: " + queryFile.getPath());
                File[] inputs = queryFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        return pathname.getName().toLowerCase().endsWith(".xml");
                    }
                });
                for (int i  = 0; i < inputs.length; i++) {
                    System.out.println("Processing file: " + inputs[i].getName());
                    try {
                        processStream(del,new FileInputStream(inputs[i]));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        fail("Could not find file: " + e.getMessage());
                    }
                }
            }
        } else { // try loading from classpath.
            System.out.println("Taking VOQL query from classpath resource" + queryFile.getPath());
            InputStream is = this.getClass().getResourceAsStream(queryFile.getPath());
            assertNotNull("VOQL query file :" + queryFile.getPath() + " not found on filesystem, or as resource",is);
            processStream(del,is);
        }
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
