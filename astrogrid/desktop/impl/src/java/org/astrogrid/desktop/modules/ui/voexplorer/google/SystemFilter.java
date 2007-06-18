package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.resource.Authority;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.DataService;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapService;

import ca.odell.glazedlists.matchers.Matcher;

/** Filter of 'system' resources. Some ad-hoc rules to identify uninteresting resources here..
 * 
 * @future encourage a more uniform way of denoting and detecting 'system' resources. - maybe all have a relationship to a 'systemKind' ?
 * @future will need to be more careful if many v1.0 resources have multiple capabilities.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 17, 200710:39:37 PM
 */
public final class SystemFilter implements Matcher {
	public static final URI FILESTORE_KIND = URI.create("ivo://org.astrogrid/FileStoreKind");
	public static final URI FILEMANAGER_KIND = URI.create("ivo://org.astrogrid/FileManagerKind");
	public static final URI MYSPACE_KIND = URI.create("ivo://org.astrogrid/MySpaceKind");
	public static final URI COMMUNITY_KIND = URI.create("ivo://org.astrogrid/CommunityServerKind");	
	public boolean matches(Object arg0) {
		Resource r = (Resource)arg0;
		
		//optimization -  stuff to pass thru straight away... (only specify interesting subtypes of service (as we'd be doing further processing on these later otherwise)
		if (
			(r instanceof ConeService)
			|| (r instanceof DataService)
			|| (r instanceof SiapService)
			) {
			return true;
		}
		// types to filter out.
		if (
				(r instanceof CeaService)
				|| (r instanceof Authority)
				|| (r instanceof RegistryService)
				|| StringUtils.contains(r.getType(),"Registry")
			) { 
			return false;
		}
		// kinds of service to filter out
		if (r instanceof Service) {

			Relationship[] relationships = r.getContent().getRelationships();
			for (int i = 0; i < relationships.length; i++) {
				Relationship rel = relationships[i];
				if (rel.getRelationshipType().equals("derived-from")) {
					URI id = rel.getRelatedResources()[0].getId();
					if (id != null && (
							id.equals(FILESTORE_KIND)
							|| id.equals(FILEMANAGER_KIND)
							|| id.equals(MYSPACE_KIND)
							|| id.equals(COMMUNITY_KIND)
					)
							) {
						return false;
					}
				}
			}
			// try looking at title instead.
			String t = r.getTitle();
			if (	 StringUtils.containsIgnoreCase(t,"Security Service")
					|| StringUtils.containsIgnoreCase(t,"Security Manager")
					|| StringUtils.containsIgnoreCase(t,"Policy Service")
					|| StringUtils.containsIgnoreCase(t,"MyProxy")
					|| StringUtils.containsIgnoreCase(t,"Myspace Manager")
			) {
				return false;
			}			
		}
		return true;
	}
}