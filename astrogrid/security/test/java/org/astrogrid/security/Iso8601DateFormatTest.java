package org.astrogrid.security;

import java.util.Date;
import junit.framework.TestCase;

/**
 * JUnit tests for {@link Iso8601DateFormat}.
 *
 * @author Guy Rixon
 */
 public class Iso8601DateFormatTest extends TestCase {

   /**
    * Tests that a date in the desired format is unchanged
    * when parsed and reformatted. The desired format is for
    * W3C XML schema.
    */
   public void testRoundTrip () throws Exception {
     Iso8601DateFormat i = new Iso8601DateFormat();
     String s1 = "2000-01-01T12:00:00.001";
     Date d = i.parse(s1);
     String s2 = i.format(d);
     System.out.println("Before: " +
                        s1 +
                        "\nAfter:  " +
                        s2);
     this.assertTrue(s1.equals(s2));
   }

 }