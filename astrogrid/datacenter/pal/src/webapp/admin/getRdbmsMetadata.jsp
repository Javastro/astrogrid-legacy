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
      RdbmsResourceGenerator  generator = new RdbmsResourceGenerator();
      generator.writeVoResources(out);
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(ServletHelper.exceptionAsHtmlPage(th+", Getting RDBMS Metadata", th));
   }
   
   %>

