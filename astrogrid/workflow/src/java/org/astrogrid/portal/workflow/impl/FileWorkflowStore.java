/*$Id: FileWorkflowStore.java,v 1.3 2004/04/14 13:02:57 nw Exp $
 * Created on 09-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.io.Piper;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowStore;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.CastorException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/** Implementation of a workflow store based on a local filesystem 
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Mar-2004
 *  @deprecated - old implementation of the workflow store, before interface changes.
 */
public class FileWorkflowStore/* implements WorkflowStore */{
    private static final Log log = LogFactory.getLog(FileWorkflowStore.class);
    /** Construct a new FileWorkflowStore
     * 
     */
    public FileWorkflowStore(File baseDir) {
        log.info ("Creating File-based workflow store at " + baseDir.getAbsolutePath());
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        this.baseDir = baseDir;
    }
    protected final File baseDir;
    private File mkFile(Account acc,String name) {
        StringBuffer filename = new StringBuffer();
        filename.append(acc.getName());
        filename.append('@');
        filename.append(acc.getCommunity());
        filename.append('#');
        filename.append(name);
        return new File(baseDir,URLEncoder.encode(filename.toString()));
    }
    
    private static final String WORKFLOW_SUFFIX = "-workflow.xml";
    private static final String QUERY_SUFFIX = "-query.xml";
    
    private File mkWorkflowFile(Account acc, String name) {
        return mkFile(acc, name + WORKFLOW_SUFFIX);
    }
    private File mkQueryFile(Account acc,String name) {
        return mkFile(acc,name+QUERY_SUFFIX);
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#deleteWorkflow(org.astrogrid.community.beans.v1.Account, java.lang.String)
     */
    public void deleteWorkflow(Account acc, String name) throws WorkflowInterfaceException {
            File f = mkWorkflowFile(acc,name);
            f.delete();              
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#readQuery(org.astrogrid.community.beans.v1.Account, java.lang.String)
     */
    public String readQuery(Account acc, String name) throws WorkflowInterfaceException {
        try {
        File f = mkQueryFile(acc,name);
        Reader reader = new FileReader(f);
        Writer writer = new StringWriter();
        Piper.pipe(reader,writer);
        reader.close();
        writer.close();
        return writer.toString();
        } catch (IOException e) {
            throw new WorkflowInterfaceException(e);
        } 
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#listQueries(org.astrogrid.community.beans.v1.Account)
     */
    public String[] listQueries(Account acc) throws WorkflowInterfaceException {
        final List results = new ArrayList();
        final String prefix = URLEncoder.encode(acc.getName() + "@" + acc.getCommunity() + "#");
        baseDir.list(new FilenameFilter() { // don't care about results. am using it to iterate through directory contents..

            public boolean accept(File dir, String name) {
                if (name.startsWith(prefix) && name.endsWith(QUERY_SUFFIX)) {
                    String originalName = name.substring(prefix.length(),name.indexOf(QUERY_SUFFIX));
                    results.add(URLDecoder.decode(originalName));
                }
                return false;
            }
        });
        return (String[])results.toArray(new String[]{});
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#readWorkflow(org.astrogrid.community.beans.v1.Account, java.lang.String)
     */
    public Workflow readWorkflow(Account acc, String name) throws WorkflowInterfaceException {
        try {
        File f = mkWorkflowFile(acc,name);
        Reader reader = new FileReader(f);
        Workflow wf = Workflow.unmarshalWorkflow(reader);
        reader.close();
        return wf;
        } catch (IOException e) {
            throw new WorkflowInterfaceException(e);
        }  catch (CastorException e) {
            throw new WorkflowInterfaceException(e);
        }
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#listWorkflows(org.astrogrid.community.beans.v1.Account)

     */
    public String[] listWorkflows(Account acc) throws WorkflowInterfaceException {
        final List results = new ArrayList();
        final String prefix = URLEncoder.encode(acc.getName() + "@" + acc.getCommunity() + "#");
        baseDir.list(new FilenameFilter() { // don't care about results. am using it to iterate through directory contents..

            public boolean accept(File dir, String name) {
                if (name.startsWith(prefix) && name.endsWith(WORKFLOW_SUFFIX)) {
                    String originalName = name.substring(prefix.length(),name.indexOf(WORKFLOW_SUFFIX));
                    results.add(URLDecoder.decode(originalName));
                }
                return false;
            }
        });
        return (String[])results.toArray(new String[]{});
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#saveWorkflow(org.astrogrid.community.beans.v1.Account, org.astrogrid.workflow.beans.v1.Workflow)
     */
    public void saveWorkflow(Account acc, Workflow workflow) throws WorkflowInterfaceException {
        try {
            File f = mkWorkflowFile(acc,workflow.getName());
            Writer w = new FileWriter(f);
            workflow.marshal(w);
            w.close();
        } catch (IOException e) {
            throw new WorkflowInterfaceException(e);
        } catch (CastorException e) {
            throw new WorkflowInterfaceException(e);
        }
    }
}


/* 
$Log: FileWorkflowStore.java,v $
Revision 1.3  2004/04/14 13:02:57  nw
cut down workflow store interface. now to implement it.

Revision 1.2  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.2  2004/03/11 13:36:10  nw
added implementations for the workflow interfaces

Revision 1.1.2.1  2004/03/09 17:41:59  nw
created a bunch of implementations,
 
*/