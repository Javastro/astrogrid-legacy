<%@ page
import="org.astrogrid.dataservice.service.DataServer"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>PAL Documentation</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<!-- packrat requirments
1 INSTALL document per component, to include:
Where and how to download the installation kit
How to deploy and configure
How to administer (stop/start/restart)
How to trouble-shoot
-->
<h1>Publisher's AstroGrid Library Documentation</h1>
<p>
Here you will find documents on how to install, configure and if necessary write special
plugins to the Publisher's AstroGrid Library (PAL).
<p>
Note that to use the 'admin' pages, you will need to sign on with the role 'paladmin'.
In Tomcat, you can edit the .../conf/tomcat-users.xml file to include the
role 'paladmin' in your set of roles.
</div>

</body>
</html>
