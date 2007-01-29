/**
 * 
 */
package examples.dalclient;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.util.Tables;

/** Transcribed from DALClient cone3.java - as a comparison of using DalClient vs AR
 * 
 * @author Noel Winstanley, from Doug Tody.
 * @since Oct 11, 20061:29:17 PM
 * 
 * Call a cone search service and return the results as a VOTable.
 *
 * Usage:        cone3 ra dec sr [fname [serviceURL]]
 *
 * Or call with no args for the built-in unit test.
 */
 
public class Cone3 {
    private static final String DEF_SERVICE = "http://chart.stsci.edu/GSCVO/GSC22VO.jsp?";
    private static final double DEF_RA = 12.0;
    private static final double DEF_DEC = 12.0;
    private static final double DEF_SR = 0.1;
    private static final String FNAME = "cone3.xml";
    
public static void main(String[] args) throws Exception{
	  String service = DEF_SERVICE;
	  String fname = FNAME;
      double ra = DEF_RA;
      double dec = DEF_DEC;
      double sr = DEF_SR;
      int arg = 0;

      if (args.length == 0) {

          // Built-in no-args unit test.

      } else if (args.length >= 3) {
          ra = Double.parseDouble(args[arg++]);
          dec = Double.parseDouble(args[arg++]);
          sr = Double.parseDouble(args[arg++]);
          fname = (arg < args.length) ? args[arg++] : "cone3.out";
          
          if (arg < args.length) {
              service = args[arg++];
          }

      } else {
          System.out.println("Usage: cone3 ra dec sr [fname [coneURL]]");
          System.exit(1);
      }
      callConeService(service, ra, dec, sr, fname);
      System.exit(0);
}



  /* Simple test routine to call a cone search service and summarize results.

   */

  static void callConeService(String service, double ra, double dec, double sr, String fname) throws Exception {

	  	// Get a new connection to the service.
		// connect to ACR
		Finder f = new Finder();
		ACR acr = f.find(); 
		Cone cone = (Cone)acr.getService(Cone.class); 
		Tables tables = (Tables)acr.getService(Tables.class);

		// Form the query.
		URL query = cone.constructQuery(new URI(service),ra, dec, sr);

		// Execute the query and fetch results.
		System.out.println("# Query: " + query);

	        // Copy the query result VOTable to the name output file, overwriting
	        // it if the file already exists.
	        System.out.println("# copying query result VOTable to file " + fname);

	        File file = new File(fname);
	        file.delete();
	        cone.executeAndSave(query,file.toURI());
	
  }


}
