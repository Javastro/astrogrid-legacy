/*$Id: JavaProviderServerInfo.java,v 1.4 2004/11/24 19:49:22 clq2 Exp $
 * Created on 30-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.workflow.beans.v1.Tool;

/** server provider info for the java cea server
 * <p>
 * also used as a place to hang methods used in all java provider scripts
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 *
 */
public class JavaProviderServerInfo implements ServerInfo{


    /** main testing application is "SUM"
     * @see org.astrogrid.applications.integration.AbstractRunTestForCEA.ServerInfo#getApplicationName()
     */
    public String getApplicationName() {
        return AbstractTestForIntegration.SUM;
    }

    /**
     * @see org.astrogrid.applications.integration.AbstractRunTestForCEA.ServerInfo#getServerSearchString()
     */
    public String getServerSearchString() {
        return "java provider";
    }
    
    public String[] getApplicationNames() {
       return new String[] {AbstractTestForIntegration.HELLO_WORLD,AbstractTestForIntegration.SUM,AbstractTestForIntegration.HELLO_YOU};
   }

    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateDirectTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public void populateDirectTool(Tool tool) {
        ParameterValue pval = (ParameterValue)tool.findXPathValue("input/parameter[name='parameter-0']");
        pval.setValue("2"); 
        pval.setIndirect(false);
        pval = (ParameterValue)tool.findXPathValue("input/parameter[name='parameter-1']");
        pval.setValue("40");
        pval.setIndirect(false);
        pval = (ParameterValue)tool.findXPathValue("output/parameter[name='result']");
        pval.setIndirect(false);
    }

    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateIndirectTool(org.astrogrid.workflow.beans.v1.Tool, java.lang.String, java.lang.String)
     */
    public void populateIndirectTool(Tool tool, String inputURI, String outputURI) {
        ParameterValue pval = (ParameterValue)tool.findXPathValue("input/parameter[name='parameter-0']");
        pval.setValue("2"); 
        pval.setIndirect(false);
        pval = (ParameterValue)tool.findXPathValue("input/parameter[name='parameter-1']");
        pval.setValue(inputURI);
        pval.setIndirect(true);
        pval = (ParameterValue)tool.findXPathValue("output/parameter[name='result']");
        pval.setIndirect(true);       
        pval.setValue(outputURI);         
    }
}


/* 
$Log: JavaProviderServerInfo.java,v $
Revision 1.4  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.1.104.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/