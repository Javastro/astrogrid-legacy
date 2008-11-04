/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.util.Comparator;

import org.astrogrid.acr.ivoa.resource.Resource;
/**
 * Comparator that compares resources based on their 'title' attribute.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20071:54:22 PM
 */
public final class ResourceTitleComparator implements Comparator {
	public int compare(final Object arg0, final Object arg1) {
		final Resource a = (Resource)arg0;
		final Resource b = (Resource)arg1;
		if (a == null || a.getTitle() == null) {
		    return -1;
		} 
		if (b == null || b.getTitle() == null) {
		    return 1;
		}
		return a.getTitle().compareTo(b.getTitle());
	}
}