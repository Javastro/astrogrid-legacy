package org.astrogrid.tools.WebServiceDelegate;

import org.apache.axis.enum.Style;

public abstract class MsgWebServiceDelegate extends WebServiceDelegate {
	
	public MsgWebServiceDelegate() {
		super();
		super.setOperationStyle(Style.MESSAGE);
	}
}