/*
 * $Id: DelegateFactory.java,v 1.1 2003/11/26 22:07:24 pah Exp $
 * 
 * Created on 26-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.delegate;

/**
 * A simple factory for creating application controller delegates.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class DelegateFactory {
   public static ApplicationController createDelegate(String serviceEndpoint)
   {
      //trivially always return the dummy delegate at the moment
      return new ApplicationControllerDummyDelegate();
   }

}
