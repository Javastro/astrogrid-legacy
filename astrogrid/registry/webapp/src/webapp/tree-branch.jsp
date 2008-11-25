<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
      <title>Resource IDs beginning with <%=request.getAttribute("ivorn")%></title>
      <style type="text/css" media="all">
        @import url("<%=request.getContextPath()%>/style/astrogrid.css");
      </style>
    </head>
    <body>
      <%@ include file="/style/header.xml" %>
      <%@ include file="/style/navigation.xml" %>
      <div id="bodyColumn">
        <div class="contentBox">
          <h1>Resource IDs beginning with <%=request.getAttribute("ivorn")%></h1>
          <ul>
            <%
            java.util.Collection c = (java.util.Collection) request.getAttribute("hrefs");
            for (Object o : c) {
            %>
            <li><a href="<%=o%>"><%=o%></a></li>
            <%
            }
            %>
          </ul>
        </div>
      </div>
    </body>
</html>
