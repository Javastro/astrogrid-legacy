/*
 * @(#)MySpaceFactory.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.myspace;
import org.astrogrid.datacenter.config.Configurable;
/** interface all myspace factories should implement */
public interface MySpaceFactory extends Configurable {
	
	/** allocate some space for the results of a job
	 * 
	 * @param jobURN unique identifier for this job ?
	 * @return new allocation object
	 * @throws AllocationException
	 */
    public Allocation allocateCacheSpace(String jobURN) throws AllocationException ;
    /** close / free up some allocated space
     * 
     * @param allocation
     * @throws AllocationException
     */
    public void close( Allocation allocation ) throws AllocationException ;
    
    
}
