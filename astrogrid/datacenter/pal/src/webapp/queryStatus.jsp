<%@ page import="java.io.*,
       org.w3c.dom.*,
       java.net.URL,
       java.util.Date,
       org.astrogrid.io.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.queriers.status.*,
       org.astrogrid.datacenter.service.*"
   isThreadSafe="false"
   session="false"
%>
<%@ page language="java" %>
<%!
    DataServer server = new DataServer();
%>
<html>
<head><title>Status of Query<%= request.getParameter("ID") %></title>
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
   boolean isFinished = false;
   try {
      QuerierStatus status = server.getQueryStatus(Account.ANONYMOUS, queryId);
%>
<h1>Status of Query <%=ServletHelper.makeSafeForHtml(queryId) %></h1>
<p>at <%= new Date() %>
</p>
         
<%   if (status == null) { %>
         <b>No Query found for that ID</b>
<%   } %>
     
<h2> <%= ServletHelper.makeSafeForHtml(status.getState().toString()) %></h2>

<p>
<b>  <%= ServletHelper.makeSafeForHtml(status.getMessage()) %></b>
</p>
      
<%
      while (status != null) {
         String[] details= status.getDetails();
         
         for (int i=0;i<details.length;i++) {
            out.write("<p>"+ServletHelper.makeSafeForHtml(details[i])+"</p>\n");
         }
   
         out.write("<p>"+status.getProgressMsg()+"</p>");

         //if any status is finsihed, the whole thing is finished
         if (status.isFinished()) isFinished = true;
         
         out.write("<hr>");
         status = status.getPrevious();
         if (status != null) {
            out.write("<h3>"+ServletHelper.makeSafeForHtml(status.getState().toString()+" at "+status.getTimestamp())+"</h3>");
            out.write(ServletHelper.makeSafeForHtml(status.getMessage()));
         }
      }
         
      if (!isFinished) {
         //automatic refresh
         URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");
         out.write(ServletHelper.makeRefreshSnippet(3, statusUrl+"?ID="+queryId));
      }
         
         
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(ServletHelper.exceptionAsHtmlPage("Getting status of query '"+queryId+"'", th));
   }

%>
</div>
</body>
</html>

