/*$Id: FileApplicationRegistryTest.java,v 1.4 2004/11/11 00:54:18 clq2 Exp $
 * Created on 10-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;

import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationDescriptionSummary;

import java.net.URL;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2004
 *
 */
public class FileApplicationRegistryTest extends TestCase {
    /**
     * Constructor for FileApplicationRegistryTest.
     * @param arg0
     */
    public FileApplicationRegistryTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        reg = new FileApplicationRegistry(TEST_DOCUMENT_URL);
        assertNotNull(reg);
    }
    
    public static final URL TEST_DOCUMENT_URL = FileApplicationRegistryTest.class.getResource("test-tool-list.xml");
    
    protected FileApplicationRegistry reg;
    
    public void testRegistry() throws Exception{
        String[] names = reg.listApplications();
        assertNotNull(names);
        assertTrue(names.length > 0);
        for (int i = 0; i < names.length; i++) {
            ApplicationDescription descr = reg.getDescriptionFor(names[i]);
            assertNotNull(descr);
            assertEquals(names[i],descr.getName());
            assertNull(descr.getOriginalVODescription()); // expect this to be null, as hasn't come rom the registry.
        }
    }
    
    public void testListUIApplications() throws Exception {
        ApplicationDescriptionSummary[] results = reg.listUIApplications();
        assertNotNull(results);
        assertTrue(results.length > 0);
    }
}


/* 
$Log: FileApplicationRegistryTest.java,v $
Revision 1.4  2004/11/11 00:54:18  clq2
nww's bug590

Revision 1.3.4.1  2004/11/10 13:33:45  nw
added new method to ApplicationRegistry - listUIApplications

Revision 1.3  2004/11/08 18:05:15  jdt
Merges from branch nww-bz#590

Revision 1.2.110.1  2004/10/28 14:53:50  nw
added method getOriginalVODescription() to ApplicationDescription,
adjusted RegistryApplicationRegistry to populate this field.

Revision 1.2  2004/03/11 13:53:51  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.1  2004/03/11 13:37:52  nw
tests for impls
 
*/