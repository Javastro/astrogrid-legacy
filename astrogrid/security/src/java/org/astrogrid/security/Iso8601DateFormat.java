package org.astrogrid.security;

import java.text.SimpleDateFormat;


/**
 * A formatter and parser for ISO8601 date-time notatation.
 *
 * This class extends <code>java.text.SimpleDateFormat</code>
 * to specify a particular pattern for the format. The format
 * is specified at construction, so no other methods of the
 * superclass need be overridden.
 *
 * @author Guy Rixon
 */
public class Iso8601DateFormat extends SimpleDateFormat {

  /**
   * Constructs an Iso8601DateFormat.
   */
  public Iso8601DateFormat () {
    super("yyyy-MM-dd'T'HH:mm:ss.SSS");
    // @TODO: sort out time zones
  }

}