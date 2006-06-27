package org.astrogrid.desktop.modules.ui.sendto;

import javax.swing.AbstractAction;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** abstract base class for send-to-actions (STA's)*/
public abstract class AbstractSTA extends AbstractAction implements SendToAction {

	public AbstractSTA(String name, String description, String icon) {
		super(name, IconHelper.loadIcon(icon));
		putValue(AbstractAction.SHORT_DESCRIPTION,description);
		setEnabled(false);
	}

	public final void applyTo(PreferredTransferable atom, UIComponent parent) {
		if 	(checkApplicability(atom)) {
			this.atom = atom;
			this.parent = parent;
			setEnabled(true);
		} else {
			this.parent = null;
			this.atom = null;
			setEnabled(false);
		}
	}
	
	protected final PreferredTransferable getAtom() {
		return this.atom;
	}
	private PreferredTransferable atom;
	private UIComponent parent;
	
	protected final UIComponent getParent() {
		return this.parent;
	}
	
	protected abstract boolean checkApplicability(PreferredTransferable atom);

}