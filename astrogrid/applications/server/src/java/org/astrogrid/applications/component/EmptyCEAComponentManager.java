/*$Id: EmptyCEAComponentManager.java,v 1.19 2006/03/11 05:57:54 clq2 Exp $
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


import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.CompositeApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.registry.RegistryAdminLocator;
import org.astrogrid.applications.description.registry.RegistryUploader;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.CeaThreadPool;
import org.astrogrid.applications.manager.ControlService;
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
 * Registers a single validity-checking component, that depends on all the publicaly-accessible compoennts (i.e.QueryService, MetaData, etc).
 * As picontainer checks that all dependencies can be resolved on startup, this component enforces the requirement that all component managers will have 
 * instances of the publically-accesible components. How thiese components are built up and registered is left free to the subclasses.
 * <p/>
 * This class also provides a set of static helper method that register 'clusters' of commonly-used components. These
 * can be called from implementations of component manager to quickly set up the basic parts of the system
 * @author Noel Winstanley nw@jb.man.ac.uk 04-May-2004
 * @TODO might be nice to have rather fewer static methods in this design to allow for overriding in subclasses - easier for a component manager to override just a part of the standard setup.
 * @see org.astrogrid.applications.component.JavaClassCEAComponentManager for example of how to assemble a server using this class.
 *
 */
public abstract class EmptyCEAComponentManager extends EmptyComponentManager implements CEAComponentManager{
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
        pico.registerComponentImplementation(VerifyRequiredComponents.class);
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
    /** dummy component that ensures required componets are registered with the container
     * not interesting, but needs to be publc so that picocontainer can instantiate it */ 
    public static final class VerifyRequiredComponents {
        /**
         * Commons Logger for this class
         */
        private static final Log logger = LogFactory
                .getLog(VerifyRequiredComponents.class);

        public VerifyRequiredComponents(ExecutionController ignored,MetadataService ignoredToo, QueryService dontcare, ControlService notused) {
        }
    }

    /** register a dummy implementatioin of the controlService introduced by paul.
     * @todo see whether the control service should really be part of the core cea server
     * - unsure whether it is required in other configurations of the cea system apart from applications
     *
     */
    protected final void registerDummyControlService() {
        log.info("Registering a dummy implementaton of the control service");
        pico.registerComponentInstance(ControlService.class, new ControlService() {

            public String deleteOldRuntimeWorkFiles(int days) {
                return "not implemented";
            }
        });
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
        log.info("Registering default services");
        pico.registerComponentImplementation(ApplicationDescriptionEnvironment.class,ApplicationDescriptionEnvironment.class);
        // trying something a little more intelligent.. pico.registerComponentImplementation(ExecutionController.class, DefaultExecutionController.class);
        pico.registerComponentImplementation(ExecutionController.class,ThreadPoolExecutionController.class);
        pico.registerComponentImplementation(PooledExecutor.class,CeaThreadPool.class);
       pico.registerComponentImplementation(QueryService.class,DefaultQueryService.class);   
       registerCompositeApplicationDescriptionLibrary(pico);
       // not added by default - stiches up cea-commandline.
        //registerContainerApplicationDescriptionLibrary(pico);
       registerEnvironmentRetriever(pico);
       
       // the protocol lib
       EmptyCEAComponentManager.registerProtocolLibrary(pico);
       EmptyCEAComponentManager.registerStandardIndirectionProtocols(pico);
       EmptyCEAComponentManager.registerAstrogridIndirectionProtocols(pico);

        }
    
