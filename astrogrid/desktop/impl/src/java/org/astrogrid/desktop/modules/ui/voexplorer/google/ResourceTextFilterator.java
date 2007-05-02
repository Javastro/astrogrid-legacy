/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.util.List;

import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Resource;

import ca.odell.glazedlists.Filterator;
import ca.odell.glazedlists.TextFilterator;

/** Filterator for resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20076:55:58 PM
 */
public final class ResourceTextFilterator implements Filterator, TextFilterator {

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
		}

		//@todo add coverage later.
	}

	public void getFilterStrings(List arg0, Object arg1) {
		getFilterValues(arg0,arg1);
	}

}
