/*
 * $Id: CannotCreateWorkingDirectoryException.java,v 1.1 2003/12/07 01:09:48 pah Exp $
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

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CannotCreateWorkingDirectoryException extends Exception {

   private File dir;
   /**
    * @param executionDirectory
    */
   public CannotCreateWorkingDirectoryException(File executionDirectory) {
      
      dir = executionDirectory;
   }

  
   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return super.toString() + "directory: "+dir.getAbsolutePath();
       }

}
