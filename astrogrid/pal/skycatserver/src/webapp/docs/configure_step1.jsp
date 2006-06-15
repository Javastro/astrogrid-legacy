<%@ page
   import="org.astrogrid.dataservice.service.DataServer,
   org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>

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
<%@ include file="navigation.xml" %>

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

<a name="TOMCAT_CONFIG"><h2>Step 1: Check your tomcat user configuration</h2></a>
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
