/*$Id: UCDResolverTest.java,v 1.1 2003/11/18 11:23:49 nw Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cdsdelegate.ucdresolver;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import junit.framework.TestCase;

/**
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
Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/