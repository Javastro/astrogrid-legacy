/*$Id: Services.java,v 1.7 2004/03/05 16:27:28 nw Exp $
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
 * @todo add community delegates - tricky - don't have an endpoint specified.
 *
 */
public class Services {

   
   /** default location of service list document - in this package on the classpath */
   public static final String SERVICE_LIST= "services.xml";
   
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
      if (serviceListDocument != null){
            is = serviceListDocument.openStream();
            if (is == null) {
               throw new IllegalArgumentException("No Service document present at " + serviceListDocument);
            }
      } else {
         is = this.getClass().getResourceAsStream(SERVICE_LIST);
         if (is == null) {
            throw new MissingResourceException("Could not load service list document resource from classpath ", this.getClass().getName(),SERVICE_LIST);
         }
      }

      dig.parse(is);
      is.close();
      
   }
   
   
   protected List myspaces = new ArrayList();
   protected List datacenters = new ArrayList();
   protected List applications = new ArrayList();
   protected List registries = new ArrayList();
   protected List jes = new ArrayList();
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
      if (Service.APPLICATION_SERVICE.equals(s.getType())) {
         applications.add(s);
      } else if (Service.DATACENTER_SERVICE.equals(s.getType())) {
         datacenters.add(s);
      } else if (Service.JOBCONTROL_SERVICE.equals(s.getType()) 
            || Service.JOBMONITOR_SERVICE.equals(s.getType())){
         jes.add(s);
      } else if (Service.MYSPACE_SERVICE.equals(s.getType())) {
         myspaces.add(s);
      } else if (Service.REGISTRY_SERVICE.equals(s.getType())) {
         registries.add(s);
      } else {
         unknownServices.add(s);
      }
   }
   
   /** Access list of all declared services
    * @return
    */
   public List getAllServices() {
      List allServices = new ArrayList(myspaces);
      allServices.addAll(datacenters);
      allServices.addAll(applications);
      allServices.addAll(registries);
      allServices.addAll(jes);
      allServices.addAll(unknownServices);
      return allServices;
   }

   /** access list of all application services
    * @return
    */
   public List getApplications() {
      return applications;
   }

   /**access list of all datacenter services
    * @return
    */
   public List getDatacenters() {
      return datacenters;
   }

   /** access list of all jes services - job controllers, job monitors, job schedulers
    * @return
    */
   public List getJes() {
      return jes;
   }

   /** access lilst of all myspace services
    * @return
    */
   public List getMyspaces() {
      return myspaces;
   }

   /**access list of all registry services
    * @return
    */
   public List getRegistries() {
      return registries;
   }

   /** access list of all unknoqn servics
    * @return
    */
   public List getUnknownServices() {
      return unknownServices;
   }

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return this.getAllServices().toString();
   }

}


/* 
$Log: Services.java,v $
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