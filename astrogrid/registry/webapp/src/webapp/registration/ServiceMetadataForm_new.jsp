<%@page contentType="text/html"%>
<%@page pageEncoding="iso-8859-1"%>
<%@page import=" org.astrogrid.config.SimpleConfig,
                 org.astrogrid.registry.server.http.servlets.helper.JSPHelper,
                 org.w3c.dom.NodeList,
                 org.w3c.dom.Element,
                 org.w3c.dom.Document,
                 org.astrogrid.util.DomHelper,
                 org.astrogrid.registry.server.http.servlets.Log4jInit,
                 org.astrogrid.xmldb.client.XMLDBManager,
                 org.astrogrid.registry.common.RegistryDOMHelper,
                 org.astrogrid.registry.server.query.*,
                 org.astrogrid.store.Ivorn,
                 org.apache.axis.utils.XMLUtils,
                 java.net.*,
                 java.util.*,
                 java.io.*"
   isThreadSafe="false"
   session="false"
%>
<%
String ivorn = request.getParameter("IVORN");
String encodedIvorn = URLEncoder.encode(ivorn, "iso-8859-1");
%>
<!DOCTYPE HTML  PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>VAMDC Registry Access Pages</title>
<meta http-equiv="Content-type" content="text/xhtml;charset=iso-8859-1">
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
	      <input type="hidden" name="IVORN" value="<%=request.getParameter("IVORN")%>">
          <table>
	        <tr>
		      <td>IVO identifier for resource</td>
		      <td><%=request.getParameter("IVORN")%>
            <tr>
              <%if(request.getParameter("appResource") != null &&
                   request.getParameter("appResource").equals("true")) { %>
              <td>URL for getting application data</td>
              <td><input type="text" name="VOSI_AppData" size="48"></td>
              <td><a href="help/capabilities.jsp">help</a></td>                   
              <% } else { %>
              <td>URL for getting service capabilities.</td>
              <td><input type="text" name="VOSI_Capabilities" size="48"></td>
              <td><a href="help/capabilities.jsp">help</a></td>
              <% } %>
            </tr>
          </table>
          <p><input type="submit" value="Update the registry entry"></p>
        </form>
      </div>
    <%@ include file="/style/footer.xml" %>
    </body>
</html>
