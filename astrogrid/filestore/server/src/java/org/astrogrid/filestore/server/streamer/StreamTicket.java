package org.astrogrid.filestore.server.streamer;

import java.util.Date;
import java.util.Random;

/**
 * A "ticket" representing an authorized download from the streaming servlet.
 * The ticket is a Java bean with two properties. "Target" is the URI for the
 * data to be streamed. "Date" is the time of issue. Tickets are stored in the
 * {@link StreamTicketList}.
 *
 * The name of the ticket is a random string that can serve as a single-use
 * password. Since the names will be embedded in URLs, it is important that
 * they contain only printing characters. In fact, it is convenient to
 * use a positive integer, covering the full integer range, converted to a
 * string. Ticket names should be unique, but ths class has no way of ensuring
 * that. Therefore, the caller must check the names against a list of all known
 * tickets. The changeName() method can be used to generate a new, random name
 * for an existing ticket.
 *
 * @author  Guy Rixon
 */
public class StreamTicket {

  /**
   * The randon-number generator for the ticket names.
   */
  static Random random = new Random(System.currentTimeMillis());

  /**
   * Holds value of property date.
   */
  private java.util.Date date;

  /**
   * Holds value of property target.
   */
  private String target;

  /**
   * Holds value of property name.
   */
  private String name;

  /**
   * Holds value of property mimeType.
   */
  private String mimeType;

  /** Creates a new instance of StreamTicketList */
  public StreamTicket() {
    this.name     = generateRandomName();
    this.date     = new Date();
    this.mimeType = "application/octet-stream";
  }

  /**
   * Getter for property date.
   * @return Value of property date.
   */
  public java.util.Date getDate() {
    return this.date;
  }

  /**
   * Getter for property target.
   * @return Value of property target.
   */
  public String getTarget() {
    return this.target;
  }

  /**
   * Setter for property target.
   * @param target New value of property target.
   */
  public void setTarget(String target) {
    this.target = target;
  }

  /**
   * Getter for property name.
   * @return Value of property name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Setter for property name.
   * @param name New value of property name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Changes the name property to a new, random value.
   * The new name is never the same as the old name.
   */
  public void changeName() {
    String newName;
    do {
      newName = this.generateRandomName();
    } while (newName.equals(this.name));
    this.name = newName;
  }

  /**
   * Getter for property mimeType.
   * @return Value of property mimeType.
   */
  public String getMimeType () {
    return this.mimeType;
  }

  /**
   * Setter for property mimeType.
   * @param mimeType New value of property mimeType.
   */
  public void setMimeType (String mimeType) {
    this.mimeType = mimeType;
  }


  /**
   * Sets the ticket name to a random string containing only digits.
   */
  private String generateRandomName () {
    return new Integer(this.random.nextInt(Integer.MAX_VALUE)).toString();
  }

}
