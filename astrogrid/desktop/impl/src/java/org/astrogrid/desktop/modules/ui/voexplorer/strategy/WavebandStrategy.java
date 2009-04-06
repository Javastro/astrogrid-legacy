/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import org.astrogrid.acr.ivoa.resource.Coverage;
import org.astrogrid.acr.ivoa.resource.HasCoverage;
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
    public Matcher createMatcher(final List<JMenuItem> selected) {
		return new Matcher() {
			public boolean matches(final Object arg0) {
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
    public TransformedList createView(final EventList base) {	
		return new CollectionList(base,
			new CollectionList.Model() {
		public List getChildren(final Object arg0) {
			if (! ( arg0 instanceof HasCoverage) ) {
				return NONE_PROVIDED;
			}
			final Coverage c = ((HasCoverage)arg0).getCoverage();
			if (c == null || c.getWavebands().length == 0) {
				return NONE_PROVIDED;
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

	@Override
    public String getName() {
		return "Coverage - Waveband";
	}

}
