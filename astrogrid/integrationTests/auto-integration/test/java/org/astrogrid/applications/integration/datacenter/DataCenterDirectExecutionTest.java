/*$Id: DataCenterDirectExecutionTest.java,v 1.1 2004/07/01 11:43:33 nw Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.AbstractRunTestForCEA;
import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.workflow.beans.v1.Tool;


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 *
 */
public class DataCenterDirectExecutionTest extends AbstractRunTestForCEA {

    /** Construct a new DataCenterDirectExecutionTest
     * @param info
     * @param arg0
     */
    public DataCenterDirectExecutionTest( String arg0) {
        super(new DataCenterProviderServerInfo(), arg0);
    }

    /**@todo further checking of results.
     * @see org.astrogrid.applications.integration.AbstractRunTestForCEA#checkResults(org.astrogrid.applications.beans.v1.cea.castor.ResultListType)
     */
    protected void checkResults(ResultListType results) throws Exception {
        softAssertEquals("more than one result returned",1,results.getResultCount());
        ParameterValue result = results.getResult(0);
        assertNotNull(result);
        // result will be a votable. need to parse / verify this.
    }

    /**
     * @see org.astrogrid.applications.integration.AbstractRunTestForCEA#populateTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    protected void populateTool(Tool tool) throws Exception {
        serverInfo.populateDirectTool(tool);
    }

}


/* 
$Log: DataCenterDirectExecutionTest.java,v $
Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/