/*$Id: DataCenterDirectExecutionTest.java,v 1.4 2004/11/19 10:27:29 clq2 Exp $
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
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.workflow.beans.v1.Tool;


/** test the Datacetner CEA with direct parameters
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 * @see org.astrogrid.applications.integration.AbstractRunTestForCEA
 * @see org.astrogrid.applications.integration.datacenter.DataCenterProviderServerInfo
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

    /**
     * @see org.astrogrid.applications.integration.AbstractRunTestForCEA#checkResults(org.astrogrid.applications.beans.v1.cea.castor.ResultListType)
     */
    protected void checkResults(ResultListType results) throws Exception {
        softAssertEquals("more than one result returned",1,results.getResultCount());
        ParameterValue result = results.getResult(0);
        assertNotNull(result);
        AstrogridAssert.assertVotable(result.getValue());
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
Revision 1.4  2004/11/19 10:27:29  clq2
nww-itn07-659

Revision 1.3.52.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.3  2004/08/27 13:16:52  nw
used AstrogridAssert to check results more thoroughly.

Revision 1.2  2004/08/13 14:04:46  nw
documentation change only

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/