/*
 * $Id: DelegateFactory.java,v 1.2 2004/07/01 11:07:09 nw Exp $
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

import org.astrogrid.applications.delegate.impl.CommonExecutionConnectorDelegate;

/**
 * A simple factory for creating CommonExecutionConnector Client delegates.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class DelegateFactory {
   /**
    * Create a CommonExecutionClientDelegate with the stated service endpoint
    * @param serviceEndpoint
    * @return
    */
   public static CommonExecutionConnectorClient createDelegate(String serviceEndpoint)
   {
      return CommonExecutionConnectorDelegate.buildDelegate(serviceEndpoint);
      
   }
   
   /**
    * @return
    */
   public static CommonExecutionConnectorClient createDelegate()
   {
      throw new UnsupportedOperationException("not yet implemented - need to do registry discovery");
   }
}
