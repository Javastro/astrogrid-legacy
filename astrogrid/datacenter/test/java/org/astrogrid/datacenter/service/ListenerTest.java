/*
 * $Id TestWorkspace.java $
 *
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.rpc.ServiceException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.delegate.WebNotifyServiceListener;
import org.astrogrid.datacenter.delegate.dummy.DummyDelegate;
import org.astrogrid.datacenter.queriers.DummyQuerier;

/**
 * Unit tests for the remote listening classes
 *
 * @author M Hill
 */

public class ListenerTest extends TestCase
{

   public void testWebListener() throws MalformedURLException, IOException, ServiceException
   {
      WebNotifyServiceListener listener = new WebNotifyServiceListener(new URL("http://wibble"));

      //make sure it can be registered properly
      DummyDelegate delegate = (DummyDelegate) DatacenterDelegate.makeDelegate(null);
      delegate.registerListener(DummyDelegate.SERVICE_ID, listener);

      DummyQuerier querier = new DummyQuerier();
   }

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(WorkspaceTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }


}

/*
$Log: ListenerTest.java,v $
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

