/*
 * $Id: VoResourceSupport.java,v 1.19 2007/03/14 16:26:49 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata.v0_10;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.metadata.VoResourceSupportBase;

/**
 * Helper methods for constructing basic VoResource documents
 *
 * <p>See package documentation.
 * <p>
 * @author M Hill
 */

public class VoResourceSupport extends VoResourceSupportBase {

   protected static Log log = LogFactory.getLog(VoResourceSupport.class);
   
   public static final String VORESOURCE_ELEMENT = "vor:Resource";

   public static final String VORESOURCE_ELEMENT_NAMESPACES = 
     "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + 
     "xmlns:vor=\"http://www.ivoa.net/xml/RegistryInterface/v0.1\" " +
     "xmlns:vr=\"http://www.ivoa.net/xml/VOResource/v0.10\" " +
     // This one is the default namespace
     "xmlns=\"http://www.ivoa.net/xml/VOResource/v0.10\" ";

   public static final String VORESOURCE_ELEMENT_SCHEMALOCATIONS = 
      "http://www.ivoa.net/xml/VOResource/v0.10 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v0.10/VOResource.xsd" + " " + 
      "http://www.ivoa.net/xml/RegistryInterface/v0.1 http://software.astrogrid.org/schema/registry/RegistryInterface/v0.1/RegistryInterface.xsd"; 

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

