<%@ taglib uri='/WEB-INF/templates/jsp/tld/template.tld' prefix='jetspeed' %>

<%@ page import = "org.apache.turbine.util.RunData" %> 
<%@ page import = "org.apache.turbine.util.Log" %> 

<%
    String[] jspheaders = (String[]) request.
              getAttribute("AstrogridRegistryJSP");
    String[] taskoutput = (String[]) request.
              getAttribute("AstrogridRegistryOutput");
    String jspeid = (String) request.getAttribute("js_peid");
    String message = null;
%>
Enter a registry command line with its parameters below. <BR>

For example <font color="red">QueryServices id </font>
<FORM METHOD="POST" ACTION="<jetspeed:dynamicUri/>">
  <INPUT TYPE="hidden" NAME="js_peid" VALUE="<%=jspeid%>">  
  Enter registry service command line: <input name="input" type="TEXT">
      <INPUT TYPE="SUBMIT" NAME="refresh" VALUE="Submit input">  
</FORM>
<%for (int i = 0; jspheaders != null && i < jspheaders.length; i++) {%>
   <%=jspheaders[i]%></BR>
<%}%>
<%for (int i = 0; taskoutput != null && i < taskoutput.length; i++) {
   if (taskoutput[i].startsWith("<message>")) {
      int start = taskoutput[i].indexOf("[CDATA") + 7;
      int end = taskoutput[i].indexOf("]]>");
      message = taskoutput[i].substring(start, end);
   } else {
      message = "";
   }%>
   <%=message%></BR>
<%}%>
