 package org.astrogrid.registry.actions.portlets;

// Axis SOAP stuff
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import javax.xml.rpc.ParameterMode;
import javax.xml.namespace.QName;

/** A class implementing SOAP communications for a client program running
 *  Web services using Axis.
 *
 * @author <a href="mailto:r.t.platon@rl.ac.uk">Roy Platon</a>
 */

public class axisSOAPService {
   boolean DEBUG = true;
   String site = null;
   String port = null;
   String application = null;
   String endpoint = null;
   Service service;
   Call call;
    
/** Set up a connection with a service at a given site and port.
 *  @param the required site
 *  @param the required port
 *  @param the name of the service
 */
   public axisSOAPService ( String s, String p, String app ) throws Exception {
      site = s;
      port = p;
      application = app;
      newService();
   }

/** Set up a call to this service
 */
   private void newService ( ) throws Exception { 
      service = new Service();
      call    = (Call) service.createCall();
      endpoint = "http://" + site;
      if ( port != null ) endpoint = endpoint.concat( ":" + port );
      if ( application != null ) endpoint = endpoint.concat( application );
      call.setTargetEndpointAddress( new java.net.URL( endpoint ) );
   }

/** Run the given method on this service with the given parameter.
 *  The interpretation of the return array of Strings will depend on the task.
 *  For normal Starlink JPCS tasks the Strings will be a {@link TaskReply} in
 *  the form of an XML document.
 *  @param method the name of the method
 *  @param param the parameter for the method
 *  @return the TaskReply document.
 */
   public String sendRequest( String method, String param ) throws Exception {
      call.setOperationName( method );
      call.removeAllParameters( );
      call.addParameter( "xmlstring", XMLType.XSD_STRING, ParameterMode.IN );
      call.setReturnType( XMLType.XSD_STRING );
      return (String) call.invoke( new Object [] { param } );
   }

/** Run the given method on this service with the given parameter and service
 *  URI.
 *  The interpretation of the return array of Strings will depend on the task.
 *  For normal Starlink JPCS tasks the Strings will be a {@link TaskReply} in
 *  the form of an XML document.
 *  @param method the name of the method
 *  @param para1 the first parameter for the method
 *  @param uri the URI for the service
 *  @return the TaskReply document.
 */
   public String sendRequest ( String method, String param, String uri ) throws Exception {
      if ( uri != null ) {
          QName ns = new QName( uri, method );
          call.setOperationName( ns );
      } else {
          call.setOperationName( method );
      }
      call.removeAllParameters( );
      call.addParameter( "xmlstring", XMLType.XSD_STRING, ParameterMode.IN );
      call.setReturnType( XMLType.XSD_STRING );
      return (String) call.invoke( new Object [] { param } );
   }    
}
