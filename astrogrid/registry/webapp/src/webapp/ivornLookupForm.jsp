<%@ page import="org.astrogrid.config.SimpleConfig,
                 org.astrogrid.registry.server.query.*,
                 java.util.ArrayList"
   isThreadSafe="false"
   session="false"
%>

<%
      RegistryQueryService server = new RegistryQueryService();
      ArrayList al = server.getAstrogridVersions();
%>

<html>
<head>
<title>AstroGrid Registry Access Pages</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>View Resource</h1>

<p>
Enter the Identifier for the entry you want to view.
</p>

<form action="viewResourceEntry.jsp" method="post">
<p>
Resource identifier
 Version:
<select name="version">
   <% for(int k = (al.size()-1);k >= 0;k--) { %>
      <option value="<%=al.get(k)%>"><%out.print(((String)al.get(k)).replaceAll("_","."));%></option>  
   <%}%>
</select>
 <br />
 ivo://<input name="IVORN" type="text" value="" size="60"/>
 <input type="submit" name="button" value="Find"/>
</p>
<p>
Examples:<br/>
roe.ac.uk/DSA_6dF/rdbms<br/>
org.astrogrid.test/registry
</p>
</form>


</body>
</html>

