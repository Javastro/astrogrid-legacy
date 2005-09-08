/*$Id: XQueryRegistryTest.java,v 1.3 2005/09/08 07:30:54 clq2 Exp $
 * Created on 15-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.registry.integration;

import org.astrogrid.registry.RegistryException;

import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;

import java.io.*;

import junit.framework.TestCase;

import java.net.URL;

/**
 * Class: XQueryRegistryTest
 * Description: Small test(s) to test out XQuery searches directly to the registry via the XQuerySearch 
 * web service interface.
 * @author Kevin Benson
 * @author Noel Winstanley
 * @todo assert something in these tests.
 *
 */
public class XQueryRegistryTest extends AbstractTestForRegistry {

    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public XQueryRegistryTest(String arg0) {
        super(arg0);
    }
            
    public void testSearch() throws Exception {
       Document  result = rs.xquerySearch("declare namespace vr = \"http://www.ivoa.net/xml/VOResource/v0.10\"; declare namespace vor=\"http://www.ivoa.net/xml/RegistryInterface/v0.1\"; for $x in //vor:Resource where $x/vr:identifier = 'ivo://org.astrogrid.localhost/org.astrogrid.registry.RegistryService' return $x");
       assertNotNull(result);
       DomHelper.DocumentToStream(result,System.out);
       assertVODescription(result);
    }
    

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

}


/* 
*/