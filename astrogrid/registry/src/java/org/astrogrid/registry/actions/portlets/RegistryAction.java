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
 * This action sets up the Astrogrid Registry Request.
 *
 * @author <a href="mailto:r.t.platon@rl.ac.uk">Roy Platon</a>
 */

public class RegistryAction extends JspPortletAction
{
  private static final boolean DEBUG = true;       
  private static final String INPUT = "RegInput";       

//  Local parameter file for registry file and style sheet
  private static final String PARAMETERHOST =
                               "http://rlspc14.bnsc.rl.ac.uk:8080";
  private static final String PARAMETERFILE =
                               "org/astrogrid/registry/localparameters.xml";
  private static final String STYLESHEET = "RegistryInterfaceStylesheet";
  private static final String SERVICES = "RegistryServers";
  private static final String DEFAULTSERVICE = 
                                          "/axis/services/RegistryInterface";

// Return parameters for JspPortlet action
  private static final String HEADERS = "RegistryHeader";
  private static final String OUTPUT = "RegistryOutput";       
  private static final String[][] SERVERS = {  
    { "Testing",    "rlspc14.bnsc.rl.ac.uk",
                    "/axis/services/AstrogridRegistryQuery" },
    { "MSSL",       "msslxy.mssl.ucl.ac.uk",
                    "/soap/servlet/rpcrouter/",
                    "urn:org.astrogrid.registry.RegistryInterface" },
    { "Leicester",  "katatjuta.star.le.ac.uk", 
                    "/axis/services/RegistryInterface" },
    { "Rutherford", "stargrid1.bnsc.rl.ac.uk",
                    "/axis/services/AstrogridRegistryQuery" }
  };       
  private static final String SERVICE = "AstrogridRegistryInterface";       
  private static final String LOCALSERVER = "stargrid1.bnsc.rl.ac.uk";
  private static final String REGISTRYMSG = "[Astrogrid Registry] ";


// Web service stuff
// Set "server" and "port" - allow property overrides eventually!
  private String StyleSheet = null;
  private static String DEFAULTPORT = "8080";
  private String results = null;
  private static String services[][] = SERVERS;
  private static String service = null;
  private static String soapservice = null;

/**
 * Build the normal state content for this portlet.
 *
 * @param portlet The jsp-based portlet that is being built.
 * @param rundata The turbine rundata context for this request.
 */
  protected void buildNormalContext( Portlet portlet, RunData rundata )
  {   
    String output = null;

    Log.debug( REGISTRYMSG + "Start of Action" );
// Get query from boxes
    String element1 = (String) PortletSessionState.getAttributeWithFallback
                                 ( portlet, rundata, "Element1" );
    String operator1 = (String) PortletSessionState.getAttributeWithFallback
                                 ( portlet, rundata, "Operator1" );
    String value1 = (String) PortletSessionState.getAttributeWithFallback
                                 ( portlet, rundata, "Value1" );
    String seperator1 = (String) PortletSessionState.getAttributeWithFallback
                                 ( portlet, rundata, "Seperator1" );
    String element2 = (String) PortletSessionState.getAttributeWithFallback
                                 ( portlet, rundata, "Element2" );
    String operator2 = (String) PortletSessionState.getAttributeWithFallback
                                 ( portlet, rundata, "Operator2" );
    String value2 = (String) PortletSessionState.getAttributeWithFallback
                                 ( portlet, rundata, "Value2" );
//      String Query = (String) PortletSessionState.getAttributeWithFallback
//                                  ( portlet, rundata, "ComplexQuery" );
// Check the Web Server Name from list
    String webserveropt = (String) PortletSessionState.getAttributeWithFallback
                                  ( portlet, rundata, "WebServerOpt" );
// also the Server Name
    String webserver = (String) PortletSessionState.getAttributeWithFallback
                                  ( portlet, rundata, "WebServer" );
// and the Server port
    String port = (String) PortletSessionState.getAttributeWithFallback
                                  ( portlet, rundata, "Port" );

/* First check that this isn't the initial form */
     rundata.getRequest().setAttribute( HEADERS, "" );
     rundata.getRequest().setAttribute( OUTPUT, "" );

     if ( webserveropt == null && webserver == null ) {
       Log.debug( REGISTRYMSG + "Blank form" );
       return;
     }

//  Open Parameter file to get Style Sheet */
     Document parameterDoc = null;
     DocumentBuilderFactory parameterFactory =
                            DocumentBuilderFactory.newInstance();
     DocumentBuilder parameterBuilder = null;

     try {
       parameterBuilder = parameterFactory.newDocumentBuilder();
       parameterDoc = parameterBuilder.parse(
                              PARAMETERHOST + "/" + PARAMETERFILE );
       Element parameterDocElement = parameterDoc.getDocumentElement();
       NodeList key = parameterDocElement.getElementsByTagName( STYLESHEET );

       StyleSheet = key.item(0).getFirstChild().getNodeValue();
       if ( DEBUG) Log.debug( REGISTRYMSG + StyleSheet );
// Check for local Proxy Server from the localparameters file
       key = parameterDocElement.getElementsByTagName( "ProxyHost" );
       if ( key != null ) { 
         String proxyhost = key.item(0).getFirstChild().getNodeValue();
         if ( DEBUG ) Log.debug( REGISTRYMSG + "Proxy " + proxyhost);
         System.setProperty( "http.proxyHost", proxyhost );
         key = parameterDocElement.getElementsByTagName( "ProxyPort" );
         if ( key != null ) System.setProperty( "http.proxyPort", 
                             key.item(0).getFirstChild().getNodeValue() );
       }
// Check for list of servers from the localparameters file       
       key = parameterDocElement.getElementsByTagName( "RegistryServers" );
       if ( key != null ) {
         NodeList servers = parameterDocElement.getElementsByTagName( "server" );
         if ( servers != null ) {
            Log.debug( REGISTRYMSG + servers.getLength() + " Servers " );
         }
         for ( int i = 0; i < servers.getLength(); i++ ) {
            Node namenode = servers.item(0);
            String name = namenode.getFirstChild().getNodeValue();
            Log.debug( REGISTRYMSG + " Server: " + i + " " + name );
         }
       } 
     }
     catch ( ParserConfigurationException e ) {
       output = "*** Parse Error in Parameter file ***<br/>";
       rundata.getRequest().setAttribute( HEADERS, output );
     }
     catch (Exception e){
       output = "*** Registry Parameters not found ***<br/>";
       rundata.getRequest().setAttribute( HEADERS, output );
     }

// Check for known servers and setup their addresses
// This should get the servers from the localparameters file
     if ( webserveropt != null ) {
       if ( DEBUG ) Log.debug( REGISTRYMSG + "Services " +
                                services.length + ":" + webserveropt );
       for ( int i = 0; i < services.length; i++ ) {
         if ( services[i][0].equals("") ) break;
         if ( webserveropt.equals( services[i][0] ) ) {
           webserver = services[i][1];
           service = services[i][2];
           if ( services[i].length == 4 ) soapservice = services[i][3];
         }       
       }
     }

     if ( StyleSheet == null ) {
       output = "*** Style Sheet Not Found ***<br/>";
       rundata.getRequest().setAttribute( HEADERS, output );
       return;
     }      

     if ( webserver == null || webserver.equals( "null" ) ) {
       output = "<strong>**** No Server Specified ****</strong><br/>";
       rundata.getRequest().setAttribute( HEADERS, output );
       return;
     }
     if ( value1 == null || value1.equals( "null" ) ) {
       output = "<strong>**** No Query Specified ****</strong><br/>";
       rundata.getRequest().setAttribute( HEADERS, output );
       return;
     }

     if ( port == null ) port = DEFAULTPORT;
     String out = "<em>Server:</em> " + webserver + ":" + port;
     if ( soapservice != null ) 
         out = out.concat( "<br/><em>Service:</em> " + soapservice );
     out = out.concat( "<br/>" );

     if ( DEBUG) Log.debug( REGISTRYMSG + out );
     output = out;

     String input = " (" + element1 + " " + operator1 + " " + value1 + ")";
     if ( seperator1 != null && ! seperator1.equals( "" ) )
       input = input.concat ( " " + seperator1
                 + " (" + element2 + " " + operator2 + " " + value2 + ")" );
     if ( DEBUG ) 
       Log.debug( REGISTRYMSG + "Query obtained from jsp form "
          + input + " [" + webserveropt + "*" + webserver + ":" + port + "]" );


     try {

       if ( DEBUG) Log.debug( REGISTRYMSG + out );
       output = out;

// Setup Registry Service command

       if ( DEBUG ) Log.debug( "\nSend message to: "
                               + webserver + ":" + port + "/" + service );       
       axisSOAPService connection =
               new axisSOAPService( webserver, port, service );
       String query = constructQuery( element1, operator1, value1,
               seperator1, element2, operator2, value2 );
       String querystring = printQuery( query );
       output = output.concat ( "<em>Sending Query:</em> <br/>"
                       + querystring + "<br/>" );

       if ( DEBUG) Log.debug( REGISTRYMSG + "Query: \n" + query );
       results = connection.sendRequest ( "submitQuery", query,
                                          soapservice );
       if ( DEBUG) Log.debug( REGISTRYMSG + "Result length:"
                               + results.length() );
       String htmlout = null;
       if ( results != null ) {

//  Convert Response to HTML for output
           Document doc = StringToDom( results );
           if ( doc != null )
               htmlout = DomToHtml( doc );
       }
       else htmlout = "";

// Place headers and Webservice results object in JSP context
       rundata.getRequest().setAttribute( HEADERS, output );
       rundata.getRequest().setAttribute( OUTPUT, htmlout );
// Place Webserver and port in JSP context
       rundata.getRequest().setAttribute( "agr_Server", webserver );
       rundata.getRequest().setAttribute( "agr_Port", port );
// Place Current Element and value
       rundata.getRequest().setAttribute( "agr_element1", element1 );
       rundata.getRequest().setAttribute( "agr_value1", value1 );
       Log.debug( REGISTRYMSG + "End of Portal Action");
     }
     catch (Exception e) {
       output = output + "<br/>**** Execution exception raised **** <br/>"
                       + e.toString() + "<br/>";
       rundata.getRequest().setAttribute( HEADERS, output );
       rundata.getRequest().setAttribute( OUTPUT, "" );
       Log.error( e );
     }
  }

