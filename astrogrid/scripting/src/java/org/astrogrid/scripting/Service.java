/*$Id: Service.java,v 1.2 2004/01/29 10:41:40 nw Exp $
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
   public static final String DATACENTER_SERVICE = "datacenter";
   public static final String MYSPACE_SERVICE= "myspace";
   public static final String REGISTRY_SERVICE = "registry";
   public static final String APPLICATION_SERVICE = "application";
   public static final String JOBCONTROL_SERVICE = "jobcontrol";
   public static final String UNNKOWN_SERVICE = "unknown";
   
   public Service() {
   }
   
   protected String endpoint;
   protected String type;
   protected String description;
   
   /** instantate a suitable delegate object for this service
    * 
    * @return a delegate object, depending on the {@ #type} atttibute of this service:
    * <ul>
    * <li>datacenter - return a {@link org.astrogrid.datacenter.delegate.FullSearcher}
    * <li>myspace - return a {@link org.astrogrid.mySpace.delegate.MySpaceClient}
    * <li>registry - not implemented yet
    * <li>application - return a {@link org.astrogrid.applications.delegate.ApplicationController}
    * <li>jobcontrol - return a {@link org.astrogrid.jes.delegate.jobController.JobControllerDelegate}
    * </ul>
    * @throws ServiceException
    * @throws IOException
    */
   public Object createDelegate() throws  ServiceException, IOException {
      if (DATACENTER_SERVICE.equals(type)) {
         return DatacenterDelegateFactory.makeFullSearcher(endpoint);
      } 
      if (MYSPACE_SERVICE.equals(type)) {
         return MySpaceDelegateFactory.createDelegate(endpoint);
      }
      if (REGISTRY_SERVICE.equals(type)) {
         throw new UnsupportedOperationException("Don't think the registry has a delegate yet.");
      }
      if(APPLICATION_SERVICE.equals(type)) {
         return DelegateFactory.createDelegate(endpoint);
      }
      if (JOBCONTROL_SERVICE.equals(type)) {
         return JobControllerDelegate.buildDelegate(endpoint);
      }
      throw new IllegalStateException("Unknown service type - cannot create delegate:" + this.endpoint);
   }
   
   /** Get the dsescription of this service
    * @return
    */
   public String getDescription() {
      return description;
   }

   /** Access the endpoint of this service
    * @return
    */
   public String getEndpoint() {
      return endpoint;
   }
 
   /** Access the type of this service - one of the constants defined in this class 
    * @return
    */
   public String getType() {
      return type;
   }

   /** Set the description of this service
    * @param string
    */
   public void setDescription(String string) {
      description = string.trim();
   }

   /** Set the endpoint of this service
    * @param string
    */
   public void setEndpoint(String string) {
      endpoint = string.trim();
   }

   /**Set the type of this service
    * @param string
    */
   public void setType(String string) {
      type = string.trim().toLowerCase();
      
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
Revision 1.2  2004/01/29 10:41:40  nw
extended scripting model with helper functions,
imporove javadoc

Revision 1.1  2004/01/28 17:19:58  nw
first check in of scripting project
 
*/