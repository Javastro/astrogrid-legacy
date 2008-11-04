package org.astrogrid.desktop.modules.ui.comp;

import org.astrogrid.desktop.modules.ui.UIComponent;

/** Guards an instance of UIComponent. - ensures
 * that all access to the parent is though the setter, not the 
 * member field reference - and traps any calls before parent is set.
 * 
 * Necessary when an object must be constructed in circumstances where
 * the parent is not yet known.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 5, 20071:03:28 PM
 */
public final class UIComponentBodyguard {
	private UIComponent parent;
	public UIComponent get() {
		if (parent == null) {
			throw new IllegalStateException("Parent accessed before set");
		}
		return parent;
	}		
	public void set(final UIComponent parent) {
		if (parent == null) {
			throw new IllegalArgumentException("Parent set to null");
		}
		if (this.parent != null) {
			throw new IllegalStateException("Parent already set");
		}
		this.parent = parent;
	}
}