<%@ page
   import="org.astrogrid.dataservice.service.DataServer,
   org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<html>
<head>
<title>DSA/catalog Documentation</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="../navigation.xml" %>

<div id='bodyColumn'>

<h1>Configuring your Datacenter</h1>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td align="left" width="33%">
<h4><font color="#999999">&nbsp;&nbsp;&lt;&lt; Previous step</font></h4>
</td>
<td align="center" width="33%">
<h4><a href="../configure.jsp">^Index^</a></h4>
</td>
<td align="right" width="33%">
<h4><a href="./configure_step2.jsp">Next step &gt;&gt;&nbsp;&nbsp;</a></h4>
</td>
</tr>
</table>

<a name="TOMCAT_CONFIG"><h2>Step 1: Check your java and tomcat configuration</h2></a>

<h3>Important note: Required java version</h3>
<p>
The DSA/catalog component <strong>MUST BE RUN UNDER JAVA 1.5.x ("Java 5")</strong>.
</p>
<p>
The component may <strong>seem</strong> to work under Java 1.4, but it will
be subject to overflow errors when trying to return large results files.
</p>

<p>
We have explicitly tested the current DSA release on linux
under the following configurations, as part of the development
cycle:
</p>
<table border="1" cellpadding="2" cellspacing="2">
<tr><td><strong>Tomcat version</strong></td><td><strong>Java version</strong></td></tr>
<tr><td>Tomcat 5.0.28</td><td>Java 1.5.0_06</td></tr>
<tr><td>Tomcat 5.5.20</td><td>Java 1.5.0_06</td></tr>
</table>

<p>Other configurations may work and we are always grateful to
hear of configurations that are/aren't compatible
with the latest release - please email
<a href="mailto:astrogrid_dsa@star.le.ac.uk">astrogrid_dsa@star.le.ac.uk</a>.
</p>

<br/>
<h3>Special settings for tomcat 5.0.x (recommended version 5.0.28)</h3>
<p>Note that if you are using tomcat 5.0.x, you will probably
need to set the following java system property:
</p>
<pre>
    javax.xml.transform.TransformerFactory=com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl
</pre>

<p>The symptom that indicates that you need to set this property is the 
following error in the DSA self-tests:
</p>
<pre>
    javax.xml.transform.TransformerFactoryConfigurationError: 
    Provider org.apache.xalan.processor.TransformerFactoryImpl not found
</pre>

<p>If you need to, you can add this property to the JAVA_OPTS environment
variable for the
user account that runs tomcat; for example, if you currently
have the following JAVA_OPTS environment variable:
</p>
<pre>
    export JAVA_OPTS="-server"
</pre>
<p>you should change this to:
</p>
<pre>
    export JAVA_OPTS="-server -Djavax.xml.transform.TransformerFactory=com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl"
</pre>
<p>Remember to restart tomcat after making the change.  Consult
your local system administrator if you are unsure about how
to check/set environment variables.
</p>

<br/>
<h3>Tomcat user accounts</h3>
<p>
The web interface to your DSA/catalog installation contains a number of
restricted pages that require the user to log in before accessing them.
This restriction is implemented using tomcat user accounts and roles.  
</p>

<p>
Tomcat user accounts and roles are configured in the file 
<tt>$TOMCAT_HOME/conf/tomcat-users.xml</tt>, where <tt>$TOMCAT_HOME</tt> is
the toplevel directory of your tomcat installation. 
You should ensure that at least one tomcat user defined in this file 
has the role <tt>"dsaadmin"</tt>; you can then use this user account to 
access the restricted pages in your DSA/catalog installation.
</p>

<p>For example, the following user is allowed administrative access to 
the DSA/catalog installation:
</p>
<pre>
    &lt;user username="user1" password="password1" roles="dsaadmin"/&gt;
</pre>

<p>
The following user is allowed administrative access to the DSA/catalog
installation, and also to the tomcat administration and manager interfaces:
</p>
<pre>
    &lt;user username="user2" password="password2" roles="admin,manager,dsaadmin"/&gt;
</pre>

<p>
Remember that tomcat usernames and passwords are stored in plain-text, so
you may wish to check that the file permissions on the 
<tt>tomcat-users.xml</tt> file are suitably restrictive.
</p>

<p>
<em>
Migration note: the legacy role "paladmin" is also acceptable for accessing
the DSA/catalog restricted pages.
</em>
</p>


</body>
</html>
