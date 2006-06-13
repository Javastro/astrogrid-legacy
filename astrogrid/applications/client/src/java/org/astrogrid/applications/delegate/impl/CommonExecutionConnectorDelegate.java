/*
 * $Id: CommonExecutionConnectorDelegate.java,v 1.3 2006/06/13 20:33:13 clq2 Exp $
 * 
 * Created on 11-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.delegate.impl;

import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.common.delegate.AbstractDelegate;
import org.astrogrid.security.AxisClientSecurityGuard;
import org.astrogrid.security.SecurityGuard;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Mar-2004
 * @author Guy Rixon
 * @since iteration5
 */
public abstract class CommonExecutionConnectorDelegate extends AbstractDelegate implements CommonExecutionConnectorClient {

   public static CommonExecutionConnectorDelegate buildDelegate( String targetEndPoint ){
       return CommonExecutionConnectorDelegate.buildDelegate( targetEndPoint, DEFAULT_TIMEOUT ) ;
   }
    
    
   public static CommonExecutionConnectorDelegate buildDelegate( String targetEndPoint
                                                    , int timeout ) { 

           if( AbstractDelegate.isTestDelegateRequired(targetEndPoint)){
               return new CommonExecutionConnectorDummyDelegate() ;
           } else {
               return new CommonExecutionConnectorDelegateImpl(targetEndPoint, timeout ) ;                
           }
   }
        
  /**
   * Sets properties that control the signing of outgoing messages.
   *
   * @param g The guard object holding the credentials.
   */
  public void setCredentials(SecurityGuard sg1) throws CEADelegateException {
    try {
      this.axisGuard = new AxisClientSecurityGuard(sg1);
    }
    catch (Exception e) {
      throw new CEADelegateException("Failed to set credentials on a CEA delegate.", e);
    }
  }
  
  protected AxisClientSecurityGuard axisGuard;
  
}