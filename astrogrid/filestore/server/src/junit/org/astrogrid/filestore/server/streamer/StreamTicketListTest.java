package org.astrogrid.filestore.server.streamer;

import java.util.Hashtable;
import junit.framework.*;

/**
 *
 * @author Guy Rixon
 */
public class StreamTicketListTest extends TestCase {

  public StreamTicketListTest(java.lang.String testName) {
    super(testName);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(StreamTicketListTest.class);
    return suite;
  }

  /**
   * Test of add method.
   */
  public void testAdd() throws Exception {
    System.out.println("testAdd");

    StreamTicket t1 = new StreamTicket();
    t1.setName("Bill");
    StreamTicketList l = new StreamTicketList();
    l.add(t1);

    // Check that the list changes duplicate names of tickets.
    StreamTicket t2 = new StreamTicket();
    t2.setName("Bill");
    l.add(t2);
    assertFalse("Ticket list has change the duplicate name of a ticket.",
                t2.getName().equals("Bill"));
  }

  /**
   * Test of use method.
   */
  public void testUse() throws Exception {
    System.out.println("testUse");

    StreamTicket t1 = new StreamTicket();
    t1.setName("Fred");
    StreamTicketList l  = new StreamTicketList();
    l.add(t1);
    StreamTicket t2 = l.use("Fred");
    assertEquals(t1, t2);

    try {
      l.use("bogus");
      fail("use() failed to throw an exception " +
           "for a bogus ticket-name.");
    }
    catch (StreamTicketException ste) {
      // This is as expected.
    }
    catch (Exception e) {
      fail("use() threw the wrong kind of exception for a bogus ticket-name.");
    }
  }

  /**
   * Tests the containsName method.
   */
  public void testContainsName() throws Exception {
    System.out.println("testContainsName");

    StreamTicket     t = new StreamTicket();
    StreamTicketList l = new StreamTicketList();
    assertFalse("Ticket's name is not in the list before the ticket is added.",
                l.containsName(t.getName()));
    l.add(t);
    assertTrue("Ticket's name is in the list after the ticket is added.",
               l.containsName(t.getName()));
    l.use(t.getName());
    assertFalse("Ticket's name is not in the list after the ticket is used.",
                l.containsName(t.getName()));
  }

}
