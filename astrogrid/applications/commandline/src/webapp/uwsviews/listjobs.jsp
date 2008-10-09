<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>UWS Jobs</title>
<jsp:include page="../inc/header.jsp" flush="true" />
<!-- body -->
<div id="bodyColumn">

<div class="contentBox">
<c:set var="base" value="${requestContext.requestUri}"></c:set>

<h1>UWS Jobs</h1>
<table border="1">
	<thead>
		<tr>
			<th>Job name</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${jobs}" var="theJob">
			<tr>
				<td><a href="${base}/${theJob.jobId}/"><c:out
					value="${theJob.jobId}"
				/></a></td>
				<td><c:out value="${theJob.phase}" /></td>
				<td>
				<c:choose>
					<c:when test='${theJob.phase eq "PENDING"}'>
				    <form action="${base}/${theJob.jobId}/phase" method="post">
						<input type="hidden" name="phase" value="RUN" />
						<input type="submit" value="RUN" />
                    </form>
					</c:when>

					<c:when test='${theJob.phase eq "EXECUTING"}'>
				     <form action="${base}/${theJob.jobId}/phase" method="post">
						<input type="hidden" name="phase" value="ABORT" />
						<input type="submit" value="ABORT" />
                     </form>
					</c:when>
					
					<c:otherwise>
				     <form action="${base}/${theJob.jobId}" method="post">
						<input type="hidden" name="ACTION" value="DELETE" />
						<input type="submit" value="DELETE" />
                     </form>
					</c:otherwise>
					
				</c:choose> 
				
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</div>
<jsp:include page="../inc/footer.jsp" flush="true" />