/*
 * $Id: MySpaceUploadTask.java,v 1.2 2004/08/04 12:23:28 pah Exp $
 * 
 * Created on 13-Jul-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.deployment;

import org.apache.tools.ant.Task;

/**
 * Ant task to upload a file to myspace. This task uses a myspace delegate to upload to a specific myspace manager.
 * @author Paul Harrison (pah@jb.man.ac.uk) 13-Jul-2004
 * @version $Name:  $
 * @since iteration5
 */
public class MySpaceUploadTask extends Task {

   private String myspace;
   private String filename;
   /**
    * 
    */
   public MySpaceUploadTask() {
      super();
      // TODO Auto-generated constructor stub
   }
   

}
