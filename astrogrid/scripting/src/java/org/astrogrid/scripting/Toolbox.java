/*$Id: Toolbox.java,v 1.5 2004/12/07 16:50:33 jdt Exp $
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
import org.astrogrid.scripting.table.StarTableBuilder;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.tree.TreeClient;
import org.astrogrid.store.tree.TreeClientFactory;
import org.astrogrid.store.tree.TreeClientLoginException;
import org.astrogrid.store.tree.TreeClientServiceException;

/** Top-level container object for delegates, helper objects and libraries useful for working with astrogrid.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Nov-2004
 * @script-summary root scripting object
 * @script-doc this is the <em>main</em> scripting object
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
    private final TableHelper tHelper = new TableHelper();
    private final TreeClientFactory treeFactory = new TreeClientFactory();


    
    // STIL
    /** access an object for building star tables from strngs, URLs and ExternalValues 
     * @script-doc-exclude
     * @deprecated
     * */
    public StarTableBuilder getStarTableBuilder() {
        return getTableHelper().getBuilder();
    }
    //@todo somehting to output star tables again.
    /** @script-doc helper object for building, manipulating and writing tables */
    public TableHelper getTableHelper() {
        return tHelper;
    }
    
    // logging
    
    /** access a standard logging object */
    public Log getLogger() {
        return this.logger;
    }
    
    // external referencing library.

    /** access library object that 'knows' about a variety of IO protocols, and can construct {@link ExternalValue} objects to 
     * read / write resources via these protocols.
     * @deprecated
     * @script-doc-exclude
     */
    public ProtocolLibrary getProtocolLibrary() {
        return iHelper.getProtocolLibrary();
    }


    // objject builders
    /** access helper object for building objects 
     * @return object that assists in building {@link User} objects, etc.
     * @script-doc helper object for building account-type objects*/
    public ObjectBuilder getObjectBuilder() {
        return oHelper;
    }

    /** access helper object for working with xml 
     * @return object that assists with constructing and manipulatingn xml.
     * @script-doc helper object for working with io*/
    public XMLHelper getXmlHelper() {
        return xHelper;
    }
    
    /** helper object for working with IO */
    public IOHelper getIoHelper() {
        return iHelper;
    }

// system information and versions.
    
    /** access all the system info and versions we can. 
     * @todo lift code from 'fingerprint' for this
     * @script-doc returns system information for the toolbox
     */
    public String getSystemInfo() {
        return "systeminfo";
    }
    
    /** access the version info for this installations's scripting engine
     * <p>
     * at present returns a list of bugzilla numbers this engine implements.
     * @todo implement to return richer info
     * @script-doc returns version information for the toolbox */
    public String getVersion() {
        return "Iteration 07, scripting-nww-715 scripting-nww-777 scripting-nww-807 scripting-nww-805";
    }

    /** @script-doc accces the system configuration object 
     * @return the system configuration object*/
    public Config getSystemConfig() {
        return SimpleConfig.getSingleton();
    }

    
// workflow inteface    
    
    /** access the workflow manager 
     * @script-doc helper obect for building, saving, submitting and inspecting worflows.
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
     * @script-doc client to query the registry 
     * */
    public RegistryService createRegistryClient() {
        return RegistryDelegateFactory.createQuery();
    }
    
    /** client to administer the registry */ 
    public RegistryAdminService createRegistryAdminClient() {
        return RegistryDelegateFactory.createAdmin();
    }

    //Access to VOSpace
    
    /** create a client to access vospace 
     * @param u object representing the user for whom to create the client for
     * @return a vospace client which has the permissions of user <tt>u</tt>
     * @see #getObjectBuilder() for how to build a <tt>User</tt> object*/
    public ScriptVoSpaceClient createVoSpaceClient(User u) {
        return new ScriptVoSpaceClient(u);
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
          return "Astrogrid Toolbox" + "\n" + this.getVersion();
       }

}


/* 
$Log: Toolbox.java,v $
Revision 1.5  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.4.2.1  2004/12/07 14:47:58  nw
got table manipulation working.

Revision 1.4  2004/12/06 20:03:03  clq2
nww_807a

Revision 1.3.2.2  2004/12/06 18:10:54  nw
backwards compatability fix.

Revision 1.3.2.1  2004/12/06 13:27:47  nw
fixes to improvide integration with external values and starTables.

Revision 1.3  2004/11/30 15:39:56  clq2
scripting-nww-777

Revision 1.1.2.1.2.1  2004/11/26 15:38:16  nw
improved some names, added some missing methods.

Revision 1.1.2.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.
 
*/