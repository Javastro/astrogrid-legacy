/*
 * $Id: Monitor.java,v 1.1 2009/05/13 13:20:37 gtr Exp $
 */

package org.astrogrid.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.encoding.XMLType;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.astrogrid.status.ServiceStatus;
import org.astrogrid.util.TimeStamp;

/**
 * Servlet for monitoring the statuses of other services and displaying them
 * in HTML
 *
 * @author mch
 */
public class Monitor  {

   Vector datacenters = null;
   Vector ceaServices = null;

   final static String ERR_COLOUR = "#FF0000";
   final static String OK_COLOUR = "#00FF00";
   final static String WARN_COLOUR = "#FFFF00";
   
   /** Set up with default list of datacenters */
   protected void initialise() {
      datacenters = new Vector();
      datacenters.add("http://grendel12.roe.ac.uk:8080/pal-6df");
      datacenters.add("http://grendel12.roe.ac.uk:8080/pal-sec");
      datacenters.add("http://grendel12.roe.ac.uk:8080/pal-vizier");
      datacenters.add("http://astrogrid.roe.ac.uk:8080/pal-ssa");
      datacenters.add("http://astrogrid.roe.ac.uk:8080/pal-usnob");
      datacenters.add("http://astrogrid.roe.ac.uk:8080/pal-twomass");
      datacenters.add("http://zhumulangma.star.le.ac.uk:8080/astrogrid-pal-SNAPSHOT");
      datacenters.add("http://msslxy.mssl.ucl.ac.uk:8080/astrogrid-pal-fits-SNAPSHOT");
      datacenters.add("http://ag01.ast.cam.ac.uk:8080/astrogrid-pal-Itn05_release");
      datacenters.add("http://twmbarlwm.star.le.ac.uk:8888/astrogrid-pal-SNAPSHOT");
      datacenters.add("http://twmbarlwm.star.le.ac.uk:8888/astrogrid-pal-fits-SNAPSHOT");
      datacenters.add("http://twmbarlwm.star.le.ac.uk:8888/astrogrid-pal-cds-SNAPSHOT");
      datacenters.add("http://twmbarlwm.star.le.ac.uk:8888/astrogrid-pal-sec-SNAPSHOT");
         
      ceaServices = new Vector();
      ceaServices.add("http://grendel12.roe.ac.uk:8080/pal-6df");
      ceaServices.add("http://grendel12.roe.ac.uk:8080/pal-sec");
      ceaServices.add("http://grendel12.roe.ac.uk:8080/pal-vizier");
      ceaServices.add("http://astrogrid.roe.ac.uk:8080/pal-ssa");
      ceaServices.add("http://astrogrid.roe.ac.uk:8080/pal-usnob");
      ceaServices.add("http://astrogrid.roe.ac.uk:8080/pal-twomass");
      ceaServices.add("http://zhumulangma.star.le.ac.uk:8080/astrogrid-pal-SNAPSHOT");
   }
   
   public Monitor() {
      initialise();
   }
   
   /**
    * Finds out statuses of all services and makes an Html table to the given
    * Writer.  Does it this way so we can flush as we write, in case some servers
    * timeout, etc
    */
   public void writeHtmlTables(PrintWriter out)  throws IOException {

      out.print("<h3>Datacenters</h3>");
      out.print("<table>"+
                   "<tr>"+
                   "<th>Endpoint</th><th>JSP</th><th>SOAP</th>"+
                   "</tr>");
      
      Enumeration datacenter = datacenters.elements();
      while (datacenter.hasMoreElements()) {
         out.flush();
         String endpoint = (String) datacenter.nextElement();
         writeDatacenterStatusRow(endpoint, out);
      }
      out.print("</table>");
      out.flush();
   }
   
   
   
