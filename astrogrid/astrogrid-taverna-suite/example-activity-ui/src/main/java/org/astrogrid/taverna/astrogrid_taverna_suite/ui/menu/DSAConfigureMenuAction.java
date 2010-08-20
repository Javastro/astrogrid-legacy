package org.astrogrid.taverna.astrogrid_taverna_suite.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.astrogrid.taverna.astrogrid_taverna_suite.DSAActivity;
import org.astrogrid.taverna.astrogrid_taverna_suite.ui.config.DSAConfigureAction;

public class DSAConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<DSAActivity> {

	public DSAConfigureMenuAction() {
		super(DSAActivity.class);
	}

	@Override
	protected Action createAction() {
		DSAActivity a = findActivity();
		Action result = null;
		result = new DSAConfigureAction(findActivity(), getParentFrame());
		result.putValue(Action.NAME, "Configure dsa service");
		addMenuDots(result);
		return result;
	}

}
