/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Coverage;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.HasCoverage;
import org.astrogrid.desktop.modules.dialogs.registry.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Strategy for filtering on list of coverage/waveband, for those Resource instances
 * that have it.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:49:49 PM
 */
public class WavebandStrategy extends PipelineStrategy {

	public Matcher createMatcher(final List selected) {
		return new Matcher() {
			public boolean matches(Object arg0) {
				if (! (arg0 instanceof HasCoverage)) {
					return false;
				}
				final Coverage c = ((HasCoverage)arg0).getCoverage();
				if (c == null) {
					return false; // no coverage provided
				}				
				String[] bands = c.getWavebands();
				for (int i = 0; i < bands.length; i++) {
					String subj = bands[i];
					if( selected.contains(subj)) {
						return true;
					}
				}
				return false;
			}					
		};
	}



	public TransformedList createView(EventList base) {	
		return new CollectionList(base,
			new CollectionList.Model() {
		public List getChildren(Object arg0) {
			if (! ( arg0 instanceof HasCoverage) ) {
				return ListUtils.EMPTY_LIST;
			}
			final Coverage c = ((HasCoverage)arg0).getCoverage();
			if (c == null) {
				return ListUtils.EMPTY_LIST;
			}			
			final String[] bands = c.getWavebands();
			final List result = new ArrayList(bands.length);
			for (int i = 0; i < bands.length; i++) {
				result.add(bands[i]);
			}
			return result;
		}
});
}

	public String getName() {
		return "Coverage - Waveband";
	}

}
