/*$Id: VoSpaceClientWorkflowStore.java,v 1.1 2004/04/14 13:45:48 nw Exp $
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
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowStore;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.exolab.castor.xml.CastorException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#readWorkflow(org.astrogrid.community.User, org.astrogrid.store.Ivorn)
     */
    public Workflow readWorkflow(User user, Ivorn locationToReadFrom) throws WorkflowInterfaceException {
        try {
            VoSpaceClient client = new VoSpaceClient(user);
            InputStream is = client.getStream(locationToReadFrom);
            return Workflow.unmarshalWorkflow(new InputStreamReader(is));
         } catch (IOException e) {
             throw new WorkflowInterfaceException("ReadWorkflow",e);
        } catch (CastorException e) {
            throw new WorkflowInterfaceException("ReadWorkflow",e);
        }
        
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#saveWorkflow(org.astrogrid.community.User, org.astrogrid.store.Ivorn, org.astrogrid.workflow.beans.v1.Workflow)
     */
    public void saveWorkflow(User user, Ivorn locationToSaveTo, Workflow workflow) throws WorkflowInterfaceException {
        try {
            VoSpaceClient client = new VoSpaceClient(user);
            OutputStream os = client.putStream(locationToSaveTo);
            workflow.marshal(new OutputStreamWriter(os));
        } catch (IOException e) {
            throw new WorkflowInterfaceException("SaveWorkflow",e);
        } catch (CastorException e) {
            throw new WorkflowInterfaceException("SaveWorkflow",e);
        }
        
    }
}


/* 
$Log: VoSpaceClientWorkflowStore.java,v $
Revision 1.1  2004/04/14 13:45:48  nw
implemented cut down workflow store interface over Ivo Delegate
 
*/