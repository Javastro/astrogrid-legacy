package org.astrogrid.dataservice.service.tap;

import java.text.SimpleDateFormat;

/**
 * A date-time format matching the ISO8601 standard.
 * The output includes, the date, time to a precision of milliseconds, and
 * a time zone.
 *
 * @author Guy Rixon
 */
public class Iso8601DateFormat extends SimpleDateFormat {

  public Iso8601DateFormat() {
    super("yyyy-MM-dd'T'HH:mm:ssZ");
  }

}
