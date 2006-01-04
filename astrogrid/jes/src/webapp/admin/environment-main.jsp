<%@page contentType="text/html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<head>
<title>Web-application environment</title>
<style type="text/css" media="all">
   @import url("../style/maven-base.css");
   @import url("../style/maven-theme.css");
</style>
</head>
<body>

<%@include file="header.xml"%>
<%@include file="navigation.xml"%>

<div id='bodyColumn'>
<h1>The web-application environment</h1>
<p>
The environment entries of this web application define its configuration.
You can set the configuration by editing the environment and re-applying
it to the application.
</p>
<ul>
<li><a href="environment-edit.jsp">View and edit the environment in your web browser.</a></li>
<li><a href="environment-tomcat.jsp">List the environment as a Tomcat context file.</a></li>
<li><a href="environment-properties.jsp">List the environment as a properties file.</a></li>
</ul>
<p>
Please note that a web application is not allow, under the J2EE rules, to alter its own
environment. Therefore, these pages cannot directly change the environment for the running
application. Instead, you make an external copy of the changed environment, and then
apply that manually to the application.
</p>

<jsp:useBean class="org.astrogrid.common.j2ee.environment.Environment"
    id="environment" scope="application"/>
<p>
To apply the environment to a Tomcat server:
</p>
<ol>
<li>List the environment as a Tomcat context file, using the link above, and save it to a file.</li>
<li>Name the file <i><c:out value="${environment.tomcatContextFileName}"/></i> after the context.</li>
<li>Copy the file into Tomcat's <i>conf/Catalina/localhost</i> directory:
this imposes the new configuration on the running web-application and makes that configuration
persistent when Tomcat restarts.</li>
</ol>
<p>You may need to wait for a minute or so for Tomcat to react to
the new configuration.</p>
</div>

<%@include file="footer.xml"%>

</body>
</html>