package org.astrogrid.tools.WebServiceDelegate;

import org.apache.axis.enum.Style;
import org.apache.axis.enum.Use;


public abstract class RPCWebServiceDelegate extends WebServiceDelegate {

	public RPCWebServiceDelegate() {
		super();
		/*
		 Axis default style is RPC.
		 super.setOperationStyle(Style.RPC);
		*/
	}
}