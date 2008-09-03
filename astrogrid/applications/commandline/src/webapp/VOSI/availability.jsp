<?xml version="1.0"?>
<%@page contentType="application/xml"%>
<%
  String base = (String) request.getAttribute("cea.base");
  if (base == null) {
    base = "";
  }
  String upTime = (String) request.getAttribute("cea.uptime");
%>
<avail:availability
    xmlns:avail="http://www.ivoa.net/xml/Availability/v0.25"
    xmlns:vr="http://www.ivoa.net/xml/VOresource/v1.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
        http://www.ivoa.net/xml/VOResource/v1.0    http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd
        http://www.ivoa.net/xml/Availability/v0.4 <%=base%>/VOSI/Availability.xsd
    "
>
  <avail:available>true</avail:available>
  <avail:upSince><%=upTime%></avail:upSince>
</avail:availability>