    /** registers the default implementaiton of the indirection protocol library 
     * NB: does not register any protocols with the library. These must be added to the container separately
     * any protocols added to the container  wil be detected and added to the library on startup
     * @see #registerStandardIndirectionProtocols(MutablePicoContainer)
     * @see #registerAstrogridIndirectionProtocols(MutablePicoContainer)*/
    protected static final void registerProtocolLibrary(final MutablePicoContainer pico) {
        log.info("Registering default indirection protocol library");
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
            pico.registerComponentInstance(FileStoreExecutionHistory.StoreDir.class, new FileStoreExecutionHistory.StoreDir(){
                private final File dir= new File(config.getString(FILESTORE_BASEDIR,System.getProperty("java.io.tmpdir")));
                public File getDir() {
                    return dir;
                }
            });
        } 
        pico.registerComponentImplementation(IdGen.class,GloballyUniqueIdGen.class);        
    }

    
    /** key to query config for the url of the registry template to use (optional, default='/CEARegistryTemplate.xml' on classpath) 
     * @see #registerDefaultVOProvider(MutablePicoContainer, Config)*/
    public static final String REGISTRY_TEMPLATE_URL  ="cea.registry.template.url";
    /** key to query config for the url of this services endpoint (optional, recommended, otherwise makes a best guess)
     * @see #registerDefaultVOProvider(MutablePicoContainer, Config)*/
    public static final String SERVICE_ENDPOINT_URL = "cea.service.endpoint.url";
    
    /**
     * key to look in config under for the authorityid to add provided
     * applications to (optional, defaults to 'org.astrogrid.localhost')
     * 
     * @see #registerDefaultVOProvider(MutablePicoContainer, Config)
     */
    public final static String AUTHORITY_NAME = "cea.application.authorityid";

    /** register the standard VO Provider - the component that generates the registry entry.
     *  standard provider operates by constructing VODecription from applicationDescriptions in library
     * @param pico
     * @param config
     * @see #REGISTRY_TEMPLATE_URL
     * @see #SERVICE_ENDPOINT_URL
     */
    protected static final void registerDefaultVOProvider(MutablePicoContainer pico, final Config config) {
        log.info("Registering default vo provider - note that this is now the MetadataService");
        registerVOProvider(pico, config, DefaultMetadataService.class);        
  
    }    
    /**
    * @param pico
    * @param config
    */
   protected static void registerVOProvider(MutablePicoContainer pico, final Config config, final Class MetadataServ) {
      try {
        pico.registerComponentImplementation(MetadataService.class, MetadataServ);
        pico.registerComponentInstance(DefaultMetadataService.URLs.class, new DefaultMetadataService.URLs() {
            private final URL registryTemplate = config.getUrl(REGISTRY_TEMPLATE_URL,EmptyCEAComponentManager.class.getResource("/CEARegistryTemplate.xml"));         
            private final URL serviceEndpoint = config.getUrl(SERVICE_ENDPOINT_URL,new URL("http://localhost:8080/astrogrid-cea-server/services/CommonExecutionConnectorService"));
            public URL getRegistryTemplate() {
                return registryTemplate;
            }
            public URL getServiceEndpoint() {
                return serviceEndpoint;
            }
        });
        pico.registerComponentInstance(new BaseApplicationDescriptionLibrary.AppAuthorityIDResolver() {
           protected final String auth = config.getString(AUTHORITY_NAME, "org.astrogrid.localhost");

           public String getAuthorityID() {
               return auth;
           }
       });

        } catch (MalformedURLException e) {
            // unlikely this will happen
            log.fatal("Could could not register the vo provider " + e);
        }
   }

   /** register optional component that uploads vodescription to registry on startup.  */
    protected static final void registerDefaultRegistryUploader(MutablePicoContainer pico) {
        log.info("Registering default registry uploader");
        pico.registerComponentImplementation(RegistryUploader.class);    
        pico.registerComponentInstance(RegistryAdminLocator.class,new RegistryAdminLocator() {

            public RegistryAdminService getClient() {                
                return RegistryDelegateFactory.createAdmin();
            }
        });        
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
Revision 1.19  2006/03/11 05:57:54  clq2
roll back to before merged apps_gtr_1489, tagged as rolback_gtr_1489

Revision 1.17  2006/01/10 14:10:45  nw
added method to register default implementation of ControlService

Revision 1.16  2006/01/10 11:26:52  clq2
rolling back to before gtr_1489

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