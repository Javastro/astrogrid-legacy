/*
 * $Id TestWorkspace.java $
 *
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DelegateQueryListener;
import org.astrogrid.datacenter.delegate.dummy.DummyDelegate;
import org.astrogrid.datacenter.queriers.DummyQuerier;
import org.astrogrid.datacenter.query.QueryStatus;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Unit tests for the remote listening classes
 *
 * @author M Hill
 * @todo add some assertions to this class - currenly tests nothing.
 */

public class ListenerTest extends ServerTestCase implements DelegateQueryListener
{
  public ListenerTest(String name) {
      super(name);
  }
   /** Called by the delegate query when it has been notified of a
    * status change.
    */
   public void delegateQueryChanged(DatacenterQuery query, QueryStatus newStatus)
   {
      // TODO
   }
   
   public void testDelegateListener() throws MalformedURLException, IOException, ServiceException, ADQLException, SAXException, ParserConfigurationException
   {
      //make sure it can be registered properly
      DummyDelegate delegate = (DummyDelegate) DatacenterDelegateFactory.makeAdqlQuerier(null);
      
      URL url = getClass().getResource("testQuery.xml");
      Element adqlQuery = XMLUtils.newDocument(url.openConnection().getInputStream()).getDocumentElement();

      Select adql = ADQLUtils.unmarshalSelect(adqlQuery);
      
      DatacenterQuery query = delegate.makeQuery(adql);
      assertNotNull(query);
      query.registerListener(this);

      DummyQuerier querier = new DummyQuerier();
   }

   public void testWebListener() throws MalformedURLException, IOException, ServiceException, ADQLException, SAXException, ParserConfigurationException
   {
      //make sure it can be registered properly
      DummyDelegate delegate = (DummyDelegate) DatacenterDelegateFactory.makeAdqlQuerier(null);
      
      URL url = getClass().getResource("testQuery.xml");
      Element adqlQuery = XMLUtils.newDocument(url.openConnection().getInputStream()).getDocumentElement();

      Select adql = ADQLUtils.unmarshalSelect(adqlQuery);
      
      DatacenterQuery query = delegate.makeQuery(adql);
      query.registerWebListener(new URL("http://wibble"));

      DummyQuerier querier = new DummyQuerier();
   }

   
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(ListenerTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }


    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}

/*
$Log: ListenerTest.java,v $
Revision 1.5  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.4  2003/11/18 14:37:35  nw
removed references to WorkspaceTest - has now been moved to astrogrid-common

Revision 1.3  2003/11/18 11:08:55  mch
Removed client dependencies on server

Revision 1.2  2003/11/17 15:42:03  mch
Package movements

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.6  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.5  2003/09/24 21:11:37  nw
altered constructor to fit in with others.

Revision 1.4  2003/09/22 16:52:12  mch
Fixes for changes to posts results to dummy myspace

Revision 1.3  2003/09/17 14:53:02  nw
tidied imports

Revision 1.2  2003/09/15 22:09:00  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.1  2003/09/15 21:28:09  mch
Listener/state refactoring.

Revision 1.9  2003/09/15 18:01:45  mch
Better test coverage

Revision 1.8  2003/09/10 14:48:35  nw
fixed breaking tests

Revision 1.7  2003/09/08 19:39:55  mch
More bugfixes and temporary file locations

Revision 1.6  2003/09/08 18:35:54  mch
Fixes for bugs raised by WorkspaceTest

Revision 1.5  2003/09/05 13:24:53  nw
added forgotten constructor (is this still needed for unit tests?)

Revision 1.4  2003/09/05 01:03:01  nw
extended to test workspace thoroughly

Revision 1.3  2003/09/04 10:49:16  nw
fixed typo

Revision 1.2  2003/09/04 09:24:32  nw
added martin's changes

Revision 1.1  2003/08/29 15:27:20  mch
Renamed TestXxxx to XxxxxTest so Maven runs them

Revision 1.1  2003/08/27 18:12:35  mch
Workspace tester

*/

