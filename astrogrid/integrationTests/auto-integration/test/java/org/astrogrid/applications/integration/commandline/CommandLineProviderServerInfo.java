/*$Id: CommandLineProviderServerInfo.java,v 1.5 2004/11/24 19:49:22 clq2 Exp $
 * Created on 30-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.commandline;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.workflow.beans.v1.Tool;

/** Configuration details for the commandline cea server.
 * Calls the 'TESTAPP' application
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 * @see org.astrogrid.applications.integration.ServerInfo
 *
 */
public class CommandLineProviderServerInfo implements ServerInfo {

    /**
     * @see org.astrogrid.applications.integration.ServerInfo#getApplicationName()
     */
    public String getApplicationName() {
        return AbstractTestForIntegration.TESTAPP;
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#getServerSearchString()
     */
    public String getServerSearchString() {
        return  "command-line provider";
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#getApplicationNames()
     */
    public String[] getApplicationNames() {
        return new String[]{AbstractTestForIntegration.TESTAPP,AbstractTestForIntegration.TESTAPP2};
    }
  

    public static final String TEST_CONTENTS = "test contents";
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateDirectTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public void populateDirectTool(Tool tool) {
        ParameterValue pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P1']");
        pval.setValue("1"); // wait one second...
        pval.setIndirect(false);
        pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P4']");
        pval.setValue("test string");
        pval.setIndirect(false);
        pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P9']");
        pval.setValue(TEST_CONTENTS);
        pval.setIndirect(false);
        pval = (ParameterValue)tool.findXPathValue("output/parameter[name='P3']");
        pval.setIndirect(false);
        pval = (ParameterValue)tool.findXPathValue("output/parameter[name='P2']");
        pval.setIndirect(false);
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateIndirectTool(org.astrogrid.workflow.beans.v1.Tool, java.lang.String, java.lang.String)
     */
    public void populateIndirectTool(Tool tool, String inputURI, String outputURI) {
        ParameterValue pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P1']");
        pval.setValue("1"); // wait one second...
        pval.setIndirect(false);
        pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P4']");
        pval.setValue("test string");
        pval.setIndirect(false);
        pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P9']");
        pval.setValue(inputURI);
        pval.setIndirect(true);
        pval = (ParameterValue)tool.findXPathValue("output/parameter[name='P3']");
        pval.setValue(outputURI);
        pval.setIndirect(true);

    }
}


/* 
$Log: CommandLineProviderServerInfo.java,v $
Revision 1.5  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.2.46.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.2  2004/09/02 17:11:31  pah
update for new test application which does more

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/