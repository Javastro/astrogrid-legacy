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
package org.astrogrid.applications.integration.http.invalidapp;

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
        return AbstractTestForIntegration.HTTP_INVALID;
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
        return new String[]{AbstractTestForIntegration.HTTP_HELLO_WORLD};
    }
  

    public static final String TEST_CONTENTS = "Hello World";
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateDirectTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public void populateDirectTool(Tool tool) {
    	//no params 
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateIndirectTool(org.astrogrid.workflow.beans.v1.Tool, java.lang.String, java.lang.String)
     */
    public void populateIndirectTool(Tool tool, String inputURI, String outputURI) {
        //no params
    }
}


/* 
$Log: HttpProviderServerInfo.java,v $
Revision 1.2  2004/09/02 11:18:09  jdt
Merges from case 3 branch for SIAP.

Revision 1.1.2.1  2004/08/23 13:18:02  jdt
More tests, and some package info.

Revision 1.1.2.1  2004/08/20 00:36:19  jdt
moved HelloWorld tests

Revision 1.1.2.1  2004/08/18 11:34:32  jdt
First integration tests - check that the server is up and OK.  And
test the hello world app.....more to follow

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/