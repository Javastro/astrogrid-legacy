<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.metadata.MetadataServer,
       org.astrogrid.datacenter.service.HtmlDataServer"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%@ page language="java" %><%
   try {
      MetadataServer.getMetadata();
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(HtmlDataServer.exceptionAsHtml("Getting metadata", th));
   }
      

%>

