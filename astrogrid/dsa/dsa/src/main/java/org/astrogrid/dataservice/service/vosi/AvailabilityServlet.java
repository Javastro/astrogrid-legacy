package org.astrogrid.dataservice.service.vosi;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import org.astrogrid.dataservice.service.DataServer;

/**
 * Servlet to report service availability.
 * @author Guy Rixon
 * @author Kona Andrews
 */
public class AvailabilityServlet extends VosiServlet {
  
  /** 
   * Writes the availability document.
   *
   * @param catalogNames Not used.
   * @param chosenCatalog Not used.
   * @param writer The output stream.
   */
  protected void output(String[] catalogNames,
                        String   chosenCatalog,
                        Writer   writer) throws IOException {

    

    // Let the DataServer do the testing
    boolean available = DataServer.isAvailable();

    writer.write("<?xml-stylesheet type='text/xsl' href='availability.xsl'?>\n");
    writer.write(
          "<avail:availability\n" + 
          "   xmlns:avail='http://www.ivoa.net/xml/Availability/v0.4'\n" +
          "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n" +
          "   xsi:schemaLocation='http://www.ivoa.net/xml/Availability/v0.4 Availability.xsd'>\n");
    writer.write(String.format("  <avail:available>%b</avail:available>\n", available));

    writer.write("  <avail:upSince>");
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    writer.write(f.format(getStartTime()));
    writer.write("</avail:upSince>\n");

    writer.write("</avail:availability>\n");
  }
}
