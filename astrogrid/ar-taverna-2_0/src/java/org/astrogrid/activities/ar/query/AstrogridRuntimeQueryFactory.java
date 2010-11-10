package org.astrogrid.activities.ar.query;

import net.sf.taverna.t2.partition.ActivityQuery;
import net.sf.taverna.t2.partition.ActivityQueryFactory;

public class AstrogridRuntimeQueryFactory  extends ActivityQueryFactory {

	@Override
	protected ActivityQuery createQuery(String property) {
		return new AstrogridRuntimeQuery();
	}
	
	@Override
	protected String getPropertyKey() {
		return "taverna.astrogrid.astroruntime";
	}	
	
	
}
