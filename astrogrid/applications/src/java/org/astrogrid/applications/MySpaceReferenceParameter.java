/*
 * $Id: MySpaceReferenceParameter.java,v 1.7 2004/04/14 13:24:55 pah Exp $
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
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.common.io.IOUtil;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
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
      try {            
         User user = application.getUser();
         URL url = new URL("file","",realFile.getAbsolutePath());
         VoSpaceClient voSpaceClient = new VoSpaceClient(user);
         OutputStream out = voSpaceClient.putStream(new Ivorn(rawValue));
         IOUtil.writeFile(out, realFile);
      }
      catch (IOException e) {
         logger.error("Myspace error - parameter"+ name+" value="+rawValue,e);
      } catch (URISyntaxException e) {
         logger.error("problem creating a url to pass to myspacemanager", e);
      }
      return true;

   }
   
   /**
    * @param file
    * @return
    * @deprecated
    */
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
