/*$Id: CommandLineApplicationDescriptionTest.java,v 1.2 2004/07/01 11:07:59 nw Exp $
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
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.indirect.DefaultIndirectionProtocolLibrary;
import org.astrogrid.applications.parameter.indirect.IndirectionProtocolLibrary;
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
        fac = new CommandLineApplicationEnvironment(idgen,new CommandLineApplicationEnvironment.WorkingDir() {
            final File file; 
            { 
                file = File.createTempFile("CommandLineApplicationDescriptionTest",null);
                file.delete();
                file.mkdirs();
                file.deleteOnExit();
            }
            public File getDir() {
                return file; 
            }
        });
        DefaultPicoContainer container = new DefaultPicoContainer();
        container.registerComponentInstance(fac);
        IndirectionProtocolLibrary lib = new DefaultIndirectionProtocolLibrary();
        container.registerComponentInstance(lib);
        appDescEnv = new ApplicationDescriptionEnvironment(idgen,lib);
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