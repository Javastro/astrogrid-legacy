<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>UWS CEA Results for <c:out value="${JobId}"/> </title>
</head>
<body>
<c:set var="base" value="${requestContext.requestUri}"></c:set>

<p>Results for <c:out value="${JobId}"/></p>
   <table border="1" >
      <thead>
        <tr>
          <th>name</th>
          <th>type</th>
          <th>description</th>
          <th>mime</th>
         </tr> 		
      </thead>
      <tbody>
      <c:forEach items="${results}" var="theResult">
        <tr>
           <td>
              <a href="${base}/${theResult.id}"><c:out value="${theResult.id}"/></a>
           </td>
           <td>
             <c:out value="${theResult.kind}"/>
           </td>
           <td>
              <c:out value="${theResult.description}"/>
           </td>
           <td>
             <c:out value="${theResult.mime}"/>
           </td>
        </tr>
      </c:forEach>
      </tbody>
      </table>


</body>
</html>
