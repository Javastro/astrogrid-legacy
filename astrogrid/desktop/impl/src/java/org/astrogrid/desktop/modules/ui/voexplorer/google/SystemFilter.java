package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.ivoa.resource.Authority;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.DataService;
import org.astrogrid.acr.ivoa.resource.Organisation;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.acr.ivoa.resource.SsapService;
import org.astrogrid.acr.ivoa.resource.StapService;
import org.astrogrid.acr.ivoa.resource.TableService;
import org.astrogrid.contracts.StandardIds;

import ca.odell.glazedlists.matchers.Matcher;

/** Filter of 'system' resources. Some ad-hoc rules to identify uninteresting resources here..
 * 
 * @future encourage a more uniform way of denoting and detecting 'system' resources. - maybe all have a relationship to a 'systemKind' ?
 * @future will need to be more careful if many v1.0 resources have multiple capabilities.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 17, 200710:39:37 PM
 */
public final class SystemFilter implements Matcher {
    /* unused now
	public static final URI FILESTORE_KIND = URI.create("ivo://org.astrogrid/FileStoreKind");
	public static final URI FILEMANAGER_KIND = URI.create("ivo://org.astrogrid/FileManagerKind");
	public static final URI MYSPACE_KIND = URI.create("ivo://org.astrogrid/MySpaceKind");
	public static final URI COMMUNITY_KIND = URI.create("ivo://org.astrogrid/CommunityServerKind");	
*/
	public boolean matches(Object arg0) {
		Resource r = (Resource)arg0;
		
		//optimization -  stuff to pass thru straight away... (only specify interesting subtypes of service (as we'd be doing further processing on these later otherwise)
		if (
			(r instanceof ConeService)
			|| (r instanceof DataService)
			|| ( r instanceof TableService)
			|| (r instanceof SiapService)
			|| (r instanceof SsapService)
			|| (r instanceof StapService)
			|| (r instanceof Organisation)
			|| (r instanceof DataCollection)
			|| (r instanceof CeaApplication)
			) {		    
			return true;
		}
		//things we know are boring.
		if (
				(r instanceof CeaService)
				|| (r instanceof Authority)
				|| (r instanceof RegistryService)
			// covered in above clause.	|| StringUtils.contains(r.getType(),"Registry")
			) { 
			return false;
		}
		// kinds of service to filter out
		if (r instanceof Service
		        && onlyBoringCapabilities((Service)r)) {
				return false;					
		}
		// if in doubt.. keep it in.
		return true;
	}

	/** returns true if the only capabilities this service has are boring ones */
    public static boolean onlyBoringCapabilities(Service s) {
        Capability[] caps = s.getCapabilities();        
        if (caps.length == 0) {
            return true; // nothing here - it's boring.
        }
        for (int i = 0; i < caps.length; i++) {
            Capability c = caps[i];
            if (! isBoringCapability(c)) {
                return false;
            }
        }
        return true; // not found an unboring one.
    }
    
    public static boolean isBoringCapability(Capability cap) {
        URI std = cap.getStandardID();
        if (std == null) { // an unlabeled std - is it boring or not - we judge not, for now.
            return false;
        }
        return boringCapabilities.contains(std);
    }
    
    
/** a set of the boring capabilities */
    private static final Set boringCapabilities = new TreeSet(
            Arrays.asList(
                    new URI[] {
                            URI.create(StandardIds.CEA_1_0)
                            ,URI.create(StandardIds.MY_PROXY_2)
                            ,URI.create(StandardIds.POLICY_MANAGER_1_0)
                            ,URI.create(StandardIds.REGISTRY_1_0)
                            ,URI.create(StandardIds.SECURITY_SERVICE_1_0)
                            ,URI.create(StandardIds.VOSI_APPLICATION_0_3)
                            ,URI.create(StandardIds.VOSI_AVAILABILITY_0_3)
                            ,URI.create(StandardIds.VOSI_CAPABILITIES_0_3)
                            ,URI.create(StandardIds.VOSI_TABLES_0_3)
                            // no standard IDs for these yet.
                            ,URI.create("ivo://org.astrogrid/std/Community/accounts")
                            ,URI.create("ivo://org.astrogrid/std/myspace/v1.0#myspace")
                    }
                    )
            );

}