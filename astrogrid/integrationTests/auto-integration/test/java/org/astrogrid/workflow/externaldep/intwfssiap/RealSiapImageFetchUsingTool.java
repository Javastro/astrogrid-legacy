/*$Id: RealSiapImageFetchUsingTool.java,v 1.2 2004/11/30 15:39:32 clq2 Exp $
 * Created on 26-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.externaldep.intwfssiap;

import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.integration.intwfssiap.SiapImageFetchUsingToolTest;

/** query a votable from the real siap service, parse and retreive images using the siap-image-fetch tool
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2004
 *
 */
public class RealSiapImageFetchUsingTool extends
        SiapImageFetchUsingToolTest {



    /** Construct a new RealSiapImageFetchUsingTool
     * @param arg0
     */
    public RealSiapImageFetchUsingTool(String arg0) {
        super(new String[]{INTWFS_SIAP},arg0);
    }

    protected void buildSourceStep() {
        try {
            Step s = SimpleINTWFSSiapWorkflowTest.buildSiapTool(reg);
            wf.getSequence().addActivity(s);     
        } catch (WorkflowInterfaceException e) {
            fail(e.getMessage());
        }
   
    }
}


/* 
$Log: RealSiapImageFetchUsingTool.java,v $
Revision 1.2  2004/11/30 15:39:32  clq2
nww-itn07-684b

Revision 1.1.2.1  2004/11/26 15:46:01  nw
tests for the siap, siap-image-fetch tool, and equivalent script
 
*/