/*
 * $Id: ExecutionPolicy.java,v 1.2 2008/09/03 14:18:56 pah Exp $
 * 
 * Created on 23 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

/**
 * Defines aspects of how an {@link ExecutionController} behaves.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 23 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public interface ExecutionPolicy {
    
    /**
     * Returns the maximum run time for applications in seconds. A value of 0 implies unlimited run times;
     * @return
     */
    public int getMaxRunTime();

    
    /**
     * Returns the period of the checks for overruning applications.
     * @return the period in seconds
     */
    public int getKillPeriod();
    
    
    /**
     * Returns the default lifetime for a job record in seconds.
     * @return
     */
    public int getDefaultLifetime();
    
    /**
     * Returns the period of the checks for jobs that should be destroyed.
     * @return the period in seconds
     */
    public int getDestroyPeriod();


    /**
     * The maximum number of jobs that can run concurrently
     * @return
     */
    public int getMaxConcurrent();
}


/*
 * $Log: ExecutionPolicy.java,v $
 * Revision 1.2  2008/09/03 14:18:56  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.3  2008/05/13 15:57:32  pah
 * uws with full app running UI is working
 *
 * Revision 1.1.2.2  2008/05/08 22:40:53  pah
 * basic UWS working
 *
 * Revision 1.1.2.1  2008/04/23 14:14:30  pah
 * ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749
 *
 */
