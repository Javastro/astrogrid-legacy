<%@ page import="org.astrogrid.mySpace.mySpaceManager.MMC,
				 org.astrogrid.mySpace.mySpaceServer.MSC,
                 java.io.PrintWriter,
                 javax.naming.Context,
                 javax.naming.spi.NamingManager,
                 javax.naming.NamingException,
                 javax.naming.InitialContext"
   session="false" %>

<html>
<head>
<title>MySpace Happiness Page</title>
</head>
<body bgcolor='#ffffff'>
<h1>MySpace Happiness Page</h1>
<h2>Configuration, Testing and Web Services</h2>
<li><a href="configuration.jsp">Check and modify MySpace configuration</a></li>
<li><a href="TestServlet?all=true">Run tests</a></li>
<li><a href="tryServices.jsp">Execute Services</a></li>
</body>
</html>

