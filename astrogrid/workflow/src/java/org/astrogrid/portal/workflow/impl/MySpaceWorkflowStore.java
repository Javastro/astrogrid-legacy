/*$Id: MySpaceWorkflowStore.java,v 1.3 2004/04/14 13:02:57 nw Exp $
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
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowStore;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/** Myspace-based implementation of a workflow store.
 * <p>
 * here be dragons...
 * <p>
 * This class assumes that all operaitons are made to myspace resources owned by
 * the calling user - as the Account object parameter is used for identification, and authentication (ish) - we know myspace doesn't authenticate at the moment.
 * any authentication details should be pulled out of soap headers in the future anyhow - so this will do for now.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Mar-2004
 * @deprecated old implementation of the previous workflow store interface.
 */
public class MySpaceWorkflowStore/* implements WorkflowStore */{
    private static final Log log = LogFactory.getLog(MySpaceWorkflowStore.class);
    /** Construct a new MySpaceWorkflowStore
     * 
     */
    public MySpaceWorkflowStore(URL endpoint) throws IOException {
        log.info("Creating myspace-backed workflow store, using service endpoint " + endpoint.toString());
        client = MySpaceDelegateFactory.createDelegate(endpoint.toString());
        assert client != null;
    }
    protected final MySpaceClient client;
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#deleteWorkflow(org.astrogrid.community.beans.v1.Account, java.lang.String)
     */
    public void deleteWorkflow(Account acc, String name) throws WorkflowInterfaceException {
        try {
            
            client.deleteDataHolding( acc.getName(), acc.getCommunity()
                                     , null//CommunityMessage.getGroup( communitySnippet )
                                     , mkWorkflowPath(acc,name) ) ;             
        } catch (Exception e) {
            log.error("DeleteWorkflow: myspace failed");
            throw new WorkflowInterfaceException("myspace failed",e);
        }
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#readQuery(org.astrogrid.community.beans.v1.Account, java.lang.String)
     */
    public String readQuery(Account acc, String name) throws WorkflowInterfaceException {
       try {
             
           String result = client.getDataHolding( acc.getName(),acc.getCommunity()
                                              , null //CommunityMessage.getGroup( communitySnippet )
                                              , mkQueryPath(acc,name) ) ; 
          if (result == null) {
              log.warn("myspace returning null results");
          }                                                                      
          return result;                         
       } catch (Exception e) {
           log.error("ReadQuery: myspace failed",e);
           throw new WorkflowInterfaceException("myspace failed",e);
       }
    }

    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#readWorkflow(org.astrogrid.community.beans.v1.Account, java.lang.String)
     */
    public Workflow readWorkflow(Account acc, String name) throws WorkflowInterfaceException {
        String result = null;
        try {             
             result = client.getDataHolding( acc.getName(),acc.getCommunity()
                                                , null //CommunityMessage.getGroup( communitySnippet )
                                                , mkWorkflowPath(acc,name) ) ;                                                                                     
         } catch (Exception e) {
             log.error("ReadWorkflow: myspace failed",e);
             throw new WorkflowInterfaceException("myspace failed",e);
         }
         
         if (result == null || result.trim().length() == 0) {
             log.error("myspace returned an empty file");
             throw new WorkflowInterfaceException("Myspace returned an empty file");
         }
         try {
            StringReader reader = new StringReader(result);
            Workflow wf = Workflow.unmarshalWorkflow(reader);
            reader.close();
            return wf;
         } catch (Exception e) {
             log.error("unable to build workflow object model from file read in from myspace",e);
             log.error(result);
             throw new WorkflowInterfaceException("Unable to marshall file contents from myspace",e);
         }
         
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#listQueries(org.astrogrid.community.beans.v1.Account)
     */
    public String[] listQueries(Account acc) throws WorkflowInterfaceException {
        return myspaceList(acc,mkQueryPath(acc,"*"));
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#listWorkflows(org.astrogrid.community.beans.v1.Account)
     */
    public String[] listWorkflows(Account acc) throws WorkflowInterfaceException {
        return myspaceList(acc,mkWorkflowPath(acc,"*"));
    }
    
   private String[] myspaceList(Account acc, String filePattern) throws WorkflowInterfaceException {        
    // JBL Note (Jan 2004): listDataHoldings returns a vector of vectors...
    // one for each list in the systems that the account participates in!
    // For now, deal with an empty vector. Then if not empty, get the
    // first (and only) vector held within the overall vector and retrieve
    // an iterator for that. This will represent the list from the one and only 
    // (at present) system that this account belongs to. 
        List matches = null;
        try {
            matches = client.listDataHoldings( acc.getName(),acc.getCommunity()
                                         , null //CommunityMessage.getGroup( communitySnippet )
                                         , filePattern) ;
        } catch (Exception e) {
            log.error("myspaceList: failed",e);
            throw new WorkflowInterfaceException(e);
        }                                 
    
        if (matches == null) {
            matches = new ArrayList();
        }
        List results = new ArrayList();
        for (Iterator i = matches.iterator(); i.hasNext(); ) {
            Object o = i.next();
            // future proofed, for the day when this returns a flat list.
            if (o instanceof Collection) { 
                for (Iterator j = ((Collection)o).iterator(); j.hasNext(); ) {
                    results.add(j.next().toString());
                }
            } else {
                results.add(o.toString());
            }
        }
        return (String[])results.toArray(new String[]{});
   }
                 
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowStore#saveWorkflow(org.astrogrid.community.beans.v1.Account, org.astrogrid.workflow.beans.v1.Workflow)
     */
    public void saveWorkflow(Account acc, Workflow workflow) throws WorkflowInterfaceException {
        StringWriter content = new StringWriter();
        try {
            workflow.marshal(content);
            content.close();
        } catch (Exception e) {
            log.error("Failed to marshall workflow to xml string",e);
            throw new WorkflowInterfaceException("Failed to marshall workflow to xml",e);
        }
        
         boolean ok = false;
         try { 
                ok = client.saveDataHolding(acc.getName(), acc.getCommunity() 
                                              , null //  CommunityMessage.getGroup( communitySnippet)
                                              , mkWorkflowPath(acc,workflow)
                                              , content.toString()
                                              , FILE_CATEGORY
                                              , MySpaceClient.OVERWRITE ) ;
        } catch (Exception e) {// can't be any more precise - myspace throws exception..
            log.error("Failed to store workflow in myspace",e);
            throw new WorkflowInterfaceException("Myspace failure:",e);
        }
        if (!ok) {
            throw new WorkflowInterfaceException("Save workflow failed wth false return code");
        }   
    }
    public static final String FILE_CATEGORY = "workflow";
    
    /** construct a path for this workflow document to be stored at 
     * @todo copied path structure from previous {@link org.astrogrid.portal.workflow.design.Workflow} class. don't know if it is still relevant*/
    protected String mkWorkflowPath(Account account, Workflow workflow) {
        return mkWorkflowPath(account,workflow.getName());
    }
    /** construct a path for this workflow document to be stored at 
     * @todo copied path structure from previous {@link org.astrogrid.portal.workflow.design.Workflow} class. don't know if it is still relevant*/    
    protected String mkWorkflowPath(Account account, String name) {
         return "/" + account.getName() + '@' + account.getCommunity() +  "/serv1/workflow/" + name;
     }
    
    protected String mkQueryPath(Account acc,String name) {
        return "/" + acc.getName() + '@' + acc.getCommunity() + "/serv1/query/" + name;
    }
    
}


/* 
$Log: MySpaceWorkflowStore.java,v $
Revision 1.3  2004/04/14 13:02:57  nw
cut down workflow store interface. now to implement it.

Revision 1.2  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.2  2004/03/11 13:36:10  nw
added implementations for the workflow interfaces

Revision 1.1.2.1  2004/03/09 17:41:59  nw
created a bunch of implementations,
 
*/