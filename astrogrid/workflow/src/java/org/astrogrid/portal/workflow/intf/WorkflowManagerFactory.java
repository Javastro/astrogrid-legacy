/*$Id: WorkflowManagerFactory.java,v 1.9 2004/04/14 13:45:48 nw Exp $
 * Created on 24-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

import org.astrogrid.config.Config;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.portal.workflow.impl.BasicWorkflowBuilder;
import org.astrogrid.portal.workflow.impl.FileApplicationRegistry;
import org.astrogrid.portal.workflow.impl.FileWorkflowStore;
import org.astrogrid.portal.workflow.impl.JesJobExecutionService;
import org.astrogrid.portal.workflow.impl.MySpaceWorkflowStore;
import org.astrogrid.portal.workflow.impl.RegistryApplicationRegistry;
import org.astrogrid.portal.workflow.impl.VoSpaceClientWorkflowStore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/** Factory that creates a new workflow manager
 * <p>
 * After creation, a factory instance maintains a single instance of the manager
 * <p>
 * The composition of the created workflow manager depends on the keys set in the {@link org.astrogrid.config.Config} system. Refer to the documentation
 * for this package for how to set keys.
 * <p>
 * The keys that control the configuration of the workflow manager are all defined in this class:
 * <h2>Workflow Store</h2>
 * <ul>
 * <li>{@link #WORKFLOW_STORE_KEY}
 * <li>{@link #WORKFLOW_MYSPACE_STORE_ENDPOINT_KEY}
 * <li>{@link #WORKFLOW_FILE_STORE_BASEDIR_KEY}
 * </ul>
 * <h2>Application Registry</h2>
 * <li>
 * <li>{@link #WORKFLOW_APPLIST_KEY}
 * <li>{@link #WORKFLOW_APPLIST_REGISTRY_ENDPOINT_KEY}
 * <li>{@link #WORKFLOW_APPLIST_XML_URL_KEY}
 * </ul>
 * <h2>Job Execution Service</h2>
 * <ul>
 * <li>{@link #WORKFLOW_JES_ENDPOINT_KEY}
 * 
 * </ul>
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public class WorkflowManagerFactory {
    private static Log log = LogFactory.getLog(WorkflowManagerFactory.class);
    /** Construct a new WorkflowManagerFactory
     * will use default configuration object from {@link SimpleConfig}
     */
    public WorkflowManagerFactory() {
        this(SimpleConfig.getSingleton());
    }
    
    /** construct a workflow manager factory, which will use the passed in configuration object */
    public WorkflowManagerFactory(Config conf) {
        this.conf = conf;
    }
    private final Config conf;
    
    /** access the manager instance */
    public synchronized WorkflowManager getManager() throws WorkflowInterfaceException{
        if (theInstance == null) {
           theInstance = createManager();           
        }
        assert theInstance != null;
        return theInstance;
    }
    
    private WorkflowManager theInstance;
    

    /** key to look under to determin implmenetation of workflow applicationRegistry to use
     * <p>possible values: <tt>xml</tt> | <tt>registry</tt> | <tt><i>java.class.name</i></tt>
     * <br> default value: <tt>registry</tt>
     */
    public static final String WORKFLOW_APPLIST_KEY = "workflow.applist";
    /** key to look under to determine url of xml file to use for xml-based application registry 
     * <p> default value: <tt>/application-list.xml</tt> on classpath*/
    public static final String WORKFLOW_APPLIST_XML_URL_KEY = "workflow.applist.xml.url";
    /** default location to look for xml file for xml-based application registry */
    public static final String WORKFLOW_APPLIST_XML_DEFAULT = "/application-list.xml";
    private static final URL WORKFLOW_APPLIST_XML_DEFAULT_URL = WorkflowManagerFactory.class.getResource(WORKFLOW_APPLIST_XML_DEFAULT);
    /** key to look under to determine endpoint of registry-based application registry */
    public static final String WORKFLOW_APPLIST_REGISTRY_ENDPOINT_KEY = "workflow.applist.registry.endpoint";
    /** key to look under to determine endpoint of jes controller service */
    public static final String WORKFLOW_JES_ENDPOINT_KEY = "workflow.jes.endpoint";
    
    /** create workflow manager, based on properties in config */
    private WorkflowManager createManager() throws WorkflowInterfaceException{
        try {
        WorkflowBuilder builder = new BasicWorkflowBuilder();
        WorkflowStore store = buildStore();
        ApplicationRegistry reg = buildAppReg();
        JobExecutionService jes = buildJes();
        return new WorkflowManager(builder,store,reg,jes);
        } catch (Exception e) {
            log.fatal("Failed to create Workflow Manager",e);
            throw new WorkflowInterfaceException(e);       
        }        
    }

    /**
     * @return
     */
    private JobExecutionService buildJes() {
        try {
            String url = conf.getString(WORKFLOW_JES_ENDPOINT_KEY);
            log.info(WORKFLOW_JES_ENDPOINT_KEY + " := " + url);
             return new JesJobExecutionService(url);
        } catch (PropertyNotFoundException e) {
            log.info("no url found under " + WORKFLOW_JES_ENDPOINT_KEY + ", trying letting delegate configure itself");
            return new JesJobExecutionService();
        }
    }

    /**
     * @return
     */
    private ApplicationRegistry buildAppReg() throws Exception {
        try {
            String option = conf.getString(WORKFLOW_APPLIST_KEY).trim();            
            log.info(WORKFLOW_APPLIST_KEY + " := " + option);
            if ("xml".equalsIgnoreCase(option)) {
                log.info("Creating xml-file backed application list");
                URL applicationListDocument= conf.getUrl(WORKFLOW_APPLIST_XML_URL_KEY,WORKFLOW_APPLIST_XML_DEFAULT_URL);
                log.info(WORKFLOW_APPLIST_XML_URL_KEY + " := " + applicationListDocument.toString());
                return new FileApplicationRegistry(applicationListDocument);
            } else if ("registry".equalsIgnoreCase(option)) {
                return buildDefaultAppReg();
            } else { // assume its a class name;
                log.info("option unrecognized - assuming its a java classname. attempting to instantiate..");
                 Class clazz = Class.forName(option);
                 return (ApplicationRegistry)clazz.newInstance();
            }
        } catch (PropertyNotFoundException e) {
            log.warn("no value for " + WORKFLOW_APPLIST_KEY + " falling back to default");
            return buildDefaultAppReg();
        }
    }
    /** build default, astorgrid-registry based implementation */
    private ApplicationRegistry buildDefaultAppReg() {
        log.info("Creating registry backed application list");
        try {
            URL endpoint = conf.getUrl(WORKFLOW_APPLIST_REGISTRY_ENDPOINT_KEY);
            log.info(WORKFLOW_APPLIST_REGISTRY_ENDPOINT_KEY + " := " + endpoint.toString());
            return new RegistryApplicationRegistry(endpoint);
        }catch (PropertyNotFoundException e) {
            log.info("No registry endpoint specified, attempting to let registry delegate configure itself");
            return new RegistryApplicationRegistry();
        }
    }

    /**
     */
    private WorkflowStore buildStore() throws Exception {
        return new VoSpaceClientWorkflowStore();
    }

}


/* 
$Log: WorkflowManagerFactory.java,v $
Revision 1.9  2004/04/14 13:45:48  nw
implemented cut down workflow store interface over Ivo Delegate

Revision 1.8  2004/04/14 13:05:20  nw
cut down workflow store interface. now to implement it.

Revision 1.7  2004/04/14 13:02:57  nw
cut down workflow store interface. now to implement it.

Revision 1.6  2004/03/15 17:01:01  nw
loosened type of endpoint for JesJobExecutionService from URL to String -
allows the dummy urn:test to be passed in.

Revision 1.5  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.4.4.1  2004/03/11 13:36:46  nw
tidied up interfaces, documented

Revision 1.4  2004/03/03 11:15:23  nw
tarted up javadocs, reviewed types

Revision 1.3  2004/03/01 15:03:38  nw
simplified by removing facade - will expose object model directly

Revision 1.2  2004/02/25 10:57:43  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.1  2004/02/24 15:35:46  nw
extracted public interface from each implementation class.
altered types to reference interface rather than implementation whever possible.
added factory and manager facade at the front
 
*/