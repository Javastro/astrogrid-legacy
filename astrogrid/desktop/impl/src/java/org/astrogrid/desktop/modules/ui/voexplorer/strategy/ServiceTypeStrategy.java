/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Filters on list of service/types - these are specialized subtypes of a service, hidden within the capability.
 * there's one for siap, and I suspect there'll be similar for ssap (although it's not apparent yet). want to put them all in a single filter wheel,
 * and make it multi-valued, in preparationn for multiple capabilities in a single resource (v1.0)
 * @todo add support for ssap too.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:49:49 PM
 */
public class ServiceTypeStrategy extends PipelineStrategy {

	@Override
    public Matcher<Resource> createMatcher(final List<String> selected) {
		return new Matcher<Resource>() {
			public boolean matches(final Resource r) {
				if (r instanceof SiapService) {
				    final SiapService siap = (SiapService)r;				   
				    final SiapCapability cap = siap.findSiapCapability();
				    if (cap == null || StringUtils.isEmpty(cap.getImageServiceType())) {
				        return selected.contains(NONE_PROVIDED.get(0));
				    }
				    if (selected.contains(cap.getImageServiceType())) {
				        return true;
				    }
				}
				return false;
			}					
		};
	}

	@Override
    public TransformedList<Resource,String> createView(final EventList<Resource> base) {	
		return new CollectionList<Resource,String>(base,
			new CollectionList.Model<Resource,String>() {
		public List<String> getChildren(final Resource r) {
			final List<String> result = new ArrayList<String>();
            if (r instanceof SiapService) {
                final SiapService siap = (SiapService)r;
                final SiapCapability cap = siap.findSiapCapability();
                if (cap == null || StringUtils.isEmpty(cap.getImageServiceType())) {
                    result.add(NONE_PROVIDED.get(0));
                } else {
                    result.add(cap.getImageServiceType());               
                }
            }			
			
			return result;
		}
});
}

	@Override
    public String getName() {
		return "Image Service Type";
	}

}
