package org.astrogrid.activities.ardsa.query;

import net.sf.taverna.t2.partition.ActivityQuery;

public class AstrogridDSAActivityQuery extends ActivityQuery {
	
	public AstrogridDSAActivityQuery() {
		super("");
		
	}
	
	@Override
	public void doQuery() {
		AstrogridDSAActivityItem ai = new AstrogridDSAActivityItem();
		add(ai);
		
	}


}
