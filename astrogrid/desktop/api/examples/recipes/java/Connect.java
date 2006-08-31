import org.astrogrid.acr.Finder;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.Configuration;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Jan-2006

minimal example of connecting to acr and calling a service
requires acr client jars in java classpath
connects to acr using rmi access 
 */
public class Connect {

    public static void main(String[] args) {
      try {
        Finder f = new Finder();
        ACR acr = f.find();
	// retrieve a service - by specifying the interface class
        Configuration conf = 
		(Configuration)acr.getService(Configuration.class);
	// call a method on this service.
        Map l = conf.list();
        for (Iterator i = l.entrySet().iterator(); i.hasNext(); ) {
            System.out.println(i.next());
        }
	// retrieve another service from the acr - this time by name
        Registry registry = (Registry)acr.getService("astrogrid.registry");
	
	// use this service.. 
        URI u = new URI("ivo://org.astrogrid/Pegase");
        System.out.println(registry.getResourceInformation(u)); 
	// returns a struct of data.
        // registry.getRecord(u) returns a org.w3c.dom.Document..

        u = new URI("ivo://uk.ac.le.star/filemanager");
        System.out.println(registry.resolveIdentifier(u)); 
	// returns a java.net.URL

      } catch (Exception e) {
            e.printStackTrace();
      }
      //shut the app down - necessary, as won't close by itself.
      System.exit(0);
    }
}

