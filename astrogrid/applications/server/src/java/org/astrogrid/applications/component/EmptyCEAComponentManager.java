/*$Id: EmptyCEAComponentManager.java,v 1.18 2006/03/07 21:45:26 clq2 Exp $
 * Created on 04-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.component;

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.CompositeApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.registry.RegistryAdminLocator;
import org.astrogrid.applications.description.registry.RegistryAdminLocatorImpl;
import org.astrogrid.applications.description.registry.RegistryUploader;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.BaseConfiguration;
import org.astrogrid.applications.manager.CeaThreadPool;
import org.astrogrid.applications.manager.ControlService;
import org.astrogrid.applications.manager.DefaultAppAuthorityIDResolver;
import org.astrogrid.applications.manager.DefaultApplicationEnvironmentRetriever;
import org.astrogrid.applications.manager.DefaultMetadataService;
import org.astrogrid.applications.manager.DefaultQueryService;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.ThreadPoolExecutionController;
import org.astrogrid.applications.manager.idgen.GloballyUniqueIdGen;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.FileStoreExecutionHistory;
import org.astrogrid.applications.manager.persist.InMemoryExecutionHistory;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.FtpProtocol;
import org.astrogrid.applications.parameter.protocol.HttpProtocol;
import org.astrogrid.applications.parameter.protocol.IvornProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.component.EmptyComponentManager;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Startable;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/** empty implementation of {@link org.astrogrid.applications.component.CEAComponentManager}
 * provides implementations of the accessor methods, but no componets are registered with the container. (this should be done in subclasses).
 * <p>
 * This class also provides a set of static helper method that register 'clusters' of commonly-used components. These
 * can be called from implementations of component manager to quickly set up the basic parts of the system
 * @author Noel Winstanley nw@jb.man.ac.uk 04-May-2004
 * @TODO might be nice to have rather fewer static methods in this design to allow for overriding in subclasses - easier for a component manager to override just a part of the standard setup.
 * @see org.astrogrid.applications.component.JavaClassCEAComponentManager for example of how to assemble a server using this class.
 *
 */
