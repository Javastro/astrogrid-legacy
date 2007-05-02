/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.util.Comparator;

import org.astrogrid.acr.ivoa.resource.Resource;
/**
 * comparator that compares resources based on their 'title' attribute
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20071:54:22 PM
 */
public final class ResourceTitleComparator implements Comparator {
	public int compare(Object arg0, Object arg1) {
		Resource a = (Resource)arg0;
		Resource b = (Resource)arg1;
		return a.getTitle().compareTo(b.getTitle());
	}
}