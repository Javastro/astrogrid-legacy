<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Iterator,
                org.astrogrid.applications.Application,
                org.astrogrid.applications.component.CEAComponentContainer
               "%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="refresh" content="2;url=./queue.jsp" />
        <title>Queue of CEA jobs</title>
<%@ include file="../inc/header.jsp" %>
<div id='bodyColumn'>

    <h2>Queue of CEA jobs</h2>
    <p><%= CEAComponentContainer.getInstance().getExecutionController().toString() %></p>
    <table>
      <thead>
        <tr>
          <th>Job name</th>
          <th>Application name</th>
          <th>State</th>
          <th>Start</th>
          <th>End</th>
          <th>Deadline</th>
          <th/>
         </tr> 
      </thead>
      <tbody>
        <%
        List qjl = CEAComponentContainer.getInstance().getExecutionController().getQueue();
        Iterator i = qjl.iterator();
        while (i.hasNext()) {
	      Application app = (Application)i.next();
	%>
	<tr>
          <td><a href="../uws/jobs/<%=app.getId()%>"><%=app.getId()%></a></td>
          <td><%=app.getApplicationDescription().getName()%></td>
	      <td><%=app.getStatus()%></td>
          <td><%=app.getStartInstant()%></td>
          <td><%=app.getEndInstant()%></td>
          <td><%=app.getDeadline()%></td>
          <td>
            <form action="../uws/jobs/<%=app.getId()%>/phase" method="post">
              <input type="hidden" name="phase" value="abort"/>
              <input type="submit" value="Abort"/>
            </form>
          </td>
        </tr>
	<%}%>
      </tbody>
    </table>
</div>
<%@ include file="../inc/footer.jsp" %>