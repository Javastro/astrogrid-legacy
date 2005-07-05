/*$Id: CommandLineCEAComponentManager.java,v 1.6 2005/07/05 08:27:01 clq2 Exp $
 * Created on 04-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.commandline;

import org.astrogrid.applications.commandline.digester.CommandLineApplicationDescriptionFactory;
import org.astrogrid.applications.commandline.digester.CommandLineDescriptionsLoader;
import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.BasicComponentParameter;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;

import java.io.File;
import java.net.URL;

/** Component manager that create a cea server that runs command-line applications
 * @author Noel Winstanley nw@jb.man.ac.uk 04-May-2004
 *
 */
public class CommandLineCEAComponentManager extends EmptyCEAComponentManager  {
    /** configuration keys looked for. */
    /** key to look in config for location of configuration file (required) */
    public static final String DESCRIPTION_URL ="cea.commandline.description.list.url";
    /** key to look in config for location of working dir (optional, defaults to /tmp) */
    public static final String WORKING_DIR = "cea.commandline.workingdir.file";
    
    /** 
     * construct a component manager configured with a working cea server with the commandline application provider.
     */
    public CommandLineCEAComponentManager() {
        super();
        final Config config =   SimpleConfig.getSingleton();        
 
        // base cea server also needs a provider for vodescription - registry entry builder does the job.        
        EmptyCEAComponentManager.registerDefaultVOProvider(pico,config);
        //auto-registration with registry -- appropriate for this cea server.
        /* temporarily commented out - kevin suspects this causes problems in auto-integration
         * when server is restarted, as cea-commandline comes up before registry - so can't register.
         * which sounds fair enought, but kevin thinks this is causing the whole system to hang.
         * @todo - investigate
         * reinstated but the start method is disabled.
         */
        EmptyCEAComponentManager.registerDefaultRegistryUploader(pico);
        
        // now need the other side - the cec manager itself.
        EmptyCEAComponentManager.registerDefaultServices(pico);        
        EmptyCEAComponentManager.registerDefaultPersistence(pico,config);
        // indirection handlers
        EmptyCEAComponentManager.registerProtocolLibrary(pico);
        EmptyCEAComponentManager.registerAstrogridIndirectionProtocols(pico);
        EmptyCEAComponentManager.registerStandardIndirectionProtocols(pico);
        
        //now the Logretriver
        //TODO this is a bit hacky - would be nicer if EmptyCEAComponentManager had fewer statics so that they could be overridden rather than having to unregister component
        pico.unregisterComponent(ApplicationEnvironmentRetriver.class);
        pico.registerComponentImplementation(ApplicationEnvironmentRetriver.class, CommandLineExecutionEnvRetriever.class);
        
        // now the provider
        registerCommandLineProvider(pico,config);
    }
    
    /** register just the components for the commandline provider - none of the generic components */
    public static final void registerCommandLineProvider(MutablePicoContainer pico, final Config config) {
         log.info("registering commandline description loader");
       // stuff required for application descriptions
         pico.registerComponentImplementation(CommandLineDescriptionsLoader.class);
         // configuration for the loader
         pico.registerComponentInstance(CommandLineDescriptionsLoader.DescriptionURL.class,new CommandLineDescriptionsLoader.DescriptionURL() {
             private final URL url = config.getUrl(DESCRIPTION_URL);
             public URL getURL() {
                     return url;
             }
         });
         log.info("registering the commandline application description factory");
         // factory for appDescs - necessary to register parameter to this by hand - as latest version of pico is a bit funny about references to the container itself.
         pico.registerComponentImplementation(CommandLineApplicationDescriptionFactory.class
                 ,CommandLineApplicationDescriptionFactory.class
                 , new Parameter[]{new ConstantParameter(pico)}
                 );
         // 'factory' for environments
         log.info("registering the commandline application environment factory");
         pico.registerComponent( // create a new instance each time.
             new ConstructorInjectionComponentAdapter(CommandLineApplicationEnvironment.class,CommandLineApplicationEnvironment.class));
         // configuration for environments
         pico.registerComponentInstance(CommandLineApplicationEnvironment.WorkingDir.class,new CommandLineApplicationEnvironment.WorkingDir() {
             private final File file = new File(config.getString(WORKING_DIR,System.getProperty("java.io.tmpdir")));
             public File getDir() {
                 return file;
             }
         }); 
          
         // again, create a new one at each call. again, need to pass in pico parameter separately.
         pico.registerComponent(
             new ConstructorInjectionComponentAdapter(CommandLineApplicationDescription.class
                     ,CommandLineApplicationDescription.class
                     , new Parameter[]{new ComponentParameter(ApplicationDescriptionEnvironment.class)
                             ,new ConstantParameter(pico)
                     }                                      
             ));
    }
 
}


/* 
$Log: CommandLineCEAComponentManager.java,v $
Revision 1.6  2005/07/05 08:27:01  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.5.42.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.5.28.2  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.5.28.1  2005/06/02 14:57:29  pah
merge the ProvidesVODescription interface into the MetadataService interface

Revision 1.5  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.4.24.1  2005/03/11 11:21:48  nw
adjusted to fit with pico 1.1

Revision 1.4  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.3.90.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.3  2004/07/09 11:00:41  nw
removed auto-registration component for now

Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:43:39  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:24:18  nw
intermediate version

Revision 1.1.2.1  2004/06/14 08:57:47  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.2  2004/05/28 10:23:11  nw
checked in early, broken version - but it builds and tests (fail)

Revision 1.1.2.1  2004/05/21 12:00:22  nw
merged in latest changes from HEAD. start of refactoring effort
 
*/