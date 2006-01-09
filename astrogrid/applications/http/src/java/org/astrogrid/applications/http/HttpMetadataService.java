/*
 * $Id: HttpMetadataService.java,v 1.3 2006/01/09 17:52:36 clq2 Exp $
 * 
 * Created on 13-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Iterator;

import org.exolab.castor.xml.CastorException;
import org.w3c.dom.Document;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.manager.DefaultMetadataService;
import org.astrogrid.registry.beans.v10.cea.CeaServiceType;
import org.astrogrid.registry.beans.v10.resource.Resource;
import org.astrogrid.registry.beans.v10.wsinterface.VOResources;

/**
 * Specization of the metadata service for a HTTP CEC. Will only provide registry entry about itself - does not try to rewrite applicaton entries.
 * @author Paul Harrison (pharriso@eso.org) 13-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class HttpMetadataService extends DefaultMetadataService {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(HttpMetadataService.class);

   /**
    * @param lib
    * @param urls
    */
   public HttpMetadataService(ApplicationDescriptionLibrary lib, URLs urls) {
      super(lib, urls);
      // TODO Auto-generated constructor stub
   }
   
   

 
   /**
    * Specialization of the getVODescription that
    * @see org.astrogrid.applications.manager.MetadataService#getVODescription()
    */
   public VOResources getVODescription() throws Exception {
      VOResources desc = super.getVODescription();
      VOResources newdesc = new VOResources();
      for (int i = 0; i < desc.getResourceCount(); i++) {
          Resource theresource = desc.getResource(i);
          if(theresource instanceof CeaServiceType)
          {
             newdesc.addResource(theresource);
             logger.info("adding CEA service " + theresource.getIdentifier());
          }
      }
      assert newdesc.getResourceCount() > 0: "there were no services found";
      return newdesc;
    }
}


/*
 * $Log: HttpMetadataService.java,v $
 * Revision 1.3  2006/01/09 17:52:36  clq2
 * gtr_1489_apps
 *
 * Revision 1.2.34.1  2005/12/22 13:56:03  gtr
 * Refactored to match the other kinds of CEC.
 *
 * Revision 1.2  2005/07/05 08:27:01  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.2.1  2005/06/14 09:49:32  pah
 * make the http cec only register itself as a ceaservice - do not try to reregister any cea applications that it finds
 *
 */
