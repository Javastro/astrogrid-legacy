<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Service metadata form</title>
        <style type="text/css" media="all">
           <%@ include file="/style/astrogrid.css" %>
        </style>
        <%@ include file="/style/link_options.xml" %>
    </head>
    <body>

    <%@ include file="/style/header.xml" %>
    <%@ include file="/style/navigation.xml" %>

      <div id='bodyColumn'>

        <h1>Recording metadata from VOSI</h1>
        <form action="ServiceMetadata" method="post">
          <input type="hidden" name="IVORN" value="<%=request.getParameter("IVORN")%>"/>
          <table>
            <tr>
              <td>IVO identifier for resource</td>
              <td><%=request.getParameter("IVORN")%>
            <tr>
              <%if(request.getParameter("appResource") != null &&
                   request.getParameter("appResource").equals("true")) { %>
              <td>URL for getting application data</td>
              <td><input type="text" name="VOSI_AppData" size="48"/></td>
              <td><a href="../help/capabilities.html">help</a></td>                
              <% } else { %>
              <td>URL for getting service capabilities.</td>
              <td><input type="text" name="VOSI_Capabilities" size="48"/></td>
              <td><a href="../help/capabilities.html">help</a></td>
              <% } %>
            </tr>
          </table>
          <p><input type="submit" value="Update the registry entry"/></p>
        </form>
      </div>
     <%@ include file="/style/footer.xml" %>
    </body>
</html>

