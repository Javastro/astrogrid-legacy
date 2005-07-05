/*
 * $Id: IvornUtilTest.java,v 1.2 2005/07/05 08:27:00 clq2 Exp $
 * 
 * Created on 30-May-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.registry;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pharriso@eso.org) 30-May-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class IvornUtilTest extends TestCase {

   private final String ivorn="ivo://authority.org/id/part";
   public static void main(String[] args) {
      junit.textui.TestRunner.run(IvornUtilTest.class);
   }

   public void testExtractIDFragment() {
      String result = IvornUtil.extractIDFragment(ivorn);
      assertEquals("id", result, "id/part");
   }

   public void testExtractAuthorityFragment() {
      String result = IvornUtil.extractAuthorityFragment(ivorn);
      assertEquals("id", result, "authority.org");
   }

   public void testRemoveProtocol() {
      String result = IvornUtil.removeProtocol(ivorn);
      assertEquals("id", result, "authority.org/id/part");
      
   }

}


/*
 * $Log: IvornUtilTest.java,v $
 * Revision 1.2  2005/07/05 08:27:00  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/05/31 12:58:26  pah
 * moved to v10 schema interpretation - this means that the authorityID is read directly with the applicaion "name"
 *
 */
