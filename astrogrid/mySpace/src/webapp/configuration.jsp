<%@ page import="org.astrogrid.mySpace.mySpaceManager.MMC,
				 org.astrogrid.mySpace.mySpaceServer.MSC,
                 java.io.PrintWriter,
                 javax.naming.Context,
                 javax.naming.spi.NamingManager,
                 javax.naming.NamingException,
                 javax.naming.InitialContext"
   session="false" %>

<%
//Do something here to see if called from configuration.jsp
//and set vars in MMC appropriately.  Make sure you distinguish between MMC
//and MSC
%>	 
	 
	 
	 
<html>
<head>
<title>MySpace Configuration Page</title>
</head>
<body bgcolor='#ffffff'>
<h2>MySpace Configuration Page</h2>
<%
//First check the properties file has been found:
try {
	MMC.getInstance().checkPropertiesLoaded();
	%>MySpaceManager Configuration file located<%
	} catch (Exception e) {
	%>Exception locating MySpaceManager configuration file:<P><%
	e.printStackTrace(new PrintWriter(out));
	}
%>
<h3>MySpace Manager</h3>
<form action="configuration.jsp" method="post">
Version: <input type="text" name="version" value="<%=MMC.getProperty(MMC.GENERAL_VERSION_NUMBER, MMC.GENERAL_CATEGORY) %>" size="5"/><BR>
MySpaceManagerURL: <input type="text" name="msm_url" value="<%=MMC.getProperty(MMC.mySpaceManagerLoc,MMC.CATLOG)%>" size="30"/><BR>
MySpaceServerURL:  <input type="text" name="mss_url" value="<%=MMC.getProperty(MMC.serverManagerLoc,MMC.CATLOG)%>" size="30"/><BR>
MySpaceServerURLs: <input type="text" name="msss_url" value="<%=MMC.getProperty(MMC.MYSPACEMANAGERURLs,MMC.CATLOG)%>" size="30"/><BR>
<button type="submit" >Change</button>
</form>
(button is a dummy at the moment - these props are readonly....)
<!--
Version in property file is: "<%=MMC.getProperty(MMC.GENERAL_VERSION_NUMBER, MMC.GENERAL_CATEGORY) %>" - should be "1.2".<P>
MySpaceManagerURL :<%=MMC.getProperty(MMC.mySpaceManagerLoc,MMC.CATLOG)%><P>
MySpaceServerURL :<%=MMC.getProperty(MMC.serverManagerLoc,MMC.CATLOG)%><P>
MySpaceServerURLs :<%=MMC.getProperty(MMC.MYSPACEMANAGERURLs,MMC.CATLOG)%><P>
<P>-->
<P>
Back to <A href="index.html">index page</a>.
</body>
</html>

