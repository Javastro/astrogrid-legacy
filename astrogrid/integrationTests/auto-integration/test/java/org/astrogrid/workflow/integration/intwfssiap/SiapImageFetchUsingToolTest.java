/*$Id: SiapImageFetchUsingToolTest.java,v 1.2 2004/11/30 15:39:32 clq2 Exp $
 * Created on 26-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration.intwfssiap;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.io.Piper;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/** fetch images resulting from a siap query, by calling the siap image fetch tool.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2004
 *
 */
public class SiapImageFetchUsingToolTest extends AbstractSiapVotableWorkflow {
    /** Construct a new ParseDownloadSIAPUsingToolTest
     * @param arg0
     */
    public SiapImageFetchUsingToolTest(String arg0) {
        super(new String[]{SIAP_IMAGE_FETCH},arg0);
    }
    
    public SiapImageFetchUsingToolTest(String[] applications, String arg0) {
        super(applications, arg0);
    }

    protected void buildWorkflow() throws Exception {
        super.buildWorkflow();
        wf.setName(this.getClass().getName());
        ApplicationDescription desc = reg.getDescriptionFor(SIAP_IMAGE_FETCH);
        Tool fetchTool = desc.createToolFromDefaultInterface();
        // populate tool.
        ParameterValue table = (ParameterValue)fetchTool.findXPathValue("input/parameter[name='table']");
        assertNotNull(table);
        table.setIndirect(false);
        table.setValue("${siap.IMAGES}");
        
        ParameterValue base = (ParameterValue)fetchTool.findXPathValue("input/parameter[name='baseIvorn']");
        assertNotNull(base);
        base.setIndirect(false);
        base.setValue("ivo://" + super.AUTHORITYID + "/" + super.USERNAME + "#" + super.USERNAME);

        
        // add to the workflow.
        Step s = new Step();
        s.setDescription("Image fetch tool");
        s.setName(SIAP_IMAGE_FETCH);
        s.setResultVar("fetched");
        s.setTool(fetchTool);
        wf.getSequence().addActivity(s);
        
    }

   

}


/* 
$Log: SiapImageFetchUsingToolTest.java,v $
Revision 1.2  2004/11/30 15:39:32  clq2
nww-itn07-684b

Revision 1.1.2.1  2004/11/26 15:46:01  nw
tests for the siap, siap-image-fetch tool, and equivalent script
 
*/