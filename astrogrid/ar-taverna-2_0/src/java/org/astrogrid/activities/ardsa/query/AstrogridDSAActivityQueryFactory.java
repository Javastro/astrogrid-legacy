package org.astrogrid.activities.ardsa.query;

import net.sf.taverna.t2.partition.ActivityQuery;
import net.sf.taverna.t2.partition.ActivityQueryFactory;


public class AstrogridDSAActivityQueryFactory extends ActivityQueryFactory {

	
	@Override
	protected ActivityQuery createQuery(String property) {
		return new AstrogridDSAActivityQuery();
	}
	
	@Override
	protected String getPropertyKey() {
		return "taverna.astrogrid.astroruntime.dsa";
	}
	
}
