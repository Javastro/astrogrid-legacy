 package org.astrogrid.registry.actions.portlets;

// Turbine stuff
import org.apache.turbine.util.Log;
import org.apache.turbine.util.RunData;
import org.apache.turbine.services.TurbineServices;

// Jetspeed stuff
import org.apache.jetspeed.portal.Portlet;
import org.apache.jetspeed.modules.actions.portlets.JspPortletAction;
import org.apache.jetspeed.webservices.finance.stockmarket.StockQuoteService;
import org.apache.jetspeed.webservices.finance.stockmarket.StockQuote;
import org.apache.jetspeed.util.PortletConfigState;
import org.apache.jetspeed.util.PortletSessionState;
import org.apache.jetspeed.util.StringUtils;

// Axis SOAP stuff
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

// Java XML, DOM etc
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.rpc.ParameterMode;
import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;


// Other Java
import java.io.*;
import java.util.*;

/**
 * This action sets up the AstroGrid Registry Request.
 *
 * @author <a href="mailto:r.t.platon@rl.ac.uk">Roy Platon</a>
 */

public class RegistryAction extends JspPortletAction
{
   private static final String INPUT = "RegInput";       
   private static final String SERVICE = "AstrogridRegistry";       
   private final String PARAMETERS =
         "http://localhost:8080/org/astrogrid/registry/parameters.xml";

// Return parameters for JspPortlet action
   private static final String HEADERS = "Registry";
   private static final String OUTPUT = "RegOutput";       
   private static final String[] SERVICES =
           {"", "Leicester", "Rutherford", "Testing"};       

// Web service stuff
// Set "server" and "port" - allow property overrides eventually!
   private static String SERVER = "stargrid1.bnsc.rl.ac.uk";
//   private static String STYLESHEET =
//         "http://localhost:8080/org/astrogrid/registry/registry.xsl";
   private String StyleSheet = null;
   private static int PORT = 8080;
   private String results = null;

/**
 * Build the normal state content for this portlet.
 *
 * @param portlet The jsp-based portlet that is being built.
 * @param rundata The turbine rundata context for this request.
 */
   protected void buildNormalContext( Portlet portlet, RunData rundata )
   {   
      String output = null;
      String services[] = SERVICES;

      Log.debug("[AstroGrid Registry] Start of Action");
// Get input string
      String element = (String) PortletSessionState.getAttributeWithFallback
                                  ( portlet, rundata, "Element" );
      String value = (String) PortletSessionState.getAttributeWithFallback
                                  ( portlet, rundata, "Value" );
// Check the Web Server Name from list
      String webserveropt = (String) PortletSessionState.getAttributeWithFallback
                                  ( portlet, rundata, "WebServerOpt" );
// also the Server Name
      String webserver = (String) PortletSessionState.getAttributeWithFallback
                                  ( portlet, rundata, "WebServer" );
// and the Server port
      String port = (String) PortletSessionState.getAttributeWithFallback
                                  ( portlet, rundata, "Port" );
// And the Query Button
      String queryType = (String) PortletSessionState.getAttributeWithFallback
                                  ( portlet, rundata, "QueryType" );

      Log.debug( "[AstroGrid Registry] Query obtained from jsp form "
                  + queryType + " " + element + " " + value
                  + " (" + webserveropt + "*" + webserver + ":" + port + ")" );

//  Open Parameter file to get Style Sheet */
      Document parameterDoc = null;
      DocumentBuilderFactory parameterFactory =
                             DocumentBuilderFactory.newInstance();
      DocumentBuilder parameterBuilder = null;

      try {
        parameterBuilder = parameterFactory.newDocumentBuilder();
        parameterDoc = parameterBuilder.parse( PARAMETERS );
      }
      catch (ParserConfigurationException e) {
        Log.error( "Parse Error: " + e );
      }
      catch (Exception e){
        Log.error("Registry Parameters not found: " + e);
      }

      Element parameterDocElement = parameterDoc.getDocumentElement();
      NodeList parameterNL =
             parameterDocElement.getElementsByTagName("registryStylesheet");

      StyleSheet = parameterNL.item(0).getFirstChild().getNodeValue();

      if ( StyleSheet == null ) {
        output = "*** Style Sheet Not Found ***<br/>";
        return;
      }      

      try {

/* First check that this isn't the initial form */
         if ( queryType == null && webserver == null ) {
            rundata.getRequest().setAttribute( HEADERS, "" );
            rundata.getRequest().setAttribute( OUTPUT, "" );
            rundata.getRequest().setAttribute( "QueryType", "listAllServices" );
            return;
         }
         String input = queryType + " " + element + " " + value;
    
         if ( webserveropt != null ) {
            if ( webserveropt.equals( "Rutherford" ) )
               webserver = "stargrid1.bnsc.rl.ac.uk";
            else if ( webserveropt.equals( "Leicester" ) ) 
               webserver = "homer.star.le.ac.uk";
            else if ( webserveropt.equals( "Testing" ) ) 
               webserver = "rlspc14.bnsc.rl.ac.uk";
            port = "8080";
         }
         if ( webserver == null ) {
            output = "<strong>**** No Server Specified ****</strong><br/>";
            if ( element == null ) output = "";
               rundata.getRequest().setAttribute( HEADERS, output );
               rundata.getRequest().setAttribute( OUTPUT, "" );
               Log.debug("[AstroGrid Registry] Blank Form");
               return;
         }
         if ( port == null ) port = Integer.toString( PORT );
            String out = "<em>Server:</em> " +
                         webserver + ":" + port + "<br/>";

            Log.debug( "[AstroGrid Registry] " + out );
            output = out;

// Setup Registry Service command

             SOAPService connection =
                     new SOAPService( webserver, port, SERVICE );
             Log.debug("[AstroGrid Registry] Running " +
                         queryType + " " + element + " " + value);
             output = output + "<em>Running Command:</em> " +
                         queryType + " " + element + " " + value + "<br/>";
             results = connection.runTask ( queryType, element, value );
             String htmlout = null;
             if ( results != null ) {
                 Log.debug( "[AstroGrid Registry] Query Result: " + results );
//                 htmlout = StringtoHTML( results);

// Convert the results to a DOM tree (add extra tag)
                 Document doc = StringToDom( "<QueryResponse>" + results +
                 "</QueryResponse>" );
//  Convert Response to HTML for output
                 if ( doc != null )
                     htmlout = DomToHtml( doc );
             }
             else htmlout = "";

// Place headers and Webservice results object in JSP context
             rundata.getRequest().setAttribute( HEADERS, output );
             rundata.getRequest().setAttribute( OUTPUT, htmlout );
// Place Webserver and port in JSP context
             rundata.getRequest().setAttribute( "ag_server", webserver );
             rundata.getRequest().setAttribute( "ag_port", port );
// Place Current Radio Button settings
             rundata.getRequest().setAttribute( "ag_type", queryType );
// Place Current Element and value
             rundata.getRequest().setAttribute( "ag_element", element );
             rundata.getRequest().setAttribute( "ag_value", value );
             Log.debug("[AstroGrid Registry] End of Portal Action");
       }
       catch (Exception e) {
          output = "**** Execution exception raised **** <br/>" + e.toString() + "<br/>";
          rundata.getRequest().setAttribute( HEADERS, output );
          rundata.getRequest().setAttribute( OUTPUT, "" );
          Log.error( e );
       }
   }

