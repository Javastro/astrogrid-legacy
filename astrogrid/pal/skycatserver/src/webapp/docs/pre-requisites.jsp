<%@ page
   import="org.astrogrid.dataservice.service.DataServer"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>PAL Installation </title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Introduction</h1>
<p>
The Astrogrid Datacenter Server, PAL, relies upon the following software components which must be
installed and configured before installing the Datacenter Server.
</p>

<h1>Java Virtual Machine</h1>
<p>
Requires Java (J2SE) version 1.4 or higher. Available from <a href="http://java.sun.com/j2se/downloads.html">http://java.sun.com/j2se/downloads.html</a>
</p>

<h1>Web Service Manager</h1>

PAL requires a web service manager to present itself to the web.  We have used and tested it with
Tomcat, but others include WebSphere and JBoss.

<h2>Tomcat Server</h2>
<p>
Requires version 4.1 or later. Available from <a href="http://jakarta.apache.org/tomcat/">http://jakarta.apache.org/tomcat/</a>;
Installation instructions <a
href="http://jakarta.apache.org/tomcat/tomcat-4.1-doc/RUNNING.txt">http://jakarta.apache.org/tomcat/tomcat-4.1-doc/RUNNING.txt</a>.
Other servlet containers should work, provided they support version 2.2 of the Servlets Spec.
<p/>
Set shell variable <tt>CATALINA_HOME</tt> to the root of your tomcat installation, as described in the installation instructions.
</p>


<h1>References<h1>
<ul>
<li><a href="http://www.astrogrid.org">Astrogrid Homepage</a>
</li><li><a href="http://java.sun.com">Java Homepage</a>
</li><li><a href="http://jakarta.apache.org/tomcat">Jakarta Tomcat Homepage</a>
</li><li><a href="http://ws.apache.org/axis/">Apache Axis Homepage</a>
</li></ul>

</div>
</body>
</html>

