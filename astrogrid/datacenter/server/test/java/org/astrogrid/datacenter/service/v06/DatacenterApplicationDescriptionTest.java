/*$Id: DatacenterApplicationDescriptionTest.java,v 1.2 2004/07/20 02:15:05 nw Exp $
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
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.indirect.DefaultIndirectionProtocolLibrary;
import org.astrogrid.datacenter.service.DataServer;

import junit.framework.TestCase;

/** Creat an application description, exercise the metadata methods, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationDescriptionTest extends TestCase {
    /**
     * Constructor for DatacenterApplicationDescriptionTest.
     * @param arg0
     */
    public DatacenterApplicationDescriptionTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ds = new DataServer();
        env = new ApplicationDescriptionEnvironment(new InMemoryIdGen(),new DefaultIndirectionProtocolLibrary());
        appDesc = new DatacenterApplicationDescription("testdss",ds,env);
    }
    protected ApplicationDescription appDesc;
    protected DataServer ds;
    protected ApplicationDescriptionEnvironment env;
    
    public void testName() {
        assertNotNull(appDesc.getName());
    }
    
    public void testMetadata() throws ParameterNotInInterfaceException {
        ApplicationInterface[] ifaces = appDesc.getInterfaces();
        assertEquals(2,ifaces.length);
        for (int i = 0; i < ifaces.length; i++) {
            ApplicationInterface iface = ifaces[i];
            // minimum for all interfaces.
            assertNotNull(iface.getName());
            assertNotNull(iface.getInputParameter(DatacenterApplicationDescription.FORMAT)); // all need a format parameter
            assertNotNull(iface.getOutputParameter(DatacenterApplicationDescription.RESULT)); // all need a result.
        }
    }
}


/* 
$Log: DatacenterApplicationDescriptionTest.java,v $
Revision 1.2  2004/07/20 02:15:05  nw
final implementaiton of itn06 Datacenter CEA interface

Revision 1.1  2004/07/13 17:11:32  nw
first draft of an itn06 CEA implementation for datacenter
 
*/