   private Document StringToDom ( String in ) {
      Document doc = null;
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating( false );
      InputSource is = new InputSource( new StringReader( in ) );
      
      try {
         DocumentBuilder builder = factory.newDocumentBuilder( );
         doc = builder.parse( is );
      }
      catch ( Exception e ) {
         Log.error( e );
      }
      return doc;
   }

/** This is a second attempt to format the return string by
 *  treating the Result String as a DOM and
 *  using an XSL style sheet to convert to HTML
 */ 
   private String DomToHtml ( Document doc ) {
      Node root = (Node) doc.getDocumentElement();
      String output = null;
      StringWriter out = new StringWriter ( );
      
      try {
         Source stylesheet = new StreamSource ( StyleSheet );
         TransformerFactory factory = TransformerFactory.newInstance();
         Transformer trans = factory.newTransformer( stylesheet );
         trans.transform( new DOMSource( doc ), new StreamResult( out ) );
         output = out.toString();
      }
      catch ( Exception e ) {
         Log.error( e );
      }
      return output;
   }

/** This is a first attempt to format the return string by
 *  parsing the Result String and highlighting tags.
 *  This code is now redundant
 */ 
    private String StringtoHTML ( String in ) {
       StringBuffer out = new StringBuffer();
       boolean intag = false;
       boolean endtag = false;
       int slength = in.length();
       int start = 0, end = 0;
       String tagstring = null;
       int indent = 4;

       out.append( "<h2>Registry Details</h2>\n" );
       for ( int i = 0; i < slength; i++ ) {
          char c = in.charAt( i );
          if ( c == '<' ) { intag = true; start = i+1; }
          if ( intag ) {
             if ( c == '/' ) endtag = true;
             if ( c == '>' ) {
                end = i;
                tagstring = in.substring( start, end );
// Special Tag Actions
                if ( tagstring.equals( "basicMetadata" ) )  out = out.append( "<p>" );
                else if ( tagstring.equals( "/basicMetadata" ) ) out = out.append( "</p>" );
                if ( tagstring.equals( "subjectList" ) ) indent += 4;
                else if ( tagstring.equals( "/subjectList" ) ) indent -= 4;
                else if ( tagstring.equals( "registrySvc" ) ) indent += 4;
                else if ( tagstring.equals( "/registrySvc" ) ) indent -= 4;
                else if ( tagstring.equals( "dataSvc" ) ) indent += 4;
                else if ( tagstring.equals( "/dataSvc" ) ) indent -= 4;
                else if ( tagstring.equals( "coverage" ) )  {
                  outstring ( out, indent, true, tagstring );
                   indent += 4;
                }
                else if ( tagstring.equals( "/coverage" ) ) indent -= 4;
                else if ( tagstring.equals( "contact" ) )  {
                  outstring ( out, indent, true, tagstring );
                   indent += 4;
                }
                else if ( tagstring.equals( "/contact" ) ) indent -= 4;
                else if ( tagstring.equals( "location" ) )  {
                  outstring ( out, indent, true, tagstring );
                   indent += 4;
                }
                else if ( tagstring.equals( "/location" ) ) indent -= 4;
// Default Tag Actions
                else if ( endtag );
                else if ( intag ) 
                          outstring ( out, indent, true, tagstring );
                else out = out.append( "&lt;" + tagstring + "&gt;" );
                intag = endtag = false; 
             }
         } else out = out.append( c );
      }
      return out.toString();
   }
   private void outstring ( StringBuffer sbuf, int indent, boolean brk, String s ) {
      if ( brk ) sbuf = sbuf.append( "<br/>" );
      for ( int i = 0; i < indent; i++ ) sbuf = sbuf.append( ' ' );
      sbuf = sbuf.append( "<strong>" + s + ": </strong>" );
   }
}

