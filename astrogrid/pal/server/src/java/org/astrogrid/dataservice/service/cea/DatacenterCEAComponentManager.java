/*$Id: DatacenterCEAComponentManager.java,v 1.12 2008/10/13 10:51:35 clq2 Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.dataservice.service.cea;

import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.manager.BaseConfiguration;
import org.astrogrid.applications.manager.idgen.GloballyUniqueIdGen;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.InMemoryExecutionHistory;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.service.DataServer;
import org.picocontainer.MutablePicoContainer;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;


/** Component manager implementation that assembles a CEA server which provides a single {@link DatacetnerApplicationDescription} for the
 * datacenter application
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterCEAComponentManager extends EmptyCEAComponentManager {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DatacenterCEAComponentManager.class);

    /** Construct a new DatacenterCEAComponentManager
     * @TOFIX : are the default implementations of various new interfaces
     * adequate here?  Ask Paul.
     */
    public DatacenterCEAComponentManager() {
        super();
        final Config config = SimpleConfig.getSingleton();
        // controller & queriers
        //CLQ bug 1359 relace the following line with registerDefaultServices(pico); to accomodate changed cea class.
        //EmptyCEAComponentManager.registerDefaultServices(pico);
        registerDefaultServices(pico);
        
        // Force the CEC to keep its job receords in memory. This replaces
        // the more-common call to registerDefaultPersistence() which sets
        // file-based persistence of records. Given the recent rearrangement
        // of confiiguration in the base CEC, it may now be reasonable to go
        // back to file-based persistence. -- GTR 2006-01-30
        pico.unregisterComponent(ExecutionHistory.class);
        pico.registerComponentImplementation(ExecutionHistory.class,InMemoryExecutionHistory.class);
        pico.unregisterComponent(IdGen.class);
        pico.registerComponentImplementation(IdGen.class,GloballyUniqueIdGen.class);
        
        EmptyCEAComponentManager.registerDefaultVOProvider(pico);

        // the protocol lib
        // KEA NOTE : These are now registered in 
        //    EmptyCEAComponentManager.registerDefaultServices
        // Must remove them here or pico gets upset about duplicate calls
        //EmptyCEAComponentManager.registerProtocolLibrary(pico);
        //EmptyCEAComponentManager.registerStandardIndirectionProtocols(pico);
        //EmptyCEAComponentManager.registerAstrogridIndirectionProtocols(pico);
        
        // Added by KEA to fix broken unit test after CEA changes.
        // TOFIX: are the default implementations adequate here?
        //DefaultQueryServicesregister
        
        // need input from Paul.
        //
        registerDatacenterProvider(pico,config);
    }
    
    /** key used to lookup in config the name of the datacenter application.  Must
     * equal with the ResourceKey given in the managed applications. */
    //public static final String DS_APP_NAME_KEY = "datacenter.cea.app.name";
    /** default value for {@link #DS_APP_NAME} if not set in config */
    //public static final String DS_APP_NAME_DEFAULT = "org.astrogrid.localhost/testdsa";
    
    /** register the datacenter-specific components of this cea server */
    public static void registerDatacenterProvider(MutablePicoContainer pico, Config config) {
        logger.info("Registering Datacenter CEA Provider");
        pico.registerComponentImplementation(DatacenterApplicationDescriptionLibrary.class,DatacenterApplicationDescriptionLibrary.class);
        pico.registerComponentImplementation(DataServer.class,DataServer.class);
        pico.registerComponentImplementation(QueuedExecutor.class,CeaQueuedExecutor.class);
        pico.registerComponentImplementation(Configuration.class, BaseConfiguration.class);
        
        // This is the generic protocol for VOSpace. Eventually, the CEA core
        // will include this by default, at which point this line has to be
        // taken out of DSA. We're superimposing it on an old version of the CEA 
        // core.
        pico.registerComponentImplementation(VosProtocol.class);
    }
    
}


/*
$Log: DatacenterCEAComponentManager.java,v $
Revision 1.12  2008/10/13 10:51:35  clq2
PAL_KEA_2799

Revision 1.11.36.1  2008/09/11 15:22:54  gtr
It now adds a protocol module for vos://.

Revision 1.11  2007/09/07 09:30:51  clq2
PAL_KEA_2235

Revision 1.10.40.1  2007/09/04 08:41:37  kea
Fixing v1.0 registrations and multi-catalog CEA stuff.

Revision 1.10  2006/03/22 09:56:14  kea
Bugfix.

Revision 1.9  2006/03/21 17:20:37  clq2
missed out bits

Revision 1.5.10.2  2006/03/21 16:43:20  gtr
*** empty log message ***

Revision 1.5.10.1  2006/01/30 11:25:18  gtr
I changed the call to registerDefaultServices()  to the modern form (doesn't take an org.astrogrid.config.Config argument) and removed Noel's fix of registerDummyControlService() which isn't needed with the revised CEA.

Revision 1.5  2006/01/10 14:13:13  nw
added dumy implementation of missinig service

Revision 1.4  2005/12/07 15:55:21  clq2
KEA-PAL-1471

Revision 1.3.4.1  2005/11/25 16:06:08  kea
Fixes for incorrect registry xml (fixing namespace prefixes, namespaces) - see bug 1450

Revision 1.3  2005/11/21 12:54:18  clq2
DSA_KEA_1451

Revision 1.2.10.1  2005/11/15 15:34:45  kea
Removed duplicate calls to protocol registration stuff (now occurring
in base class and pico didn't like duplication).
NOTE: This class is currently broken according to unit tests.

Revision 1.2  2005/09/07 08:54:42  clq2
changes for cea static method changing

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.4  2004/11/08 23:15:38  mch
Various fixes for SC demo, more store browser, more Vizier stuff

Revision 1.3  2004/11/08 02:58:44  mch
Various fixes and better error messages

Revision 1.2  2004/11/04 15:04:29  mch
Removed default cea name so we know we need to set it

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.3  2004/09/17 01:27:21  nw
added thread management.

Revision 1.2  2004/07/20 02:14:48  nw
final implementaiton of itn06 Datacenter CEA interface

Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/
