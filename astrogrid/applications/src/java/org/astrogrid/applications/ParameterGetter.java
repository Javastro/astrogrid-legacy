/*
 * $Id: ParameterGetter.java,v 1.2 2004/03/23 12:51:25 pah Exp $
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
 * @author Paul Harrison (pah@jb.man.ac.uk) 19-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public interface ParameterGetter {
   /**
    * @param app the application that is being used to process the parameter - REFACTORME this should be refactored to the AbsractApplication really - but time constraints have not allowed.
    * Performs whatever actions are necessary for the processing the parameter. Most of the real work is done by looking in the {@link ParameterDescription} object.
    * @return true if the processing did not have any errors
    */
   public abstract boolean process() throws CeaException ;
}