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
    * NVO Compliant cone search
    */
   String ra = request.getParameter("RA");
   String dec = request.getParameter("DEC");
   String sr = request.getParameter("SR");
   
   URL serviceURL = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath() + "/services/AxisDataServer");

   ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(serviceURL.toString());

   InputStream results = delegate.coneSearch(Double.parseDouble(ra), Double.parseDouble(dec), Double.parseDouble(sr));

   Piper.bufferedPipe(new InputStreamReader(results), (Writer) out);

%>

