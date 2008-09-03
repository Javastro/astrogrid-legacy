<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:set var="base" value="${requestContext.requestUri}/"></c:set>
<%
String viewbase= request.getRequestURI();
%>
<?xml-stylesheet type="text/xsl" href="<%=viewbase%>/../applist.xsl"?>
<apps base="${base}">
  <c:forEach items="${apps}" var="theApp">
    <app id="${theApp.id}" name="${theApp.name}" ref="${theApp.shortref}">
        <c:forEach items="${theApp.ifaces}" var="theIface">
        <iface><c:out value="${theIface}"/></iface>
        </c:forEach>
    </app>
  </c:forEach>
</apps>