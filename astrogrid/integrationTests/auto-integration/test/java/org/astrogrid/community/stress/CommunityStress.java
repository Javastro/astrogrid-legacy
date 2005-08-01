/*$Id: CommunityStress.java,v 1.4 2005/08/01 08:15:52 clq2 Exp $
 * Created on 15-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.community.stress;

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
public class CommunityStress extends StressTester {
    
    public CommunityStress(String arg0) {
        this(arg0,"community_stress.xml");
    }
    
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public CommunityStress(String arg0, String stressConfig) {
        super(arg0,stressConfig);
        System.out.println("stressconfig = " + stressConfig);
    }

    /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      System.out.println("finished setup of CommunityStress");
   }  
   
   public void testNothing() throws Exception {
       System.out.println("do nothing");
   }

}

/* 
*/