<?xml version="1.0"?>
<%@page contentType="application/xml"
        import="java.net.URI"%>

<% 
String base = (String) request.getAttribute("org.astrogrid.vosi.baseurl");
String secure = (String) request.getAttribute("org.astrogrid.vosi.baseurlsecure");
%>

<?xml-stylesheet type="text/xsl" href="<%=base%>/VOSI/capabilities.xsl"?>

<cap:capabilities
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0"
    xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0"
    xmlns:cap="urn:astrogrid:schema:Capabilities"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
        http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd
        http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd
        urn:astrogrid:schema:Capabilities <%=base%>/VOSI/Capabilities.xsd
    "
>

  <capability standardID="ivo://org.astrogrid/std/Community/accounts">
    <interface xsi:type="vs:ParamHTTP" version="3" role="std">
      <accessURL use="base"><%=secure%>/accounts</accessURL>
    </interface>
  </capability>
  
  <capability standardID="ivo://org.astrogrid/std/Community/v1.0#PolicyManager">
    <interface xsi:type="vr:WebService">
      <accessURL use="full"><%=base%>/services/PolicyManager</accessURL>
    </interface>
  </capability>

  <capability standardID="ivo://org.astrogrid/std/VOSI/v0.3#capabilities">
    <interface xsi:type="vs:ParamHTTP">
      <accessURL use="full"><%=base%>/VOSI/capabilities</accessURL>
      <queryType>GET</queryType>
      <resultType>application/xml</resultType>
    </interface>
  </capability>
  
  <capability standardID="ivo://org.astrogrid/std/VOSI/v0.3#availability">
    <interface xsi:type="vs:ParamHTTP">
      <accessURL use="full"><%=base%>/VOSI/availability</accessURL>
      <queryType>GET</queryType>
      <resultType>application/xml</resultType>
    </interface>
  </capability>

</cap:capabilities>