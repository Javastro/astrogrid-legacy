<%@page contentType="text/html" import="java.io.*, java.net.URL"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<head>
<title>Saved environment for web-app</title>
<style type="text/css" media="all">
  @import url("../style/maven-base.css"); 
  @import url("../style/maven-theme.css");
</style>
</head>
<body class="composite">

<%@include file="header.xml"%>

<jsp:useBean class="org.astrogrid.common.j2ee.environment.Environment"
    id="environment" scope="application"/>
<%
org.astrogrid.applications.manager.BaseConfiguration configuration = new org.astrogrid.applications.manager.BaseConfiguration();
File contextFile = new File(configuration.getConfigurationDirectory(), environment.getTomcatContextFileName());
String contextFilePath = contextFile.getAbsolutePath();
String endpoint = request.getScheme() +
                  "://" +
                  request.getLocalName() +
                  ":" + 
                  new Integer(request.getLocalPort()).toString() +
                  environment.getContextPath() +
                  "/admin/environment-tomcat.jsp";
configuration.copyUrlToFile(new URL(endpoint), contextFile);
%>

<div id='bodyColumn'>
<h1>Environment saved (but not yet applied)</h1>
<p>
Your settings have been saved to disc in the file
<i><%=contextFilePath%></i>.
</p>
<form action="environment-apply.jsp">
<p>
<strong>The CEC isn't using these settings yet; you still have to apply them.</strong>
</p>
<p>
Applying the settings will restart your web application (so you may not get any response
in your browser for a minute or so).
</p>
<p>
<input type="submit" value="Apply settings"/>
<p>
</form>
</div>

<%@include file="footer.xml"%>

</body>
</html>