package org.astrogrid.filestore.server.streamer;

import java.util.Date;
import junit.framework.*;

/**
 * JUnit tests for org.astrogrid.mySpace.mySpaceServer.StreamTicket.
 *
 * @author Guy Rixon
 */
public class StreamTicketTest extends TestCase {

  public StreamTicketTest(java.lang.String testName) {
    super(testName);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(StreamTicketTest.class);
    return suite;
  }

  /**
   * Test of getDate method.
   */
  public void testGetDate() throws Exception {
    System.out.println("testGetDate");

    StreamTicket t = new StreamTicket();
    Date then = t.getDate();
    Date now  = new Date();
    System.out.println(then);
    System.out.println(now);
    assertFalse(now.before(then));
  }

  /**
   * Test of getTarget method.
   */
  public void testGetTarget() throws Exception {
    System.out.println("testGetTarget");

    StreamTicket t = new StreamTicket();
    t.setTarget("whatever");
    assertEquals("whatever", t.getTarget());
  }

  /**
   * Test of getName method.
   */
  public void testGetName() throws Exception {
    System.out.println("testGetName");

    StreamTicket t = new StreamTicket();
    assertNotNull("Ticket t has a name.", t.getName());

    // This has a tiny possibility of failing because the random-number
    // generator gives the same name for two successive tickets.  In fact,
    // we're testing a stronger condition than the contract requires: the
    // class is allowed to generate two equal names occasionally. This
    // test really catches the case where the names are consistently the
    // same.
    for (int i = 1; i <= 1000; i++) {
      StreamTicket t2 = new StreamTicket();
      assertNotNull("Ticket t2 has a name.", t2.getName());
      assertFalse("No two tickets have the same name.",
                  t.getName().equals(t2.getName()));
    }

    t.setName("whatever");
    assertEquals("getName() returns the value set by setName().",
                 "whatever", t.getName());
  }

  /**
   * Tests the changeName method.
   */
  public void testChangeName() throws Exception {
    System.out.println("testChangeName");

    StreamTicket t = new StreamTicket();
    for (int i = 1; i <=100; i++) {
      String oldName = t.getName();
      t.changeName();
      String newName = t.getName();
      assertFalse("Name is different before and after changeName().",
                  oldName.equals(newName));
    }
  }

  /**
   * Test of getMimeType method; also tests setMimeType.
   */
  public void testGetMimeType() throws Exception {
    System.out.println("testGetMimeType");

    StreamTicket t = new StreamTicket();
    assertEquals("application/octet-stream", t.getMimeType());
    t.setMimeType("text/xml");
    assertEquals("text/xml", t.getMimeType());
  }

}
