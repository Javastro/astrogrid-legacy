<%@ page import="org.astrogrid.applications.common.config.CeaControllerConfig,
                 java.io.PrintWriter,
                 javax.naming.Context,
                 javax.naming.spi.NamingManager,
                 javax.naming.NamingException,
                 javax.naming.InitialContext"
   session="false" %>

<html>
<head>
<title>Application Controller Config Happiness Page</title>
</head>
<body bgcolor='#ffffff'>
<h1>Application Controller Config Happiness Page</h1>

<!-- will need to do this differently config is not supposed to be visible outside IoC -->
<%

CeaControllerConfig appConConfig = CeaControllerConfig.getInstance();

%>
<%= appConConfig.toHTMLReport() %>

</body>
</html>

