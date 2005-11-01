/*$Id: WorkbenchCeaComponentManager.java,v 1.1 2005/11/01 09:19:46 nw Exp $
 * Created on 19-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.astrogrid.acr.system.Configuration;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.CeaThreadPool;
import org.astrogrid.applications.manager.DefaultApplicationEnvironmentRetriever;
import org.astrogrid.applications.manager.DefaultQueryService;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.ThreadPoolExecutionController;
import org.astrogrid.applications.manager.idgen.GloballyUniqueIdGen;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.FileStoreExecutionHistory;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.FtpProtocol;
import org.astrogrid.applications.parameter.protocol.HttpProtocol;
import org.astrogrid.applications.parameter.protocol.IvornProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.desktop.modules.system.ConfigurationKeys;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Startable;
import org.picocontainer.defaults.DefaultPicoContainer;

import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

import java.io.File;
import java.util.Iterator;

/** Custom cea component manager - just the things needed for an in-process cea server.
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Oct-2005
 * @todo add method to delete unnneeded applications
 */
public class WorkbenchCeaComponentManager implements TasksInternal, Startable{

    private final MutablePicoContainer pico;
    public WorkbenchCeaComponentManager(MutablePicoContainer parent, final Configuration configuration) { 
        super();
        this.pico = new DefaultPicoContainer(parent);
        pico.registerComponentImplementation(ApplicationDescriptionEnvironment.class,ApplicationDescriptionEnvironment.class);
        pico.registerComponentImplementation(ApplicationEnvironmentRetriver.class, DefaultApplicationEnvironmentRetriever.class);
        pico.registerComponentImplementation(ExecutionController.class, MessagingExecutionController.class);
        pico.registerComponentInstance(new PooledExecutor(new LinkedQueue(),4)) ; // 4-thread queue. @todo check this is configured enough - compare to use in astroscope launcher.
        pico.registerComponentImplementation(QueryService.class,DefaultQueryService.class);
        
        // protocol library - necessary, but not using it.
        pico.registerComponentImplementation(ProtocolLibrary.class,DefaultProtocolLibrary.class);

        // persistence
        pico.registerComponentImplementation(ExecutionHistory.class,FileStoreExecutionHistory.class);
        pico.registerComponentInstance(FileStoreExecutionHistory.StoreDir.class, new FileStoreExecutionHistory.StoreDir(){
            private final File dir= new File(new File(configuration.getKey(ConfigurationKeys.WORK_DIR_KEY)),"cea");
            public File getDir() {
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                return dir;
            }
        });    

        pico.registerComponentInstance(new IdGen(){
            public String getNewID() {
                return Long.toString(System.currentTimeMillis()); // simple  - don't need to be globally unique.
            }
        });
        
        pico.registerComponentImplementation(BestMatchApplicationDescriptionLibrary.class);
        pico.registerComponentInstance(new BaseApplicationDescriptionLibrary.AppAuthorityIDResolver() {
            public String getAuthorityID() {
                return "in-process";
            }
        });        
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
             
        pico.registerComponentImplementation(SiapApplicationDescription.class);
        pico.registerComponentImplementation(ConeApplicationDescription.class);        
    }

    public ExecutionController getExecutionController() {
        return (ExecutionController)pico.getComponentInstanceOfType(ExecutionController.class);
        
    }

    public QueryService getQueryService() {
        return (QueryService)pico.getComponentInstanceOfType(QueryService.class);
        
    }
    
    public BestMatchApplicationDescriptionLibrary getAppLibrary() {
        return (BestMatchApplicationDescriptionLibrary) pico.getComponentInstance(BestMatchApplicationDescriptionLibrary.class);
    }

    public void start() {
        pico.start();
    }


    public void stop() {
        pico.stop();
    }
    
}


/* 
$Log: WorkbenchCeaComponentManager.java,v $
Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/