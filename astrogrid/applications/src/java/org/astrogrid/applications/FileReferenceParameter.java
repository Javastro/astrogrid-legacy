/*
 * $Id: FileReferenceParameter.java,v 1.9 2004/04/28 16:10:11 pah Exp $
 * 
 * Created on 15-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications;

import java.io.File;

import org.astrogrid.applications.description.ParameterDescription;

/**
 * A file reference parameter. This is a parameter whose value is a reference to a file on a locally accessible filesystem.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class FileReferenceParameter extends Parameter {

   /**
    * @param application
    * @param parameterDescription
    */
   public FileReferenceParameter(
      AbstractApplication application,
      ParameterDescription parameterDescription) {
      super(application, parameterDescription);
   }

   /**
    * The file that the parameter value points to.
    */
   protected File realFile;

   /**
       * @return
       */
   public File getRealFile() {
      return realFile;
   }

   /**
       * @param file
       */
   public void setRealFile(File file) {
      realFile = file;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.Parameter#setRawValue(java.lang.String)
    */
   public void setRawValue(String string) {
      super.setRawValue(string);
      // set the file also - this is the essence of what it is to be a file reference parameter...
      realFile = new File(string);
   }

}
