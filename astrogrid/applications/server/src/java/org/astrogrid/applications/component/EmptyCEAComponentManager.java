/*$Id: EmptyCEAComponentManager.java,v 1.4 2004/07/23 13:21:21 nw Exp $
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
import org.astrogrid.applications.description.registry.RegistryEntryBuilder;
import org.astrogrid.applications.description.registry.RegistryUploader;
import org.astrogrid.applications.manager.DefaultExecutionController;
import org.astrogrid.applications.manager.DefaultMetadataService;
import org.astrogrid.applications.manager.DefaultQueryService;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.idgen.GloballyUniqueIdGen;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.FileStoreExecutionHistory;
import org.astrogrid.applications.parameter.indirect.AgslProtocol;
import org.astrogrid.applications.parameter.indirect.DefaultIndirectionProtocolLibrary;
import org.astrogrid.applications.parameter.indirect.FileProtocol;
import org.astrogrid.applications.parameter.indirect.FtpProtocol;
import org.astrogrid.applications.parameter.indirect.HttpProtocol;
import org.astrogrid.applications.parameter.indirect.IndirectionProtocolLibrary;
import org.astrogrid.applications.parameter.indirect.IvornProtocol;
import org.astrogrid.applications.parameter.indirect.Protocol;
import org.astrogrid.component.EmptyComponentManager;
import org.astrogrid.config.Config;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Startable;

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
 * @see org.astrogrid.applications.component.JavaClassCEAComponentManager for example of how to assemble a server using this class.
 *
 */
public abstract class EmptyCEAComponentManager extends EmptyComponentManager implements CEAComponentManager{
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
    /** dummy component that ensures required componets are registered with the container
     * not interesting, but needs to be publc so that picocontainer can instantiate it */ 
    public static final class VerifyRequiredComponents {
        public VerifyRequiredComponents(ExecutionController ignored,MetadataService ignoredToo, QueryService dontcare) {
        }
    }

 

    // convenience methods for regostering commnly used sets of components
    /** register the default top-level services core of CEA 
     * registers default implementations of {@link QueryService}, {@link MetadataService} and {@link ExecutionController},
     * plus the {@link ApplicationDescriptionEnvironment}
     * */
    protected static final void registerDefaultServices(MutablePicoContainer pico) {
        log.info("Registering default services");
        pico.registerComponentImplementation(ApplicationDescriptionEnvironment.class,ApplicationDescriptionEnvironment.class);
        pico.registerComponentImplementation(ExecutionController.class, DefaultExecutionController.class);
        pico.registerComponentImplementation(MetadataService.class, DefaultMetadataService.class);     
        pico.registerComponentImplementation(QueryService.class,DefaultQueryService.class);   
      
        }
    
