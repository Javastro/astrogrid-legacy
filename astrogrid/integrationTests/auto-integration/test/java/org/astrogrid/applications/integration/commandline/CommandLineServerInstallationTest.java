/*$Id: CommandLineServerInstallationTest.java,v 1.1 2004/07/01 11:43:33 nw Exp $
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
 *
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
Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/