/*
 * $Id: AbstractCommandLineEnvironmentTestCase.java,v 1.2 2008/09/03 12:22:54 pah Exp $
 * 
 * Created on 20-Jul-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import java.io.File;

import junit.framework.TestCase;

import org.astrogrid.applications.contracts.CEAConfiguration;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.io.FileUtils;

/**
 * @author Paul Harrison (pharriso@eso.org) 20-Jul-2005
 * @version $Name:  $
 * @since initial Coding
 */
public abstract class AbstractCommandLineEnvironmentTestCase extends TestCase {

    protected ApplicationEnvironment env;
    protected File workingDir;
    protected CEAConfiguration configuration;
    private IdGen idgen;

    /**
     * 
     */
    public AbstractCommandLineEnvironmentTestCase() {
	super();
	// TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public AbstractCommandLineEnvironmentTestCase(String arg0) {
	super(arg0);
	// TODO Auto-generated constructor stub
    }

    protected void setUp() throws Exception {
	super.setUp();
	this.configuration = new CEAConfiguration();
	configuration.setRecordsDirectory(FileUtils.makeTemporaryDirectory());
	configuration.setTemporaryFilesDirectory(FileUtils.makeTemporaryDirectory());
	idgen = new InMemoryIdGen();
	ProtocolLibrary lib = new DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
	AppAuthorityIDResolver resolver = new TestAuthorityResolver();

	//TODO fill more of the configuration with suitable defaults
	env = new CommandLineApplicationEnvironment("userAssignedId",new User(), idgen, this.configuration);
	assertNotNull(env);


    }

    @Override
    protected void tearDown() throws Exception {
	//clean up some directories
	FileUtils.forceDeleteDirectory(configuration.getRecordsDirectory());
	FileUtils.forceDeleteDirectory(configuration.getTemporaryFilesDirectory());
    }


}


/*
 * $Log: AbstractCommandLineEnvironmentTestCase.java,v $
 * Revision 1.2  2008/09/03 12:22:54  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1  2008/08/29 07:28:28  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.7.56.4  2008/08/02 13:32:23  pah
 * safety checkin - on vacation
 *
 * Revision 1.7.56.3  2008/06/11 14:32:48  pah
 * merged the ids into the application execution environment
 *
 * Revision 1.7.56.2  2008/04/17 16:16:55  pah
 * removed all castor marshalling - even in the web service layer - unit tests passing
 * some uws functionality present - just the bare bones.
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 * ASSIGNED - bug 2739: remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 *
 * Revision 1.7.56.1  2008/04/08 14:04:11  pah
 * Completed move to using spring as container for webapp - replaced picocontainer
 *
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 *
 * Revision 1.7  2006/03/17 17:50:58  clq2
 * gtr_1489_cea correted version
 *
 * Revision 1.5  2006/03/07 21:45:26  clq2
 * gtr_1489_cea
 *
 * Revision 1.2.20.2  2006/01/26 13:16:34  gtr
 * BasicCommandLineConfiguration has absorbed the functions of TestCommandLineConfiguration.
 *
 * Revision 1.2.20.1  2005/12/19 18:12:30  gtr
 * Refactored: changes in support of the fix for 1492.
 *
 * Revision 1.2  2005/08/10 14:45:37  clq2
 * cea_pah_1317
 *
 * Revision 1.1.2.1  2005/07/21 15:12:06  pah
 * added workfile deletion
 *
 */
