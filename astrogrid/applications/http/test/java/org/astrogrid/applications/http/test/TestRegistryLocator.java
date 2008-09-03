/*
 * $Id: TestRegistryLocator.java,v 1.3 2008/09/03 14:18:50 pah Exp $
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

import javax.xml.bind.MarshalException;
import javax.xml.bind.ValidationException;

import org.astrogrid.applications.description.registry.RegistryQueryLocator;
import org.astrogrid.registry.RegistryException;
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
   public RegistryService getClient() throws RegistryException {
      RegistryService service = null;
     
      try {
	service = new TestRegistryQuerier(null);
    } catch (Exception e) {
	throw new RegistryException("problem setting up the test locator", e);
    }
      
  
   return service;
   }

}


/*
 * $Log: TestRegistryLocator.java,v $
 * Revision 1.3  2008/09/03 14:18:50  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.2.86.1  2008/04/01 13:50:06  pah
 * http service also passes unit tests with new jaxb metadata config
 *
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
