/*
 * $Id: CannotCreateWorkingDirectoryException.java,v 1.2 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 06-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.exceptions;

import java.io.File;

import org.astrogrid.applications.CeaException;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CannotCreateWorkingDirectoryException extends CeaException {

   /**
    * @param executionDirectory
    */
   public CannotCreateWorkingDirectoryException(File executionDirectory) {
      
      super(executionDirectory.getAbsolutePath());
   }


}
