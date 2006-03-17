/*$Id: CommandLineApplicationDescriptionTest.java,v 1.10 2006/03/17 17:50:58 clq2 Exp $
 * Created on 27-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.commandline;


import org.astrogrid.applications.Application;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.manager.TestAppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;

import org.picocontainer.defaults.DefaultPicoContainer;

import java.io.File;

import junit.framework.TestCase;

/** Test some aspects of the commandLineApplicationDescription - the creation of commandLineApplications and custom application classes
 * @author Noel Winstanley nw@jb.man.ac.uk 27-May-2004
 *
 */
public class CommandLineApplicationDescriptionTest extends TestCase {
    /**
     * Constructor for CommandLineApplicationDescriptionTest.
     * @param arg0
     */
    public CommandLineApplicationDescriptionTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        IdGen idgen = new InMemoryIdGen();
        BasicCommandLineConfiguration config = new BasicCommandLineConfiguration();
        fac = new CommandLineApplicationEnvironment(idgen, config);
        
        DefaultPicoContainer container = new DefaultPicoContainer();
        container.registerComponentInstance(fac);
        ProtocolLibrary lib = new DefaultProtocolLibrary();
        container.registerComponentInstance(lib);
        //FIXME need to think about how the cmdline apps have their authorityID set....
        appDescEnv = new ApplicationDescriptionEnvironment(idgen,
                                                           lib, 
                                                           new TestAppAuthorityIDResolver("org.astrogrid.test"));
        descr = new CommandLineApplicationDescription(appDescEnv,container);
        
        descr.setName("test");
        ApplicationInterface appInterface = new BaseApplicationInterface("bar",descr);
        descr.addInterface(appInterface);
        
        tool = new Tool();
        tool.setInterface("bar");
    }
    protected ApplicationDescriptionEnvironment appDescEnv;
    protected CommandLineApplicationEnvironment fac;
    protected CommandLineApplicationDescription descr;
    protected User user = new User();
    protected Tool tool;
    
    public void testCreateCustomApplication() throws Exception{
        descr.setInstanceClass(TestApp.class.getName());
        Application app = descr.initializeApplication("foo",user,tool);
        assertNotNull(app);
        assertTrue(app instanceof TestApp);
        
    }
    
    public void testCreateDefaultApplication() throws Exception {
        Application app = descr.initializeApplication("foo",user,tool);
        assertNotNull(app);
        assertEquals(CommandLineApplication.class,app.getClass()); 
    }
}


/* 
$Log: CommandLineApplicationDescriptionTest.java,v $
Revision 1.10  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.8  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.5.34.3  2006/01/26 14:36:07  gtr
I removed dead import statements.

Revision 1.5.34.2  2006/01/26 13:16:34  gtr
BasicCommandLineConfiguration has absorbed the functions of TestCommandLineConfiguration.

Revision 1.5.34.1  2005/12/19 18:12:30  gtr
Refactored: changes in support of the fix for 1492.

Revision 1.5  2005/07/05 08:26:56  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.4.66.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.4.52.1  2005/06/02 14:57:29  pah
merge the ProvidesVODescription interface into the MetadataService interface

Revision 1.4  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.3.70.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.3  2004/07/26 12:03:33  nw
updated to match name changes in cea server library

Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:43:39  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:57:48  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:11  nw
checked in early, broken version - but it builds and tests (fail)
 
*/