public abstract class EmptyCEAComponentManager 
    extends EmptyComponentManager 
    implements CEAComponentManager{
    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory
            .getLog(EmptyCEAComponentManager.class);

    /** Construct a new EmptyCEAComponentManager
     * 
     */
    public EmptyCEAComponentManager() {
      super();
    }

    /**
     * @see org.astrogrid.applications.component.CEAComponentManager#getController()
     */
    public final ExecutionController getExecutionController() {
        return (ExecutionController)this.pico.getComponentInstanceOfType(ExecutionController.class);
    }

    /**
     * @see org.astrogrid.applications.component.CEAComponentManager#getMetaData()
     */
    public final MetadataService getMetadataService() {
        return (MetadataService)this.pico.getComponentInstanceOfType(MetadataService.class);
    }
    
    public final QueryService getQueryService() {
        return (QueryService)this.pico.getComponentInstanceOfType(QueryService.class);
    }
    
    public final RegistryUploader getRegistryUploaderService() {
       return (RegistryUploader)this.pico.getComponentInstance(RegistryUploader.class);
    }
    
    public final ControlService getControlService() {
       return (ControlService) this.pico.getComponentInstanceOfType(ControlService.class);
    }
    
    public final Configuration getConfiguration() {
      return (Configuration)this.pico.getComponentInstanceOfType(Configuration.class);
    }

    // convenience methods for regostering commnly used sets of components
    /** register the default top-level services core of CEA 
     * registers default implementations of {@link QueryService}, {@link MetadataService} and {@link ExecutionController},
     * plus the {@link ApplicationDescriptionEnvironment}
     * also registers a composite application description library, and a container application description library.
     * @see #registerCompositeApplicationDescriptionLibrary(MutablePicoContainer)
     * @see #registerContainerApplicationDescriptionLibrary(MutablePicoContainer)
     * */
    protected  final void registerDefaultServices(MutablePicoContainer pico) {
       log.debug("Registering default services");
        
       pico.registerComponentImplementation(ApplicationDescriptionEnvironment.class,ApplicationDescriptionEnvironment.class);
       pico.registerComponentImplementation(ExecutionController.class,ThreadPoolExecutionController.class);
       pico.registerComponentImplementation(PooledExecutor.class,CeaThreadPool.class);
       pico.registerComponentImplementation(QueryService.class,DefaultQueryService.class);   
       registerCompositeApplicationDescriptionLibrary(pico);
       registerEnvironmentRetriever(pico);
       
       // the protocol lib
       EmptyCEAComponentManager.registerProtocolLibrary(pico);
       EmptyCEAComponentManager.registerStandardIndirectionProtocols(pico);
       EmptyCEAComponentManager.registerAstrogridIndirectionProtocols(pico);
       
       // The persistence mechanism. This is needed to make the QueryService work.
       this.registerDefaultPersistence(pico, SimpleConfig.getSingleton());
       
       // This seems to be necessary to get the ApplicationDescriptionEnvironment to work.
       this.registerDefaultVOProvider(pico);
       
       log.info("Components for a generic CEC have been registered.");
    }
    
    /** registers the default implementaiton of the indirection protocol library 
     * NB: does not register any protocols with the library. These must be added to the container separately
     * any protocols added to the container  wil be detected and added to the library on startup
     * @see #registerStandardIndirectionProtocols(MutablePicoContainer)
     * @see #registerAstrogridIndirectionProtocols(MutablePicoContainer)*/
    protected static final void registerProtocolLibrary(final MutablePicoContainer pico) {
        log.debug("Registering default indirection protocol library");
        pico.registerComponentImplementation(ProtocolLibrary.class,DefaultProtocolLibrary.class);
        // on startup (i.e. after everythinig has been registered, this throwaway component will add all known indirection protocols to the library).
        pico.registerComponentInstance(new Startable() {
            public void start() {
                DefaultProtocolLibrary lib = (DefaultProtocolLibrary) pico.getComponentInstanceOfType(DefaultProtocolLibrary.class);
                for (Iterator i = pico.getComponentAdaptersOfType(Protocol.class).iterator(); i.hasNext(); ) {
                    ComponentAdapter ca = (ComponentAdapter)i.next();
                    Protocol p = (Protocol)ca.getComponentInstance(pico);
                    lib.addProtocol(p);
                }
            }

            public void stop() {
            }
        });
    }
    
    /** registers the standard indirection protocols - http:, ftp:, file: */
    protected static final void registerStandardIndirectionProtocols(MutablePicoContainer pico) {
        pico.registerComponentImplementation(HttpProtocol.class);
        pico.registerComponentImplementation(FtpProtocol.class);
        pico.registerComponentImplementation(FileProtocol.class);
    }
    
    /** registers the astorgird-specific protocols - ivo: and agsl:*/
    protected static final void registerAstrogridIndirectionProtocols(MutablePicoContainer pico) {
        pico.registerComponentImplementation(IvornProtocol.class);
    }
    
    /** key used to determing from config where to store execution history files
     * @see #registerDefaultPersistence(MutablePicoContainer, Config) */
    public static final String FILESTORE_BASEDIR = "cea.filestore.basedir";
    
    /** key used to determine which persistence back-end to use -
     * <br> valid values - <tt>file</tt>, <tt>memory</tt>
     * <br>optional- defaults to <tt>file</tt>*/
    public static final String PERSISTENCE_BACKEND = "cea.persistence.backend";
    
    /** register the standard persistence system - globally unique id generation, and file-based exection history 
     * configured by {@link #FILESTORE_BASEDIR}
     * */
    protected static final void registerDefaultPersistence(MutablePicoContainer pico, final Config config) {

        String backend = System.getProperty(PERSISTENCE_BACKEND,"file").toLowerCase().trim();
        if (backend.equals("memory")) {
            log.warn("Only using memory-based persistence system - all history will be lost on restart");
            pico.registerComponentImplementation(ExecutionHistory.class,InMemoryExecutionHistory.class);
        } else {
            if (! backend.equals("file")) {
                log.error("Unrecognized value '" + backend + "' for key " +  PERSISTENCE_BACKEND + " - defaulting to file-based persistence");
            }
            log.info("Registering file-based persistence system");
            pico.registerComponentImplementation(ExecutionHistory.class,FileStoreExecutionHistory.class);
        } 
        pico.registerComponentImplementation(IdGen.class,GloballyUniqueIdGen.class);        
    }
    
    /**
     * key to look in config under for the authorityid to add provided
     * applications to (optional, defaults to 'org.astrogrid.localhost')
     * 
     * @see #registerDefaultVOProvider(MutablePicoContainer, Config)
     */
    public final static String AUTHORITY_NAME = "cea.application.authorityid";

    /** register the standard VO Provider - the component that generates the registry entry.
     *  standard provider operates by constructing VODecription from applicationDescriptions in library
     */
    protected static final void registerDefaultVOProvider(MutablePicoContainer pico) {
        log.info("Registering default vo provider - note that this is now the MetadataService");
        registerVOProvider(pico, DefaultMetadataService.class);        
  
    }    
    /**
    * @param pico
    */
   protected static void registerVOProvider(MutablePicoContainer pico,
                                            final Class MetadataServ) {
     // This method can get called twice for some variants, so allow
     // overriding of the registrations.
     pico.unregisterComponent(MetadataService.class);
     pico.unregisterComponent(AppAuthorityIDResolver.class);
     pico.registerComponentImplementation(MetadataService.class,
                                          MetadataServ);
     pico.registerComponentImplementation(AppAuthorityIDResolver.class, 
                                          DefaultAppAuthorityIDResolver.class); 
   }

   /** register optional component that uploads vodescription to registry on startup.  */
    protected static final void registerDefaultRegistryUploader(MutablePicoContainer pico) {
        log.info("Registering default registry uploader");
        pico.registerComponentImplementation(RegistryUploader.class);    
        pico.registerComponentImplementation(RegistryAdminLocator.class,
                                             RegistryAdminLocatorImpl.class);
    }
    
    /** register the {@link CompositeApplicationDescriptionLibrary} - this will assemble other implementations of {@link ApplicationDescriptionLibrary}
     *  registered in the container into a single unified library - allowing multiple 
     * providers to be merged.
     * <p/>
     * For this to work, other app description libs must be registered with pico under their own classname - <b>not</b>
     * the interface {@link ApplicationDescriptionLIbrary}
     * @param pico
     */
    protected static final void registerCompositeApplicationDescriptionLibrary(final MutablePicoContainer pico) {
        pico.registerComponentImplementation(ApplicationDescriptionLibrary.class,CompositeApplicationDescriptionLibrary.class);
        // on startup (i.e. after everythinig has been registered, this throwaway component will add all other application description libraries to this one
        pico.registerComponentInstance(new Startable() {
            public void start() {
                logger.info("Registering application description libraries in composite");
                CompositeApplicationDescriptionLibrary uberLib = (CompositeApplicationDescriptionLibrary)pico.getComponentInstanceOfType(CompositeApplicationDescriptionLibrary.class);
                for (Iterator i = pico.getComponentAdaptersOfType(ApplicationDescriptionLibrary.class).iterator(); i.hasNext(); ) {
                    ComponentAdapter ca = (ComponentAdapter)i.next();
                    ApplicationDescriptionLibrary lib = (ApplicationDescriptionLibrary)ca.getComponentInstance(pico);
                    // watch out for self.
                    if (lib != uberLib) {
                        uberLib.addLibrary(lib);
                    }
                }
            }

            public void stop() {
            }
        });        
    }
    /** register an {@link ApplicationDescriptionLibrary} that will assemble all {@link ApplicationDescription} components registered with the container 
     *  - this allows applicatonDescriptions to be setup and configured individually at the picocontainer level.
     * @author Noel Winstanley nw@jb.man.ac.uk 05-Jul-2004
     *
     */
   public static void registerContainerApplicationDescriptionLibrary(final MutablePicoContainer pico) {
       pico.registerComponentImplementation(BaseApplicationDescriptionLibrary.class);
       // register a throwaway component that adds all applicationDescriptions in the container into the library on container startup.
       pico.registerComponentInstance(new Startable() {

        public void start() {
            BaseApplicationDescriptionLibrary lib = (BaseApplicationDescriptionLibrary)pico.getComponentInstanceOfType(BaseApplicationDescriptionLibrary.class);
            for (Iterator i = pico.getComponentAdaptersOfType(ApplicationDescription.class).iterator(); i.hasNext();) {
                ComponentAdapter ca = (ComponentAdapter)i.next();
                ApplicationDescription appDesc = (ApplicationDescription)ca.getComponentInstance(pico);
                lib.addApplicationDescription(appDesc);
            }
        }
        public void stop() {
        }
       }); 
    }

   public void registerEnvironmentRetriever(final MutablePicoContainer pico)
   {
      logger.info("registering Default Environment Retriever");
      pico.registerComponentImplementation(ApplicationEnvironmentRetriver.class, DefaultApplicationEnvironmentRetriever.class);
   }
   

}


