package examples;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
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
public class Simplest {

    public static void main(String[] args) throws Exception {
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
        Registry registry = (Registry)acr.getService("ivoa.registry");
	
	// use this service.. 
        URI u = new URI("ivo://org.astrogrid/Pegase");
        // returns a datastructure representing the registry record
        Resource res =registry.getResource(u);
        System.out.println(res.getTitle());
        System.out.println(res.getContent().getDescription());


      //shut the app down - necessary, as won't close by itself.
      System.exit(0);
    }
}

