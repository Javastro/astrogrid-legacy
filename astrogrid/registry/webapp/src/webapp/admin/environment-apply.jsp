<%@page contentType="text/html" import="java.io.*, java.net.URL"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<head>
<title>Applied environment to web-app</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</head>
<body class="composite">
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<jsp:useBean class="org.astrogrid.common.j2ee.environment.Environment"
    id="environment" scope="application"/>
<%
File confLocalhost = new File(System.getProperty("catalina.home"), "conf/Catalina/localhost");
File contextFile = new File(confLocalhost, environment.getTomcatContextFileName());
String contextFilePath = contextFile.getAbsolutePath();
String endpoint = request.getScheme() +
                  "://" +
                  request.getLocalName() +
                  ":" + 
                  new Integer(request.getLocalPort()).toString() +
                  environment.getContextPath() +
                  "/environment-tomcat.jsp";
org.astrogrid.io.Piper.pipe((new URL(endpoint)).openStream(),new java.io.FileOutputStream(contextFile));
%>

<div id='bodyColumn'>
<h1>Environment applied</h1>
<p>
Your settings have been copied to <i><%=contextFilePath%></i>.
</p>
<p>
<strong>The web application should now be restarting itself.</strong>
It may become unavailable for a minute or so.
(Tomcat 5.5 has been known not to pick up changes requiring a restart of tomcat, See the 'fingerprint' link in the menu to verify properties.)
</p>
</div>
<%@ include file="/style/footer.xml" %>

</body>
</html>
