package org.astrogrid.taverna.astrogrid_taverna_suite.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import org.astrogrid.taverna.astrogrid_taverna_suite.CEAActivity;

public class CEAActivityContextViewFactory implements
		ContextualViewFactory<CEAActivity> {

	public boolean canHandle(Object selection) {
		return selection instanceof CEAActivity;
	}

	public List<ContextualView> getViews(CEAActivity selection) {
		return Arrays.<ContextualView>asList(new CEAContextualView(selection));
	}
	
}
