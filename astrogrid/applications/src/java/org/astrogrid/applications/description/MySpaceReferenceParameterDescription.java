/*
 * $Id: MySpaceReferenceParameterDescription.java,v 1.7 2004/04/14 13:24:55 pah Exp $
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.cocoon.util.IOUtils;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.FileParameter;
import org.astrogrid.applications.MySpaceReferenceParameter;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.common.io.IOUtil;
import org.astrogrid.applications.manager.externalservices.ServiceNotFoundException;
import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManager;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;

/**
 * Describes a reference to "MySpace". In iteration 5 this is actually an ivorn
 * @TODO need to improve error handling...
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class MySpaceReferenceParameterDescription extends ParameterDescription {
   /* (non-Javadoc)
    * @see org.astrogrid.applications.description.ParameterDescription#process(java.lang.String)
    */
   public List process(Parameter parameter) throws CeaException {
      //we know that we must be dealing with a file reference parameter - do cast to get extra functionality
      MySpaceReferenceParameter frefParameter = (MySpaceReferenceParameter)parameter;
      AbstractApplication application = frefParameter.getApplication();
      //create a local file reference to copy the myspace file to and store in the 
      File localFile = application.createLocalTempFile();
      frefParameter.setRealFile(localFile);
      List result = new ArrayList();

         User user = application.getUser();
         VoSpaceClient voSpaceClient = new VoSpaceClient(user);
         String urlstring = null;

         //get the myspace file and copy it locally...
         if (application.getApplicationInterface().parameterType(name) == ApplicationInterface.ParameterDirection.INPUT) {
           logger.info("trying to get myspace value for "+ frefParameter.getName()+"="+frefParameter.getRawValue());
           try {
               InputStream is = voSpaceClient.getStream(new Ivorn(frefParameter.getRawValue()));
               IOUtil.copyStreamToFile(is, localFile);
            }
            catch (Exception e) {
               logger.error("could not get parameter "+name+"="+frefParameter.getRawValue(), e);
              throw new ParameterMySpaceReferenceNotFound("could not get parameter "+name, e);
            }
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
