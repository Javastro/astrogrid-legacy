/*
 * $Id: CommonExecutionConnectorDelegate.java,v 1.2 2004/03/23 12:51:25 pah Exp $
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

package org.astrogrid.applications.delegate;

import java.rmi.RemoteException;

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.delegate.impl.*;
import org.astrogrid.applications.service.v1.cea.CeaFault;
import org.astrogrid.jes.delegate.impl.AbstractDelegate;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Mar-2004
 * @version $Name:  $
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
        

}
