/*$Id: HttpApplicationCEAServerTest.java,v 1.13 2006/03/17 17:50:58 clq2 Exp $
 * Created on 30-July-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.http;

import junit.framework.TestCase;

import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.applications.component.EmptyCEAComponentManager;
import org.astrogrid.applications.http.registry.RegistryQuerier;
import org.astrogrid.applications.http.test.TestRegistryQuerier;

/** test of a cea server configured with the javaclass backend.
 * @author jdt/Noel Winstanley nw@jb.man.ac.uk 21-Jun-2004
 * @todo exercise other components of server here..
 *
 */
public class HttpApplicationCEAServerTest extends TestCase {
    /**
     * Constructor 
     * @param arg0
     */
    public HttpApplicationCEAServerTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
    	super.setUp();
    	manager = new HttpApplicationCEAComponentManager();
        //@TODO temp hack to replace the registry querier with our test one
    	manager.getContainer().unregisterComponent(RegistryQuerier.class);
    	manager.getContainer().registerComponentInstance(RegistryQuerier.class, new TestRegistryQuerier(null));
    }
    
    protected CEAComponentManager manager;
    
     public void testIsValid() {
        manager.getContainer().verify();
    }
    
    public void testGetController() {
        assertNotNull(manager.getExecutionController());
    }
    public void testGetMetaData() {
        assertNotNull(manager.getMetadataService());
    }

    public void testInformation() {
        System.out.println(manager.information());
    }    
    
    
}


/* 
$Log: HttpApplicationCEAServerTest.java,v $
Revision 1.13  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.11  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.8.34.1  2005/12/22 13:56:03  gtr
Refactored to match the other kinds of CEC.

Revision 1.8  2005/07/05 08:26:56  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.7.100.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.7.86.1  2005/06/08 22:10:45  pah
make http applications v10 compliant

Revision 1.7  2004/09/10 11:04:22  jdt
Organised imports.

Revision 1.6  2004/09/07 08:13:20  pah
remove incorrect import

Revision 1.5  2004/09/01 16:36:50  jdt
Was failing....perhaps a change in the server code since we branched?

Revision 1.4  2004/09/01 15:42:26  jdt
Merged in Case 3


 
*/