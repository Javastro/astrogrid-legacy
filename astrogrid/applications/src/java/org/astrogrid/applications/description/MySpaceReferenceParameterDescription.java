/*
 * $Id: MySpaceReferenceParameterDescription.java,v 1.4 2004/01/15 13:51:26 pah Exp $
 *
 * Created on 26 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.description;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.FileParameter;
import org.astrogrid.applications.MySpaceReferenceParameter;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.common.config.ApplicationControllerConfig;
import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManager;

/**
 * @TODO need to improve error handling...
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class MySpaceReferenceParameterDescription extends ParameterDescription {
   /* (non-Javadoc)
    * @see org.astrogrid.applications.description.ParameterDescription#process(java.lang.String)
    */
   public List process(Parameter parameter) {
      //we know that we must be dealing with a file reference parameter - do cast to get extra functionality
      MySpaceReferenceParameter frefParameter = (MySpaceReferenceParameter)parameter;
      AbstractApplication application = frefParameter.getApplication();
      //create a local file reference to copy the myspace file to and store in the 
      File localFile = application.createLocalTempFile();
      frefParameter.setRealFile(localFile);
      List result = new ArrayList();

      try {
         //TODO REFACTORME surely the myspace manager should come from the myspace reference string
         MySpaceClient mySpaceManager =
            ApplicationControllerConfig.getInstance().getMySpaceManager();
         User user = application.getUser();
         String urlstring = null;

         //get the myspace file and copy it locally...
         if (application.getApplicationInterface().parameterType(name) == ApplicationInterface.ParameterDirection.INPUT) {
         try {
            urlstring =
               mySpaceManager.getDataHoldingUrl(
                  user.getAccount(),
                  user.getGroup(),
                  user.getToken(),
                  frefParameter.getRawValue());
         }
         catch (Exception e1) {
            logger.error("myspace error", e1);
         }

            URL url = new URL(urlstring);
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            BufferedOutputStream out =
               new BufferedOutputStream(new FileOutputStream(localFile));
            int c;
            while ((c = in.read()) != -1) {
               out.write(c);
            }
            in.close();
            out.close();
         }

      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      //add the command line adornment using the local file name to pass to the application...
      return addCmdlineAdornment(localFile.getName()); //

   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.description.ParameterDescription#createValueObject(org.astrogrid.applications.AbstractApplication)
    */
   public Parameter createValueObject(AbstractApplication app) {
      return new MySpaceReferenceParameter(app, this);
   }

}
