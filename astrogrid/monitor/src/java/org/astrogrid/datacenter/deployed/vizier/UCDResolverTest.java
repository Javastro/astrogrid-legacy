/*$Id: UCDResolverTest.java,v 1.1 2004/11/03 05:20:50 mch Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.deployed.vizier;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import junit.framework.TestCase;
import org.astrogrid.datacenter.impl.cds.ucdresolver.InvalidUcdException;
import org.astrogrid.datacenter.impl.cds.ucdresolver.UcdResolverDelegate;

/** Test the ucd resolver web service.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Oct-2003
 *
 */
public class UCDResolverTest extends TestCase {

    /**
     * Constructor for UCDResolverTest.
     * @param arg0
     */
    public UCDResolverTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(UCDResolverTest.class);
    }
    
    protected void setUp() throws ServiceException {
        res = new UcdResolverDelegate();
    }
    
    protected UcdResolverDelegate res;
    
    public void testUCDResolver() throws RemoteException, InvalidUcdException {
        String result = res.resolve("VELOC_WIND");
        assertNotNull(result);
        assertEquals("Wind Velocity",result.trim());
    }
    
    public void testUCDResolverNotKnown() throws RemoteException {
        try {
        String result = res.resolve("fooble");
        System.out.println(result);
        fail("expected to fail");
        } catch (InvalidUcdException e) {
            // ok
        }
    }
    
    public void testUCDResolverEmpty() throws RemoteException {
        try {
        String result = res.resolve("");
        System.out.println(result);
        fail("expected to fail");
        } catch (InvalidUcdException e) {
            // ok
        }
    }
    
    /** you put null in, you get null out */
    public void testUCDResolverNull() throws RemoteException {
        try {
        String result = res.resolve(null);
        assertNull(result);
        fail("expected to fail");
        } catch (InvalidUcdException e) {
            // ok
        }
    }

}


/*
$Log: UCDResolverTest.java,v $
Revision 1.1  2004/11/03 05:20:50  mch
Moved datacenter deployment tests out of standard tests

Revision 1.1.2.1  2004/11/02 21:51:03  mch
Moved deployment tests here - not exactly right either

Revision 1.2  2004/10/05 17:08:22  mch
Fixed package names and imports

Revision 1.1  2004/10/05 17:06:53  mch
Moved service implementation tests from unit tests to integration tests

Revision 1.2  2003/11/20 15:47:18  nw
improved testing

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/
