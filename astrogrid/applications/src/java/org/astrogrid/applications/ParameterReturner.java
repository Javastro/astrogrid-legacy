/*
 * $Id: ParameterReturner.java,v 1.3 2004/04/19 17:34:08 pah Exp $
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
public interface ParameterReturner {
   /**
    * writes a parameter back to the invoking process. The default is to do nothing, most parameter types do not get written back
    * @return
    * @throws ParameterWriteBackException
    * @TODO does not really need to be boolean....
    */
   public abstract boolean writeBack() throws ParameterWriteBackException;
}