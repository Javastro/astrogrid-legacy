<?xml version="1.0"?>
<%@page contentType="application/xml"%>

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

<?xml-stylesheet type="text/xsl" href="<%=base%>/VOSI/capabilities.xsl"?>

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

  <capability xsi:type="cea:CeaCapability" standardID="ivo://org.astrogrid/std/CEA/v1.0">
    <interface xsi:type="vr:WebService" version="1.0">
      <accessURL use="full"><%=base%>services/CommonExecutionConnectorService</accessURL>
    </interface>
    <!-- would be better if this were its own interface type to make UWS -->
    <interface xsi:type="vr:WebService" version="0.9"> 
      <accessURL use="full"><%=base%>uws/jobs</accessURL>
    </interface>
    
    <managedApplications>
      <% for (int i = 0; i < apps.length; i++) { %>
	<ApplicationReference><%=apps[i]%></ApplicationReference>
      <%}%>
    </managedApplications>
  </capability>

  <capability standardID="ivo://org.astrogrid/std/VOSI/v0.4#capabilities">
	<interface xsi:type="vs:ParamHTTP">
	  <accessURL use="full"><%=base%>VOSI/capabilities</accessURL>
	  <queryType>GET</queryType>
	  <resultType>application/xml</resultType>
	</interface>
  </capability>
  
  <capability standardID="ivo://org.astrogrid/std/VOSI/v0.4#availability">
	<interface xsi:type="vs:ParamHTTP">
	  <accessURL use="full"><%=base%>VOSI/availability</accessURL>
	  <queryType>GET</queryType>
	  <resultType>application/xml</resultType>
	</interface>
  </capability>
  
   <capability standardID="ivo://org.astrogrid/std/VOSI/v0.4#applications">
	<interface xsi:type="vs:ParamHTTP">
	  <accessURL use="full"><%=base%>uws/reg/app</accessURL>
	  <queryType>GET</queryType>
	  <resultType>application/xml</resultType>
	</interface>
  </capability>
  

</cap:capabilities>