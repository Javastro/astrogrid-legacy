<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.net.*"%>

<%
String ivorn = request.getParameter("IVORN");
String encodedIvorn = URLEncoder.encode(ivorn, "UTF-8");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>Editing options for a registry entry</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style type="text/css" media="all">
      <%@ include file="/style/astrogrid.css" %>
    </style>
    <%@ include file="/style/link_options.xml" %>
    </head>
    <body>

<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

      <div id='bodyColumn'>
        <h1>Editing options for <%=ivorn%></h1>
        <ul>
          <% if(!contractVersion.equals("0.1")) { %>
          <li><a href="DublinCore?IVORN=<%=encodedIvorn%>">Edit core information</a></li>
          <li><a href="ServiceMetadata?IVORN=<%=encodedIvorn%>">Edit metadata (service/application) via VOSI</a></li>
          <li><a href="Coverage?IVORN=<%=encodedIvorn%>">Edit coverage</a></li>
          <% } %>
          <li><a href="../admin/editEntry.jsp?IVORN=<%=encodedIvorn%>">Edit XML text</a> (low level)</li>
        </ul>
     </div>
    <%@ include file="/style/footer.xml" %>
   </body>
</html>

