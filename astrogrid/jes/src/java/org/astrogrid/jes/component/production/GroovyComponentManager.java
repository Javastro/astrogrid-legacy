/*$Id: GroovyComponentManager.java,v 1.7 2006/01/04 09:52:32 clq2 Exp $
 * Created on 27-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component.production;

import org.astrogrid.component.ComponentManagerException;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.component.descriptor.SimpleComponentDescriptor;
import org.astrogrid.config.Config;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.jes.component.EmptyJesComponentManager;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.CachingFileJobFactory;
import org.astrogrid.jes.impl.workflow.DBJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.FileJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.SqlCommands;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.dispatcher.CeaApplicationDispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.CompositeDispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.ConeSearchDispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.SiapDispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.SsapDispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.inprocess.InProcessCeaComponentManager;
import org.astrogrid.jes.jobscheduler.impl.SchedulerTaskQueueDecorator;
import org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreterFactory;
import org.astrogrid.jes.jobscheduler.impl.groovy.GroovySchedulerImpl;
import org.astrogrid.jes.jobscheduler.impl.groovy.GroovyTransformers;
import org.astrogrid.jes.jobscheduler.impl.groovy.XStreamPickler;
import org.astrogrid.jes.jobscheduler.locator.RegistryToolLocator;
import org.astrogrid.jes.jobscheduler.locator.XMLFileLocator;
import org.astrogrid.jes.resultlistener.JesResultsListener;
import org.astrogrid.jes.service.v1.cearesults.ResultsListener;
import org.astrogrid.jes.util.BaseDirectory;
import org.astrogrid.jes.util.TemporaryBuffer;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;

import javax.sql.DataSource;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2004
 *
 */
public class GroovyComponentManager extends EmptyJesComponentManager{
    public GroovyComponentManager() throws ComponentManagerException {
        this(SimpleConfig.getSingleton());
    }
    
    public GroovyComponentManager(Config conf) throws ComponentManagerException {
        super();
        try {
            pico.registerComponentInstance("jes-meta",JES_META);
            pico.registerComponentInstance(Config.class,conf); 

            registerGroovyEngine(pico);   
           
            // different kinds of dispatcher.
           pico.registerComponentImplementation(Dispatcher.class,CompositeDispatcher.class); // _the_ dispatcher.
           pico.registerComponentImplementation(CeaApplicationDispatcher.class);           
           pico.registerComponentImplementation(CeaApplicationDispatcher.Endpoints.class,EndpointsFromConfig.class);
           
           pico.registerComponentImplementation(ConeSearchDispatcher.class); // dispach cone searches.
           pico.registerComponentImplementation(SiapDispatcher.class);
           pico.registerComponentImplementation(SsapDispatcher.class);
           // as a work-around for a bug (picos don't register themselves anymore, but claim they do), need to set up parameters for this component by hand.
           pico.registerComponentImplementation(InProcessCeaComponentManager.class,InProcessCeaComponentManager.class,
                   new Parameter[]{
                       new ConstantParameter(pico)
                       , new ConstantParameter(conf)                    
           }                    
          );        // register factory for temp buffers- so each component that requires one is passed it.
           pico.registerComponent(new ConstructorInjectionComponentAdapter(TemporaryBuffer.class,TemporaryBuffer.class));
           registerStandardComponents(pico);
           registerLocator(pico,conf);                    
           registerJobFactory(pico,conf);

           } catch (Exception e) {
               log.fatal("Could not create component manager",e);
               throw new ComponentManagerException(e);
          }
      }
      /**Description-only component - metadata for entire system. 
       * 
       * @todo add config tests for a production system in here */
      private static final ComponentDescriptor JES_META = new SimpleComponentDescriptor(
          "Groovy-Driven Job Execution System",
          "job execution system"
      );    

      public static void registerGroovyEngine(MutablePicoContainer pico) {
               pico.registerComponentImplementation(SCHEDULER_ENGINE,GroovySchedulerImpl.class);          
                 pico.registerComponentImplementation(GroovySchedulerImpl.Transformers.class,GroovyTransformers.class);         
                 pico.registerComponentImplementation(GroovyInterpreterFactory.class,GroovyInterpreterFactory.class);
                 pico.registerComponentImplementation(GroovyInterpreterFactory.Pickler.class,XStreamPickler.class);
                 
        }
      /** registers standard web-interface implementations (monitor / controller / listener),standard facade., standard scheduler task queue. */
      public static void registerStandardComponents(MutablePicoContainer pico) {   
          
          pico.registerComponentImplementation(JobMonitor.class,org.astrogrid.jes.jobmonitor.JobMonitor.class);
          pico.registerComponentImplementation(JobController.class,org.astrogrid.jes.jobcontroller.JobController.class);       
          pico.registerComponentImplementation(ResultsListener.class,JesResultsListener.class);
          pico.registerComponentImplementation(JobScheduler.class,SchedulerTaskQueueDecorator.class,
             new Parameter[]{
                 new ComponentParameter(SCHEDULER_ENGINE)
             });        
      }

