<%@page contentType="text/html" import="java.io.*, java.net.URL"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.astrogrid.component.descriptor.ComponentDescriptor"%>
<head>
<title>Current configuration of CEC</title>
<%@ include file="../inc/header.jsp" %>

<%
//naughty direct use of the application context - lazy should go into mvc
ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
%>    
<div id='bodyColumn'>
<h1><%=((ComponentDescriptor)ctx.getBean("config")).getName() %></h1>
<pre style="font-size: 120%;">
<%=((ComponentDescriptor)ctx.getBean("config")).getDescription() %>
</pre>
<h1>Execution Policy</h1>
<pre style="font-size: 120%;">
<%=((ComponentDescriptor)ctx.getBean("ExecutionPolicy")).getDescription() %>
</pre>
</div>
<%@ include file="../inc/footer.jsp" %>
