/*$Id: VoSpaceClientWorkflowStore.java,v 1.2 2005/03/13 07:13:39 clq2 Exp $
 * Created on 14-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;

import org.astrogrid.community.User;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowStore;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.exolab.castor.xml.CastorException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;

/** Implementation of a workflow store
 * @author Noel Winstanley nw@jb.man.ac.uk 14-Apr-2004
 *
 */
public class VoSpaceClientWorkflowStore implements WorkflowStore {
    /** Construct a new VoSpaceClientWorkflowStore
     * 
     */
    public VoSpaceClientWorkflowStore() {
        super();
        this.factory = new FileManagerClientFactory(NO_PREFETCH_POLICY);        
    }
    protected final FileManagerClientFactory factory;
    /** there's no point prefetching stuff - as we're just going to be reading / writing to a single ivorn location */
    private static final BundlePreferences NO_PREFETCH_POLICY = new BundlePreferences();
    static {
        NO_PREFETCH_POLICY.setFetchParents(false);
        NO_PREFETCH_POLICY.setMaxExtraNodes(new Integer(0));
        NO_PREFETCH_POLICY.setPrefetchDepth(new Integer(0));
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#readWorkflow(org.astrogrid.community.User, org.astrogrid.store.Ivorn)
     */
    public Workflow readWorkflow(User user, Ivorn locationToReadFrom) throws WorkflowInterfaceException {
        try {
            // @todo can't work out how to convert from User to ivorn - so will login anonymously for now, and hope this suffices. 
            FileManagerClient client = factory.login();
            //client.
           FileManagerNode target = client.node(locationToReadFrom);
           InputStream is= target.readContent();
            return Workflow.unmarshalWorkflow(new InputStreamReader(is));
         } catch (Exception e) {
             throw new WorkflowInterfaceException("ReadWorkflow",e);
        } 
        
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#readWorkflow(org.astrogrid.store.Ivorn, java.lang.String, org.astrogrid.store.Ivorn)
     */
    public Workflow readWorkflow(Ivorn userId, String password, Ivorn locationToReadFrom) throws WorkflowInterfaceException {
        try {
            FileManagerClient client = factory.login(userId,password);
            FileManagerNode target = client.node(locationToReadFrom);
            InputStream is= target.readContent();
            return Workflow.unmarshalWorkflow(new InputStreamReader(is));
         } catch (Exception e) {
             throw new WorkflowInterfaceException("ReadWorkflow",e);
        }             
    }    
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#saveWorkflow(org.astrogrid.community.User, org.astrogrid.store.Ivorn, org.astrogrid.workflow.beans.v1.Workflow)
     */
    public void saveWorkflow(User user, Ivorn locationToSaveTo, Workflow workflow) throws WorkflowInterfaceException {
        OutputStream os = null;
        try {
            // @todo can't work out how to convert from User to ivorn - so will login anonymously for now, and hope this suffices. 
             FileManagerClient client = factory.login();
             FileManagerNode target;             
             if (client.exists(locationToSaveTo) != null) {
                 target = client.node(locationToSaveTo);
             } else { // create it
                 target = client.createFile(locationToSaveTo);
             }             
            os = target.writeContent();
            workflow.marshal(new OutputStreamWriter(os));
        } catch (Exception e) {
            throw new WorkflowInterfaceException("SaveWorkflow",e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // ignored
                }
            }
        }
        
    }

    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#saveWorkflow(org.astrogrid.store.Ivorn, java.lang.String, org.astrogrid.store.Ivorn, org.astrogrid.workflow.beans.v1.Workflow)
     */
    public void saveWorkflow(Ivorn userId, String password, Ivorn locationToSaveTo, Workflow workflow) throws WorkflowInterfaceException {
        OutputStream os = null;
        try {
             FileManagerClient client = factory.login(userId,password);
             FileManagerNode target;             
             if (client.exists(locationToSaveTo) != null) {
                 target = client.node(locationToSaveTo);
             } else { // create it
                 target = client.createFile(locationToSaveTo);
             }             
            os = target.writeContent();
            workflow.marshal(new OutputStreamWriter(os));
        } catch (Exception e) {
            throw new WorkflowInterfaceException("SaveWorkflow",e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // ignored
                }
            }
        }
        
    }
}


/* 
$Log: VoSpaceClientWorkflowStore.java,v $
Revision 1.2  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.1.142.1  2005/03/11 12:39:30  nw
replaced vospaceClient with FileManagerClient

Revision 1.1  2004/04/14 13:45:48  nw
implemented cut down workflow store interface over Ivo Delegate
 
*/