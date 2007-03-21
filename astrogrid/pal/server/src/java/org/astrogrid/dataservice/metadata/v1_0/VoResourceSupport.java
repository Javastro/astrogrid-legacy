/*
 * $Id: VoResourceSupport.java,v 1.1 2007/03/21 18:56:30 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata.v1_0;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.metadata.VoResourceSupportBase;

/**
 * Helper methods for constructing basic VoResource documents
 * for the v1.0 VoResource specification.
 *
 * @author M Hill, K Andrews
 */

public class VoResourceSupport extends VoResourceSupportBase {

   protected static Log log = LogFactory.getLog(VoResourceSupport.class);
   
   public static final String VORESOURCE_ELEMENT = "ri:Resource";

   public static final String VORESOURCE_ELEMENT_NAMESPACES = 
     "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + 
     "xmlns:ri=\"http://www.ivoa.net/xml/RegistryInterface/v1.0\" "+
     "xmlns:vs=\"http://www.ivoa.net/xml/VODataService/v1.0\" " +
     // This one is the default namespace
     "xmlns=\"http://www.ivoa.net/xml/RegistryInterface/v1.0\" ";

   public static final String VORESOURCE_ELEMENT_SCHEMALOCATIONS = 
      "http://www.ivoa.net/xml/RegistryInterface/v1.0 http://software.astrogrid.org/schema/registry/RegistryInterface/v1.0/RegistryInterface.xsd" + " " +
      "http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd";

   /** Constructs  a VoResource opening tag with the given resource type */
   public String makeVoResourceElement(String vorType, String namespaces, String schemaLocations) {
     String nsSp, slSp;
     if (namespaces.equals("")) {
       nsSp = "";
     } else {
       nsSp = " ";
     }
     if (schemaLocations.equals("")) {
       slSp = "";
     } else {
       slSp = " ";
     }
     return "<" + VORESOURCE_ELEMENT + " " + 
         VORESOURCE_ELEMENT_NAMESPACES + nsSp + 
         namespaces + " " +
         "xsi:schemaLocation=\"" +
         VORESOURCE_ELEMENT_SCHEMALOCATIONS + slSp +
         schemaLocations + 
         "\"" +
         " status='active' updated='"+toRegistryForm(new Date())+"' xsi:type='"+vorType+"'"+
         ">";
   }
}
