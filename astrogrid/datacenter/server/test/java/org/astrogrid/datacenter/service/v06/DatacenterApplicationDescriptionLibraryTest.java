/*$Id: DatacenterApplicationDescriptionLibraryTest.java,v 1.1 2004/07/13 17:11:32 nw Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.service.v06;

import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.indirect.DefaultIndirectionProtocolLibrary;
import org.astrogrid.applications.parameter.indirect.IndirectionProtocolLibrary;
import org.astrogrid.datacenter.service.DataServer;

import junit.framework.TestCase;

/** Test the application description library - not much to do here..
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationDescriptionLibraryTest extends TestCase {
    /**
     * Constructor for DatacenterApplicationDescriptionLibraryTest.
     * @param arg0
     */
    public DatacenterApplicationDescriptionLibraryTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        dataServer = new DataServer();
        env = new ApplicationDescriptionEnvironment(new InMemoryIdGen(),new DefaultIndirectionProtocolLibrary());
        lib = new DatacenterApplicationDescriptionLibrary(dataServer,env);
    }
    protected ApplicationDescriptionLibrary lib;
    protected DataServer dataServer;
    protected ApplicationDescriptionEnvironment env;


    public void testContents() throws ApplicationDescriptionNotFoundException {
        String[] names = lib.getApplicationNames();
        assertEquals(1,names.length);
        ApplicationDescription appDesc = lib.getDescription(names[0]);
        assertNotNull(appDesc);       
    }
}
/* 
$Log: DatacenterApplicationDescriptionLibraryTest.java,v $
Revision 1.1  2004/07/13 17:11:32  nw
first draft of an itn06 CEA implementation for datacenter
 
*/