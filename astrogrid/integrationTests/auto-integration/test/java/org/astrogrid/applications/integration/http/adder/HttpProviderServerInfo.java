/*$Id: HttpProviderServerInfo.java,v 1.2 2004/09/02 11:18:09 jdt Exp $
 * Created on 30-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.http.adder;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 *
 */
public class HttpProviderServerInfo implements ServerInfo {

    /**
     * @see org.astrogrid.applications.integration.ServerInfo#getApplicationName()
     */
    public String getApplicationName() {
        return AbstractTestForIntegration.HTTP_ADDER;
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
    	assert false : "I don't expect to be called";
        return new String[]{AbstractTestForIntegration.HTTP_HELLO_WORLD, AbstractTestForIntegration.HTTP_ADDER};
    }
  

    public static final String TEST_CONTENTS = "9112001";
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateDirectTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public void populateDirectTool(Tool tool) {
    	ParameterValue pval_x = (ParameterValue)tool.findXPathValue("input/parameter[name='x']");
        pval_x.setValue("9110000");
        pval_x.setIndirect(false);
        
        ParameterValue pval_y = (ParameterValue)tool.findXPathValue("input/parameter[name='y']");
        pval_y.setValue("2001");
        pval_y.setIndirect(false);
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateIndirectTool(org.astrogrid.workflow.beans.v1.Tool, java.lang.String, java.lang.String)
     */
    public void populateIndirectTool(Tool tool, String inputURI, String outputURI) {
    	ParameterValue pval_x = (ParameterValue)tool.findXPathValue("input/parameter[name='x']");
        pval_x.setValue(inputURI);
        pval_x.setIndirect(true);
        
        ParameterValue pval_y = (ParameterValue)tool.findXPathValue("input/parameter[name='y']");
        pval_y.setValue("1");
        pval_y.setIndirect(false);
    	
    	ParameterValue pval = (ParameterValue)tool.findXPathValue("output/parameter[name='sum']");
        pval.setValue(outputURI);
        pval.setIndirect(true);
    }
}


/* 
$Log: HttpProviderServerInfo.java,v $
Revision 1.2  2004/09/02 11:18:09  jdt
Merges from case 3 branch for SIAP.

Revision 1.1.2.1  2004/08/20 00:42:56  jdt
added another test app

Revision 1.1.2.1  2004/08/18 11:34:32  jdt

 
*/