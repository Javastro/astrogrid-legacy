/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Filters on  content/types.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:49:49 PM
 */
public class TypesStrategy extends PipelineStrategy {

	@Override
    public Matcher<Resource> createMatcher(final List<String> selected) {
		return new Matcher<Resource>() {
			public boolean matches(final Resource r) {
				final Content content = r.getContent();
				if (content == null || content.getType() == null || content.getType().length == 0) {
					return selected.contains(NONE_PROVIDED.get(0));
				}
				final String[] types = content.getType();
				for (int i = 0; i < types.length; i++) {
					final String subj = types[i];
					if (selected.contains(subj)) {
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
			final Content content = r.getContent();
			if (content == null || content.getType() == null || content.getType().length == 0) {
				return NONE_PROVIDED;
			}
			final String[] types =  content.getType();
			final List<String> result = new ArrayList<String>(types.length);
			for (int i = 0; i < types.length; i++) {
				result.add(types[i]);
			}
			return result;
		}
});
}

	@Override
    public String getName() {
		return "Content - Type";
	}

}
