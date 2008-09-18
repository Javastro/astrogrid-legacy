/*
 * $Id: ConfigurableExecutionPolicy.java,v 1.3 2008/09/18 09:13:39 pah Exp $
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

public class ConfigurableExecutionPolicy extends DefaultExecutionPolicy implements ExecutionPolicy {

 
     
    public void setKillPeriod(int killPeriod) {
        this.killPeriod = killPeriod;
    }

    public void setMaxRunTime(int maxRunTime) {
        this.maxRunTime = maxRunTime;
    }

    public void setDefaultLifetime(int defaultLifetime) {
        this.defaultLifetime = defaultLifetime;
    }

    public void setDestroyPeriod(int destroyPeriod) {
        this.destroyPeriod = destroyPeriod;
    }
    
    
    public void setMaxConcurrent(int njobs) {
        this.maxConcurrent = njobs;
    }


}


/*
 * $Log: ConfigurableExecutionPolicy.java,v $
 * Revision 1.3  2008/09/18 09:13:39  pah
 * improved javadoc
 *
 * Revision 1.2  2008/09/03 14:18:56  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/05/08 22:40:53  pah
 * basic UWS working
 *
 * Revision 1.1.2.1  2008/04/23 14:14:30  pah
 * ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749
 *
 */
