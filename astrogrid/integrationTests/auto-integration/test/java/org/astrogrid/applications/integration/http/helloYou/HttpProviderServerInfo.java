/*$Id: HttpProviderServerInfo.java,v 1.2 2004/09/14 16:35:56 jdt Exp $
 * Created on 30-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.http.helloYou;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author jdt
 *
 */
public class HttpProviderServerInfo implements ServerInfo {

    /**
     * @see org.astrogrid.applications.integration.ServerInfo#getApplicationName()
     */
    public String getApplicationName() {
        return AbstractTestForIntegration.HTTP_HELLO_YOU;
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#getServerSearchString()
     */
    public String getServerSearchString() {
        return  "cea http provider";
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#getApplicationNames()
     */
    public String[] getApplicationNames() {
    	assert false : "I really don't expect to be called";
        return new String[]{AbstractTestForIntegration.HTTP_HELLO_YOU};
    }
  

    public static final String TEST_CONTENTS = "Hello Boys";
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateDirectTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public void populateDirectTool(Tool tool) {
    	ParameterValue pval_name = (ParameterValue)tool.findXPathValue("input/parameter[name='name']");
    	pval_name.setValue("Boys");
    	pval_name.setIndirect(false);
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateIndirectTool(org.astrogrid.workflow.beans.v1.Tool, java.lang.String, java.lang.String)
     */
    public void populateIndirectTool(Tool tool, String inputURI, String outputURI) {
    	ParameterValue pval_name = (ParameterValue)tool.findXPathValue("input/parameter[name='name']");
    	pval_name.setValue("Boys");
    	pval_name.setIndirect(false);
    	
        ParameterValue pval = (ParameterValue)tool.findXPathValue("output/parameter[name='reply']");
        pval.setValue(outputURI);
        pval.setIndirect(true);
    }
}


/* 
$Log: HttpProviderServerInfo.java,v $
Revision 1.2  2004/09/14 16:35:56  jdt
Take responsibity for my sins.

Revision 1.1  2004/09/13 18:14:53  jdt
Added a new cea-http integration test to see if saving xml to
mySpace is causing any problems.


*/