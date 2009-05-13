/*$Id: DatacenterApplicationDescriptionTest.java,v 1.1 2009/05/13 13:20:59 gtr Exp $
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

import EDU.oswego.cs.dl.util.concurrent.DirectExecutor;
import junit.framework.TestCase;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.cea.DatacenterApplicationDescription;

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
        env = new ApplicationDescriptionEnvironment(
           new InMemoryIdGen(),
           new DefaultProtocolLibrary(),
           new AppAuthorityIDResolver() {
                     public String getAuthorityID() {
                        return "org.astrogrid.test";
                     }
               });
          
        appDesc = new DatacenterApplicationDescription("testdss",ds,env,new DirectExecutor());
    }
    protected ApplicationDescription appDesc;
    protected DataServer ds;
    protected ApplicationDescriptionEnvironment env;
    
    public void testName() {
        assertNotNull(appDesc.getName());
    }
    
    public void testMetadata() throws ParameterNotInInterfaceException {
        ApplicationInterface[] ifaces = appDesc.getInterfaces();
        assertEquals("Unexpected number of interfaces (cone search may be disabled) ",3,ifaces.length);
        for (int i = 0; i < ifaces.length; i++) {
            ApplicationInterface iface = ifaces[i];
            // minimum for all interfaces.
            assertNotNull(iface.getName());
            if (DatacenterApplicationDescription.MULTICONE_IFACE.equals(iface.getName())) {
               assertNotNull(iface.getInputParameter(DatacenterApplicationDescription.INPUT_VOTABLE)); 
            } 
            else if (DatacenterApplicationDescription.CONE_IFACE.equals(iface.getName())) {
               assertNotNull(iface.getInputParameter(DatacenterApplicationDescription.RA)); 
            }
            else if (DatacenterApplicationDescription.ADQL_IFACE.equals(iface.getName())) {
               assertNotNull(iface.getInputParameter(DatacenterApplicationDescription.QUERY)); 
            }
            assertNotNull(iface.getOutputParameter(DatacenterApplicationDescription.RESULT)); // all need a result.
        }
    }
}


/*
$Log: DatacenterApplicationDescriptionTest.java,v $
Revision 1.1  2009/05/13 13:20:59  gtr
*** empty log message ***

Revision 1.8  2008/02/07 17:27:45  clq2
PAL_KEA_2518

Revision 1.7.12.1  2008/02/07 16:36:16  kea
Further fixes for 1.0 support, and also MBT's changes merged into my branch.

Revision 1.7  2007/10/17 09:58:20  clq2
PAL_KEA-2314

Revision 1.6.46.1  2007/09/25 17:17:30  kea
Working on CEA interface for multicone service.

Revision 1.6  2006/03/17 17:56:58  clq2
gtr_1489_cea correted version

Revision 1.4  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.3.58.1  2006/01/30 11:39:22  gtr
I corrected the use of the AppAuthorityResolver interface to match the new CEA code.

Revision 1.3  2005/05/27 16:21:03  clq2
mchv_1

Revision 1.2.24.1  2005/05/03 19:35:01  mch
fixes to tests

Revision 1.2  2005/02/28 18:47:05  mch
More compile fixes

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:25  mch
Initial checkin

Revision 1.2.8.1  2005/01/13 18:57:31  mch
Fixes to metadata mostly

Revision 1.2  2004/11/09 17:42:22  mch
Fixes to tests after fixes for demos, incl adding closable to targetIndicators

Revision 1.1  2004/09/28 15:11:33  mch
Moved server test directory to pal

Revision 1.4  2004/09/17 01:27:06  nw
added thread management.

Revision 1.3  2004/07/27 13:48:33  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package

Revision 1.2  2004/07/20 02:15:05  nw
final implementaiton of itn06 Datacenter CEA interface

Revision 1.1  2004/07/13 17:11:32  nw
first draft of an itn06 CEA implementation for datacenter
 
*/
