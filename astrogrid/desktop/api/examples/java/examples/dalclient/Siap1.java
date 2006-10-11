/**
 * 
 */
package examples.dalclient;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Siap;

/** Transcribed from DALClient siap1.java - as a comparison of using DalClient vs AR
 * 
 * @author Noel Winstanley, from Doug Tody.
 * @since Oct 11, 20061:29:17 PM
 * 
 * Original Comment:
 * Call a Simple Image Access (SIA) service and print selected fields of
 * the query result table.
 *
 * Usage:        siap1 ra dec size [serviceURL]
 *
 * Or call with no args for the built-in unit test.
 */
 
public class Siap1 {
	 private static final String DEF_SERVICE = "http://skyview.gsfc.nasa.gov/cgi-bin/vo/sia.pl?";
	    private static final double DEF_RA = 12.0;
	    private static final double DEF_DEC = 0.0;
	    private static final double DEF_SIZE = 0.5;


    
public static void main(String[] args) throws Exception{
	  String service = DEF_SERVICE;
      double ra = DEF_RA;
      double dec = DEF_DEC;
      double size = DEF_SIZE;
      int arg = 0;

      if (args.length == 0) {

          // Built-in no-args unit test.

      } else if (args.length >= 3) {
          ra = Double.parseDouble(args[arg++]);
          dec = Double.parseDouble(args[arg++]);
          size = Double.parseDouble(args[arg++]);

          if (arg < args.length) {
              service = args[arg++];
          }

      } else {
          System.out.println("Usage: siap1 ra dec sr [siapURL]");
          System.exit(1);
      }
      callSiapService(service, ra, dec, size);
      System.exit(0);
  }



  /* Simple test routine to call a cone search service and summarize results.

   */

  static void callSiapService(String service, double ra, double dec, double sr) throws Exception {

	  	// Get a new connection to the service.
		// connect to ACR
		Finder f = new Finder();
		ACR acr = f.find(); 
		Siap siap = (Siap)acr.getService(Siap.class); 

		// Form the query.
		URL query = siap.constructQuery(new URI(service),ra, dec, sr);

		// Execute the query and fetch results.
		System.out.println("# Query: " + query);
		Map[] qr = siap.execute(query);

      if (qr.length <= 0) {
          System.out.println("no images found");
          System.exit(1);
      }

      // Summarize query response.

      {

          int nrec = qr.length;
          Map r = qr[0];
          int nattr = (r != null) ? r.size() : 0;
          System.out.println("# returns " + nrec + " records containing " +
              nattr + " attributes each");
          System.out.println("# --- Summary output ---");
      }

      // Summarize and print selected query results.
      for (int i = 0; i < qr.length; i++) {
    	  Map r = qr[i];
          String s_ra;
          String s_dec;
          String s_naxes;
          String s_naxis;
          String s_format;
          String s_title;
          Object v;

          s_ra = ((v = r.get("RA")) != null)
          	? v.toString()  : "<unknown>";
          
          s_dec = ((v = r.get("DEC")) != null) 
          	? v.toString()  : "<unknown>";
          
          s_naxes = ((v = r.get("Naxes")) != null)
          	? v.toString()  : "<unknown>";
          
          s_naxis = ((v = r.get("Naxis")) != null) 
          	? v.toString()  : "<unknown>";

          s_format = ((v = r.get("ImageFormat")) != null)
              ? v.toString() : "<unknown>";

          s_title = ((v = r.get("Title")) != null) 
          	? v.toString() : (((v = r.get("ID_MAIN")) != null) 
          			? v.toString() : "<unknown>");

          System.out.println("ra=" + atmost(s_ra, 10) + "\tdec=" +
              atmost(s_dec, 10) + "\t[" + s_naxis + "]\t" + s_format + "\t" +
              atmost(s_title, 32));
      }

  }



  /* Return at most N chars from string S. */
  private static String atmost(String s, int n) {
      return (s.substring(0, (s.length() < n) ? s.length() : n));
  }


}
