/*$Id: ComponentManager.java,v 1.2 2004/02/27 00:46:03 nw Exp $
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

import org.astrogrid.config.SimpleConfig;
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
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.MockJobScheduler;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerDispatcher;
import org.astrogrid.jes.jobscheduler.locator.MapLocator;
import org.astrogrid.jes.jobscheduler.locator.XMLFileLocator;
import org.astrogrid.jes.jobscheduler.policy.RoughPolicy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


/**
 * Class that handles reading of configuration files, and constructing appropriate components.
 * then provides a place to store components, so they can be shared between services.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Feb-2004
 *
 */
public final class ComponentManager {
    private static final Log log = LogFactory.getLog(ComponentManager.class);
    /** Construct a new ComponentManager
     */
    private ComponentManager() {
        super();        
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
    public static final String DB_JOB_FACTORY_DATASOURCE_JNDI_URL = "java:comp/env/jdbc/jes-datasource";
    

   /** build a scheduler notifier, possibly using  the previously-constructed scheduler */     
 protected SchedulerNotifier buildNotifier(org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler scheduler) {
        return  new MemoryQueueSchedulerNotifier(scheduler);
}

/** type of tool locator to create
 * possible values in config: <tt>xml</tt> | <tt>registry</tt>
 */
public static final String TOOL_LOCATOR_TYPE = "tool.locator.type";
/** location of xml config file to parse - used for xml-based tool locator */
public static final String XML_TOOL_LOCATOR_URL = "xml.tool.locator.url";

/** build a job scheduler */
    protected org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler buildScheduler() {
        try {
            Locator locator = buildLocator();  
             Policy policy = buildPolicy();
             Dispatcher dispatcher = buildDispatcher(locator);
             return  new JobScheduler(facade,dispatcher,policy);
        } catch (MalformedURLException e) {
            log.fatal("Cannot initialize dispatcher",e);
            return buildFallbackScheduler();
        }        
    }


protected org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler buildFallbackScheduler() {
    log.fatal("Building mock scheduler - nothings going to work now");
    return new MockJobScheduler();
}


protected Policy buildPolicy() {
    return new RoughPolicy();
}

public static final String RELATIVE_MONITOR_URL="dispatcher.relative.monitor.url";
public static final String ABSOLUTE_MONITOR_URL="dispatcher.absolute.monitor.url";
/** build an application dispatcher 
 * @todo find a way to resolve relative URL into absolute */
protected Dispatcher buildDispatcher(Locator locator) throws MalformedURLException {
    URL monitorURL = null;
    String relativeURLString = SimpleConfig.getProperty(RELATIVE_MONITOR_URL);
    if (relativeURLString != null) {
        log.info(RELATIVE_MONITOR_URL + " := '" + relativeURLString + "'");
        monitorURL = new URL(relativeURLString);
    } else {
        String absoluteURLString = SimpleConfig.getProperty(ABSOLUTE_MONITOR_URL);
        log.info(ABSOLUTE_MONITOR_URL + " := '" + absoluteURLString + "'");
        if (absoluteURLString == null) {
            throw new MalformedURLException("Monitor Endpoint cannot be null");
        }
        monitorURL = new URL(absoluteURLString);
    }
    log.info("Monitor URL for dispatcher callback :" + monitorURL.toString());
    return new ApplicationControllerDispatcher(locator,monitorURL);
}




protected Locator buildLocator() {
        Locator locator = null;
        String toolLocatorType = SimpleConfig.getProperty(TOOL_LOCATOR_TYPE).trim();
        log.info(TOOL_LOCATOR_TYPE + " := '" + toolLocatorType + "'");
        
        if ("xml".equalsIgnoreCase(toolLocatorType)) {
            log.info("Creating XML based Tool Locator");
            String urlString = SimpleConfig.getProperty(XML_TOOL_LOCATOR_URL);
            if (urlString == null) {
                log.info("loading tool configuration from default location");
               // locator = new XMLFileLocator();
            }
        } else if ("registry".equalsIgnoreCase(toolLocatorType)) {
            log.info("Creating Registry based Tool Locator");
            log.error("Not implemented yet");
            locator = buildFallbackLocator();
            
        } else {
            log.error("Unrecognized tool locator type");
            locator = buildFallbackLocator();
        }
        return locator;
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
        String factoryType = SimpleConfig.getProperty(JOB_FACTORY_IMPL_TYPE).trim();
        log.info(JOB_FACTORY_IMPL_TYPE + " := '" + factoryType + "'");
        try {
            if ("database".equalsIgnoreCase(factoryType)) {
                log.info("Creating Database-backed Job Factory");
                Object o = new InitialContext().lookup(DB_JOB_FACTORY_DATASOURCE_JNDI_URL);
                if (o == null ||! (o instanceof DataSource)) {
                    throw new NamingException("result of looking up " + DB_JOB_FACTORY_DATASOURCE_JNDI_URL + " is not a datasource");
                }
                SqlCommands commands = new ConfigSqlCommands();
                fac = new DBJobFactoryImpl((DataSource)o,commands);
                      
            } else if ("file".equalsIgnoreCase(factoryType)) {
                log.info("Creating Filestore-backed Job Factory");
                String baseDirLocation = SimpleConfig.getProperty(FILE_JOB_FACTORY_BASEDIR);
                log.info(FILE_JOB_FACTORY_BASEDIR + " := '" + baseDirLocation + "'");
                if (baseDirLocation == null || baseDirLocation.length() == 0) {
                    baseDirLocation = System.getProperty("java.io.tmpdir");
                    log.warn("No directory for filestore provided - falling back to " + baseDirLocation );
                }
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
    
    protected final BeanFacade facade;
    protected final SchedulerNotifier notifier;    
    protected final org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler scheduler;
    
    /** get the instnace.
     * lazily initialized - parses configuration and creates components on first call
     * @return
     */
    public static synchronized ComponentManager getInstance() {
                if (theInstance == null) {
                    theInstance = new ComponentManager();
                }           
        return theInstance;
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
    public org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler getScheduler() {
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

