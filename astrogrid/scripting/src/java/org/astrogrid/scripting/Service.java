/*$Id: Service.java,v 1.1 2004/01/28 17:19:58 nw Exp $
 * Created on 27-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting;

import java.io.IOException;

import javax.xml.rpc.ServiceException;

import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.jes.delegate.jobController.JobControllerDelegate;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;


/**
 * Data object representing a service 
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2004
 *
 */ 
public class Service {
   public static final String DATACENTER_SERVICE = "Datacenter";
   public static final String MYSPACE_SERVICE= "Myspace";
   public static final String REGISTRY_SERVICE = "Registry";
   public static final String APPLICATION_SERVICE = "Application";
   public static final String JOBCONTROL_SERVICE = "Jobcontrol";
   public static final String UNNKOWN_SERVICE = "Unknown";
   
   public Service() {
   }
   
   protected String endpoint;
   protected String type;
   protected String description;
   public Object createDelegate() throws  ServiceException, IOException {
      if (DATACENTER_SERVICE.equals(type)) {
         return DatacenterDelegateFactory.makeFullSearcher(endpoint);
      } 
      if (MYSPACE_SERVICE.equals(type)) {
         return MySpaceDelegateFactory.createDelegate(endpoint);
      }
      if (REGISTRY_SERVICE.equals(type)) {
         throw new UnsupportedOperationException("Some prat hasn't not implemented this");
      }
      if(APPLICATION_SERVICE.equals(type)) {
         return DelegateFactory.createDelegate(endpoint);
      }
      if (JOBCONTROL_SERVICE.equals(type)) {
         return JobControllerDelegate.buildDelegate(endpoint);
      }
      throw new IllegalStateException("Unknown service type - cannot create delegate:" + this.endpoint);
   }
   
   /**
    * @return
    */
   public String getDescription() {
      return description;
   }

   /**
    * @return
    */
   public String getEndpoint() {
      return endpoint;
   }

   /**
    * @return
    */
   public String getType() {
      return type;
   }

   /**
    * @param string
    */
   public void setDescription(String string) {
      description = string.trim();
   }

   /**
    * @param string
    */
   public void setEndpoint(String string) {
      endpoint = string.trim();
   }

   /**
    * @param string
    */
   public void setType(String string) {
      type = string;
   }

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return "<Service " + this.type + " " + this.endpoint +  " >";
   }

}

/* 
$Log: Service.java,v $
Revision 1.1  2004/01/28 17:19:58  nw
first check in of scripting project
 
*/