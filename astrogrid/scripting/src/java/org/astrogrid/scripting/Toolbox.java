/*$Id: Toolbox.java,v 1.2 2004/11/22 18:26:54 clq2 Exp $
 * Created on 19-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibraryFactory;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.io.Piper;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.portal.workflow.intf.WorkflowManagerFactory;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.tree.TreeClient;
import org.astrogrid.store.tree.TreeClientFactory;
import org.astrogrid.store.tree.TreeClientLoginException;
import org.astrogrid.store.tree.TreeClientServiceException;

/** Top-level container object for delegates, helper objects and libraries useful for working with astrogrid.
 * 
 * @todo add hook into something for writing out star tables back into myspace - wait for mark to extend the interfaces to allow this.
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Nov-2004
 *
 */
public class Toolbox {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog("TOOLBOX");

    /** Construct a new Toolbox
     * 
     */
    public Toolbox() {
        super();
    }

    private final WorkflowManagerFactory workflowManagerFactory = new WorkflowManagerFactory();
    private final ObjectBuilder oHelper = new ObjectBuilder();
    private final XMLHelper xHelper = new XMLHelper();
    private final IOHelper iHelper = new IOHelper();
    private final StarTableBuilder tBulider = new StarTableBuilder();
    private final TreeClientFactory treeFactory = new TreeClientFactory();
    private final ProtocolLibrary protocolLib = (new DefaultProtocolLibraryFactory()).createLibrary();


    
    // STIL
    /** access an object for building star tables from strngs, URLs and ExternalValues */
    public StarTableBuilder getStarTableBuilder() {
        return tBulider;
    }
    //@todo somehting to output star tables again.

    
    // logging
    
    /** access a standard logging object */
    public Log getLogger() {
        return this.logger;
    }
    
    // external referencing library.

    /** access library object that 'knows' about a variety of IO protocols, and can construct {@link ExternalValue} objects to 
     * read / write resources via these protocols.
     */
    public ProtocolLibrary getProtocolLibrary() {
        return protocolLib;
    }

    // objject builders
    /** access helper object for building objects 
     * @return object that assists in building {@link User} objects, etc.*/
    public ObjectBuilder getObjectBuilder() {
        return oHelper;
    }

    /** access helper object for working with xml 
     * @return object that assists with constructing and manipulatingn xml.*/
    public XMLHelper getXMLHelper() {
        return xHelper;
    }
    
    /** access helper object for working with IO */
    public IOHelper getIOHelper() {
        return iHelper;
    }

// system information and versions.
    
    /** access all the system info and versions we can. 
     * @todo lift code from 'fingerprint' for this
     */
    public String getSystemInfo() {
        return "systeminfo";
    }
    
    /** access the version info for this installation 
     * @todo implement to return richer info*/
    public String getVersion() {
        return "Iteration 07";
    }

    /** accces the system configuration object 
     * @return the system configuration object*/
    public Config getSystemConfig() {
        return SimpleConfig.getSingleton();
    }

    
// workflow inteface    
    
    /** access the workflow manager 
     * @return interface to system for building, saving, submitting and inspecting worflows.
     * @throws WorkflowInterfaceException*/
    public WorkflowManager getWorkflowManager() throws WorkflowInterfaceException {
        return workflowManagerFactory.getManager();
    }

  // access to cea services
    //@todo add in memory cea client runner,
    
    /** create a CEA client connected to the specified service endpoint */
    public CommonExecutionConnectorClient createCeaClient(String endpoint) {
        return DelegateFactory.createDelegate(endpoint);
    }
    
    
    // access to registry.
    
    /** create client to query default registry 
     * @returna registry client connected to the default registry location
     * */
    public RegistryService createRegistryClient() {
        return RegistryDelegateFactory.createQuery();
    }
    
    /** create client to admin default registry */ 
    public RegistryAdminService createRegistryAdminClient() {
        return RegistryDelegateFactory.createAdmin();
    }

    //Access to VOSpace
    
    /** create a client to access vospace 
     * @param u object representing the user for whom to create the client for
     * @return a vospace client which has the permissions of user <tt>u</tt>
     * @see #getObjectBuilder() for how to build a <tt>User</tt> object*/
    public VoSpaceClient createVoSpaceClient(User u) {
        return new VoSpaceClient(u);
    }
    
    /** create a client to work with the vospace tree-model 
     * 
     * @param acc the ivorn of the account to connect to
     * @param password the password for this account
     * @return a logged-in treeclient for this account
     * @throws TreeClientLoginException if login failed
     * @throws TreeClientServiceException if communication failed
     * @see #getObjectBuilder() to access a helper object for constructing an ivorn.
     * @see ObjectBuilder#createLocalUserIvorn(String) for simplest way to create the ivorn.
     */
    public TreeClient createTreeClient(Ivorn acc, String password) throws TreeClientLoginException, TreeClientServiceException {        
        TreeClient tc = treeFactory.createClient();
        tc.login(acc,password);
        return tc;
    }
    
    

    public String toString() {
          return "Astrogrid Toolbox";
       }

}


/* 
$Log: Toolbox.java,v $
Revision 1.2  2004/11/22 18:26:54  clq2
scripting-nww-715

Revision 1.1.2.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.
 
*/