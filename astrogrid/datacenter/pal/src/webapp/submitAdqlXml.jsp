<% // NB Don't allow any blank lines to be printed before the results %><%@
   page import="java.io.*,
       java.net.URL,
       org.apache.commons.logging.*,
       org.astrogrid.store.Agsl,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.service.*,
       org.astrogrid.datacenter.returns.*,
       org.astrogrid.datacenter.query.AdqlQuery,
       org.astrogrid.io.*"
   isThreadSafe="false"
   session="false"
%><%@ page language="java" %><%!
    DataServer server = new DataServer();
%><%
   /**
    * Runs ADQL query from given ADQL string
    */
   String adqlXml = request.getParameter("AdqlXml");
   
   try {
      ReturnSpec returns = ServletHelper.makeReturnSpec(request);

      if (returns.getTarget() == null) {
         returns.setTarget(new TargetIndicator(out));
         server.askQuery(Account.ANONYMOUS, new AdqlQuery(adqlXml), returns);
      }
      else {
         String id = server.submitQuery(Account.ANONYMOUS, new AdqlQuery(adqlXml), returns);

         URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");
         //indicate status
         out.write("Adql Query has been submitted, and assigned ID "+id+".  <a href='"+statusUrl+"?ID="+id+"'>Query Status Page</a>\n");
         //redirect to status
         out.write("<META HTTP-EQUIV='Refresh' CONTENT='0;URL="+statusUrl+"?ID="+id+"'>");
      }
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(ServletHelper.exceptionAsHtmlPage("Asking ADQL/xml ", th, adqlXml));
   }

%>
