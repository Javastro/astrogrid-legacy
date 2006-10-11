/**
 * 
 */
package examples.dalclient;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Siap;

/** Transcribed from DALClient siap2.java - as a comparison of using DalClient vs AR
 * 
 * @author Noel Winstanley, from Doug Tody.
 * @since Oct 11, 20061:29:17 PM
 * 
 * Call a Simple Image Access (SIA) service and download a number of the
 * images referenced in the query result table.
 *
 * Usage:        siap4 ra dec size [format [maximages [serviceURL]]]
 *
 * Or call with no args for the built-in unit test.
 */
 
public class Siap4 {
	private static final String SERVICE1 = "http://www-gsss.stsci.edu/gscvo/DSS2.jsp";
    private static final String SERVICE2 = "http://skyview.gsfc.nasa.gov/cgi-bin/vo/sia.pl?";
    private static final double DEF_RA = 12.0;
    private static final double DEF_DEC = 10.0;
    private static final double DEF_SIZE = 0.2;
    private static final String FORMAT = "ALL";
    private static final int MAXIMAGES = 5;




public static void main(String[] args) throws Exception{
	  String service = SERVICE2;
	  String format = FORMAT;
      double ra = DEF_RA;
      double dec = DEF_DEC;
      double size = DEF_SIZE;
      int maximages = MAXIMAGES;
      int arg = 0;

      if (args.length == 0) {

          // Built-in no-args unit test.

      } else if (args.length >= 3) {
          ra = Double.parseDouble(args[arg++]);
          dec = Double.parseDouble(args[arg++]);
          size = Double.parseDouble(args[arg++]);
   
          if (arg < args.length) {
              format = args[arg++];
          }

          if (arg < args.length) {
              maximages = Integer.parseInt(args[arg++]);
          }         

          if (arg < args.length) {
              service = args[arg++];
          }

      } else {
          System.out.println("Usage: siap4 ra dec size [format [maximages [URL]]]");
          System.exit(1);
      }
      callSiapService(service, ra, dec, size,format, maximages);
      System.exit(0);
  }



  /* Simple test routine to call a cone search service and summarize results.

   */

  static void callSiapService(String service, double ra, double dec, double sr, String format, int maximages) throws Exception {

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
      }
      
      System.out.println("Downloading images: ");
      
      int imageCount = Math.min(qr.length, maximages);
      List indexes = new ArrayList();
      for (int i = 0; i < imageCount; i++) {
    	   indexes.add(new Integer(i));
      }
      // create a directory to write results to
      File loc = new File("siap4");
      loc.mkdir();
      // in comparison to DALClient, this is a bulk save - but doesn't
      // give any feedback on whether images fail to download.
      siap.saveDatasetsSubset(query,loc.toURI(),indexes);
      System.out.println("Downloaded");
  }
}