   protected void writeDatacenterStatusRow(String endpoint, PrintWriter out) {
      out.println("<tr>");
      out.println("<td><a href='"+endpoint+"'>"+endpoint+"</a></td>");
      try {
         //test JSP connection
         TimeStamp timestamp = new TimeStamp();
         URL url = new URL(endpoint+"/serverStatus.jsp");
         InputStream in = url.openStream();
         String bgcolor=OK_COLOUR;
         if (timestamp.getSecsSince()>5) { bgcolor=WARN_COLOUR; }
         out.println("<td bgcolor='"+bgcolor+"'>"+timestamp.getSecsSince()+"s</td>");
      }
      catch (MalformedURLException mue) {
         out.println("<td bgcolor='"+ERR_COLOUR+"'>"+mue.getMessage()+"</td>");
      }
      catch (IOException ioe) {
         out.println("<td bgcolor='"+ERR_COLOUR+"'>"+ioe.getMessage()+"</td>");
      }
         
      //test SOAP connection
      try {
         TimeStamp timestamp = new TimeStamp();
         ServiceStatus status = getDatacenterStatus(endpoint);
         out.println("<td bgcolor='"+OK_COLOUR+"'>"+timestamp.getSecsSince()+"s</td>");
      }
      catch (ServiceException e) {
         out.println("<td bgcolor='"+ERR_COLOUR+"'>"+e+"</td>");
      }
      catch (RemoteException e) {
         out.println("<td bgcolor='"+ERR_COLOUR+"'>"+e+"</td>");
      }
         
      out.println("</tr>\n");
      
   }
   
   public static ServiceStatus getDatacenterStatus(String endpoint) throws ServiceException, RemoteException {
      Service  service = new Service();
      Call     call    = (Call) service.createCall();
      
      call.setTargetEndpointAddress( endpoint+"/services/AxisDataService06");
      call.setOperationName("getServiceStatus");
      
      //call.setReturnType( XMLType.XSD_STRING );
      
      Object response = call.invoke( new Object[] {  } );
      
      return null;
   }
   
   public static String getSimpleDatacenterStatus(String endpoint) throws ServiceException, RemoteException {
      Service  service = new Service();
      Call     call    = (Call) service.createCall();
      
      call.setTargetEndpointAddress( endpoint+"/services/AxisDataService06");
      call.setOperationName("getSimpleServiceStatus");
      
      call.setReturnType( XMLType.XSD_STRING );
      
      return (String) call.invoke( new Object[] {  } );
   }
   /*
   public String getCeaStatus(String endpoint) {
      Call call = getCall();
      SOAPBodyElement sbeRequest =
                                 new SOAPBodyElement();
      sbeRequest.setName("submitQuery");
      try {
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
         Document resultDoc = sbe.getAsDocument();
      }catch(RemoteException re) {
         log.debug(re);
      } catch (Exception e) {
         resultDoc = null;
         e.printStackTrace();
         log.debug(e);
   }

   private Call getCall() {
      log.debug("entered getCall()");
      if(DEBUG_FLAG) log.info("entered getCall()");
      Call _call = null;
      try {
         Service  service = new Service();
         _call = (Call) service.createCall();
         _call.setTargetEndpointAddress(this.endPoint);
         _call.setSOAPActionURI("");
         _call.setOperationStyle(org.apache.axis.enum.Style.MESSAGE);
         _call.setOperationUse(org.apache.axis.enum.Use.LITERAL);
         _call.setEncodingStyle(null);
      } catch(ServiceException se) {
         se.printStackTrace();
         log.debug(se);
         _call = null;
      }finally {
         log.debug("exiting getCall()");
         if(DEBUG_FLAG) log.info("exiting getCall()");
         return _call;
      }
   }
    */
   
   /**
    *
    */
   public static void main(String[] args) throws IOException {
      Monitor m = new Monitor();
      PrintWriter out =new PrintWriter(new OutputStreamWriter(System.out));
      out.write("<html><body>");
      m.writeHtmlTables(out);
      out.write("</body></html>");
   }
}

