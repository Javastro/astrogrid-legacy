/*
 * $Id: TestRegistryLocator.java,v 1.2 2005/07/05 08:26:57 clq2 Exp $
 * 
 * Created on 07-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.http.test;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import org.astrogrid.applications.description.registry.RegistryQueryLocator;
import org.astrogrid.registry.client.query.RegistryService;

/**
 * @author Paul Harrison (pharriso@eso.org) 07-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class TestRegistryLocator implements RegistryQueryLocator {

   /**
    * 
    */
   public TestRegistryLocator() {
      super();
     }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.description.registry.RegistryQueryLocator#getClient()
    */
   public RegistryService getClient() {
      RegistryService service = null;
     try {
      service = new TestRegistryQuerier(null);
      
   } catch (MarshalException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
   } catch (ValidationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
   }
   return service;
   }

}


/*
 * $Log: TestRegistryLocator.java,v $
 * Revision 1.2  2005/07/05 08:26:57  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/06/08 22:10:46  pah
 * make http applications v10 compliant
 *
 */
