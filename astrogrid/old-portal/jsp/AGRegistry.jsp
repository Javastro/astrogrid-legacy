<%@ taglib uri='/WEB-INF/templates/jsp/tld/template.tld' prefix='jetspeed' %>

<%@ page import = "org.apache.turbine.util.RunData" %> 
<%@ page import = "org.apache.turbine.util.Log" %> 

<%
    String[] jspheaders = (String[]) request.getAttribute("AGRegistry");
    String taskoutput = (String) request.getAttribute("RegOutput");
    String agpeid = (String) request.getAttribute("ag_peid");
    String message = null;
%>

<FORM METHOD="POST" ACTION="<jetspeed:dynamicUri/>">
  <INPUT TYPE="hidden" NAME="ag_peid" VALUE="<%=agpeid%>">
  Enter Web Server:
  <SELECT name="WebServerOpt">
      <OPTGROUP label="Web Servers">
	  <OPTION label="other"> Specify</OPTION>
          <OPTION label="stargrid1"> stargrid1</OPTION>
	  <OPTION label="rlspc14">   rlspc14</OPTION>
      </OPTGROUP>
   </SELECT>
   <INPUT name="WebServer" type = "TEXT" >
   Port Number: <INPUT name="port" type="TEXT" value="8080">
  <P> Check a Registry Query Type and enter any parameters below. <BR/>
  <INPUT TYPE="radio" NAME="QueryType", VALUE="queryServices">             Query Services
  <BR/>
  <INPUT TYPE="radio" NAME="QueryType", VALUE="listAllServices" checked>   List All Services
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
   <%=jspheaders[i]%></BR>
<%}%>

<P><PRE>
<%  StringBuffer cs = new StringBuffer();
    for ( int i = 0; taskoutput != null && i < taskoutput.length(); i++ ) {
       char c = taskoutput.charAt(i);
       if ( c == '<' ) cs = cs.append("&lt;");
       else if ( c == '>' ) cs = cs.append("&gt;");
       else cs = cs.append(c);
   }
   String str = cs.toString();
   if ( str != null ) %> <%=str%>
</PRE></P>