<%@ page import="java.io.*,
       java.net.URL,
       org.w3c.dom.*,
       org.apache.axis.utils.XMLUtils,
       org.astrogrid.datacenter.delegate.*,
       org.astrogrid.io.*,
       org.astrogrid.community.User,
       org.astrogrid.datacenter.adql.generated.*"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%>

<%@ page language="java" %>

<%
   /**
    * Simple image access protocol
    */
   
   String pos = request.getParameter("POS");
   String size = request.getParameter("SIZE");
   String formatList = request.getParameter("FORMAT");

   int comma = pos.indexOf(",");
   String ra = pos.substring(0,comma);
   String dec = pos.substring(comma+1);
   
   URL serviceURL = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath() + "/services/AxisDataServer");

   ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(serviceURL.toString());

   InputStream results = delegate.coneSearch(Double.parseDouble(ra), Double.parseDouble(dec), Double.parseDouble(size));

   Piper.bufferedPipe(new InputStreamReader(results), (Writer) out);

%>

