package org.astrogrid.community;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.namespace.QName;

public class AuthorizationDelegate {

   public boolean checkPermision( String who, String action, String resource ) {
   
      String endpoint = "http://localhost:8080/axis/services/AuthorizationService";
      
      Service service = new Service();
      
      Call    call    = (Call) service.createCall();
      
      call.setTargetEndpointAddress( new java.net.URL(endpoint) );
      call.setOperationName(new QName("http://soapinterop.org/", "checkPermission"));
      
      
      boolean ret = (boolean) call.invoke( new Object[] { who, action, resource} );
      
      return ret;
      
   }
}  