<%@ page import="java.io.*,
       org.w3c.dom.*,
       java.net.URL,
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
<head><title>Status of Query<%= request.getParameter("ID") %>
<style type="text/css" media="all">
          @import url("./style/maven-base.css");
          @import url("./style/maven-theme.css");
</style>
</title></head>
<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>
<div id='bodyColumn'>

<%
   String queryId = request.getParameter("ID");
   try {
      QuerierStatus status = server.getQueryStatus(Account.ANONYMOUS, queryId);
%>
<h1>Status of Query <%=ServletHelper.makeSafeForHtml(queryId) %></h1>

<%   if (status == null) { %>
         <b>No Query found for that ID</b>
<%   } %>
     
<h2> <%= ServletHelper.makeSafeForHtml(status.getState().toString()) %></h2>

<p>
<b>  <%= ServletHelper.makeSafeForHtml(status.getNote()) %></b>
</p>
      
<%
      String[] details= status.getDetails();
      
      for (int i=0;i<details.length;i++) {
         out.write("<p>"+ServletHelper.makeSafeForHtml(details[i])+"</p>\n");
      }

      out.write("<p>"+status.getProgressMsg()+"</p>");
      
      if (!(status instanceof QuerierClosed)) {
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

