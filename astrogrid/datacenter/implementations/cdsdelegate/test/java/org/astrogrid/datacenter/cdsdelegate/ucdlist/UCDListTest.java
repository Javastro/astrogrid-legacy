/*$Id: UCDListTest.java,v 1.2 2003/11/20 15:47:18 nw Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cdsdelegate.ucdlist;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import junit.framework.TestCase;

/** Test the UCDList web server - returns a list of all known UCDs, HTML formatted.. (why not just put in on a web page :)
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Oct-2003
 *
 */
public class UCDListTest extends TestCase {

    /**
     * Constructor for UCDListTest.
     * @param arg0
     */
    public UCDListTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(UCDListTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testUCDList() throws ServiceException, RemoteException {
        SesameService serv = new SesameServiceLocator();
        UCDList service = serv.getUCDList();
        assertNotNull(service);
        String result = service.UCDList();
        assertNotNull(result);
        assertTrue(result.length() > 0);
        //System.out.println(result);
    }

}


/* 
$Log: UCDListTest.java,v $
Revision 1.2  2003/11/20 15:47:18  nw
improved testing

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/