<%@ page import="org.astrogrid.registry.server.query.*,
				     org.astrogrid.registry.server.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.w3c.dom.Element,
                 org.w3c.dom.NodeList,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Browse Registred Resources</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<%
   RegistryQueryService server = new RegistryQueryService();
   ArrayList al = server.getAstrogridVersions();
%>

<p>
When you hit submit, a XML screen will show you all the xml of identifiers that have authority ids managed by this registry.
You will need to save the xml yourself from your browser.<br />
</p>
<form action="backupData.jsp" method='get'>
<p>
Version: 
<select name="version">
   <% for(int k = (al.size()-1);k >= 0;k--) { %>
      <option value="<%=al.get(k)%>"><%out.print(((String)al.get(k)).replaceAll("_","."));%></option>  
   <%}%>
</select>
<br/>
<input type="submit" name="button" value='List'/>
</p>
</form>
</body>
</html>