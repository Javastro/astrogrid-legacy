/*$Id: ProductionComponentManager.java,v 1.8 2004/03/18 10:54:22 nw Exp $
 * Created on 07-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component.production;
import org.astrogrid.config.Config;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.jes.component.ComponentManagerException;
import org.astrogrid.jes.component.EmptyComponentManager;
import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.component.descriptor.SimpleComponentDescriptor;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.CastorBeanFacade;
import org.astrogrid.jes.impl.workflow.DBJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.FileJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.SqlCommands;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerDispatcher;
import org.astrogrid.jes.jobscheduler.impl.SchedulerTaskQueueDecorator;
import org.astrogrid.jes.jobscheduler.locator.RegistryToolLocator;
import org.astrogrid.jes.jobscheduler.locator.XMLFileLocator;
import org.astrogrid.jes.jobscheduler.policy.FlowPolicy;
import org.astrogrid.jes.jobscheduler.policy.JoinPolicy;
import org.astrogrid.jes.jobscheduler.policy.LinearPolicy;

import org.picocontainer.Parameter;
import org.picocontainer.defaults.ComponentParameter;

import javax.sql.DataSource;
/** Implementation of Component Manager used in production / deployed jes system.
 * instantiates production set of components, configured from a Config class.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public final class ProductionComponentManager extends EmptyComponentManager {
    /** construct a component manager, that gets configuration from {@link SimpleConfig}*/
    public ProductionComponentManager() throws ComponentManagerException {
        this(SimpleConfig.getSingleton());
    }
    /** Construct a new ProductionComponentManager
     * @param conf confugration object to use to look up keys.
     */
    public ProductionComponentManager(Config conf) throws ComponentManagerException{
        super();
        this.conf = conf;
        try {
         pico.registerComponentInstance("jes-meta",JES_META);
         pico.registerComponentInstance(Config.class,conf); 
         pico.registerComponentImplementation(JobScheduler.class,SchedulerTaskQueueDecorator.class,
            new Parameter[]{
                new ComponentParameter(SCHEDULER_ENGINE)
            });
         pico.registerComponentImplementation(SCHEDULER_ENGINE,org.astrogrid.jes.jobscheduler.impl.SchedulerImpl.class);
         registerPolicy();
         
         pico.registerComponentImplementation(Dispatcher.class,ApplicationControllerDispatcher.class);
         pico.registerComponentImplementation(ApplicationControllerDispatcher.MonitorEndpoint.class,MonitorEndpointFromConfig.class);
         
         registerLocator();
            
         pico.registerComponentImplementation(BeanFacade.class,CastorBeanFacade.class);
         registerJobFactory();
         
         pico.registerComponentImplementation(JobMonitor.class,org.astrogrid.jes.jobmonitor.JobMonitor.class);
         pico.registerComponentImplementation(JobController.class,org.astrogrid.jes.jobcontroller.JobController.class);       
         } catch (Exception e) {
             log.fatal("Could not create component manager",e);
             throw new ComponentManagerException(e);
        }
    }
    
    /**Description-only component - metadata for entire system. 
     * 
     * @todo add config tests for a production system in here */
    private static final ComponentDescriptor JES_META = new SimpleComponentDescriptor(
        "Production Job Execution System",
        "job execution system"
    );
    /** key to look in config for implementation of job factory to use
     * <p>
     * possible values = <tt>db</tt> | <tt>file</tt> | <tt><i>java.class.name</i></tt> */
    public static final String JOB_FACTORY_KEY = "jes.jobfactory";
    /** key to look in config for a datasource object */
    public static final String DB_JOB_FACTORY_DATASOURCE_KEY = "jdbc/jes-datasource";
    private static final ComponentDescriptor JOBFACTORY_META_DATA 
        = new SimpleComponentDescriptor("Job Factory Configuration",
          "to select job factory implementation set key " + JOB_FACTORY_KEY + " to \n" +            "'db' - for database-backed job factory, and set jndi key " + DB_JOB_FACTORY_DATASOURCE_KEY + " to the datasource \n" +                "'file' - for file-backed job factory\n" +                "or name of java class to instantiate"
                );
    /**
     * 
     */
    private final void registerJobFactory() {
        String jobFactory = conf.getString(JOB_FACTORY_KEY,"file").trim();
        pico.registerComponentInstance("jobfactory-meta",JOBFACTORY_META_DATA);        
        if ("db".equalsIgnoreCase(jobFactory)) {
            try {
                Object o = conf.getProperty(DB_JOB_FACTORY_DATASOURCE_KEY);
                if (! (o instanceof DataSource)) {
                    throw new PropertyNotFoundException("datasource does not implement javax.sql.DataSource");
                }
                DataSource ds = (DataSource)o;
                pico.registerComponentImplementation(AbstractJobFactoryImpl.class,DBJobFactoryImpl.class);
                pico.registerComponentImplementation(SqlCommands.class,SqlCommandsFromConfig.class);
                pico.registerComponentInstance(DataSource.class,ds);
            } catch (PropertyNotFoundException e) {
                log.error("Could not find datasource, falling back to file-backed job factory",e);
                registerFallbackFactory();
            }
        } else if ("file".equalsIgnoreCase(jobFactory)){ 
            registerFallbackFactory();
        } else { // try treating it as a classname
            log.info("Trying to instantiate " + jobFactory);
            try {
                Class c = Class.forName(jobFactory);
                pico.registerComponentImplementation(AbstractJobFactoryImpl.class,c);
            } catch (ClassNotFoundException e) {
                log.error("Could not find java class " + jobFactory + " falling back to file-backed job factory",e);
                registerFallbackFactory();
            }
        }
    }
    
    private final void registerFallbackFactory() {
            pico.registerComponentImplementation(AbstractJobFactoryImpl.class,FileJobFactoryImpl.class);
            pico.registerComponentImplementation(FileJobFactoryImpl.BaseDirectory.class,BaseDirectoryFromConfig.class);
                   
    }
    /** key to look in config for implementation of locator to use
     * <p>
     * possible values: <tt>xml</tt> | <tt>registry</tt> | <tt><i>java.class.name</i></tt>
     */
    public static final String LOCATOR_KEY = "jes.locator";
    private static final ComponentDescriptor LOCATOR_META = new SimpleComponentDescriptor("Tool Locator Configuration",
        "to select tool locator implementation, set key " + LOCATOR_KEY + " to \n" + 
        "'registry' - for locator that queries the registry\n" +        "'xml' - for locator that is backed by xml config file\n" +        "or name of java class to instantiate"
        );
    /**
     * @todo registry should be the default.
     */
    private final  void registerLocator() {
        pico.registerComponentInstance("locator-meta",LOCATOR_META);
        String locator = conf.getString(LOCATOR_KEY,"xml").trim();
        if ("registry".equalsIgnoreCase(locator)) {
            pico.registerComponentImplementation(Locator.class,RegistryToolLocator.class);
            pico.registerComponentImplementation(RegistryToolLocator.RegistryEndpoint.class,RegistryEndpointFromConfig.class);
        } else if ("xml".equalsIgnoreCase(locator)) {
            registerFallbackLocator();
        } else {
            log.info("Trying to instantiate " + locator);
            try {
                Class c = Class.forName(locator);
                pico.registerComponentImplementation(Locator.class,c);
            } catch (ClassNotFoundException e) {
                log.error("Could not find java class " + locator + " falling back to xml-backed tool locator",e);
                registerFallbackLocator();
            }
        }
    }
        
    private final void registerFallbackLocator() {
        pico.registerComponentImplementation(Locator.class,XMLFileLocator.class);
        pico.registerComponentImplementation(XMLFileLocator.ToolList.class,ToolListFromConfig.class);
        
    }
    /** key to look in config for implementation of policy touse
     * <p>
     * possible vales: <tt>linear</tt> | <tt>join</tt> | <tt>flow</tt> | <tt><i>java.class.name</i></tt><br />
     * default: <tt>join</tt>
     */
    public static final String POLICY_KEY = "jes.policy";
    private static final ComponentDescriptor POLICY_META = new SimpleComponentDescriptor("Policy Configuration",
        "to select a policy implementation, set key " + POLICY_KEY + " to \n" + 
           "'linear' - for scheduling policy that executes everything in a sequential manner, ignoring join conditions\n" +           "'join' - for scheduling policy that executes steps in a sequential manner, obeying join conditions\n" +           "'flow' - for scheduling polucy that executes steps in parallel where possible (in flows), obeying join conditions\n" +           " or name of java class to instantiate"
           );
            

    /** Select which policy impleemntation to use.
     * 
     */
    private final void registerPolicy() {
        pico.registerComponentInstance("policy-meta",POLICY_META);
        String policy = conf.getString(POLICY_KEY,"join").trim();
        if ("linear".equalsIgnoreCase(policy)) {
            pico.registerComponentImplementation(Policy.class,LinearPolicy.class);
        } else if ("flow".equalsIgnoreCase(policy)) {
            pico.registerComponentImplementation(Policy.class,FlowPolicy.class);
        } else if ("join".equalsIgnoreCase(policy)) {
            pico.registerComponentImplementation(Policy.class,JoinPolicy.class);
        } else { 
            log.info("Trying to instantiate " + policy);
            try {
                Class c = Class.forName(policy);
                pico.registerComponentImplementation(Policy.class,c);
            } catch (ClassNotFoundException e) {
                log.error("Could not find java class " + policy + " falling back to linear scheduling policy",e);
                pico.registerComponentImplementation(Policy.class,LinearPolicy.class);
            }
        }
    }
    protected final Config conf;
}


/* 
$Log: ProductionComponentManager.java,v $
Revision 1.8  2004/03/18 10:54:22  nw
added code to make policy implementation configurable

Revision 1.7  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.6  2004/03/15 01:30:06  nw
factored component descriptor out into separate package

Revision 1.5  2004/03/15 00:30:19  nw
updaed to refer to moved classes

Revision 1.4  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.3  2004/03/08 00:36:34  nw
added configuration of registry tool locator to production components

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:39:26  nw
added implementation of a self-configuring production set of component
 
*/