      /** key to look in config for implementation of job factory to use
       * <p>
       * possible values = <tt>db</tt> | <tt>file</tt> | <tt><i>java.class.name</i></tt> */
      public static final String JOB_FACTORY_KEY = "jes.jobfactory";
      /** key to look in config for a datasource object */
      public static final String DB_JOB_FACTORY_DATASOURCE_KEY = "jdbc/jes-datasource";
      public static final ComponentDescriptor JOBFACTORY_META_DATA 
          = new SimpleComponentDescriptor("Job Factory Configuration",
            "to select job factory implementation set key " + JOB_FACTORY_KEY + " to \n" +
              "'db' - for database-backed job factory, and set jndi key " + DB_JOB_FACTORY_DATASOURCE_KEY + " to the datasource \n" +
                  "'file' - for file-backed job factory\n" +
                  "or name of java class to instantiate"
                  );
      /**
       * 
       */
      public static  final void registerJobFactory(MutablePicoContainer pico,Config conf) {
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
                  registerFallbackFactory(pico);
              }
          } else if ("file".equalsIgnoreCase(jobFactory)){ 
              registerFallbackFactory(pico);
          } else { // try treating it as a classname
              log.info("Trying to instantiate " + jobFactory);
              try {
                  Class c = Class.forName(jobFactory);
                  pico.registerComponentImplementation(AbstractJobFactoryImpl.class,c);
              } catch (ClassNotFoundException e) {
                  log.error("Could not find java class " + jobFactory + " falling back to file-backed job factory",e);
                  registerFallbackFactory(pico);
              }
          }
      }
      
      private static final void registerFallbackFactory(MutablePicoContainer pico) {
              pico.registerComponentImplementation(AbstractJobFactoryImpl.class,CachingFileJobFactory.class);
              pico.registerComponentImplementation(BaseDirectory.class,BaseDirectoryFromConfig.class);
                     
      }
      /** key to look in config for implementation of locator to use
       * <p>
       * possible values: <tt>xml</tt> | <tt>registry</tt> | <tt><i>java.class.name</i></tt>
       */
      public static final String LOCATOR_KEY = "jes.locator";
      private static final ComponentDescriptor LOCATOR_META = new SimpleComponentDescriptor("Tool Locator Configuration",
          "to select tool locator implementation, set key " + LOCATOR_KEY + " to \n" + 
          "'registry' - for locator that queries the registry\n" +
          "'xml' - for locator that is backed by xml config file\n" +
          "or name of java class to instantiate"
          );
      /**
       * @todo registry should be the default.
       */
      public static final  void registerLocator(MutablePicoContainer pico, Config conf) {
          pico.registerComponentInstance("locator-meta",LOCATOR_META);
          String locator = conf.getString(LOCATOR_KEY,"xml").trim();
          if ("registry".equalsIgnoreCase(locator)) {
              pico.registerComponentImplementation(Locator.class,RegistryToolLocator.class);
              pico.registerComponentImplementation(RegistryToolLocator.RegistryEndpoint.class,RegistryEndpointFromConfig.class);
          } else if ("xml".equalsIgnoreCase(locator)) {
              registerFallbackLocator(pico);
          } else {
              log.info("Trying to instantiate " + locator);
              try {
                  Class c = Class.forName(locator);
                  pico.registerComponentImplementation(Locator.class,c);
              } catch (ClassNotFoundException e) {
                  log.error("Could not find java class " + locator + " falling back to xml-backed tool locator",e);
                  registerFallbackLocator(pico);
              }
          }
      }
          
      private static final void registerFallbackLocator(MutablePicoContainer pico) {
          pico.registerComponentImplementation(Locator.class,XMLFileLocator.class);
          pico.registerComponentImplementation(XMLFileLocator.ToolList.class,ToolListFromConfig.class);
          
      }          
  }
            
    


/* 
$Log: GroovyComponentManager.java,v $
Revision 1.7  2006/01/04 09:52:32  clq2
jes-gtr-1462

Revision 1.6.42.1  2005/12/09 23:11:55  gtr
I refactored the base-directory feature out of its inner class and interface in FileJobFactory and into org.aastrogrid.jes.util. This addresses part, but not all, of BZ1487.

Revision 1.6  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.5.20.1  2005/04/11 16:31:13  nw
updated version of xstream.
added caching to job factory

Revision 1.5  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.4.36.1  2005/03/11 14:02:10  nw
changes to work with pico1.1, and linked in the In-process
cea server

Revision 1.4  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.3.60.1  2004/11/05 15:25:44  nw
added temporary buffers into the component manager

Revision 1.3  2004/08/03 16:31:25  nw
simplified interface to dispatcher and locator components.
removed redundant implementations.

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.3  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.2  2004/07/27 23:50:09  nw
removed betwixt (duff). replaces with xstream.

Revision 1.1.2.1  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.
 
*/