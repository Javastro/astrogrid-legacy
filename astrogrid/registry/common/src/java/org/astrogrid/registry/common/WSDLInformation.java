package org.astrogrid.registry.common;

import javax.xml.namespace.QName;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.*;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBody;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import javax.wsdl.factory.WSDLFactory;
import org.astrogrid.registry.RegistryException;



public class WSDLInformation {
   
   public WSDLInformation() {
      
   }
   
   public static String getNameSpaceFromBinding(String url, String interfaceName) throws RegistryException {
      try {
         WSDLFactory wf = WSDLFactory.newInstance();
         WSDLReader wr = wf.newWSDLReader();                        
         Definition def = wr.readWSDL(url);
         Map mp = def.getBindings();
         Set bindingSet = mp.keySet();
         Iterator iter = bindingSet.iterator();
         while(iter.hasNext()) {
            QName bindingQName = (QName) iter.next();
            javax.wsdl.Binding binding = (javax.wsdl.Binding)mp.get(bindingQName);
            BindingOperation boperation = binding.getBindingOperation(interfaceName,null,null);
            if(boperation == null) {
               throw new RegistryException("Could not find binding for operation name = " + interfaceName);
            }
            BindingInput bi = boperation.getBindingInput();
            List lst = bi.getExtensibilityElements();
            for(int i = 0; i < lst.size();i++) {
               ExtensibilityElement extElement = (ExtensibilityElement)lst.get(i);
               if(extElement instanceof SOAPBody) {
                  SOAPBody soapBody = (SOAPBody)extElement;                
                  System.out.println("the nanespaceuri from soapbody = " + soapBody.getNamespaceURI());
                  return soapBody.getNamespaceURI();
               }//if
               System.out.println("the extensiblity type = " + extElement.getElementType().toString());
            }
         }
      }catch(WSDLException wse) {
         throw new RegistryException(wse);
      }
      return null;
   }
   
   public static WSDLBasicInformation getBasicInformationFromURL(String url) throws RegistryException {
         WSDLBasicInformation wsdlBasic = null;
         System.out.println("begin getBasicInformationFromURL with url = " + url);
         try {
            WSDLFactory wf = WSDLFactory.newInstance();
            WSDLReader wr = wf.newWSDLReader();                        
            Definition def = wr.readWSDL(url);
            
            System.out.println("processed targetnamespace = " + def.getTargetNamespace());
            wsdlBasic = new WSDLBasicInformation();
            wsdlBasic.setTargetNameSpace(def.getTargetNamespace());
            //do a getBinding from the definition then a getBindingOperation
            Map mp = def.getServices();               
            Set serviceSet = mp.keySet();
            Iterator iter = serviceSet.iterator();
            while(iter.hasNext()) {
               //I think this is actually a QName may need to change.
               //String serviceName = (String)iter.next();
   //          javax.wsdl.Service service = (javax.wsdl.Service)mp.get(serviceName);
               QName serviceQName = (QName)iter.next();
               javax.wsdl.Service service = (javax.wsdl.Service)mp.get(serviceQName);
               Set portSet = service.getPorts().keySet();
               Iterator portIter = portSet.iterator();
               while(portIter.hasNext()) {
                  //Probably also a QName
                  String portName = (String)portIter.next();
                  Port port = (Port)service.getPorts().get(portName);
                  List lst = port.getExtensibilityElements();
                  for(int i = 0; i < lst.size();i++) {
                     ExtensibilityElement extElement = (ExtensibilityElement)lst.get(i);                        
                     if(extElement instanceof SOAPAddress) {
                        SOAPAddress soapAddress = (SOAPAddress)extElement;                
                        System.out.println("the locationuri = " + soapAddress.getLocationURI());           
                        wsdlBasic.addEndPoint(port.getName(),soapAddress.getLocationURI());   
                     }//if   
                  }//for                        
               }//while                     
            }//while
            System.out.println("finished with getBasicInformationFromURL and wsdlBasic = " + wsdlBasic.toString());
         }catch(WSDLException wse) {
            throw new RegistryException(wse);
         }
         return wsdlBasic;
   }
}