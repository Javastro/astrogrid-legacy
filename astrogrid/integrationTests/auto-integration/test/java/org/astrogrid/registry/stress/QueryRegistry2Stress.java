/*$Id: QueryRegistry2Stress.java,v 1.3 2004/09/22 10:47:25 nw Exp $
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
import org.astrogrid.registry.integration.AbstractTestForRegistry;

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
public class QueryRegistry2Stress extends AbstractTestForRegistry {
     
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public QueryRegistry2Stress(String arg0) {
        super(arg0);
    }
   
   public void testTabularSkyServiceQuery() throws Exception {
      Document queryDoc = askQueryFromFile("TabularSkyServiceQuery.xml");
      Document  result = rs.submitQuery(queryDoc);
      assertNotNull(result);
      DomHelper.DocumentToStream(result,System.out);
      assertNonEmptyVODescription(result);      
   }
       
   protected Document askQueryFromFile(String queryFile) throws Exception {
      assertNotNull(queryFile);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      
      assertNotNull("Could not open query file :" + queryFile,is);
      Document queryDoc = DomHelper.newDocument(is);
      //Document queryDoc = DomHelper.newDocument(new File(queryFile));
      return queryDoc;
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
