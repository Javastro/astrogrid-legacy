/*$Id: HttpProviderServerInfo.java,v 1.6 2004/11/19 10:27:29 clq2 Exp $
 * Created on 30-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.http;

import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.workflow.beans.v1.Tool;

/** Information on the Http CEA server
 * calls the 'HTTP_ADDER_GET'
 * @author jdt
 * @link org.astrogrid.applications.integration.ServerInfo
 *
 */
public class HttpProviderServerInfo implements ServerInfo {

    /**
     * @see org.astrogrid.applications.integration.ServerInfo#getApplicationName()
     */
    public String getApplicationName() {
    	assert false : "I shouldn't be called";
    	return AbstractTestForIntegration.HTTP_ADDER_GET;
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
        return new String[]{AbstractTestForIntegration.HTTP_HELLO_WORLD, AbstractTestForIntegration.HTTP_ADDER_GET, AbstractTestForIntegration.HTTP_ADDER_POST, AbstractTestForIntegration.HTTP_HELLO_YOU};
    }
  

        /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateDirectTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public void populateDirectTool(Tool tool) {
    	assert false : "I shouldn't be called";
    }
    /**
     * @see org.astrogrid.applications.integration.ServerInfo#populateIndirectTool(org.astrogrid.workflow.beans.v1.Tool, java.lang.String, java.lang.String)
     */
    public void populateIndirectTool(Tool tool, String inputURI, String outputURI) {
    	assert false : "I shouldn't be called";
    }
}


/* 
$Log: HttpProviderServerInfo.java,v $
Revision 1.6  2004/11/19 10:27:29  clq2
nww-itn07-659

Revision 1.5.34.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.5  2004/09/15 17:09:01  jdt
Removed superfluous imports.

Revision 1.4  2004/09/14 16:35:15  jdt
Added tests for an http-post service.

Revision 1.3  2004/09/13 18:14:53  jdt
Added a new cea-http integration test to see if saving xml to
mySpace is causing any problems.

Revision 1.2  2004/09/02 11:18:09  jdt
Merges from case 3 branch for SIAP.

Revision 1.1.2.2  2004/08/20 00:36:19  jdt
moved HelloWorld tests

Revision 1.1.2.1  2004/08/18 11:34:32  jdt
First integration tests - check that the server is up and OK.  And
test the hello world app.....more to follow

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/