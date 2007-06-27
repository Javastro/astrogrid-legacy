/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;
import org.astrogrid.desktop.modules.votech.Annotation;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.UserAnnotation;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** strategy that that filters on tag annotations.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 20, 20071:47:40 AM
 */
public class TagStrategy extends PipelineStrategy {

	private final AnnotationService annServer;
	
	public TagStrategy(final AnnotationService annServer) {
		super();
		this.annServer = annServer;
	}

	public Matcher createMatcher(final List selected) {
		return new Matcher() {

			public boolean matches(Object arg0) {
				Resource r = (Resource)arg0;
				boolean seenNone = true;
				for (Iterator i = annServer.getLocalAnnotations(r); i.hasNext(); ) {
					Annotation a = (Annotation)i.next();
					if (a instanceof UserAnnotation 
							&& ((UserAnnotation)a).isFlagged()
							&& selected.contains("FLAGGED")) {
						return true;
					}					
					String[] tags = a.getTags();
					if (tags != null && tags.length !=0) {
						seenNone = false;
						for (int j = 0; j < tags.length; j++) {
							if (selected.contains(tags[j])) {
								return true;
							}
						}
					}
				}
				return seenNone && selected.contains(NONE_PROVIDED.get(0));
			}
		};
	}

	public TransformedList createView(EventList base) {
		return new CollectionList(base,
				new CollectionList.Model() {
			public List getChildren(Object arg0) {
				final Resource r = (Resource)arg0;
				List result = NONE_PROVIDED;
				for(Iterator i = annServer.getLocalAnnotations(r); i.hasNext(); ) {
					Annotation a = (Annotation)i.next();
					if (a instanceof UserAnnotation && ((UserAnnotation)a).isFlagged()) {
						if (result == NONE_PROVIDED) {
							result = new ArrayList();
						}						
						result.add("FLAGGED");
					}
					String[] tags = a.getTags();
					if (tags != null && tags.length != 0) {
						if (result == NONE_PROVIDED) {
							result = new ArrayList(tags.length);
						}
						for (int j = 0; j < tags.length; j++) {
							result.add(tags[j]);
						}
					}
				}
				return result;
			}
	});
	}

	public String getName() {
		return "Tags";
	}

}
