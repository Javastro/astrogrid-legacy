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
<%if(request.getParameter("AUTORUN") != null){ %>
<meta http-equiv="refresh" content="2" />
<%} %>
<title>UWS Job Detail for <c:out value="${JobId}"/></title>
</head>
<body>
<h1>Job Detail for <c:out value="${JobId}"/></h1>
<c:set var="base" value="${requestContext.requestUri}"></c:set>
<table>
<tbody>
<tr><td>Phase</td><td><a href="${base}/phase"><c:out value="${theJob.phase}"/></a> 
				<c:choose>
					<c:when test='${theJob.phase eq "PENDING"}'>
				    <form action="${base}/phase" method="post">
						<input type="hidden" name="phase"  value="RUN" />
						<input type="submit" value="RUN" />
                    </form>
					</c:when>

					<c:when test='${theJob.phase eq "EXECUTING"}'>
				     <form action="${base}/phase" method="post">
						<input type="hidden" name="phase" value="ABORT" />
						<input type="submit" value="ABORT" />
                     </form>
					</c:when>
					
					<c:otherwise>
				     <form action="${base}" method="post">
						<input type="hidden" name="ACTION" value="DELETE" />
						<input type="submit" value="DELETE" />
                     </form>
					</c:otherwise>
					
				</c:choose> 

</td></tr>
<tr><td>Start</td><td><c:out value="${theJob.startTime}"/></td></tr>
<tr><td>End</td><td><c:out value="${theJob.endTime}"/></td></tr>
<tr><td>ExecutionDuration</td><td><a href="${base}/executionDuration"><c:out value="${theJob.executionDuration}"/></a></td></tr>
<tr><td>Destruction</td><td><a href="${base}/destruction"><c:out value="${theJob.destruction}"/></a></td></tr>
<tr><td colspan="2"><b> PA details</b></td></tr>
<tr><td>Application</td><td><c:out value="${theJob.applicationName}"/></td></tr>

</tbody>
</table>

<p> Inputs....</p>
<table>
<tbody>
     <c:forEach items="${theJob.inputList.parameter}" var="theInput">
        <tr>
           <td>
              <c:out value="${theInput.id}"/>
           </td>
           <td><c:out value="${theInput.value}"/></td>
        </tr>
      </c:forEach>

</tbody>
</table>

<c:if test='${theJob.phase eq "ERROR"}'>
<p><a href="${base}/error">Error message</a></p>
</c:if>
<p><a href="${base}/results">Results</a>


</body>
</html>
