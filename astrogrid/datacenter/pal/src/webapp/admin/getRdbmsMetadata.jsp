<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.apache.commons.logging.*,
       org.astrogrid.datacenter.queriers.sql.*,
       org.astrogrid.datacenter.metadata.*,
       org.astrogrid.datacenter.service.*"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%
   try {
      RdbmsResourceGenerator  plugin = new RdbmsResourceGenerator();
      String resources[] = plugin.getVoResources();
      out.write(VoDescriptionServer.VODESCRIPTION_ELEMENT);
      for (int i = 0; i < resources.length; i++) {
         out.write(resources[i]);
      }
      out.write(VoDescriptionServer.VODESCRIPTION_ELEMENT_END);
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(ServletHelper.exceptionAsHtmlPage(th+", Getting RDBMS Metadata", th));
   }
   
   %>

