package org.astrogrid.registry.common;
import java.util.Hashtable;
import java.util.Enumeration;


public class WSDLBasicInformation {
   
   private Hashtable  endPoint = null;
   private String targetNameSpace = null;
   
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
   
   public Hashtable getEndPoint() {
      return this.endPoint;
   }
   
   public String getTargetNameSpace() {
      return this.targetNameSpace;
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