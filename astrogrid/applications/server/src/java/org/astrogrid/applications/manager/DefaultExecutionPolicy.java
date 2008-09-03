/*
 * $Id: DefaultExecutionPolicy.java,v 1.2 2008/09/03 14:18:56 pah Exp $
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

import junit.framework.Test;

import org.astrogrid.component.descriptor.ComponentDescriptor;


/**
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 8 May 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 **/
public class DefaultExecutionPolicy implements ExecutionPolicy, ComponentDescriptor {

    protected  int maxRunTime = 60*60*24*5; // 5 days
    
    protected int killPeriod = 60; // 60 Seconds
    
    protected  int defaultLifetime = 60*60*24*30; // 30 days

    protected  int destroyPeriod = 60* 5; // 5 minutes
    
    protected int maxConcurrent = 4; //number of concurrent jobs;
    
    public int getMaxRunTime() {
	return maxRunTime;
    }
    public int getKillPeriod() {
	return killPeriod;
    }
    public int getDefaultLifetime() {
	return defaultLifetime;
    }
    public int getDestroyPeriod() {
	return destroyPeriod ;
    }
    public String getDescription() {
	return toString();
    }
    public Test getInstallationTest() {
	// TODO create test
	return null;
    }
    public String getName() {
	return "Default Excection Policy";
    }
    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer("Execution Policy\n");
	sb.append("max conurrent jobs = ");
	sb.append(maxConcurrent);
	sb.append("\n");
	sb.append("max run time = ");
	sb.append(maxRunTime);
	sb.append(" (s)\n");
	sb.append("kill period = ");
	sb.append(killPeriod);
	sb.append(" (s)\n");
	sb.append("lifetime = ");
	sb.append(defaultLifetime);
	sb.append("(s)\n");
	sb.append("destroy period = ");
	sb.append(destroyPeriod);
	sb.append(" (s)\n");
	
	return sb.toString();
    }
    public int getMaxConcurrent() {
	return maxConcurrent;
    }
    
    

}


/*
 * $Log: DefaultExecutionPolicy.java,v $
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
