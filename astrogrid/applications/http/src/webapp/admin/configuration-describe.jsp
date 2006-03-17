<%@page contentType="text/html" import="java.io.*, java.net.URL"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<head>
<title>Current configuration of CEC</title>
<style type="text/css" media="all">
  @import url("../style/maven-base.css"); 
  @import url("../style/maven-theme.css");
</style>
</head>
<body class="composite">

<%@include file="header.xml"%>

<jsp:useBean class="org.astrogrid.applications.manager.BaseConfiguration"
    id="configuration" scope="application"/>
    
<div id='bodyColumn'>
<h1><jsp:getProperty name="configuration" property="name"/></h1>
<pre style="font-size: 150%;">
<jsp:getProperty name="configuration" property="description"/>
</pre>
</div>

<%@include file="footer.xml"%>

</body>
</html>