/* 
$Log: EmptyCEAComponentManager.java,v $
Revision 1.18  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.14.20.7  2006/01/31 21:39:07  gtr
Refactored. I have altered the configuration code slightly so that the JUnit tests can impose a Configuration instance to configure the tests. I have also fixed up almost all the bad tests for commandline and http.

Revision 1.14.20.6  2006/01/26 13:19:04  gtr
Refactored.

Revision 1.14.20.5  2006/01/26 11:05:19  gtr
The new configuration service is used instead of the separate configuration interfaces.

Revision 1.14.20.4  2006/01/25 17:04:32  gtr
Refactored: the configuration is now a fixed structure based at the configurable location cea.base.dir.

Revision 1.14.20.3  2005/12/22 13:57:56  gtr
registerVOProvider() can now be called more than once; it deregisters and reregisters components as necessary,

Revision 1.14.20.2  2005/12/19 18:12:30  gtr
Refactored: changes in support of the fix for 1492.

Revision 1.14.20.1  2005/12/18 14:48:24  gtr
Refactored to allow the component managers to pass their unit tests and the fingerprint JSP to work. See BZ1492.

Revision 1.14  2005/08/10 14:45:37  clq2
cea_pah_1317

Revision 1.13.6.1  2005/07/21 15:10:22  pah
changes to acommodate contol component, and starting to change some of the static methods to dynamic

Revision 1.13  2005/07/05 08:26:57  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.12.42.2  2005/06/14 09:49:32  pah
make the http cec only register itself as a ceaservice - do not try to reregister any cea applications that it finds

Revision 1.12.42.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.12.28.2  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.12.28.1  2005/06/02 14:57:28  pah
merge the ProvidesVODescription interface into the MetadataService interface

Revision 1.12  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.11.26.1  2005/03/11 11:21:13  nw
adjusted to fit with pico 1.1

Revision 1.11  2004/11/27 13:20:03  pah
result of merge of pah_cea_bz561 branch

Revision 1.10  2004/11/05 13:07:04  nw
added option to use memory-backed execution history.

Revision 1.9  2004/09/22 10:52:50  pah
getting rid of some unused imports

Revision 1.8  2004/09/17 01:20:22  nw
added lifecycle listener and threadpool

Revision 1.7.20.1  2004/09/14 13:45:38  nw
made thread-pooled executor the default.

Revision 1.7  2004/08/27 10:56:38  nw
removed container-inspecting applicationDescriptionLibrary from default setup - cea-commandline doesn't like it.

Revision 1.6  2004/08/11 16:50:56  nw
added in default application description libraries.

Revision 1.5  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.4  2004/07/23 13:21:21  nw
Javadocs

Revision 1.3  2004/07/05 18:45:09  nw
added helper method to assemble lib from registered descriptions

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.2  2004/05/28 10:23:11  nw
checked in early, broken version - but it builds and tests (fail)

Revision 1.1.2.1  2004/05/21 12:00:22  nw
merged in latest changes from HEAD. start of refactoring effort
 
*/