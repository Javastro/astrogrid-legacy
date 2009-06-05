<?xml version="1.0"?>
<%@page contentType="application/xml"%>

<%
  String base = (String) request.getAttribute("cea.base");
  if (base == null) {
    base = "";
  }
  if(!base.endsWith("/")){
      base = base + "/";
  }
  String caps = (String)request.getAttribute("cea.caps");
%>

<?xml-stylesheet type="text/xsl" href="<%=base%>/VOSI/capabilities.xsl"?>

<cap:capabilities
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0"
    xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0"
    xmlns:cea="http://www.ivoa.net/xml/CEA/v1.0"
    xmlns:cap="urn:astrogrid:schema:Capabilities"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
        http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd
        http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd
        http://www.ivoa.net/xml/CEA/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOCEA/v1.0/VOCEA.xsd
        urn:astrogrid:schema:Capabilities <%=base%>VOSI/Capabilities.xsd
    "
>
<%=caps%>
</cap:capabilities>