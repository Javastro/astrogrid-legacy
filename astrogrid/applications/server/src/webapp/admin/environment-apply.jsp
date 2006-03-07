<%@page contentType="text/html" import="java.io.*, java.net.URL"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<head>
<title>Applied environment to web-app</title>
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
File confLocalhost = new File(System.getProperty("catalina.home"), "conf/Catalina/localhost");
File contextFile = new File(confLocalhost, environment.getTomcatContextFileName());
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
<h1>Environment applied</h1>
<p>
Your settings have been copied to <i><%=contextFilePath%></i>.
</p>
<p>
<strong>The web application should now be restarting itself.</strong>
It may become unavailable for a minute or so.
</p>
</div>

<%@include file="footer.xml"%>

</body>
</html>