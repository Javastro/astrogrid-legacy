/*$Id: RoughLocator.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.locator;

import org.astrogrid.jes.JES;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.jobscheduler.Locator;

/** Rough implementation of a tool locator, based on code cut out from job scheduler.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 * @deprecated use another implementation instead.
 *
 */
public class RoughLocator implements Locator {
    /** Construct a new RoughToolLocator
     * 
     */
    public RoughLocator() {
        super();
    }

    public String locateTool( JobStep step )  { 
        
        String
            toolLocation  = null,
            toolName = step.getTool().getName() ;
            
        
        
        
            toolLocation = JES.getProperty( JES.TOOLS_LOCATION + toolName
                                          , JES.TOOLS_CATEGORY ) ;

        
        return toolLocation ;                   
        
    } 
    
    
    
    public String getToolInterface( JobStep step ) { 
        
        String
            toolInterface  = null,
            toolName = step.getTool().getName() ;

        
            toolInterface = JES.getProperty( JES.TOOLS_INTERFACE + toolName
                                           , JES.TOOLS_CATEGORY ) ;

        
        return toolInterface ;                   
        
    } // end JobScheduler.getToolInterface()
}


/* 
$Log: RoughLocator.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:27:21  nw
rearranging code

Revision 1.1.2.3  2004/02/19 13:38:17  nw
started implementation of an alternative tool locator

Revision 1.1.2.2  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.1  2004/02/12 12:56:39  nw
factored out two components, concerned with accessing tool details
and talking with the application controller
 
*/