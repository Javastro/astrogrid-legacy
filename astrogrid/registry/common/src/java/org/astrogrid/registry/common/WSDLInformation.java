package org.astrogrid.registry.common;

import javax.xml.namespace.QName;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.*;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import javax.wsdl.factory.WSDLFactory;
import org.astrogrid.registry.RegistryException;



public class WSDLInformation {
   
   public WSDLInformation() {
      
   }
   
   public static WSDLBasicInformation getBasicInformationFromURL(String url) throws RegistryException {
         WSDLBasicInformation wsdlBasic = null;
         try {
            WSDLFactory wf = WSDLFactory.newInstance();
            WSDLReader wr = wf.newWSDLReader();                        
            Definition def = wr.readWSDL(url);
            
            wsdlBasic = new WSDLBasicInformation();
            wsdlBasic.setTargetNameSpace(def.getTargetNamespace());
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
                        wsdlBasic.addEndPoint(port.getName(),soapAddress.getLocationURI());   
                     }//if   
                  }//for                        
               }//while                     
            }//while
         }catch(WSDLException wse) {
            throw new RegistryException(wse);
         }
         return wsdlBasic;
   }
}