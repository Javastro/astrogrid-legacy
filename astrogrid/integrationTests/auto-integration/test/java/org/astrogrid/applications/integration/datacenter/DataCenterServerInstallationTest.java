/*$Id: DataCenterServerInstallationTest.java,v 1.3 2004/11/19 14:17:56 clq2 Exp $
 * Created on 12-Mar-2004
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
import org.astrogrid.applications.integration.ServerInstallationTest;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/** Test  CEA installation for datacenter provider.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class DataCenterServerInstallationTest extends ServerInstallationTest {
    /**
     * Constructor for DataCenterIntegrationTest.
     * @param arg0
     */
    public DataCenterServerInstallationTest(String arg0) {
        super(new DataCenterProviderServerInfo(),arg0);
    }


}


/* 
$Log: DataCenterServerInstallationTest.java,v $
Revision 1.3  2004/11/19 14:17:56  clq2
roll back beforeMergenww-itn07-659

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor

Revision 1.2  2004/04/26 12:16:07  nw
got applications int test working.
dsa works, but suspect its failing under the hood.

Revision 1.1  2004/04/21 13:41:34  nw
set up applications integration tests

Revision 1.8  2004/04/21 10:44:05  nw
tidied to check applicatrions are resolvable.

Revision 1.7  2004/04/20 14:47:41  nw
changed to use an agsl for now

Revision 1.6  2004/04/19 09:35:24  nw
added constants for ivorns of services.
added test query

Revision 1.5  2004/04/15 23:11:20  nw
tweaks

Revision 1.4  2004/04/15 12:18:25  nw
updating tests

Revision 1.3  2004/04/15 10:28:40  nw
improving testing

Revision 1.2  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/