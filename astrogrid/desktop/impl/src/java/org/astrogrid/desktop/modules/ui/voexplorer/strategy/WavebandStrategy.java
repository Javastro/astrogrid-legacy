/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Coverage;
import org.astrogrid.acr.ivoa.resource.HasCoverage;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/**Filters on coverage / wavebamd
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:49:49 PM
 */
public class WavebandStrategy extends PipelineStrategy {

	@Override
    public Matcher<Resource> createMatcher(final List<String> selected) {
		return new Matcher<Resource>() {
			public boolean matches(final Resource arg0) {
				if (! (arg0 instanceof HasCoverage)) {
					return selected.contains(NONE_PROVIDED.get(0));
				}
				final Coverage c = ((HasCoverage)arg0).getCoverage();
				if (c == null || c.getWavebands() == null || c.getWavebands().length == 0) {
					return  selected.contains(NONE_PROVIDED.get(0));
				}				
				final String[] bands = c.getWavebands();
				for (int i = 0; i < bands.length; i++) {
					final String subj = bands[i];
					if( selected.contains(subj)) {
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
		public List<String> getChildren(final Resource arg0) {
			if (! ( arg0 instanceof HasCoverage) ) {
				return NONE_PROVIDED;
			}
			final Coverage c = ((HasCoverage)arg0).getCoverage();
			if (c == null || c.getWavebands().length == 0) {
				return NONE_PROVIDED;
			}			
			final String[] bands = c.getWavebands();
			final List<String> result = new ArrayList<String>(bands.length);
			for (int i = 0; i < bands.length; i++) {
				result.add(bands[i]);
			}
			return result;
		}
});
}

	@Override
    public String getName() {
		return "Coverage - Waveband";
	}

}
