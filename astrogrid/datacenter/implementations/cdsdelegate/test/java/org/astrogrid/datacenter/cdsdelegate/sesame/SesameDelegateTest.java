/*$Id: SesameDelegateTest.java,v 1.2 2003/11/20 15:47:18 nw Exp $
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

/** test that exercises methods of sesame delegate
 * just tests that each doesn't return null for now. uncomment println statements to see what the service returns
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Oct-2003
 *
 */
public class SesameDelegateTest extends TestCase {

    /**
     * Constructor for SesameDelegateTest.
     * @param arg0
     */
    public SesameDelegateTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SesameDelegateTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
       ses = new SesameDelegate();
    }

    protected SesameDelegate ses;
    public final static String NAME = "M51";
    public final static String UNKNOWN = "fooble";
    
    public void testResolveName() throws RemoteException {
        String result = ses.resolveName(NAME);
        assertNotNull(result);
    //    System.out.println(result);
    }
    
    public void testResolveUnknown() throws RemoteException {
        String result = ses.resolveName(UNKNOWN);
        assertNotNull(result);
  //      System.out.println(result);
    }
    
    public void testResolveNameXML() throws ParserConfigurationException, SAXException, IOException {
        Document doc = ses.resolveNameToXML(NAME);        
        assertNotNull(doc);
    }
    
    public void testResolveUnknownXML() throws ParserConfigurationException, SAXException, IOException {
        Document doc  = ses.resolveNameToXML(UNKNOWN);
        assertNotNull(doc);
    }
    
    public void testResolveNameHTML() throws RemoteException {
        String result = ses.resolveNameToHTML(NAME);
        assertNotNull(result);
//        System.out.println(result);
    }
    
    public void testResolveUnknownHTML() throws RemoteException {
        String result = ses.resolveNameToHTML(UNKNOWN);
        assertNotNull(result);
      //  System.out.println(result);
    }



}


/* 
$Log: SesameDelegateTest.java,v $
Revision 1.2  2003/11/20 15:47:18  nw
improved testing

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/