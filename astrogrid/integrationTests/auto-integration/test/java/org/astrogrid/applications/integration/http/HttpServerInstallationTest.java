/*$Id: HttpServerInstallationTest.java,v 1.2 2004/09/02 11:18:09 jdt Exp $
 * Created on 29-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.http;

import org.astrogrid.applications.integration.ServerInstallationTest;

/** installation test for commandline cea server.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jun-2004
 *
 */
public class HttpServerInstallationTest extends ServerInstallationTest {
    /** Construct a new CommandLineServerInstallationTest
     * @param arg0
     */
    public HttpServerInstallationTest(String arg0) {
        super(new HttpProviderServerInfo(),arg0);
    }

    

}


/* 
$Log: HttpServerInstallationTest.java,v $
Revision 1.2  2004/09/02 11:18:09  jdt
Merges from case 3 branch for SIAP.

Revision 1.1.2.1  2004/08/18 11:34:32  jdt
First integration tests - check that the server is up and OK.  And
test the hello world app.....more to follow

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/