/*$Id: Service.java,v 1.13 2004/11/22 18:26:54 clq2 Exp $
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

import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.registry.client.RegistryDelegateFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.URL;

import javax.xml.rpc.ServiceException;


/**
 * Data object representing a service 
 * @deprecated don't think things are going to work out this way any more. stay clear. unstable code.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2004
 *
 */ 
public class Service {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Service.class);
    

   public static final String DATACENTER_SERVICE = "datacenter";
   public static final String REGISTRY_SERVICE = "registry";
   public static final String REGISTRYADMIN_SERVICE = "registryadmin";
   public static final String CEA_SERVICE = "cea";
   public static final String JOBCONTROL_SERVICE = "jobcontrol";
   public static final String JOBMONITOR_SERVICE = "jobmonitor";
   public static final String UNNKOWN_SERVICE = "unknown";
   
   
   protected String endpoint;
   protected String type;
   protected String description;
   
   /** instantate a suitable delegate object for this service
    * 
    * @return a delegate object, depending on the {@ #type} atttibute of this service:
    * <ul>
    * <li>datacenter - return a {@link org.astrogrid.datacenter.delegate.QuerySearcher}
    * <li>registry - return a {@link org.astrogrid.registry.client.query.RegistryService}
    * <li>registryadmin - return a {@link org.astrogrid.registry.client.admin.RegistryAdminService}
    * <li>application - return a {@link org.astrogrid.applications.delegate.CommonExecutionConnectorClient}
    * <li>jobcontrol - return a {@link org.astrogrid.jes.delegate.JobController}
    * <li>jobmonitor - return a {@link org.astrogrid.jes.delegate.JobMonitor}
    * </ul>
    * @throws ServiceException
    * @throws IOException
    */
   public Object createDelegate() throws  ServiceException, IOException {
      if (DATACENTER_SERVICE.equals(type)) {
          logger.info("Creating datacenter delegate");
         return DatacenterDelegateFactory.makeQuerySearcher(endpoint);
      } 
      if (REGISTRY_SERVICE.equals(type)) {
          logger.info("Creating registry delegate");
          return RegistryDelegateFactory.createQuery(new URL(endpoint));
      }
      if (REGISTRYADMIN_SERVICE.equals(type)) {
          logger.info("Creating registry admin delegate");
          return RegistryDelegateFactory.createAdmin(new URL(endpoint));
      }
      if(CEA_SERVICE.equals(type)) {
          logger.info("Creating cea delegate");
         return DelegateFactory.createDelegate(endpoint);
      }
      if (JOBCONTROL_SERVICE.equals(type)) {
          logger.info("Creating job controller delegate");
         return JesDelegateFactory.createJobController(endpoint);
      }
      if (JOBMONITOR_SERVICE.equals(type)) {
          logger.info("Creating job monitor delegate");
         return JesDelegateFactory.createJobMonitor(endpoint);
      }
      logger.error("Unknown service type - cannot create delegate for " + this.toString());
      throw new IllegalStateException("Unknown service type - cannot create for " + this.toString());
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

    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (endpoint == null ? 0 : endpoint.hashCode());
        hashCode = 31 * hashCode + (type == null ? 0 : type.hashCode());
        hashCode = 31
            * hashCode
            + (description == null ? 0 : description.hashCode());
        return hashCode;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Service:");
        buffer.append(" endpoint: ");
        buffer.append(endpoint);
        buffer.append(" type: ");
        buffer.append(type);
        buffer.append(" description: ");
        buffer.append(description);
        buffer.append("]");
        return buffer.toString();
    }
    /**
     * Returns <code>true</code> if this <code>Service</code> is the same as the o argument.
     *
     * @return <code>true</code> if this <code>Service</code> is the same as the o argument.
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        Service castedObj = (Service) o;
        return ((this.endpoint == null
            ? castedObj.endpoint == null
            : this.endpoint.equals(castedObj.endpoint))
            && (this.type == null ? castedObj.type == null : this.type
                .equals(castedObj.type)) && (this.description == null
            ? castedObj.description == null
            : this.description.equals(castedObj.description)));
    }
}

/* 
$Log: Service.java,v $
Revision 1.13  2004/11/22 18:26:54  clq2
scripting-nww-715

Revision 1.12.68.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.

Revision 1.12  2004/08/09 11:28:17  nw
improvied behaviour when no service list is found.
tidied imports.

Revision 1.11  2004/08/09 09:10:38  nw
updated list of service types (renamed applications to cea)
improved toString()
added logging

Revision 1.10  2004/07/01 11:36:25  nw
fixed for removal of myspace delegate

Revision 1.9  2004/05/07 15:33:50  pah
added egistryAdmin as a service type

Revision 1.8  2004/04/15 11:22:47  nw
added support for registry delegate.

Revision 1.7  2004/03/16 13:57:41  nw
added in registry delegate

Revision 1.6  2004/03/14 23:11:32  nw
commented out code that used methods that have dissapeared from datacenter and applications delegate jars

Revision 1.5  2004/03/05 16:27:28  nw
updated to new jes delegates

Revision 1.4  2004/02/27 00:51:01  nw
updated to use new jes delegates

Revision 1.3  2004/02/01 00:38:24  nw
added two other kinds of job delegate

Revision 1.2  2004/01/29 10:41:40  nw
extended scripting model with helper functions,
imporove javadoc

Revision 1.1  2004/01/28 17:19:58  nw
first check in of scripting project
 
*/