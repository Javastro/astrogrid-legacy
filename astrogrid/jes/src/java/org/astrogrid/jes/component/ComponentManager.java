/*$Id: ComponentManager.java,v 1.4 2004/03/05 16:16:23 nw Exp $
 * Created on 16-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component;

import org.astrogrid.config.Config;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.jes.comm.JobScheduler;
import org.astrogrid.jes.comm.MemoryQueueSchedulerNotifier;
import org.astrogrid.jes.comm.SchedulerNotifier;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.CastorBeanFacade;
import org.astrogrid.jes.impl.workflow.ConfigSqlCommands;
import org.astrogrid.jes.impl.workflow.DBJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.FileJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.InMemoryJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.SqlCommands;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.MockJobScheduler;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerDispatcher;
import org.astrogrid.jes.jobscheduler.locator.ConstantToolLocator;
import org.astrogrid.jes.jobscheduler.locator.MapLocator;
import org.astrogrid.jes.jobscheduler.locator.XMLFileLocator;
import org.astrogrid.jes.jobscheduler.policy.LinearPolicy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.NamingException;
import javax.sql.DataSource;


/**
 * Class that handles reading of configuration files, and constructing appropriate components.
 * then provides a place to store components, so they can be shared between services.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Feb-2004
 *
 */
public class ComponentManager {
    protected static final Log log = LogFactory.getLog(ComponentManager.class);
    /** Construct a new ComponentManager, loading data via simpleConfig
     */
    protected ComponentManager() {
        this(SimpleConfig.getSingleton());
    }
    
    /** construct a new component manager, loading data from supplied config 
     * used only for testing.*/
    protected ComponentManager(Config conf) {
        this.conf = conf;
    }
    
    /** configuration object to query for data */
    protected final Config conf;
    
    /** assemble services - top level call that initialized everything.*/
    protected void buildServices(){     
        this.facade = buildFacade();
        this.scheduler = buildScheduler();
        this.notifier = buildNotifier(this.scheduler);
    } 

    /** type of job factory to create.
     * possible values in config: <tt>database</tt> | <tt>file</tt> | <tt>memory</tt>
     */
    public static final String JOB_FACTORY_IMPL_TYPE = "job.factory.type";
    /** directory to use to store workflow documents if using the file-based job factory */
    public static final String FILE_JOB_FACTORY_BASEDIR = "file.job.factory.basedir";
    /** JNDI url to look for a dataSource, if using the db-based job factory */
    public static final String DB_JOB_FACTORY_DATASOURCE_JNDI_URL ="jdbc/jes-datasource"; // removed java:/comp/env prefix
    

   /** build a scheduler notifier, possibly using  the previously-constructed scheduler */     
 protected SchedulerNotifier buildNotifier(JobScheduler scheduler) {
        return  new MemoryQueueSchedulerNotifier(scheduler);
}

/** type of tool locator to create
 * possible values in config: <tt>xml</tt> | <tt>registry</tt> | <tt>constant</tt>
 */
public static final String TOOL_LOCATOR_TYPE = "tool.locator.type";
/** location of xml config file to parse - used for xml-based tool locator */
public static final String XML_TOOL_LOCATOR_URL = "xml.tool.locator.url";
/** endpoint to use for constant tool locator */
public static final String CONSTANT_TOOL_LOCATOR_ENDPOINT = "constant.tool.locator.endpoint";
/** interface to return for constant tool locator */
public static final String CONSTANT_TOOL_LOCATOR_INTERFACE = "constant.tool.locator.interface";

/** build a job scheduler */
    protected JobScheduler buildScheduler() {
        try {
            Locator locator = buildLocator();  
             Policy policy = buildPolicy();
             Dispatcher dispatcher = buildDispatcher(locator);
             return  new org.astrogrid.jes.jobscheduler.JobScheduler(facade,dispatcher,policy);
        } catch (MalformedURLException e) {
            log.fatal("Cannot initialize dispatcher",e);
            return buildFallbackScheduler();
        }        
    }


protected JobScheduler buildFallbackScheduler() {
    log.fatal("Building mock scheduler - nothings going to work now");
    return new MockJobScheduler();
}


protected Policy buildPolicy() {
    return new LinearPolicy();
}

public static final String RELATIVE_MONITOR_URL="dispatcher.relative.monitor.url";
public static final String ABSOLUTE_MONITOR_URL="dispatcher.absolute.monitor.url";
/** build an application dispatcher 
 * @todo find a way to resolve relative URL into absolute */
protected Dispatcher buildDispatcher(Locator locator) throws MalformedURLException {
    URL monitorURL = null;
    try {
        String relativeURLString = conf.getString(RELATIVE_MONITOR_URL);
        log.info(RELATIVE_MONITOR_URL + " := '" + relativeURLString + "'");
        monitorURL = new URL(relativeURLString);
    } catch (PropertyNotFoundException e) { // oh well,    
        String absoluteURLString = conf.getString(ABSOLUTE_MONITOR_URL);
        log.info(ABSOLUTE_MONITOR_URL + " := '" + absoluteURLString + "'");
        monitorURL = new URL(absoluteURLString);        
    }
    log.info("Monitor URL for dispatcher callback :" + monitorURL.toString());
    return new ApplicationControllerDispatcher(locator,monitorURL);
}




protected Locator buildLocator() {
        String toolLocatorType = conf.getString(TOOL_LOCATOR_TYPE,"xml").trim();
        log.info(TOOL_LOCATOR_TYPE + " := '" + toolLocatorType + "'");
        try {
        if ("xml".equalsIgnoreCase(toolLocatorType)) {
            log.info("Creating XML based Tool Locator");
            try {
                String urlString = conf.getString(XML_TOOL_LOCATOR_URL);
                log.info(XML_TOOL_LOCATOR_URL + " := '" + urlString);
                return new XMLFileLocator(new URL(urlString));
            } catch (PropertyNotFoundException e) {
                log.info("loading tool configuration from default location" + XMLFileLocator.DEFAULT_CONFIG_LOCATION);
                return new XMLFileLocator();                
            }
            
        } else if ("registry".equalsIgnoreCase(toolLocatorType)) {
            log.info("Creating Registry based Tool Locator");
            log.error("Not implemented yet");
            return buildFallbackLocator();
    
        } else if ("constant".equalsIgnoreCase(toolLocatorType)) {
            log.info("Creating constant tool locator");
            String endpoint = conf.getString(CONSTANT_TOOL_LOCATOR_ENDPOINT);
            String interfaceName = conf.getString(CONSTANT_TOOL_LOCATOR_INTERFACE);
            return new ConstantToolLocator(new URL(endpoint),interfaceName);         
        } else {
            log.error("Unrecognized tool locator type");
            return buildFallbackLocator();
        }      
        } catch (Exception e) {
            log.fatal("Failed to create chosen locator",e);
            return buildFallbackLocator(); 
        }
    }
    
