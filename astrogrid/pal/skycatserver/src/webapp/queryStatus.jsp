<%@ page import="java.io.*,
       org.w3c.dom.*,
       java.net.URL,
       java.util.Date,
       org.astrogrid.io.*,
       org.apache.commons.logging.LogFactory,
       java.security.Principal,
       org.astrogrid.account.LoginAccount,
       org.astrogrid.status.*,
       org.astrogrid.dataservice.queriers.status.*,
       org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
%>
<%@ page language="java" %>
<%!
    DataServer server = new DataServer();
%>
<html>
<head><title>Status of Query <%= request.getParameter("ID") %></title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</head>
<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>
<div id='bodyColumn'>

<%
   String queryId = request.getParameter("ID");
   
   if (queryId == null) {
      throw new IllegalArgumentException("No query 'ID' given");
   }
   
%>
<h1>Status of Query <%=ServletHelper.makeSafeForHtml(queryId) %></h1>
<p>at <%= new Date() %>
</p>
<%
   boolean isFinished = false;
   try {
      TaskStatus status = server.getQueryStatus(LoginAccount.ANONYMOUS, queryId);

      if (status == null) {
         out.write("<p>No live status information, looking in history...</p>");
         status = new StatusLogger().getPersistedStatus(queryId);
         if (status == null) {
            out.write("<p>No status information in history</p>");
            out.write("<b>Status Unavailable</b>");
            return;
         }
         
      }

      //print first status as header level 2, all following ones at header level 3
      if (status instanceof QuerierStatus) {
         out.write("<h2>"+ServletHelper.makeSafeForHtml(((QuerierStatus) status).getState().toString())+" at "+status.getTimestamp()+"</h2>");
      }
      else {
         out.write("<h2>"+ServletHelper.makeSafeForHtml(status.getStage().toString())+" at "+status.getTimestamp()+"</h2>");
      }
      out.write("<p><b>"+ServletHelper.makeSafeForHtml(status.getMessage())+"</b></p>");

      while (status != null)
      {
         String[] details= status.getDetails();
         
         for (int i=0;i<details.length;i++) {
            out.write("<p>"+ServletHelper.makeSafeForHtml(details[i])+"</p>\n");
         }
   
         if (status instanceof QuerierStatus) {
            out.write("<p>"+((QuerierStatus) status).getProgressMsg()+"</p>");
         }

         //if any status is finsihed, the whole thing is finished
         if (status.isFinished()) isFinished = true;
         
         out.write("<hr>");
         status = status.getPrevious();
         if (status != null) {
            if (status instanceof QuerierStatus) {
               out.write("<h3>"+ServletHelper.makeSafeForHtml(((QuerierStatus) status).getState().toString()+" at "+status.getTimestamp())+"</h3>");
            }
            else {
               out.write("<h3>"+ServletHelper.makeSafeForHtml(status.getStage().toString()+" at "+status.getTimestamp())+"</h3>");
            }
            out.write(ServletHelper.makeSafeForHtml(status.getMessage()));
         }
      }
         
      if (!isFinished) {
         //automatic refresh
         URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");
         out.write(ServletHelper.makeRefreshSnippet(3, statusUrl+"?ID="+queryId));
      }
         
         
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th+", getting status of query "+queryId, th);
      out.write(ServletHelper.exceptionAsHtmlPage("Getting status of query '"+queryId+"'", th));
   }

%>
</div>
</body>
</html>

