<%@page contentType="text/html"%>

<html>
<head>
<title>Editing environment entries</title>
<style type="text/css" media="all">
  @import url("../style/maven-base.css"); 
  @import url("../style/maven-theme.css");
</style>
<style>dt.envEntryName{font-weight: bold;}</style> 
</head>
<body>

<%@include file="header.xml"%>

<div id='bodyColumn'>
<h1>Editing environment entries</h1>
<p>
These are the environment entries for the current web-application.
</p>
<p>
There are three values for each environment entry:
</p>
<ul>
<li>The default values are those defined in the deployment descriptor (web.xml)
that came with the web-application code.</li>
<li>The operational values are those that the running web-application is currently using.</li>
<li>The replacement values, which you may edit, are those that you will set if you apply this
configuration.</li>
</ul>
<p>
To reconfigure the web application, edit the replacement values, press the submit button,
and then follow the instructions on the next page.
</p>
<form action="EnvironmentServlet" method="post">
<dl>

<c:set var="avoid" value="cea.component.manager.class"/>
<jsp:useBean class="org.astrogrid.common.j2ee.environment.Environment" 
    id="environment" scope="application"/>
<%
org.astrogrid.common.j2ee.environment.EnvEntry[] entries = environment.getEnvEntry();
pageContext.setAttribute("entries", entries);
for (int i = 0; i < entries.length; i++) {
  EnvEntry e = entries[i];
%>
  <dt class="envEntryName"><%=e.getName()%>}</dt>
  <dd><table>
    <tr>
      <td>Usage</td>
      <td><%=e.getDescription()%></td>
    </tr>
    <tr>
      <td>Type</td>
      <td><%=e.getType%></td>
    </tr>
    <tr>
      <td>Default value</td>
      <td><%=e.getDefaultValue()%></td>
    </tr>
    <tr>
      <td>Operational value</td>
      <td><%=e.operationalValue()%></td>
    </tr>
  </table></dd>
<% } %>
</dl>
<p><input type="submit" value="Submit"></p>
</form>
</div>

<%@include file="footer.xml"%>

</body>
</html>
