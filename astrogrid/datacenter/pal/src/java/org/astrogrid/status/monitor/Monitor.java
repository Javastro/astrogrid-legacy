/*
 * $Id: Monitor.java,v 1.1 2004/10/01 18:04:59 mch Exp $
 */

package org.astrogrid.status.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
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
 
   /** Set up with default list of datacenters */
   protected void initialise() {
      datacenters = new Vector();
      datacenters.add("//http://grendel12.roe.ac.uk:8080/pal-6df");
      datacenters.add("http://grendel12.roe.ac.uk:8080/pal-sec");
      datacenters.add("http://grendel12.roe.ac.uk:8080/pal-vizier");
      datacenters.add("http://astrogrid.roe.ac.uk:8080/pal-ssa");
      datacenters.add("http://astrogrid.roe.ac.uk:8080/pal-usnob");
      datacenters.add("http://astrogrid.roe.ac.uk:8080/pal-twomass");
      datacenters.add("http://zhumulangma.star.le.ac.uk:8080/astrogrid-pal-SNAPSHOT");
         
      ceaServices = new Vector();
      ceaServices.add("//http://grendel12.roe.ac.uk:8080/pal-6df");
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
                   "<th>Endpoint</th><th>Status</th>"+
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
      out.println("<td>"+endpoint+"</td>");
      try {
         TimeStamp timestamp = new TimeStamp();
         URL url = new URL(endpoint+"/serverStatus.jsp");
         InputStream in = url.openStream();
         //could parse it but can't be bothered just now. Should really do
         //something more sensible anyway
         out.println("<td>"+timestamp.getSecsSince()+"s</td>");
      }
      catch (MalformedURLException mue) {
         out.println("<td bgcolor='#FF0000'>"+mue.getMessage()+"</td>");
      }
      catch (IOException ioe) {
         out.println("<td bgcolor='#FF0000'>"+ioe.getMessage()+"</td>");
      }
      out.println("</tr>");
      
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
}

