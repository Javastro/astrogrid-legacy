/*
 * $Id: ParameterGetter.java,v 1.3 2004/04/20 09:03:22 pah Exp $
 * 
 * Created on 19-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications;

/**
 * Defines how parameter values are obtained.
 * @author Paul Harrison (pah@jb.man.ac.uk) 19-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public interface ParameterGetter {
   /**
    * Performs whatever actions are necessary for the processing the parameter. 
    * @return true if the processing did not have any errors
    */
   public abstract boolean process() throws CeaException ;
}