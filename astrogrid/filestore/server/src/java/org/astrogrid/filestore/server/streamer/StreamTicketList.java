package org.astrogrid.filestore.server.streamer;

import java.util.Hashtable;

/**
 * A collection of authorized downloads from the streaming servlet.
 * Tickets representing allowed downloads are added to the collection
 * by a MySpace control-service and are consumed by a data-streaming
 * service.
 *
 * The collection is a map containing StreamTicket objects. The keys
 * to the map are strings. The keys are supposed to be unique. The map
 * is a java.util.Hashtable and thus is internally synchronized for
 * use by multiple threads. Since the map holds the only state in this
 * this class, the class is MT-safe.
 *
 * There is only one map: it is a static variable shared between all
 * instances of this class. Thus, all StreamTicketLists in the same
 * JVM share the same map and StreamTicketLists in separate JVMs are
 * not interconnnected. This behaviour suits the intended use of this
 * class: the controlling service and data-streaming service are expected
 * to be in the same web-application and thus run from the same JVM.
 *
 * It is essential that no two tickets in the list have the same name;
 * the map does not allow duplicate keys. Therefore, the add method
 * is allowed to change the name of a ticket if it detects a collision.
 * Clients should call getName on the ticket after calling add
 * on the list to make sure that they have the correct name.
 *
 * @author Guy Rixon
 */
public class StreamTicketList {

  private static Hashtable map = new Hashtable();

  /** Creates a new instance of TicketList */
  public StreamTicketList() {
  }

  /**
   * Adds a ticket to the list. The name of the
   * ticket may be changed to avoid duplication.
   * @param ticket the ticket to be added.
   */
  public void add(StreamTicket ticket) {
    while (this.containsName(ticket.getName())) {
      ticket.changeName();
    }
    this.map.put(ticket.getName(), ticket);
  }

  /**
   * Looks up a ticket name, removes the ticket from the list and returns
   * the ticket.
   * @param ticketName the name of the ticket.
   * @return the ticket.
   * @throws StreamTicketException if the ticket name is null or
   *  not found in the list.
   */
  public StreamTicket use(String ticketName) throws StreamTicketException {
    System.out.println("StreamTicket.use(): ticket name is " + ticketName);
    StreamTicket ticket;
    try {
      ticket = (StreamTicket)this.map.get(ticketName);
      if (ticket == null) {
        throw new StreamTicketException("The given ticket is not valid");
      }
      this.map.remove(ticketName);
    }
    catch (Exception e) {
      throw new StreamTicketException("The given ticket is not valid");
    }
    return ticket;
  }

  /**
   * Tests whether a ticket of the given name is in the list.
   *
   * @param ticketName The name to be tested.
   * @return True is the name is in the list; false otherwise.
   */
  public boolean containsName(String ticketName) {
    return this.map.containsKey(ticketName);
  }

}