  private String constructQuery ( String el1, String op1, String val1, 
                  String sep1, String el2, String op2, String val2 ) {
    String query = "";
    if ( op1 != null ) {
      query = query + "<selection item=\"" + el1 + 
                      "\" itemOp=\"" + op1 + 
                      "\" value=\"" + val1 + "\"/>\n";
      if ( sep1 != null && ! sep1.equals("") ) {
        query = query + "<selectionOp op=\"" + sep1 + "\"/>";
        query = query + "<selection item=\"" + el2 + 
                        "\" itemOp=\"" + op2 + 
                        "\" value=\"" + val2 + "\"/>\n";
      }
    }
    query = "<query><selectionSequence>\n" + query +
            "</selectionSequence></query>\n";
    return query;
  }

  private String printQuery ( String str ) {
    String out = "";
    int n = str.length();
    for ( int i = 0; i < n; i++ ) {
      char c = str.charAt( i );
      if ( c == '<' ) out = out.concat( "&lt;" );
      else if ( c == '>' ) out = out.concat( "&gt;" );
      else if ( c == '\n' ) out = out.concat( "<br/>" );
      else out = out.concat( String.valueOf( c ) );
    }
    return out;
  }

/** Converts an XML String toi a DOM
 */ 
  private Document StringToDom ( String in ) throws Exception {
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
      throw e;
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

  private void outstring ( StringBuffer sbuf, int indent, boolean brk,
                           String s ) {
    if ( brk ) sbuf = sbuf.append( "<br/>" );
    for ( int i = 0; i < indent; i++ ) sbuf = sbuf.append( ' ' );
    sbuf = sbuf.append( "<strong>" + s + ": </strong>" );
  }
}