/** A class implementing SOAP communications for a client program running
 *  Web services using Axis.
 */
class SOAPService {
   boolean DEBUG = true;
   String site = "rlspc14.bnsc.rl.ac.uk";
   String port = "8080";
   String application = null;
   String endpoint = null;
   Service service;
   Call call;

/** Set up a connection with a service on the default site and port.
 *  @param the name of the service
 */
   public SOAPService ( String app ) throws Exception {
      application = app;
      newService();
   }
    
/** Set up a connection with a service at a given site and port.
 *  @param the required site
 *  @param the required port
 *  @param the name of the service
 */
   public SOAPService ( String s, String p, String app ) throws Exception {
      site = s;
      port = p;
      application = app;
      if ( port == null ) port = "8080";
      newService();
   }

/** Set up a call to this service
 */
   private void newService ( ) throws Exception { 
      service = new Service();
      call    = (Call) service.createCall();
      endpoint = "http://" + site + ":" + port + "/axis/services/" + application;
      call.setTargetEndpointAddress( new java.net.URL(endpoint) );
      if ( DEBUG ) System.out.println( "\nSend message to: " + endpoint );       
   }

/** Run the given method on this service with the given parameters.
 *  The interpretation of the return array of Strings will depend on the task.
 *  For normal Starlink JPCS tasks the Strings will be a {@link TaskReply} in
 *  the form of an XML document.
 *  @param the name of the method
 *  @param the parameter for the method
 *  @return the TaskReply document.
 */
   public String[] runTask ( String method, String params ) throws Exception {
      call.setOperationName( method );
      call.removeAllParameters( );
      call.addParameter( "method", XMLType.XSD_STRING, ParameterMode.IN );
      call.setReturnType( XMLType.SOAP_ARRAY );
      return (String[]) call.invoke( new Object [] { params } );
   }
   public String runTask ( String method, String para1, String para2 ) throws Exception {
      call.setOperationName( method );
      call.removeAllParameters( );
      call.addParameter( "queryElement", XMLType.XSD_STRING, ParameterMode.IN );
      call.addParameter( "queryElementValue", XMLType.XSD_STRING, ParameterMode.IN );
      call.setReturnType( XMLType.XSD_STRING );
      return (String) call.invoke( new Object [] { para1, para2 } );
   }    
}
