<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.util.DomHelper,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.metadata.MetadataServer,
       org.astrogrid.datacenter.admin.*,
       org.astrogrid.datacenter.service.HtmlDataServer"
   isThreadSafe="false"
   session="false"
%>
<html>
<head>
<title>Metadata</title>
</head>
<body bgcolor=#ffffff>
<h1>Configured Metadata (file)</h1>
<%
   try {
      Document metadata = MetadataServer.getMetadata();
      out.write("<pre>\n");
      DomHelper.DocumentToWriter(out);
      out.write("</pre>\n");
      
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(HtmlDataServer.exceptionAsHtml("Getting metadata", th));
   }

%>
<hr>
<h1>Generated Metadata (direct from plugin)</h1>
<%
   try {
      out.write("<pre>\n");
      MetadataGenerator.writeMetadata(out);
      out.write("</pre>\n");
      
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(HtmlDataServer.exceptionAsHtml("Getting metadata", th));
   }

%>

</body>
</html>


