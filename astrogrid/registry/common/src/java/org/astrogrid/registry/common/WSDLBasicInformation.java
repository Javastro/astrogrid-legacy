package org.astrogrid.registry.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Hashtable;
import java.util.Enumeration;

/** @todo javadoc */
public class WSDLBasicInformation {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(WSDLBasicInformation.class);
   
   private Hashtable  endPoint = null;
   private String targetNameSpace = null;
   private String schemaTargetNameSpace = null;
   public WSDLBasicInformation(Hashtable endPoint, String targetNameSpace) {
      this.endPoint = endPoint;
      this.targetNameSpace = targetNameSpace;
   }
   
   public WSDLBasicInformation() {
      this.endPoint = new Hashtable();
      this.targetNameSpace = null;
   }
   
   public void setEndPoint(Hashtable endPoint) {
      this.endPoint = endPoint;
   }
   
   public void setTargetNameSpace(String targetNameSpace) {
      this.targetNameSpace = targetNameSpace;
   }
   
   public void setSchemaTargetNameSpace(String schemaTargetNameSpace) {
      this.schemaTargetNameSpace = schemaTargetNameSpace;
   }
   
   
   public Hashtable getEndPoint() {
      return this.endPoint;
   }
   
   public String getTargetNameSpace() {
      return this.targetNameSpace;
   }

   public String getSchemaTargetNameSpace() {
      return this.schemaTargetNameSpace;
   }
   
   public void addEndPoint(String key, String endPoint) {
      this.endPoint.put(key,endPoint);
   }
   
   public String toString() {
      String s = "TargetNameSpace = " + this.targetNameSpace + "\n";
      Enumeration enum = endPoint.keys();
      while(enum.hasMoreElements()) {
         s += "Port Name = ";
         String key = (String)enum.nextElement();
         s += key + " With Endpoint/location = " + endPoint.get(key) + "\n";
      }
      return s;      
   }
   
   
}