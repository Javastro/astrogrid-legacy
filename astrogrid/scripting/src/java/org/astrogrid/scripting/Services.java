/*$Id: Services.java,v 1.11 2004/08/09 09:11:24 nw Exp $
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

import org.apache.log4j.Logger;

import org.astrogrid.config.SimpleConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.apache.commons.digester.*;
import org.xml.sax.SAXException;

/** Maintains lists of known astrogrid services<p />
 * 
 * The list of services is loaded from an xml file. This can either loaded from the default location on the classpath,
 * or by specifying a URL.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2004
 *
 */
public class Services {
    /**
     * Commons Logger for this class
     */
    private static final Logger logger = Logger.getLogger(Services.class);

   
   /** default location of service list document - in this package on the classpath */
   public static final String DEFAULT_SERVICE_LIST= "services.xml";
   
   /** key to look in config for a service list */
   public static final String SERVICE_LIST_URL_KEY = "scripting.service.list.url";
   
   /** constructor, that populates list using the default service list document
    * 
    * @return a {@link java.util.List} of {@link Service} objects
    * @throws IOException
    * @throws SAXException
    */
   public Services()  throws IOException, SAXException  {
      this((URL)null);
   }
   
   /** constructor, populates a list using a document at specified URL */
   public Services(String url) throws MalformedURLException, IOException, SAXException {
      this( new URL(url) );      
   }
   
   /** constructor, that populates service list using document at specified url
    * 
    * @param serviceListDocument URL of the service list document to read in
    * @return a {@link java.util.List} of {@link Service} objects
    * @throws IOException
    * @throws SAXException
    */
   public Services(URL serviceListDocument) throws IOException, SAXException {
      Digester dig = new Digester();
      dig.push(this);
      dig.addObjectCreate("services/service",Service.class);
      dig.addSetProperties("services/service");
      dig.addBeanPropertySetter("services/service/endpoint");
      dig.addBeanPropertySetter("services/service/description");
      dig.addBeanPropertySetter("services/service/type");      
      dig.addSetNext("services/service","addService");
      InputStream is = null;
      if (serviceListDocument == null){
          serviceListDocument = SimpleConfig.getSingleton().getUrl(SERVICE_LIST_URL_KEY,this.getClass().getResource(DEFAULT_SERVICE_LIST));
      }
      logger.info("Reading service list from " + serviceListDocument);
      is = serviceListDocument.openStream();
      if (is == null) {
          logger.warn("No Service document present at " + serviceListDocument + " - service list will be empty");
      }
      dig.parse(is);
      is.close();
      
   }
   
   
   protected List datacenters = new ArrayList();
   protected List cea = new ArrayList();
   protected List registries = new ArrayList();
   protected List registryAdmins = new ArrayList();
   protected List jobcontrollers = new ArrayList();
   protected List jobmonitors = new ArrayList();
   protected List unknownServices = new ArrayList();
   
   /** helper method to create a service 
    * 
    * @param type one of the constants in {@link Service}
    * @param endpoint url of the service endpoint
    * @return a freshly-created service object.
    */
   public Service createService(String type,String endpoint) {
      Service s = new Service();
      s.setType(type);
      s.setEndpoint(endpoint);
      return s;
   }
   
   /** add a service to the list */
   public void addService(Service s) {
      if (Service.CEA_SERVICE.equals(s.getType())) {
         cea.add(s);
      } else if (Service.DATACENTER_SERVICE.equals(s.getType())) {
         datacenters.add(s);
      } else if (Service.JOBCONTROL_SERVICE.equals(s.getType())){
          jobcontrollers.add(s);
      } else if (Service.JOBMONITOR_SERVICE.equals(s.getType())){
         jobmonitors.add(s);
      } else if (Service.REGISTRY_SERVICE.equals(s.getType())) {
         registries.add(s);
      } else if (Service.REGISTRYADMIN_SERVICE.equals(s.getType())) {
               registryAdmins.add(s);
      } else {
         unknownServices.add(s);
      }
   }
   
   /** Access list of all declared services
    * @return
    */
   public List getAllServices() {
      List allServices = new ArrayList();
      allServices.addAll(datacenters);
      allServices.addAll(cea);
      allServices.addAll(registries);
      allServices.addAll(registryAdmins);
      allServices.addAll(jobcontrollers);
      allServices.addAll(jobmonitors);
      allServices.addAll(unknownServices);
      return allServices;
   }

   /** access list of all application services
    * @return
    */
   public List getApplications() {
      return cea;
   }

   /**access list of all datacenter services
    * @return
    */
   public List getDatacenters() {
      return datacenters;
   }

   /** access list of all jes services - job controllers, job monitors,
    * @return
    * @deprecated use {@link #getJobControllers()} or {@link #getJobMonitors()} instead
    */
   public List getJes() {
       List a = new ArrayList(jobcontrollers);
       a.addAll(jobmonitors);
      return a;
   }
   
   public List getJobControllers() {
       return jobcontrollers;
   }
   
   public List getJobMonitors() {
       return jobmonitors;
   }


   /**access list of all registry services
    * @return list of {@link Service} objects
    */
   public List getRegistries() {
      return registries;
   }

   /**access list of all registry admin services
    * @return
    */
   public List getRegistryAdmins() {
      return registryAdmins;
   }

   /** access list of all unknoqn servics
    * @return
    */
   public List getUnknownServices() {
      return unknownServices;
   }



    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Services:");
        if (datacenters.size() > 0) {
            buffer.append(" datacenters: ");
            buffer.append(datacenters);
        }
        if (cea.size() > 0) {
            buffer.append(" cea: ");
            buffer.append(cea);
        }
        if (registries.size() > 0) {
            buffer.append(" registries: ");
            buffer.append(registries);
        }
        if (registryAdmins.size() > 0) {
            buffer.append(" registryAdmins: ");
            buffer.append(registryAdmins);
        }
        if (jobcontrollers.size() > 0) {
            buffer.append(" jobcontrollers: ");
            buffer.append(jobcontrollers);
        } 
        if (jobmonitors.size() > 0) {
            buffer.append(" jobmonitors: ");
            buffer.append(jobmonitors);
        }
        if (unknownServices.size() > 0) {
            buffer.append(" unknownServices: ");
            buffer.append(unknownServices);
        }
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: Services.java,v $
Revision 1.11  2004/08/09 09:11:24  nw
added logging.
improved toString()
altered so that it degrades gracefully if service list document cannot be found

Revision 1.10  2004/07/01 11:36:25  nw
fixed for removal of myspace delegate

Revision 1.9  2004/05/07 15:33:50  pah
added egistryAdmin as a service type

Revision 1.8  2004/03/12 13:50:23  nw
improved scripting object

Revision 1.7  2004/03/05 16:27:28  nw
updated to new jes delegates

Revision 1.6  2004/02/27 00:51:32  nw
improved loading

Revision 1.5  2004/02/02 18:43:14  nw
improved constructors

Revision 1.4  2004/02/02 10:30:48  nw
added helper method to create services.

Revision 1.3  2004/02/01 00:38:24  nw
added two other kinds of job delegate

Revision 1.2  2004/01/29 10:41:40  nw
extended scripting model with helper functions,
imporove javadoc

Revision 1.1  2004/01/28 17:19:58  nw
first check in of scripting project
 
*/