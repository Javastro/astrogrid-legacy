/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.dialogs.registry.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Strategy for filtering on list of content/types - whatever they are..
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:49:49 PM
 */
public class TypesStrategy extends PipelineStrategy {

	public Matcher createMatcher(final List selected) {
		return new Matcher() {
			public boolean matches(Object arg0) {
				Resource r = (Resource)arg0;
				final Content content = r.getContent();
				if (content == null || content.getSubject() == null || content.getSubject().length == 0) {
					return selected.contains(NONE_PROVIDED.get(0));
				}
				String[] subjects = content.getType();
				for (int i = 0; i < subjects.length; i++) {
					String subj = subjects[i];
					if (selected.contains(subj)) {
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
			final Resource r = (Resource)arg0;
			final Content content = r.getContent();
			if (content == null || content.getType() == null || content.getType().length == 0) {
				return NONE_PROVIDED;
			}
			final String[] types =  content.getType();
			final List result = new ArrayList(types.length);
			for (int i = 0; i < types.length; i++) {
				result.add(types[i]);
			}
			return result;
		}
});
}

	public String getName() {
		return "Content - Type";
	}

}
