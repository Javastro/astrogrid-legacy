package org.astrogrid.tools.WebServiceDelegate;

import org.apache.axis.enum.Style;
import org.apache.axis.enum.Use;


public class DocWebServiceDelegate extends WebServiceDelegate {
	
	public DocWebServiceDelegate() {
		super();
		super.setOperationStyle(Style.DOCUMENT);
		super.setOperationUse(Use.LITERAL);
	}
}