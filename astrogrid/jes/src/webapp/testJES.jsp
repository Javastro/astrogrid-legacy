<%@ page import="org.astrogrid.jes.JES,
                 java.io.PrintWriter,
                 javax.naming.Context,
                 javax.naming.NamingException,
                 javax.naming.InitialContext"
   session="false" %>

<html>
<head>
<title>JES Happiness Page</title> 
</head>
<body bgcolor='#ffffff'>
<h1>JES Happiness Page</h1>
<h2>Locating Configuration File</h2>
<%
	String jndiName = JES.getInstance().getJNDIName();
%> 
Attempting to locate a configuration URL using JNDI under name <%=jndiName%>...<P>
<%
         try {
			// Obtain our environment naming context
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");   
            
            String url = (String) initCtx.lookup(jndiName);
            %> ...found URL: <%=url%><P> <%
          } catch (NamingException ne) {
					  jndiName = null;
			%> ...no entry found in the JNDI naming service.  If you expected one, check your $CATALINA_HOME/tomcat/conf/servers.xml file.  You need to place the following entry under the 
			&lt;context&gt; element for your webapp: <BR>
			&lt;Environment description="URL of config file" name="jesConfigFileURL" override="false" type="java.lang.String" value="&lt;Your URL&gt"/&gt;
			
			<P>
			   Attempting to locate a configuration file <%=JES.getInstance().getConfigFileName()%> on the CLASSPATH instead.<P> 
<%
          }
%>
<%
try {
	JES.getInstance().checkPropertiesLoaded();
	%>Located configuration file.<P><%
	} catch (Exception e) {
	%>Failed to locate configuration file.  Check that it's <%=jndiName==null ? "on the classpath" : "at the above URL"%>.<P>
	Exception:<P><%
	e.printStackTrace(new PrintWriter(out));
	}
%>
<P>
<form action="testJES.jsp" method="post">
Version: <input readonly type="text" name="version" value="<%=JES.getProperty(JES.GENERAL_VERSION_NUMBER, JES.GENERAL_CATEGORY) %>" size="5"/><BR>
JES ID: <input readonly type="text" name="msm_url" value="<%=JES.getProperty(JES.JES_ID,JES.JES_CATEGORY)%>" size="80"/><BR>
Monitor URL:  <input readonly type="text" name="mss_url" value="<%=JES.getProperty(JES.MONITOR_URL,JES.MONITOR_CATEGORY)%>" size="80"/><BR>
Controller URL:  <input readonly type="text" name="mss_url" value="<%=JES.getProperty(JES.CONTROLLER_URL,JES.CONTROLLER_CATEGORY)%>" size="80"/><BR>
Scheduler URL:  <input readonly type="text" name="mss_url" value="<%=JES.getProperty(JES.SCHEDULER_URL,JES.SCHEDULER_CATEGORY)%>" size="80"/><BR>

<button type="submit" >Change</button>
</form>


</body>
</html>

