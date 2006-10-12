/**
 * 
 */
package examples;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.PlasticHubListener;

/**
 * This example walks through all the steps of querying a Cone service and displaying the
 * results in a plastic application (requires something like Topcat to be connected to the hub)
 * 1) locating the service
 * 2) selecting the position to query on
 * 3) instructing Topcat to display query result.
 * 
 * @author Noel Winstanley
 * @since Oct 11, 200612:05:38 PM
 */
public class ConeExampleToPlastic {
	public static void main(String[] args) throws Exception {
			// connect to ACR
			Finder f = new Finder();
			ACR acr = f.find();
			
			// get the components we're going to use.
			Cone cone = (Cone)acr.getService(Cone.class);
			PlasticHubListener hub = (PlasticHubListener)acr.getService(PlasticHubListener.class);
			Registry reg = (Registry)acr.getService(Registry.class);
			Sesame ses = (Sesame)acr.getService(Sesame.class);

			// list all Sia services known to the registry
			String xq = cone.getRegistryXQuery();
			Resource[] res = reg.xquerySearch(xq);
			
			// select the first one.
			Resource r = res[0];
			System.out.println("Will query: " + r.getTitle());
			
			// resolve an object name to a position
			SesamePositionBean pos = ses.resolve("crab");
			System.out.println("'crab' resolves to " + pos);
			
			// constryct a query to the service.
			URL query = cone.constructQuery(r.getId(),pos.getRa(),pos.getDec(),1.0);
			
			// register with the plastic Hub
			URI myId = hub.registerNoCallBack("ConeExampleToPlastic");
			// compose and send the message
			List messageArgs = new ArrayList();
			messageArgs.add(query);
			messageArgs.add(query);
			hub.requestAsynch(myId,CommonMessageConstants.VOTABLE_LOAD_FROM_URL,messageArgs);
			// deregister
			hub.unregister(myId);
		
			System.exit(0);
	}
}
