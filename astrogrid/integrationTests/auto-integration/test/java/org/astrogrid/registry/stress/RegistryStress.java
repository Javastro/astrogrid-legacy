/*$Id: RegistryStress.java,v 1.1 2004/09/16 11:18:15 KevinBenson Exp $
 * Created on 15-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.registry.stress;

import org.astrogrid.registry.RegistryException;

import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import java.util.Date;
import java.util.Calendar;

import java.util.HashMap;
import java.util.List;

import java.io.*;

import junit.framework.TestCase;

import java.net.URL;
import org.astrogrid.stress.StressTester;

/**
 * @author Kevin Benson
 * @author Noel Winstanley 
 *
 */
public class RegistryStress extends StressTester {
    
    public RegistryStress(String arg0) {
        this(arg0,"registry_stress.xml");
    }
    
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public RegistryStress(String arg0, String stressConfig) {
        super(arg0,stressConfig);
        System.out.println("stressconfig = " + stressConfig);
    }

    /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      System.out.println("finished setup of simplequerystresstest");
   }  
   
   public void testNothing() throws Exception {
       System.out.println("do nothing");
   }

}

/* 
*/