/*$Id: QueryRegistryStress.java,v 1.1 2004/09/16 11:18:15 KevinBenson Exp $
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

import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;

import java.io.*;

import junit.framework.TestCase;

import java.net.URL;

/**
 * @author Kevin Benson
 * @author Noel Winstanley
 * @author Paul Harrison pah@jb.man.ac.uk 07-May-2004
 * @author Roy Platon rtp@rl.ac.uk 16-Aug-2004
 * 
 *
 */
public class QueryRegistryStress extends RegistryBaseTest {
     
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public QueryRegistryStress(String arg0) {
        super(arg0);
    }
            
   public void testResourceByIdentifier() throws Exception {
      Document result =
         rs.getResourceByIdentifier("org.astrogrid.localhost/noaa_trace");
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);
      assertSingletonVODescription(result);      
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
