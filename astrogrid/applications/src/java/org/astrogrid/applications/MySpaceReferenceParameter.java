/*
 * $Id: MySpaceReferenceParameter.java,v 1.1 2004/01/15 13:51:26 pah Exp $
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.astrogrid.applications.common.config.ApplicationControllerConfig;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
public class MySpaceReferenceParameter extends Parameter {
   private File realFile;
   /**
    * @param parameterDescription
    */
   public MySpaceReferenceParameter(AbstractApplication application, ParameterDescription parameterDescription) {
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

   /* (non-Javadoc)
    * @see org.astrogrid.applications.Parameter#writeBack()
    */
   public boolean writeBack() {
      String importURL = null;
      //TODO might want to do something different for VOTABLES later on
      //TODO REFACTORME surely the myspace manager should come from the myspace reference string
      try {
         MySpaceClient mySpaceManager =
            ApplicationControllerConfig.getInstance().getMySpaceManager();
            
         User user = application.getUser();
         URL url = new URL("file","",realFile.getAbsolutePath());
         importURL = url.toString();
         mySpaceManager.saveDataHoldingURL(user.getUserId(), user.getCommunity(), user.getToken(), rawValue, importURL, "data"   , mySpaceManager.OVERWRITE);
      }
      catch (MalformedURLException e) {
         // TODO Auto-generated catch block
         logger.error("problem creating a url to pass to myspacemanager", e);
        
      }
      catch (IOException e) {
         logger.error(e);
      }
      catch (Exception e) {
        logger.error(e);
      }
      return true;

   }

}
