/*$Id: CommandLineServerInstallationTest.java,v 1.4 2004/11/24 19:49:22 clq2 Exp $
 * Created on 29-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.commandline;

import org.astrogrid.applications.integration.ServerInstallationTest;

/** installation test for commandline cea server.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jun-2004
 *@see org.astrogrid.applications.integration.ServerInstallationTest
 */
public class CommandLineServerInstallationTest extends ServerInstallationTest {
    /** Construct a new CommandLineServerInstallationTest
     * @param arg0
     */
    public CommandLineServerInstallationTest(String arg0) {
        super(new CommandLineProviderServerInfo(),arg0);
    }

    

}


/* 
$Log: CommandLineServerInstallationTest.java,v $
Revision 1.4  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.1.104.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/