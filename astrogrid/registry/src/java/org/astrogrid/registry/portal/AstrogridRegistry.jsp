<%@ taglib uri='/WEB-INF/templates/jsp/tld/template.tld' prefix='jetspeed' %>

<%@ page import = "org.apache.turbine.util.RunData" %> 
<%@ page import = "org.apache.turbine.util.Log" %> 

<%
   try {
      String peid = (String) request.getAttribute("ag_peid");
      String[] DEFAULT_SERVICES = { "",
                                    "MSSL",
                                    "Leicester",
                                    "Rutherford" };
      String[] services = (String[]) request.getAttribute("agr_services");
      if ( services == null ) services = DEFAULT_SERVICES;
      String jspheaders = (String) request.getAttribute("RegistryHeader");
      if ( jspheaders == null ) jspheaders = "";
      String taskoutput = (String) request.getAttribute("RegistryOutput");
      if ( taskoutput == null ) taskoutput = "";
      String server = (String) request.getAttribute("agr_Server");
      if ( server == null ) server = "";
      String port = (String) request.getAttribute("agr_Port");
      if ( port == null ) port = "";

      String element1 = (String) request.getAttribute("agr_element1");
      if ( element1 == null ) element1 = "id";
      String operator1 = (String) request.getAttribute("agr_operator1");
      if ( operator1 == null ) operator1 = "EQ";
      String value1 = (String) request.getAttribute("agr_value1");
      if ( value1 == null ) value1 = "";
      String seperator1 = (String) request.getAttribute("agr_seperator1");
      if ( seperator1 == null ) seperator1 = "";
      String element2 = (String) request.getAttribute("agr_element2");
      if ( element2 == null ) element2 = "";
      String operator2 = (String) request.getAttribute("agr_operator2");
      if ( operator2 == null ) operator2 = "";
      String value2 = (String) request.getAttribute("agr_value2");
      if ( value2 == null ) value2 = "";

%>

<form method="POST" action="<jetspeed:dynamicUri/>">
  <input type="hidden" name="ag_peid" value="<%=peid%>">
  Enter Web Server:
  <select name="WebServerOpt" value="<%=server%>">
      <optgroup label="Web Servers">
<%    for ( int j = 0; j < services.length; j++ ) {%>
	       <option> <%=services[j]%> </option>
<%    }
%>    </optgroup>
   </select>
   <input name="WebServer" type = "TEXT" value="<%=server%>"/>
   Port Number: <input name="Port" type="TEXT" value="<%=port%>"/>

  <p> Type a Registry Query in the boxes below (one or two conditions):
  </p>
  <p> 
  <input name="Element1" type="TEXT" value="<%=element1%>"/> 
  <select name="Operator1" value="EQ">
     <optgroup>
        <option>EQ</option>
        <option>NE</option>
        <option>GT</option>
        <option>GE</option>
        <option>LT</option>
        <option>LE</option>
        <option>STARTS WITH</option>
        <option>CONTAINS</option>
     </optgroup>
  </select>
  <input name="Value1" type="TEXT" value="<%=value1%>"/>
  <br/>
  <select name="Seperator1" value="">
     <optgroup>
        <option></option>
        <option>AND</option>
        <option>OR</option>
        <option>NOT</option>
     </optgroup>
  </select>
  <br/> 
  <input name="Element2" type="TEXT" value="<%=element2%>"/> 
  <select name="Operator2" value="">
     <optgroup>
        <option></option>
        <option>EQ</option>
        <option>NE</option>
        <option>GT</option>
        <option>GE</option>
        <option>LT</option>
        <option>LE</option>
        <option>STARTS WITH</option>
        <option>CONTAINS</option>
     </optgroup>
  </select>
  <input name="Value2" type="TEXT" value=""/>
  </p>
  <%
//  For future more complex Queries
//  <p> Complex Query: <input name="Query" type="TEXT" value="" size="80"/>
//  <br/>
//  <input type="SUBMIT" name="refresh" value="Submit"/> 
//  </p>
  %>
  <p> <input type="SUBMIT" name="refresh" value="Submit"/> </p>
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
