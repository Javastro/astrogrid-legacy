<?xml version="1.0"?>
<%@page contentType="application/xml"%>
<%System.out.println("capabilities.jsp");%>

<%
  String base = (String) request.getAttribute("cea.base");
  if (base == null) {
    base = "";
  }
  String[] apps = (String[]) request.getAttribute("cea.apps");
  if (apps == null) {
    apps = new String[0];
  }
%>


<cap:capabilities
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0"
    xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0"
    xmlns:cea="http://www.ivoa.net/xml/CEA/v1.0rc1"
    xmlns:cap="urn:astrogrid:schema:Capabilities"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
        http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd
        http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd
        http://www.ivoa.net/xml/CEA/v1.0rc1 http://software.astrogrid.org/schema/vo-resource-types/CEAService/v1.0rc1/CEAService.xsd
        urn:astrogrid:schema:Capabilities <%=base%>/VOSI/Capabilities.xsd
    "
>

  <capability xsi:type="cea:CeaCapability">
    <interface xsi:type="vr:WebService">
      <accessURL use="full"><%=base%>/services/CommonExecutionConnectorService</accessURL>
    </interface>
    <managedApplications>
      <% for (int i = 0; i < apps.length; i++) { %>
	<ApplicationReference><%=apps[i]%></ApplicationReference>
      <%}%>
    </managedApplications>
  </capability>

  <capability>
	<interface xsi:type="vs:ParamHTTP">
	  <accessURL use="full"><%=base%>/VOSI/capabilities</accessURL>
	  <queryType>GET</queryType>
	  <resultType>application/xml</resultType>
	</interface>
  </capability>
  
  <capability>
	<interface xsi:type="vs:ParamHTTP">
	  <accessURL use="full"><%=base%>/VOSI/availability</accessURL>
	  <queryType>GET</queryType>
	  <resultType>application/xml</resultType>
	</interface>
  </capability>

</cap:capabilities>