/*$Id: AbstractTestInstallation.java,v 1.9 2004/01/16 13:30:57 nw Exp $
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
import java.util.Enumeration;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.datacenter.delegate.FullSearcher;
import org.astrogrid.datacenter.delegate.Metadata;
import org.astrogrid.datacenter.query.QueryStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

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
        FullSearcher del = createDelegate();
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
    
    public void testGetMetatdata() throws Throwable{
        try {
        FullSearcher del = createDelegate();
            Metadata result = del.getMetadata();
            assertNotNull(result);
            Document d = result.getDocument();             
             assertIsMetadata(d);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    /** do a standard (blocking) query for each query file found */
    public void testDoBlockingQuery() throws Throwable {
        try {
        FullSearcher del = createDelegate();
        FileProcessor fp = new FileProcessor() {
            protected void processStream(FullSearcher del, InputStream is) throws Exception{
                doQuery(del,is);
            }
        };
        fp.findFiles(del);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    /**do a nonblocking query for each file found */
    public void testDoNonblockingQuery() throws Throwable{
        try {
        FullSearcher del = createDelegate();
        FileProcessor fp = new FileProcessor() {
            protected void processStream(FullSearcher del, InputStream is) throws Exception{
                doNonBlockingQuery(del,is);
            }
        };
        fp.findFiles(del);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    /** helper method to create a datacenter delegate, based on properties initialized in {@link setUp} */
    protected FullSearcher createDelegate() {

        System.out.println("Connecting to datacenter service at " + serviceURL.toString());
        FullSearcher del = null;
        try {
            del = DatacenterDelegateFactory.makeFullSearcher( serviceURL.toString()) ;// pity it can't take a URL
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
    protected void doQuery(FullSearcher del, InputStream is) throws Exception {
       assertNotNull(is);
       Select adql = Select.unmarshalSelect(new InputStreamReader(is));
       assertNotNull("query input is null",adql);
       DatacenterResults result = del.doQuery(FullSearcher.VOTABLE, ADQLUtils.toQueryBody(adql));
        assertNotNull("Result of query was null",result);
       Element vo = result.getVotable();
       assertNotNull(vo);
       XMLUtils.PrettyDocumentToStream(vo.getOwnerDocument(),System.out);
       assertIsVotableResultsResponse(vo.getOwnerDocument());
       
    }



    /** helper test method to do a non-blockinig query */
    protected void doNonBlockingQuery(FullSearcher del, InputStream is) throws Exception {
        assertNotNull(is);
        Select adql = Select.unmarshalSelect(new InputStreamReader(is));
        assertNotNull(adql);
        DatacenterQuery query = del.makeQuery(ADQLUtils.toQueryBody(adql));
        // check the response document.
        assertNotNull("query creation response document is null",query);
        assertNotNull("Query response document has not ID",query.getId());

        System.out.println("Non blocking QueryID:" + query.getId());
               
        QueryStatus stat = query.getStatus();
        assertNotNull("status is null",stat);
        assertEquals("status code is not as expected",QueryStatus.UNKNOWN,stat);
       // need to add this back in later - maybe a web listener that can't find its endpoint should just failsafe..               
       // URL notifyURL = new URL("http://www.nobody.there.com");
       // query.registerWebListener(notifyURL);
                 
        query.start();
        try { // bit ad-hoc really- results may depend on load / processor speed
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            fail("Been interrupted");
        }
        stat = query.getStatus();
        assertNotNull("status is null",stat);
        assertEquals("status code not as expected",QueryStatus.FINISHED,stat);
        
        DatacenterResults result = query.getResultsAndClose();
        assertNotNull("result of query is null",result);
        
        // specific to what query is being performed - works for {@link DatacenterTest} - should be moved into there 
        assertFalse(result.isFits());
        assertFalse(result.isVotable());
        assertTrue(result.isUrls());
        
        String[] resultUrls = result.getUrls();
        assertNotNull(resultUrls);
        assertTrue(resultUrls.length > 0);
        for (int i = 0; i < resultUrls.length; i++) {
        System.out.println(resultUrls[i]);
        }
        URL url = new URL(resultUrls[0]);
        Document resultDoc = XMLUtils.newDocument(url.openStream());
        assertNotNull(resultDoc);
        XMLUtils.PrettyDocumentToStream(resultDoc,System.out);
        assertIsVotable(resultDoc);
  
    }
    
    /** abstract class that captures the pattern of scanning for query files
     * implement processStream to do something once query files are found
     * @author Noel Winstanley nw@jb.man.ac.uk 17-Sep-2003
     *
     */
   protected abstract class FileProcessor {
       /** extender-defined method that consumes the files that are found */
    protected abstract void processStream(FullSearcher del,InputStream is) throws Exception;
    /** run a series of sample queries through the service
     *  <p>
     *  examines value of {@link #QUERY_FILE_KEY}, searches for input files in following order
     * <ol>
     * <li>a directory named ${QUERY_FILE_KEY} - uses each *.xml file within it as an input
     * <li>a file named ${QUERY_FILE_KEY} - uses this as the single input
     * <li>a resource on classpath named ${QUERY_FILE_KEY} - uses this as the single input.
     * @throws Exception */
    public void findFiles(FullSearcher del) throws Exception{
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
