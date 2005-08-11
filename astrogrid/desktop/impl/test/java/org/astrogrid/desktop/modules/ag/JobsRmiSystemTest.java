/*$Id: JobsRmiSystemTest.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 09-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.framework.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/** execrcise jobs, via rmi interface
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Aug-2005
 *
 */
public class JobsRmiSystemTest extends JobsSystemTest {

    protected ACR getACR() throws Exception{
        return (new Finder()).find();
    }
    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(JobsRmiSystemTest.class),true); // login.
    }    
}


/* 
$Log: JobsRmiSystemTest.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/09 17:33:07  nw
finished system tests for ag components.
 
*/