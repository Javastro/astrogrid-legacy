/*$Id: SiapImageFetchUsingScriptTest.java,v 1.2 2004/11/30 15:39:32 clq2 Exp $
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

import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Workflow;

/** fetch images from siap query, by using an embedded script
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2004
 *
 */
public class SiapImageFetchUsingScriptTest extends
        AbstractSiapVotableWorkflow {

    /** Construct a new ParseDownloadSIAPUsingScriptTest
     * @param applications
     * @param arg0
     */
    public SiapImageFetchUsingScriptTest(String[] applications, String arg0) {
        super(applications, arg0);
    }

    /** Construct a new ParseDownloadSIAPUsingScriptTest
     * @param arg0
     */
    public SiapImageFetchUsingScriptTest(String arg0) {
        super(arg0);
    }
    
    

    protected void buildWorkflow() throws Exception {
        super.buildWorkflow();

        // add in a new script step
        Script sc = new Script();
        sc.setDescription("Extract URLS from votable, save to myspace");
        sc.setBody(
                "table = astrogrid.starTableBuilder.makeStarTableFromString( siap.IMAGES, 'votable' )\n" +
                "col = ( 0 ... table.columnCount ).find{ table.getColumnInfo( it ).getUCD() == 'VOX:Image_AccessReference' }\n" +
               "urls = ( 0L ... table.rowCount ).collect{ table.getCell( it, col ) }\n" +
               "jes.info(urls)\n" +
               "vospace = astrogrid.createVoSpaceClient(user)\n" +
               "root = astrogrid.objectBuilder.newIvorn(homeIvorn,astrogrid.ioHelper.dateTimeStamp())\n" +
               "vospace.newFolder(root)\n" +
               "count = 0\n" +
                "urls.each{target = astrogrid.objectBuilder.newIvorn(root, (++count) + '.fits'); vospace.putUrl(it,target,false)}\n"
                );
        wf.getSequence().addActivity(sc);
    }
    /** @todo should check files are in myspace now */
    public void checkExecutionResults(Workflow result) throws Exception {
        super.checkExecutionResults(result);
        // for simplicity's sake, assume that if the workflow completes, all is well.
    }
}


/* 
$Log: SiapImageFetchUsingScriptTest.java,v $
Revision 1.2  2004/11/30 15:39:32  clq2
nww-itn07-684b

Revision 1.1.2.1  2004/11/26 15:46:01  nw
tests for the siap, siap-image-fetch tool, and equivalent script
 
*/