    /** registers the default implementaiton of the indirection protocol library 
     * NB: does not register any protocols with the library. These must be added to the container separately
     * any protocols added to the container  wil be detected and added to the library on startup
     * @see #registerStandardIndirectionProtocols(MutablePicoContainer)
     * @see #registerAstrogridIndirectionProtocols(MutablePicoContainer)*/
    protected static final void registerProtocolLibrary(final MutablePicoContainer pico) {
        log.info("Registering default indirection protocol library");
        pico.registerComponentImplementation(IndirectionProtocolLibrary.class,DefaultIndirectionProtocolLibrary.class);
        // on startup (i.e. after everythinig has been registered, this throwaway component will add all known indirection protocols to the library).
        pico.registerComponentInstance(new Startable() {
            public void start() {
                DefaultIndirectionProtocolLibrary lib = (DefaultIndirectionProtocolLibrary) pico.getComponentInstanceOfType(DefaultIndirectionProtocolLibrary.class);
                for (Iterator i = pico.getComponentAdaptersOfType(Protocol.class).iterator(); i.hasNext(); ) {
                    ComponentAdapter ca = (ComponentAdapter)i.next();
                    Protocol p = (Protocol)ca.getComponentInstance();
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
        pico.registerComponentImplementation(AgslProtocol.class);
        pico.registerComponentImplementation(IvornProtocol.class);
    }
    
    /** key used to determing from config where to store execution history files
     * @see #registerDefaultPersistence(MutablePicoContainer, Config) */
    public static final String FILESTORE_BASEDIR = "cea.filestore.basedir";
    
    /** register the standard persistence system - globally unique id generation, and file-based exection history 
     * configured by {@link #FILESTORE_BASEDIR}
     * */
    protected static final void registerDefaultPersistence(MutablePicoContainer pico, final Config config) {
        log.info("Registering default persistence system");
        pico.registerComponentImplementation(ExecutionHistory.class,FileStoreExecutionHistory.class); 
        pico.registerComponentInstance(FileStoreExecutionHistory.StoreDir.class, new FileStoreExecutionHistory.StoreDir(){
            private final File dir= new File(config.getString(FILESTORE_BASEDIR,System.getProperty("java.io.tmpdir")));
            public File getDir() {
                return dir;
            }
        });
        pico.registerComponentImplementation(IdGen.class,GloballyUniqueIdGen.class);        
    }
    /** key to query config for the url of the registry template to use (optional, default='/CEARegistryTemplate.xml' on classpath) 
     * @see #registerDefaultVOProvider(MutablePicoContainer, Config)*/
    public static final String REGISTRY_TEMPLATE_URL  ="cea.registry.template.url";
    /** key to query config for the url of this services endpoint (optional, recommended, otherwise makes a best guess)
     * @see #registerDefaultVOProvider(MutablePicoContainer, Config)*/
    public static final String SERVICE_ENDPOINT_URL = "cea.service.endpoint.url";
    /** register the standard VO Provider - the component that generates the registry entry.
     *  standard provider operates by constructing VODecription from applicationDescriptions in library
     * @param pico
     * @param config
     * @see #REGISTRY_TEMPLATE_URL
     * @see #SERVICE_ENDPOINT_URL
     */
    protected static final void registerDefaultVOProvider(MutablePicoContainer pico, final Config config) {
        log.info("Registering default vo provider");
        try {
        pico.registerComponentImplementation(ProvidesVODescription.class, RegistryEntryBuilder.class);
        pico.registerComponentInstance(RegistryEntryBuilder.URLs.class, new RegistryEntryBuilder.URLs() {
            private final URL registryTemplate = config.getUrl(REGISTRY_TEMPLATE_URL,EmptyCEAComponentManager.class.getResource("/CEARegistryTemplate.xml"));         
            private final URL serviceEndpoint = config.getUrl(SERVICE_ENDPOINT_URL,new URL("http://localhost:8080/astrogrid-cea-server/services/CommonExecutionConnectorService"));
            public URL getRegistryTemplate() {
                return registryTemplate;
            }
            public URL getServiceEndpoint() {
                return serviceEndpoint;
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
                CompositeApplicationDescriptionLibrary uberLib = (CompositeApplicationDescriptionLibrary)pico.getComponentAdapterOfType(CompositeApplicationDescriptionLibrary.class);
                for (Iterator i = pico.getComponentAdaptersOfType(ApplicationDescriptionLibrary.class).iterator(); i.hasNext(); ) {
                    ComponentAdapter ca = (ComponentAdapter)i.next();
                    ApplicationDescriptionLibrary lib = (ApplicationDescriptionLibrary)ca.getComponentInstance();
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
            BaseApplicationDescriptionLibrary lib = (BaseApplicationDescriptionLibrary)pico.getComponentAdapter(BaseApplicationDescriptionLibrary.class);
            for (Iterator i = pico.getComponentAdaptersOfType(ApplicationDescription.class).iterator(); i.hasNext();) {
                ComponentAdapter ca = (ComponentAdapter)i.next();
                ApplicationDescription appDesc = (ApplicationDescription)ca.getComponentInstance();
                lib.addApplicationDescription(appDesc);
            }
        }
        public void stop() {
        }
       }); 
    }

}


/* 
$Log: EmptyCEAComponentManager.java,v $
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