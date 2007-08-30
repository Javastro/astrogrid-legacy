<?xml version="1.0"?>
<%@page contentType="application/xml" 
        import="org.astrogrid.common.j2ee.environment.*"%>
<jsp:useBean class="org.astrogrid.common.j2ee.environment.Environment" 
    id="environment" scope="application"/>
<Context path="<%=environment.getContextPath()%>">
<%
EnvEntry[] entries = environment.getEnvEntry();
for (int i = 0; i < entries.length; i++) {
  EnvEntry e = entries[i];
%>
  <Environment
      description="<%=e.getDescription()%>"
      name="<%=e.getName()%>"
      override="false"
      type="<%=e.getType()%>"
      value="<%=e.getReplacementValue()%>"
    />
<% } %>
</Context>