    protected Locator buildFallbackLocator() {
        log.fatal("Unable to initialize tool Locator - falling back to empty locator");
        return new MapLocator();
    }


    /** build a bean facade, containing a job factory
     * Job Factory built depends on configuration settings.
     * @return
     */
    protected BeanFacade buildFacade() {
        AbstractJobFactoryImpl fac = null;
        String factoryType = conf.getString(JOB_FACTORY_IMPL_TYPE,"memory").trim();
        log.info(JOB_FACTORY_IMPL_TYPE + " := '" + factoryType + "'");
        try {
            if ("database".equalsIgnoreCase(factoryType)) {
                log.info("Creating Database-backed Job Factory");
                Object o = conf.getProperty(DB_JOB_FACTORY_DATASOURCE_JNDI_URL); // expected in jndi.
                if (o == null ||! (o instanceof DataSource)) {
                    throw new NamingException("result of looking up " + DB_JOB_FACTORY_DATASOURCE_JNDI_URL + " is not a datasource");
                }
                SqlCommands commands = new ConfigSqlCommands(conf);
                fac = new DBJobFactoryImpl((DataSource)o,commands);
                      
            } else if ("file".equalsIgnoreCase(factoryType)) {
                log.info("Creating Filestore-backed Job Factory");
                String baseDirLocation = conf.getString(FILE_JOB_FACTORY_BASEDIR, System.getProperty("java.io.tmpdir"));
                log.info(FILE_JOB_FACTORY_BASEDIR + " := '" + baseDirLocation + "'");
                File baseDir = new File(baseDirLocation);
                fac = new FileJobFactoryImpl(baseDir);
                
            } else if ("memory".equalsIgnoreCase(factoryType)) {
                fac = buildFallbackFactory();
            } else {
                log.error("Could not determine which job factory to create");
                fac = buildFallbackFactory();
            }
        } catch (IOException e) {
            log.fatal("Exception initializing job factory",e);
            fac = buildFallbackFactory();
        } catch (NamingException e) {
            log.fatal("Exception looking up datasource",e);
            fac = buildFallbackFactory();
        }                                 
        return new CastorBeanFacade(fac); 
    }

/** simplest possbible job factory, it all else fails. */            
protected AbstractJobFactoryImpl buildFallbackFactory() {
    log.warn("Creating Memory-only Job Factory - no workflows will persist");
    return new InMemoryJobFactoryImpl();
}    
    
    protected  BeanFacade facade;
    protected SchedulerNotifier notifier;    
    protected JobScheduler scheduler;
    
    /** get the instnace.
     * lazily initialized - parses configuration and creates components on first call
     * @return
     */
    public static synchronized ComponentManager getInstance() {
                if (theInstance == null) {
                    theInstance = new ComponentManager();
                    theInstance.buildServices();
                }           
        return theInstance;
    }
    
    /** unsafe, useful for testing */
    public static void _setInstance(ComponentManager mgr) {
        theInstance = mgr;
        theInstance.buildServices();
    }
    
    protected static ComponentManager theInstance;
    /**
     * @return
     */
    public BeanFacade getFacade() {
        return facade;
    }

    /**
     * @return
     */
    public SchedulerNotifier getNotifier() {
        return notifier;
    }


    /**
     * @return
     */
    public JobScheduler getScheduler() {
        return scheduler;
    }
    
    /** used for debugging / ouotput to JSP 
     * @todo fill this in.*/
    public static String produceConfigurationInformation() {
        StringBuffer buff = new StringBuffer();
        buff.append("<i>will produce configuration info here</i><br />");
        return buff.toString();
        
    }

}

