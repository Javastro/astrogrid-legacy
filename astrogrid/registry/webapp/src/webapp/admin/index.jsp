<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry Administration Pages</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Administration</h1>
<p>
These are the AstroGrid Registry administration pages.

<p>
<p>To have your registry mirrored by other registries - for them to harvest this
one to pick up resources that have been registered on it - you must copy
your registryType entry into those registries. (This is optional)</p>
<ul>
<li><a href="../admin/registerSelfInExternalRegistry.jsp">Register with other Registries</a></li>
</ul>

<p>To make your registry mirror other registries - for it to 'harvest' them regularly
to pick up resources that have been registered on them - you must copy their
registryType entries into your registry. (This is optional)</p>

<ul>
<li><a href="../admin/registerExternalRegistryInSelf.jsp">Harvesting other Registries</a></li>
</ul>


</p>
</body>
</html>

