<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.apache.commons.logging.*,
       org.astrogrid.tableserver.jdbc.*,
       org.astrogrid.dataservice.metadata.*,
       org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%
   try {
      RdbmsTableMetaDocGenerator  generator = new RdbmsTableMetaDocGenerator();
      generator.writeTableMetaDoc(out);
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th+" getting rdbms metadata",th);
      out.write(ServletHelper.exceptionAsHtmlPage(th+", Getting RDBMS Metadata", th));
   }
%>

