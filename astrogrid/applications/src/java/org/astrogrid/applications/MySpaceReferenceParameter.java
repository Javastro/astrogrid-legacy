/*
 * $Id: MySpaceReferenceParameter.java,v 1.5 2004/03/23 12:51:25 pah Exp $
 *
 * Created on 08 December 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
public class MySpaceReferenceParameter extends FileReferenceParameter {
   /**
    * @param parameterDescription
    * @TODO REFACTORME - need to look at this hierarchy - also a general file reference could also be a url - it makes sense...
    */
   public MySpaceReferenceParameter(AbstractApplication application, ParameterDescription parameterDescription) {
      super(application, parameterDescription);
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.Parameter#writeBack()
    */
   public boolean writeBack() {
      String importURL = null;
      //TODO might want to do something different for VOTABLES later on
      //TODO REFACTORME surely the myspace manager should come from the registry via myspace reference string
      try {
         MySpaceClient mySpaceManager =
            application.getController().getMySpaceLocator().getClient();
            
         User user = application.getUser();
         URL url = new URL("file","",realFile.getAbsolutePath());
         importURL = url.toString();
         String content = getFileContent(realFile);
         //REFACTORME send the file as content string now - to allow remote use - however needs to use direct stream when myspace has that functionality.. 
         mySpaceManager.saveDataHolding(user.getUserId(), user.getCommunity(), user.getToken(), rawValue, content, "data"   , mySpaceManager.OVERWRITE);
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
   
   private String getFileContent(File file)
   {
      String line = null;
      //TODO REFACTORME this is ugly and wasteful - it assumes that the files are text files - needs to be refactored when myspace offers proper file writing...
      StringBuffer sb = new StringBuffer();
     
      try {
         BufferedReader br = new BufferedReader(new FileReader(file));
         while ((line = br.readLine())!=null) {
            sb.append(line);
         }
      }
      catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         logger.error("problem reading file contents to write back to MySpace",e);
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         logger.error("problem reading file contents to write back to MySpace",e);
     }
      
      
      return sb.toString();
     
   }

}
