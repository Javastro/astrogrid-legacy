package org.astrogrid.taverna.astrogrid_taverna_suite.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import org.astrogrid.taverna.astrogrid_taverna_suite.DSAActivity;

public class DSAActivityContextViewFactory implements
		ContextualViewFactory<DSAActivity> {

	public boolean canHandle(Object selection) {
		return selection instanceof DSAActivity;
	}

	public List<ContextualView> getViews(DSAActivity selection) {
		return Arrays.<ContextualView>asList(new DSAContextualView(selection));
	}
	
}
