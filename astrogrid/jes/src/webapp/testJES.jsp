<%@ page import="org.astrogrid.jes.JES,
                 java.io.PrintWriter,
                 javax.naming.Context,
                 javax.naming.spi.NamingManager,
                 javax.naming.NamingException,
                 javax.naming.InitialContext"
   session="false" %>

<html>
<head>
<title>JES Happiness Page</title>
</head>
<body bgcolor='#ffffff'>
<h1>JES Happiness Page</h1>
<h2>Try to load some properties</h2>
<%
	String jndiName = JES.getInstance().getJNDIName();
%>
The name of the properties file URL in the Naming Service is <%=jndiName%>. <P>
Attempting to locate this in the NamingService....<P>
<%
         try {
			// Obtain our environment naming context
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");   
            
            String url = (String) envCtx.lookup(jndiName);
            %> The URL is: <%=url%><P> <%
          } catch (NamingException ne) {
			%> No entry found in the JNDI naming service.<p> <% ne.printStackTrace(new PrintWriter(out)); 
          }
%>
First check the properties file has been found:
<%
try {
	JES.getInstance().checkPropertiesLoaded();
	%>OK<%
	} catch (Exception e) {
	%>Whoops - caught exception:<P><%
	e.printStackTrace(new PrintWriter(out));
	}
%>
<P>
Version in property file is: "<%=JES.getProperty(JES.GENERAL_VERSION_NUMBER, JES.GENERAL_CATEGORY) %>" - should be "1.2".


</body>
</html>

