/*$Id: JavaClassCEAServerTest.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 21-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.javaclass;

import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.applications.component.JavaClassCEAComponentManager;
import org.astrogrid.applications.manager.MetadataService;

import junit.framework.TestCase;

/** test of a cea server configured with the javaclass backend.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Jun-2004
 * @todo exercise other components of server here..
 *
 */
public class JavaClassCEAServerTest extends TestCase {
    /**
     * Constructor for JavaClassCEAServerTest.
     * @param arg0
     */
    public JavaClassCEAServerTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        container = new JavaClassCEAComponentManager();
    }
    
    protected CEAComponentManager container;
    
    public void testMetadata() throws Exception {
        MetadataService ms = container.getMetadataService();
        assertNotNull(ms);
        String reg = ms.returnRegistryEntry();
        assertNotNull(reg);
        
    }
    
    
}


/* 
$Log: JavaClassCEAServerTest.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:42:46  nw
final version, before merge
 
*/