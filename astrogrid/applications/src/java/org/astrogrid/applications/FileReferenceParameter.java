/*
 * $Id: FileReferenceParameter.java,v 1.3 2003/12/31 00:56:17 pah Exp $
 *
 * Created on 08 December 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import java.io.File;

import org.astrogrid.applications.description.ParameterDescription;
public class FileReferenceParameter extends Parameter {
   private File realFile;
   /**
    * @param parameterDescription
    */
   public FileReferenceParameter(AbstractApplication application, ParameterDescription parameterDescription) {
      super(application, parameterDescription);
   }

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

}
