/*$Id: AbstractSiapVotableWorkflow.java,v 1.2 2004/11/30 15:39:32 clq2 Exp $
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

import org.astrogrid.io.Piper;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;

/** abstract class that constructs a workflow with a single step that loads a votable from disk into a JEScript variable.
 * needs to re-write votable to make absolute references to the image files.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2004
 *
 */
public abstract class AbstractSiapVotableWorkflow extends AbstractTestForWorkflow implements INTWFSIAPKeys {

    /** Construct a new AbstractSIAPVotableWorkflow
     * @param applications
     * @param arg0
     */
    public AbstractSiapVotableWorkflow(String[] applications, String arg0) {
        super(applications, arg0);
    }

    public AbstractSiapVotableWorkflow(String arg0) {
        super(new String[]{}, arg0);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        
        URL templateURL = this.getClass().getResource("siap.votable");
        assertNotNull(templateURL);
        Reader template = new InputStreamReader(templateURL.openStream());
        StringWriter sw = new StringWriter();
        Piper.pipe(template,sw);
        String baseURL = templateURL.toString().substring(0, templateURL.toString().lastIndexOf("/"));
        String votable = sw.toString().replaceAll("@BASE@",baseURL);
        votableFile = File.createTempFile("siap-test.votable",null);
        votableFile.createNewFile();
        votableFile.deleteOnExit();
        PrintWriter pw = new PrintWriter(new FileWriter(votableFile));
        pw.print(votable);
        pw.close();
    }
    protected File votableFile;
    
    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws Exception {
        wf.setName(this.getClass().getName());
        buildSourceStep();
    }

    /**
     *  build the step that will act as the source of the votable.
     */
    protected void buildSourceStep() {
        Script sc = new Script();        
        sc.setDescription("load votable into scripting variable of same form as if it'd been created by a previus cea step.");
        sc.setBody(
                "votableResource = astrogrid.protocolLibrary.getExternalValue('" + votableFile.toURI().toString()+"')\n" +
                "votable = astrogrid.ioHelper.getContents(votableResource); \n"+
                "siap = ['IMAGES':votable];\n" + 
                "vars.set('siap',siap);\n" 
                );
        wf.getSequence().addActivity(sc);
    }

}


/* 
$Log: AbstractSiapVotableWorkflow.java,v $
Revision 1.2  2004/11/30 15:39:32  clq2
nww-itn07-684b

Revision 1.1.2.1  2004/11/26 15:46:01  nw
tests for the siap, siap-image-fetch tool, and equivalent script
 
*/