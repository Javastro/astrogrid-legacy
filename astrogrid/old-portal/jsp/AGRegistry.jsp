<%@ taglib uri='/WEB-INF/templates/jsp/tld/template.tld' prefix='jetspeed' %>

<%@ page import = "org.apache.turbine.util.RunData" %> 
<%@ page import = "org.apache.turbine.util.Log" %> 

<%
    String[] jspheaders = (String[]) request.getAttribute("AGRegistry");
    String taskoutput = (String) request.getAttribute("RegOutput");
    String agpeid = (String) request.getAttribute("ag_peid");
	String agserver = (String) request.getAttribute("ag_server");
	if ( agserver == null ) agserver = "";
	String agport = (String) request.getAttribute("ag_port");
	if ( agport == null ) agport = "";
%>

<FORM METHOD="POST" ACTION="<jetspeed:dynamicUri/>">
  <INPUT TYPE="hidden" NAME="ag_peid" VALUE="<%=agpeid%>">
  Enter Web Server:
  <SELECT name="WebServerOpt" value="<%=agserver%>">
      <OPTGROUP label="Web Servers">
	  <OPTION> </OPTION>
      <OPTION> Leicester </OPTION>
      <OPTION> Rutherford </OPTION>
      </OPTGROUP>
   </SELECT>
   <INPUT name="WebServer" type = "TEXT" value="<%=agserver%>">
   Port Number: <INPUT name="port" type="TEXT" value="<%=agport%>">

  <P> Check a Registry Query Type and enter any parameters below. <BR/>
  <INPUT TYPE="radio" NAME="QueryType", VALUE="queryServices">             Query Services
  <BR/>
  <INPUT TYPE="radio" NAME="QueryType", VALUE="listAllServices">           List All Services
  <BR/>
  <INPUT TYPE="radio" NAME="QueryType", VALUE="listServiceMetadata">       List Service Metadata
  <BR/>
  <INPUT TYPE="radio" NAME="QueryType", VALUE="listRegistryMetadata">      List Registry Metadata
  <BR/>
  <INPUT TYPE="radio" NAME="QueryType", VALUE="listServiceMetadataFormat"> List Services Metadata Format
  <P>
  Element: <INPUT name="Element" type="TEXT" value="id">
  Element Value: <INPUT name="Value" type="TEXT" value="all">
  <INPUT TYPE="SUBMIT" NAME="refresh" VALUE="Submit"> 
  </P>
</FORM>
<HR/>
<%for (int i = 0; jspheaders != null && i < jspheaders.length; i++) {%>
   <%=jspheaders[i]%> </BR>
<%}%>

<P><PRE>
<%if ( taskoutput != null ) %> <%=taskoutput%>
</PRE></P>