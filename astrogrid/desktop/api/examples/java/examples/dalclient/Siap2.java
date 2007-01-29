/**
 * 
 */
package examples.dalclient;

import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.util.Tables;

/** Transcribed from DALClient siap2.java - as a comparison of using DalClient vs AR
 * 
 * @author Noel Winstanley, from Doug Tody.
 * @since Oct 11, 20061:29:17 PM
 * 
* Call a Simple Image Access (SIA) service and print the results in CSV format.
 *
 * Usage:        siap2 ra dec size [serviceURL]
 *
 * Or call with no args for the built-in unit test.
 */
 
public class Siap2 {
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
          System.out.println("Usage: siap2 ra dec sr [siapURL]");
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
		Tables tables = (Tables)acr.getService(Tables.class);
		
		// Form the query.
		URL query = siap.constructQuery(new URI(service),ra, dec, sr);

		// Execute the query and fetch results.
		System.out.println("# Query: " + query);
		String csv = tables.convertFromFile(new URI(query.toString()),"votable","csv");
		System.out.println(csv);
  }
}