/*$Id: RoughDispatcher.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import org.astrogrid.applications.delegate.ApplicationController;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.delegate.beans.ParameterValues;
import org.astrogrid.applications.delegate.beans.User;
import org.astrogrid.jes.JES;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.types.v1.Status;

/** Rough implementation of a Dispatcher, based on code cut out of job scheduler.
 * @deprecated replace with something cleaner
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 *
 */
public class RoughDispatcher implements Dispatcher {
    /** Construct a new RoughDispatcher
     * 
     */
    public RoughDispatcher(Locator locator) {
        this.locator = locator;
    }
    protected final Locator locator;

     public void dispatchStep( String communitySnippet, JobStep step)  throws JesException {
        
         String
             requestXML = null,
             toolLocation = null,
             jobMonitorURL = null,
             toolInterface = null ;
         ApplicationController
             applicationController = null ;
         String applicationID = null ;
         ParameterValues parameterValues = new ParameterValues() ;
         boolean bSubmit = true ;
        
         try {
           
             toolLocation = locator.locateTool( step ) ;
             toolInterface = locator.getToolInterface( step ) ;
             applicationController = DelegateFactory.createDelegate( toolLocation ) ;
             parameterValues.setParameterSpec( step.getTool().toXML() ) ; 
             parameterValues.setMethodName( toolInterface ) ;
            
             final Job parent = step.getParent();
             final User user = new User();
             user.setAccount(parent.getUserId()+"@"+parent.getCommunity());
             user.setGroup(parent.getGroup());
             user.setToken(parent.getToken());
            
             // set the URL for the JobMonitor so that it can be contacted... 
             jobMonitorURL = JES.getProperty( JES.MONITOR_URL, JES.MONITOR_CATEGORY ) ; 
 
             // This conditional is for short-term testing purposes only (JBL - Dec 2003)
             if( (toolLocation != null) && (!toolLocation.equals("")) ) { 
                
                 //JBL Note: the jobURN has had step number concatinated to it to effectively eliminate 
                 // the application controller (and data centers) needing to know anything regarding steps.
                 // This needs to be followed up in the JobMonitor delegate at some point, with
                 // rationalization of the xml flowing to the JobMonitor, which is now dealing with a lot
                 // of redundant information.
                 applicationID = applicationController.initializeApplication( step.getTool().getName() 
                                                                            , step.getParent().getId() + ":" + step.getStepNumber()
                                                                            , jobMonitorURL
                                                                            , user
                                                                            , parameterValues ) ;
                                                                        
                 bSubmit = applicationController.executeApplication( applicationID ) ;         
             }
 
             if( bSubmit == true ) { 
                 step.setStatus( Status.RUNNING ) ;                                                          
             }
             else {
                 step.setStatus(Status.ERROR ) ;
                 throw new JesException("failed when contacting application controller") ;
             }
  
         }
         catch( Exception rex ) {
             step.setStatus(Status.ERROR ) ;
             throw new JesException("failed when contacting application controller",rex) ;
         }

      
     } // end of dispatchOneStep()
    
    
}


/* 
$Log: RoughDispatcher.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:27:07  nw
rearranging code

Revision 1.1.2.3  2004/02/19 13:38:54  nw
tidied imports, updated to use status enumeration

Revision 1.1.2.2  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.1  2004/02/12 12:56:39  nw
factored out two components, concerned with accessing tool details
and talking with the application controller
 
*/