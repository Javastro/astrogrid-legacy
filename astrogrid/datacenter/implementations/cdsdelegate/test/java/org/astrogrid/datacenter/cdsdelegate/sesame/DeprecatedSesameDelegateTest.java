/*$Id: DeprecatedSesameDelegateTest.java,v 1.1 2003/11/18 11:23:49 nw Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cdsdelegate.sesame;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Oct-2003
 *
 */
public class DeprecatedSesameDelegateTest extends TestCase {

    /**
     * Constructor for DeprecatedSesameDelegateTest.
     * @param arg0
     */
    public DeprecatedSesameDelegateTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DeprecatedSesameDelegateTest.class);
    }
    
    protected void setUp() throws Exception {
        del = new DeprecatedSesameDelegate();
    }
    public final static String NAME = "M51";
    public final static String UNKNOWN = "fooble";
    protected DeprecatedSesameDelegate del;

    public void testResolve() throws RemoteException {
        String result = del.resolveName(NAME);
        assertNotNull(result);
        System.out.println(result);
    }
    
    public void testResolveUnknown() throws RemoteException {
        String result = del.resolveName(UNKNOWN);
        assertNotNull(result);
        System.out.println(result);
    }
    
    public void testResolveXML() throws ParserConfigurationException, SAXException, IOException {
        Document doc  = del.resolveNameAsXML(NAME);
        assertNotNull(doc);
        assertEquals("Sesame",doc.getDocumentElement().getLocalName());
        // check it fits schema..
    }
    
    public void testResolveXMLUnknown() throws ParserConfigurationException, SAXException, IOException {
        Document doc = del.resolveNameAsXML(UNKNOWN);
        assertNotNull(doc);
        assertEquals("Sesame",doc.getDocumentElement().getLocalName());
        // check with schema
    }
        

}


/* 
$Log: DeprecatedSesameDelegateTest.java,v $
Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/