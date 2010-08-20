package org.astrogrid.taverna.astrogrid_taverna_suite.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.astrogrid.taverna.astrogrid_taverna_suite.CEAActivity;
import org.astrogrid.taverna.astrogrid_taverna_suite.ui.config.CEAConfigureAction;

public class CEAConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<CEAActivity> {

	public CEAConfigureMenuAction() {
		super(CEAActivity.class);
	}

	@Override
	protected Action createAction() {
		CEAActivity a = findActivity();
		Action result = null;
		result = new CEAConfigureAction(findActivity(), getParentFrame());
		result.putValue(Action.NAME, "Configure dsa service");
		addMenuDots(result);
		return result;
	}

}
