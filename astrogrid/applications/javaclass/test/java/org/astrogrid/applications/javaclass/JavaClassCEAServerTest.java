/*$Id: JavaClassCEAServerTest.java,v 1.2 2007/02/19 16:20:21 gtr Exp $
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

import org.w3c.dom.Document;

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
        container.start();
    }
    
    protected CEAComponentManager container;
    
    public void testMetadata() throws Exception {
        MetadataService ms = container.getMetadataService();
        assertNotNull(ms);
        Document reg = ms.returnRegistryEntry();
        assertNotNull(reg);
        
    }
    
    
}


/* 
$Log: JavaClassCEAServerTest.java,v $
Revision 1.2  2007/02/19 16:20:21  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:29:17  gtr
no message

Revision 1.4  2005/07/05 08:27:01  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.3.140.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.3.126.1  2005/06/02 14:57:29  pah
merge the ProvidesVODescription interface into the MetadataService interface

Revision 1.3  2004/08/11 16:54:50  nw
must start the container, before testing it.

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:42:46  nw
final version, before merge
 
*/