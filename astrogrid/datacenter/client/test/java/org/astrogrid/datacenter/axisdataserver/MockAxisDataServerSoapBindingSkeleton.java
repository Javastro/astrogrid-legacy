/*$Id: MockAxisDataServerSoapBindingSkeleton.java,v 1.1 2003/11/17 12:12:28 nw Exp $
 * Created on 16-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.axisdataserver;

/** Mock Implementation of the AxisDataServer
 * - subclasses the standard skeleton, creates a {@link MockServer} that will handle the requests.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Nov-2003
 *
 */
public class MockAxisDataServerSoapBindingSkeleton
    extends AxisDataServerSoapBindingSkeleton {

    /**
     * 
     */
    public MockAxisDataServerSoapBindingSkeleton() {        
        super ( (AxisDataServer) MockServer.createMockServer(AxisDataServer.class));
    }

    /**
     * @param impl
     */
    public MockAxisDataServerSoapBindingSkeleton(AxisDataServer impl) {
        super(impl);

    }

}


/* 
$Log: MockAxisDataServerSoapBindingSkeleton.java,v $
Revision 1.1  2003/11/17 12:12:28  nw
first stab at mavenizing the subprojects.
 
*/