/*
 * $Id: MySpaceFromConfig.java,v 1.2 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 19-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager.externalservices;

import java.io.IOException;

import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.manager.MySpaceLocator;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 19-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class MySpaceFromConfig extends ServiceFromConfig implements MySpaceLocator  {

   /**
    * @param config
    */
   public MySpaceFromConfig(CeaControllerConfig config) {
      
      super(config);
   }

   /** 
    * @see org.astrogrid.applications.manager.MySpaceLocator#getClient()
    */
   public MySpaceClient getClient() {
      MySpaceClient result = null;
      try {
         result = MySpaceDelegateFactory.createDelegate(config.getMySpaceManagerEndpoint());
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return result;
   }

}
