/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.votech.Annotation;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.UserAnnotation;

import ca.odell.glazedlists.Filterator;
import ca.odell.glazedlists.TextFilterator;

/** Filterator for resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20076:55:58 PM
 */
public final class ResourceTextFilterator implements Filterator, TextFilterator {

	private final AnnotationService annotationService;
	
	
	public ResourceTextFilterator(final AnnotationService annotationService) {
		super();
		this.annotationService = annotationService;
	}

	public void getFilterValues(List l, Object arg1) {
		final Resource res = (Resource)arg1;
		l.add(res.getId().toString());
		l.add(res.getTitle());
		l.add(res.getShortName());
		final Content content = res.getContent();
		if (content != null) {
			final String[] subject = content.getSubject();
			if (subject != null) {
				for (int i = 0; i < subject.length; i++) {
					l.add(subject[i]);
				}
			}
			final String[] type = content.getType();
			if (type != null) {
				for (int i = 0; i < type.length; i++) {
					l.add(type[i]);
				}
			}
		// unsure whether to include this one..
		l.add(content.getDescription());
		
		// check for annotations too.
		for (Iterator i = annotationService.getLocalAnnotations(res); i.hasNext(); ) {
			Annotation a = (Annotation)i.next();
			String t = a.getAlternativeTitle();
			String n = a.getNote();
			if (t != null) {
				l.add(t);
			}
			if (n != null) {
				l.add(n);
			}
			Set tags = a.getTags();
			if (tags != null) {
			    l.addAll(tags);
			}
			if (a instanceof UserAnnotation 
					&& ((UserAnnotation)a).isFlagged()) {
				l.add("FLAGGED");
			}			
		}
		}

		//@todo add coverage later.
	}

	public void getFilterStrings(List arg0, Object arg1) {
		getFilterValues(arg0,arg1);
	}

}
