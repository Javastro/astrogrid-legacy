/*$Id: DataCenterProviderServerInfo.java,v 1.5 2004/11/19 14:17:56 clq2 Exp $
 * Created on 30-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.datacenter;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.io.Piper;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import junit.framework.Assert;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 *
 */
public class DataCenterProviderServerInfo implements ServerInfo {

    public static final String SAMPLE_QUERY_RESOURCE = "/org/astrogrid/applications/integration/datacenter/DataCenterIntegrationTest-sample-query.xml";
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#getApplicationName()
     */
    public String getApplicationName() {
        return AbstractTestForIntegration.TESTDSA;
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#getServerSearchString()
     */
    public String getServerSearchString() {
        return "datacenter provider";
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#getApplicationNames()
     */
    public String[] getApplicationNames() {
        return new String[]{AbstractTestForIntegration.TESTDSA};
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateDirectTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public void populateDirectTool(Tool tool) {
        ParameterValue query= (ParameterValue)tool.findXPathValue("input/parameter[name='Query']");
        query.setIndirect(false);
        InputStream is = this.getClass().getResourceAsStream(SAMPLE_QUERY_RESOURCE);
        StringWriter out = new StringWriter();
        try {
            Piper.pipe(new InputStreamReader(is),out);
        } catch (Exception e) {
            Assert.fail("Could not read query " + e.getMessage());
        } 
        query.setValue(out.toString());           

        ParameterValue format= (ParameterValue)tool.findXPathValue("input/parameter[name='Format']");
        format.setIndirect(false);
        format.setValue("VOTABLE");// rely on default values.
        ParameterValue target = (ParameterValue)tool.findXPathValue("output/parameter[name='Result']");
        target.setIndirect(false);        
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateIndirectTool(org.astrogrid.workflow.beans.v1.Tool, java.lang.String, java.lang.String)
     */
    public void populateIndirectTool(Tool tool, String inputURI, String outputURI) {
        ParameterValue query= (ParameterValue)tool.findXPathValue("input/parameter[name='Query']");
        query.setIndirect(true);
        query.setValue(inputURI);           

        ParameterValue format= (ParameterValue)tool.findXPathValue("input/parameter[name='Format']");
        format.setIndirect(false); 
        format.setValue("VOTABLE");
        ParameterValue target = (ParameterValue)tool.findXPathValue("output/parameter[name='Result']");
        target.setIndirect(true);
        target.setValue(outputURI);   
    }
    
 
}


/* 
$Log: DataCenterProviderServerInfo.java,v $
Revision 1.5  2004/11/19 14:17:56  clq2
roll back beforeMergenww-itn07-659

Revision 1.3  2004/07/20 01:59:25  nw
testing new datacenter cea

Revision 1.2  2004/07/19 10:21:44  nw
fixed these to refer to the correct v06 cea datacenter application

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/