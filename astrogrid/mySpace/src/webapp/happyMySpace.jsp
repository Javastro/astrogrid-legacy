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
<h2>Try to load some properties</h2>
<h3>MySpace Manager</h3>
First check the properties file has been found:
<%
try {
	MMC.getInstance().checkPropertiesLoaded();
	%>OK<%
	} catch (Exception e) {
	%>Whoops - caught exception:<P><%
	e.printStackTrace(new PrintWriter(out));
	}
%>
<P>
Version in property file is: "<%=MMC.getProperty(MMC.GENERAL_VERSION_NUMBER, MMC.GENERAL_CATEGORY) %>" - should be "1.2".<P>
MySpaceManagerURL :<%=MMC.getProperty(MMC.mySpaceManagerLoc,MMC.CATLOG)%><P>
MySpaceServerURL :<%=MMC.getProperty(MMC.serverManagerLoc,MMC.CATLOG)%><P>
MySpaceServerURLs :<%=MMC.getProperty(MMC.MYSPACEMANAGERURLs,MMC.CATLOG)%><P>
<P>
<h3>MySpace Server</h3>
First check the properties file has been found:
<%
try {
	MSC.getInstance().checkPropertiesLoaded();
	%>OK<%
	} catch (Exception e) {
	%>Whoops - caught exception:<P><%
	e.printStackTrace(new PrintWriter(out));
	}
%>
<P>
Version in property file is: "<%=MSC.getProperty(MMC.GENERAL_VERSION_NUMBER, MMC.GENERAL_CATEGORY) %>" - should be "1.2".<P>
DATAHOLDERFOLDER :<%=MSC.getProperty(MSC.dataHolderFolder,MSC.CATLOG)%><P>
<P>
Try executing some <A href="tryServices.jsp">webservices.</a>
</body>
</html>

