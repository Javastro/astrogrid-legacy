<%@ taglib uri='/WEB-INF/templates/jsp/tld/template.tld' prefix='jetspeed' %>

<%@ page import = "org.apache.turbine.util.RunData" %> 
<%@ page import = "org.apache.turbine.util.Log" %> 

<%
   try {
      String peid = (String) request.getAttribute("ag_peid");
      String[] DEFAULT_SERVICES = { "", "Leicester", "Rutherford" };
      String[] services = (String[]) request.getAttribute("services");
      if ( services == null ) services = DEFAULT_SERVICES;
      String jspheaders = (String) request.getAttribute("Registry");
      if ( jspheaders == null ) jspheaders = "";
      String taskoutput = (String) request.getAttribute("RegOutput");
      if ( taskoutput == null ) taskoutput = "";
      String server = (String) request.getAttribute("ag_server");
      if ( server == null ) server = "";
      String port = (String) request.getAttribute("ag_port");
      if ( port == null ) port = "";
      String element = (String) request.getAttribute("ag_element");
      if ( element == null ) element = "id";
      String value = (String) request.getAttribute("ag_value");
      if ( value == null ) value = "all";
      String type = (String) request.getAttribute("ag_type");
      if ( type == null ) type = "listAllServices";

      String check1 = "";
      if ( type.equals( "queryServices" ) ) check1 = "checked";
      String check2 = "";
      if ( type.equals( "listAllServices" ) ) check2 = "checked";
      String check3 = "";
      if ( type.equals( "listServiceMetadata" ) ) check3 = "checked";
      String check4 = "";
      if ( type.equals( "listRegistryMetadata" ) ) check4 = "checked";
      String check5 = "";
      if ( type.equals( "listServiceMetadataFormat" ) ) check5 = "checked";
%>

<form method="POST" action="<jetspeed:dynamicUri/>">
  <input type="hidden" name="ag_peid" value="<%=peid%>">
  <input type="hidden" name="type" value="<%=type%>">
  Enter Web Server:
  <select name="WebServerOpt" value="<%=server%>">
      <optgroup label="Web Servers">
<%    for ( int j = 0; j < services.length; j++ ) {%>
	       <option> <%=services[j]%> </option>
<%    }
%>    </optgroup>
   </select>
   <input name="WebServer" type = "TEXT" value="<%=server%>">
   Port Number: <input name="Port" type="TEXT" value="<%=port%>">

  <p> Check a Registry Query Type and enter any parameters below. <br/>
  <input type="radio" name="QueryType", value="queryServices" <%=check1%> >
     Query Services
  <br/>
  <input type="radio" name="QueryType" value="listAllServices" <%=check2%> >
     List All Services
  <br/>
  <input type="radio" NAME="QueryType" value="listServiceMetadata" <%=check3%> >
     List Service Metadata
  <br/>
  <input type="radio" NAME="QueryType" value="listRegistryMetadata" <%=check4%> >
     List Registry Metadata
  <br/>
  <input type="radio" NAME="QueryType" value="listServiceMetadataFormat" <%=check5%> >
     List Services Metadata Format
  <P>
  Element: <input name="Element" type="TEXT" value="<%=element%>">
  Element Value: <input name="Value" type="TEXT" value="<%=value%>">
  <input type="SUBMIT" name="refresh" value="Submit"> 
  </P>
</form>
<hr/>
<%if ( jspheaders != null ) %> <%=jspheaders%> 
<br/>
<%if ( taskoutput != null ) %> <%=taskoutput%>
<%  }
    catch (Exception e) {
      Log.error(e);
      return;
    }
%>
