/**
 * 
 */
package examples;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.resource.Resource;

/**
 * This example walks through all the steps of querying a Simple Image Access service.
 * 1) locating the service
 * 2) selecting the position to query on
 * 3) inspecting the query return
 * 4) saving the resulting images to myspace.
 * 
 * This example will prompt the user to login before myspace is accessed.
 * Hence, it requires a valid Astrogrid account.
 * 
 * @author Noel Winstanley
 * @since Oct 11, 200612:05:38 PM
 */
public class SiapExampleToMyspace {
	public static void main(String[] args) throws Exception {
		try {
			// connect to ACR
			Finder f = new Finder();
			ACR acr = f.find();
			
			// get the components we're going to use.
			Siap siap = (Siap)acr.getService(Siap.class);
			Registry reg = (Registry)acr.getService(Registry.class);
			Sesame ses = (Sesame)acr.getService(Sesame.class);
			Myspace ms = (Myspace)acr.getService(Myspace.class);

			// list all Sia services known to the registry
			String xq = siap.getRegistryXQuery();
			Resource[] res = reg.xquerySearch(xq);
			
			// select the first one.
			Resource r = res[0];
			System.out.println("Will query: " + r.getTitle());
			
			// resolve an object name to a position
			SesamePositionBean pos = ses.resolve("crab");
			System.out.println("'crab' resolves to " + pos);
			
			// constryct a query to the service.
			URL query = siap.constructQuery(r.getId(),pos.getRa(),pos.getDec(),1.0);
			// execute the query
			Map[] rows = siap.execute(query);
			if (rows.length == 0) {
				System.out.println("No results returned");
				System.exit(0);
			}	
				
			// list the column headings of the query response.
			System.out.println("Columns Names: " + rows[0].keySet());
			
			//scan for rows pointing to fits images (this service also returns gif and html images)
			List indexes = new ArrayList();
			for (int i = 0; i < rows.length; i++) {
				if (rows[i].get("Format").equals("image/fits")) {
					indexes.add(new Integer(i));
				}
			}
			
			System.out.println("Selected Indexes " + indexes);
			
			
			// create a directory to store these images.
			URI home = ms.getHome();
			URI resultsDir = ms.createChildFolder(home,"SiapExampleResults");
			
			// download the selected images.
			siap.saveDatasetsSubset(query,resultsDir,indexes);
			
			System.out.println("Done: Images saved to " + resultsDir);
	} finally {
			System.exit(0);
	}
	}
}
